package comment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import annotationen.ClassComment;
import annotationen.Comment;

@ClassComment(
		comment = "Contain classes to create the -documentation html file."
				+ "This classes are not a part of the Project.",
		author = "Janne",
		version = {3, 0, 0})
class Create extends ui.CommandLineFunctions {

	public static class Record {
		String make;
		String retu;
		String name;
		String para;
	}

	public static class Klasse<T> {

		private Class<T> Typ;
		private ArrayList<Create.Record> record = new ArrayList<Create.Record>();
		public String name;

		public Klasse(Class<T> Typ) {
			this.Typ = Typ;
			this.name = Typ.getName();
			getData();
		}

		private void getData() {
			Method[] methoden = Typ.getDeclaredMethods();
			for (Method m : methoden) {
				Create.Record local = new Create.Record();
				local.name = m.getName();
				Comment me = m.getAnnotation(Comment.class);
				if (!(me == null)) {
					local.make = me.make();
					local.retu = me.ret();
					String params = "";
					String[] arr = me.param();
					for (String s : arr) {
						params = params + " | " + s;
					}
					local.para = params;
				} else {
					String def = "no meta data";
					local.make = def;
					local.para = def;
					local.retu = def;
				}
				record.add(local);
			}
		}

		public ArrayList<Create.Record> getAnnotationOfClass() {
			return this.record;
		}
	}

	public static class Project {

		private final String PATH;
		private final String NAME;
		public ArrayList<ArrayList<Create.Record>> list = new ArrayList<ArrayList<Create.Record>>();
		public String[] classNames;

		public Project(String name, String path) {
			this.NAME = name;
			this.PATH = path;
		}

		public void execute() {
			String code = "<!DOCTYPE HTML>\n" + "<html>\n" + "<head>\n" + "<title>" + NAME + "</title>" + "\n"
					+ "</head>\n"
					+ "<body text=\"#000000\" bgcolor=\"#FFFFFF\" link=\"#FF0000\" alink=\"#FF0000\" vlink=\"#FF0000\">\n"
					+ tabels() + "\n" + "</body>\n" + "</html>";
			paste(code);
		}

		private void paste(String code) {
			String localPath = PATH + "/" + NAME + "-documentation" + ".html";
			p(localPath);
			File f = new File(localPath);
			try {
				f.createNewFile();
			} catch (IOException ex) {
				Logger.getLogger(Create.class.getName()).log(Level.SEVERE, null, ex);
				p("ERROR: path not found: " + f.getAbsolutePath());
			}
			p("File created at " + localPath);
			NC nc = new NC();
			nc.sid(localPath, code, false);
		}

		private class NC {

			private void help(String path, String text, boolean newLine, boolean bbbb) {
				File ff = new File(path);
				if (ff.exists()) {
					try {
						FileWriter fwf = new FileWriter(ff, bbbb);
						if (newLine) {
							fwf.write(System.getProperty("line.separator"));
						}
						fwf.write(text);
						fwf.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					p("ERROR: Datei wurde nicht gefunden:" + path);
				}
			}

			public void sid(String path, String text, boolean newLine) {
				help(path, text, newLine, false);
				p("finish");
			}
		}

		private String tabels() {
			String result = "<h1>" + "Projectname: " + NAME + "</h1>";
			for (int x = 0; x < list.size(); x++) {
				result = result + "<h2>" + classNames[x] + "</h2><br>\n"
						+ "<table width=\"100%\" border=\"1\" cellpadding=\"0\" cellspacing=\"2\">\n";
				result = result + "<tr>\n" + "<td>" + "<b>name</b>" + "</td>" + "\n" + "<td>" + "<b>return value</b>"
						+ "</td>" + "\n" + "<td>" + "<b>action</b>" + "</td>" + "\n" + "<td>" + "<b>parameter</b>"
						+ "</td>" + "\n" + "</tr>";
				for (int y = 0; y < list.get(x).size(); y++) {
					result = result + "<tr>\n" + "<td>" + list.get(x).get(y).name + "</td>" + "\n" + "<td>"
							+ list.get(x).get(y).retu + "</td>" + "\n" + "<td>" + list.get(x).get(y).make + "</td>"
							+ "\n" + "<td>" + list.get(x).get(y).para + "</td>" + "\n" + "</tr>";
				}
				result = result + "\n" + "</table>";
			}
			return result;
		}
	}
}