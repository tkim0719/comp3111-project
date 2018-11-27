package comp3111.webscraper;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;


import javafx.application.Application;


/**
 * JUnit Testing for the functions for WebScraper
 */
public class WebScraperTest {
	
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
	

	// WebScraper
	
	@Test
	public void testCscrape() {
		WebScraper testWebScraper = new WebScraper();
		Assert.assertFalse(testWebScraper.Cscrape("").isEmpty());
	}
	@Test
	public void testCAscrape() {
		WebScraper testWebScraper = new WebScraper();
		Assert.assertFalse(testWebScraper.CAscrape("").isEmpty());
	}
	@Test
	public void tesScrape() {
		WebScraper testWebScraper =  new WebScraper();
		Assert.assertFalse(testWebScraper.scrape("").isEmpty());
	
	}
	@Test
	public void testMerge() {
		List<Item> result = new ArrayList<Item>();
		List<Item> result2 = new ArrayList<Item>();
		Item a = new Item();
		Item b = new Item();
		a.setDate("");
		a.setPortal("Craiglist");
		a.setPrice(10);
		a.setTitle("a");
		a.setUrl("");
		b.setDate("");
		b.setPortal("Preloved");
		b.setPrice(10);
		b.setTitle("b");
		b.setUrl("");
		result.add(a);
		result2.add(b);
		result = WebScraper.merge(result, result2);
		assertEquals(result.get(0).getPortal(), "Craiglist");
		
	}


	
}