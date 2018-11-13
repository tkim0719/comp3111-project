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
    private TableColumn<Item, Hyperlink> tURL;
    
    @FXML
    private TableColumn<Item, String> tDate;
    
    @FXML
    private Button refineButton;
    
    @FXML
    private Button goButton;
    
    @FXML
    private MenuItem revertButton;
    
    
    private WebScraper scraper;
    private List<Item> prev_result;  
    private List<Item> reverting_result;
    private Boolean first_item = true;
    
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
    public void actionSearch(ActionEvent event) {
		// disable refine button
		refineButton.setDisable(false);
		revertButton.setDisable(false);
		
    	System.out.println("actionSearch: " + textFieldKeyword.getText());
    	List<Item> result = scraper.scrape(textFieldKeyword.getText());
    	System.out.println("Additional website chosen is Carousell");
    	String output = "";
    	
    	// task 1 - dhleeab 
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
    	String latest_url = result.get(0).getUrl().getText();
    	double avg_price = 0.0;
    	int numOfItems = 0; 
    	String late_date = result.get(0).getDate();
    	
		// task 4 - mkimaj
    	final ObservableList<Item> data = FXCollections.observableArrayList();

    	for (Item item : result) {

    		// for console
    		output += item.getTitle() + "\t$" + item.getPrice() + "\t" + item.getPortal() + "\t" + item.getUrl().getText() + "\t" + item.getDate() + "\n";
    		
    		// for summary task 1 - dhleeab
    		if(item.getPrice() != 0) {
    			avg_price += item.getPrice();
    			numOfItems++;
    		}

    		if(min > item.getPrice() && item.getPrice() != 0) {
    			min = item.getPrice();
    			min_url = item.getUrl().getText();
    		}
    		
    		EventHandler<ActionEvent> hyperlinkHandler_action = new EventHandler<ActionEvent>() {
    		    public void handle(ActionEvent event) {
    		    	try {
    					Desktop.getDesktop().browse(new URL(item.getUrl().getText()).toURI());
    				} catch (IOException | URISyntaxException e) {
    					e.printStackTrace();
    				}
    		    }
    		};
    		
    		item.getUrl().setOnAction(hyperlinkHandler_action);

    		try {
	    		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	    		Date this_date = formatter1.parse(item.getDate());
	    		Date min_date = formatter1.parse(late_date);
	    		if(min_date.compareTo(this_date) > 0) {
	    			late_date = item.getDate();
	    			latest_url = item.getUrl().getText();
	    		}
    		} catch(ParseException e) {
    			e.printStackTrace();
    		}

    		// for table 
    		data.add(item);
    	}
    	
    	// for refine search and revert
    	if(first_item) {
    		prev_result = result;
    		first_item = false;
    	} else {
    		reverting_result = prev_result;
    		prev_result = result;
    	}
    	
    	// for console
    	textAreaConsole.setText(output);
    	
    	// for table
    	tableView.setItems(data);
    	
    	// for summary
    	if(size != 0) {
	    	labelCount.setText(String.valueOf(size));
	    	if (avg_price == 0.0) {
	    		labelPrice.setText("$ " + Double.toString(0.0));
	    	}
	    	else {
	    		labelPrice.setText("$ " + Double.toString(avg_price/numOfItems));
	    	}
	    	labelMin.setText(min_url);
	    	labelLatest.setText(latest_url);
    	}
    }
    
	/**
     * Called when the refine button is pressed.
     */
	
	// task 5 - mkimaj
	@FXML
    private void refineSearch(ActionEvent event) {
		// disable refine button
		refineButton.setDisable(true);
		
		String keyword = textFieldKeyword.getText();
		System.out.println("refineSearch: " + keyword);
		final List<Item> refinedResult = new ArrayList<Item> ();

    	for (Item item : prev_result) {
    		boolean TitleContains = item.getTitle().toLowerCase().indexOf(keyword) != -1? true: false;
    		if (TitleContains == true) { refinedResult.add(item); }
    	}
    	
    	// task 1 - dhleeab
    	int size = refinedResult.size();
    	double min = -1;
    	String latest_url = "-"; // refineResult.get(0) gets error don't know why so I changed this part @dhleeab
    	String late_date = "-"; 
    	if(size != 0) {
    		for(int i = 0; i < size; i++) {
    			if (i == 0) {
    				latest_url = refinedResult.get(0).getUrl().getText();
    				late_date = refinedResult.get(0).getDate();
    			}
    			if(refinedResult.get(i).getPrice() != 0) {
    				min = refinedResult.get(i).getPrice();
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
    	double avg_price = 0.0;
    	int numOfItems = 0;

		String output = "";
		final ObservableList<Item> data = FXCollections.observableArrayList();
    	
    	for (Item item : refinedResult) {
    		// for console
    		output += item.getTitle() + "\t$" + item.getPrice() + "\t" + item.getPortal() + "\t" + item.getUrl().getText() + "\t" + item.getDate() + "\n";
    		
    		// for summary
    		if(item.getPrice() != 0) {
    			avg_price += item.getPrice();
    			numOfItems++;
    		}

    		if(min > item.getPrice() && item.getPrice() != 0) {
    			min = item.getPrice();
    			min_url = item.getUrl().getText();
    		}
    		
    		EventHandler<ActionEvent> hyperlinkHandler_refine = new EventHandler<ActionEvent>() {
    		    public void handle(ActionEvent event) {
    		    	try {
    					Desktop.getDesktop().browse(new URL(item.getUrl().getText()).toURI());
    				} catch (IOException | URISyntaxException e) {
    					e.printStackTrace();
    				}
    		    }
    		};
    		
    		try {
	    		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	    		Date this_date = formatter1.parse(item.getDate());
	    		Date min_date = formatter1.parse(late_date);
	    		if(min_date.compareTo(this_date) > 0) {
	    			late_date = item.getDate();
	    			latest_url = item.getUrl().getText();
	    		}
    		} catch(ParseException e) {
    			e.printStackTrace();
    		}
    		
    		item.getUrl().setOnAction(hyperlinkHandler_refine);
    		
    		// for table 
    		data.add(item);
    	}
    	// for console
    	textAreaConsole.setText(output);
    	
    	// for table
    	tableView.setItems(data);
    	
    	// for summary
    	if(size != 0) {
	    	labelCount.setText(String.valueOf(size));
	    	if (avg_price == 0) {
	    		labelPrice.setText("$ " + Double.toString(0.0));
	    	}
	    	else {
	    		labelPrice.setText("$ " + Double.toString(avg_price/numOfItems));
	    	}
	    	labelMin.setText(min_url);
	    	labelLatest.setText(latest_url);
    	}
    	
	}
	
	public void MinClick(ActionEvent event) {
        try {
			Desktop.getDesktop().browse(new URL(labelMin.getText()).toURI());
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
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
	// task 6 - dhleeab
	// add revering button disable
    @FXML
    private void actionNew() {
		refineButton.setDisable(true);
		revertButton.setDisable(true);
		
		System.out.println("previous Search");
    	
    	int size = reverting_result.size();
    	double min = -1;
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
    	
    	String min_url = "-";
    	double avg_price = 0.0;
    	int numOfItems = 0;

		String output = "";
		final ObservableList<Item> data = FXCollections.observableArrayList();
    	
    	for (Item item : reverting_result) {
    		// for console
    		output += item.getTitle() + "\t$" + item.getPrice() + "\t" + item.getPortal() + "\t" + item.getUrl().getText() + "\t" + item.getDate() + "\n";
    		
    		// for summary
    		if(item.getPrice() != 0) {
    			avg_price += item.getPrice();
    			numOfItems++;
    		}

    		if(min > item.getPrice() && item.getPrice() != 0) {
    			min = item.getPrice();
    			min_url = item.getUrl().getText();
    		}
    		
    		EventHandler<ActionEvent> hyperlinkHandler_refine = new EventHandler<ActionEvent>() {
    		    public void handle(ActionEvent event) {
    		    	try {
    					Desktop.getDesktop().browse(new URL(item.getUrl().getText()).toURI());
    				} catch (IOException | URISyntaxException e) {
    					e.printStackTrace();
    				}
    		    }
    		};
    		
    		try {
	    		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	    		Date this_date = formatter1.parse(item.getDate());
	    		Date min_date = formatter1.parse(late_date);
	    		if(min_date.compareTo(this_date) > 0) {
	    			late_date = item.getDate();
	    			latest_url = item.getUrl().getText();
	    		}
    		} catch(ParseException e) {
    			e.printStackTrace();
    		}
    		
    		item.getUrl().setOnAction(hyperlinkHandler_refine);
    		
    		// for table 
    		data.add(item);
    	}
    	// for console
    	textAreaConsole.setText(output);
    	
    	// for table
    	tableView.setItems(data);
    	
    	// for summary
    	if(size != 0) {
	    	labelCount.setText(String.valueOf(size));
	    	if (avg_price == 0) {
	    		labelPrice.setText("$ " + Double.toString(0.0));
	    	}
	    	else {
	    		labelPrice.setText("$ " + Double.toString(avg_price/numOfItems));
	    	}
	    	labelMin.setText(min_url);
	    	labelLatest.setText(latest_url);
    	}
    	
    	first_item = true;
    	reverting_result = null;
    }
    
    @FXML
    private void actionClose() {
//    	System.out.println("actionClose");
//    	primaryStage.close();
//    	Platform.runLater( () -> new ReloadApp().start( new Stage() ) );
    	
//    	WebScraperApplication app = new WebScraperApplication();	
//    	try {
//			app.start(null);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	
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

