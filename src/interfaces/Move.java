package interfaces;

import java.io.File;

public interface Move {

	public void move(File from, File to, byte[] fileInBytes);
	
	public void joinAll();
	
	default public void print(File a, File b) {
		main.Main.userInterface.move(a.getAbsolutePath(), b.getAbsolutePath(), a.length());
	}
}