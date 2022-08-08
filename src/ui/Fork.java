package ui;

import java.lang.reflect.Method;
import java.util.ArrayList;

import comment.Comment;
import comment.Create;
import enums.RecoverOrUpdate;
import enums.SettingType;
import interfaces.UI;
import main.Main.TwoFiles;

public class Fork implements UI {

	private UI[] ui = {new Cmd(), new Gui()};
	
	private enum InOrOut {
		SHOW, ASK
	}
	
	private class ReadAnnotations<T> {
		
		Class<T> type;
		String[] methodNames;
		InOrOut[] inOrOut;
		
		public ReadAnnotations(Class<T> type) {
			this.type = type;
		}
		
		private String[] getMethodNames(Class<T> type) {
			Method[] methoden = type.getDeclaredMethods();
			methodNames = new String[methoden.length];
			inOrOut = new InOrOut[methoden.length];
			int i = 0;
			for (Method m : methoden) {
				methodNames[i] = m.getName();
				Show show = m.getAnnotation(Show.class);
				Ask ask = m.getAnnotation(Ask.class);
				if (ask == null && show != null) {
					// vars setzen
				}
				i++;
			
			return null;
		}
	}
	
	@Override
	public void showHead() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SettingType getSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RecoverOrUpdate updateOrRecover() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSourceRootPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDestinationRootPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPathForKeyFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRecoveryOutputPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void move(String from, String to, long lengthFile) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finishMessage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showLoadingScreen(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeLoadingScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showNotFoundFiles(ArrayList<TwoFiles> list) {
		// TODO Auto-generated method stub
		
	}

}