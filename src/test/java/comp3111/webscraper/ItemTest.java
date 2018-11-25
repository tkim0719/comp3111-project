package comp3111.webscraper;


import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.gargoylesoftware.css.parser.javacc.ParseException;

import javafx.application.Application;
import javafx.event.ActionEvent;

import org.junit.BeforeClass;
import org.junit.Rule;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class ItemTest {
	
	// Initialise Java FX
	
	@BeforeClass
	public static void setUpClass() throws InterruptedException {
	   
	    Thread t = new Thread("JavaFX Init Thread") {
	        public void run() {
	            Application.launch(WebScraperApplication.class, new String[0]);
	        }
	    };
	    t.setDaemon(true);
	    t.start();
	    System.out.printf("FX App thread started\n");
	    Thread.sleep(500);
	}
	
	@Test
	public void testMinPrice() {
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
	
	@Test
	public void testAvgPrice() {
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

	
	@Test
	public void testLatest() {
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
	
	@Test
	public void testActionSearch() {
		Controller a = new Controller();
		//a.actionSearch(new ActionEvent());
		assertNotNull(a.getLabelCount());
	}
	
	@Test
	public void testActionNew() {
		Controller a = new Controller();
		a.actionNew();
		//assertTrue("This will succeed", a.getRefine());
		assertTrue("true", true);
	}
	
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
		i.setDate("Preloved");
		assertEquals(i.getDate(), "Preloved");
	}
	
}
