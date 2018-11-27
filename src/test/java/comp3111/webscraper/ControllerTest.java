package comp3111.webscraper;


import org.junit.Test;
import org.junit.Assert;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

import javafx.application.Application;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * 
 * JUnit Testing for the functions for Controller
 */

public class ControllerTest {
	
	/**
	 * Initialise Java FX
	 * @throws InterruptedException when there is an error setting up the javafx environment
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
	 * Testing for getting url of the minimum price item
	 */
	@Test
	public void testMinPrice_1() {
		List<Item> result = new ArrayList<Item>();
		
		Item a = new Item();
		a.setPrice(1);
		a.setUrl("A");
		Item b = new Item();
		b.setPrice(10);
		b.setUrl("B");
		Item c = new Item();
		c.setPrice(100);
		c.setUrl("C");
		
		result.add(a);
		result.add(b);
		result.add(c);
		
		assertEquals(Controller.findMinPrice(result), "A");
	}
	
	/**
	 * Testing for getting url of the minimum price item if the all price is 0
	 */
	@Test
	public void testMinPrice_2() {
		List<Item> result = new ArrayList<Item>();
		
		Item a = new Item();
		a.setPrice(0);
		a.setUrl("A");
		Item b = new Item();
		b.setPrice(0);
		b.setUrl("B");
		Item c = new Item();
		c.setPrice(0);
		c.setUrl("C");
		
		result.add(a);
		result.add(b);
		result.add(c);
		
		assertEquals(Controller.findMinPrice(result), "-");
	}
	
	/**
	 * Testing for getting average price for all searched items
	 */
	@Test
	public void testAvgPrice_1() {
		List<Item> list = new ArrayList<Item>();
		
		Item a = new Item();
		a.setPrice(10.0);
		Item b = new Item();
		b.setPrice(20.0);
		Item c = new Item();
		c.setPrice(30.0);
		
		list.add(a);
		list.add(b);
		list.add(c);
		
		assertEquals(0, Double.compare(Controller.findAvgPrice(list), 20.0));
	}
	
	/**
	 * Testing for getting average price if all items price is 0
	 */
	@Test
	public void testAvgPrice_2() {
		List<Item> list = new ArrayList<Item>();
		
		Item a = new Item();
		a.setPrice(0.0);
		Item b = new Item();
		b.setPrice(0.0);
		Item c = new Item();
		c.setPrice(0.0);
		
		list.add(a);
		list.add(b);
		list.add(c);
		
		assertEquals(0, Double.compare(Controller.findAvgPrice(list), 0.0));
	}

	/**
	 * Testing for getting url of the latest post item if the year is different
	 */
	@Test
	public void testLatest_1() {
		List<Item> result = new ArrayList<Item>();
		
		Item a = new Item();
		a.setDate("2015-10-25 00:00");
		a.setUrl("A");
		Item b = new Item();
		b.setDate("2016-10-25 00:00");
		b.setUrl("B");
		
		result.add(a);
		result.add(b);
		
		assertEquals(Controller.findLatest(result), "B");
	}
	
	/**
	 * Testing for getting url of the latest post item if the time is different
	 */
	@Test
	public void testLatest_2() {
		List<Item> result = new ArrayList<Item>();
		Item a = new Item();
		a.setDate("2018-06-27 19:00");
		a.setUrl("A");
		Item b = new Item();
		b.setDate("2018-06-27 18:00");
		b.setUrl("B");
		
		result.add(a);
		result.add(b);
		
		assertEquals(Controller.findLatest(result), "A");
	}
	
	/**
	 * Testing for ActionSearch whether go button shows the non-null data
	 */
	@Test
	public void testActionSearch() {
		Controller a = new Controller();
		ActionEvent mockEvent = new ActionEvent();
		a.actionSearch(mockEvent);
		Assert.assertNotNull(a.getLabelCount());
	}
	
	/**
	 * Testing for RefineSearch whether refine button shows the non-null data
	 */
	@Test
	public void testRefineSearch() {
		Controller a = new Controller();
		ActionEvent mockEvent = new ActionEvent();
		a.actionSearch(mockEvent);
		a.refineSearch(mockEvent);
		Assert.assertNotNull(a.getLabelCount());
	}
	
	/**
	 * Testing whether we can enter the url of the minimum price item
	 * @throws IOException throw exception when there is invalid input of url
	 */
	@Test
	public void testMinClick() throws IOException {
		Controller a = new Controller();
		ActionEvent mockEvent = new ActionEvent();
		a.setLabelMin();
		a.MinClick(mockEvent);
	}
	
	/**
	 * Testing whether we can enter the url of the latest post item
	 * @throws IOException throw exception when there is invalid input of url
	 */
	@Test
	public void testLatestClick() throws IOException {
		Controller a = new Controller();
		ActionEvent mockEvent = new ActionEvent();
		a.setLabelLatest();
		a.LatestClick(mockEvent);
	}
	
	/**
	 * Testing for an action new 
	 */
	@Test
	public void testActionNew() {
		Controller a = new Controller();
		a.actionNew();
		assertTrue("This will succeed", a.getRefine());
	}
	
	/**
	 * Testing for an action close
	 */
	@Test
	public void testActionClose() {
		Controller a = new Controller();
		a.actionClose();
		assertEquals("", a.getTextField());
	}
	
}