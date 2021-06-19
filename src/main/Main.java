package main;

import java.io.File;
import enums.SettingType;
import interfaces.UI;

public class Main {

	private static String rootSource = null;
	private static String rootDestination = null;
	private static String keyFilePath = null;
	private static String password = null;
	private static String recoveryOutputPath = null;
	
	public static void main(String[] args) {
		UI userInterface = new ui.Cmd();
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
		if (new File(getPathListPath()).exists()) {
			boolean update = userInterface.updateOrRecover();
			if (update) {
				switch (setting) {
				case COPY_ONLY:
					new CopyUpdate();
					break;
				default:
					System.exit(100);
				}
			} else {
				recoveryOutputPath = userInterface.getRecoveryOutputPath();
				if (setting == SettingType.COPY_ONLY) {
					new recover.R_Copy();
				}
			}
		} else {
			File[] root = {new File(getDataPath()), new File(getSettingsPath())};
			for (File f : root) {
				f.mkdirs();
			}
			new Copy();
		}
	}
	
	public static String getDataPath() {
		return (rootDestination + "/data");
	}
	
	public static String getSettingsPath() {
		return (rootDestination + "/settings");
	}
	
	public static String getSettingsFilePath() {
		return (getSettingsPath() + "/settings.txt");
	}
	
	public static String getPathListPath() {
		return (getSettingsPath() + "/pathList.txt");
	}
	
	public static String getTimeFilePath() {
		return (getSettingsPath() + "/time.txt");
	}
	
	public static String getPassword() {
		return password;
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
}