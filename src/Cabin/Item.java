package Cabin;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Item {
	
	private StringProperty cabinName, itemName, amount;  
	
	
	public Item(String itemName, String amount, String cabinName){
		this.cabinName = new SimpleStringProperty(cabinName);
		this.itemName = new SimpleStringProperty(itemName);
		this.amount = new SimpleStringProperty(amount);
	}
	
	public Item(){
		this(null,null,null);
	}
	
	
	//getters and setters here

	public String getCabinName() {
		return cabinName.get();
	}

	public void setCabinName(String cabinName) {
		this.cabinName.set(cabinName);
	}

	public String getItemName() {
		return itemName.get();
	}

	public void setItemName(String itemName) {
		this.itemName.set(itemName);
	}

	public String getAmount() {
		return amount.get();
	}

	public void setAmount(String amount) {
		this.amount.set(amount);
	}

	
	//property getters her
	public StringProperty getItemNameProperty(){
		return this.itemName;
	}
	
	public StringProperty getCabinNameProperty(){
		return this.cabinName;
	}
	
	public StringProperty getAmountProperty(){
		return this.amount;
	}

	
	
	
}
