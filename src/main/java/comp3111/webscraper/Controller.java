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
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.application.Platform;
import javafx.scene.control.TabPane;

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
	@FXML
	private TabPane tabPane;
	
	@FXML
	private Tab consoleTab;
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
     * getter for unit test
     */
    public String getLabelCount() {
    	return labelCount.getText();
    }
    
    public Boolean getRefine() {
    	return refineButton.isDisabled();
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
    
	// for summary task 1 min price url- dhleeab
    public static String findMinPrice(List<Item> result) {
     	double min = 1;	// by setting the initial min as 1 to handle the zero division
    	int size = result.size();
    	String min_url = "-";
    	
    	for(int i = 0; i < size; i++) {
			if(result.get(i).getPrice() != 0) {
				min = result.get(i).getPrice();
				min_url = result.get(i).getUrl().getText();
				break;
			}
    	}
    	
    	for (Item item : result) {
       		if(min > item.getPrice() && item.getPrice() != 0) {
    			min = item.getPrice();
    			min_url = item.getUrl().getText();
    		}
    	}
    	
    	return min_url;
    }
    
	// for summary task 1 average price - dhleeab
    public static double findAvgPrice(List<Item> result) {
    	int numOfItems = 0; 
    	double aggregatePrice = 0.0;
    	
    	for (Item item : result) {
    		if(item.getPrice() != 0) {
    			aggregatePrice += item.getPrice();
    			numOfItems++;
    		}
    	}
    	
    	return aggregatePrice == 0.0 ? 0.0 : aggregatePrice/numOfItems;
    }
    
	// for summary task 1 latest url - dhleeab
    public static String findLatest(List<Item> result) {
    	String latest_url = result.get(0).getUrl().getText();
    	String late_date = result.get(0).getDate();
    	
    	for(Item item : result) {
	    	try {
	    		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	    		Date this_date = formatter1.parse(item.getDate());
	    		Date min_date = formatter1.parse(late_date);
	    		if(min_date.compareTo(this_date) < 0) {
	    			late_date = item.getDate();
	    			latest_url = item.getUrl().getText();
	    		}
			} catch(ParseException e) {
				e.printStackTrace();
			} 	
    	}
		
    	return latest_url;
    }
    
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
    	
    	// TASK 1 - dhleeab
    	int size = result == null ? 0 : result.size();
    	
    	// for summary
    	if (size != 0) {
    		labelCount.setText(String.valueOf(size));
    		labelMin.setText(findMinPrice(result));
    		labelPrice.setText("$" + Double.toString(findAvgPrice(result)));
    		labelLatest.setText(findLatest(result));
    	} else {	
    		// Initialization of the data on the Summary Tab if there is no data 
	    	labelCount.setText("0");
    		labelMin.setText("-");
    		labelPrice.setText("-");
    		labelLatest.setText("-");	
    	}
    	
    	// ## TASK 4 - mkimaj ## ///////////////////////////////////////////////////////////////
    	
    	final ObservableList<Item> data = FXCollections.observableArrayList();	// Initialize ObservaleList used for
    																			// inserting data into the table in the Table tab
    	if (size != 0) {
	    	for (Item item : result) {
	
	    		// for console output
	    		output += item.getTitle() + "\tHK$" + item.getPrice() + "\t" + item.getPortal() + "\t" + item.getUrl().getText() + "\t" + item.getDate() + "\n";
	    		
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
	    		data.add(item);
	    	}
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
    	
     	// TASK 1 - dhleeab
    	int size = refinedResult == null ? 0 : refinedResult.size();
    	
    	// for summary
    	if (size != 0) {
    		labelCount.setText(String.valueOf(size));
    		labelMin.setText(findMinPrice(refinedResult));
    		labelPrice.setText("$" + Double.toString(findAvgPrice(refinedResult)));
    		labelLatest.setText(findLatest(refinedResult));
    	} else {	
    		// Initialization of the data on the Summary Tab if there is no data 
	    	labelCount.setText("0");
    		labelMin.setText("-");
    		labelPrice.setText("-");
    		labelLatest.setText("-");	
    	}

    	
   		final ObservableList<Item> data = FXCollections.observableArrayList();
    	if (size != 0) {
	   		for (Item item : refinedResult) {
	
	    		// for console output
	    		output += item.getTitle() + "\tHK$" + item.getPrice() + "\t" + item.getPortal() + "\t" + item.getUrl().getText() + "\t" + item.getDate() + "\n";
	    		
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
    public void actionNew() {
		refineButton.setDisable(true);
		revertButton.setDisable(true);
		
		System.out.println("Reverting to previous Search");
		String output = "";
    	
    	int size = reverting_result == null ? 0 : reverting_result.size(); 	
    	// for summary
    	if (size != 0) {
    		labelCount.setText(String.valueOf(size));
    		labelMin.setText(findMinPrice(reverting_result));
    		labelPrice.setText("$" + Double.toString(findAvgPrice(reverting_result)));
    		labelLatest.setText(findLatest(reverting_result));
    	} else {	
    		// Initialization of the data on the Summary Tab if there is no data 
	    	labelCount.setText("0");
    		labelMin.setText("-");
    		labelPrice.setText("-");
    		labelLatest.setText("-");	
    	}

		final ObservableList<Item> data = FXCollections.observableArrayList();
    	
		if(size != 0) {
	    	for (Item item : reverting_result) {
	    		
	    		// for console output
	    		output += item.getTitle() + "\tHK$" + item.getPrice() + "\t" + item.getPortal() + "\t" + item.getUrl().getText() + "\t" + item.getDate() + "\n";
	    		
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
		}
    	// for console output
    	textAreaConsole.setText(output);
    	
    	// insert item in the table as a single row
    	tableView.setItems(data);

    	first_item = true;
    	
    	reverting_result = null;
    }
    
    @FXML
    private void actionClose() {
    
    	refineButton.setDisable(true);
    	revertButton.setDisable(true);
		textFieldKeyword.setText("");
		SingleSelectionModel<Tab> selectionModel =tabPane.getSelectionModel();
		selectionModel.select(consoleTab);
		
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
    	labelMin.setText("<Lowest>");
   		labelPrice.setText("<AvgPrice>");
   		labelLatest.setText("<Latest>");
   		labelMin.setVisited(false);
   		labelLatest.setVisited(false);

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

