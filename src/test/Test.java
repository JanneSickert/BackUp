package test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import ui.Gui.MoveGui;
import ui.Gui;

public class Test {

	public static void main(String[] args) {
		// testMoveGui();
		// testLoadingScreen();
		testErrorFiles();
		// testCollectPath();
	}
	
	@SuppressWarnings("unused")
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

	@SuppressWarnings("unused")
	private static void testLoadingScreen() {
        ui.Gui gui = new ui.Gui();
        gui.showLoadingScreen("test message............");
	}

	@SuppressWarnings("unused")
	private static void testMoveGui() {
        AtTest<MoveGui> c;
        c = new Test.AtTest<>() {
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