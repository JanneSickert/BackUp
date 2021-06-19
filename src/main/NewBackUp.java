package main;

import java.io.File;
import java.io.IOException;

public abstract class NewBackUp extends Collect{
	
	@Override
	public void doWithFile(File f) {
			srcPath.add(f.getAbsolutePath());
	}
	
	public NewBackUp() {
		try {
			collectFiles(new File(Main.getRootSourcePath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.relPath = new String[srcPath.size()];
		makeRelPath();
		writeRelPath();
		moveFiles();
		MyDate.BackUpTime.createTimeFile(Main.getTimeFilePath());
	}

	private void moveFiles() {
		for (int i = 0; i < srcPath.size(); i++) {
			copyFileToDirectory(new File(srcPath.get(i)), new File(Main.getDataPath() + "/" + i));
		}
	}
	
	protected abstract void copyFileToDirectory(File from, File to);
}
