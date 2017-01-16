import static org.junit.Assert.*;

import org.junit.Test;

/* 
 * If you want to just run this instead of doing terminal inputs with our test cases, 
 * it will print out the assembly code for all of the test cases, in order starting 
 * with ints. It will also note whether the test should pass or fail. However, these 
 * do not check for the correctness of the assembly code. 
 */

public class Phase2b_Testing {

	/*
	 * Boolean and Logical Operators
	 */
	
	/*
	@Test
	public void test26() {
		System.out.println("This should FAIL.\n");
		Parser.main("testcases/test_strings_11-Error.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	*/
	
	
	@Test
	public void test_bool_00() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_bool_00.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test_bool_01() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_bool_01.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test_bool_02() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_bool_02.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test_bool_03() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_bool_03.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test_bool_04() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_bool_04.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test_bool_05() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_bool_05.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	/*
	 * Multiplication
	 */
	
	@Test
	public void test_mul_00() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_mul_00.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test_mul_01() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_mul_01.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test_mul_02() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_mul_02.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	/*
	 * Divide
	 */
	@Test
	public void test_div_00() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_div_00.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test_div_01() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_div_01.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	/*
	 * Mod
	 */
	
	@Test
	public void test_mod_00() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_mod_00.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}

	@Test
	public void test_mod_01() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_mod_01.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	@Test
	public void test_mod_02() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_mod_02.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	/*
	 * Order of Operations
	 */
	
	@Test
	public void test_order_00() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_order_00.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test_order_01() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_order_01.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
}
