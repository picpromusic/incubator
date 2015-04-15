package tbIncubator.generator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import tbIncubator.domain.DataType;
import tbIncubator.domain.Interaction;
import tbIncubator.domain.InteractionParameter;
import tbIncubator.domain.Link;
import tbIncubator.domain.Representative;
import tbIncubator.domain.TestSatz;

public class JavaCodeGeneratorWithStringBuilders extends JavaCodeGenerator {

	public static class FileContent {

		private LinkedList<StringBuilder> lines = new LinkedList<StringBuilder>();

		public Iterable<? extends CharSequence> getLines() {
			return Collections.unmodifiableCollection(lines);
		}

		public void println(String line) {
			print(line);
			newLine();
		}

		public void println(int index, CharSequence line) {
			lines.add(diffIndex(index), new StringBuilder(line));
		}

		private int diffIndex(int index) {
			return index >= 0 ? index : lines.size() - 1 + index;
		}

		private void ensureLine() {
			if (lines.isEmpty()) {
				newLine();
			}
		}

		private void ensureLine(int index) {
			if (lines.size() < index) {
				lines.add(new StringBuilder());
			}
		}

		private void newLine() {
			lines.add(new StringBuilder());
		}

		public void print(String text) {
			ensureLine();
			lines.getLast().append(text);
		}

		public void print(int index, CharSequence text) {
			ensureLine(diffIndex(index));
			lines.get(diffIndex(index)).append(text);
		}

	}

	private class MyFlushToDir implements FlushToDir {

		private final String fqClassName;
		private final FileContent fileContent;
		private boolean flushed;

		public MyFlushToDir(FileContent fileContent, String fqClassName) {
			this.fileContent = fileContent;
			this.fqClassName = fqClassName;
			flushed = false;
		}

		@Override
		public void flush(File dir) throws IOException {
			if (!flushed) {
				File file = new File(dir, fqClassName.replaceAll("\\.", "/")
						+ ".java");
				file.getParentFile().mkdirs();
				PrintWriter pw = new PrintWriter(file);
				for (CharSequence charSequence : fileContent.getLines()) {
					pw.println(charSequence);
				}
				pw.close();
				flushed = true;
			}
		}

		@Override
		public String getName() {
			return fqClassName;
		}
	}

	private Map<String, MyFlushToDir> interactionFiles;

	public JavaCodeGeneratorWithStringBuilders(List<DataType> datatypes,
			List<Interaction> interactions, List<TestSatz> testsaetze) {
		super(datatypes, interactions,testsaetze);
		this.interactionFiles = new HashMap<String, MyFlushToDir>();
	}

	@Override
	protected FlushToDir generateDataType(DataType dataType) {
		FileContent pw = new FileContent();
		String javaName = dataType.toJavaName();
		MyFlushToDir mfd = new MyFlushToDir(pw, javaName);
		if (javaName.startsWith("Neues")) {
			pw.println("package " + dataType.getPackage() + ";");

			pw.println("public enum " + dataType.getSimpleName() + "{");
			System.out.println(dataType.getSimpleName());

			for (Representative rep : dataType.getRepresentatives()) {
				pw.print("\t" + rep.toJavaName());
				StringBuilder sb = new StringBuilder();
				for (Link link : rep.representativeLinks) {
					Representative representative = lookupRepresentative(link.ref);
					String jName = representative.getDefinedIn().toJavaName();
					jName = shortenPackage(dataType, jName);
					sb.append(jName);
					sb.append(".");
					sb.append(representative.toJavaName());
					sb.append(",");
				}
				if (sb.length() > 0) {
					sb.setLength(sb.length() - 1);
					pw.print("(");
					pw.print(sb.toString());
					pw.print(")");
				} else {
					pw.print("(\"");
					pw.print(rep.name);
					pw.print("\")");
				}
				pw.println(",//");
			}
			pw.println(";");

			StringBuilder ctorDef = new StringBuilder();
			ctorDef.append("\t");
			ctorDef.append(dataType.getSimpleName());
			ctorDef.append("(");
			StringBuilder ctorImpl = new StringBuilder();
			ctorImpl.append("\t{\n");
			for (Link link : dataType.fieldLinks) {
				String name = link.name.substring(0, 1).toLowerCase()
						+ link.name.substring(1);

				String jName = lookupDataType(link.ref).toJavaName();
				jName = shortenPackage(dataType, jName);
				pw.println("\tpublic final " + jName + " " + name + ";");
				ctorDef.append(jName);
				ctorDef.append(" ");
				ctorDef.append(name);
				ctorDef.append(",");
				ctorImpl.append("\t\t");
				ctorImpl.append("this.");
				ctorImpl.append(name);
				ctorImpl.append("=");
				ctorImpl.append(name);
				ctorImpl.append(";\n");
			}
			if (dataType.fieldLinks.isEmpty()) {
				pw.println("\tpublic final String textRepresentation;");
				ctorDef.append("String textRepresentation,");
				ctorImpl.append("\t\tthis.textRepresentation = textRepresentation;\n");
			}

			pw.println("\n");

			ctorDef.setLength(ctorDef.length() - 1);
			ctorDef.append(")");
			ctorImpl.append("\t}");

			pw.println(ctorDef.toString());
			pw.println(ctorImpl.toString());

			pw.println("}");
			return mfd;
		} else {
			return noFlushableResult(javaName);
		}
	}

	@Override
	protected FlushToDir generateInteraction(Interaction inter) {
		String fqClassName = getFQClassName(inter);
		String fqPackage = fqClassName.substring(0,
				fqClassName.lastIndexOf('.'));
		String className = fqClassName.substring(fqPackage.length() + 1);

		MyFlushToDir mfd = interactionFiles.get(fqClassName);
		FileContent fc = null;
		if (mfd != null) {
			fc = mfd.fileContent;
		} else {
			mfd = new MyFlushToDir(new FileContent(), fqClassName);
			interactionFiles.put(fqClassName, mfd);

			fc = mfd.fileContent;
			fc.println("package " + fqPackage + ";");
			fc.println("public interface " + className + "{");
			fc.println("}");
		}

		fc.println(-1, "public void " + inter.getSimpleName() + "(");

		StringBuilder sb = new StringBuilder();

		for (InteractionParameter parameter : inter.getParameters()) {
			DataType dataType = lookupDataType(parameter.dataTypeRef.ref);
			fc.println(
					1,
					"import " + dataType.getPackage() + "."
							+ dataType.getSimpleName() + ";");

			sb.append(dataType.getSimpleName() + " " + parameter.getJavaName()
					+ ",");
		}
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 1);
			fc.print(-2, sb.toString());
		}
		fc.print(-2, ");");
		return mfd;
	}

	@Override
	protected FlushToDir generateTest(TestSatz test) {
		return noFlushableResult("not implemented yet");
	}

}
