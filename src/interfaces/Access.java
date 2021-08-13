package interfaces;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import main.Main;

public interface Access {

	default public byte[] makeFileToByteArr(File f) {
		Path path = Paths.get(f.getAbsolutePath());
		try {
			if (f.length() > (long) Integer.MAX_VALUE) {
				Main.addErrorFile(f);
				return null;
			} else {
				return (Files.readAllBytes(path));
			}
		} catch (IOException e) {
			main.Main.addErrorFile(f);
			return null;
		}
	}
}