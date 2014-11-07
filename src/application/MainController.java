package application;


import java.util.EventObject;

import org.controlsfx.dialog.Dialogs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import application.MainApp;
import Cabin.Cabin;
import Cabin.Forgotten;
import Cabin.Reservation;

@SuppressWarnings("deprecation")
public class MainController {
    @FXML
    private TableView<Cabin> cabinTable,cabinTable2;
    @FXML
    private TableColumn<Cabin, String> cabinNameColumn, cabinNameColumn2;
   
    @FXML
    private TableView<Forgotten> forgottenTable;
    @FXML
    private TableColumn<Forgotten, String> forgottenMailColumn;
    
    @FXML
    private TableView<Reservation> reservationTable,sendTable;
    @FXML
    private TableColumn<Reservation, String> reservationTo, reservationFrom, reservationFirstName, reservationLastName,
    sendFirstName, sendLastName;
    //cabin lables
    @FXML
    private Label beds, tables, yearBuilt, terrain, reachableByBike, trip, guitar, waffleIron, hunting, fishing, specialties, woodStatus;
    @FXML
    private TextField to, subject;
    @FXML
    private TextArea body, mailBody;
    @FXML
    //dottene p� kartet
    private ImageView Flaakoia, Fosenkoia, Heinfjordstua, Hognabu, Holmsaakoia, Holvassgamma, Iglbu, 
    Kamtjonnkoia, Kraaklikaaten, Kvernmovollen,	Kaasen, Lynhogen, Mortenskaaten, Nicokoia, Rindalsloa,
    Selbukaaten, Sonvasskoia, Stabburet, Stakkslettbua, Telin, Taagaabu, Vekvessaetra, Ovensenget;
    //skriv om testing p� forskjellige os osv
    
    // referanse til main classen.
    private MainApp mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public MainController() {
    	
    //	Flaakoia.addEventHandler(MouseEvent.MOUSE_ENTERED, new MyButtonHandler());
    }


    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the cabin table with the two columns.
    	cabinNameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
    	forgottenMailColumn.setCellValueFactory(cellData -> cellData.getValue().getEmailProperty());
    	//reservationTo.setCellValueFactory(cellData -> cellData.getValue().getReservationList().get);
    	cabinNameColumn2.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
    	//change this later to name
    	sendFirstName.setCellValueFactory(cellData -> cellData.getValue().getEmailProperty());

    	
    	
        // Clear cabin details.
        showCabinDetails(null);
        showForgottenDetails(null);
        // Listen for selection changes and show the cabin details when changed.
        cabinTable.getSelectionModel().selectedItemProperty().addListener(
        		(observable, oldValue, newValue) -> showCabinDetails(newValue));

       // reservationTable.getSelectionModel().selectedItemProperty().addListener(
        		//(observable, oldValue, newValue) -> showReservationDetail(newValue));
       cabinTable2.getSelectionModel().selectedItemProperty().addListener(
        		(observable, oldValue, newValue) -> showReservationDetail(newValue));
       sendTable.getSelectionModel().selectedItemProperty().addListener(
       		(observable, oldValue, newValue) -> showMessagingDetail(newValue));
       forgottenTable.getSelectionModel().selectedItemProperty().addListener(
          		(observable, oldValue, newValue) -> showForgottenDetails(newValue));
    }




	/**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        cabinTable.setItems(mainApp.getCabinData());
        forgottenTable.setItems(mainApp.getForgottenData());
        cabinTable2.setItems(mainApp.getCabinData());
        sendTable.setItems(mainApp.getReservationData());
        
    }
    /**
     * Fills all text fields to show details about the cabin.
     * If the specified cabin is null, all text fields are cleared.
     * 
     * @param newValue the cabin or null
     */
    
    //showDetails klassene som s�rger for at riktig info vises i tabellene/labelene
    
    
    private void showMessagingDetail(Reservation newValue) {
    	to.setText(newValue.getEmail());
		
	}
    
    private void showReservationDetail(Cabin newValue){
    	reservationTable.setItems(newValue.getReservationList());
    	reservationTo.setCellValueFactory(cellData -> cellData.getValue().getEndDateProperty());
    	reservationFrom.setCellValueFactory(cellData -> cellData.getValue().getStartDateProperty());
    	reservationFirstName.setCellValueFactory(cellData -> cellData.getValue().getEmailProperty());
    }
    
    private void showForgottenDetails(Forgotten forgotten){
    	if(forgotten != null){
    		mailBody.setText(forgotten.getDescription());
    	}
    	else{
    		mailBody.setText("");
    	}
    }
    private void showCabinDetails(Cabin cabin) {
        if (cabin != null) {
            // Fill the labels with info from the cabin object.
        	beds.setText(cabin.getBednumber());
        	tables.setText(cabin.getTablenumber());
        	yearBuilt.setText(cabin.getYear());
        	terrain.setText(cabin.getTerrain());
        	reachableByBike.setText(cabin.getBike());     	
        	trip.setText(cabin.getTrip());
        	guitar.setText(cabin.getGuitar());
        	waffleIron.setText(cabin.getWaffleiron());
        	hunting.setText(cabin.getHunting());
        	fishing.setText(cabin.getFishing());
        	specialties.setText(cabin.getSpecialities());
        	woodStatus.setText(cabin.getWood());
        } else {
            // cabin is null, remove all the text.
        	beds.setText("");
        	tables.setText("");
        	yearBuilt.setText("");
        	terrain.setText("");
        	reachableByBike.setText("");
        	trip.setText("");
        	guitar.setText("");
        	waffleIron.setText("");
        	hunting.setText("");
        	fishing.setText("");
        	specialties.setText("");
        	woodStatus.setText("");
        }
    }
    /**
     * Called when the user clicks on the delete button.
     */
	@FXML
    private void handleDeleteCabin() {
        int selectedIndex = cabinTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            cabinTable.getItems().remove(selectedIndex);
        } else {
            // Nothing selected.
            Dialogs.create()
                .title("No Selection")
                .masthead("No cabin Selected")
                .message("Please select a cabin in the table.")
                .showWarning();
        }
    }
    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new cabin.
     */
  
    @FXML
    private void handleNewCabin() {
        Cabin tempCabin = new Cabin();
        boolean okClicked = mainApp.showCabinEditDialog(tempCabin);
        if (okClicked) {
            mainApp.getCabinData().add(tempCabin);
        }
    }
    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected cabin.
     */
	@FXML
    private void handleEditCabin() {
        Cabin selectedCabin = cabinTable.getSelectionModel().getSelectedItem();
        if (selectedCabin != null) {
            boolean okClicked = mainApp.showCabinEditDialog(selectedCabin);
            if (okClicked) {
                showCabinDetails(selectedCabin);
            }

        } else {
            // Nothing selected.
            Dialogs.create()
                .title("No Selection")
                .masthead("No cabin Selected")
                .message("Please select a cabin in the table.")
                .showWarning();
        }
    }
	@FXML
	private void handleMouseOver(Event evt){
		ImageView lol = (ImageView) evt.getSource();
		System.out.println(lol.getId());
	
	}
	@FXML
	private void handleMoveRemoved(Event evt){
		System.out.println("hade");
	}
	@FXML
	private void handleSendMail(){
		SendEmail.sendEmail(to.getText(), subject.getText(), body.getText());
	}




}
