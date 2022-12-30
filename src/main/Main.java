package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import enums.RecoverOrUpdate;
import enums.SettingType;
import interfaces.Calculate;
import interfaces.Move;
import interfaces.NewBackUp;
import interfaces.NewOrUpdate;
import interfaces.Recovery;
import interfaces.UI;
import interfaces.Update;
import interfaces.PathList;
import interfaces.Collect;
import comment.Comment;
import annotationen.*;

public class Main {

	private static String rootSource = null;
	private static String rootDestination = null;
	private static String keyFilePath = null;
	private static String password = null;
	private static String recoveryOutputPath = null;
	private static byte[] key = null;
	public static UI userInterface;
	public static long lengthOfAllFiles = 0L;
	static Calculate calculateMethod;
	private static ArrayList<TwoFiles> errorFiles = new ArrayList<TwoFiles>();
	public static SettingType setting = null;
	private static boolean recoveryMove = false;
	private static int nrOfFiles = 0;
	private static Map<List<String>, Method> map;

	public static void main(String[] args) {
		System.out.println("@version 3.0");
		map = new HashMap<List<String>, Method>();
		Class<Main> c0 = Main.class;
		Class<comment.CreateHTML> c1 = comment.CreateHTML.class;
		Class<test.Test> c2 = test.Test.class;
		readParameter(c0);
		readParameter(c1);
		readParameter(c2);
		String parameter = args[0];
		List<String> key = null;
		for (List<String> p : map.keySet()) {
			if (p.equals(parameter)) {
				key = p;
			}
		}
		Method entryPointMethod = map.get(key);
	    if (entryPointMethod.isAnnotationPresent(EntryPoint.class)) {
	    	try {
				entryPointMethod.invoke(null);
			} catch (IllegalAccessException | 
				   IllegalArgumentException | 
				   InvocationTargetException e) {
					e.printStackTrace();
			}
	    }
	}
	
	private static <T> void readParameter(Class<T> c) {
		Method[] methoden = c.getDeclaredMethods();
		for (Method m : methoden) {
			EntryPoint me = m.getAnnotation(EntryPoint.class);
			List<String> list = new ArrayList<String>();
			for (String key : me.keys()) {
				list.add(key);
			}
			list.add(me.describtion());
			map.put(list, m);
		}
	}
	
	private static String getKeysOptions(Set<List<String>> set) {
		Iterator<List<String>> iterator = set.iterator();
		String res = "";
		while (iterator.hasNext()) {
		  List<String> element = iterator.next();
		  for (int i = 0; i < element.size() - 1; i++) {
			  res += element.get(i) + ", ";
		  }
		}
		return res;
	}

	@EntryPoint(
			keys = {"help", "--help"},
			describtion = "show parameter options"
			)
	public static void help() {
		Map<String, String> infomap = new HashMap<String, String>();
		infomap.put(
				getKeysOptions(map.keySet()),
				map.keySet().toArray()[map.keySet().toArray().length - 1].toString()
						);
		infomap.forEach((k, v) -> System.out.println("gradlew run --args=\"\"" + k + ": " + v));
	}
	
	@EntryPoint(
			keys = {"GUI", "gui"},
			describtion = "run with GUI"
			)
	public static void runWithGui() {
		userInterface = new ui.Gui();
		start();
	}
	
	@EntryPoint(
			keys = {"CMD", "cmd"},
			describtion = "run with cmd"
			)
	public static void runWithCmd() {
		userInterface = new ui.Cmd();
		start();
	}

	public static void start() {
		userInterface.showHead();
		rootDestination = userInterface.getDestinationRootPath();
		boolean settingFileExists = new File(getSettingTypeFilePath()).exists();
		ImportExport ix_setting = new ImportExport(new File(getSettingTypeFilePath()));
		ImportExport ix_source = new ImportExport(new File(getSourceFilePath()));
		File[] root = { new File(getDataPath()), new File(getSettingsPath()) };
		for (File f : root) {
			f.mkdirs();
		}
		if (settingFileExists) {
			String str = ix_setting.inport();
			setting = SettingType.getTypeByName(str);
		} else {
			rootSource = userInterface.getSourceRootPath();
			setting = userInterface.getSettings();
			ix_setting.export(setting.toString());
			ix_source.export(rootSource);
		}
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
			moveMethod = getCryptMove(false);
		}
		if (settingFileExists) {
			decryptAllSettingFiles();
		}
		rootSource = ix_source.inport();
		userInterface.showLoadingScreen("Determine amount of data...");
		getLengthOfAllFiles();
		userInterface.closeLoadingScreen();
		if (new File(getPathListPath()).exists()) {
			boolean update = (userInterface.updateOrRecover() == RecoverOrUpdate.UPDATE);
			if (update) {
				new Update() {
				}.update(moveMethod);
				moveMethod.joinAll();
				main.MyDate.BackUpTime.createTimeFile(Main.getTimeFilePath());
			} else {// recovery
				recoveryMove = true;
				recoveryOutputPath = userInterface.getRecoveryOutputPath();
				if (setting != SettingType.COPY_ONLY) {
					calculateMethod = minus;
					moveMethod = getCryptMove(false);
				}
				new Recovery() {
				}.start(moveMethod);
				moveMethod.joinAll();
			}
		} else {
			new NewBackUp() {
			}.newBackUp(moveMethod);
			moveMethod.joinAll();
			main.MyDate.BackUpTime.createTimeFile(Main.getTimeFilePath());
		}
		Main.NotFoundFiles notFoundFiles = new Main.NotFoundFiles();
		if (!(recoveryMove)) {
			notFoundFiles.updatePathList();
			if (setting == SettingType.COPY_ONLY) {
				notFoundFiles.retry(moveMethod);
			} else {
				notFoundFiles.retry(getCryptMove(true));
			}
		} else {// recovery large files
			if (setting == SettingType.COPY_ONLY) {
				notFoundFiles.retry(moveMethod);
			} else {
				notFoundFiles.retryRecovery(getCryptMove(true));
			}
		}
		if (errorFiles.size() != 0) {
			userInterface.showNotFoundFiles(errorFiles);
			notFoundFiles.updatePathList();
		}
		if (setting != SettingType.COPY_ONLY) {
			encryptAllSettingFiles();
		}
		userInterface.finishMessage();
	}
	
	private static void encryptAllSettingFiles() {
		for (SettingEncryption se : getSettingEncryption()) {
			se.encrypt();
		}
	}
	
	private static void decryptAllSettingFiles() {
		for (SettingEncryption se : getSettingEncryption()) {
			se.decrypt();
		}
	}
	
	private static SettingEncryption[] getSettingEncryption() {
		return (new SettingEncryption[] {
				new SettingEncryption(getEmptyFolderPathList(), key, true),
				new SettingEncryption(getPathListPath(), key, true),
				new SettingEncryption(getSourceFilePath(), key, false),
				new SettingEncryption(getTimeFilePath(), key, false)
		});
	}
	
	public static class TwoFiles {
		
		public File from, to;
		
		public TwoFiles(File from, File to) {
			this.from = from;
			this.to = to;
		}
	}


	public static synchronized void addErrorFile(TwoFiles files) {
		errorFiles.add(files);
	}
	
	public static class NotFoundFiles implements Collect, PathList {
		
		private String[] errorList = new String[errorFiles.size()];
		
		@Comment(make = "Try to process the files that were "
				+ "previously used by another process, deleted or moved.")
		void retry(Move moveMethod) {
			ArrayList<TwoFiles> localErrorFiles = new ArrayList<TwoFiles>();
			for (int i = 0; i < errorFiles.size(); i++) {
				localErrorFiles.add(errorFiles.get(i));
			}
			errorFiles = new ArrayList<TwoFiles>();
			nrOfFiles = new File(getDataPath()).listFiles().length;
			for (int i = 0; i < localErrorFiles.size(); i++) {
				moveMethod.move(localErrorFiles.get(i).from, localErrorFiles.get(i).to, key);
				toPathList(getRelPath(localErrorFiles.get(i).from.getAbsolutePath(), getRootSourcePath()));
			}
			moveMethod.joinAll();
		}
		
		void retryRecovery(Move moveMethod) {
			ArrayList<TwoFiles> localErrorFiles = new ArrayList<TwoFiles>();
			for (int i = 0; i < errorFiles.size(); i++) {
				localErrorFiles.add(errorFiles.get(i));
			}
			errorFiles = new ArrayList<TwoFiles>();
			for (int i = 0; i < localErrorFiles.size(); i++) {
				moveMethod.move(localErrorFiles.get(i).from, localErrorFiles.get(i).to, key);
			}
			moveMethod.joinAll();
		}
		
		private void toPathList(String relPath) {
			try {
				FileWriter fwf = new FileWriter(new File(getPathListPath()), true);
				fwf.write("\n" + relPath);
				fwf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Comment(make = "Deletes the paths from the path list that "
				+ "are contained in the ArrayList<File> errorFiles")
		void updatePathList() {
			ArrayList<String> pathList = getRecoveryPathList();
			String[] nextPathList = new String[pathList.size() - errorFiles.size()];
			int k = 0;
			for (int i = 0; i < errorFiles.size(); i++) {
				errorList[k] = getRelPath(errorFiles.get(i).from.getAbsolutePath(), getRootSourcePath());
				k++;
			}
			k = 0;
			for (int i = 0; i < pathList.size(); i++) {
				if (!(existInErrorList(pathList.get(i)))) {
					nextPathList[k] = pathList.get(i);
					k++;
				}
			}
			Storage.Collect.relPath = nextPathList;
			new File(getPathListPath()).delete();
			writeRelPath(false);
		}
		
		private boolean existInErrorList(String path) {
			for (int i = 0; i < errorList.length; i++) {
				if (path.equals(errorList[i])) {
					return true;
				}
			}
			return false;
		}
	}

	
	private static void getLengthOfAllFiles() {
		Collect collectLarge = new Collect() {
		};
		try {
			collectLarge.collectFiles(new File(rootSource), new NewOrUpdate() {
				@Override
				public void doWithFile(File f) {
					lengthOfAllFiles = lengthOfAllFiles + f.length();
				}

				@Override
				public void doWithEmptyFolder(File f) {
					// Folders have no size.
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class CryptoThread extends Thread {

		private File from;
		private File to;
		private byte[] key;
		private Calculate cryCal;
		private byte[] fileInBytes;

		CryptoThread(File from, File to, byte[] key, Calculate cryCal, byte[] fileInBytes) {
			this.from = from;
			this.to = to;
			this.key = key;
			this.cryCal = cryCal;
			this.fileInBytes = fileInBytes;
		}

		@Override
		public void run() {
			print(from, to);
			byte[] arr = fileInBytes;
			byte[] cry = encryptOrDecrypt(arr, key, cryCal);
			writeFileFromBytes(to.getAbsolutePath(), cry);
		}

		private void print(File a, File b) {
			main.Main.userInterface.move(a.getAbsolutePath(), b.getAbsolutePath(), a.length());
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
		Thread[] thread = new Thread[NR_OF_THREADS];
		int threadIndex = 0;
		private boolean largeFile = false;

		public CryptedMove(boolean largeFile) {
			this.largeFile = largeFile;
			for (int i = 0; i < NR_OF_THREADS; i++) {
				thread[i] = null;
			}
		}

		@Override
		public void move(File from, File to, byte[] fileInBytes) {
			if (thread[threadIndex] == null) {
				if (largeFile) {
					thread[threadIndex] = new LargeCryptoThread(new TwoFiles(from, to), Main.key.clone(), Main.calculateMethod, Main.recoveryMove, nrOfFiles, getDataPath());
				} else {
					thread[threadIndex] = new CryptoThread(from, to, Main.key.clone(), Main.calculateMethod, fileInBytes);
				}
				thread[threadIndex].start();
				Main.nrOfFiles++;
				threadIndex++;
				if (threadIndex == NR_OF_THREADS) {
					threadIndex = 0;
				}
			} else {
				boolean b = true;
				do {
					if (!(thread[threadIndex].isAlive())) {
						b = false;
						if (largeFile) {
							thread[threadIndex] = new LargeCryptoThread(new TwoFiles(from, to), Main.key.clone(), Main.calculateMethod, Main.recoveryMove, nrOfFiles, getDataPath());
						} else {
							thread[threadIndex] = new CryptoThread(from, to, Main.key.clone(), Main.calculateMethod, fileInBytes);
						}
						thread[threadIndex].start();
						Main.nrOfFiles++;
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
					if (thread[i] != null) {
						thread[i].join();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static Move getCryptMove(boolean largeFile) {
		return ((Move) new Main.CryptedMove(largeFile));
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
	
	private static String getSourceFilePath() {
		return (getSettingsPath() + "/source.txt");
	}
	
	private static String getSettingTypeFilePath() {
		return (getSettingsPath() + "/settingType.txt");
	}
	
	public static String getEmptyFolderPathList() {
		return (getSettingsPath() + "/EmptyFolderPathList.txt");
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
		public void move(File from, File to, byte[] fileInBytes) {
			print(from, to);
			try {
				Files.copy(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				errorFiles.add(new TwoFiles(from, to));
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

		@Override
		public void doWithEmptyFolder(File f) {
			main.Storage.Update.absolutEmptyFolderSourcePath.add(f.getAbsolutePath());
		}
	};

	public static NewOrUpdate newBackUp = new NewOrUpdate() {
		@Override
		public void doWithFile(File f) {
			main.Storage.Collect.srcPath.add(f.getAbsolutePath());
		}

		@Override
		public void doWithEmptyFolder(File f) {
			main.Storage.Collect.absolutEmptyFolderSourcePath.add(f.getAbsolutePath());
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