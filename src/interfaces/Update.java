package interfaces;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import main.Main;
import main.MyDate;

public interface Update extends Collect, PathList{

	default public void update(Move moveMethod) {
		initVars();
		initUpdateVars();
		moveMissingFiles(moveMethod);
		updateChangedFiles(moveMethod);
	}
	
	default public void initUpdateVars() {
		main.Storage.Update.absolutSourcePath = new ArrayList<String>();
		main.Storage.Update.relDestinationPath = new ArrayList<String>();
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
	
	default public void updateChangedFiles(Move moveMethod) {
		ArrayList<Index> replaceList = new ArrayList<Index>();
		MyDate.TimeAndDate backUpDate = MyDate.BackUpTime.getTimeFile(Main.getTimeFilePath());
		BasicFileAttributes attrs = null;
		Path path = null;
		main.Main.userInterface.showLoadingScreen("Changed files are determined...");
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
		main.Main.userInterface.closeLoadingScreen();
		for (int i = 0; i < replaceList.size(); i++) {
			File dataPath = new File(Main.getDataPath() + "/" + replaceList.get(i).des);
			dataPath.delete();
			moveMethod.move(new File(main.Storage.Update.absolutSourcePath.get(replaceList.get(i).src)), new File(Main.getDataPath() + "/" + replaceList.get(i).des));
		}
	}

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
			makeRelPath();
			writeRelPath();
			int to = main.Storage.Update.relDestinationPath.size();
			main.Main.userInterface.closeLoadingScreen();
			for (int i = 0; i < missingIndex.size(); i++) {
				moveMethod.move(new File(main.Storage.Update.absolutSourcePath.get((int) missingIndex.get(i))), new File(Main.getDataPath() + "/" + to));
				to++;
			}
		} else {
			main.Main.userInterface.closeLoadingScreen();
		}
	}
}