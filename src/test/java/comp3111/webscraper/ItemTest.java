package comp3111.webscraper;

import org.junit.Test;
import static org.junit.Assert.*;


public class ItemTest {

	@Test
	public void testSetTitle() {
		Item i = new Item();
		i.setTitle("COMP 3111 Team 6 sudo korean");
		assertEquals(i.getTitle(), "COMP 3111 Team 6 sudo korean");
	}
	
	@Test
	public void testSetPrice() {
		Item i = new Item();
		double price = 100.0;
		i.setPrice(100.0);
		assertEquals(price, i.getPrice(), 0.0);
	}
	
//	@Test
//	public void testSetUrl() {
//		Item i = new Item();
//		i.setUrl("https://newyork.craigslist.org/");
//		assertEquals(i.getUrl().getText(), "https://newyork.craigslist.org/");
//	}
	
	@Test
	public void testSetDate() {
		Item i = new Item();
		i.setDate("2018-06-12 00:00");
		assertEquals(i.getDate(), "2018-06-12 00:00");
	}
	
	@Test
	public void testSetPortal() {
		Item i = new Item();
		i.setDate("Preloved");
		assertEquals(i.getDate(), "Preloved");
	}
	
}
