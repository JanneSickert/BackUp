package interfaces;

import java.io.BufferedReader;
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
	
	default public String[] getRecoveryFolderPathList(ArrayList<String> recoveryPathList) {
		String[] arr = new String[recoveryPathList.size()];
		for (int i = 0; i < recoveryPathList.size(); i++) {
			arr[i] = getFolderPath(recoveryPathList.get(i));
		}
		return arr;
	}
	
	@Check_IO(rightOutput = "C:/aa/bb",
				   input = {"C:/aa/bb/text.txt"})
	default public String getFolderPath(String path) {
		int lastSlash = -1;
		for (int i = path.length() - 1; i > 0; i--) {
			if (path.charAt(i) == '/') {
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
}