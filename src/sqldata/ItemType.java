package sqldata;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class for handling all items of same type
 */
public class ItemType {
    private IntegerProperty amount;
    private StringProperty name;
    private ObservableList<Item> items = FXCollections.observableArrayList();


    public ItemType(String name){
        this.name = new SimpleStringProperty(name);
        this.amount = new SimpleIntegerProperty(0);
    }
    public ItemType(String name, String amount, Item item){
        this.name = new SimpleStringProperty(name);
        this.amount = new SimpleIntegerProperty(Integer.parseInt(amount));
        this.items.add(item);
    }

    public void amountIncrease(int am){

        int amt = amount.get() + am;
        amount.set(amt);
    }
    public void amountDecrease(int am){
        amount.set(amount.get() - am);
    }


    public String getItemName(){return name.get();}
    public String getAmount(){return ""+amount.get();}
    public ObservableList<Item> getItemList(){ return this.items;}
    public StringProperty getItemNameProperty(){return this.name;}
    public IntegerProperty getAmountProperty(){return this.amount;}


    public void addItem(Item i){
        int ammm = Integer.parseInt(i.getAmount());
        amountIncrease(ammm);
        items.add(i);

    }

    public void removeItem(Item i){
        amountDecrease(Integer.parseInt(i.getAmount()));
        for(Item j : items){
            if(j.getCabinName().equals(i.getCabinName())){
                items.remove(j);
            }
        }
    }

    public void updateAmount(){
        int am = 0;
        for(Item i : items){
            am += Integer.parseInt(i.getAmount());
        }
        this.amount.set(am);
    }


}
