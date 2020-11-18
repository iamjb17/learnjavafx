/**
 * @author Jessie Burton
 */

package Software1v2;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MainControl implements Initializable {

    /**
     * Configure the Table Views and other controls
     */

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="tfPartsSearch"
    private TextField tfPartsSearch; // Value injected by FXMLLoader

    @FXML // fx:id="tvMainPartsPane"
    private TableView<Part> tvMainPartsPane; // Value injected by FXMLLoader

    @FXML // fx:id="tvPartsID"
    private TableColumn<Part, Integer> tvPartsID; // Value injected by FXMLLoader

    @FXML // fx:id="tvPartsName"
    private TableColumn<Part, String> tvPartsName; // Value injected by FXMLLoader

    @FXML // fx:id="tvPartsInventoryLevel"
    private TableColumn<Part, Integer> tvPartsInventoryLevel; // Value injected by FXMLLoader

    @FXML // fx:id="tvPartsPCPerUnit"
    private TableColumn<Part, Double> tvPartsPCPerUnit; // Value injected by FXMLLoader

    @FXML // fx:id="btnAddParts"
    private Button btnAddParts; // Value injected by FXMLLoader

    @FXML // fx:id="btnModifyParts"
    private Button btnModifyParts; // Value injected by FXMLLoader

    @FXML // fx:id="btnDeleteParts"
    private Button btnDeleteParts; // Value injected by FXMLLoader

    @FXML // fx:id="tfProductSearch"
    private TextField tfProductSearch; // Value injected by FXMLLoader

    @FXML // fx:id="tvMainProductsPane"
    private TableView<Product> tvMainProductsPane; // Value injected by FXMLLoader

    @FXML // fx:id="tvProductsID"
    private TableColumn<Product, Integer> tvProductsID; // Value injected by FXMLLoader

    @FXML // fx:id="tvProductsName"
    private TableColumn<Product, String> tvProductsName; // Value injected by FXMLLoader

    @FXML // fx:id="tvProductsInventoryLevel"
    private TableColumn<Product, Integer> tvProductsInventoryLevel; // Value injected by FXMLLoader

    @FXML // fx:id="tvProductsPCPerUnit"
    private TableColumn<Product, Double> tvProductsPCPerUnit; // Value injected by FXMLLoader

    @FXML // fx:id="btnAddProducts"
    private Button btnAddProducts; // Value injected by FXMLLoader

    @FXML // fx:id="btnModifyProducts"
    private Button btnModifyProducts; // Value injected by FXMLLoader

    @FXML // fx:id="btnDeleteProducts"
    private Button btnDeleteProducts; // Value injected by FXMLLoader

    @FXML // fx:id="btnExit"
    private Button btnExit; // Value injected by FXMLLoader

    @FXML // fx:id="lbProductsUnableToDelete"
    private Label lbProductsUnableToDelete; // Value injected by FXMLLoader

    @FXML // fx:id="lbPartsUnableToDelete"
    private Label lbPartsUnableToDelete; // Value injected by FXMLLoader

    // get price of part from table view
    public TableColumn<Part, Double> getTvPartsPCPerUnit() {
        return tvPartsPCPerUnit;
    }

    // set price of part from table view
    public void setTvPartsPCPerUnit(TableColumn<Part, Double> tvPartsPCPerUnit) {
        this.tvPartsPCPerUnit = tvPartsPCPerUnit;
    }

    // create temp list to store parts
    public ObservableList<Part> newPartsList = FXCollections.observableArrayList();
    // create temp list to store products
    public ObservableList<Product> newProductsList = FXCollections.observableArrayList();

    // constant string for no part to delete error
    private final String NO_PART_TO_DELETE = "No Part Selected To Delete";
    // constant string for no product to delete error
    private final String NO_PRODUCT_TO_DELETE = "No Product Selected To Delete";
    // constant string for no part found in search
    private final String NO_PART_SEARCH_RESULTS = "No Parts Found From Search";
    // constant string for no product found in search
    private final String NO_PRODUCT_SEARCH_RESULTS = "No Products Found From Search";

    // Method called when scene shows; will house on Action events; setup tables;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Setup the table views, Parts and Products
        tvPartsID.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        tvPartsName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        tvPartsInventoryLevel.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        tvPartsPCPerUnit.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        tvProductsID.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        tvProductsName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        tvProductsInventoryLevel.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
        tvProductsPCPerUnit.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));

        /**
         *
         * - Error: Setting the items in tvMainProductsPane before setting the items in tvMainPartsPane -
         * This one mistake caused this "java.lang.RuntimeException: Exception in Application start method"
         * that looked super long and complicated. What clued me in to what the problem was, was a coubple of
         * lines in the stack trace: "at Software1v2.MainControl.getProduct(MainControl.java:458)" and
         * "Caused by: java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0".
         * Because I knew that products have a dependency on parts, I cant create the products without first
         * creating the parts that they rely on. So, setting the parts first solved the problem.
         *
         */
        // Insert dummy data for testing. Comment out if you don't want this functionality
        tvMainPartsPane.setItems(getParts());
        tvMainProductsPane.setItems(getProduct());

        // Select the first item by default
        tvMainPartsPane.getSelectionModel().selectFirst();
        tvMainProductsPane.getSelectionModel().selectFirst();

        // Get loader of all the fxml files needed
        FXMLLoader partsLoader = new FXMLLoader(getClass().getResource("/Resources/Parts.fxml"));
        FXMLLoader addProductsLoader = new FXMLLoader(getClass().getResource("/Resources/AddProducts.fxml"));
        FXMLLoader modifyProductsLoader = new FXMLLoader(getClass().getResource("/Resources/ModifyProducts.fxml"));

        // Part search event using anonymous inner class. Searches based on enter key pressed.
        tfPartsSearch.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyevent) {
                boolean isNumb = false;
                int currentPartID = -1;
                if (keyevent.getCode() == KeyCode.ENTER) {
                    // See if the text field is only a valid number
                    try {
                        currentPartID = Integer.parseInt(tfPartsSearch.getText());
                        isNumb = true;
                    } catch(NumberFormatException nfx) {
                        isNumb = false;
                    }
                    // If its a number search based on ID, if not a number, search based on name
                    if(isNumb) {
                        if (Inventory.lookupPart(currentPartID) == null) {
                            unableToComplete("parts search");
                            newPartsList.clear();
                            tvMainPartsPane.setItems(Inventory.getAllParts());
                        } else {
                            newPartsList.add(Inventory.lookupPart(currentPartID));
                            tvMainPartsPane.setItems(newPartsList);
                            highlightTopSelectedItem(tvMainPartsPane);
                        }
                    } else {

                        if (tfPartsSearch.getText() == "" || Inventory.lookupPart(tfPartsSearch.getText()).isEmpty()) {
                            unableToComplete("parts search");
                            newPartsList.clear();
                            tvMainPartsPane.setItems(Inventory.getAllParts());
                        } else {
                            newPartsList = Inventory.lookupPart(tfPartsSearch.getText());
                            tvMainPartsPane.setItems(newPartsList);
                            highlightTopSelectedItem(tvMainPartsPane);
                        }
                    }
                }
            }
        });


        // Product search event using anonymous inner class. Searches based on enter key pressed.
        tfProductSearch.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyevent) {
                boolean isNumb = false;
                int currentProductID = -1;
                if (keyevent.getCode() == KeyCode.ENTER) {
                    // See if the text field is only a valid number
                    try {
                        currentProductID = Integer.parseInt(tfProductSearch.getText());
                        isNumb = true;
                    } catch(NumberFormatException nfx) {
                        isNumb = false;
                    }
                    // If its a number search based on ID, if not a number, search based on name
                    if(isNumb) {
                        if (Inventory.lookupProduct(currentProductID) == null) {
                            unableToComplete("products search");
                            newProductsList.clear();
                            tvMainProductsPane.setItems(Inventory.getAllProducts());
                        } else {
                            newProductsList.add(Inventory.lookupProduct(currentProductID));
                            tvMainProductsPane.setItems(newProductsList);
                            highlightTopSelectedItem(tvMainProductsPane);
                        }
                    } else {

                        if (tfProductSearch.getText() == "" || Inventory.lookupProduct(tfProductSearch.getText()).isEmpty()) {
                            unableToComplete("products search");
                            newProductsList.clear();
                            tvMainProductsPane.setItems(Inventory.getAllProducts());
                        } else {
                            newProductsList = Inventory.lookupProduct(tfProductSearch.getText());
                            tvMainProductsPane.setItems(newProductsList);
                            highlightTopSelectedItem(tvMainProductsPane);
                        }
                    }
                }
            }
        });


        /**
         * Events Handling lamda expressions
         */

        // Lamda for add part button scene change. loads parts.fxml. Uses same stage but changes scene.
        btnAddParts.setOnAction(event -> {
            PartsController.isModify = false;
            Parent partsRoot = null;
            try {
               partsRoot = partsLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene partsScene = new Scene(partsRoot, 600, 600);
            Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            primaryStage.setScene(partsScene);
        });
        
        // lamda for modify parts button. Changes the scene to Modify Part
        btnModifyParts.setOnAction(event -> {
            PartsController.isModify = true;
            Parent partsRoot = null;
            try {
                partsRoot = partsLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // get the controller of parts.fxml to change label to Modify Parts via method call
            PartsController partsController = partsLoader.getController();

            Scene partsScene = new Scene(partsRoot, 600, 600);
            Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();

            // Setup the fields in the next scene(modify part)
            partsController.populatePartForModification(tvMainPartsPane.getSelectionModel().getSelectedItem());
            partsController.changeToModify();

            primaryStage.setScene(partsScene);

        });

        // Go to add products scene
        btnAddProducts.setOnAction(event -> {
            Parent productsRoot = null;
            try {
                productsRoot = addProductsLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene addProductsScene = new Scene(productsRoot, 800, 600);
            Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            primaryStage.setScene(addProductsScene);
        });

        // Go to modify parts scene
        btnModifyProducts.setOnAction(event -> {
            Parent productsRoot = null;
            try {
                productsRoot = modifyProductsLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // get the controller of products.fxml to load product data via method call
            ModifyProductsController modifyProductsController = modifyProductsLoader.getController();

            Scene modifyProductScene = new Scene(productsRoot, 800, 600);
            Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();

            // get the selected product to send to modify products scene
            int currentIndex = Inventory.getAllProducts().indexOf(tvMainProductsPane.getSelectionModel().getSelectedItem());
            modifyProductsController.bringInProductToModify(currentIndex, tvMainProductsPane.getSelectionModel().getSelectedItem());

            primaryStage.setScene(modifyProductScene);
        });


        // Delete selected part
        btnDeleteParts.setOnAction(event -> {
            Parent popupParent = null;
            try {
                popupParent = FXMLLoader.load(getClass().getResource("/Resources/ConfirmationPopup.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(popupParent));
            popupStage.showAndWait();

            if (ConfirmationPopupController.deleteOrRemove) {
                if (newPartsList.isEmpty()){
                    if(Inventory.getAllParts().isEmpty()) {
                        unableToComplete("parts delete");
                    } else {
                        int currentSelectedIndex = tvMainPartsPane.getSelectionModel().getSelectedIndex();
                        Inventory.deletePart(Inventory.getAllParts().get(currentSelectedIndex));
                    }
                } else {
                    int currentSelectedIndex = tvMainPartsPane.getSelectionModel().getSelectedIndex();
                    newPartsList.remove(currentSelectedIndex);
                    Inventory.deletePart(Inventory.getAllParts().get(currentSelectedIndex));
                }
            }
            ConfirmationPopupController.deleteOrRemove = false;
        });

        // Delete selected product depending on parts
        btnDeleteProducts.setOnAction(event -> {
            if(tvMainProductsPane.getSelectionModel().getSelectedItem().getAllAssociatedParts().isEmpty())  {
                Parent popupParent = null;
                try {
                    popupParent = FXMLLoader.load(getClass().getResource("/Resources/ConfirmationPopup.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage popupStage = new Stage();
                popupStage.setScene(new Scene(popupParent));
                popupStage.showAndWait();
                if (ConfirmationPopupController.deleteOrRemove) {
                    if (newProductsList.isEmpty()) {
                        if (Inventory.getAllProducts().isEmpty()) {
                            unableToComplete("products delete");
                        } else {
                            int currentSelectedIndex = tvMainProductsPane.getSelectionModel().getSelectedIndex();
                            Inventory.deleteProduct(Inventory.getAllProducts().get(currentSelectedIndex));
                        }
                    } else {
                        int currentSelectedIndex = tvMainProductsPane.getSelectionModel().getSelectedIndex();
                        newProductsList.remove(currentSelectedIndex);
                        Inventory.deleteProduct(Inventory.getAllProducts().get(currentSelectedIndex));
                    }
                }
                ConfirmationPopupController.deleteOrRemove = false;
            } else {
                unableToComplete("has parts");
            }
        });

        // Close the application
        btnExit.setOnAction(event -> { Platform.exit(); });
    }

    /**
    * Helper Methods
     *
     */

    // Highlight top result in selected table view
    public static void highlightTopSelectedItem (TableView tableView) {
        tableView.getSelectionModel().selectFirst();
    }

    // Method to toggle the label indicating no part/product to delete
    public void unableToComplete(String label) {
        // show the label
        if (label == "parts delete") {
            lbPartsUnableToDelete.setText(NO_PART_TO_DELETE);
            lbPartsUnableToDelete.setVisible(true);
        } else if (label == "products delete") {
            lbProductsUnableToDelete.setText(NO_PRODUCT_TO_DELETE);
            lbProductsUnableToDelete.setVisible(true);
        } else if(label == "parts search") {
            lbPartsUnableToDelete.setText(NO_PART_SEARCH_RESULTS);
            lbPartsUnableToDelete.setVisible(true);
        } else if(label == "products search") {
            lbProductsUnableToDelete.setText(NO_PRODUCT_SEARCH_RESULTS);
            lbProductsUnableToDelete.setVisible(true);
        } else if (label == "has parts") {
            lbProductsUnableToDelete.setText("Associated Parts, Cant Delete");
            lbProductsUnableToDelete.setVisible(true);
        }

        PauseTransition visibleLabel = new PauseTransition(
                Duration.millis(2000)
        );
        // After 2 seconds, make label invis
        visibleLabel.setOnFinished(event -> {
            if (label.contains("parts")) {
                lbPartsUnableToDelete.setVisible(false);
            } else {
                lbProductsUnableToDelete.setVisible(false);
            }
            if (label == "has parts") {
                lbProductsUnableToDelete.setVisible(false);
            }
        }
        );
        visibleLabel.play();
    }

    // Input dummy data into allParts as needed
    public ObservableList<Part> getParts() {
        if(Inventory.getAllParts().isEmpty()) {
            InHouse part1 = new InHouse(1, "par", 4.99, 7, 1, 15, 27);
            InHouse part2 = new InHouse(2, "partod2", 2.99, 3, 1, 15, 20);
            OutSourced part3 = new OutSourced(3, "dsdr3", 8.99, 4, 1, 10, "company1");
            OutSourced part4 = new OutSourced(4, "dod3", 12.99, 12, 1, 15, "company2");
            Inventory.addPart(part1);
            Inventory.addPart(part2);
            Inventory.addPart(part3);
            Inventory.addPart(part4);
        }
        return Inventory.getAllParts();
    }
    // Input dummy data into allProducts as needed
    public ObservableList<Product> getProduct() {
        if(Inventory.getAllProducts().isEmpty()) {
            Product product1 = new Product(1, "pro", 2.99, 5, 1 , 10);
            Product product2 = new Product(2, "prkoa", 3.99, 52, 1 , 100);
            Product product3 = new Product(3, "dfkll", 4.99, 12, 1 , 20);
            Inventory.addProduct(product1);
            Inventory.addProduct(product2);
            Inventory.addProduct(product3);
            product1.addAssociatedPart(Inventory.getAllParts().get(0));
            product1.addAssociatedPart(Inventory.getAllParts().get(2));
            product3.addAssociatedPart(Inventory.getAllParts().get(1));
        }
        return Inventory.getAllProducts();
    }


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert tfPartsSearch != null : "fx:id=\"tfPartsSearch\" was not injected: check your FXML file 'mainPane.fxml'.";
        assert tvMainPartsPane != null : "fx:id=\"tvMainPartsPane\" was not injected: check your FXML file 'mainPane.fxml'.";
        assert tvPartsID != null : "fx:id=\"tvPartsID\" was not injected: check your FXML file 'mainPane.fxml'.";
        assert tvPartsName != null : "fx:id=\"tvPartsName\" was not injected: check your FXML file 'mainPane.fxml'.";
        assert tvPartsInventoryLevel != null : "fx:id=\"tvPartsInventoryLevel\" was not injected: check your FXML file 'mainPane.fxml'.";
        assert tvPartsPCPerUnit != null : "fx:id=\"tvPartsPCPerUnit\" was not injected: check your FXML file 'mainPane.fxml'.";
        assert lbPartsUnableToDelete != null : "fx:id=\"lbPartsUnableToDelete\" was not injected: check your FXML file 'mainPane.fxml'.";
        assert btnAddParts != null : "fx:id=\"btnAddParts\" was not injected: check your FXML file 'mainPane.fxml'.";
        assert btnModifyParts != null : "fx:id=\"btnModifyParts\" was not injected: check your FXML file 'mainPane.fxml'.";
        assert btnDeleteParts != null : "fx:id=\"btnDeleteParts\" was not injected: check your FXML file 'mainPane.fxml'.";
        assert tfProductSearch != null : "fx:id=\"tfProductSearch\" was not injected: check your FXML file 'mainPane.fxml'.";
        assert tvMainProductsPane != null : "fx:id=\"tvMainProductsPane\" was not injected: check your FXML file 'mainPane.fxml'.";
        assert tvProductsID != null : "fx:id=\"tvProductsID\" was not injected: check your FXML file 'mainPane.fxml'.";
        assert tvProductsName != null : "fx:id=\"tvProductsName\" was not injected: check your FXML file 'mainPane.fxml'.";
        assert tvProductsInventoryLevel != null : "fx:id=\"tvProductsInventoryLevel\" was not injected: check your FXML file 'mainPane.fxml'.";
        assert tvProductsPCPerUnit != null : "fx:id=\"tvProductsPCPerUnit\" was not injected: check your FXML file 'mainPane.fxml'.";
        assert lbProductsUnableToDelete != null : "fx:id=\"lbProductsUnableToDelete\" was not injected: check your FXML file 'mainPane.fxml'.";
        assert btnAddProducts != null : "fx:id=\"btnAddProducts\" was not injected: check your FXML file 'mainPane.fxml'.";
        assert btnModifyProducts != null : "fx:id=\"btnModifyProducts\" was not injected: check your FXML file 'mainPane.fxml'.";
        assert btnDeleteProducts != null : "fx:id=\"btnDeleteProducts\" was not injected: check your FXML file 'mainPane.fxml'.";
        assert btnExit != null : "fx:id=\"btnExit\" was not injected: check your FXML file 'mainPane.fxml'.";

    }
}
