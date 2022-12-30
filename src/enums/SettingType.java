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
	
	public static SettingType getTypeByName(String str) {
		SettingType setting = null;
		if (str.equals(SettingType.COPY_ONLY.toString())) {
			setting = SettingType.COPY_ONLY;
		} else if (str.equals(SettingType.KEY_FILE.toString())) {
			setting = SettingType.KEY_FILE;
		} else if (str.equals(SettingType.PASSWORD.toString())) {
			setting = SettingType.PASSWORD;
		} else if (str.equals(SettingType.PASSWORD_AND_KEY_FILE.toString())) {
			setting = SettingType.PASSWORD_AND_KEY_FILE;
		}
		return setting;
	}
}