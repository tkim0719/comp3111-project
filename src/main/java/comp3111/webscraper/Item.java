package comp3111.webscraper;

import javafx.scene.control.Hyperlink;

/**
 * Item Class
 */

public class Item {
	private String title ;
	private double price ;
	private Hyperlink url = new Hyperlink();
	private String date ;
	private String portal ;
	
	/** 
	 * Default Constructor
	 */
	public Item() {
		
	}
	
	/**
	 * Setter for title
	 * @param title String
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Getter for title
	 * @return void
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Setter for price
	 * @param price Double
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	
	/**
	 * Getter for price
	 * @return void
	 */
	public double getPrice() {
		return price;
	}
	
	/**
	 * Setter for URL
	 * @param url String
	 */
	public void setUrl(String url) {
		this.url.setText(url);
	}
	
	/**
	 * Getter for URL
	 * @return void
	 */
	public Hyperlink getUrl() {
		return url;
	}
	
	/**
	 * Setter for date
	 * @param date String
	 */
	public void setDate(String date) {
		this.date = date;
	}
	
	/**
	 * Getter for date
	 * @return void
	 */
	public String getDate() {
		return date;
	}
	
	/**
	 * Setter for portal
	 * @param portal String
	 */
	public void setPortal(String portal) {
		this.portal = portal;
	}
	
	/**
	 * Getter for portal
	 * @return void
	 */
	public String getPortal() {
		return portal;
	}
	
}
