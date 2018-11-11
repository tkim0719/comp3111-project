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
    	System.out.println("Additional website chosen is Carousell");
    	String output = "";
    	for (Item item : result) {
    		output += item.getTitle() + "\t" + item.getPrice() + "\t" + item.getPortal() + "\t" + item.getUrl() + "\n";
    	}
    	textAreaConsole.setText(output);
    	////////////////////////////////
    	labelCount.setText("Hi");
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

