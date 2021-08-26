package ui;

import java.io.File;
import java.util.ArrayList;
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
		return (SettingType.COPY_ONLY);
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

	/**
	 *  Type 0 to recover files from BackUp
	 *	Type 1 to update the BackUp
	 */
	@Override
	public boolean updateOrRecover() {
		int nr = 1;// type here!
		return (nr == 1);
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
	public void showNotFoundFiles(ArrayList<File> list) {
		for (File f : list) {
			p("missing file: " + f.getAbsolutePath());
		}
	}
}