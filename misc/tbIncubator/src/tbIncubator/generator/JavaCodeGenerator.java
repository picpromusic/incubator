package tbIncubator.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import tbIncubator.domain.DataType;
import tbIncubator.domain.HasParameters;
import tbIncubator.domain.Interaction;
import tbIncubator.domain.InteractionParameter;
import tbIncubator.domain.Representative;
import tbIncubator.domain.TbElement;
import tbIncubator.domain.TestSatz;

public abstract class JavaCodeGenerator {

	public interface FlushToDir {
		String getName();

		void flush(File dir) throws IOException;
	}

	private Map<String, Representative> index;
	private Map<String, DataType> typeIndex;
	private Map<String, Interaction> interIndex;
	private Map<String, InteractionParameter> paraIndex;
	private final List<DataType> datatypes;
	private final List<Interaction> interactions;
	private final List<TestSatz> testsatz;
	private final Properties prop;
	private Set<String> typeNames;

	protected JavaCodeGenerator(List<DataType> datatypes,
			List<Interaction> interactions, List<TestSatz> testsatz) {
		this.datatypes = Collections.unmodifiableList(datatypes);
		this.interactions = Collections.unmodifiableList(interactions);
		this.testsatz = Collections.unmodifiableList(testsatz);
		prop = new Properties();
		try {
			prop.load(new FileReader("mapping.properties"));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void ensureIndexesAreBuild() {
		boolean indexesBuild = false;
		if (typeNames == null) {
			indexesBuild = true;
			typeNames = new HashSet<String>();
		}
		if (index == null || typeIndex == null) {
			indexesBuild = true;
			index = new HashMap<String, Representative>();
			typeIndex = new HashMap<String, DataType>();
			for (DataType dataType : datatypes) {
				typeNames.add(getFqClassName(dataType));
				typeIndex.put(dataType.pk, dataType);
				for (Representative rep : dataType.getRepresentatives()) {
					index.put(rep.pk, rep);
				}
			}
			index = Collections.unmodifiableMap(index);
			typeIndex = Collections.unmodifiableMap(typeIndex);
		}
		if (interIndex == null || paraIndex == null) {
			indexesBuild = true;
			interIndex = new HashMap<String, Interaction>();
			paraIndex = new HashMap<String, InteractionParameter>();
			for (Interaction inter : interactions) {
				typeNames.add(getFQClassName(inter, true));
				typeNames.add(getFQClassName(inter, true) + "Impl");
				interIndex.put(inter.pk, inter);
				indexParameter(inter);
			}
			
			for (TestSatz ts: testsatz) {
				indexParameter(ts);
			}
			
			interIndex = Collections.unmodifiableMap(interIndex);
			paraIndex = Collections.unmodifiableMap(paraIndex);
		}
	}


	private void indexParameter(HasParameters inter) {
		for (InteractionParameter para : inter.getParameters()) {
			paraIndex.put(para.pk, para);
		}
	}

	protected String getFqClassName(DataType dataType) {
		return simpleCombinedName(dataType);
	}

	private String simpleCombinedName(TbElement ele) {
		return ele.getPackage() + "." + ele.getSimpleName();
	}

	protected String getFQClassName(Interaction inter, boolean noUnderscore) {
		return getFQClass(simpleCombinedName(inter), noUnderscore);
	}

	protected String getFQClassName(Interaction inter) {
		return getFQClass(simpleCombinedName(inter), false);
	}

	protected String getFQClassName(TestSatz testsatz) {
		return simpleCombinedName(testsatz);
	}

	private String getFQClass(final String pack, boolean noUnderscore) {
		String property;
		String actPack = pack;
		do {
			property = prop.getProperty(actPack);
			int indexOf = actPack.lastIndexOf('.');
			if (indexOf > 0 && property == null) {
				actPack = actPack.substring(0, indexOf);
			} else if (property == null) {
				throw new RuntimeException("Nichts gefunden für " + pack);
			}
		} while (property == null);

		if (property.endsWith(".*")) {
			String rest = pack.substring(actPack.length() + 1);
			int lastIndexOf = Math.max(0, rest.lastIndexOf('.'));
			rest = rest.substring(0, lastIndexOf);
			property = property.substring(0, property.length() - 1) + rest;
			property = TbElement.replaceAll(property);
		}

		boolean again = true;
		while (again && !noUnderscore) {
			again = false;
			for (String tn : typeNames) {
				if (property.startsWith(tn) && property.length() > tn.length()) {
					// + 1 ist super , weil wir ein zeichen nach dem . landen
					// wollen und wenn es keinen punkt gibt sind wir bei 0 das
					// auch super ist
					int lastIndexOf = tn.lastIndexOf('.') + 1;
					property = tn.substring(0, lastIndexOf) + "_"
							+ tn.substring(lastIndexOf)
							+ property.substring(tn.length());
					again = true;
				}
			}
		}
		;
		return property;
	}

	protected Representative lookupRepresentative(String pk) {
		ensureIndexesAreBuild();
		return index.get(pk);
	}

	protected DataType lookupDataType(String pk) {
		ensureIndexesAreBuild();
		return typeIndex.get(pk);
	}

	protected Interaction lookupInteraction(String pk) {
		ensureIndexesAreBuild();
		return interIndex.get(pk);
	}

	protected InteractionParameter lookupInteractionParameter(String pk) {
		ensureIndexesAreBuild();
		return paraIndex.get(pk);
	}

	public List<DataType> getDatatypes() {
		return datatypes;
	}

	public List<Interaction> getInteractions() {
		return interactions;
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

		for (TestSatz testsatz : getTestsatz()) {
			generateTest(testsatz).flush(dir);
		}

	}

	public List<TestSatz> getTestsatz() {
		return testsatz;
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

	protected abstract FlushToDir generateDataType(DataType dataType);

	protected abstract FlushToDir generateInteraction(Interaction interaction);

	protected abstract FlushToDir generateTest(TestSatz test);

}
