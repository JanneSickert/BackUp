package test;

import annotationen.EntryPoint;

public class Test {

	@EntryPoint(
			keys = {"test", "null", "-t"},
			describtion = "Run Tests",
			isMainClass = false
			)
	public static void test() {
		
	}
}
