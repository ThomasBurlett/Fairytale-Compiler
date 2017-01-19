import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Phase2b_Testing {
	
	private static ArrayList<String> tests = new ArrayList<>();
	
	/*
	 * Boolean and Logical Operators
	 */
	
	@Test
	public void test_bool_00() {
		System.out.println("This should pass.");
		System.out.println("Testing: test_bool_00.txt");
		
		tests.add("test_bool_00");
		Parser.main("test_bool_00.txt");
		
		System.out.println("Please run test_bool_00.s to get output.");
		System.out.println("\n-------------------------------------------\n");
	}
	
	@Test
	public void test_bool_01() {
		System.out.println("This should pass.");
		System.out.println("Testing: test_bool_01.txt");
		
		tests.add("test_bool_01");
		Parser.main("test_bool_01.txt");
		
		System.out.println("Please run test_bool_01.s to get output.");		
		System.out.println("\n-------------------------------------------\n");
	}
	
	@Test
	public void test_bool_02() {
		System.out.println("This should pass.");
		System.out.println("Testing: test_bool_02.txt");
		
		tests.add("test_bool_02");
		Parser.main("test_bool_02.txt");
		
		System.out.println("Please run test_bool_02.s to get output.");
		System.out.println("\n-------------------------------------------\n");
	}
	
	@Test
	public void test_bool_03() {
		System.out.println("This should pass.");
		System.out.println("Testing: test_bool_03.txt");
		
		tests.add("test_bool_03");
		Parser.main("test_bool_03.txt");
		
		System.out.println("Please run test_bool_03.s to get output.");
		System.out.println("\n-------------------------------------------\n");
	}
	
	@Test
	public void test_bool_04() {
		System.out.println("This should pass.");
		System.out.println("Testing: test_bool_04.txt");
		
		tests.add("test_bool_04");
		Parser.main("test_bool_04.txt");
		
		System.out.println("Please run test_bool_04.s to get output.");
		System.out.println("\n-------------------------------------------\n");
	}
	
	@Test
	public void test_bool_05() {
		System.out.println("This should pass.");
		System.out.println("Testing: test_bool_05.txt");
		
		tests.add("test_bool_05");
		Parser.main("test_bool_05.txt");
		
		System.out.println("Please run test_bool_05.s to get output.");
		System.out.println("\n-------------------------------------------\n");
	}
	
	//Boolean Errors
	@Test
	public void test_bool_06_error() {
		System.out.println("This should FAIL.");
		System.out.println("Testing: test_bool_06_error.txt");
		
		try {
			Parser.main("test_bool_06_error.txt");
			fail(); // FAIL when no exception is thrown
		} catch (NullPointerException e) {
			assert(true);
		}
		
		// Set output back to console
        System.setOut(Parser.stdout);
        
		System.out.println("Please look at test_bool_06_error.s to see errors.");
		System.out.println("\n-------------------------------------------\n");
	}
	
	@Test
	public void test_bool_07_error() {
		System.out.println("This should FAIL.");
		System.out.println("Testing: test_bool_07_Error.txt");
		
		Parser.main("test_bool_07_Error.txt");
		
		System.out.println("Please look at test_bool_07_error.s to see errors.");		
		System.out.println("\n-------------------------------------------\n");
	}
	
	@Test
	public void test_bool_08_error() {
		System.out.println("This should FAIL.");
		System.out.println("Testing: test_bool_08_error.txt");
		
		try {
			Parser.main("test_bool_08_error.txt");
			fail(); // FAIL when no exception is thrown
		} catch (NullPointerException e) {
			assert(true);
		}
		
		// Set output back to console
        System.setOut(Parser.stdout);
		
		System.out.println("Please look at test_bool_08_error.s to see errors.");
		System.out.println("\n-------------------------------------------\n");
	}
	
	/*
	 * Multiplication
	 */
	
	@Test
	public void test_mul_00() {
		System.out.println("This should pass.");
		System.out.println("Testing: test_mul_00.txt");
		
		tests.add("test_mul_00");
		Parser.main("test_mul_00.txt");
		
		System.out.println("Please run test_mul_00.s to get output.");
		System.out.println("\n-------------------------------------------\n");
	}
	
	@Test
	public void test_mul_01() {
		System.out.println("This should pass.");
		System.out.println("Testing: test_mul_01.txt");
		
		tests.add("test_mul_01");
		Parser.main("test_mul_01.txt");

		System.out.println("Please run test_mul_01.s to get output.");
		System.out.println("\n-------------------------------------------\n");
	}
	
	@Test
	public void test_mul_02() {
		System.out.println("This should pass.");
		System.out.println("Testing: test_mul_02.txt");
		
		tests.add("test_mul_02");
		Parser.main("test_mul_02.txt");

		System.out.println("Please run test_mul_02.s to get output.");
		System.out.println("\n-------------------------------------------\n");
	}
	
	//Multiplication Error
	
	@Test
	public void test_mul_03_error() {
		System.out.println("This should FAIL.");
		System.out.println("Testing: test_mul_03_error.txt");
		
		Parser.main("test_mul_03_error.txt");

		System.out.println("Please look at test_mul_03_error.s to see errors.");
		System.out.println("\n-------------------------------------------\n");
	}
	
	/*
	 * Divide
	 */
	
	@Test
	public void test_div_00() {
		System.out.println("This should pass.");
		System.out.println("Testing: test_div_00.txt");
		
		tests.add("test_div_00");
		Parser.main("test_div_00.txt");

		System.out.println("Please run test_div_00.s to get output.");
		System.out.println("\n-------------------------------------------\n");
	}
	
	@Test
	public void test_div_01() {
		System.out.println("This should pass.");
		System.out.println("Testing: test_div_01.txt");
		
		tests.add("test_div_01");
		Parser.main("test_div_01.txt");

		System.out.println("Please run test_div_01.s to get output.");
		System.out.println("\n-------------------------------------------\n");
	}
	
	//Error Div
	
	@Test
	public void test_div_02_error() {
		System.out.println("This should FAIL.");
		System.out.println("Testing: test_div_02_error.txt");
		
		Parser.main("test_div_02_error.txt");

		System.out.println("Please look at test_div_01_error.s to see errors.");
		System.out.println("\n-------------------------------------------\n");
	}
	
	
	/*
	 * Mod
	 */
	
	@Test
	public void test_mod_00() {
		System.out.println("This should pass.");
		System.out.println("Testing: test_mod_00.txt");
		
		tests.add("test_mod_00");
		Parser.main("test_mod_00.txt");
		
		System.out.println("Please run test_mod_00.s to get output.");
		System.out.println("\n-------------------------------------------\n");
	}

	@Test
	public void test_mod_01() {
		System.out.println("This should pass.");
		System.out.println("Testing: test_mod_01.txt");
		
		tests.add("test_mod_00");
		Parser.main("test_mod_01.txt");
		
		System.out.println("Please run test_mod_01.s to get output.");
		System.out.println("\n-------------------------------------------\n");
	}
	
	
	@Test
	public void test_mod_02() {
		System.out.println("This should pass.");
		System.out.println("Testing: test_mod_02.txt");
		
		tests.add("test_mod_00");
		Parser.main("test_mod_02.txt");
		
		System.out.println("Please run test_mod_02.s to get output.");
		System.out.println("\n-------------------------------------------\n");
	}
	
	//Mod Error
	@Test
	public void test_mod_03() {
		System.out.println("This should pass.");
		System.out.println("Testing: test_mod_03.txt");
		
		tests.add("test_mod_03");
		Parser.main("test_mod_03.txt");
		
		System.out.println("Please run test_mod_03.s to get output.");
		System.out.println("\n-------------------------------------------\n");
	}
	
	/*
	 * Order of Operations
	 */
	
	@Test
	public void test_order_00() {
		System.out.println("This should pass.");
		System.out.println("Testing: test_order_00.txt");
		
		tests.add("test_order_00");
		Parser.main("test_order_00.txt");
		
		System.out.println("Please run test_order_00.s to get output.");
		System.out.println("\n-------------------------------------------\n");
	}
	
	@Test
	public void test_order_01() {
		System.out.println("This should pass.");
		System.out.println("Testing: test_order_01.txt");
		
		tests.add("test_order_01");
		Parser.main("test_order_01.txt");
		
		System.out.println("Please run test_order_01.s to get output.");
		System.out.println("\n-------------------------------------------\n");
	}	
	
	
	/* Since we use 0's and 1's for our boolean values, the write is the same as int's write */
	/* Since we run a JUnit test, the program does not stop running. Because of this, if we run 
	 * two separate tests where we are calling WRITE, then the second test will act as if it 
	 * already printed the write code. If you want to test writing false (aka 0), replace
	 * "test_write_00.txt" with "test_write_01.txt" */
	@Test
	public void test_write_00() {
		System.out.println("This should pass.");
		System.out.println("Testing: test_write_00.txt");
		
		CodeFactory.firstWrite = true;
		tests.add("test_write_00");
		Parser.main("test_write_00.txt");
		
		System.out.println("Please run test_write_00.s to get output.");
		System.out.println("\n-------------------------------------------\n");
	}		
	
	@Test
	public void test_write_01() {
		System.out.println("This should pass.");
		System.out.println("Testing: test_write_01.txt");
		
		CodeFactory.firstWrite = true;
		tests.add("test_write_01");
		Parser.main("test_write_01.txt");
		
		System.out.println("Please run test_write_01.s to get output.");
		System.out.println("\n-------------------------------------------\n");
	}
	
	// Add to Bash Script
	@Test
	public void writeScript() {
		
		try {	   
			File comp = new File("compile1.bash");
			FileOutputStream compileStream = new FileOutputStream(comp);
			PrintStream out1 = new PrintStream(compileStream);

			File comp2 = new File("compile2.bash");
			FileOutputStream compileStream2 = new FileOutputStream(comp2);
			PrintStream out2 = new PrintStream(compileStream2);
			
			File run = new File("runAll.bash");
			FileOutputStream outStream = new FileOutputStream(run);
			PrintStream out3 = new PrintStream(outStream);
			
			for (String testcase : tests) {
				out1.println("as -g --32 AssemblyCode/" + testcase + ".s -o " + testcase + ".o");
				out2.println("ld -m elf_i386 " + testcase + ".o -o " + testcase);
				out3.println("echo 'Output for " + testcase + " :'");
				out3.println("./" + testcase);
				out3.println("echo ' '");
			}
			
			out1.close();
			out2.close();
			out3.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error creating file.");
			// e.printStackTrace();
		}
	}
}
