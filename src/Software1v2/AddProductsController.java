/**
 * @author Jessie B. 'AddProducts.fxml' Controller Class
 */

package Software1v2;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import static Software1v2.PartsController.truncateDecimal;

public class AddProductsController implements Initializable {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="lbPartsUnableToSearch"
    private Label lbPartsUnableToSearch; // Value injected by FXMLLoader

    @FXML // fx:id="lbAddProduct"
    private Label lbAddProduct; // Value injected by FXMLLoader

    @FXML // fx:id="tfProductPartsSearch"
    private TextField tfProductPartsSearch; // Value injected by FXMLLoader

    @FXML // fx:id="tvProductPartsTable"
    private TableView<Part> tvProductPartsTable; // Value injected by FXMLLoader

    @FXML // fx:id="tvProductPartID"
    private TableColumn<Part, Integer> tvProductPartID; // Value injected by FXMLLoader

    @FXML // fx:id="tvProductPartName"
    private TableColumn<Part, String> tvProductPartName; // Value injected by FXMLLoader

    @FXML // fx:id="tvProductPartInventoryLevel"
    private TableColumn<Part, Integer> tvProductPartInventoryLevel; // Value injected by FXMLLoader

    @FXML // fx:id="tvProductPartPCPerUnit"
    private TableColumn<Part, Double> tvProductPartPCPerUnit; // Value injected by FXMLLoader

    @FXML // fx:id="btnProductAdd"
    private Button btnProductAdd; // Value injected by FXMLLoader

    @FXML // fx:id="tvProductPartsAddedTable"
    private TableView<Part> tvProductPartsAddedTable; // Value injected by FXMLLoader

    @FXML // fx:id="tvProductPartAddedPartID"
    private TableColumn<Part, Integer> tvProductPartAddedPartID; // Value injected by FXMLLoader

    @FXML // fx:id="tvProductPartAddedPartName"
    private TableColumn<Part, String> tvProductPartAddedPartName; // Value injected by FXMLLoader

    @FXML // fx:id="tvProductPartAddedInventoryLevel"
    private TableColumn<Part, Integer> tvProductPartAddedInventoryLevel; // Value injected by FXMLLoader

    @FXML // fx:id="tvProductPartAddedPCPerUnit"
    private TableColumn<Part, Double> tvProductPartAddedPCPerUnit; // Value injected by FXMLLoader

    @FXML // fx:id="btnProductRemoveAPart"
    private Button btnProductRemoveAPart; // Value injected by FXMLLoader

    @FXML // fx:id="tfProductID"
    private TextField tfProductID; // Value injected by FXMLLoader

    @FXML // fx:id="tfProductName"
    private TextField tfProductName; // Value injected by FXMLLoader

    @FXML // fx:id="tfProductInv"
    private TextField tfProductInv; // Value injected by FXMLLoader

    @FXML // fx:id="tfProductPrice"
    private TextField tfProductPrice; // Value injected by FXMLLoader

    @FXML // fx:id="tfProductMax"
    private TextField tfProductMax; // Value injected by FXMLLoader

    @FXML // fx:id="btnProductSave"
    private Button btnProductSave; // Value injected by FXMLLoader

    @FXML // fx:id="btnProductCancel"
    private Button btnProductCancel; // Value injected by FXMLLoader

    @FXML // fx:id="tfProductMin"
    private TextField tfProductMin; // Value injected by FXMLLoader

    @FXML // fx:id="lbAddProductError"
    private Label lbAddProductError; // Value injected by FXMLLoader

    // constant for error string content
    private final String NO_PART_SEARCH_RESULTS = "No Parts Found From Search";

    // create temp list to put results of search
    public ObservableList<Part> newPartsList = FXCollections.observableArrayList();

    // create temp list to store associated parts of products
    public ObservableList<Part> assocPartsList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Setup the table views, Parts and Parts Added
        tvProductPartID.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        tvProductPartName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        tvProductPartInventoryLevel.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        tvProductPartPCPerUnit.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        tvProductPartAddedPartID.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        tvProductPartAddedPartName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        tvProductPartAddedInventoryLevel.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        tvProductPartAddedPCPerUnit.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        // setUp the the parts table and associated parts table
        getPartsTable();


        // Go back to main screen
        btnProductCancel.setOnAction(event -> {
            Parent addProductSceneParent = null;
            try {
                addProductSceneParent = FXMLLoader.load(getClass().getResource("/Resources/mainPane.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene addProductsScene = new Scene(addProductSceneParent, 800, 350);
            Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            primaryStage.setScene(addProductsScene);
        });

        // Part search event using anonymous inner class. Searches based on enter key pressed.
        tfProductPartsSearch.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyevent) {
                boolean isNumb = false;
                int currentPartID = -1;
                if (keyevent.getCode() == KeyCode.ENTER) {
                    // See if the text field is only a valid number
                    try {
                        currentPartID = Integer.parseInt(tfProductPartsSearch.getText());
                        isNumb = true;
                    } catch(NumberFormatException nfx) {
                        isNumb = false;
                    }
                    // If its a number search based on ID, if not a number, search based on name
                    if(isNumb) {
                        if (Inventory.lookupPart(currentPartID) == null) {
                            unableToSearch("parts search");
                            newPartsList.clear();
                            tvProductPartsTable.setItems(Inventory.getAllParts());
                        } else {
                            newPartsList.add(Inventory.lookupPart(currentPartID));
                            tvProductPartsTable.setItems(newPartsList);
                            MainControl.highlightTopSelectedItem(tvProductPartsTable);
                        }
                    } else {

                        if (tfProductPartsSearch.getText() == "" || Inventory.lookupPart(tfProductPartsSearch.getText()).isEmpty()) {
                            unableToSearch("parts search");
                            newPartsList.clear();
                            tvProductPartsTable.setItems(Inventory.getAllParts());
                        } else {
                            newPartsList = Inventory.lookupPart(tfProductPartsSearch.getText());
                            tvProductPartsTable.setItems(newPartsList);
                            MainControl.highlightTopSelectedItem(tvProductPartsTable);
                        }
                    }
                }
            }
        });

        // add part to temp associated parts list
        btnProductAdd.setOnAction(event -> {
            if (!(tvProductPartsTable.getSelectionModel().getSelectedItems().isEmpty())) {
                assocPartsList.addAll(tvProductPartsTable.getSelectionModel().getSelectedItems());
                tvProductPartsAddedTable.setItems(assocPartsList);
            }
        });

        // remove associated part from temp associated list
        btnProductRemoveAPart.setOnAction(event -> {
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
                assocPartsList.remove(tvProductPartsAddedTable.getSelectionModel().getSelectedItem());
            }
            ConfirmationPopupController.deleteOrRemove = false;
        });

        // create new product and save it into the list
        btnProductSave.setOnAction(event -> {
            if (checkTextFields()) {
                // Trim (not round) the price to the penny
                double correctPartPrice = Double.parseDouble(tfProductPrice.getText());
                correctPartPrice = truncateDecimal(correctPartPrice, 2).doubleValue();

                // create to product to save all input data and store back in list
                Product newProduct = new Product(
                        Integer.parseInt(tfProductID.getText()),
                        tfProductName.getText(),
                        correctPartPrice,
                        Integer.parseInt(tfProductInv.getText()),
                        Integer.parseInt(tfProductMin.getText()),
                        Integer.parseInt(tfProductMax.getText()));
                newProduct.getAllAssociatedParts().setAll(assocPartsList);
                Inventory.addProduct(newProduct);

                // Go back to main scene
                Parent addProductSceneParent = null;
                try {
                    addProductSceneParent = FXMLLoader.load(getClass().getResource("/Resources/mainPane.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene addProductsScene = new Scene(addProductSceneParent, 800, 350);
                Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                primaryStage.setScene(addProductsScene);
            }
        });

        // Set Auto Generated product ID
        tfProductID.setText(genProductID());
    }


    /**
     * Helper Methods
     */

    // generate a contiguous product ID
    private String genProductID() {
        int sizePlusOne = Inventory.getAllProducts().size() + 1;
        return Integer.toString(sizePlusOne);
    }

    // Method to toggle the label indicating no part/product to delete
    public void unableToSearch(String label) {
        // show the label
        if(label == "parts search") {
            lbPartsUnableToSearch.setText(NO_PART_SEARCH_RESULTS);
            lbPartsUnableToSearch.setVisible(true);
        }
        // create 2 sec timer
        PauseTransition visibleLabel = new PauseTransition(
                Duration.millis(2000)
        );
        // After 2 seconds, make label invis
        visibleLabel.setOnFinished(event -> {
                    if (label.contains("parts")) {
                        lbPartsUnableToSearch.setVisible(false);
                    }
                }
        );
        visibleLabel.play();
    }

    // Get and set the parts table
    private void getPartsTable() {
        tvProductPartsTable.setItems(Inventory.getAllParts());
    }

    // Check all the text field and show a timed error message if they are not all correct
    public boolean checkTextFields() {
        int minVal =0;
        int maxVal =0;
        int invVal =0;
        boolean allRight = true;

        PauseTransition visibleLabel = new PauseTransition(
                Duration.millis(5000)
        );

        // Test all the text fields for correctness
        if (
                tfProductName.getText() == "" || tfProductInv.getText() == "" ||
                        tfProductMax.getText() == "" || tfProductMin.getText() == "" ||
                        tfProductPrice.getText() == ""
        ) { allRight = false; }
        else {
            try {
                minVal = Integer.parseInt(tfProductMin.getText());
            } catch(NumberFormatException nfx) {
                allRight = false;
            }

            try {
                maxVal = Integer.parseInt(tfProductMax.getText());
            } catch(NumberFormatException nfx1) {
                allRight = false;
            }

            try {
                invVal = Integer.parseInt(tfProductInv.getText());
            } catch(NumberFormatException nfx2) {
                allRight = false;
            }

            try {
                Double.parseDouble(tfProductPrice.getText());
            } catch(NumberFormatException nfx3) {
                allRight = false;
            }

            if (!(minVal <= maxVal && invVal >= minVal && invVal <= maxVal)) {
                allRight = false;
            }
        }

        // Show the error message if everything is not correct
        if (!allRight) {
            lbAddProductError.setVisible(true);
        }

        // After 2 seconds, make label invisible
        visibleLabel.setOnFinished(event -> {
            lbAddProductError.setVisible(false);
        });
        visibleLabel.play();

        return allRight;
    }


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert lbAddProduct != null : "fx:id=\"lbAddProduct\" was not injected: check your FXML file 'AddProducts.fxml'.";
        assert tfProductPartsSearch != null : "fx:id=\"tfProductPartsSearch\" was not injected: check your FXML file 'AddProducts.fxml'.";
        assert tvProductPartsTable != null : "fx:id=\"tvProductPartsTable\" was not injected: check your FXML file 'AddProducts.fxml'.";
        assert tvProductPartID != null : "fx:id=\"tvProductPartID\" was not injected: check your FXML file 'AddProducts.fxml'.";
        assert tvProductPartName != null : "fx:id=\"tvProductPartName\" was not injected: check your FXML file 'AddProducts.fxml'.";
        assert tvProductPartInventoryLevel != null : "fx:id=\"tvProductPartInventoryLevel\" was not injected: check your FXML file 'AddProducts.fxml'.";
        assert tvProductPartPCPerUnit != null : "fx:id=\"tvProductPartPCPerUnit\" was not injected: check your FXML file 'AddProducts.fxml'.";
        assert btnProductAdd != null : "fx:id=\"btnProductAdd\" was not injected: check your FXML file 'AddProducts.fxml'.";
        assert tvProductPartsAddedTable != null : "fx:id=\"tvProductPartsAddedTable\" was not injected: check your FXML file 'AddProducts.fxml'.";
        assert tvProductPartAddedPartID != null : "fx:id=\"tvProductPartAddedPartID\" was not injected: check your FXML file 'AddProducts.fxml'.";
        assert tvProductPartAddedPartName != null : "fx:id=\"tvProductPartAddedPartName\" was not injected: check your FXML file 'AddProducts.fxml'.";
        assert tvProductPartAddedInventoryLevel != null : "fx:id=\"tvProductPartAddedInventoryLevel\" was not injected: check your FXML file 'AddProducts.fxml'.";
        assert tvProductPartAddedPCPerUnit != null : "fx:id=\"tvProductPartAddedPCPerUnit\" was not injected: check your FXML file 'AddProducts.fxml'.";
        assert btnProductRemoveAPart != null : "fx:id=\"btnProductRemoveAPart\" was not injected: check your FXML file 'AddProducts.fxml'.";
        assert tfProductID != null : "fx:id=\"tfProductID\" was not injected: check your FXML file 'AddProducts.fxml'.";
        assert tfProductName != null : "fx:id=\"tfProductName\" was not injected: check your FXML file 'AddProducts.fxml'.";
        assert tfProductInv != null : "fx:id=\"tfProductInv\" was not injected: check your FXML file 'AddProducts.fxml'.";
        assert tfProductPrice != null : "fx:id=\"tfPrice\" was not injected: check your FXML file 'AddProducts.fxml'.";
        assert tfProductMax != null : "fx:id=\"tfMax\" was not injected: check your FXML file 'AddProducts.fxml'.";
        assert btnProductSave != null : "fx:id=\"btnProductSave\" was not injected: check your FXML file 'AddProducts.fxml'.";
        assert btnProductCancel != null : "fx:id=\"btnProductCancel\" was not injected: check your FXML file 'AddProducts.fxml'.";
        assert tfProductMin != null : "fx:id=\"tfMin\" was not injected: check your FXML file 'AddProducts.fxml'.";
        assert lbAddProductError != null : "fx:id=\"lbAddProductError\" was not injected: check your FXML file 'AddProducts.fxml'.";

    }
}

