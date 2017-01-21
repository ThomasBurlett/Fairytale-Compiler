import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;
import org.junit.Test;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Phase3_Testing {

	private static ArrayList<String> tests = new ArrayList<>();
	
	/*
	 * 		For Loops
	 */
	
	@Test
	public void test_FORLOOP_00() {
		System.out.println("This should pass.");
		System.out.println("test_FORLOOP_00.txt");
		System.out.println("");
		
		Parser.main("test_FORLOOP_00.txt");
		tests.add("test_FORLOOP_00.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test_FORLOOP_01() {
		System.out.println("This should pass.");
		System.out.println("test_FORLOOP_01.txt");
		System.out.println("");
		
		Parser.main("test_FORLOOP_01.txt");
		tests.add("test_FORLOOP_01.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test_FORLOOP_02() {
		System.out.println("This should pass.");
		System.out.println("test_FORLOOP_02.txt");
		System.out.println("");
		
		Parser.main("test_FORLOOP_02.txt");
		tests.add("test_FORLOOP_02.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test_FORLOOP_03() {
		System.out.println("This should pass.");
		System.out.println("test_FORLOOP_03.txt");
		System.out.println("");
		
		Parser.main("test_FORLOOP_03.txt");
		tests.add("test_FORLOOP_03.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}

	
	//	For Loop Errors
	
	@Test
	public void test_FORLOOP_04E() {
		System.out.println("This should FAIL.");
		System.out.println("test_FORLOOP_04E.txt");
		System.out.println("");

		Parser.main("test_FORLOOP_04E.txt");
		System.setOut(Parser.stdout);
				
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test_FORLOOP_05E() {
		System.out.println("This should FAIL.");
		System.out.println("test_FORLOOP_05E.txt");
		System.out.println("");

		Parser.main("test_FORLOOP_05E.txt");
		System.setOut(Parser.stdout);
				
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	
	/*
	 * If Statements
	 */
	
	@Test
	public void test_IF_00() {
		System.out.println("This should pass.");
		System.out.println("test_IF_00.txt");
		System.out.println("");
		
		Parser.main("test_IF_00.txt");
		tests.add("test_IF_00.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	
	@Test
	public void test_IF_01() {
		System.out.println("This should pass.");
		System.out.println("test_IF_01.txt");
		System.out.println("");
		
		Parser.main("test_IF_01.txt");
		tests.add("test_IF_01.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test_IF_02() {
		System.out.println("This should pass.");
		System.out.println("test_IF_02.txt");
		System.out.println("");
		
		Parser.main("test_IF_02.txt");
		tests.add("test_IF_02.txt");

		System.out.println("\n\n\n-------------------------------------------");
	}
		
	@Test
	public void test_IF_03() {
		System.out.println("This should pass.");
		System.out.println("test_IF_03.txt");
		System.out.println("");
		
		Parser.main("test_IF_03.txt");
		tests.add("test_IF_03.txt");

		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test_IF_04() {
		System.out.println("This should pass.");
		System.out.println("test_IF_04.txt");
		System.out.println("");
		
		Parser.main("test_IF_04.txt");
		tests.add("test_IF_04.txt");

		System.out.println("\n\n\n-------------------------------------------");
	}
	
	// IF Error Cases
	
	@Test
	public void test_IF_05E() {
		System.out.println("This should pass.");
		System.out.println("test_IF_05E.txt");
		System.out.println("");
		
		Parser.main("test_IF_05E.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test_IF_06E() {
		System.out.println("This should pass.");
		System.out.println("test_IF_06E.txt");
		System.out.println("");
		
		Parser.main("test_IF_06E.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	
	// IF ELSE STATEMENTS
	
	@Test
	public void test_IFELSE_00() {
		System.out.println("This should pass.");
		System.out.println("test_if_01.txt");
		System.out.println("");
		
		Parser.main("test_IFELSE_00.txt");
		tests.add("test_IFELSE_00.txt");

		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test_IFELSE_01() {
		System.out.println("This should pass.");
		System.out.println("test_IFELSE_01.txt");
		System.out.println("");
		
		Parser.main("test_IFELSE_01.txt");
		tests.add("test_IFELSE_01.txt");

		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test_IFELSE_02() {
		System.out.println("This should pass.");
		System.out.println("test_IFELSE_02.txt");
		System.out.println("");
		
		Parser.main("test_IFELSE_02.txt");
		tests.add("test_IFELSE_02.txt");

		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test_IFELSE_03() {
		System.out.println("This should pass.");
		System.out.println("test_IFELSE_03.txt");
		System.out.println("");
		
		Parser.main("test_IFELSE_03.txt");
		tests.add("test_IFELSE_03.txt");

		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test_IFELSE_04() {
		System.out.println("This should pass.");
		System.out.println("test_IFELSE_04.txt");
		System.out.println("");
		
		Parser.main("test_IFELSE_04.txt");
		tests.add("test_IFELSE_04.txt");

		System.out.println("\n\n\n-------------------------------------------");
	}
	
	
		
		/*
		 * While Loops
		 */
		
		@Test
		public void test_WHILE_00() {
			System.out.println("This should pass.");
			System.out.println("test_WHILE_00.txt");
			System.out.println("");
			
			Parser.main("test_WHILE_00.txt");
			tests.add("test_WHILE_00.txt");

			System.out.println("\n\n\n-------------------------------------------");
		}
		
		@Test
		public void test_WHILE_01() {
			System.out.println("This should pass.");
			System.out.println("test_WHILE_01.txt");
			System.out.println("");
			
			Parser.main("test_WHILE_01.txt");
			tests.add("test_WHILE_01.txt");

			System.out.println("\n\n\n-------------------------------------------");
		}
		
		@Test
		public void test_WHILE_02() {
			System.out.println("This should pass.");
			System.out.println("test_WHILE_02.txt");
			System.out.println("");
			
			Parser.main("test_WHILE_02.txt");
			tests.add("test_WHILE_02.txt");

			System.out.println("\n\n\n-------------------------------------------");
		}
		
		@Test
		public void test_WHILE_03() {
			System.out.println("This should pass.");
			System.out.println("test_WHILE_03.txt");
			System.out.println("");
			
			Parser.main("test_WHILE_03.txt");
			tests.add("test_WHILE_03.txt");

			System.out.println("\n\n\n-------------------------------------------");
		}
		
		@Test
		public void test_WHILE_04() {
			System.out.println("This should pass.");
			System.out.println("test_WHILE_04.txt");
			System.out.println("");
			
			Parser.main("test_WHILE_04.txt");
			tests.add("test_WHILE_04.txt");

			System.out.println("\n\n\n-------------------------------------------");
		}
		
		@Test
		public void test_WHILE_05() {
			System.out.println("This should pass.");
			System.out.println("test_WHILE_05.txt");
			System.out.println("");
			
			Parser.main("test_WHILE_05.txt");
			tests.add("test_WHILE_05.txt");

			System.out.println("\n\n\n-------------------------------------------");
		}
		
		@Test
		public void test_WHILE_06() {
			System.out.println("This should pass.");
			System.out.println("test_WHILE_06.txt");
			System.out.println("");
			
			Parser.main("test_WHILE_06.txt");
			tests.add("test_WHILE_06.txt");

			System.out.println("\n\n\n-------------------------------------------");
		}
		
		// While Loop Errors
		
		@Test
		public void test_WHILE_07E() {
			System.out.println("This should FAIL.");
			System.out.println("test_WHILE_07E.txt");
			System.out.println("");

		
				Parser.main("test_WHILE_07E.txt");
				System.setOut(Parser.stdout);
			
			System.out.println("\n\n\n-------------------------------------------");
		}

		
		// Complicated Goodness
		
		@Test
		public void test_Complex_IF() {
			System.out.println("This should pass.");
			System.out.println("test_Complex_IF.txt");
			System.out.println("");
		
			Parser.main("test_Complex_IF.txt");
			tests.add("test_Complex_IF.txt");

			System.out.println("\n\n\n-------------------------------------------");
		}
}
