/**
 * 
 */
package comp3111.webscraper;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
//import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.Hyperlink;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;


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
    
    
//    @FXML
//    private TableView<Item> tableView;
//    
//    @FXML
//    private TableColumn<Item, String> tTitle;
//    
//    @FXML
//    private TableColumn<Item, String> tPrice;
//    
//    @FXML
//    private TableColumn<Item, String> tURL;
//    
//    @FXML
//    private TableColumn<Item, String> tDate;
    
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
     * Called when the search button is pressed.
     */
    @FXML
    private void actionSearch() {
    	System.out.println("actionSearch: " + textFieldKeyword.getText());
    	List<Item> result = scraper.scrape(textFieldKeyword.getText());
    	String output = "";
    	int size = result.size();
    	double min = (size ! = 0) ? result.get(0).getPrice() : 0;
    	String min_url = "-";
    	String latest_url = "-";
    	for (Item item : result) {
    		output += item.getTitle() + "\t" + item.getPrice() + "\t" + item.getUrl() + "\n";
    		if(min > item.getPrice()) {
    			min = item.getPrice();
    			min_url = item.getUrl();
    		}
    	}
    	textAreaConsole.setText(output);
    	////////////////////////////////
    	labelCount.setText(String.valueOf(size));
    	labelMin = new Hyperlink(min_url);
    	
    	
    	@Override
    	public void handle(ActionEvent event) {
    		getHostServices().showDocument(min_url);
    	}
    	labelLatest = new Hyperlink(latest_url);
    	
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

