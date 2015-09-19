import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.Test;

public class TestSorting {

	@Test
	public void test() {
		File file = new File("c.txt");
		ArrayList<String> list =new ArrayList<String>();
		list.add("jack");
		list.add("Lily");
		list.add("michael");
		list.add("jet");
		TextBuddy.writeToFile(list, file);
		
		File sorted = new File("sorted.txt");
		ArrayList<String> sortedList =new ArrayList<String>();
		list.add("jack");
		list.add("jet");
		list.add("Lily");
		list.add("michael");
		TextBuddy.writeToFile(sortedList, file);
		
		assertEquals("lines are sorted alphabetically",TextBuddy.executeUserCommand(list, file, "Sort"));
		assertEquals(sorted,file);
	}

}
