/**
 * team 6 - sudo korean
 */
package comp3111.webscraper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;  
import java.util.Date; 
import java.text.ParseException;

/**
 * 
 * @author kevinw
 *
 *
 * Controller class that manage GUI interaction. Please see document about JavaFX for details.
 * 
 */
public class Controller {
	
	// Summary /////////////////////////////////////////
	
    @FXML 
    private Label labelCount; 

    @FXML 
    private Label labelPrice; 

    @FXML 
    private Hyperlink labelMin; 

    @FXML 
    private Hyperlink labelLatest; 
    
    
    // Search ///////////////////////////////////////////

    @FXML
    private TextField textFieldKeyword;
    
    @FXML
    private Button refineButton;
    
    @FXML
    private Button goButton;
    
    @FXML
    private MenuItem revertButton;
    
    
    // Console //////////////////////////////////////////
    
    @FXML
    private TextArea textAreaConsole;
    
    
    // Table ////////////////////////////////////////////
    
    @FXML
    private TableView<Item> tableView;
    
    @FXML
    private TableColumn<Item, String> tTitle;
    
    @FXML
    private TableColumn<Item, String> tPrice;
    
    @FXML
    private TableColumn<Item, Hyperlink> tURL;
    
    @FXML
    private TableColumn<Item, String> tDate;

    
    private WebScraper scraper;
    private List<Item> prev_result;  
    private List<Item> reverting_result;
    private Boolean first_item = true;	// to distinguish which result should be used for revert & refine
    
    
    
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
    	
    	// Initialize the table columns in the Table tab
    	tTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
		tPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		tURL.setCellValueFactory(new PropertyValueFactory<>("url"));
		tDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		
    }
    
    
//    public String findMinPrice(List<Item> result) {
//    	
//    }
//    
//    
//    public String findAvgPrice(List<Item> result) {
//    	
//    }
//    
//    
//    public String findLatest(List<Item> result) {
//    	
//    }
    
    
    /**
     * Called when the search button is pressed.
     */
	@FXML
    public void actionSearch(ActionEvent event) {
		
		// disable refine & revert button when Go button is clicked
		refineButton.setDisable(false);
		revertButton.setDisable(false);
		
    	System.out.println("actionSearch: " + textFieldKeyword.getText());
    	String output = "";
    	
    	List<Item> result = scraper.scrape(textFieldKeyword.getText());
    	
    	System.out.println("Additional website chosen is Preloved");
    	
    	
    	// ## TASK 1 - dhleeab ## ///////////////////////////////////////////////////////////////
    	
    	double min = 1;	// by setting the initial min as 1 to handle the zero division
    	int numOfItems = 0; 
    	double avg_price = 0.0;
    	int size = result.size();
    	
    	String min_url = "-";
    	String latest_url = result.get(0).getUrl().getText();
    	String late_date = result.get(0).getDate();
    	
    	if (size != 0) {
    		for(int i = 0; i < size; i++) {
    			if(result.get(i).getPrice() != 0) {
    				min = result.get(i).getPrice();
    				break;
    			}
    		}
    	} else {
    		
    		// Initialization of the data on the Summary Tab
	    	labelCount.setText("0");
    		labelMin.setText("-");
    		labelPrice.setText("-");
    		labelLatest.setText("-");
    		
    	}
    	
    	// ## TASK 4 - mkimaj ## ///////////////////////////////////////////////////////////////
    	
    	final ObservableList<Item> data = FXCollections.observableArrayList();	// Initialize ObservaleList used for
    																			// inserting data into the table in the Table tab

    	for (Item item : result) {

    		// for console output
    		output += item.getTitle() + "\tHK$" + item.getPrice() + "\t" + item.getPortal() + "\t" + item.getUrl().getText() + "\t" + item.getDate() + "\n";
    		
    		
    		// find min price
    		
    		
    		// find avg price
    		
    		
    		// find latest post date 
    		
    		
    		// EventHandler for click action on the URL in the table cell
    		EventHandler<ActionEvent> hyperlinkHandler_action = new EventHandler<ActionEvent>() {
    		    public void handle(ActionEvent event) {
    		    	try {
    					Desktop.getDesktop().browse(new URL(item.getUrl().getText()).toURI());	// open the browser for given url
    				} catch (IOException | URISyntaxException e) {
    					e.printStackTrace();
    				}
    		    }
    		};
    		
    		// put EventHandler on the url attribute for item
    		item.getUrl().setOnAction(hyperlinkHandler_action);
    		
    		// add item into ObservableList
    		data.add(item);
    	}
    	
    	// for recalling result list for refine search and revert
    	if(first_item) {
    		prev_result = result;
    		first_item = false;
    	} else {
    		reverting_result = prev_result;
    		prev_result = result;
    	}
    	
    	// for console output
    	textAreaConsole.setText(output);
    	
    	// insert item in the table as a single row
    	tableView.setItems(data);
    	
    	// for summary
    	if(size != 0) {
    		// number of the scraped data
	    	labelCount.setText(String.valueOf(size));
	    	
	    	// average price for the search
	    	
	    	
    		// lowest price for the search
	    	labelMin.setText(min_url);
	    	
	    	// latest post for the search
	    	labelLatest.setText(latest_url);
    	}
    }
    
	/**
     * Called when the refine button is pressed.
     */
	
	// ## TASK 5 - mkimaj ## ///////////////////////////////////////////////////////////////
	
	@FXML
    private void refineSearch(ActionEvent event) {
		
		// disable refine & revert button when Refine button is clicked
		refineButton.setDisable(true);
		revertButton.setDisable(true);
		
		String keyword = textFieldKeyword.getText();
		String output = "";
		
		final List<Item> refinedResult = new ArrayList<Item> ();
		
		System.out.println("refineSearch: " + keyword);

		// filter searched result by checking whether the title contains the keyword 
    	for (Item item : prev_result) {
    		boolean TitleContains = item.getTitle().toLowerCase().indexOf(keyword) != -1? true: false;
    		if (TitleContains == true) { refinedResult.add(item); }
    	}
    	
    	
    	// ## TASK 1 - dhleeab ## ///////////////////////////////////////////////////////////////
    	
    	double min = 1;	// by setting the initial min as 1 to handle the zero division
    	int numOfItems = 0; 
    	double avg_price = 0.0;
    	int size = refinedResult.size();
    	
    	String min_url = "-";
    	String latest_url = refinedResult.get(0).getUrl().getText();
    	String late_date = refinedResult.get(0).getDate();
    	
    	if (size != 0) {
    		for(int i = 0; i < size; i++) {
    			if(refinedResult.get(i).getPrice() != 0) {
    				min = refinedResult.get(i).getPrice();
    				break;
    			}
    		}
    	} else {
    		
    		// Initialization of the data on the Summary Tab
	    	labelCount.setText("0");
    		labelMin.setText("-");
    		labelPrice.setText("-");
    		labelLatest.setText("-");
    		
    	}
    	
   		final ObservableList<Item> data = FXCollections.observableArrayList();
    	
   		for (Item item : refinedResult) {

    		// for console output
    		output += item.getTitle() + "\tHK$" + item.getPrice() + "\t" + item.getPortal() + "\t" + item.getUrl().getText() + "\t" + item.getDate() + "\n";
    		
    		
    		// find min price
    		
    		
    		// find avg price
    		
    		
    		// find latest post date 
    		
    		
    		// EventHandler for click action on the URL in the table cell
    		EventHandler<ActionEvent> hyperlinkHandler_action = new EventHandler<ActionEvent>() {
    		    public void handle(ActionEvent event) {
    		    	try {
    					Desktop.getDesktop().browse(new URL(item.getUrl().getText()).toURI());	// open the browser for given url
    				} catch (IOException | URISyntaxException e) {
    					e.printStackTrace();
    				}
    		    }
    		};
    		
    		// put EventHandler on the url attribute for item
    		item.getUrl().setOnAction(hyperlinkHandler_action);
    		
    		// add item into ObservableList
    		data.add(item);
    	}
    	
    	// for recalling result list for refine search and revert
    	if(first_item) {
    		prev_result = refinedResult;
    		first_item = false;
    	} else {
    		reverting_result = prev_result;
    		prev_result = refinedResult;
    	}
    	
    	// for console output
    	textAreaConsole.setText(output);
    	
    	// insert item in the table as a single row
    	tableView.setItems(data);
    	
    	// for summary
    	if(size != 0) {
    		// number of the scraped data
	    	labelCount.setText(String.valueOf(size));
	    	
	    	// average price for the search
	    	
	    	
    		// lowest price for the search
	    	labelMin.setText(min_url);
	    	
	    	// latest post for the search
	    	labelLatest.setText(latest_url);
    	}
    }
	
	// EventHandler for the clicking on the lowest price url in Summary tab
	public void MinClick(ActionEvent event) {
        try {
			Desktop.getDesktop().browse(new URL(labelMin.getText()).toURI());
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	// EventHandler for the clicking on the latest post url in Summary tab
	public void LatestClick(ActionEvent event) {
		try {
			Desktop.getDesktop().browse(new URL(labelLatest.getText()).toURI());
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

    
    /**
     * Called when the new button is pressed. Very dummy action - print something in the command prompt.
     */
	
	// ## TASK 6 - dhleeab ## ///////////////////////////////////////////////////////////////
	
    @FXML
    private void actionNew() {
		refineButton.setDisable(true);
		revertButton.setDisable(true);
		
		System.out.println("Reverting to previous Search");
		String output = "";
    	
    	double min = -1;
    	int numOfItems = 0;
    	double avg_price = 0.0;
    	int size = reverting_result.size();
    	
    	String min_url = "-";
    	String latest_url = "-"; 
    	String late_date = "-";
    	
    	if(size != 0) {
    		for(int i = 0; i < size; i++) {
    			if (i == 0) {
    				latest_url = reverting_result.get(0).getUrl().getText();
    				late_date = reverting_result.get(0).getDate();
    			}
    			if(reverting_result.get(i).getPrice() != 0) {
    				min = reverting_result.get(i).getPrice();
    				break;
    			}
    		}
    	} else {
	    	labelCount.setText("0");
    		labelMin.setText("-");
    		labelPrice.setText("-");
    		labelLatest.setText("-");
    	}
		
		final ObservableList<Item> data = FXCollections.observableArrayList();
    	
    	for (Item item : reverting_result) {
    		
    		// for console output
    		output += item.getTitle() + "\tHK$" + item.getPrice() + "\t" + item.getPortal() + "\t" + item.getUrl().getText() + "\t" + item.getDate() + "\n";
    		
    		
    		// find min price
    		
    		
    		// find avg price
    		
    		
    		// find latest post date 
    		
    		
    		// EventHandler for click action on the URL in the table cell
    		EventHandler<ActionEvent> hyperlinkHandler_action = new EventHandler<ActionEvent>() {
    		    public void handle(ActionEvent event) {
    		    	try {
    					Desktop.getDesktop().browse(new URL(item.getUrl().getText()).toURI());	// open the browser for given url
    				} catch (IOException | URISyntaxException e) {
    					e.printStackTrace();
    				}
    		    }
    		};
    		
    		// put EventHandler on the url attribute for item
    		item.getUrl().setOnAction(hyperlinkHandler_action);
    		
    		// add item into ObservableList
    		data.add(item);
    	}
    	
    	// for console output
    	textAreaConsole.setText(output);
    	
    	// insert item in the table as a single row
    	tableView.setItems(data);
    	
    	// for summary
    	if(size != 0) {
    		// number of the scraped data
	    	labelCount.setText(String.valueOf(size));
	    	
	    	// average price for the search
	    	
	    	
    		// lowest price for the search
	    	labelMin.setText(min_url);
	    	
	    	// latest post for the search
	    	labelLatest.setText(latest_url);
    	}
    	
    	first_item = true;
    	reverting_result = null;
    }
    
    @FXML
    private void actionClose() {
    
    	refineButton.setDisable(true);
    	revertButton.setDisable(true);
    	if(prev_result != null) {
    		prev_result.clear();
    	}
    	if(reverting_result != null) {
    		reverting_result.clear();
    	}
    	first_item = true;
    	
    	String output = "";
    	
    	// summary 
	    labelCount.setText("<total>");
    	labelMin.setText("<AvgPrice>");
   		labelPrice.setText("<Lowest>");
   		labelLatest.setText("<Latest>");
    
    	final ObservableList<Item> data = FXCollections.observableArrayList();

    	// for console
    	textAreaConsole.setText(output);
    	
    	// for table
    	tableView.setItems(data);
    }
    
    @FXML
    private void actionQuit() {
    	System.exit(0);
        Platform.exit();
    }
    
    @FXML
    private void actionAboutTeam() {
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Sudo Korean Introduction");
    	alert.setHeaderText("Team Information");
    	alert.setContentText("KIM, Tae Woo/ tkimae@connect.ust.hk/ tkimae \n"
    			+ "KIM, MinKyung/ mkimaj@connect.ust.hk/ mkimaj \n"
    			+ "LEE, Do Hyun/ dhleeab@connect.ust.hk/ dhleeab "
    			);

    	alert.showAndWait();
    }
}

