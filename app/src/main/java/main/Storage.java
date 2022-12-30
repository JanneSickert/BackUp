package main;

import java.util.ArrayList;
import annotationen.ClassComment;

@ClassComment(author = "Janne", version = {3, 0, 0},
comment = "this class contains the attributes of the interfaces.")
public class Storage {

	public static class Collect{
		public static String[] relPath;
		public static String[] relEmptyFolderPath;
		public static ArrayList<String> srcPath;
		public static ArrayList<String> absolutEmptyFolderSourcePath;
	}
	
	public static class Update{
		public static ArrayList<String> absolutSourcePath;
		public static ArrayList<String> absolutEmptyFolderSourcePath;
		public static ArrayList<String> relDestinationPath;
		public static String[] relSourcePath;
		public static String[] relEmptyFolderSourcePath;
	}
}
