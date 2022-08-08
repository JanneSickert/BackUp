package test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import ui.Gui.MoveGui;
import ui.Gui;
import main.LargeCryptoThread;

public class Test {

	public static void main(String[] args) {
		final int TEST_NR = 4;
		switch (TEST_NR) {
		case 0:
			testMoveGui();
			break;
		case 1:
			testLoadingScreen();
			break;
		case 2:
			testErrorFiles();
			break;
		case 3:
			testCollectPath();
			break;
		case 4:
			dynamicTest();
			break;
		case 5:
			testLargeCryptoThread();
			break;
		}
	}
	
	private static void testLargeCryptoThread() {
		final String FILE_NAME = "f0_.js", PATH = "C:/Users/janne/Desktop/test/src";
		String src = PATH + "/" + FILE_NAME, des = "C:/Users/janne/Desktop/test/" + "test2.js";
		main.Main.userInterface = new test.Source();
		LargeCryptoThread lct = new LargeCryptoThread(
				new main.Main.TwoFiles(new File(src), new File(des)),
				new byte[] {'1', '2', '3', '4'},
				main.Main.plus,
				true,
				0,
				""
				);
		lct.start();
		try {
			lct.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("finish");
	}

	private static void dynamicTest() {
		main.Main.userInterface = new test.Source();
		main.Main.start();
		main.Main.start();
	}
	
	private static void testCollectPath() {
		try {
			new interfaces.Collect() {
			}.collectFiles(
					new File("C:/Users/janne/Desktop/test/src"),
					new interfaces.NewOrUpdate() {
						@Override
						public void doWithFile(File f) {
							System.out.println("File:" + f.getAbsolutePath());
						}
						@Override
						public void doWithEmptyFolder(File f) {
							System.out.println("Folder:" + f.getAbsolutePath());
						}
					});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void testErrorFiles() {
		ArrayList<main.Main.TwoFiles> list = new ArrayList<main.Main.TwoFiles>();
		for (int i = 0; i < 50; i++) {
			list.add(new main.Main.TwoFiles(new File("C:/files/" + i), null));
		}
		new Gui().showNotFoundFiles(list);
	}

	private static void testLoadingScreen() {
        ui.Gui gui = new ui.Gui();
        gui.showLoadingScreen("test message............");
	}

	private static void testMoveGui() {
        AtTest<MoveGui> c;
        c = new Test.AtTest<MoveGui>() {
        	@Override
        	public void eql() {
        		MoveGui mg = new ui.Gui().new MoveGui();
        		mg.setText(input[0], input[1]);
        	}
        };
        c.doTest(MoveGui.class);
	}

	public static abstract class AtTest<T>{

		String rightOutput;
		String[] input;

		public abstract void eql();

		public void doTest(Class<T> Typ) {
			Method[] methoden = Typ.getDeclaredMethods();
			for (Method m : methoden) {
				Check_IO c = m.getAnnotation(Check_IO.class);
				if (c != null) {
					rightOutput = c.rightOutput();
					input = c.input();
					eql();
				}
			}
		}
	}
}