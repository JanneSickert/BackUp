package test;

import java.util.ArrayList;

import enums.RecoverOrUpdate;
import enums.SettingType;

public class Source implements interfaces.UI{

	private void p(String text) {
		System.out.println(text);
	}
	
	@Override
	public void showHead() {
		p("BackUp program");
		p("Is in developing!");
	}

	@Override
	public SettingType getSettings() {
		return (SettingType.PASSWORD_AND_KEY_FILE);
	}

	@Override
	public String getSourceRootPath() {
		String p = "C:/Users/janne/Desktop/test/src";
		return p;
	}

	@Override
	public String getDestinationRootPath() {
		String p = "C:/Users/janne/Desktop/test/des";
		return p;
	}

	@Override
	public RecoverOrUpdate updateOrRecover() {
		return RecoverOrUpdate.UPDATE;
	}

	@Override
	public String getPathForKeyFile() {
		String s = "C:/Users/janne/Desktop/test/keyFile.txt";
		return s;
	}

	@Override
	public String getPassword() {
		String s = "#myPassword123";
		return s;
	}

	@Override
	public String getRecoveryOutputPath() {
		String s = "C:/Users/janne/Desktop/test/recovery";
		return s;
	}

	@Override
	public void move(String from, String to, long lengthFile) {
		System.out.println("Move file from:" + from + " to " + to);
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
		p("------------------------------------------------------");
		for (main.Main.TwoFiles f : list) {
			p("missing file: " + f.from.getAbsolutePath());
		}
	}
}