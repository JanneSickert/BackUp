package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import test.Check_IO;

public class NewBackUp extends Collect{

	ArrayList<String> srcPath = new ArrayList<String>();
	
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
	}
}
