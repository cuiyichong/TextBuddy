import static org.junit.Assert.*;

import org.junit.Test;
import java.io.File;
import java.util.ArrayList;;


public class TestSearch {

	@Test
	public void testSearchString() {
		File file = new File("b.txt");
		ArrayList<String> list =new ArrayList<String>();
		list.add("jack");
		list.add("Lily");
		list.add("michael");
		TextBuddy.writeToFile(list, file);
//		assertEquals("all content deleted from b.txt",TextBuddy.executeUserCommand(list, file, "clear"));
System.out.println(TextBuddy.executeUserCommand(list, file, "jack"));
		assertEquals("1. jack",TextBuddy.executeUserCommand(list, file, "Search jack"));
	}

}
