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
			if (fileList.length == 0) {
				method.doWithEmptyFolder(from);
			} else {
				for (File file : fileList) {
					if (file.isDirectory()) {
						collectFiles(file, method);
					} else {
						method.doWithFile(file);
					}
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
		main.Storage.Collect.absolutEmptyFolderSourcePath = new ArrayList<String>();
	}
	
	@Comment(make = "write main.Storage.Collect.relPath to the path file",
			param = {"append the paths if true", ""})
	default public void writeRelPath(boolean append, boolean updateMode) {
		File f = new File(main.Main.getPathListPath());
		File emptyFolder = new File(main.Main.getEmptyFolderPathList());
		if (!(f.exists())) {
			try {
				f.createNewFile();
				emptyFolder.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				FileWriter fwf = new FileWriter(f, append);// for files
				for (int i = 0; i < main.Storage.Collect.relPath.length - 1; i++) {
					fwf.write(main.Storage.Collect.relPath[i] + "\n");
				}
				if (!updateMode) {
					fwf.write(main.Storage.Collect.relPath[main.Storage.Collect.relPath.length - 1]);
				}
				fwf.close();
				if (main.Storage.Collect.relEmptyFolderPath != null) {
					int len_ef = main.Storage.Collect.relEmptyFolderPath.length;
					if (len_ef > 0) {
						FileWriter efw = new FileWriter(emptyFolder, append);// for empty folders
						for (int i = 0; i < main.Storage.Collect.relEmptyFolderPath.length - 1; i++) {
							efw.write(main.Storage.Collect.relEmptyFolderPath[i] + "\n");
						}
						efw.write(main.Storage.Collect.relEmptyFolderPath[len_ef - 1]);
						efw.close();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				if (main.Storage.Collect.relPath.length != 0) {
					FileWriter fwf = new FileWriter(f, append);
					fwf.write("\n");
					for (int i = 0; i < main.Storage.Collect.relPath.length - 1; i++) {
						fwf.write(main.Storage.Collect.relPath[i] + "\n");
					}
					fwf.write(main.Storage.Collect.relPath[main.Storage.Collect.relPath.length - 1]);
					fwf.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Comment(param = {"true if this is a new backup."})
	default public void makeRelPath(boolean newBackUp) {
		for (int i = 0; i < main.Storage.Collect.relPath.length; i++) {// for files
			main.Storage.Collect.relPath[i] = getRelPath(main.Storage.Collect.srcPath.get(i), main.Main.getRootSourcePath());
		}
		if (newBackUp) {
			for (int i = 0; i < main.Storage.Collect.relEmptyFolderPath.length; i++) {// for folders
				main.Storage.Collect.relEmptyFolderPath[i] = getRelPath(main.Storage.Collect.absolutEmptyFolderSourcePath.get(i), main.Main.getRootSourcePath());
			}
		}
	}
}