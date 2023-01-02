package test;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import annotationen.EntryPoint;
import ui.CommandLineFunctions;
import interfaces.Collect;
import interfaces.NewOrUpdate;

public class Test extends CommandLineFunctions implements Collect {

	private ArrayList<File> testFiles = new ArrayList<File>();
	private ArrayList<File> testFolders = new ArrayList<File>();
	
	@EntryPoint(
			keys = {"test", "null", "-t"},
			describtion = "Run Tests",
			isMainClass = false
			)
	public static void test() {
		
	}
	
	private <T> void testIO(Class<T> c) {
		Method[] methoden = c.getDeclaredMethods();
		for (Method m : methoden) {
			annotationen.CheckStringIO me = m.getAnnotation(annotationen.CheckStringIO.class);
			if (me != null) {
				String testInput = me.parameter();
				String rightOutput = me.returnValue();
				try {
					String res = (String) m.invoke(testInput);
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
	
	private int[] getRandomNumbers(int max, int anzahl) {
		int[] arr = new int[anzahl];
		List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < max; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
		int index = 0;
        for (int number : numbers) {
            arr[index] = number;
			index++;
        }
		return arr;
	}
	
	private void collectTestFilePaths() {
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
	}
}
