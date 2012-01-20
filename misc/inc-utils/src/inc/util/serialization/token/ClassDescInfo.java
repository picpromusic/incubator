package inc.util.serialization.token;

import java.util.List;

public class ClassDescInfo extends DefaultPositionMarkers {

  final static byte SC_WRITE_METHOD = 0x01; // if SC_SERIALIZABLE
  final static byte SC_BLOCK_DATA = 0x08; // if SC_EXTERNALIZABLE
  final static byte SC_SERIALIZABLE = 0x02;
  final static byte SC_EXTERNALIZABLE = 0x04;
  final static byte SC_ENUM = 0x10;
  private ClassDesc self;
	
  public ClassDescInfo(ClassDesc self) {
	this.self = self;
  }
  
  private byte flags = (byte) 0xff;

  private List<FieldDesc> fields;
  private ClassAnnotation classAnnotations;
  private ClassDesc superClass;

  public void setFlags(byte flags) {
    this.flags = flags;
  }

  public byte getFlags() {
    return flags;
  }

  public void setFields(List<FieldDesc> fields) {
    this.fields = fields;
  }

  public void setClassAnnotation(ClassAnnotation classAnnotations) {
    this.classAnnotations = classAnnotations;
  }

  public void setSuperClass(ClassDesc classDesc) {
    this.superClass = classDesc;
  }

  private void checkFlags() {
      if (flags == 0xff) {
  	  throw new RuntimeException("Flags not initialized");
        }
  }

  public boolean isSerializable() {
      checkFlags();
    return (flags & SC_SERIALIZABLE) != 0;
  }


  public boolean hasWriteMethod() {
      checkFlags();
    return (flags & SC_WRITE_METHOD) != 0;
  }

  public boolean isExternalizable() {
      checkFlags();
    return (flags & SC_EXTERNALIZABLE) != 0;
  }

  public boolean hasBlockData() {
      checkFlags();
    return (flags & SC_BLOCK_DATA) != 0;
  }

  public List<FieldDesc> getFields() {
    return this.fields;
  }

  public ClassDesc getSuperClass() {
    return superClass;
  }

  @Override
    public String toString() {
	return super.toString() + " " + self.toString();
    }
}
