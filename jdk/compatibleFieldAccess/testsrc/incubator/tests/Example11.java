package incubator.tests;

import example11.SubjectToChange;
import playground.PlaygroundSubjectToChange;



public class Example11 {
	public static void main(String[] args) {
		boolean changedVersion = Boolean.getBoolean("cfa.changedVersion");

		String solution = System.getProperty("SolutionList");
		boolean sol2 = solution != null ? solution.contains("2") : false;
		boolean sol1 = solution != null ? solution.contains("1") : false;

//		for(int i = 0 ; i < 100000; i++) {
		
			SubjectToChange.sField = 27;
			
			SubjectToChange pg = new SubjectToChange();
			pg.field += 5;
			
			
			System.out.println(pg.field) ;
			System.out.println(SubjectToChange.sField);
//		}
	}

}
