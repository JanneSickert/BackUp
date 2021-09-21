package main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileOutputStream;
import comment.Comment;
import interfaces.Calculate;

public class LargeCryptoThread extends Thread {
	
	private Main.TwoFiles files;
	private byte[] key;
	private int keyIndex;
	private Calculate calculateMethod;
	private final int BLOCK_SIZE = 35714285;
	private byte[] buffer = new byte[BLOCK_SIZE];
	private int bytesPerBlock = 0;
	private boolean recoveryMove = false;
	private int nrOfFiles = 0;
	private String dataPath = null;
	private FileOutputStream output = null;
	private boolean settingsEncryption = false;
	
	public LargeCryptoThread(Main.TwoFiles files, byte[] key, Calculate calculateMethod, boolean recoveryMove, int nrOfFiles, String dataPath) {
		this.files = files;
		this.key = key;
		this.calculateMethod = calculateMethod;
		this.keyIndex = 0;
		this.recoveryMove = recoveryMove;
		this.nrOfFiles = nrOfFiles;
		this.dataPath = dataPath;
	}
	
	public LargeCryptoThread(Main.TwoFiles files, byte[] key, Calculate calculateMethod, boolean recoveryMove, boolean settingsEncryption) {
		this.files = files;
		this.key = key;
		this.calculateMethod = calculateMethod;
		this.keyIndex = 0;
		this.recoveryMove = recoveryMove;
		this.settingsEncryption = settingsEncryption;
	}
	
	@Override
	public void run() {
		if (!recoveryMove) {
			files.to = new File(dataPath + "/" + nrOfFiles);
		}
		if (!settingsEncryption) {
			print(files.from, files.to);
		}
		try {
			FileInputStream fin = new FileInputStream(files.from.getAbsolutePath());
			BufferedInputStream bufin = new BufferedInputStream(fin);
			files.to.createNewFile();
			output = new FileOutputStream(files.to.getAbsolutePath(), true);
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
			bufin.close();
		} catch (FileNotFoundException e) {
			Main.addErrorFile(files);
		} catch (IOException e) {
			Main.addErrorFile(files);
		}
	}

	@Comment(param = {"from", "to"})
	private static synchronized void print(File a, File b) {
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
	
	private void writeBlock(boolean lastBlock) throws IOException {
		byte[] localBuffer = null;
		if (lastBlock) {
			if (bytesPerBlock != 0) {
				localBuffer = new byte[bytesPerBlock];
			}
			for (int i = 0; i < bytesPerBlock; i++) {
				localBuffer[i] = buffer[i];
			}
			try {
			   if (bytesPerBlock != 0) {
				   output.write(localBuffer);
			   }
			} finally {
			   output.close();
			}
		} else {
			localBuffer = buffer;
			output.write(localBuffer);
		}
	}
}