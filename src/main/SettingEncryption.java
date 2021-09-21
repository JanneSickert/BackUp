package main;

import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import interfaces.Calculate;
import comment.Comment;
import main.Main.TwoFiles;

public class SettingEncryption {

	private String path;
	private byte[] key;
	private boolean bigFile;
	
	@Comment(make = "Encrypt and decrypt all files in the settings folder but not the settingType.txt file",
			param = {
					"Path of the file.",
					"The key.",
					"Only the pathFile.txt and the emptyFolderPathList.txt is a big file."
					})
	SettingEncryption(String path, byte[] key, boolean bigFile) {
		this.path = path;
		this.key = key;
		this.bigFile = bigFile;
	}
	
	private byte[] readFile() throws IOException {
		Path pathObject = Paths.get(path);
		return (Files.readAllBytes(pathObject));
	}
	
	private void writeFile(byte[] fileInBytes) throws IOException {
		FileOutputStream stream = new FileOutputStream(path);
		stream.write(fileInBytes);
		stream.close();
	}
	
	private byte[] encryptOrDecrypt(byte[] fileInBytes, Calculate method) {
		int nr_index = 0;
		for (int i = 0; i < fileInBytes.length; i++) {
			fileInBytes[i] = (byte) method.calc(fileInBytes[i], key[nr_index]);
			nr_index++;
			if (nr_index == key.length) {
				nr_index = 0;
			}
		}
		return fileInBytes;
	}
	
	private void renaimFile(String path, String newName) throws IOException {
		File file1 = new File(path);
        File file2 = new File(newName);
        if (file2.exists()) throw new java.io.IOException("to rename file exists");
        boolean success = file1.renameTo(file2);
        if (!success) {
        	System.out.println("Cannot rename file");
        	System.exit(11);
        }
	}
	
	public void encrypt() {
		action(Main.plus);
	}
	
	public void decrypt() {
		action(Main.minus);
	}
	
	private void action(Calculate cm) {
		if (bigFile) {
			String nextName = path + "_copy";
			LargeCryptoThread lct = new LargeCryptoThread(new TwoFiles(new File(path), new File(nextName)), key, cm, true, true);
			lct.start();
			try {
				lct.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			new File(path).delete();
			try {
				renaimFile(nextName, path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				writeFile(encryptOrDecrypt(readFile(), cm));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}