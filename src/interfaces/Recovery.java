package interfaces;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import test.Check_IO;

public interface Recovery {

	default public ArrayList<String> getRecoveryPathList() {
		ArrayList<String> list = new ArrayList<String>();
		FileReader fr;
		try {
			fr = new FileReader(main.Main.getPathListPath());
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			while ((line = br.readLine()) != null) {
				list.add(main.Main.getRecoveryOutputPath() + "/" + line);
			}
			br.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return list;
	}

	@Check_IO(rightOutput = "C:/aa/bb", input = { "C:/aa/bb/text.txt" })
	default public String getFolderPath(String path) {
		int lastSlash = -1;
		for (int i = path.length() - 1; i > 0; i--) {
			if (path.charAt(i) == '/' || path.charAt(i) == '\\') {
				lastSlash = i;
				break;
			}
		}
		char[] arr = new char[lastSlash];
		for (int i = 0; i < lastSlash; i++) {
			arr[i] = path.charAt(i);
		}
		return (new String(arr));
	}

	public void move(File from, File to);

	default public void createFolderStructur(ArrayList<String> folderPath) {
		for (String s : folderPath) {
			String p = getFolderPath(s);
			File f = new File(p);
			f.mkdirs();
		}
	}

	default public void moveAllFiles(ArrayList<String> recoveryOutputPaths) {
		String backup = main.Main.getDataPath() + "/";
		for (int i = 0; i < recoveryOutputPaths.size(); i++) {
			File a = new File(backup + i);
			File b = new File(recoveryOutputPaths.get(i));
			pri(a, b);
			move(a, b);
		}
	}

	default public void pri(File from, File to) {
		System.out.println("Move file from:" + from.getAbsolutePath() + " to " + to.getAbsolutePath());
	}

	default public void start() {
		ArrayList<String> rpl = getRecoveryPathList();
		createFolderStructur(rpl);
		moveAllFiles(rpl);
	}
}