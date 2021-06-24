package test;

import java.lang.reflect.Method;
import ui.Gui.MoveGui;

public class Test {

	public static void main(String[] args) {
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

		public abstract void eql();

		String rightOutput;
		String[] input;

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