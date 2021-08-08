package interfaces;

import java.io.File;
import java.io.IOException;

import main.Main;

public interface NewBackUp extends Collect{
	
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

	private void moveFiles(Move moveMethod) {
		for (int i = 0; i < main.Storage.Collect.srcPath.size(); i++) {
			moveMethod.move(new File(main.Storage.Collect.srcPath.get(i)), new File(Main.getDataPath() + "/" + i));
		}
	}
}
