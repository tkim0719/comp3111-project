/**
 * 
 */
package comp3111.webscraper;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.Hyperlink;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author kevinw
 *
 *
 * Controller class that manage GUI interaction. Please see document about JavaFX for details.
 * 
 */
public class Controller {

    @FXML 
    private Label labelCount; 

    @FXML 
    private Label labelPrice; 

    @FXML 
    private Hyperlink labelMin; 

    @FXML 
    private Hyperlink labelLatest; 

    @FXML
    private TextField textFieldKeyword;
    
    @FXML
    private TextArea textAreaConsole;
    
    @FXML
    private TableView<Table_Item> tableView;
    
    @FXML
    private TableColumn<Table_Item, String> tTitle;
    
    @FXML
    private TableColumn<Table_Item, String> tPrice;
    
    @FXML
    private TableColumn<Table_Item, String> tURL;
    
    @FXML
    private TableColumn<Table_Item, String> tDate;
    
    private List<Item> prev_result;
    
    private WebScraper scraper;
    
    /**
     * Default controller
     */
    public Controller() {
    	scraper = new WebScraper();
    }

    /**
     * Default initializer. It is empty.
     */
    @FXML
    private void initialize() {
    	tTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
		tPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		tURL.setCellValueFactory(new PropertyValueFactory<>("url"));
		tDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    }
    
    /** 
     * Table_Item class
     * @author minkyungkim
     *
     */
    public static class Table_Item {
    	
    	public final SimpleStringProperty title;
    	public final SimpleDoubleProperty price;
    	public final SimpleStringProperty url;
    	public final SimpleStringProperty date;
     
    	public Table_Item(String title, Double price, String url, String date) {
            this.title = new SimpleStringProperty(title);
            this.price = new SimpleDoubleProperty(price);
            this.url = new SimpleStringProperty(url);
            this.date = new SimpleStringProperty(date);
        }
    	
    	public String getTitle() { return title.get(); }
        public void setTitle(String title) { this.title.set(title); }
 
        public Double getPrice() { return price.get(); }
        public void setPrice(Double price) { this.price.set(price); }
 
        public String getUrl() { return url.get(); }
        public void setUrl(String url) { this.url.set(url); }
        
        public String getDate() { return date.get(); }
        public void setDate(String date) { this.date.set(date); }
    }
    
    /**
     * Called when the search button is pressed.
     */
	@FXML
    public void actionSearch() {
    	System.out.println("actionSearch: " + textFieldKeyword.getText());
    	List<Item> result = scraper.scrape(textFieldKeyword.getText());
    	String output = "";
    	
    	// dhleeab
    	int size = result.size();
    	double min = (size != 0) ? result.get(0).getPrice() : 0;
    	String min_url = "-";
    	String latest_url = "-";
    	
		// task 4 - mkimaj
		final ObservableList<Table_Item> data = FXCollections.observableArrayList();

    	for (Item item : result) {
    		// for console
    		output += item.getTitle() + "\t$" + item.getPrice() + "\t" + item.getPortal() + "\t" + item.getUrl() + "\t" + item.getDate() + "\n";
    		
    		// for summary
    		if(min > item.getPrice()) {
    			min = item.getPrice();
    			min_url = item.getUrl();
    		}
    		
    		// for table 
    		data.add(new Table_Item(item.getTitle(), item.getPrice(), item.getUrl(), item.getDate()));
    	}
    	// for refine search
    	prev_result = result;
    	// for console
    	textAreaConsole.setText(output);
    	// for table
    	tableView.setItems(data);
    	// for summary
    	labelCount.setText(String.valueOf(size));
    	labelMin.setText(min_url);
    	labelLatest = new Hyperlink(latest_url);
    }
    
	/**
     * Called when the refine button is pressed.
     */
	
	// task 5 - mkimaj
	@FXML
    private void refineSearch() {
		String keyword = textFieldKeyword.getText();
		System.out.println("refineSearch: " + keyword);
		final List<Item> refinedResult = new ArrayList();

    	for (Item item : prev_result) {
    		boolean TitleContains = item.getTitle().toLowerCase().indexOf(keyword) != -1? true: false;
    		if (TitleContains == true) { refinedResult.add(item); }
    	}
    	
    	// dhleeab
    	int size = refinedResult.size();
    	double min = (size != 0) ? refinedResult.get(0).getPrice() : 0;
    	String min_url = "-";
    	String latest_url = "-";

		String output = "";
		final ObservableList<Table_Item> data = FXCollections.observableArrayList();
    	
    	for (Item item : refinedResult) {
    		// for console
    		output += item.getTitle() + "\t$" + item.getPrice() + "\t" + item.getPortal() + "\t" + item.getUrl() + "\t" + item.getDate() + "\n";
    		
    		// for summary
    		if(min > item.getPrice()) {
    			min = item.getPrice();
    			min_url = item.getUrl();
    		}
    		
    		// for table 
    		data.add(new Table_Item(item.getTitle(), item.getPrice(), item.getUrl(), item.getDate()));
    	}
    	// for console
    	textAreaConsole.setText(output);
    	// for table
    	tableView.setItems(data);
    	// for summary
    	labelCount.setText(String.valueOf(size));
    	labelMin.setText(min_url);
    	labelLatest = new Hyperlink(latest_url);
    }
    
    /**
     * Called when the new button is pressed. Very dummy action - print something in the command prompt.
     */
    @FXML
    private void actionNew() {
    	System.out.println("actionNew");
    }
}

