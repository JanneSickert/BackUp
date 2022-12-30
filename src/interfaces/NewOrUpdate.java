package interfaces;

import java.io.File;

public interface NewOrUpdate {

	public void doWithFile(File f);
	
	public void doWithEmptyFolder(File f);
}