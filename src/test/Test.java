package test;

import java.lang.reflect.Method;

public class Test {

	public static void main(String[] args) {
        AtTest<main.NewBackUp> c;
        c = new Test.AtTest<>() {
        	@Override
        	public void eql() {
        		String realOutput = new main.NewBackUp().getRelPath(input[0], input[1]);
        		if (realOutput.equals(rightOutput)) {
        			System.out.println("Test complete");
        		} else {
        			System.out.println("Test failed");
        		}
        		System.out.println(realOutput);
        		System.out.println(rightOutput);
        	}
        };
        c.doTest(main.NewBackUp.class);
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