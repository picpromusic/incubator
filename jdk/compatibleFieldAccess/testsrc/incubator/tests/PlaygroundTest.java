package incubator.tests;

import playground.PlaygroundSubjectToChange;



public class PlaygroundTest {
	public static void main(String[] args) {
		boolean changedVersion = Boolean.getBoolean("cfa.changedVersion");

		String solution = System.getProperty("SolutionList");
		boolean sol2 = solution != null ? solution.contains("2") : false;
		boolean sol1 = solution != null ? solution.contains("1") : false;

		PlaygroundSubjectToChange.sField = 27;
		
		System.out.println(PlaygroundSubjectToChange.sField);
	}

}
