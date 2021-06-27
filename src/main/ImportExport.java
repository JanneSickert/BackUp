package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ImportExport {

	private File f;

	ImportExport(File f) {
		this.f = f;
	}

	String inport() {
		String line = null;
		FileReader fr;
		try {
			fr = new FileReader(f.getAbsolutePath());
			BufferedReader br = new BufferedReader(fr);
			line = br.readLine();
			br.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return line;
	}
	
	void export(String text) {
		if (!(f.exists())) {
			try {// create file
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}// write file
		try {
			FileWriter fwf = new FileWriter(f, false);
			fwf.write(text);
			fwf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}