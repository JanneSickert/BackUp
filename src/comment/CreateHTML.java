package comment;

public class CreateHTML {

	public static void main(String[] args) {
		final String FOLDER_OF_THE_HTML_FILE = "C:/Users/janne";
		Create.Project project = new Create.Project("BackUp", FOLDER_OF_THE_HTML_FILE);
		Create.Klasse<interfaces.Collect> c0 = new Create.Klasse<>(interfaces.Collect.class);
		Create.Klasse<interfaces.NewBackUp> c1 = new Create.Klasse<>(interfaces.NewBackUp.class);
		Create.Klasse<interfaces.Update> c2 = new Create.Klasse<>(interfaces.Update.class);
		project.classNames = new String[3];
		project.classNames[0] = c0.name;
		project.classNames[1] = c1.name;
		project.classNames[2] = c2.name;
		project.list.add(c0.getAnnotationOfClass());
		project.list.add(c1.getAnnotationOfClass());
		project.list.add(c2.getAnnotationOfClass());
		project.execute();
	}
}