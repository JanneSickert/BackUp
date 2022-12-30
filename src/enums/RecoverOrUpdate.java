package enums;

public enum RecoverOrUpdate {

	RECOVER,
	UPDATE;
	
	public String getName() {
		String s = null;
		switch (this) {
		case RECOVER:
			s = "Recover";
			break;
		case UPDATE:
			s = "Update";
			break;
		}
		return s;
	}
}