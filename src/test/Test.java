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

import annotationen.EntryPoint;
import ui.CommandLineFunctions;
import interfaces.Collect;
import interfaces.NewOrUpdate;
import interfaces.Recovery;

public class Test extends CommandLineFunctions implements Collect {

	private List<File> testFiles = new ArrayList<File>();
	private List<File> testFolders = new ArrayList<File>();
	private List<Set<Integer>> indexSetFiles;
	private List<Set<Integer>> indexSetFolders;
	
	@EntryPoint(
			keys = {"TEST", "-t", "--test"},
			describtion = "Run Tests",
			isMainClass = false
			)
	public static void test() {
		p("start test");
		testIO(Collect.class);
		testIO(Recovery.class);
		new Test();
	}
	
	private Test() {
		try {
			collectTestFilePaths();
			createFoldersForTest();
			String rootTestFilesSource = new File("../../").getAbsolutePath();
			String rootTestFilesDestination = new File("testSpace/src").getAbsolutePath();
			for (int setIndex = 0; setIndex < indexSetFiles.size(); setIndex++) {
				int size = indexSetFiles.get(setIndex).size();
				String[] relativPath = new String[size];
				int index = 0;
				Iterator<Integer> iterator = indexSetFiles.get(setIndex).iterator();
				if(iterator.hasNext()){
					relativPath[index] = getRelPath(testFiles.get((int) iterator.next()).getAbsolutePath(), rootTestFilesSource);
					index++;
				}
				for (int i = 0; i < size; i++) {
					try {
						Files.copy(
								new File(rootTestFilesSource + "/" + relativPath[i]).toPath(),
								new File(rootTestFilesDestination + "/" + relativPath[i]).toPath(),
								StandardCopyOption.REPLACE_EXISTING
						);
					} catch (IOException e) {
						p("ERROR: Cannot Copy: " + relativPath[i]);
					}
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
	
	private static <T> void testIO(Class<T> c) {
		Method[] methoden = c.getDeclaredMethods();
		for (Method m : methoden) {
			annotationen.CheckStringIO me = m.getAnnotation(annotationen.CheckStringIO.class);
			if (me != null) {
				String[] testInput = me.parameter();
				String rightOutput = me.returnValue();
				int anzahlParameter = me.anzahlParameter();
				try {
					String res = null;
					switch(anzahlParameter) {
					case 1:
						if (m.invoke(testInput[0]) instanceof String) {
							res = (String) m.invoke(testInput[0]);
						} else {
							p("ERROR: Fehlerhafter Datentyp -> " + m.invoke(testInput[0]).getClass());
						}
						break;
					case 2:
						if (m.invoke(testInput[0], testInput[1]) instanceof String) {
							res = (String) m.invoke(testInput[0], testInput[1]);
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
        int from = 0;
        for (int zahl : anzahl) {
        	Set<Integer> set = new HashSet<Integer>();
        	Stream.iterate(from, i -> i + 1).limit(from + zahl - 1).forEach(new Consumer<Integer>() {
        		  @Override
        		  public void accept(Integer index) {
        			  set.add(numbers.get(index));
        		  }
        	});
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
		collectFiles(new File("../../"), nou);
		int size = testFiles.size();
		indexSetFiles = getRandomNumbers(size, size * 0.5, size * 0.3, size * 0.2);
		size = testFolders.size();
		indexSetFolders = getRandomNumbers(size, size * 0.5, size * 0.3, size * 0.2);
	}
}
