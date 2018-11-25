package comp3111.webscraper;


import org.junit.Test;
import org.junit.Assert;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class ControllerTest {
	
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
		ActionEvent mockEvent = new ActionEvent();
		a.actionSearch(mockEvent);
		Assert.assertNotNull(a.getLabelCount());
	}
	
	@Test
	public void testRefineSearch() {
		Controller a = new Controller();
		ActionEvent mockEvent = new ActionEvent();
		a.actionSearch(mockEvent);
		a.refineSearch(mockEvent);
		Assert.assertNotNull(a.getLabelCount());
	}
	
	@Test
	public void testMinClick() throws IOException {
		Controller a = new Controller();
		ActionEvent mockEvent = new ActionEvent();
		a.setLabelMin();
		a.MinClick(mockEvent);
	}
	
	@Test
	public void testLatestClick() throws IOException {
		Controller a = new Controller();
		ActionEvent mockEvent = new ActionEvent();
		a.setLabelLatest();
		a.LatestClick(mockEvent);
	}
	
	@Test
	public void testActionNew() {
		Controller a = new Controller();
		a.actionNew();
		assertTrue("This will succeed", a.getRefine());
	}
	
	@Test
	public void testActionClose() {
		Controller a = new Controller();
		a.actionClose();
		assertEquals("", a.getTextField());
	}
	
}