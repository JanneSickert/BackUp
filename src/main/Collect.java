package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import test.Check_IO;

abstract class Collect {

	protected String[] relPath;
	protected ArrayList<String> srcPath = new ArrayList<String>();
	
	public abstract void doWithFile(File f);
	
	public void collectFiles(File from) throws IOException {
		File[] fileList = from.listFiles();
		if (fileList != null) {
			for (File file : fileList) {
				if (file.isDirectory()) {
					collectFiles(file);
				} else {
					doWithFile(file);
				}
			}
		}
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
	
	protected void writeRelPath() {
		File f = new File(Main.getPathListPath());
		if (!(f.exists())) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
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
		} else {
			try {
				FileWriter fwf = new FileWriter(f, true);
				fwf.write("\n");
				for (int i = 0; i < relPath.length - 1; i++) {
					fwf.write(relPath[i] + "\n");
				}
				fwf.write(relPath[relPath.length - 1]);
				fwf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void makeRelPath() {
		for (int i = 0; i < relPath.length; i++) {
			relPath[i] = getRelPath(srcPath.get(i), Main.getRootSourcePath());
		}
	}
}