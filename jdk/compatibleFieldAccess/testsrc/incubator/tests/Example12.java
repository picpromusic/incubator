package incubator.tests;

import example12.SubjectToChange;



public class Example12 {
	public static void main(String[] args) {
		boolean changedVersion = Boolean.getBoolean("cfa.changedVersion");

		String solution = System.getProperty("SolutionList");
		boolean sol2 = solution != null ? solution.contains("2") : false;
		boolean sol1 = solution != null ? solution.contains("1") : false;

		SubjectToChange.sField = 27;
		
		System.out.println(SubjectToChange.sField);
	}

}
