package ui;

import java.util.Scanner;
import enums.SettingType;

public class Cmd implements interfaces.UI{
	
	private void p(String text) {
		System.out.println(text);
	}
	
	@Override
	public void showHead() {
		p("BackUp program");
	}

	@Override
	public SettingType getSettings() {
		@SuppressWarnings("resource")
		final Scanner sc = new Scanner(System.in);
		p("Type 0 to copy only.");
		p("Type 1 to encrypt with a password.");
		p("Type 2 to encrypt with a key file.");
		p("Type 3 to encrypt with key file and password.");
		int nr = sc.nextInt();
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
		@SuppressWarnings("resource")
		final Scanner sc = new Scanner(System.in);
		p("Path for BackUp source:");
		String p = sc.nextLine();
		return p;
	}

	@Override
	public String getDestinationRootPath() {
		@SuppressWarnings("resource")
		final Scanner sc = new Scanner(System.in);
		p("Path for BackUp destination:");
		String p = sc.nextLine();
		return p;
	}

	@Override
	public boolean updateOrRecover() {
		@SuppressWarnings("resource")
		final Scanner sc = new Scanner(System.in);
		p("Type 0 to recover files from BackUp");
		p("Type 1 to update the BackUp");
		int nr = sc.nextInt();
		return (nr == 1);
	}

	@Override
	public String getPathForKeyFile() {
		@SuppressWarnings("resource")
		final Scanner sc = new Scanner(System.in);
		p("Path for key file:");
		String s = sc.nextLine();
		return s;
	}

	@Override
	public String getPassword() {
		@SuppressWarnings("resource")
		final Scanner sc = new Scanner(System.in);
		p("Password:");
		String s = sc.nextLine();
		return s;
	}

	@Override
	public String getRecoveryOutputPath() {
		@SuppressWarnings("resource")
		final Scanner sc = new Scanner(System.in);
		p("Recovery output path::");
		String s = sc.nextLine();
		return s;
	}

	@Override
	public synchronized void move(String from, String to, long lengthFile) {
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
}