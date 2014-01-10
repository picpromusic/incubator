package incubator.tests;

import example1.SubjectToChange;

public class Example1 {
	public static void main(String[] args) {
		SubjectToChange stc = new SubjectToChange(5);
		System.out.println(++stc.publicField);
		System.out.println(++SubjectToChange.publicStaticField);
		System.out.println(Integer.parseInt("CAFE",16));
		System.out.println(Integer.parseInt("BABE",16));
	}
}
