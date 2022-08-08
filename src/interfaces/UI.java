package interfaces;

import java.util.ArrayList;
import enums.SettingType;
import enums.RecoverOrUpdate;
import ui.Show;
import ui.Ask;

public interface UI {

	@Show
	public void showHead();
	
	@Ask
	public SettingType getSettings();
	
	@Ask
	public RecoverOrUpdate updateOrRecover();
	
	@Ask
	public String getSourceRootPath();
	
	@Ask
	public String getDestinationRootPath();
	
	@Ask
	public String getPathForKeyFile();
	
	@Ask
	public String getPassword();
	
	@Ask
	public String getRecoveryOutputPath();
	
	@Show
	public void move(String from, String to, long lengthFile);
	
	@Show
	public void finishMessage();
	
	@Show
	public void showLoadingScreen(String message);
	
	@Show
	public void closeLoadingScreen();
	
	@Show
	public void showNotFoundFiles(ArrayList<main.Main.TwoFiles> list);
}