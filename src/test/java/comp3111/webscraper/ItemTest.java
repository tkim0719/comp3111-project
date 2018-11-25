package comp3111.webscraper;


import org.junit.Test;
import org.junit.BeforeClass;
import static org.junit.Assert.*;


import javafx.application.Application;



public class ItemTest {
	
	// Initialise Java FX
	
	@BeforeClass
	public static void setUpClass() throws InterruptedException {
	   
	    Thread t = new Thread("JavaFX Init Thread") {
	        public void run() {
	            Application.launch(WebScraperApplication.class, new String[0]);
	        }
	    };
	    t.start();
	    System.out.printf("FX App thread started\n");
	    Thread.sleep(500);
	}
	
	// Item
	
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
	
	@Test
	public void testSetUrl() {
		Item i = new Item();
		i.setUrl("https://newyork.craigslist.org/");
		assertEquals(i.getUrl().getText(), "https://newyork.craigslist.org/");
	}
	
	@Test
	public void testSetDate() {
		Item i = new Item();
		i.setDate("2018-06-12 00:00");
		assertEquals(i.getDate(), "2018-06-12 00:00");
	}
	
	@Test
	public void testSetPortal() {
		Item i = new Item();
		i.setPortal("Preloved");
		assertEquals(i.getPortal(), "Preloved");
	}
	
}
