package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Key {

	private static String readKeyFile() {
		String text = "";
		FileReader fr;
		try {
			fr = new FileReader(Main.getKeyFilePath());
			BufferedReader br = new BufferedReader(fr);
			String zeile = "";
			while ((zeile = br.readLine()) != null) {
				text = text + zeile;
			}
			br.close();
		} catch (FileNotFoundException e1) {
			System.exit(0);
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return text;
	}

	private static void writeFile(String text) {
		File ff = new File(Main.getKeyFilePath());
			try {
				ff.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				FileWriter fwf = new FileWriter(ff, false);
				fwf.write(text);
				fwf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static boolean keyFileExists() {
		return (new File(Main.getKeyFilePath()).exists());
	}
	
	public static byte[] readKey() {
		String skey = readKeyFile();
		ArrayList<Byte> akey = new ArrayList<Byte>();
		for (int i = 0; i < skey.length() - 1; i++) {
			akey.add((Byte) Byte.parseByte(new String(new char[] {skey.charAt(i), skey.charAt(i + 1)})));
		}
		byte[] key = new byte[akey.size()];
		for (int i = 0; i < akey.size(); i++) {
			key[i] = (byte) akey.get(i);
		}
		return key;
	}
	
	public static void createKey() {
		final int SIZE = 3000;
		int[] arr = new int[SIZE];
		for (int i = 0; i < SIZE; i++) {
			arr[i] = (int) Math.round(Math.random() * 10);
		}
		String text = "";
		for (int nr : arr) {
			text = text + nr;
		}
		writeFile(text);
	}
	
	public static byte[] mergeToKeys(byte[] file, byte[] password) {
		byte[] next = new byte[file.length];
		int index = 0;
		while (index < file.length) {
			for (int i = 0; i < password.length && index < file.length; i++) {
				next[index] = Main.plus.calc(file[index], password[i]);
				index++;
			}
		}
		return next;
	}
}