import static org.junit.Assert.*;

import org.junit.Test;

/* 
 * If you want to just run this instead of doing terminal inputs with our test cases, 
 * it will print out the assembly code for all of the test cases, in order starting 
 * with ints. It will also note whether the test should pass or fail. However, these 
 * do not check for the correctness of the assembly code. 
 */

public class Phase1b_Testing {

	@Test
	public void test01() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_ints_00.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test02() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_ints_01.txt");
		
		System.out.println("\n\n\n-------------------------------------------");	
	}
	
	@Test
	public void test03() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_ints_02.txt");
		
		System.out.println("\n\n\n-------------------------------------------");	
	}
	
	@Test
	public void test04() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_ints_03.txt");
		
		System.out.println("\n\n\n-------------------------------------------");	
	}
	
	@Test
	public void test05() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_ints_04.txt");
		
		System.out.println("\n\n\n-------------------------------------------");	
	}
	
	@Test
	public void test06() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_ints_05.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test07() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_ints_06.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test08() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_ints_07.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test09() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_ints_08.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test10() {
		System.out.println("This should FAIL.\n");
		Parser.main("testcases/test_ints_09-Error.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test11() {
		System.out.println("This should FAIL.\n");
		Parser.main("testcases/test_ints_10-Error.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test12() {
		System.out.println("This should FAIL.\n");
		Parser.main("testcases/test_ints_11-Error.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test13() {
		System.out.println("This should FAIL.\n");
		Parser.main("testcases/test_ints_12-Error.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test14() {
		System.out.println("This should FAIL.\n");
		Parser.main("testcases/test_ints_13-Error.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test15() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_strings_00.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test16() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_strings_01.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test17() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_strings_02.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	
	
	
	@Test
	public void test18() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_strings_03.txt");
		
		System.out.println("\n\n\n-------------------------------------------");	
	}
	
	@Test
	public void test19() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_strings_04.txt");
		
		System.out.println("\n\n\n-------------------------------------------");	
	}
	
	@Test
	public void test20() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_strings_05.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test21() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_strings_06.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test22() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_strings_07.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test23() {
		System.out.println("This should pass.\n");
		Parser.main("testcases/test_strings_08-Error.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test24() {
		System.out.println("This should FAIL.\n");
		Parser.main("testcases/test_strings_09-Error.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test25() {
		System.out.println("This should FAIL.\n");
		Parser.main("testcases/test_strings_10-Error.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}
	
	@Test
	public void test26() {
		System.out.println("This should FAIL.\n");
		Parser.main("testcases/test_strings_11-Error.txt");
		
		System.out.println("\n\n\n-------------------------------------------");
	}

}
