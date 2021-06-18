package main;

import java.io.File;
import java.io.IOException;

abstract class Collect {

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
}
