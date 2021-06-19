package main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import interfaces.PathList;

public abstract class Update extends Collect implements PathList {

	protected static ArrayList<String> absolutSourcePath = new ArrayList<String>();
	protected static ArrayList<String> relDestinationPath = new ArrayList<String>();
	protected static String[] relSourcePath;

	Update() {
		moveMissingFiles();
		updateChangedFiles();
	}
	
	private int findIndexByRelPath(String path) {
		for (int i = 0; i < relDestinationPath.size(); i++) {
			if (path.equals(relDestinationPath.get(i))) {
				return i;
			}
		}
		return -2;
	}
	
	private class Index{
		int src, des;
	}
	
	private void updateChangedFiles() {
		ArrayList<Index> replaceList = new ArrayList<Index>();
		MyDate.TimeAndDate backUpDate = MyDate.BackUpTime.getTimeFile(Main.getTimeFilePath());
		BasicFileAttributes attrs = null;
		Path path = null;
		for (int i = 0; i < absolutSourcePath.size(); i++) {
			path = new File(absolutSourcePath.get(i)).toPath();
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
				in.des = findIndexByRelPath(getRelPath(absolutSourcePath.get(i), Main.getRootSourcePath()));
				replaceList.add(in);
			}
		}
		for (int i = 0; i < replaceList.size(); i++) {
			File dataPath = new File(Main.getDataPath() + "/" + replaceList.get(i).des);
			dataPath.delete();
			move(new File(absolutSourcePath.get(replaceList.get(i).src)), new File(Main.getDataPath() + "/" + replaceList.get(i).des));
		}
	}

	private void moveMissingFiles() {
		ArrayList<Integer> missingIndex = new ArrayList<Integer>();
		try {
			collectFiles(new File(Main.getRootSourcePath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		relDestinationPath = getRecoveryPathList();
		relSourcePath = new String[absolutSourcePath.size()];
		for (int i = 0; i < relSourcePath.length; i++) {
			relSourcePath[i] = getRelPath(absolutSourcePath.get(i), Main.getRootSourcePath());
		}
		for (int i = 0; i < relSourcePath.length; i++) {
			String currentPath = relSourcePath[i];
			boolean exists = false;
			for (int k = 0; k < relDestinationPath.size(); k++) {
				if (currentPath.equals(relDestinationPath.get(k))) {
					exists = true;
					break;
				}
			}
			if (!(exists)) {
				missingIndex.add((Integer) i);
			}
		}
		for (int i = 0; i < missingIndex.size(); i++) {
			super.srcPath.add(absolutSourcePath.get((int) missingIndex.get(i)));
		}
		super.relPath = new String[srcPath.size()];
		makeRelPath();
		writeRelPath();
		int to = relDestinationPath.size();
		for (int i = 0; i < missingIndex.size(); i++) {
			move(new File(absolutSourcePath.get((int) missingIndex.get(i))), new File(Main.getDataPath() + "/" + to));
			to++;
		}
	}
	
	protected abstract void move(File from, File to);

	@Override
	public void doWithFile(File f) {
		absolutSourcePath.add(f.getAbsolutePath());
	}
}