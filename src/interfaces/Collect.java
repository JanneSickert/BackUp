package interfaces;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import test.Check_IO;
import comment.Comment;

public interface Collect {
	
	@Comment(make = "Collects all absolute paths of the files to be processed",
			param = {
					"the root folder",
					"The method the stores the paths"
			})
	default public void collectFiles(File from, NewOrUpdate method) throws IOException {
		File[] fileList = from.listFiles();
		if (fileList != null) {
			for (File file : fileList) {
				if (file.isDirectory()) {
					collectFiles(file, method);
				} else {
					method.doWithFile(file);
				}
			}
		}
	}
	
	@Check_IO(rightOutput = "aa/bb.txt",
			input = {
					"C:/backUp/aa/bb.txt",
					"C:/backUp"
			})
	default public String getRelPath(String path, String root) {
		char[] arr = new char[path.length() - root.length() - 1];
		int index = 0;
		for (int i = root.length() + 1; i < path.length(); i++) {
			arr[index] = path.charAt(i);
			index++;
		}
		return (new String(arr));
	}
	
	default public void initVars() {
		main.Storage.Collect.srcPath = new ArrayList<String>();
	}
	
	@Comment(make = "write main.Storage.Collect.relPath to the path file")
	default public void writeRelPath() {
		File f = new File(main.Main.getPathListPath());
		if (!(f.exists())) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				FileWriter fwf = new FileWriter(f, true);
				for (int i = 0; i < main.Storage.Collect.relPath.length - 1; i++) {
					fwf.write(main.Storage.Collect.relPath[i] + "\n");
				}
				fwf.write(main.Storage.Collect.relPath[main.Storage.Collect.relPath.length - 1]);
				fwf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				FileWriter fwf = new FileWriter(f, true);
				fwf.write("\n");
				for (int i = 0; i < main.Storage.Collect.relPath.length - 1; i++) {
					fwf.write(main.Storage.Collect.relPath[i] + "\n");
				}
				fwf.write(main.Storage.Collect.relPath[main.Storage.Collect.relPath.length - 1]);
				fwf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	default public void makeRelPath() {
		for (int i = 0; i < main.Storage.Collect.relPath.length; i++) {
			main.Storage.Collect.relPath[i] = getRelPath(main.Storage.Collect.srcPath.get(i), main.Main.getRootSourcePath());
		}
	}
}