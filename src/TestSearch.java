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
		assertEquals("1. jack",TextBuddy.excuteUserCommand(list, file, "Jack"));
	}

}
