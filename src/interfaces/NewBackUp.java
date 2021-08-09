package interfaces;

import java.io.File;
import java.io.IOException;
import comment.Comment;
import main.Main;

public interface NewBackUp extends Collect{
	
	@Comment(make = "First collects all paths of the folder, "
			+ "then it converts them into relative paths and finally it "
			+ "writes them to a file. The files are then processed.",
			param = {"is only passed"})
	default public void newBackUp(Move moveMethod) {
		initVars();
		try {
			main.Main.userInterface.showLoadingScreen("Missing files are determined...");
			collectFiles(new File(Main.getRootSourcePath()), main.Main.newBackUp);
			main.Main.userInterface.closeLoadingScreen();
		} catch (IOException e) {
			e.printStackTrace();
		}
		main.Storage.Collect.relPath = new String[main.Storage.Collect.srcPath.size()];
		makeRelPath();
		writeRelPath();
		moveFiles(moveMethod);
	}

	@Comment(make = "This is the actual copying or encryption process",
			param = "Can contain two methods: the one to encrypt or the one to copy.")
	private void moveFiles(Move moveMethod) {
		for (int i = 0; i < main.Storage.Collect.srcPath.size(); i++) {
			moveMethod.move(new File(main.Storage.Collect.srcPath.get(i)), new File(Main.getDataPath() + "/" + i));
		}
	}
}
