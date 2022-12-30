package interfaces;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import main.Main;
import main.MyDate;
import comment.Comment;
import enums.SettingType;

public interface Update extends Collect, PathList, Access {

	default public void update(Move moveMethod) {
		initVars();
		initUpdateVars();
		moveMissingFiles(moveMethod);
		try {
			moveMissingFolders();
		} catch (IOException e) {
			e.printStackTrace();
		}
		updateChangedFiles(moveMethod);
	}
	
	default public void initUpdateVars() {
		main.Storage.Update.absolutSourcePath = new ArrayList<String>();
		main.Storage.Update.relDestinationPath = new ArrayList<String>();
		main.Storage.Update.absolutEmptyFolderSourcePath = new ArrayList<String>();
	}
	
	default public int findIndexByRelPath(String path) {
		for (int i = 0; i < main.Storage.Update.relDestinationPath.size(); i++) {
			if (path.equals(main.Storage.Update.relDestinationPath.get(i))) {
				return i;
			}
		}
		return -2;
	}
	
	class Index{
		int src, des;
	}
	
	@Comment(make = "Dest compares the modification date "
			+ "of all files in the source directory with "
			+ "the date of the last update, then the files are processed.",
			param = {
					"copy or encryption method"
			})
	default public void updateChangedFiles(Move moveMethod) {
		ArrayList<Index> replaceList = new ArrayList<Index>();
		MyDate.TimeAndDate backUpDate = MyDate.BackUpTime.getTimeFile(Main.getTimeFilePath());
		BasicFileAttributes attrs = null;
		Path path = null;
		for (int i = 0; i < main.Storage.Update.absolutSourcePath.size(); i++) {
			path = new File(main.Storage.Update.absolutSourcePath.get(i)).toPath();
			try {
				attrs = Files.readAttributes(path, BasicFileAttributes.class);
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			FileTime time = attrs.lastModifiedTime();
			MyDate.TimeAndDate tad = MyDate.getFileDate(time);
			if (backUpDate.isBefore(tad)) {
				Index in = new Index();
				in.src = i;
				in.des = findIndexByRelPath(getRelPath(main.Storage.Update.absolutSourcePath.get(i), Main.getRootSourcePath()));
				if (in.des == (-2)) continue;
				replaceList.add(in);
			}
		}
		for (int i = 0; i < replaceList.size(); i++) {
			File dataPath = new File(Main.getDataPath() + "/" + replaceList.get(i).des);
			File from = new File(main.Storage.Update.absolutSourcePath.get(replaceList.get(i).src));
			if (!(SettingType.COPY_ONLY == Main.setting)) {
				byte[] fileInBytes = makeFileToByteArr(from, dataPath);
				if (fileInBytes == null) {
					continue;
				} else {
					dataPath.delete();
					moveMethod.move(from, new File(Main.getDataPath() + "/" + replaceList.get(i).des), fileInBytes);
				}
			} else {
				dataPath.delete();
				moveMethod.move(from, new File(Main.getDataPath() + "/" + replaceList.get(i).des), null);
			}
		}
	}

	@Comment(make = "Compares the relative paths from the path "
			+ "list with those of the directory to be processed. "
			+ "The paths that do not exist are missed. "
			+ "The missing files are then processed.",
			param = {
					"copy or encryption method"
			})
	default public void moveMissingFiles(Move moveMethod) {
		ArrayList<Integer> missingIndex = new ArrayList<Integer>();
		main.Main.userInterface.showLoadingScreen("Missing files are determined...");
		try {
			collectFiles(new File(Main.getRootSourcePath()), main.Main.update);
		} catch (IOException e) {
			e.printStackTrace();
		}
		main.Storage.Update.relDestinationPath = getRecoveryPathList();
		main.Storage.Update.relSourcePath = new String[main.Storage.Update.absolutSourcePath.size()];
		for (int i = 0; i < main.Storage.Update.relSourcePath.length; i++) {
			main.Storage.Update.relSourcePath[i] = getRelPath(main.Storage.Update.absolutSourcePath.get(i), Main.getRootSourcePath());
		}
		for (int i = 0; i < main.Storage.Update.relSourcePath.length; i++) {
			String currentPath = main.Storage.Update.relSourcePath[i];
			boolean exists = false;
			for (int k = 0; k < main.Storage.Update.relDestinationPath.size(); k++) {
				if (currentPath.equals(main.Storage.Update.relDestinationPath.get(k))) {
					exists = true;
					break;
				}
			}
			if (!(exists)) {
				missingIndex.add((Integer) i);
			}
		}
		for (int i = 0; i < missingIndex.size(); i++) {
			main.Storage.Collect.srcPath.add(main.Storage.Update.absolutSourcePath.get((int) missingIndex.get(i)));
		}
		if (main.Storage.Collect.srcPath.size() != 0) {
			main.Storage.Collect.relPath = new String[main.Storage.Collect.srcPath.size()];
			makeRelPath(false);
			writeRelPath(true);
			int to = main.Storage.Update.relDestinationPath.size();
			main.Main.userInterface.closeLoadingScreen();
			for (int i = 0; i < missingIndex.size(); i++) {
				File from = new File(main.Storage.Update.absolutSourcePath.get((int) missingIndex.get(i)));
				if (SettingType.COPY_ONLY == Main.setting) {
					moveMethod.move(from, new File(Main.getDataPath() + "/" + to), null);
				} else {
					byte[] fileInBytes = makeFileToByteArr(from, new File(Main.getDataPath() + "/" + to));
					if (fileInBytes == null) {
						to--;
					} else {
						moveMethod.move(from, new File(Main.getDataPath() + "/" + to), fileInBytes);
					}
				}
				to++;
			}
		} else {
			main.Main.userInterface.closeLoadingScreen();
		}
	}
	
	@Comment(make = "Append the missing empty folders relative paths to the empty folders path list file.")
	default public void moveMissingFolders() throws IOException {
		ArrayList<String> list = getEmptyFolderPathList();
		String[] source = new String[main.Storage.Update.absolutEmptyFolderSourcePath.size()];
		for (int i = 0; i < main.Storage.Update.absolutEmptyFolderSourcePath.size(); i++) {
			source[i] = getRelPath(main.Storage.Update.absolutEmptyFolderSourcePath.get(i), Main.getRootSourcePath());
		}
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		for (int i = 0; i < source.length; i++) {
			boolean found = false;
			for (int k = 0; k < list.size(); k++) {
				if (source[i].equals(list.get(k))) {
					found = true;
					break;
				}
			}
			if (!found) {
				indexList.add((Integer) i);
			}
		}
		main.Storage.Collect.relEmptyFolderPath = new String[indexList.size() + list.size()];
		int index = 0;
		for (int i = 0; i < indexList.size(); i++) {
			main.Storage.Collect.relEmptyFolderPath[index] = source[indexList.get(index)];
			index++;
		}
		for (String list_value : list) {
			main.Storage.Collect.relEmptyFolderPath[index] = list_value;
			index++;
		}
		if (main.Storage.Collect.relEmptyFolderPath.length != 0) {
			File emptyFolder = new File(main.Main.getEmptyFolderPathList());
			FileWriter efw = new FileWriter(emptyFolder, false);// for empty folders
			efw.write("\n");
			for (int i = 0; i < main.Storage.Collect.relEmptyFolderPath.length - 1; i++) {
				efw.write(main.Storage.Collect.relEmptyFolderPath[i] + "\n");
			}
			efw.write(main.Storage.Collect.relEmptyFolderPath[main.Storage.Collect.relEmptyFolderPath.length - 1]);
			efw.close();
		}
	}
}