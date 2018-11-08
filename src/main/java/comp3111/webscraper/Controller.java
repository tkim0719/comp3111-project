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
    	double min = -1;
    	if(size != 0) {
    		for(int i = 0; i < size; i++) {
    			if(result.get(i).getPrice() != 0) {
    				min = result.get(i).getPrice();
    				break;
    			}
    		}
    	} else {
	    	labelCount.setText("0");
    		labelMin.setText("-");
    		labelPrice.setText("-");
    		labelLatest.setText("-");
    	}
    	
    	String min_url = "-";
    	String latest_url = result.get(0).getDate();
    	double avg_price = 0.0;
    	int numOfItems = 0; 
    	
    	for (Item item : result) {
    		output += item.getTitle() + "\t" + item.getPrice() + "\t" + item.getUrl() + "\n";
    		if(item.getPrice() != 0) {
    			avg_price += item.getPrice();
    			numOfItems++;
    		}
    		if(min > item.getPrice() && item.getPrice() != 0) {
    			min = item.getPrice();
    			min_url = item.getUrl();
    		}
    		
    	}
    	
    	textAreaConsole.setText(output);
    	
    	if(size != 0) {
	    	labelCount.setText(String.valueOf(size));
	    	labelPrice.setText("$ " + String.valueOf(avg_price/numOfItems));
	    	labelMin.setText(min_url);
	    	labelLatest.setText(latest_url);
    	}
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

