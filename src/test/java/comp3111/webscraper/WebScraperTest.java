package comp3111.webscraper;


import org.junit.Test;
import org.junit.Assert;
import org.junit.BeforeClass;


import javafx.application.Application;



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
	public void testCAscrape() {
		WebScraper testWebScraper = new WebScraper();
		Assert.assertFalse(testWebScraper.CAscrape("").isEmpty());
	}
	
	@Test
	public void testCscrape() {
		WebScraper testWebScraper = new WebScraper();
		Assert.assertFalse(testWebScraper.Cscrape("").isEmpty());
	}
	
	@Test
	public void tesScrape() {
		WebScraper testWebScraper = new WebScraper();
		Assert.assertFalse(testWebScraper.scrape("").isEmpty());
	
	}
	@Test
	public void testMerge() {
		
	}
	
}