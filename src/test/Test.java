package test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import annotationen.CheckStringIO;
import annotationen.EntryPoint;
import ui.CommandLineFunctions;
import interfaces.Collect;
import interfaces.NewOrUpdate;
import interfaces.Recovery;

public class Test extends CommandLineFunctions implements Collect, Recovery {

	private static final String DUMMY_DATEN = "/home/janne/httrack";
	
	private List<File> testFiles = new ArrayList<File>();
	private List<File> testFolders = new ArrayList<File>();
	private List<Set<Integer>> indexSetFiles;
	private List<Set<Integer>> indexSetFolders;
	
	@EntryPoint(
			keys = {"null", "-t", "--test"},
			describtion = "Run Tests",
			isMainClass = false
			)
	public static void test() {
		p("----- start test -----");
		testIO(Collect.class, new Collect() {});
		testIO(Recovery.class, new Recovery() {});
		new Test();
	}
	
	private Test() {
		try {
			collectTestFilePaths();
			createFoldersForTest();
			String rootTestFilesSource = new File(DUMMY_DATEN).getAbsolutePath();
			String rootTestFilesDestination = new File("testSpace/src").getAbsolutePath();
			main.Main.userInterface = new ui.Cmd();
			for (int setIndex = 0; setIndex < indexSetFiles.size(); setIndex++) {
				int size = indexSetFiles.get(setIndex).size();
				String[] relativPath = new String[size];
				int index = 0;
				Iterator<Integer> iterator = indexSetFiles.get(setIndex).iterator();
				while (iterator.hasNext()){
					relativPath[index] = getRelPath(testFiles.get((int) iterator.next()).getAbsolutePath(), rootTestFilesSource);
					index++;
				}
				for (int i = 0; i < size; i++) {
					new File(getFolderPath(rootTestFilesDestination + "/" + relativPath[i])).mkdirs();
				}
				for (int i = 0; i < size; i++) {
					main.Main.copy.move(new File(rootTestFilesSource + "/" + relativPath[i]),
								        new File(rootTestFilesDestination + "/" + relativPath[i]),
								        null
					);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void createFoldersForTest() {
		new File("testSpace").mkdir();
		new File("testSpace/src").mkdir();
		new File("testSpace/des").mkdir();
		new File("testSpace/rec").mkdir();
	}
	
	private static <T> void testIO(Class<T> c, Object objFromClass) {
		Method[] methoden = c.getDeclaredMethods();
		for (Method m : methoden) {
			CheckStringIO me = m.getAnnotation(CheckStringIO.class);
			if (me != null) {
				String[] testInput = me.parameter();
				String rightOutput = me.returnValue();
				int anzahlParameter = me.anzahlParameter();
				try {
					String res = null;
					switch(anzahlParameter) {
					case 1:
						if (m.invoke(objFromClass, testInput[0]) instanceof String) {
							res = (String) m.invoke(objFromClass, testInput[0]);
						} else {
							p("ERROR: Fehlerhafter Datentyp -> " + m.invoke(testInput[0]).getClass());
						}
						break;
					case 2:
						if (m.invoke(objFromClass, testInput[0], testInput[1]) instanceof String) {
							res = (String) m.invoke(objFromClass, testInput[0], testInput[1]);
						} else {
							p("ERROR: Fehlerhafter Datentyp -> " + m.invoke(testInput[0], testInput[1]).getClass());
						}
					}
					if (res.equals(rightOutput)) {
						p(m.getName() + " have same output");
					} else {
						p("ERROR:" + m.getName() + " have: " + res + " right output: " + rightOutput);
					}
				} catch (IllegalAccessException | 
						IllegalArgumentException | 
						InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private List<Set<Integer>> getRandomNumbers(int max, double... pAnzahl) {
		int[] anzahl = new int[pAnzahl.length];
		for (int i = 0; i < pAnzahl.length; i++) {
			anzahl[i] = (int) pAnzahl[i];
		}
		int sum = Arrays.stream(anzahl).sum();
		if (sum > max) {
			throw new ArithmeticException("Summe von anzahl != max");
		}
		List<Set<Integer>> list = new ArrayList<Set<Integer>>();
		List<Integer> numbers = new ArrayList<Integer>();
        for (int i = 0; i < max; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        int from = 0, i;
        for (int zahl : anzahl) {
        	Set<Integer> set = new HashSet<Integer>();
        	for (i = from; i < from + zahl - 1; i++) {
        		set.add(numbers.get(i));
        	}
        	list.add(set);
        	from += zahl;
        }
        return list;
	}
	
	private void collectTestFilePaths() throws IOException {
		NewOrUpdate nou = new NewOrUpdate() {
			@Override
			public void doWithFile(File f) {
				testFiles.add(f);
			}
			@Override
			public void doWithEmptyFolder(File f) {
				testFolders.add(f);
			}
		};
		collectFiles(new File(DUMMY_DATEN), nou);
		pArr(testFiles);
		int size = testFiles.size();
		indexSetFiles = getRandomNumbers(size, size * 0.5, size * 0.3, size * 0.2);
		size = testFolders.size();
		indexSetFolders = getRandomNumbers(size, size * 0.5, size * 0.3, size * 0.2);
	}
}
