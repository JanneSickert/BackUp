package interfaces;

import java.util.ArrayList;
import enums.SettingType;
import enums.RecoverOrUpdate;

public interface UI {

	public void showHead();
	
	public SettingType getSettings();
	
	public RecoverOrUpdate updateOrRecover();
	
	public String getSourceRootPath();
	
	public String getDestinationRootPath();
	
	public String getPathForKeyFile();
	
	public String getPassword();
	
	public String getRecoveryOutputPath();
	
	public void move(String from, String to, long lengthFile);
	
	public void finishMessage();
	
	public void showLoadingScreen(String message);
	
	public void closeLoadingScreen();
	
	public void showNotFoundFiles(ArrayList<main.Main.TwoFiles> list);
}