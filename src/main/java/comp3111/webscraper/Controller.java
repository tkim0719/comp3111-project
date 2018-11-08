/**
 * team 6 - sudo korean
 */
package comp3111.webscraper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
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
    private TableView<Item> tableView;
    
    @FXML
    private TableColumn<Item, String> tTitle;
    
    @FXML
    private TableColumn<Item, String> tPrice;
    
    @FXML
    private TableColumn<Item, String> tURL;
    
    @FXML
    private TableColumn<Item, String> tDate;
    
    
    @FXML
    private Button goButton;
    private Button refineButton;
    
    private WebScraper scraper;
    private List<Item> prev_result;
    
    
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
     * Called when the search button is pressed.
     */
	@FXML
    public void actionSearch() {
    	System.out.println("actionSearch: " + textFieldKeyword.getText());
    	List<Item> result = scraper.scrape(textFieldKeyword.getText());
    	String output = "";
    	
    	// dhleeab
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
    	
		// task 4 - mkimaj
//		final ObservableList<Table_Item> data = FXCollections.observableArrayList();
    	final ObservableList<Item> data = FXCollections.observableArrayList();

    	for (Item item : result) {
    		// for console
    		output += item.getTitle() + "\t$" + item.getPrice() + "\t" + item.getPortal() + "\t" + item.getUrl() + "\t" + item.getDate() + "\n";
    		
    		// for summary
    		if(item.getPrice() != 0) {
    			avg_price += item.getPrice();
    			numOfItems++;
    		}

    		if(min > item.getPrice() && item.getPrice() != 0) {
    			min = item.getPrice();
    			min_url = item.getUrl();
    		}
    		
    		// for table 
    		data.add(item);
    	}
    	// for refine search
    	prev_result = result;
    	
    	// for console
    	textAreaConsole.setText(output);
    	
    	// for table
    	tableView.setItems(data);
    	
    	// for summary
    	if(size != 0) {
	    	labelCount.setText(String.valueOf(size));
	    	labelPrice.setText("$ " + String.valueOf(avg_price/numOfItems));
	    	labelMin.setText(min_url);
	    	labelLatest.setText(latest_url);
    	}
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
		final ObservableList<Item> data = FXCollections.observableArrayList();
    	
    	for (Item item : refinedResult) {
    		// for console
    		output += item.getTitle() + "\t$" + item.getPrice() + "\t" + item.getPortal() + "\t" + item.getUrl() + "\t" + item.getDate() + "\n";
    		
    		// for summary
    		if(min > item.getPrice()) {
    			min = item.getPrice();
    			min_url = item.getUrl();
    		}
    		
    		// for table 
    		data.add(item);
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

