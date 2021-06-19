package main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class CopyUpdate extends Update{

	CopyUpdate() {
		super();
	}
	
	@Override
	protected void move(File from, File to) {
		try {
			Files.copy(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
