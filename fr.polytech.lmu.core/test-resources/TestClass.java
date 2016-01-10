package org.lucci.lmu.test;

public class TestClass extends Test {
	private int i;
	
	Test test = new Test();
	
	public void add (int j) {
		i += j;
	}
	
	public int get() {
		return i;
	}
}
