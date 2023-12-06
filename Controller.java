package lab;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.scene.layout.AnchorPane;

/**
 * Name: Shaurya Beriwala
 * Username: beris01 
 */

public class Controller {
	@FXML
    private ListView<String> cartListView;

    @FXML
    private Button addBtn;

    @FXML
    private ListView<String> avbleBooksListView;

    @FXML
    private ImageView logoImg;

    @FXML
    private Button okButton;

    @FXML
    private Button removeBtn;

    @FXML
    private TextField subTotalTxt;

    @FXML
    private TextField taxTxt;
    
    @FXML
    private AnchorPane pane;

    @FXML
    private TextField totalTxt;
    private ArrayList<Book> books;
    
    @FXML
    public void initialize() {
    	avbleBooksListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	cartListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	books= new ArrayList<Book>();
    }

    @FXML
    void checkOut(ActionEvent event) {
    	double subtotal= calculateSubTotal(), tax;
    	tax= 0.07*subtotal;
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Checkout");
    	alert.setHeaderText("Checkout Details");
    	alert.setContentText(String.format(
    	"%-15s$%-8.2f\n%-15s$%-8.2f\n%-15s$%-8.2f",
    	"Subtotal:", subtotal,
    	"Tax:", tax,
    	"Total:", subtotal + tax));
    	alert.showAndWait();
    }

    @FXML
    void clearCart(ActionEvent event) {
    	cartListView.getItems().clear();
    }

    @FXML
    void exitStore(ActionEvent event) {
    	cartListView.getItems().clear();
    	avbleBooksListView.getItems().clear();
    	System.exit(0);
    }
    

    @FXML
    void loadBooks(ActionEvent event) throws IOException {
    	FileChooser fileChooser = new FileChooser();
    	File projectDirectory = new File(System.getProperty("user.dir"));
    	fileChooser.setInitialDirectory(projectDirectory);
    	//FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Binary Files (.bin)", ".bin");
        //fileChooser.getExtensionFilters().add(extFilter);

        // Show the file chooser dialog
        File selectedFile = fileChooser.showOpenDialog(null);
    	RandomAccessFile reader= new RandomAccessFile(selectedFile, "r");
    	try {
			while(true) {
				String name= reader.readUTF();
				double price= reader.readDouble();
				Book book= new Book(name, price);
				avbleBooksListView.getItems().add(book.getName());
				books.add(book);
			}
		}
		catch(EOFException e) {  
			reader.close();
		} 
    	avbleBooksListView.setOrientation(Orientation.VERTICAL);
    }
    
    @FXML
    void addToCart(ActionEvent event) {
        ObservableList<String> names= avbleBooksListView.getSelectionModel().getSelectedItems();
        cartListView.getItems().addAll(names);
    }
    
    @FXML
    void removeFromCart(ActionEvent event) {
    	ObservableList<String> names=  cartListView.getSelectionModel().getSelectedItems();
    	for(String name: names) {
    		cartListView.getItems().remove(name);
    	}
    }
    
    double calculateSubTotal() {
    	double subTotal= 0;
    	for(String name: cartListView.getItems()) {
    		for(Book book: books) {
    			if(book.getName().equalsIgnoreCase(name)) {
    				subTotal+= book.getPrice();
    				break;
    			}
    		}
    	}
    	return subTotal;
    }
    
    @FXML
    void lightTheme(ActionEvent event) {
    	pane.getStylesheets().clear();
        pane.getStylesheets().add(getClass().getResource("light-style.css").toExternalForm());
    }
    
    @FXML
    void darkTheme(ActionEvent event) {
    	pane.getStylesheets().clear();
        pane.getStylesheets().add(getClass().getResource("dark-style.css").toExternalForm());
    }
}























