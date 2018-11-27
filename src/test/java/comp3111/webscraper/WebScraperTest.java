package comp3111.webscraper;


import org.junit.Test;
import org.junit.Assert;
import org.junit.BeforeClass;


import javafx.application.Application;


/**
 * JUnit Testing for the functions for WebScraper
 */
public class WebScraperTest {
	
	/**
	 * Initialise Java FX
	 * @throws InterruptedException when Java FX is not initialized before the testing
	 */
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
	

	/**
	 * Test CAscrape() function with empty keyword
	 */
	@Test
	public void testCAscrape() {
		WebScraper testWebScraper = new WebScraper();
		Assert.assertFalse(testWebScraper.CAscrape("").isEmpty());
	}
	
	/**
	 * Test Cscrape() function with empty keyword
	 */
	@Test
	public void testCscrape() {
		WebScraper testWebScraper = new WebScraper();
		Assert.assertFalse(testWebScraper.Cscrape("").isEmpty());
	}
	
	/**
	 * Test scrape() function with empty keyword
	 */
	@Test
	public void testScrape() {
		WebScraper testWebScraper = new WebScraper();
		Assert.assertFalse(testWebScraper.scrape("").isEmpty());
	}
	
}