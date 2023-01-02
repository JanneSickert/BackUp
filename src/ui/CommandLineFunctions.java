package ui;

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
		if (text.contains("ERROR") || text.contains("error")) {
			pri("\u001B[31m" + text + "\u001B[0m");
		} else {
			pri("\u001B[32m" + text + "\u001B[0m");
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
