package ui;

import java.util.ArrayList;
import enums.RecoverOrUpdate;
import enums.SettingType;

public class Cmd extends ui.CommandLineFunctions implements interfaces.UI {

	@Override
	public void showHead() {
		p("BackUp program");
	}

	@Override
	public SettingType getSettings() {
		p("Type 0 to copy only.");
		p("Type 1 to encrypt with a password.");
		p("Type 2 to encrypt with a key file.");
		p("Type 3 to encrypt with key file and password.");
		int nr = getInt();
		SettingType type = null;
		switch (nr) {
		case 0:
			type = SettingType.COPY_ONLY;
			break;
		case 1:
			type = SettingType.PASSWORD;
			break;
		case 2:
			type = SettingType.KEY_FILE;
			break;
		case 3:
			type = SettingType.PASSWORD_AND_KEY_FILE;
			break;
		default:
			p("ERROR: invalid user input!");
			System.exit(0);
		}
		return type;
	}

	@Override
	public String getSourceRootPath() {
		try {
			p("Path for BackUp source:");
			String p = getString();
			return p;
		} catch (Exception e) {
		    e.getCause().printStackTrace();
		    return null;
		}
	}

	@Override
	public String getDestinationRootPath() {
		p("Path for BackUp destination:");
		String p = getString();
		return p;
	}

	@Override
	public RecoverOrUpdate updateOrRecover() {
		p("Type 0 to recover files from BackUp");
		p("Type 1 to update the BackUp");
		int nr = getInt();
		if (nr == 0) {
			return RecoverOrUpdate.RECOVER;
		} else {
			return RecoverOrUpdate.UPDATE;
		}
	}

	@Override
	public String getPathForKeyFile() {
		p("Path for key file:");
		String s = getString();
		return s;
	}

	@Override
	public String getPassword() {
		p("Password:");
		String s = getString();
		return s;
	}

	@Override
	public String getRecoveryOutputPath() {
		p("Recovery output path::");
		String s = getString();
		return s;
	}

	@Override
	public synchronized void move(String from, String to, long lengthFile) {
		p("Move file from:" + from + " to " + to);
	}

	@Override
	public void finishMessage() {
		p("finish");
	}

	@Override
	public void showLoadingScreen(String message) {
		p(message);
	}

	@Override
	public void closeLoadingScreen() {
		p("finish loading");
	}

	@Override
	public void showNotFoundFiles(ArrayList<main.Main.TwoFiles> list) {
		for (main.Main.TwoFiles f : list) {
			p("missing file: " + f.from.getAbsolutePath());
		}
	}
}