package main;

import java.util.ArrayList;

/**
 * this class contains the attributes of the interfaces.
 * @author janne
 *
 */
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
