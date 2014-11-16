package gui;




import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;


import javafx.scene.control.*;

import org.controlsfx.dialog.Dialogs;


import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.StringConverter;
//import gui.MainApp;
import sqldata.Cabin;
import sqldata.Item;
import sqldata.ItemType;
import sqldata.MailInterface;
import sqldata.Reservation;
import sqldata.Sent;


@SuppressWarnings("deprecation")
public class MainController{


    @FXML
    private ChoiceBox woodLevelBox;
    @FXML
    private TableView<ItemType> itemTable;
    @FXML
    private TableColumn<ItemType, String> itemNameColumn, itemAmountColumn;
	@FXML
	private TableView<Item> cabinItemTable, itemSingleTable;
	@FXML
	private TableColumn<Item, String> cabinItemName, cabinItemAmount,itemSingleAmountColumn, itemSingleNameColumn;
	@FXML
	private TableView<Cabin> woodTable;
	@FXML
	private TableColumn<Cabin,String> woodCabinName, woodLevel;
	@FXML
	private TableView<Cabin> cabinTable,cabinTable2;
	@FXML
	private TableColumn<Cabin, String> cabinNameColumn, cabinNameColumn2;
    @FXML
    private TableView<Sent> outboxTable;
    @FXML
    private TableColumn<Sent, String> outboxEmailColumn, outboxSubjectColumn;


	@FXML
	private Button reservationAdd, reservationRemove, reservationEdit, map;

	@FXML
	private TableView<MailInterface> forgottenTable;
	@FXML
	private TableColumn<MailInterface, String> forgottenMailColumn, forgottenSubjectColumn;

	@FXML
	private TableView<Reservation> reservationTable,sendTable,mainResTable;
	@FXML
	private TableColumn<Reservation, String> reservationFirstName, reservationLastName,
	sendFirstName, sendLastName,mainResName, mainResFirstName, mainResLastName;
    @FXML
    TableColumn<Reservation, LocalDate> reservationTo,mainResFrom, mainResTo, reservationFrom;

	//cabin lables
	@FXML
	private Label woodLabelLevel1,woodLabelName, woodLabelLevel, woodLevelDugnad, beds, tables, yearBuilt, terrain, reachableByBike, trip, guitar, waffleIron, hunting, fishing, specialties, woodStatus;
	@FXML
	private TextField to, subject;
	@FXML
	private TextArea body, mailBody, outboxBody;
	@FXML
	private DatePicker reservationDateFrom, reservationDateTo, reservationDateFrom1, reservationDateTo1;

    private int removing = 0;

	// referanse til main classen.
	private MainApp mainApp;

	public MainController() {


	}

    /**
     * Lager en referanse til mainApp classen og legger observable listene til de riktige tabellene
     * @param mainApp
     */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

		//legger observable listene til i riktig tabell
		cabinTable.setItems(mainApp.getCabinData());
		forgottenTable.setItems(mainApp.getForgottenData());
		cabinTable2.setItems(mainApp.getCabinData());
		sendTable.setItems(mainApp.getReservationData());
		mainResTable.setItems(mainApp.getReservationData());
		woodTable.setItems(mainApp.getCabinData());
        itemTable.setItems(mainApp.getItemTypeData());
        outboxTable.setItems(mainApp.getOutBox());
        //denne skal nok flyttes
		woodLevelAverage();

	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */

    /**
     * Initialiserer kolonnene og tabellene
     */
	@FXML
	private void initialize() {


		cabinNameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());

		forgottenMailColumn.setCellValueFactory(cellData -> cellData.getValue().getEmailProperty());
        forgottenSubjectColumn.setCellValueFactory(cellData -> cellData.getValue().getSubject());

		cabinNameColumn2.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
		//change this later to name
		sendFirstName.setCellValueFactory(cellData -> cellData.getValue().getFirstNameProperty());
		sendLastName.setCellValueFactory(cellData -> cellData.getValue().getLastNameProperty());
        outboxEmailColumn.setCellValueFactory(cellData -> cellData.getValue().getToProperty());
        outboxSubjectColumn.setCellValueFactory(cellData -> cellData.getValue().getSubjectProperty());



		mainResName.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
		mainResFrom.setCellValueFactory(cellData -> cellData.getValue().getStartLocalDateProperty());
		mainResTo.setCellValueFactory(cellData -> cellData.getValue().getEndLocalDateProperty());
		mainResFirstName.setCellValueFactory(cellData -> cellData.getValue().getFirstNameProperty());
		mainResLastName.setCellValueFactory(cellData -> cellData.getValue().getLastNameProperty());

		itemNameColumn.setCellValueFactory(cellData -> cellData.getValue().getItemNameProperty());
        itemAmountColumn.setCellValueFactory(cellData -> cellData.getValue().getAmountProperty());

		woodLevel.setCellValueFactory(cellData -> cellData.getValue().getWoodProperty());
		woodCabinName.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        initWoodTable();


        //setter kartet på kart knappen
		Image img = new Image(getClass().getResourceAsStream("/resources/map.png"));
		map.setGraphic(new ImageView(img));

        woodLevelBox.getItems().addAll("Full", "High", "Medium", "Low", "Empty");

        //clearer verdier når det starer og når ingenting er targeta
		showCabinDetails(null);
		showForgottenDetails(null);


        //sortWoodTable();
        woodLevel.setComparator(new woodComp());

		// kjører endringsmethodene når rader i tabellene blir trykket på
		cabinTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> showCabinDetails(newValue));
		cabinTable2.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> showReservationDetail(newValue));
		sendTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> showMessagingDetail(newValue));
		forgottenTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> showForgottenDetails(newValue));
		woodTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showWoodStatus(newValue));
        itemTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showItemDetail(newValue));
        outboxTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showOutboxDetail(newValue));
        woodLevelBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> woodChoiceBox(newValue));

	}

    private void showOutboxDetail(Sent newValue) {
        outboxBody.setText(newValue.getBody());
    }

    private void woodChoiceBox(Object newValue) {
      try{

        System.out.println(newValue);
        woodTable.getSelectionModel().getSelectedItem().setWood(newValue.toString());
        cabinTable.getSelectionModel().clearSelection();
        showWoodStatus(woodTable.getSelectionModel().getSelectedItem());
        woodLevelAverage();
        }catch(Exception e){
          Dialogs.create()
                  .title("Ingen koie valgt")
                  .masthead("Velg koien du ønsker å endre på i tabellen til venstre")
                  .message("")
                  .showWarning();
      }


    }

    private void updateItemType(){
        for(ItemType it : mainApp.getItemTypeData()){
            it.updateAmount();
        }
    }
    private void updateCabinItems(){
        for(Cabin c : mainApp.getCabinData()){
            c.getItemList().clear();
            for(Item i : mainApp.getItemData()){
                if(i.getCabinName().equals(c.getName())){
                    c.addItem(i);
                }
            }
        }

    }

    /**
     * Initializes the wood table with all the css it needs
     */
    private void initWoodTable(){
        woodLevel.setCellFactory(column -> {
            return new TableCell<Cabin, String>(){
                @Override
                protected void updateItem(String item, boolean empty){
                    super.updateItem(item, empty);
                    setText(item);
                    TableRow currentRow = getTableRow();
                    currentRow.getStylesheets().add(getClass().getResource("/css/woodColors.css").toExternalForm());
                    currentRow.getStyleClass().clear();
                    if(item == null || empty){
                        setText(null);
                        setStyle("");
                    }

                    else{
                        if(item.equals("Full")){
                            currentRow.getStyleClass().add("test1");
                            currentRow.getStyleClass().add("table-view");
                            currentRow.getStyleClass().add("table-row-cell");
                        }
                        else if(item.equals("High")){
                            currentRow.getStyleClass().add("test5");
                            currentRow.getStyleClass().add("table-view");
                            currentRow.getStyleClass().add("table-row-cell");
                        }
                        else if(item.equals("Medium")){
                            currentRow.getStyleClass().add("test3");
                            currentRow.getStyleClass().add("table-view");
                            currentRow.getStyleClass().add("table-row-cell");
                        }
                        else if(item.equals("Empty")){
                            currentRow.getStyleClass().add("test2");
                            currentRow.getStyleClass().add("table-view");
                            currentRow.getStyleClass().add("table-row-cell");

                        }
                        else if(item.equals("Low")){
                            currentRow.getStyleClass().add("test4");
                            currentRow.getStyleClass().add("table-view");
                            currentRow.getStyleClass().add("table-row-cell");

                        }
                    }
                }
            };
        });

    }

    /**
     * Sets the item list equal to the items in the ItemType it gets as a parameter
     * @param it
     */
    private void showItemDetail(ItemType it){
        if(removing == 0){
            itemSingleTable.getSelectionModel().clearSelection();
            itemSingleTable.setItems(it.getItemList());
            itemSingleAmountColumn.setCellValueFactory(cellData -> cellData.getValue().getAmountProperty());
            itemSingleNameColumn.setCellValueFactory(cellData -> cellData.getValue().getCabinNameProperty());
        }


    }


    /**
     * Limit the reservations between the two dates that have been set by the user.
     */
    @FXML
    private void DateReservation(){

        System.out.println(reservationDateFrom1.getValue());
        System.out.println(reservationDateTo1.getValue());

        String pattern = "yyyy-MM-dd";

        reservationDateFrom1.setPromptText(pattern.toLowerCase());

        reservationDateFrom1.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

        reservationDateTo1.setPromptText(pattern.toLowerCase());

        reservationDateTo1.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

        ObservableList<Reservation> currentRes = FXCollections.observableArrayList();
        ObservableList<Reservation> from = FXCollections.observableArrayList();
        ObservableList<Reservation> to = FXCollections.observableArrayList();

        if(((!(reservationDateFrom1.getValue() == null)))){


            for(Reservation r : mainApp.getReservationData()){
                LocalDate date = r.getStartLocalDate();
                System.out.println(date.compareTo(reservationDateFrom1.getValue()));
                if(date.compareTo(reservationDateFrom1.getValue()) >= 0){
                    from.add(r);
                }
            }
        }

        if(!(reservationDateTo1.getValue() == null)){
            for(Reservation r : mainApp.getReservationData()){
                LocalDate date = r.getEndLocalDate();

                System.out.println("2: " + date.compareTo(reservationDateTo1.getValue()));
                if(date.compareTo(reservationDateTo1.getValue()) <= 0){
                    to.add(r);
                }
            }
        }

        if((from.size() == 0 && reservationDateFrom1.getValue() != null) || (to.size() == 0 && reservationDateTo1.getValue() != null)){
            mainResTable.setItems(FXCollections.observableArrayList());
        }else if(from.size() > 0 && to.size() > 0){
            for(Reservation f : from){
                for(Reservation t : to){
                    if(f.equals(t)){
                        currentRes.add(f);
                    }
                }
            }
            mainResTable.setItems(currentRes);
        }
        else if(from.size() > 0){
            System.out.println("JA FOR FAEN");
            mainResTable.setItems(from);
        }
        else if(to.size() > 0){
            mainResTable.setItems(to);
        }
    }


    /**
     * shows the wood level details for the cabin that is targeted.
     * @param cab
     */
	private void showWoodStatus(Cabin cab){
        woodLevelBox.setValue(cab.getWood());
		String wood = cab.getWood();
		if(wood.equals("Full")){
			woodLevelDugnad.setText("40 dager");

		}
		else if(wood.equals("High")){
			woodLevelDugnad.setText("30 dager");
		}
		else if(wood.equals("Medium")){
			woodLevelDugnad.setText("20 dager");
		}
		else if(wood.equals("Empty")){
			woodLevelDugnad.setText("S� fort som mulig");
		}
		else if(wood.equals("Low")){
			woodLevelDugnad.setText("10 dager");
		}


		woodLabelName.setText(cab.getName());
		woodLabelLevel.setText(wood);

	}

    /**
     * sets the Til field in the send window to the email of the targeted reservation
     * @param res
     */
	private void showMessagingDetail(Reservation res) {
        to.setText(res.getEmail());

	}


    /**
     * shows the reservations to the selected sqldata
     * @param cab
     */
	private void showReservationDetail(Cabin cab){
		reservationTable.setItems(cab.getReservationList());
		reservationTo.setCellValueFactory(cellData -> cellData.getValue().getEndLocalDateProperty());
		reservationFrom.setCellValueFactory(cellData -> cellData.getValue().getStartLocalDateProperty());
		reservationFirstName.setCellValueFactory(cellData -> cellData.getValue().getFirstNameProperty());
		reservationLastName.setCellValueFactory(cellData -> cellData.getValue().getLastNameProperty());
		
	}

    /**
     * Shows the forgotten and destroyed items in the mail list.
     * @param newValue
     */
	private void showForgottenDetails(MailInterface newValue){
		if(newValue != null){
			mailBody.setText(newValue.getDescription());
		}
		else{
			mailBody.setText("");
		}
	}


    /**
     * Show cabin info when the cabin is selected and nothing when its not.
     * @param cabin
     */
	private void showCabinDetails(Cabin cabin) {
		if (cabin != null) {
			// Fill the labels with info from the cabin object.
			beds.setText(cabin.getBednumber());
			tables.setText(cabin.getTablenumber());
			yearBuilt.setText(cabin.getYear());
			terrain.setText(cabin.getTerrain());
			reachableByBike.setText(cabin.getBike());     	
			trip.setText(cabin.getTrip());
			//guitar.setText(cabin.getGuitar());
			//waffleIron.setText(cabin.getWaffleiron());
			hunting.setText(cabin.getHunting());
			fishing.setText(cabin.getFishing());
			specialties.setText(cabin.getSpecialities());
			woodStatus.setText(cabin.getWood());
			cabinItemTable.setItems(cabin.getItemList());
			cabinItemTable.getSelectionModel().clearSelection();
			cabinItemName.setCellValueFactory(cellData -> cellData.getValue().getItemNameProperty());
			cabinItemAmount.setCellValueFactory(cellData -> cellData.getValue().getAmountProperty());

		} else {
			// cabin is null, remove all the text.
			beds.setText("");
			tables.setText("");
			yearBuilt.setText("");
			terrain.setText("");
			reachableByBike.setText("");
			trip.setText("");
			hunting.setText("");
			fishing.setText("");
			specialties.setText("");
			woodStatus.setText("");
		}
	}


    /**
     * Removes the selected reservation and makes sure that it get removed other places where its necessary.
     */
    @FXML
    private void handleReservationCabinRemove(){
        int selected = reservationTable.getSelectionModel().getSelectedIndex();
        if (selected >= 0){
          //  mainResTable.getItems().remove(selected);
        mainApp.getReservationData().remove(reservationTable.getSelectionModel().getSelectedItem());
        mainApp.reservationSorting();
        

           // mainResTable.getItems().remove(selected);
            //reservationTable.getItems().remove(selected);
        }
        else{
            Dialogs.create()
                    .title("No Selection")
                    .masthead("No reservation was selected")
                    .message("Please select a reservation in the table.")
                    .showWarning();
        }
    }

    /**
     * Delete the targeted cabin.
     */
	@FXML
	private void handleCabinDelete() {
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
     * Removes the targeted item and makes sure that it gets removed where it is neccessary.
     */
	@FXML
	private void handleItemRemove(){

        removing = 1;
        int selected = itemSingleTable.getSelectionModel().getSelectedIndex();
        Item item = itemSingleTable.getSelectionModel().getSelectedItem();
        if(selected >= 0){
            //fjerner fra itemdata lista
            mainApp.getItemData().remove(selected);

            //fjærner fra cabin
            for(Cabin c : mainApp.getCabinData()){
                if(c.getName().equals(item.getCabinName())){
                    c.getItemList().remove(item);
                }
            }

            //fjerner fra itemType

            for(ItemType it : mainApp.getItemTypeData()){
                if(it.getItemName().equals(item.getItemName())){
                    it.getItemList().remove(item);
                    it.updateAmount();
                }
                if(Integer.parseInt(it.getAmount()) <= 0){
                    itemTable.getSelectionModel().clearSelection();
                    //  it.getItemList().clear();
                    mainApp.getItemTypeData().remove(it);
                    break;
                }
            }

        }
        else {
            // Nothing selected.
            Dialogs.create()
                    .title("")
                    .masthead("Ingen Koie Valgt")
                    .message("Velg en koie du vil endre utstyret how i tabellen til høyre")
                    .showWarning();
        }


        removing = 0;

	}

    /**
     * Adds a new item and makes sure it gets added whereever neccessary.
     */
	@FXML
	private void handleItemAdd(){
        Item item = new Item();
        boolean okClicked = mainApp.showItemEditDialog(item);
        if(okClicked){
            //legger til main lista
            mainApp.getItemData().add(item);

            //legger til cabin lista
            for(Cabin c : mainApp.getCabinData()){
                if(c.getName().equals(item.getCabinName())){
                    c.addItem(item);
                }

            }
            int test = 0;
            //legger item til itemtype lista
            for(ItemType it : mainApp.getItemTypeData()){
                if(it.getItemName().equals(item.getItemName())){
                    it.addItem(item);
                    test = 1;
                }
            }
            //lager nytt ItemType objekt om det ikke finnes et fra før
            if(test == 0){
                mainApp.getItemTypeData().add(new ItemType(item.getItemName(), item.getAmount(), item));

            }
        }
    }
	@FXML
	private void handleItemCabinRemove(){

        removing = 1;
        int selected = cabinItemTable.getSelectionModel().getSelectedIndex();
        Item item = cabinItemTable.getSelectionModel().getSelectedItem();
        if(selected >= 0){
            //fjerner fra itemdata lista
            mainApp.getItemData().remove(selected);



            //fjærner fra cabin
            for(Cabin c : mainApp.getCabinData()){
                if(c.getName().equals(item.getCabinName())){
                    c.getItemList().remove(item);
                }
            }

            //fjærner fra itemType

            for(ItemType it : mainApp.getItemTypeData()){
                if(it.getItemName().equals(item.getItemName())){
                    it.getItemList().remove(item);
                    it.updateAmount();
                }
                if(Integer.parseInt(it.getAmount()) <= 0){

                    //  it.getItemList().clear();
                   mainApp.getItemTypeData().remove(it);
                   break;
                }
            }

        }

    removing = 0;

	}

    @FXML
    private void handleReservationRemove(){
        int selected = mainResTable.getSelectionModel().getSelectedIndex();
        if (selected >= 0){


            mainApp.getReservationData().remove(selected);
            mainApp.reservationSorting();

            DateReservation();

            // mainResTable.getItems().remove(selected);
            //reservationTable.getItems().remove(selected);
        }
        else{
            Dialogs.create()
                    .title("Ingen reservasjon valgt")
                    .masthead("")
                    .message("Velg en reservasjon fra tabellen")
                    .showWarning();
        }
    }

    @FXML
	private void handleItemEdit(){
        System.out.println("handleitemedit called");
        Item selected = itemSingleTable.getSelectionModel().getSelectedItem();
        if(selected != null){
            System.out.println("inside if");
            boolean okClicked = mainApp.showItemEditDialog(selected);
            if(okClicked){
                updateCabinItems();
                updateItemType();

            }
        }
            else {
                // Nothing selected.
                Dialogs.create()
                        .title("")
                        .masthead("Ingen Koie Valgt")
                        .message("Velg en koie du vil endre utstyret how i tabellen til høyre")
                        .showWarning();
            }

	}

    @FXML
    private void handleItemCabinEdit(){
        Item selected = cabinItemTable.getSelectionModel().getSelectedItem();
        if(selected != null){
            boolean okClicked = mainApp.showItemEditDialog(selected);
            if(okClicked){
                updateCabinItems();
                updateItemType();

            }
            else {
                // Nothing selected.
                Dialogs.create()
                        .title("Ingen item valgt")
                        .masthead("")
                        .message("Velg et item fra listen")
                        .showWarning();
            }
        }
    }

	@FXML
	private void handleReservationEdit(){
		Reservation selected = mainResTable.getSelectionModel().getSelectedItem();
		if (selected != null) {
            boolean okClicked = mainApp.showReservationEditDialog(selected);
            if (okClicked) {
            mainApp.reservationSorting();
            }



            else {
                // Nothing selected.
                Dialogs.create()
                        .title("No Selection")
                        .masthead("No cabin Selected")
                        .message("Please select a cabin in the table.")
                        .showWarning();
            }
        }

	}
	@FXML
	private void handleItemCabinAdd(){
        Item item = new Item();
        boolean okClicked = mainApp.showItemEditDialog(item);
        if(okClicked){
            //legger til main lista
            mainApp.getItemData().add(item);

            //legger til cabin lista
            for(Cabin c : mainApp.getCabinData()){
               if(c.getName().equals(item.getCabinName())){
                   c.addItem(item);
               }

            }
            int test = 0;
            //legger item til itemtype lista
            for(ItemType it : mainApp.getItemTypeData()){
                if(it.getItemName().equals(item.getItemName())){
                    it.addItem(item);
                    test = 1;
                }
            }
            //lager nytt ItemType objekt om det ikke finnes et fra før
            if(test == 0){
                mainApp.getItemTypeData().add(new ItemType(item.getItemName(), item.getAmount(), item));

            }
        }
	}

    @FXML
    private void handleReservationAdd(){
        Reservation res = new Reservation();
        boolean okClicked = mainApp.showReservationEditDialog(res);
        if(okClicked){
            mainApp.getReservationData().add(res);
            for (Cabin c : mainApp.getCabinData()){
                System.out.println(c.getName());
                if(c.getName().toLowerCase().equals(res.getName().toLowerCase())){
                    c.addReservation(res);
                    System.out.println(c.getReservationList());
                }
            }

        }
        System.out.println(res.getlastname());

    }

	@FXML
	private void handleReservationOk(){
		System.out.println(reservationDateFrom.getValue());
	}

    @FXML
    private void handleReservationCabinEdit(){
        Reservation selected = reservationTable.getSelectionModel().getSelectedItem();
        if (selected != null){
            boolean okClicked = mainApp.showReservationEditDialog(selected);
            if(okClicked){
                System.out.println("reservationEDIT whawhawha");

            }
        }else {
            // Nothing selected.
            Dialogs.create()
                    .title("No Selection")
                    .masthead("No cabin Selected")
                    .message("Please select a cabin in the table.")
                    .showWarning();
        }

    }



	@FXML
	private void handleSendMail(){

		SendEmail email = new SendEmail(to.getText(), subject.getText(), body.getText());
		email.start();
        mainApp.getOutBox().add(new Sent(to.getText(), subject.getText(), body.getText()));

	}

	@FXML
	private void handleOpenMap(){


		Stage dialogStage = new Stage();
		try {
			Map lol = new Map(dialogStage);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}


	private void woodLevelAverage(){
		int lol = 0;
		for(Cabin c : mainApp.getCabinData()){
			String woodLevel = c.getWood();
			if(woodLevel.equals("Full")){
				lol += 4;
			}
			else if(woodLevel.equals("High")){
				lol += 3;
			}
			else if(woodLevel.equals("Medium")){
				lol += 2;
			}
			else if(woodLevel.equals("Low")){
				lol += 1;
			}
			else if(woodLevel.equals("Empty")){
				lol += 0;
			}
		}
		double average = ((double) lol / (double) mainApp.getCabinData().size());
		System.out.println(average);
		if(average > 4){
			woodLabelLevel1.setText("Full");
		}
		else if(average > 3){
			woodLabelLevel1.setText("High");
		}
		else if(average > 2){
			woodLabelLevel1.setText("Medium");
		}
		else if(average > 1){
			woodLabelLevel1.setText("Low");
		}
		else{
			woodLabelLevel1.setText("Empty");
		}
	}


    public class woodComp implements Comparator<String> {

        public int woodLVL(String s){
            if (s.equals("Full")){
                return 4;
            }
            if (s.equals("High")){
                return 3;
            }
            if (s.equals("Medium")){
                return 2;
            }
            if (s.equals("Low")){
                return 1;
            }
            if (s.equals("Empty")){
                return 0;
            }
            return 0;
        }

        @Override
        public int compare(String s1, String s2) {

            return woodLVL(s1) - woodLVL(s2);
        }
    }


}