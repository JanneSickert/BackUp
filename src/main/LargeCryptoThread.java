package main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import comment.Comment;
import interfaces.Calculate;
import java.nio.charset.StandardCharsets;

public class LargeCryptoThread extends Thread {
	
	private Main.TwoFiles files;
	private byte[] key;
	private int keyIndex;
	private Calculate calculateMethod;
	private final int BLOCK_SIZE = 35714285;
	private byte[] buffer = new byte[BLOCK_SIZE];
	private int bytesPerBlock = 0;
	private FileWriter fwf;
	
	LargeCryptoThread(Main.TwoFiles files, byte[] key, Calculate calculateMethod) {
		this.files = files;
		this.key = key;
		this.calculateMethod = calculateMethod;
		this.keyIndex = 0;
	}
	
	@Override
	public void run() {
		print(files.from, files.to);
		try {
			FileInputStream fin = new FileInputStream(files.from.getAbsolutePath());
			BufferedInputStream bufin = new BufferedInputStream(fin);
			files.to.createNewFile();
			fwf = new FileWriter(files.to, true);
			int data = 0;
			while ((data = bufin.read()) != -1) {
				buffer[bytesPerBlock] = calculateMethod.calc((byte) data, getNextKeyByte());
				bytesPerBlock++;
				if (bytesPerBlock == BLOCK_SIZE) {
					bytesPerBlock = 0;
					writeBlock(false);
				}
			}
			writeBlock(true);
		} catch (FileNotFoundException e) {
			Main.addErrorFile(files);
		} catch (IOException e) {
			Main.addErrorFile(files);
		}
	}

	@Comment(param = {"from", "to"})
	private void print(File a, File b) {
		main.Main.userInterface.move(a.getAbsolutePath(), b.getAbsolutePath(), a.length());
	}
	
	private byte getNextKeyByte() {
		byte by = key[keyIndex];
		keyIndex++;
		if (keyIndex == key.length) {
			keyIndex = 0;
		}
		return by;
	}
	
	private void writeBlock(boolean lastBlock) {
		if (lastBlock) {
			byte[] buffer = new byte[bytesPerBlock];
			for (int i = 0; i < bytesPerBlock; i++) {
				buffer[i] = this.buffer[i];
			}
		}
		String block = new String(buffer, StandardCharsets.UTF_8);
		try {
			fwf.write(block);
			if (lastBlock) {
				fwf.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}