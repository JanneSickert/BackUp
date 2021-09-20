package interfaces;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import main.Main;

public interface Access {

	default public byte[] makeFileToByteArr(File from, File to) {
		Path path = Paths.get(from.getAbsolutePath());
		try {
			if (from.length() > (long) Integer.MAX_VALUE / 2) {
				Main.addErrorFile(new main.Main.TwoFiles(from, to));
				return null;
			} else {
				return (Files.readAllBytes(path));
			}
		} catch (IOException e) {
			main.Main.addErrorFile(new main.Main.TwoFiles(from, to));
			return null;
		}
	}
}