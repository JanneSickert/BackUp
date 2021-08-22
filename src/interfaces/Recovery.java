package interfaces;

import java.io.File;
import java.util.ArrayList;
import enums.SettingType;
import main.Main;
import test.Check_IO;

public interface Recovery extends PathList, Access {

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

	default public void createFolderStructur(ArrayList<String> folderPath) {
		for (String s : folderPath) {
			String p = getFolderPath(s);
			File f = new File(p);
			f.mkdirs();
		}
	}

	default public void moveAllFiles(ArrayList<String> recoveryOutputPaths, Move moveMethod) {
		String backup = main.Main.getDataPath() + "/";
		for (int i = 0; i < recoveryOutputPaths.size(); i++) {
			File a = new File(backup + i);
			File b = new File(recoveryOutputPaths.get(i));
			if (SettingType.COPY_ONLY == Main.setting) {
				moveMethod.move(a, b, null);
			} else {
				byte[] bArr = makeFileToByteArr(a);
				if (bArr == null) {
					continue;
				} else {
					moveMethod.move(a, b, bArr);
				}
			}
		}
	}

	default public ArrayList<String> addRoot(ArrayList<String> p) {
		ArrayList<String> res = new ArrayList<String>();
		for (int i = 0; i < p.size(); i++) {
			res.add(Main.getRecoveryOutputPath() + "/" + p.get(i));
		}
		return res;
	}
	
	default public void createEmptyFolderStructur(ArrayList<String> list) {
		for (String s : addRoot(list)) {
			new File(s).mkdirs();
		}
	}

	default public void start(Move moveMethod) {
		main.Main.userInterface.showLoadingScreen("Determines data to be restored...");
		ArrayList<String> rpl = getRecoveryPathList();
		rpl = addRoot(rpl);
		main.Main.userInterface.closeLoadingScreen();
		main.Main.userInterface.showLoadingScreen("Creates folder structure...");
		createFolderStructur(rpl);
		createEmptyFolderStructur(getEmptyFolderPathList());
		main.Main.userInterface.closeLoadingScreen();
		moveAllFiles(rpl, moveMethod);
	}
}