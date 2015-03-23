package tbIncubator.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Flushable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import tbIncubator.domain.DataType;
import tbIncubator.domain.Interaction;
import tbIncubator.domain.InteractionParameter;
import tbIncubator.domain.Representative;
import tbIncubator.domain.TbElement;

public abstract class JavaCodeGenerator<//
DATATYPE extends JavaCodeGenerator.FlushToDir, //
INTERACTION extends JavaCodeGenerator.FlushToDir, //
TEST extends JavaCodeGenerator.FlushToDir> {

	public interface FlushToDir {
		String getName();

		void flush(File dir) throws IOException;
	}

	private Map<String, Representative> index;
	private Map<String, DataType> typeIndex;
	private Map<String, Interaction> interIndex;
	private Map<String, InteractionParameter> paraIndex;
	private List<DataType> datatypes;
	private List<Interaction> interactions;
	private Properties prop;

	protected JavaCodeGenerator(List<DataType> datatypes,
			List<Interaction> interactions) {
		this.datatypes = Collections.unmodifiableList(datatypes);
		this.interactions = Collections.unmodifiableList(interactions);
		prop = new Properties();
		try {
			prop.load(new FileReader("mapping.properties"));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void ensureIndexBuild() {
		if (index == null || typeIndex == null) {
			index = new HashMap<String, Representative>();
			typeIndex = new HashMap<String, DataType>();
			for (DataType dataType : datatypes) {
				typeIndex.put(dataType.pk, dataType);
				for (Representative rep : dataType.representatives) {
					index.put(rep.pk, rep);
				}
			}
			index = Collections.unmodifiableMap(index);
			typeIndex = Collections.unmodifiableMap(typeIndex);
		}
		if (interIndex == null || paraIndex == null) {
			interIndex = new HashMap<String, Interaction>();
			paraIndex = new HashMap<String,InteractionParameter>();
			for(Interaction inter : interactions) {
				interIndex.put(inter.pk,inter);
				for (InteractionParameter para : inter.parameters) {
					paraIndex.put(para.pk,para);
				}
			}
			interIndex = Collections.unmodifiableMap(interIndex);
			paraIndex = Collections.unmodifiableMap(paraIndex);
		}
	}

	protected Representative lookupRepresentative(String pk) {
		ensureIndexBuild();
		return index.get(pk);
	}

	protected DataType lookupDataType(String pk) {
		ensureIndexBuild();
		return typeIndex.get(pk);
	}

	protected Interaction lookupInteraction(String pk) {
		ensureIndexBuild();
		return interIndex.get(pk);
	}
	
	protected InteractionParameter lookupInteractionParameter(String pk) {
		ensureIndexBuild();
		return paraIndex.get(pk);
	}
	
	public List<DataType> getDatatypes() {
		return datatypes;
	}

	public List<Interaction> getInteractions() {
		return interactions;
	}

	protected String getFQClass(final String pack) {
		String property;
		String actPack = pack;
		do {
			property = prop.getProperty(actPack);
			int indexOf = actPack.lastIndexOf('.');
			if (indexOf > 0) {
				actPack = actPack.substring(0, indexOf);
			} else if (property == null) {
				throw new RuntimeException("Nichts gefunden für " + pack);
			}
		} while (property == null);

		return property;
	}

	protected String shortenPackage(TbElement dataType, String jName) {
		if (jName.startsWith(dataType.getPackage())) {
			jName = jName.substring(dataType.getPackage().length() + 1);
		}
		return jName;
	}

	public final void generate(File dir) throws IOException {
		// List<DATATYPE> dataTypes = new ArrayList<DATATYPE>(getDatatypes()
		// .size());
		for (DataType dataType : getDatatypes()) {
			generateDataType(dataType).flush(dir);
		}

		HashSet<FlushToDir> interactionInterfaces = new HashSet<FlushToDir>();
		for (Interaction interaktion : getInteractions()) {
			interactionInterfaces.add(generateInteraction(interaktion));
		}
		for (FlushToDir flushToDir : interactionInterfaces) {
			flushToDir.flush(dir);
		}
	}

	protected FlushToDir noFlushableResult(String name) {
		return new FlushToDir() {
			@Override
			public void flush(File dir) throws IOException {
			}

			@Override
			public String getName() {
				return name;
			}
		};
	}

	protected abstract DATATYPE generateDataType(DataType dataType);

	protected abstract INTERACTION generateInteraction(Interaction interaction);

	protected abstract TEST generateTest(Object test);

}
