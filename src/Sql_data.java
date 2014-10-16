import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;

//tid brukt: ca. 5 timer

//stmt.execute("INSERT INTO login (username, passwd) VALUES ('SomeUsername', '123')");
public class Sql_data {

	private String url = null;
	private String username = null;
	private String passwd = null;
	private Connection connection = null;
	private java.sql.Statement statement = null;
	private ResultSet resultSet = null;

	public Sql_data(String sqlURL, String username,String passwd){

		this.url = sqlURL;
		this.username = username;
		this.passwd = passwd;

		try {
			System.out.println("Loading driver...");
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver loaded!");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Cannot find the driver in the classpath!", e);
		}
	}

	//Connecting to the SQL database with information given in the constructor. 
	//You have to use connect before any other functions! And remember to close the connection
	
	
	public void connect(){

		try {
			System.out.println("Connecting to SQLdatabase...");  
			connection = DriverManager.getConnection(url, username, passwd);
			System.out.println("connection established");	
		}

		catch (SQLException e) {
			throw new RuntimeException("Cannot connect to the database!", e);
		}
	}

	public void closeConnection(){

		try {
			System.out.println("Closing connection...");
			this.connection.close();
			resultSet.close();
			statement.close();
			} 
		
		catch (SQLException e) {
			System.out.println("Failed to close connections" + e);
		}
		System.out.println("Connection closed!");
	}

	public ResultSet getTableInformation(String table){

		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			System.out.println("Could not create Statement in connection" + e);
			e.printStackTrace();
		}
		try {
			
			resultSet  = statement.executeQuery("SELECT * FROM " + table);
			
		} catch (SQLException e) {
			System.out.println("Failed to fetch data from table");
			e.printStackTrace();
		}
		return resultSet;
	}
	
	//Cabin database information: cnr-name-bednumber-tablenumber-year-terrain-bike-trip-guitar-waffleiron-hunting-fishing-specialities-wood
	//Retrieves data from getTableInformation(), and uses the results to make an ArrayList with Cabin objects.
	public ArrayList<Cabin> getCabinData(){
		ResultSet cabin = getTableInformation("Cabin");
		Cabin c;
		ArrayList<Cabin> cabinList = new ArrayList<Cabin>(); 
		
		try {
			while(cabin.next()){
				
				c = new Cabin(cabin.getInt("cnr"),cabin.getString("name"), cabin.getInt("bednumber"), cabin.getInt("tablenumber"),
						cabin.getInt("year"), cabin.getString("terrain"), cabin.getInt("bike"), cabin.getInt("trip"),
						cabin.getInt("guitar"), cabin.getInt("waffleiron"), cabin.getInt("hunting"),cabin.getInt("fishing"),
						cabin.getString("specialities"), cabin.getString("wood"));		
				cabinList.add(c);
			}
		} catch (SQLException e) {
			
			System.out.println("failed to retrieve CabinData from sql server" + e);
		}	
		return cabinList;
	}
	
	public ArrayList<Destroyed> getDestroyedData(){
		ArrayList<Destroyed> destroyed = new ArrayList<Destroyed>();
		Destroyed d;
		
		ResultSet destroyedItems = getTableInformation("Destroyed");
		
		try {
			while(destroyedItems.next()){
				d = new Destroyed(destroyedItems.getInt("cnr"), destroyedItems.getString("cabinname"), destroyedItems.getString("description")
						, destroyedItems.getString("email"));
			}
			
		} catch (SQLException e) {
			System.out.println("Failed to read from ResultSet");
			e.printStackTrace();
		}
		
		return destroyed;
	}
}