package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import test.Check_IO;

public abstract class NewBackUp extends Collect{

	private ArrayList<String> srcPath = new ArrayList<String>();
	private String[] relPath;
	
	@Override
	public void doWithFile(File f) {
			srcPath.add(f.getAbsolutePath());
	}
	
	@Check_IO(rightOutput = "aa/bb.txt",
			input = {
					"C:/backUp/aa/bb.txt",
					"C:/backUp"
			})
	public String getRelPath(String path, String root) {
		char[] arr = new char[path.length() - root.length() - 1];
		int index = 0;
		for (int i = root.length() + 1; i < path.length(); i++) {
			arr[index] = path.charAt(i);
			index++;
		}
		return (new String(arr));
	}
	
	public NewBackUp() {
		try {
			collectFiles(new File(Main.getRootSourcePath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		relPath = new String[srcPath.size()];
		makeRelPath();
		writeRelPath();
		moveFiles();
	}

	private void moveFiles() {
		for (int i = 0; i < srcPath.size(); i++) {
			copyFileToDirectory(new File(srcPath.get(i)), new File(Main.getDataPath() + "/" + i));
		}
	}
	
	protected abstract void copyFileToDirectory(File from, File to);
	
	private void writeRelPath() {
		File f = new File(Main.getPathListPath());
		if (!(f.exists())) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			FileWriter fwf = new FileWriter(f, true);
			for (int i = 0; i < relPath.length - 1; i++) {
				fwf.write(relPath[i] + "\n");
			}
			fwf.write(relPath[relPath.length - 1]);
			fwf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void makeRelPath() {
		for (int i = 0; i < relPath.length; i++) {
			relPath[i] = getRelPath(srcPath.get(i), Main.getRootSourcePath());
		}
	}
}
