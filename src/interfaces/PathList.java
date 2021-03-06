package interfaces;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public interface PathList {

	default public ArrayList<String> getList(String path) {
		ArrayList<String> list = new ArrayList<String>();
		FileReader fr;
		try {
			fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			while ((line = br.readLine()) != null) {
				list.add(line);
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
	
	default public ArrayList<String> getRecoveryPathList() {
		return (getList(main.Main.getPathListPath()));
	}
	
	default public ArrayList<String> getEmptyFolderPathList() {
		return (getList(main.Main.getEmptyFolderPathList()));
	}
}
