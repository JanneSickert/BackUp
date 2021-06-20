package ui;

import enums.SettingType;

public class Source implements interfaces.UI{

	private void p(String text) {
		System.out.println(text);
	}
	
	@Override
	public void showHead() {
		p("BackUp program");
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
		String p = "C:/Users/janne/Desktop/test/backup";
		return p;
	}

	/**
	 *  Type 0 to recover files from BackUp
	 *	Type 1 to update the BackUp
	 */
	@Override
	public boolean updateOrRecover() {
		int nr = 0;
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
	public void move(String from, String to) {
		System.out.println("Move file from:" + from + " to " + to);
	}
}