package interfaces;

import java.io.File;

public interface Move {

	public void move(File from, File to);
	
	public void joinAll();
	
	default public void print(File a, File b) {
		main.Main.userInterface.move(a.getAbsolutePath(), b.getAbsolutePath());
	}
}