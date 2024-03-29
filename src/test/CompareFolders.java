package test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import ui.CommandLineFunctions;
import annotationen.Comment;

public class CompareFolders extends CommandLineFunctions {

	private static ArrayList<String> path = new ArrayList<String>();
	private static boolean[] same;
	private final static int NR_OF_THREADS = 7;
	private static MyThread[] thread = new MyThread[NR_OF_THREADS];
	private static int threadIndex = 0;

	@Comment(
			make = "Compare two Folders",
			param = {"Path Folder A", "Path Folder B"},
			ret = "true: The Folders are the same"
			)
	public static boolean compare(String pathA, String pathB) {
		for (int i = 0; i < NR_OF_THREADS; i++) {
			thread[i] = null;
		}
		try {
			collectPaths(new File(pathA));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayList<String> a = path;
		path = new ArrayList<String>();
		try {
			collectPaths(new File(pathB));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayList<String> b = path;
		if (a.size() == b.size()) {
			same = new boolean[a.size()];
			for (int i = 0; i < a.size(); i++) {
				compareFiles(new File(a.get(i)), new File(b.get(i)), i);
			}
			joinAll();
			if (checkSame()) {
				return true;
			} else {
				return false;
			}
		} else {
			// The folders are not the same.
			return false;
		}
	}

	private static void joinAll() {
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

	/**
	 * 
	 * @return true if the folders are the same.
	 */
	private static boolean checkSame() {
		int nr = 0;
		for (int i = 0; i < same.length; i++) {
			if (same[i])
				nr++;
		}
		return (nr == same.length);
	}

	private static void compareFiles(File a, File b, int sameIndex) {
		if (thread[threadIndex] == null) {
			thread[threadIndex] = new MyThread(a, b, sameIndex);
			thread[threadIndex].start();
			threadIndex++;
			if (threadIndex == NR_OF_THREADS) {
				threadIndex = 0;
			}
		} else {
			boolean bb = true;
			do {
				if (!(thread[threadIndex].isAlive())) {
					bb = false;
					thread[threadIndex] = new MyThread(a, b, sameIndex);
					thread[threadIndex].start();
				}
				threadIndex++;
				if (threadIndex == NR_OF_THREADS) {
					threadIndex = 0;
				}
			} while (bb);
		}
	}
	
	private static synchronized void setSame(int sameIndex, boolean theSame) {
		CompareFolders.same[sameIndex] = theSame;
	}

	static class MyThread extends Thread{
		
		File a, b;
		int sameIndex = -1;
		final long MAX_SPACE = Integer.MAX_VALUE - 255;
		
		MyThread(File a, File b, int sameIndex) {
			this.a = a;
			this.b = b;
			this.sameIndex = sameIndex;
		}
		
		@Override
		public void run() {
			p(a.getAbsolutePath() + " = " + b.getAbsolutePath());
			long len_a = a.length(), len_b = b.length();
			boolean theSame = (len_a == len_b);
			if (theSame) {
				if (len_a < MAX_SPACE) {
					byte[] b_a = makeFileToByteArr(a), b_b = makeFileToByteArr(b);
					for (int i = 0; i < b_b.length; i++) {
						if (!(b_a[i] == b_b[i])) {
							theSame = false;
							break;
						}
					}
				} else {
					try {
						FileInputStream fin_a = new FileInputStream(a.getAbsolutePath());
						FileInputStream fin_b = new FileInputStream(b.getAbsolutePath());
						BufferedInputStream bufin_a = new BufferedInputStream(fin_a);
						BufferedInputStream bufin_b = new BufferedInputStream(fin_b);
						int data_a = 0, data_b = 0;
						while (((data_a = bufin_a.read()) != -1) && (data_b = bufin_b.read()) != -1) {
							if (data_a != data_b) {
								theSame = false;
								break;
							}
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			CompareFolders.setSame(sameIndex, theSame);
		}
		
		private byte[] makeFileToByteArr(File currentFile) {
			Path path = Paths.get(currentFile.getAbsolutePath());
			try {
				return (Files.readAllBytes(path));
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	private static void collectPaths(File from) throws IOException {
		File[] fileList = from.listFiles();
		if (fileList != null) {
			for (File file : fileList) {
				if (file.isDirectory()) {
					collectPaths(file);
				} else {
					path.add(file.getAbsolutePath());
				}
			}
		}
	}
}
