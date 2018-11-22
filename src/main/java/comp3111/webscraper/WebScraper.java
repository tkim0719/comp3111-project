package comp3111.webscraper;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.Vector;
import java.util.Calendar;
import java.util.Comparator;


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
	private static final String NEW_URL = "https://www.preloved.co.uk";
	private WebClient client;

	/**
	 * Default Constructor 
	 */
	public WebScraper() {
		client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
	}

	
	public List<Item> Cscrape(String keyword) {

		
		try {
			String searchUrl = DEFAULT_URL + "/search/sss?sort=rel&query=" + URLEncoder.encode(keyword, "UTF-8");
			HtmlPage page = client.getPage(searchUrl);

			//String searchUrl2 = NEW_URL + "/search/products/?query=" + URLEncoder.encode(keyword, "UTF-8");
			//HtmlPage page2 = client.getPage(searchUrl2);
			List<?> items = (List<?>) page.getByXPath("//li[@class='result-row']");

			//Determining last page pN
			HtmlElement it = (HtmlElement) items.get(1);
			HtmlElement pageNum = ((HtmlElement) it.getFirstByXPath("//span[@class='totalcount']"));
			String pageNum1 = pageNum.asText();
			
			int pageNum2 = Integer.parseInt(pageNum1);
			int pN;
			if(pageNum2<120)
			{
				pN = 1;
			}
			else if(pageNum2%120!=0)
			{
				pN = (pageNum2/120) +1;
			}
			else
			{
				pN = pageNum2/120;
			}			
			//List<?> items2 = (List<?>) page2.getByXPath("//div[@class='col-lg-3 col-md-4 col-sm-4 col-xs-6']");
			Vector<Item> result = new Vector<Item>();
			//////////////////////////////
			pN=1;
			//////////////////////////
			for(int i=0;i<pN;i++)
			{
				System.out.println("Number of Craiglist page scraped so far is "+(i+1)+"/"+pN);
				String extra = "";
				if(i==0)
				{
					extra = "";
				}
				else
				{
					extra = "s=" + Integer.toString(i*120)+"&";
				}
				String searchUrlM = DEFAULT_URL + "search/sss?"+extra+"sort=rel&query="+ URLEncoder.encode(keyword, "UTF-8");
				HtmlPage pageM = client.getPage(searchUrlM);
				List<?> itemsM = (List<?>) pageM.getByXPath("//li[@class='result-row']");
				//Vector<Item> result2 = new Vector<Item>();
				for (int j = 0; j < itemsM.size();j++) {
					HtmlElement htmlItem = (HtmlElement) itemsM.get(j);
					HtmlAnchor itemAnchor = ((HtmlAnchor) htmlItem.getFirstByXPath(".//p[@class='result-info']/a"));
					HtmlElement spanPrice = ((HtmlElement) htmlItem.getFirstByXPath(".//a/span[@class='result-price']"));
					HtmlElement spanDate = ((HtmlElement) htmlItem.getFirstByXPath(".//time[@class='result-date']"));

					// It is possible that an item doesn't have any price, we set the price to 0.0
					// in this case
					String itemPrice = spanPrice == null ? "0.0" : spanPrice.asText();
					String postDate = spanDate.getAttribute("datetime");

					Item item = new Item();
					item.setTitle(itemAnchor.asText());
					item.setUrl(itemAnchor.getHrefAttribute());
	
					Double x = new Double(itemPrice.replace("$", ""));
					x = x * 7.8;		// US $ to HK $
					item.setPrice(x);
					item.setPortal("Craiglist");
					item.setDate(postDate);
					result.add(item);
				}

			}
			result.sort(Comparator.comparing(Item::getPrice));
			System.out.println(result.size());
			//client.close();
			return result;
			
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	public List<Item> CAscrape(String keyword) {

		try {
			String searchUrl2 = NEW_URL + "/search?keyword=" + URLEncoder.encode(keyword, "UTF-8");
			HtmlPage page2 = client.getPage(searchUrl2);
			
			List<?> items2 = (List<?>) page2.getByXPath("//li[@class='search-result']");
			Vector<Item> result2 = new Vector<Item>();

			for (int i = 0; i < items2.size(); i++) {
				
				HtmlElement htmlItem = (HtmlElement) items2.get(i);
				
				HtmlAnchor itemAnchor = ((HtmlAnchor) htmlItem.getFirstByXPath(".//div[@class='search-result__content']/header/h2/a"));
				HtmlElement itemName = ((HtmlElement) htmlItem.getFirstByXPath(".//span[@itemprop='name']"));
				HtmlElement spanPrice = ((HtmlElement) htmlItem.getFirstByXPath(".//span[@itemprop='price']"));
				
				String itemPrice = spanPrice == null ? "0.0" : spanPrice.asText();

				Item item = new Item();
				item.setTitle(itemName.asText());
				item.setDate("0001-01-01 00:00");
				item.setUrl(itemAnchor.getHrefAttribute());

				Double x = new Double(itemPrice.replace("£", "").replace(",", ""));
				x = x * 10.17;		// convert £ to HK $
				item.setPrice(x);
				item.setPortal("Preloved");
				
				result2.add(item);
			}
			result2.sort(Comparator.comparing(Item::getPrice));
			//client.close();

			return result2;
			
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	public List<Item> scrape(String keyword) {
		client.close();
		return merge(Cscrape(keyword),CAscrape(keyword));
		 
	}

	public List<Item> merge(List<Item> l1, List<Item> l2) {
	    for (int index1 = 0, index2 = 0; index2 < l2.size(); index1++) {
	        if (index1 == l1.size() || l1.get(index1).getPrice() > l2.get(index2).getPrice()) {
	            l1.add(index1, l2.get(index2++));
	        }
	    }
	    return l1;
	}  
}
