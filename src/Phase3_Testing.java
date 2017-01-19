import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;
import org.junit.Test;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Phase3_Testing {

	private static ArrayList<String> tests = new ArrayList<>();

	@Test
	public void test_ex1() {
		CodeFactory.firstWrite = true;
		Parser.main("filename.txt");
		tests.add("filename");

		System.out.println("This will print to the console.");
	}


}
