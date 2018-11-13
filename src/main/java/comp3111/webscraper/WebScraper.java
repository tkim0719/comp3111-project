package comp3111.webscraper;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.Vector;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

/**
 * WebScraper provide a sample code that scrape web content. After it is constructed, you can call the method scrape with a keyword, 
 * the client will go to the default url and parse the page by looking at the HTML DOM.  
 * <br/>
 * In this particular sample code, it access to craigslist.org. You can directly search on an entry by typing the URL
 * <br/>
 * https://newyork.craigslist.org/search/sss?sort=rel&amp;query=KEYWORD
 *  <br/>
 * where KEYWORD is the keyword you want to search.
 * <br/>
 * Assume you are working on Chrome, paste the url into your browser and press F12 to load the source code of the HTML. You might be freak
 * out if you have never seen a HTML source code before. Keep calm and move on. Press Ctrl-Shift-C (or CMD-Shift-C if you got a mac) and move your
 * mouse cursor around, different part of the HTML code and the corresponding the HTML objects will be highlighted. Explore your HTML page from
 * body &rarr; section class="page-container" &rarr; form id="searchform" &rarr; div class="content" &rarr; ul class="rows" &rarr; any one of the multiple 
 * li class="result-row" &rarr; p class="result-info". You might see something like this:
 * <br/>
 * <pre>
 * {@code
 *    <p class="result-info">
 *        <span class="icon icon-star" role="button" title="save this post in your favorites list">
 *           <span class="screen-reader-text">favorite this post</span>
 *       </span>
 *       <time class="result-date" datetime="2018-06-21 01:58" title="Thu 21 Jun 01:58:44 AM">Jun 21</time>
 *       <a href="https://newyork.craigslist.org/que/clt/d/green-star-polyp-gsp-on-rock/6596253604.html" data-id="6596253604" class="result-title hdrlnk">Green Star Polyp GSP on a rock frag</a>
 *       <span class="result-meta">
 *               <span class="result-price">$15</span>
 *               <span class="result-tags">
 *                   pic
 *                   <span class="maptag" data-pid="6596253604">map</span>
 *               </span>
 *               <span class="banish icon icon-trash" role="button">
 *                   <span class="screen-reader-text">hide this posting</span>
 *               </span>
 *           <span class="unbanish icon icon-trash red" role="button" aria-hidden="true"></span>
 *           <a href="#" class="restore-link">
 *               <span class="restore-narrow-text">restore</span>
 *               <span class="restore-wide-text">restore this posting</span>
 *           </a>
 *       </span>
 *   </p>
 *}
 *</pre>
 * <br/>
 * The code 
 * <pre>
 * {@code
 * List<?> items = (List<?>) page.getByXPath("//li[@class='result-row']");
 * }
 * </pre>
 * extracts all result-row and stores the corresponding HTML elements to a list called items. Later in the loop it extracts the anchor tag 
 * &lsaquo; a &rsaquo; to retrieve the display text (by .asText()) and the link (by .getHrefAttribute()). It also extracts  
 * 
 *
 */
public class WebScraper {

	private static final String DEFAULT_URL = "https://newyork.craigslist.org/";
	private static final String NEW_URL = "https://hk.carousell.com";
	private WebClient client;

	/**
	 * Default Constructor 
	 */
	public WebScraper() {
		client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
	}

	/**
	 * The only method implemented in this class, to scrape web content from the craigslist
	 * 
	 * @param keyword - the keyword you want to search
	 * @return A list of Item that has found. A zero size list is return if nothing is found. Null if any exception (e.g. no connectivity)
	 */
	
	public List<Item> scrape(String keyword) {

		try {
			String searchUrl = DEFAULT_URL + "search/sss?sort=rel&query=" + URLEncoder.encode(keyword, "UTF-8");
			HtmlPage page = client.getPage(searchUrl);
			
			String searchUrl2 = NEW_URL + "/search/products/?query=" + URLEncoder.encode(keyword, "UTF-8");
			HtmlPage page2 = client.getPage(searchUrl2);
			System.out.println(page2);
			
			List<?> items = (List<?>) page.getByXPath("//li[@class='result-row']");
			List<?> items2 = (List<?>) page2.getByXPath("//div[@class='col-lg-3 col-md-4 col-sm-4 col-xs-6']");
			System.out.println(items2);

			Vector<Item> result = new Vector<Item>();
			Vector<Item> result2 = new Vector<Item>();

			for (int i = 0; i < items.size(); i++) {
				HtmlElement htmlItem = (HtmlElement) items.get(i);
				
				HtmlAnchor itemAnchor = ((HtmlAnchor) htmlItem.getFirstByXPath(".//p[@class='result-info']/a"));
				HtmlElement spanPrice = ((HtmlElement) htmlItem.getFirstByXPath(".//a/span[@class='result-price']"));
				HtmlElement spanDate = ((HtmlElement) htmlItem.getFirstByXPath(".//time[@class='result-date']"));

				// It is possible that an item doesn't have any price, we set the price to 0.0
				// in this case
				String itemPrice = spanPrice == null ? "0.0" : spanPrice.asText();
				String postDate = spanDate.getAttribute("datetime");
				String Url = DEFAULT_URL + itemAnchor.getHrefAttribute();

				Item item = new Item();
				item.setTitle(itemAnchor.asText());
				item.setUrl(Url);
				item.setPrice(new Double(itemPrice.replace("$", "")));
				item.setDate(postDate);
				item.setPortal("Craiglist");
				
				result.add(item);
			}
			
			Calendar real_postDate;
			String CalculatedPostDate = "";
			
			for (int i = 0; i < items2.size(); i++) {
				real_postDate = Calendar.getInstance();
				System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(real_postDate.getTime()));
				
				HtmlElement htmlItem = (HtmlElement) items2.get(i);
				
				HtmlAnchor itemAnchor = ((HtmlAnchor) htmlItem.getFirstByXPath(".//a[@class='G-Y']"));
				HtmlElement itemName = ((HtmlElement) htmlItem.getFirstByXPath(".//div[@class='G-m']"));
				HtmlElement spanPrice = ((HtmlElement) htmlItem.getFirstByXPath(".//div[@class='G-k']//div[1]"));
				HtmlElement spanDate = ((HtmlElement) htmlItem.getFirstByXPath(".//time[@class='G-u']"));
				
				// It is possible that an item doesn't have any price, we set the price to 0.0
				// in this case
				String itemPrice = spanPrice == null ? "0.0" : spanPrice.asText();
				String postDate = spanDate.asText();
				
				if (postDate.indexOf(" ago") != -1) {
					String ago_date = postDate.substring(0, postDate.indexOf(" ago"));
					System.out.println(ago_date);
					
					int seconds = 0;
					int minutes = 0;
					int hours = 0;
					int days = 0;
					int months = 0;
					int years = 0;
					
					if (ago_date.indexOf(" second") != -1) {
						seconds = Integer.parseInt(ago_date.substring(0, ago_date.indexOf(" second")));
						real_postDate.add(Calendar.SECOND, -1 * seconds);
					}
					if (ago_date.indexOf(" minute") != -1) {
						minutes = Integer.parseInt(ago_date.substring(0, ago_date.indexOf(" minute")));
						real_postDate.add(Calendar.MINUTE, -1 * minutes);
					}
					if (ago_date.indexOf(" hour") != -1) {
						hours = Integer.parseInt(ago_date.substring(0, ago_date.indexOf(" hour")));
						real_postDate.add(Calendar.HOUR, -1 * hours);
					}
					if (ago_date.indexOf(" day") != -1) {
						days = Integer.parseInt(ago_date.substring(0, ago_date.indexOf(" day")));
						real_postDate.add(Calendar.DATE, -1 * days);
					}					
					if (ago_date.indexOf(" month") != -1) {
						months = Integer.parseInt(ago_date.substring(0, ago_date.indexOf(" month")));
						real_postDate.add(Calendar.MONTH, -1 * months);
					}
					if (ago_date.indexOf(" year") != -1) {
						years = Integer.parseInt(ago_date.substring(0, ago_date.indexOf(" year")));
						real_postDate.add(Calendar.YEAR, -1 * years);
					}
				}
				
				CalculatedPostDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(real_postDate.getTime());
				System.out.println(CalculatedPostDate+"\n");
				
				Item item = new Item();
				item.setTitle(itemName.asText());
				item.setDate(CalculatedPostDate);
				item.setUrl(NEW_URL + itemAnchor.getHrefAttribute());
				Double x = new Double(itemPrice.replace("HK$", "").replace(",", ""));
				x = x * 0.128;		//change currency to USD
				item.setPrice(x);
				item.setPortal("Carousell");
				
				System.out.println(item.getTitle() + "\t$" + item.getPrice() + "\t" + item.getPortal() + "\t" + item.getUrl().getText() + "\t" + item.getDate() + "\n");
				
				result2.add(item);
			}
			
			result.sort(Comparator.comparing(Item::getPrice));
			result2.sort(Comparator.comparing(Item::getPrice));
			merge(result,result2);
			
			client.close();
			return result;
			
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	public List<Item> newScrape(String keyword) {

		try {
			String searchUrl2 = NEW_URL + "search/products/?query=" + URLEncoder.encode(keyword, "UTF-8");
			HtmlPage page2 = client.getPage(searchUrl2);

			List<?> items2 = (List<?>) page2.getByXPath("//div[@class='col-lg-3 col-md-4 col-sm-4 col-xs-6']");
			Vector<Item> result2 = new Vector<Item>();
			
			Calendar real_postDate;
			String CalculatedPostDate = "";

			for (int i = 0; i < items2.size(); i++) {
				
				real_postDate = Calendar.getInstance();
				System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(real_postDate.getTime()));
				
				HtmlElement htmlItem = (HtmlElement) items2.get(i);
				HtmlAnchor itemAnchor = ((HtmlAnchor) htmlItem.getFirstByXPath(".//a[@class='G-Y']"));
				HtmlElement itemName = ((HtmlElement) htmlItem.getFirstByXPath(".//div[@class='G-m']"));
				HtmlElement spanPrice = ((HtmlElement) htmlItem.getFirstByXPath(".//div[@class='G-k']//div[1]"));
				HtmlElement spanDate = ((HtmlElement) htmlItem.getFirstByXPath(".//time[@class='G-u']"));
				
				String postDate = spanDate.asText();
				
				if (postDate.indexOf(" ago") != -1) {
					String ago_date = postDate.substring(0, postDate.indexOf(" ago"));
					System.out.println(ago_date);
					
					int seconds = 0;
					int minutes = 0;
					int hours = 0;
					int days = 0;
					int months = 0;
					int years = 0;
					
					if (ago_date.indexOf(" second") != -1) {
						seconds = Integer.parseInt(ago_date.substring(0, ago_date.indexOf(" second")));
						real_postDate.add(Calendar.SECOND, -1 * seconds);
					}
					if (ago_date.indexOf(" minute") != -1) {
						minutes = Integer.parseInt(ago_date.substring(0, ago_date.indexOf(" minute")));
						real_postDate.add(Calendar.MINUTE, -1 * minutes);
					}
					if (ago_date.indexOf(" hour") != -1) {
						hours = Integer.parseInt(ago_date.substring(0, ago_date.indexOf(" hour")));
						real_postDate.add(Calendar.HOUR, -1 * hours);
					}
					if (ago_date.indexOf(" day") != -1) {
						days = Integer.parseInt(ago_date.substring(0, ago_date.indexOf(" day")));
						real_postDate.add(Calendar.DATE, -1 * days);
					}
					if (ago_date.indexOf(" month") != -1) {
						months = Integer.parseInt(ago_date.substring(0, ago_date.indexOf(" month")));
						real_postDate.add(Calendar.MONTH, -1 * months);
					}
					if (ago_date.indexOf(" year") != -1) {
						years = Integer.parseInt(ago_date.substring(0, ago_date.indexOf(" year")));
						real_postDate.add(Calendar.YEAR, -1 * years);
					}
				}
				
				CalculatedPostDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(real_postDate.getTime());
				System.out.println(CalculatedPostDate+"\n");
				
				// It is possible that an item doesn't have any price, we set the price to 0.0
				// in this case
				String itemPrice = spanPrice == null ? "0.0" : spanPrice.asText();

				Item item = new Item();
				item.setTitle(itemName.asText());
				item.setDate(CalculatedPostDate);
				item.setUrl(NEW_URL + itemAnchor.getHrefAttribute());
				item.setPrice(new Double(itemPrice.replace("HK$", "").replace(",", "")));
				item.setPortal("Carousell");
				
				result2.add(item);
			}
			result2.sort(Comparator.comparing(Item::getPrice));
			client.close();
			
			return result2;
			
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	public static void merge(List<Item> l1, List<Item> l2) {
	    for (int index1 = 0, index2 = 0; index2 < l2.size(); index1++) {
	        if (index1 == l1.size() || l1.get(index1).getPrice() > l2.get(index2).getPrice()) {
	            l1.add(index1, l2.get(index2++));
	        }
	    }
	}  
}
