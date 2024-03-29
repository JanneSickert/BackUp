package comment;

import annotationen.EntryPoint;

public class CreateHTML {

	@EntryPoint(
			keys = {"DOKU", "-d", "--doku"},
			describtion = "Create a project dokumentation in HTML.",
			isMainClass = false
			)
	public static void doku() {
		final String FOLDER_OF_THE_HTML_FILE = "C:/Users/janne/OneDrive/Desktop/Projekte/BackUp";
		Create.Project project = new Create.Project("BackUp", FOLDER_OF_THE_HTML_FILE);
		Create.Klasse<interfaces.Collect> c0 = new Create.Klasse<>(interfaces.Collect.class);
		Create.Klasse<interfaces.NewBackUp> c1 = new Create.Klasse<>(interfaces.NewBackUp.class);
		Create.Klasse<interfaces.Update> c2 = new Create.Klasse<>(interfaces.Update.class);
		Create.Klasse<main.Main.NotFoundFiles> c3 = new Create.Klasse<>(main.Main.NotFoundFiles.class);
		Create.Klasse<main.SettingEncryption> c4 = new Create.Klasse<>(main.SettingEncryption.class);
		project.classNames = new String[5];
		project.classNames[0] = c0.name;
		project.classNames[1] = c1.name;
		project.classNames[2] = c2.name;
		project.classNames[3] = c3.name;
		project.classNames[4] = c4.name;
		project.list.add(c0.getAnnotationOfClass());
		project.list.add(c1.getAnnotationOfClass());
		project.list.add(c2.getAnnotationOfClass());
		project.list.add(c3.getAnnotationOfClass());
		project.list.add(c4.getAnnotationOfClass());
		project.execute();
	}
}