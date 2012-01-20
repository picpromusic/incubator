package inc.util.serialization.token;

 
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class SerializationTokenizer {

    final static short STREAM_MAGIC = (short) 0xaced;
    final static short STREAM_VERSION = 5;
    private int depth;
    private int nextHandle;
    private List<Content> contents;
    private DataInputStream in;
    private ParsedWrapper<Short> magic;
    private ParsedWrapper<Short> version;
    private SortedMap<Integer, Object> handles;
    private PositionTrackerStream positionTracker;
    private TokenTracker tt;

    public SerializationTokenizer(InputStream in) throws IOException {
	this.nextHandle = 0x7E0000;
	this.depth = 0;
	this.positionTracker = new PositionTrackerStream(in);
	this.in = new DataInputStream(positionTracker);
	this.handles = new TreeMap<>();
	this.tt = new TokenTracker(positionTracker);
    }

    public short getMagic() {
	return magic.getContent();
    }

    public short getVersion() {
	return version.getContent();
    }

    public void tokenize() throws IOException {
	List<Content> cList = new LinkedList<>();
	try {
	    this.magic = readShort();
	    tt.addMagicToken(magic);
	    this.version = readShort();
	    tt.addVersionToken(version);
	    contents(cList);
	} catch (EOFException e) {
	    e.printStackTrace();
	} finally {
	    this.contents = Collections.unmodifiableList(cList);
	    in.close();
	}
    }

    public List<Content> getContents() {
	return contents;
    }

    private void contents(List<Content> list) throws IOException {
	while (true) {
	    byte type;
	    try {
		type = readType();
	    } catch (EOFException e) {
		return;
	    }
	    list.add(content(type));
	}
    }

    private void print(String string) {
	for (int i = 0; i < depth; i++) {
	    System.out.print('\t');
	}
	System.out.println(string);
    }

    private Content content(byte type) throws IOException {
	tt.addTypeToken(type);
	Content ret = new Content();
	ret.setStartPos(positionTracker.getPos()-1);
	print("Content");
	depth++;
	switch (type) {
	case TypeToken.TC_OBJECT:
	case TypeToken.TC_CLASS:
	case TypeToken.TC_ARRAY:
	case TypeToken.TC_STRING:
	case TypeToken.TC_LONGSTRING:
	case TypeToken.TC_ENUM:
	case TypeToken.TC_CLASSDESC:
	case TypeToken.TC_PROXYCLASSDESC:
	case TypeToken.TC_REFERENCE:
	case TypeToken.TC_NULL:
	case TypeToken.TC_EXCEPTION:
	case TypeToken.TC_RESET: // ????????
	    ret.setObject(object(type));
	    break;
	case TypeToken.TC_BLOCKDATA:
	    System.out.println("NYI");
	    break;
	case TypeToken.TC_BLOCKDATALONG:
	    System.out.println("NYI");
	    break;
	default:
	    throw new RuntimeException(Integer.toHexString(type & 0xff));
	}
	ret.setEndPos(positionTracker.getPos());
	return ret;
    }


    private ContentObject object(byte type) throws IOException {
	ContentObject co = null;
	long startPos = positionTracker.getPos() - 1;

	if (type == TypeToken.TC_BLOCKDATA || type == TypeToken.TC_ENDBLOCKDATA) {
	    while (type != TypeToken.TC_ENDBLOCKDATA) {
		type = read();
	    }
	    type = readType();
	}

	switch (type) {
	case TypeToken.TC_OBJECT:
	    print("newObject");
	    NewContentObject nco = new NewContentObject();
	    co = nco;
	    nco.setClassDesc(classdesc());
	    newHandle(co);
	    nco.setClassData(classData(nco.getClassDesc()));
	    break;
	case TypeToken.TC_CLASS:
	    print("newClass");
	    NewClass nc = new NewClass();
	    co = nc;
	    nc.setClassDesc(classdesc());
	    newHandle(co); // CHECK
	    break;
	case TypeToken.TC_ARRAY:
	    // TC_ARRAY classDesc newHandle (int)<size> values[size]
	    NewArray na = new NewArray();
	    co = na;
	    na.setClassDesc(classdesc());
	    newHandle(co);
	    int size = readInt();
	    for (int i = 0; i < size; i++) {
		byte dontKnow = readType();
		tt.addTypeToken(dontKnow);
		na.add(object(dontKnow));
	    }
	    break;
	case TypeToken.TC_STRING:
	    NewString ns = new NewString();
	    co = ns;
	    newHandle(co);
	    ns.setString(utf8());
	    break;
	case TypeToken.TC_LONGSTRING:
	    ns = new NewString();
	    co = ns;
	    newHandle(co);
	    ns.setString(utf8());
	    break;
	case TypeToken.TC_ENUM:
	case TypeToken.TC_CLASSDESC:
	case TypeToken.TC_PROXYCLASSDESC:
	case TypeToken.TC_REFERENCE:
	    co = new ContentObjectReference(
		    (DefaultPositionMarkers) handleReference());
	    break;
	case TypeToken.TC_NULL:
	    co = new ContentObjectReference(null);
	    break;
	case TypeToken.TC_EXCEPTION:
	case TypeToken.TC_RESET:
	    break;
	}

	if (co == null) {
	    throw new RuntimeException("unbehandelter Typ:" + type);
	}
	co.setStartPos(startPos);
	co.setEndPos(positionTracker.getPos());
	return co;
    }

    private List<ClassData> classData(ClassDesc classDesc) throws IOException {
	ClassDescInfo classDescInfo = null;
	LinkedList<ClassDesc> typeHierarchie = new LinkedList<>();
	ArrayList<ClassData> cd = new ArrayList<>(16);
	do {
	    typeHierarchie.push(classDesc);
	    classDescInfo = (classDesc.isReference() ? classDesc.getReference()
		    .getClassDescInfo() : classDesc.getClassDescInfo());
	    classDesc = classDescInfo.getSuperClass();
	} while (classDesc.getClassDescInfo() != null
		|| classDesc.getReference() != null);

	while (!typeHierarchie.isEmpty()) {
	    for (ClassDesc it : typeHierarchie) {
		ClassDescInfo itInfo = it.isReference() ? it.getReference()
			    .getClassDescInfo() : it.getClassDescInfo();
		System.out.println(it+":"+itInfo.getFlags());
	    }
	    
	    classDesc = typeHierarchie.pop();
	    classDescInfo = (classDesc.isReference() ? classDesc.getReference()
		    .getClassDescInfo() : classDesc.getClassDescInfo());
	    if (classDescInfo.isSerializable()) {
		if (!classDescInfo.hasWriteMethod()) {
		    cd.addAll(nowrClass(classDescInfo));
		    // nowrclass // SC_SERIALIZABLE & classDescFlag &&
		    // !(SC_WRITE_METHOD & classDescFlags)
		} else {
		    cd.addAll(wrClass(classDescInfo));
		    // wrclass objectAnnotation // SC_SERIALIZABLE &
		    // classDescFlag &&
		    // SC_WRITE_METHOD & classDescFlags
		}
	    } else if (classDescInfo.isExternalizable()) {
		if (!classDescInfo.hasBlockData()) {
		    // externalContents // SC_EXTERNALIZABLE & classDescFlag &&
		    // !(SC_BLOCKDATA & classDescFlags
		} else {
		    // objectAnnotation // SC_EXTERNALIZABLE & classDescFlag&&
		    // SC_BLOCKDATA & classDescFlags
		}
	    } else {
		throw new RuntimeException();
	    }
	    classDesc = classDescInfo.getSuperClass();
	} 

	return cd;
    }

    private List<ClassData> wrClass(ClassDescInfo classDescInfo)
	    throws IOException {
	List<ClassData> cd = nowrClass(classDescInfo);
	ClassData classData = new ClassData();
	classData.setClassRef(classDescInfo);
	classData.setData(objectAnnotation());
	return cd;
    }

    private byte[] objectAnnotation() throws IOException {
	ByteArrayOutputStream bout = new ByteArrayOutputStream();
	try {
	    long start = positionTracker.getPos();
	    byte data = read();
	    while (data != TypeToken.TC_ENDBLOCKDATA) {
		bout.write(data);
		data = read();
	    }
	    return bout.toByteArray();
	} finally {
	}
    }

    private List<ClassData> nowrClass(ClassDescInfo classDescInfo)
	    throws IOException {
	List<FieldDesc> fields = classDescInfo.getFields();
	ArrayList<ClassData> cd = new ArrayList<>(fields.size());
	for (FieldDesc fieldDesc : fields) {
	    char ftype = fieldDesc.getType();
	    if (ftype == 'L' || ftype == '[') {
		continue;
	    }
	    System.out.println(fieldDesc);
	    ClassData c = new ClassData();
	    c.setFieldRef(fieldDesc);
	    c.setClassRef(classDescInfo);
	    switch (ftype) {
	    case 'B':
		throw new RuntimeException("NYI");
	    case 'C':
		throw new RuntimeException("NYI");
	    case 'D':
		throw new RuntimeException("NYI");
	    case 'F':
		throw new RuntimeException("NYI");
	    case 'I':
		c.setData(readInt());
		break;
	    case 'J':
		throw new RuntimeException("NYI");
	    case 'S':
		throw new RuntimeException("NYI");
	    case 'Z':
		throw new RuntimeException("NYI");
	    }
	    cd.add(c);
	}
	for (FieldDesc fieldDesc : fields) {
	    char ftype = fieldDesc.getType();
	    if (ftype != 'L' && ftype != '[') {
		continue;
	    }
	    System.out.println(fieldDesc);
	    ClassData c = new ClassData();
	    c.setFieldRef(fieldDesc);
	    c.setClassRef(classDescInfo);
	    switch (ftype) {
	    case 'L':
	    case '[':
		byte otype = readType();
		tt.addTypeToken(otype);
		c.setData(object(otype));
	    }
	    cd.add(c);

	}
	return cd;
    }

    private ClassDesc classdesc() throws IOException {
	ClassDesc cd = new ClassDesc();
	cd.setStartPos(positionTracker.getPos());
	// classDesc:
	// newClassDesc
	// *TC_CLASSDESC className serialVersionUID newHandle classDescInfo
	// *TC_PROXYCLASSDESC newHandle proxyClassDescInfo
	// nullReference
	// (ClassDesc)prevObject // an object required to be of type
	// // ClassDesc
	byte type = readType();
	tt.addTypeToken(type);
	switch (type) {
	case TypeToken.TC_NULL:
	    // print("TC_NULL");
	    break;
	case TypeToken.TC_REFERENCE:
	    cd.setReference((ClassDesc) handleReference());
	    break;
	case TypeToken.TC_CLASSDESC:
	    // print("TC_CLASSDESC");
	    cd.setClassName(utf8());
	    cd.setSerialVersionUid(readLong());
	    newHandle(cd);
	    cd.setClassDescInfo(classDescInfo(cd));
	    break;
	case TypeToken.TC_PROXYCLASSDESC:
	    // print("TC_PROXYCLASSDESC");
	    break;
	default:
	    print("ERROR");
	    throw new RuntimeException();
	}
	cd.setEndPos(positionTracker.getPos());
	return cd;
    }

    private void newHandle(ReferenceableParsedObject o) {
	int i = nextHandle++;
	print("newHandle: " + Integer.toHexString(i));
	tt.addNewHandleToken(i);
	this.handles.put(i, o);
    }


    private ClassDescInfo classDescInfo(ClassDesc self) throws IOException {
	// classDescInfo:
	// classDescFlags fields classAnnotation superClassDesc
	ClassDescInfo cdi = new ClassDescInfo(self);
	cdi.setStartPos(positionTracker.getPos());
	byte flags = read();
	cdi.setFlags(flags);
	cdi.setFields(fields());
	cdi.setClassAnnotation(classAnnotations());
	cdi.setSuperClass(superClassDesc());
	cdi.setEndPos(positionTracker.getPos());
	return cdi;
    }

    private ClassDesc superClassDesc() throws IOException {
	return classdesc();
    }

    private ClassAnnotation classAnnotations() throws IOException {
	ClassAnnotation ca = new ClassAnnotation();
	ca.setStartPos(positionTracker.getPos());
	try {
	    long start = positionTracker.getPos();
	    byte endBlockdata = readType();
	    if (endBlockdata == TypeToken.TC_ENDBLOCKDATA)
		return ca;
	    List<Content> cList = new LinkedList<>();
	    contents(cList);
	    return ca;
	} finally {
	    ca.setEndPos(positionTracker.getPos());
	}
    }

    private byte readType() throws IOException {
	byte read = read();
	printType(read);
	return read;
    }

    private List<FieldDesc> fields() throws IOException {
	// fields:
	// (short)<count> fieldDesc[count]
	long startPos = positionTracker.getPos();
	int count = readUnsignedShort();
	List<FieldDesc> fields = new ArrayList<>(count);
	for (short i = 0; i < count; i++) {
	    fields.add(fieldDesc());
	}
	return new ListWithPositionWrapper<FieldDesc>(fields, startPos,
		positionTracker.getPos());
    }

    private FieldDesc fieldDesc() throws IOException {
	// fieldDesc:
	// primitiveDesc
	// objectDesc
	FieldDesc fieldDesc = new FieldDesc();
	fieldDesc.setStartPos(positionTracker.getPos());
	char type = (char) in.readByte();
	fieldDesc.setType(type);
	print("Type: " + type);
	switch (type) {
	case 'L':
	case '[':
	    fieldDesc.setFieldName(utf8());
	    byte literal = readType();
	    tt.addTypeToken(literal);
	    switch (literal) {
	    case TypeToken.TC_STRING:
		fieldDesc.setClassName(object(literal));
		// int newHandle = newHandle(); // CHECK
		// putHandle(newHandle, utf8);
		// UTF8 fieldName = utf8();
		// fieldDesc.setClassName(fieldName);
		break;
	    case TypeToken.TC_REFERENCE:
		Object hobject = handleReference();
		print("Handle Object: " + hobject);
		fieldDesc.setClassName((ContentObject) hobject);
		break;
	    }

	    break;
	case 'B':
	case 'C':
	case 'D':
	case 'F':
	case 'I':
	case 'J':
	case 'S':
	case 'Z':
	    fieldDesc.setFieldName(utf8());
	    break;
	}
	fieldDesc.setEndPos(positionTracker.getPos());
	return fieldDesc;
    }

    private Object handleReference() throws IOException {
	int handle = readInt();
	print("Handle: " + Integer.toHexString(handle));
	tt.addReferenceToken(handle);
	Object hobject = getHandle(handle);
	return hobject;
    }

    private UTF8 utf8() throws IOException {
	UTF8 utf8 = new UTF8();
	utf8.setStartPos(positionTracker.getPos());
	int length = readUnsignedShort();
	print("UTF8-Length: " + length);
	return readUtf(utf8, length);
    }

    private UTF8 readUtf(UTF8 utf8, int length) throws IOException,
	    UnsupportedEncodingException {
	byte[] buffer = new byte[length];
	readBytes(buffer);
	String content = new String(buffer, "UTF-8");
	print("UTF: " + content);
	utf8.setEndPos(positionTracker.getPos());
	utf8.setLength(length);
	utf8.setContent(content);
	return utf8;
    }

    private UTF8 longUtf8() throws IOException {
	UTF8 utf8 = new UTF8();
	utf8.setStartPos(positionTracker.getPos());
	ParsedWrapper<Long> length = readLong();
	print("UTF8-Length: " + length.getContent());
	return readUtf(utf8, (int) (long) length.getContent());
    }

    private void readBytes(byte[] buffer) throws IOException {
	int pos = 0;
	while (pos < buffer.length) {
	    int r = in.read(buffer, 0, buffer.length - pos);
	    if (r <= 0) {
		throw new EOFException();
	    }
	    pos += r;
	}
    }

    private void printType(byte type) {
	switch (type) {
	case TypeToken.TC_NULL:
	    print("TC_NULL");
	    break;
	case TypeToken.TC_REFERENCE:
	    print("TC_REFERENCE");
	    break;
	case TypeToken.TC_CLASSDESC:
	    print("TC_CLASSDESC");
	    break;
	case TypeToken.TC_OBJECT:
	    print("TC_OBJECT");
	    break;
	case TypeToken.TC_STRING:
	    print("TC_STRING");
	    break;
	case TypeToken.TC_ARRAY:
	    print("TC_ARRAY");
	    break;
	case TypeToken.TC_CLASS:
	    print("TC_CLASS");
	    break;
	case TypeToken.TC_BLOCKDATA:
	    print("TC_BLOCKDATA");
	    break;
	case TypeToken.TC_ENDBLOCKDATA:
	    print("TC_ENDBLOCKDATA");
	    break;
	case TypeToken.TC_RESET:
	    print("TC_RESET");
	    break;
	case TypeToken.TC_BLOCKDATALONG:
	    print("TC_BLOCKDATALONG");
	    break;
	case TypeToken.TC_EXCEPTION:
	    print("TC_EXCEPTION");
	    break;
	case TypeToken.TC_LONGSTRING:
	    print("TC_LONGSTRING");
	    break;
	case TypeToken.TC_PROXYCLASSDESC:
	    print("TC_PROXYCLASSDESC");
	    break;
	case TypeToken.TC_ENUM:
	    print("TC_ENUM");
	    break;
	}
    }

    // stream:
    // magic version contents
    //
    // contents:
    // content
    // contents content
    //
    // content:
    // object
    // blockdata
    //
    // object:
    // newObject
    // newClass
    // newArray
    // newString
    // newEnum
    // newClassDesc
    // prevObject
    // nullReference
    // exception
    // TC_RESET
    //
    // newClass:
    // TC_CLASS classDesc newHandle
    //
    //
    // superClassDesc:
    // classDesc
    //
    //
    //
    // className:
    // (utf)
    //
    // serialVersionUID:
    // (long)
    //
    // classDescFlags:
    // (byte) // Defined in Terminal Symbols and
    // // Constants
    //
    // proxyClassDescInfo:
    // (int)<count> proxyInterfaceName[count] classAnnotation
    // superClassDesc
    //
    // proxyInterfaceName:
    //
    // (utf)
    //
    // fields:
    // (short)<count> fieldDesc[count]
    //
    // fieldDesc:
    // primitiveDesc
    // objectDesc
    //
    // primitiveDesc:
    // prim_typecode fieldName
    //
    // objectDesc:
    // obj_typecode fieldName className1
    //
    // fieldName:
    // (utf)
    //
    // className1:
    // (String)object // String containing the field's type,
    // // in field descriptor format
    //
    // classAnnotation:
    // endBlockData
    // contents endBlockData // contents written by annotateClass
    //
    // prim_typecode:
    // `B' // byte
    // `C' // char
    // `D' // double
    // `F' // float
    // `I' // integer
    // `J' // long
    // `S' // short
    // `Z' // boolean
    //
    // obj_typecode:
    // `[` // array
    // `L' // object
    //
    // newArray:
    // TC_ARRAY classDesc newHandle (int)<size> values[size]
    //
    // newObject:
    // TC_OBJECT classDesc newHandle classdata[] // data for each class
    //
    // classdata:
    // nowrclass // SC_SERIALIZABLE & classDescFlag &&
    // // !(SC_WRITE_METHOD & classDescFlags)
    // wrclass objectAnnotation // SC_SERIALIZABLE & classDescFlag &&
    // // SC_WRITE_METHOD & classDescFlags
    // externalContents // SC_EXTERNALIZABLE & classDescFlag &&
    // // !(SC_BLOCKDATA & classDescFlags
    // objectAnnotation // SC_EXTERNALIZABLE & classDescFlag&&
    // // SC_BLOCKDATA & classDescFlags
    //
    // nowrclass:
    // values // fields in order of class descriptor
    //
    // wrclass:
    // nowrclass
    //
    // objectAnnotation:
    // endBlockData
    // contents endBlockData // contents written by writeObject
    // // or writeExternal PROTOCOL_VERSION_2.
    //
    // blockdata:
    // blockdatashort
    // blockdatalong
    //
    // blockdatashort:
    // TC_BLOCKDATA (unsigned byte)<size> (byte)[size]
    //
    // blockdatalong:
    // TC_BLOCKDATALONG (int)<size> (byte)[size]
    //
    // endBlockData :
    // TC_ENDBLOCKDATA
    //
    // externalContent: // Only parseable by readExternal
    // ( bytes) // primitive data
    // object
    //
    // externalContents: // externalContent written by
    // externalContent // writeExternal in PROTOCOL_VERSION_1.
    // externalContents externalContent
    //
    // newString:
    // TC_STRING newHandle (utf)
    // TC_LONGSTRING newHandle (long-utf)
    //
    // newEnum:
    // TC_ENUM classDesc newHandle enumConstantName
    //
    // enumConstantName:
    // (String)object
    //
    // prevObject
    // TC_REFERENCE (int)handle
    //
    // nullReference
    // TC_NULL
    //
    // exception:
    // TC_EXCEPTION reset (Throwable)object reset
    //
    // magic:
    // STREAM_MAGIC
    //
    // version
    // STREAM_VERSION
    //
    // values: // The size and types are described by the
    // // classDesc for the current object
    //
    // newHandle: // The next number in sequence is assigned
    // // to the object being serialized or deserialized
    //
    // reset: // The set of known objects is discarded
    // // so the objects of the exception do not
    // // overlap with the previously sent objects
    // // or with objects that may be sent after
    // // the exception

    private ParsedWrapper<Long> readLong() throws IOException {
	ParsedWrapper<Long> pl = new ParsedWrapper<>();
	pl.setStartPos(positionTracker.getPos());
	pl.setContent(in.readLong());
	pl.setEndPos(positionTracker.getPos());
	return pl;
    }

    private int readInt() throws IOException {
	return in.readInt();
    }

    private int readUnsignedShort() throws IOException {
	return in.readShort() & 0xffff;
    }

    private ParsedWrapper readShort() throws IOException {
	ParsedWrapper ps = new ParsedWrapper();
	ps.setStartPos(positionTracker.getPos());
	ps.setEndPos(positionTracker.getPos());
	ps.setContent(in.readShort());
	return ps;
    }

    private byte read() throws IOException {
	int i = in.read();
	if (i < 0) {
	    throw new EOFException();
	}
	return (byte) (i & 0xff);
    }

    private Object getHandle(int handle) {
	return this.handles.get(handle);
    }

    public List<ParsedToken> getTokens() {
	return tt.getTokens();
    }

    @Override
    public String toString() {
	return "0x" + Long.toHexString(positionTracker.getPos());
    }
}
