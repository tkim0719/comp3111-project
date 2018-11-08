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
    private void actionSearch() {
    	System.out.println("actionSearch: " + textFieldKeyword.getText());
    	List<Item> result = scraper.scrape(textFieldKeyword.getText());
    	String output = "";
    	
    	tTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
		tPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		tURL.setCellValueFactory(new PropertyValueFactory<>("url"));
		tDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		
		final ObservableList<Table_Item> data = FXCollections.observableArrayList();
		
    	for (Item item : result) {
    		output += item.getTitle() + "\t$" + item.getPrice() + "\t" + item.getPortal() + "\t" + item.getUrl() + "\t" + item.getDate() + "\n";
    		data.add(new Table_Item(item.getTitle(), item.getPrice(), item.getUrl(), item.getDate()));
    	}
    	textAreaConsole.setText(output);
    	tableView.setItems(data);
    }
    
//    @FXML
//    private void refineSearch() {
//    	System.out.println("refineSearch: " + textFieldKeyword.getText());
//    	List<Item> result = scraper.scrape(textFieldKeyword.getText());
//		List<Item> refined_result;
//    	String output = "";
//    	for (Item item : result) {
//    		output += item.getTitle() + "\t" + item.getPrice() + "\t" + item.getUrl() + "\n";
//    	}
//    	textAreaConsole.setText(output);
//    }
    
    /**
     * Called when the new button is pressed. Very dummy action - print something in the command prompt.
     */
    @FXML
    private void actionNew() {
    	System.out.println("actionNew");
    }
}

