package recover;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class R_Copy implements interfaces.Recovery{

	public R_Copy() {
		start();
	}
	
	@Override
	public void move(File from, File to) {
		try {
			Files.copy(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
