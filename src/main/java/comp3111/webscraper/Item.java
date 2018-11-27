package comp3111.webscraper;

import javafx.scene.control.Hyperlink;


public class Item {
	private String title ;
	private double price ;
	private Hyperlink url = new Hyperlink();
	private String date ;
	private String portal ;
	
	public Item() {
		
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	public double getPrice() {
		return price;
	}
	
	public void setUrl(String url) {
		this.url.setText(url);
	}
	public Hyperlink getUrl() {
		return url;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	public String getDate() {
		return date;
	}
	
	public void setPortal(String portal) {
		this.portal = portal;
	}
	public String getPortal() {
		return portal;
	}
	
}
