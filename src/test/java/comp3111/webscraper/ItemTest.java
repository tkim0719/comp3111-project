package comp3111.webscraper;


//import javafx.scene.control.Hyperlink;

import org.junit.Test;
import static org.junit.Assert.*;


public class ItemTest {

	@Test
	public void testSetTitle() {
		Item i = new Item();
		System.out.println(i);
		i.setTitle("ABCDE");
		System.out.println(i.getTitle());
		assertEquals(i.getTitle(), "ABCDE");
	}
}
