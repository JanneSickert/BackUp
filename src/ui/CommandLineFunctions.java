package ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import annotationen.Comment;

public class CommandLineFunctions {

	private static List<String> log = new ArrayList<String>();
	private static final Scanner sc = new Scanner(System.in);
	
	@Comment(make = "print text and save it in log Array.",
			param = {"String to print"},
			ret = "void")
	public static void pri(String text) {
		System.out.println(text);
		log.add(text);
	}
	
	@Comment(make = "Print Text.",
			param = {"With keyword error the text will be"
					+ " print red, without green."},
					ret = "void"
			)
	public static void p(String text) {
		if (text.contains("ERROR")) {
			pri("\u001B[31m" + text + "\u001B[0m");
		} else if (text.contains("->")) {
			pri(text);
		} else {
			pri("\u001B[32m" + text + "\u001B[0m");
		}
	}
	
	@Comment(make = "Print a String Array")
	public static void pArr(String[] arr) {
		for (String s : arr) {
			p("->" + s);
		}
	}
	
	@Comment(make = "Print a String Array")
	public static void pArr(List<File> arr) {
		for (File f : arr) {
			p("->" + f.getAbsolutePath());
		}
	}
	
	@Comment(ret = "User input from the command line")
	public static String getString() {
		return sc.nextLine();
	}
	
	@Comment(ret = "User input from the command line")
	public static int getInt() {
		return sc.nextInt();
	}
}
