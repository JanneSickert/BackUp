package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDate {

	public static class TimeAndDate {

		int houer;
		int minutes;
		int sec;
		int day;
		int month;
		int year;

		public boolean isBefore(TimeAndDate tad) {
			if (this.year < tad.year) {
				return true;
			} else if (this.year > tad.year) {
				return false;
			} else {
				if (this.month < tad.month) {
					return true;
				} else if (this.month > tad.month) {
					return false;
				} else {
					if (this.day < tad.day) {
						return true;
					} else if (this.day > tad.day) {
						return false;
					} else {
						if (this.houer < tad.houer) {
							return true;
						} else if (this.houer > tad.houer) {
							return false;
						} else {
							if (this.minutes < tad.minutes) {
								return true;
							} else if (this.minutes > tad.minutes) {
								return false;
							} else {
								if (this.sec < tad.sec) {
									return true;
								} else {
									return false;
								}
							}
						}
					}
				}
			}
		}
	}

	public static class CurrentDate {

		public String getTimePoint() {
			String time = getTime() + "-" + getDate();
			return time;
		}

		public String getDate() {
			SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
			Date currentTime = new Date();
			String d = formatter.format(currentTime);
			return d;
		}

		public String getTime() {
			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
			Date currentTime = new Date();
			String d = formatter.format(currentTime);
			return d;
		}
	}

	public static class BackUpTime {

		/**
		 * example output: 00:48:34-15.06.2021
		 * 
		 * @param path
		 */
		public static void createTimeFile(String path) {
			CurrentDate currentDate = new CurrentDate();
			String text = currentDate.getTimePoint();
			File ff = new File(path);
			if (ff.exists()) {
				ff.delete();
			}
			if (!ff.exists()) {
				try {
					ff.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					FileWriter fwf = new FileWriter(ff, false);
					fwf.write(text);
					fwf.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		public static TimeAndDate getTimeFile(String path) {
			TimeAndDate t = new TimeAndDate();
			String text = "";
			FileReader fr;
			try {
				fr = new FileReader(path);
				BufferedReader br = new BufferedReader(fr);
				String zeile = "";
				while ((zeile = br.readLine()) != null) {
					text = text + zeile;
				}
				br.close();
			} catch (FileNotFoundException e1) {
				System.exit(0);
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(44);
			}
			String[] next = split(text, '-');
			t.houer = (int) Integer.parseInt(next[0].substring(0, 2));
			t.minutes = (int) Integer.parseInt(next[0].substring(3, 5));
			t.sec = (int) Integer.parseInt(next[0].substring(6, 8));
			t.day = (int) Integer.parseInt(next[1].substring(0, 2));
			t.month = (int) Integer.parseInt(next[1].substring(3, 5));
			t.year = (int) Integer.parseInt(next[1].substring(6));
			return t;
		}

		/**
		 * 
		 * @param str       zu teilende String
		 * @param delimiter nur ein char
		 * @return Array mit zwei feldern
		 * 
		 *         vor und nach dem delimiter. Delimmmiter selbst geht verloren
		 * 
		 */
		public static String[] split(String str, char delimiter) {
			String[] arr = new String[2];
			int d = firstIndexOff(str, delimiter);
			char[] vorn = new char[d];
			char[] hint = new char[str.length() - d - 1];
			int co = 0;
			for (int i = 0; i < vorn.length; i++) {
				vorn[i] = str.charAt(co);
				co++;
			}
			co++;
			for (int k = 0; k < hint.length; k++) {
				hint[k] = str.charAt(co);
				co++;
			}
			arr[0] = new String(vorn);
			arr[1] = new String(hint);
			return arr;
		}

		private static int firstIndexOff(String str, char delimiter) {
			int in = -1;
			for (int i = 0; i < str.length(); i++) {
				if (str.charAt(i) == delimiter) {
					in = i;
					break;
				}
			}
			return in;
		}
	}

	public static MyDate.TimeAndDate getFileDate(FileTime time) {
		String s = time.toString();// 2021-06-14T21:51:22.4115736Z
		MyDate.TimeAndDate tad = new MyDate.TimeAndDate();
		tad.year = (int) Integer.parseInt(s.substring(0, 4));
		tad.month = (int) Integer.parseInt(s.substring(5, 7));
		tad.day = (int) Integer.parseInt(s.substring(8, 10));
		tad.houer = (int) Integer.parseInt(s.substring(11, 13)) + 2;
		tad.minutes = (int) Integer.parseInt(s.substring(14, 16));
		tad.sec = (int) Integer.parseInt(s.substring(17, 19));
		return tad;
	}
}