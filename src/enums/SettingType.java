package enums;

public enum SettingType {

	COPY_ONLY,
	PASSWORD,
	KEY_FILE,
	PASSWORD_AND_KEY_FILE;
	
	public String getName() {
		String s = null;
		switch(this) {
		case COPY_ONLY:
			s = "Copy only";
			break;
		case PASSWORD:
			s = "Password";
			break;
		case KEY_FILE:
			s = "Key file";
			break;
		case PASSWORD_AND_KEY_FILE:
			s = "Password and key file";
			break;
		}
		return s;
	}
}