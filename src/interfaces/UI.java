package interfaces;

import enums.SettingType;

public interface UI {

	public void showHead();
	
	public SettingType getSettings();
	
	public String getSourceRootPath();
	
	public String getDestinationRootPath();
	
	/**
	 * 
	 * @return true if update
	 */
	public boolean updateOrRecover();
	
	public String getPathForKeyFile();
	
	public String getPassword();
	
	public String getRecoveryOutputPath();
	
	public void move(String from, String to);
	
	public void finishMessage();
}