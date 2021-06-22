package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import enums.SettingType;
import interfaces.*;

public class Main {

	private static String rootSource = null;
	private static String rootDestination = null;
	private static String keyFilePath = null;
	private static String password = null;
	private static String recoveryOutputPath = null;
	private static byte[] key;
	public static UI userInterface;
	static Calculate calculateMethod;

	public static void main(String[] args) {
		userInterface = new ui.Source();
		userInterface.showHead();
		SettingType setting = userInterface.getSettings();
		rootDestination = userInterface.getDestinationRootPath();
		rootSource = userInterface.getSourceRootPath();
		keyFilePath = null;
		password = null;
		if (setting == SettingType.KEY_FILE || setting == SettingType.PASSWORD_AND_KEY_FILE) {
			keyFilePath = userInterface.getPathForKeyFile();
		}
		if (setting == SettingType.PASSWORD || setting == SettingType.PASSWORD_AND_KEY_FILE) {
			password = userInterface.getPassword();
		}
		Move moveMethod;
		switch (setting) {
		case COPY_ONLY:
			moveMethod = copy;
			break;
		default:
			calculateMethod = plus;
			key = generateKey(setting);
			moveMethod = getCryptMove();
		}
		if (new File(getPathListPath()).exists()) {
			boolean update = userInterface.updateOrRecover();
			if (update) {
				new Update() {
				}.update(moveMethod);
				moveMethod.joinAll();
				main.MyDate.BackUpTime.createTimeFile(Main.getTimeFilePath());
			} else {// recovery
				recoveryOutputPath = userInterface.getRecoveryOutputPath();
				if (setting != SettingType.COPY_ONLY) {
					calculateMethod = minus;
					moveMethod = getCryptMove();
				}
				new Recovery() {
				}.start(moveMethod);
				moveMethod.joinAll();
			}
		} else {
			File[] root = { new File(getDataPath()), new File(getSettingsPath()) };
			for (File f : root) {
				f.mkdirs();
			}
			new NewBackUp() {
			}.newBackUp(moveMethod);
			moveMethod.joinAll();
			main.MyDate.BackUpTime.createTimeFile(Main.getTimeFilePath());
		}
		userInterface.finishMessage();
	}

	static class CryptoThread extends Thread {

		private File from;
		private File to;
		private byte[] key;
		private Calculate cryCal;

		CryptoThread(File from, File to, byte[] key, Calculate cryCal) {
			this.from = from;
			this.to = to;
			this.key = key;
			this.cryCal = cryCal;
		}

		@Override
		public void run() {
			print(from, to);
			byte[] arr = makeFileToByteArr(from);
			byte[] cry = encryptOrDecrypt(arr, key, cryCal);
			writeFileFromBytes(to.getAbsolutePath(), cry);
		}

		private void print(File a, File b) {
			main.Main.userInterface.move(a.getAbsolutePath(), b.getAbsolutePath());
		}

		private byte[] encryptOrDecrypt(byte[] fileInBytes, byte[] key, Calculate method) {
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

		private byte[] makeFileToByteArr(File f) {
			byte[] fileInBytes = null;
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(f);
				long size = f.length();
				fileInBytes = new byte[(int) size];
				int by, i = 0;
				while ((by = fis.read()) != -1)
					fileInBytes[i++] = (byte) by;
			} catch (IOException ex) {
				System.out.println(ex);
			} finally {
				if (fis != null)
					try {
						fis.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
			}
			return fileInBytes;
		}

		private void writeFileFromBytes(String path, byte[] fileInBytes) {
			File f = new File(path);
			try {
				if (!(f.exists())) {
					f.createNewFile();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try (FileOutputStream stream = new FileOutputStream(path)) {
				stream.write(fileInBytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static class CryptedMove implements Move {

		final int NR_OF_THREADS = 7;
		CryptoThread[] thread = new CryptoThread[NR_OF_THREADS];
		int threadIndex = 0;
		
		public CryptedMove() {
			for (int i = 0; i < NR_OF_THREADS; i++) {
				thread[i] = null;
			}
		}

		public void move(File from, File to) {
			if (thread[threadIndex] == null) {
				thread[threadIndex] = new CryptoThread(from, to, Main.key.clone(), Main.calculateMethod);
				thread[threadIndex].start();
				threadIndex++;
				if (threadIndex == NR_OF_THREADS) {
					threadIndex = 0;
				}
			} else {
				boolean b = true;
				do {
					if (!(thread[threadIndex].isAlive())) {
						b = false;
						thread[threadIndex] = new CryptoThread(from, to, Main.key.clone(), Main.calculateMethod);
						thread[threadIndex].start();
					}
					threadIndex++;
					if (threadIndex == NR_OF_THREADS) {
						threadIndex = 0;
					}
				} while (b);
			}
		}
		
		@Override
		public void joinAll() {
			for (int i = 0; i < NR_OF_THREADS; i++) {
				try {
					thread[i].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static Move getCryptMove() {
		return ((Move) new Main.CryptedMove());
	}

	@SuppressWarnings("incomplete-switch")
	private static byte[] generateKey(SettingType setting) {
		byte[] ret = null;
		switch (setting) {
		case PASSWORD:
			ret = password.getBytes();
			break;
		case KEY_FILE:
			if (!Key.keyFileExists()) {
				Key.createKey();
			}
			ret = Key.readKey();
			break;
		case PASSWORD_AND_KEY_FILE:
			byte[] pasBytes = password.getBytes();
			if (!Key.keyFileExists()) {
				Key.createKey();
			}
			byte[] keyBytes = Key.readKey();
			ret = Key.mergeToKeys(keyBytes, pasBytes);
		}
		return ret;
	}

	public static String getDataPath() {
		return (rootDestination + "/data");
	}

	public static String getSettingsPath() {
		return (rootDestination + "/settings");
	}

	public static String getPathListPath() {
		return (getSettingsPath() + "/pathList.txt");
	}

	public static String getTimeFilePath() {
		return (getSettingsPath() + "/time.txt");
	}

	public static String getKeyFilePath() {
		return keyFilePath;
	}

	public static String getRootSourcePath() {
		return rootSource;
	}

	public static String getRecoveryOutputPath() {
		return recoveryOutputPath;
	}

	public static Move copy = new Move() {
		@Override
		public void move(File from, File to) {
			print(from, to);
			try {
				Files.copy(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void joinAll() {
			// Use only one thread.
		}
	};

	public static NewOrUpdate update = new NewOrUpdate() {
		@Override
		public void doWithFile(File f) {
			main.Storage.Update.absolutSourcePath.add(f.getAbsolutePath());
		}
	};

	public static NewOrUpdate newBackUp = new NewOrUpdate() {
		@Override
		public void doWithFile(File f) {
			main.Storage.Collect.srcPath.add(f.getAbsolutePath());
		}
	};

	public static Calculate plus = new Calculate() {
		@Override
		public byte calc(byte a, byte b) {
			byte by = (byte) (a + b);
			return by;
		}
	};

	public static Calculate minus = new Calculate() {
		@Override
		public byte calc(byte a, byte b) {
			byte by = (byte) (a - b);
			return by;
		}
	};
}