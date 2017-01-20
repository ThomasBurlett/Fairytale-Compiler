import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;
import org.junit.Test;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Phase3_Testing {

	private static ArrayList<String> tests = new ArrayList<>();

	/*
	@Test
	public void test_ex1() {
		CodeFactory.firstWrite = true;
		Parser.main("filename.txt");
		tests.add("filename");

		System.out.println("This will print to the console.");
	}
	*/
	
	/*
	 * For Loops
	 */
	
	@Test
	public void test_for_00() {
		System.out.println("This should pass.");
		System.out.println("test_for_00.txt");
		System.out.println("");
		
		Parser.main("test_for_00.txt");
		tests.add("test_for_00.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test_for_01() {
		System.out.println("This should pass.");
		System.out.println("test_for_01.txt");
		System.out.println("");
		
		Parser.main("test_for_01.txt");
		tests.add("test_for_01.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test_for_02() {
		System.out.println("This should pass.");
		System.out.println("test_for_02.txt");
		System.out.println("");
		
		Parser.main("test_for_02.txt");
		tests.add("test_for_02.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test_for_03() {
		System.out.println("This should pass.");
		System.out.println("test_for_03.txt");
		System.out.println("");
		
		Parser.main("test_for_03.txt");
		tests.add("test_for_03.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}

	
	//For Loop Errors
	@Test
	public void test_for_04_error() {
		System.out.println("This should FAIL.");
		System.out.println("test_for_04_error.txt");
		System.out.println("");

		try {
			Parser.main("test_for_04_error.txt");
			fail(); // FAIL when no exception is thrown
		} catch (IndexOutOfBoundsException e) {
			assert(true);
			System.setOut(Parser.stdout);
		}
				
		// System.setOut(Parser.stdout);
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	/*
	 * If Statements
	 */
	
	@Test
	public void test_if_00() {
		System.out.println("This should pass.");
		System.out.println("test_if_00.txt");
		System.out.println("");
		
		Parser.main("test_if_00.txt");
		tests.add("test_if_00.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	
	@Test
	public void test_if_02() {
		System.out.println("This should pass.");
		System.out.println("test_if_02.txt");
		System.out.println("");
		
		Parser.main("test_if_02.txt");
		tests.add("test_if_02.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test_if_03() {
		System.out.println("This should pass.");
		System.out.println("test_if_03.txt");
		System.out.println("");
		
		Parser.main("test_if_03.txt");
		tests.add("test_if_03.txt");

		System.out.println("\n\n\n-------------------------------------------");
	}
		
	@Test
	public void test_if_04() {
		System.out.println("This should pass.");
		System.out.println("test_if_04.txt");
		System.out.println("");
		
		Parser.main("test_if_04.txt");
		tests.add("test_if_04.txt");

		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test_if_05() {
		System.out.println("This should pass.");
		System.out.println("test_if_05.txt");
		System.out.println("");
		
		Parser.main("test_if_05.txt");
		tests.add("test_if_05.txt");

		System.out.println("\n\n\n-------------------------------------------");
	}
	
//	@Test
//	public void test_if_06() {
//		System.out.println("This should pass.");
//		System.out.println("test_if_06.txt");
//		System.out.println("");
//		
//		Parser.main("test_if_06.txt");
//		
//		System.out.println("\n\n\n-------------------------------------------");
//	}
//	
	
	@Test
	public void test_ifelse_00() {
		System.out.println("This should pass.");
		System.out.println("test_if_01.txt");
		System.out.println("");
		
		Parser.main("test_ifelse_00.txt");
		tests.add("test_ifelse_00.txt");

		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test_ifelse_06() {
		System.out.println("This should pass.");
		System.out.println("test_if_01.txt");
		System.out.println("");
		
		Parser.main("test_ifelse_06.txt");
		tests.add("test_ifelse_06.txt");

		System.out.println("\n\n\n-------------------------------------------");
	}
	
	//If Statement Errors
		@Test
		public void test_if_07_error() {
			System.out.println("This should FAIL.");
			System.out.println("test_if_07_error.txt");
			System.out.println("");

			try {
			Parser.main("test_if_07_error.txt");
			fail();
			} catch(NullPointerException e) {
				assert(true);
				System.setOut(Parser.stdout);
			}
			
			System.out.println("\n\n\n-------------------------------------------");
		}
		
		@Test
		public void test_if_08_error() {
			System.out.println("This should FAIL.");
			System.out.println("test_if_08_error.txt");
			System.out.println("");

			Parser.main("test_if_08_error.txt");
			
			System.out.println("\n\n\n-------------------------------------------");
		}
		
		/*
		 * While Loops
		 */
		
		@Test
		public void test_while_00() {
			System.out.println("This should pass.");
			System.out.println("test_while_00.txt");
			System.out.println("");
			
			Parser.main("test_while_00.txt");
			tests.add("test_while_00.txt");

			System.out.println("\n\n\n-------------------------------------------");
		}
		
		@Test
		public void test_while_01() {
			System.out.println("This should pass.");
			System.out.println("test_while_01.txt");
			System.out.println("");
			
			Parser.main("test_while_01.txt");
			tests.add("test_while_01.txt");

			System.out.println("\n\n\n-------------------------------------------");
		}
		
		@Test
		public void test_while_02() {
			System.out.println("This should pass.");
			System.out.println("test_while_02.txt");
			System.out.println("");
			
			Parser.main("test_while_02.txt");
			tests.add("test_while_02.txt");

			System.out.println("\n\n\n-------------------------------------------");
		}
		
		@Test
		public void test_while_03() {
			System.out.println("This should pass.");
			System.out.println("test_while_03.txt");
			System.out.println("");
			
			Parser.main("test_while_03.txt");
			tests.add("test_while_03.txt");

			System.out.println("\n\n\n-------------------------------------------");
		}
		
		@Test
		public void test_while_04() {
			System.out.println("This should pass.");
			System.out.println("test_while_04.txt");
			System.out.println("");
			
			Parser.main("test_while_04.txt");
			tests.add("test_while_04.txt");

			System.out.println("\n\n\n-------------------------------------------");
		}
		
		@Test
		public void test_while_05() {
			System.out.println("This should pass.");
			System.out.println("test_while_05.txt");
			System.out.println("");
			
			Parser.main("test_while_05.txt");
			tests.add("test_while_05.txt");

			System.out.println("\n\n\n-------------------------------------------");
		}
		
		@Test
		public void test_while_06() {
			System.out.println("This should pass.");
			System.out.println("test_while_06.txt");
			System.out.println("");
			
			Parser.main("test_while_06.txt");
			tests.add("test_while_06.txt");

			System.out.println("\n\n\n-------------------------------------------");
		}
		
		//While Loop Errors
		@Test
		public void test_while_07_error() {
			System.out.println("This should FAIL.");
			System.out.println("test_while_07_error.txt");
			System.out.println("");

			try {
				Parser.main("test_while_07_error.txt");
			} catch (NullPointerException e) {
				assert(true);
				System.setOut(Parser.stdout);
			}
			
			System.out.println("\n\n\n-------------------------------------------");
		}

}
