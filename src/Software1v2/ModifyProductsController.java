/**
 * @author Jessie B 'ModifyProducts.fxml' Controller Class
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

public class ModifyProductsController implements Initializable {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="lbPartsUnableToSearch"
    private Label lbPartsUnableToSearch; // Value injected by FXMLLoader

    @FXML // fx:id="lbModifyProduct"
    private Label lbModifyProduct; // Value injected by FXMLLoader

    @FXML // fx:id="tfModifyProductPartSearch"
    private TextField tfModifyProductPartSearch; // Value injected by FXMLLoader

    @FXML // fx:id="tvModifyProductPartTable"
    private TableView<Part> tvModifyProductPartTable; // Value injected by FXMLLoader

    @FXML // fx:id="tvModifyProductPartID"
    private TableColumn<Part, Integer> tvModifyProductPartID; // Value injected by FXMLLoader

    @FXML // fx:id="tvModifyProductPartName"
    private TableColumn<Part, String> tvModifyProductPartName; // Value injected by FXMLLoader

    @FXML // fx:id="tvModifyProductPartInventoryLevel"
    private TableColumn<Part, Integer> tvModifyProductPartInventoryLevel; // Value injected by FXMLLoader

    @FXML // fx:id="tvModifyProductPartPCPerUnit"
    private TableColumn<Part, Double> tvModifyProductPartPCPerUnit; // Value injected by FXMLLoader

    @FXML // fx:id="btnModifyProductAdd"
    private Button btnModifyProductAdd; // Value injected by FXMLLoader

    @FXML // fx:id="tvModifyProductAddedTable"
    private TableView<Part> tvModifyProductAddedTable; // Value injected by FXMLLoader

    @FXML // fx:id="tvModifyProductAddedPartID"
    private TableColumn<Part, Integer> tvModifyProductAddedPartID; // Value injected by FXMLLoader

    @FXML // fx:id="tvModifyProductAddedPartName"
    private TableColumn<Part, String> tvModifyProductAddedPartName; // Value injected by FXMLLoader

    @FXML // fx:id="tvModifyProductAddedInventoryLevel"
    private TableColumn<Part, Integer> tvModifyProductAddedInventoryLevel; // Value injected by FXMLLoader

    @FXML // fx:id="tvModifyProductAddedPCPerUnit"
    private TableColumn<Part, Double> tvModifyProductAddedPCPerUnit; // Value injected by FXMLLoader

    @FXML // fx:id="btnModifyProductRemoveAPart"
    private Button btnModifyProductRemoveAPart; // Value injected by FXMLLoader

    @FXML // fx:id="tfModifyProductID"
    private TextField tfModifyProductID; // Value injected by FXMLLoader

    @FXML // fx:id="tfModifyProductName"
    private TextField tfModifyProductName; // Value injected by FXMLLoader

    @FXML // fx:id="tfModifyProductInv"
    private TextField tfModifyProductInv; // Value injected by FXMLLoader

    @FXML // fx:id="tfModifyProductPrice"
    private TextField tfModifyProductPrice; // Value injected by FXMLLoader

    @FXML // fx:id="tfModifyProductMax"
    private TextField tfModifyProductMax; // Value injected by FXMLLoader

    @FXML // fx:id="btnModifyProductSave"
    private Button btnModifyProductSave; // Value injected by FXMLLoader

    @FXML // fx:id="btnModifyProductCancel"
    private Button btnModifyProductCancel; // Value injected by FXMLLoader

    @FXML // fx:id="tfModifyProductMin"
    private TextField tfModifyProductMin; // Value injected by FXMLLoader

    @FXML // fx:id="lbModifyProductError"
    private Label lbModifyProductError; // Value injected by FXMLLoader

    // store the index of product to modify
    public int indexOfProductToModify = 0;

    // constant string for no parts found in search
    private final String NO_PART_SEARCH_RESULTS = "No Parts Found From Search";

    // temp list to store parts from search
    public ObservableList<Part> newPartsList = FXCollections.observableArrayList();
    // temp list to store associated parts for table view
    public ObservableList<Part> assocPartsList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Setup the table views, Parts and Parts Added
        tvModifyProductPartID.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        tvModifyProductPartName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        tvModifyProductPartInventoryLevel.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        tvModifyProductPartPCPerUnit.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        tvModifyProductAddedPartID.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        tvModifyProductAddedPartName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        tvModifyProductAddedInventoryLevel.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        tvModifyProductAddedPCPerUnit.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        // get the parts table and set it
        getPartsTable();

        // Cancel and go back to main scene
        btnModifyProductCancel.setOnAction(event -> {
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
        tfModifyProductPartSearch.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyevent) {
                boolean isNumb = false;
                int currentPartID = -1;
                if (keyevent.getCode() == KeyCode.ENTER) {
                    // See if the text field is only a valid number
                    try {
                        currentPartID = Integer.parseInt(tfModifyProductPartSearch.getText());
                        isNumb = true;
                    } catch(NumberFormatException nfx) {
                        isNumb = false;
                    }
                    // If its a number search based on ID, if not a number, search based on name
                    if(isNumb) {
                        if (Inventory.lookupPart(currentPartID) == null) {
                            unableToSearch("parts search");
                            newPartsList.clear();
                            tvModifyProductPartTable.setItems(Inventory.getAllParts());
                        } else {
                            newPartsList.add(Inventory.lookupPart(currentPartID));
                            tvModifyProductPartTable.setItems(newPartsList);
                            MainControl.highlightTopSelectedItem(tvModifyProductPartTable);
                        }
                    } else {

                        if (tfModifyProductPartSearch.getText() == "" || Inventory.lookupPart(tfModifyProductPartSearch.getText()).isEmpty()) {
                            unableToSearch("parts search");
                            newPartsList.clear();
                            tvModifyProductPartTable.setItems(Inventory.getAllParts());
                        } else {
                            newPartsList = Inventory.lookupPart(tfModifyProductPartSearch.getText());
                            tvModifyProductPartTable.setItems(newPartsList);
                            MainControl.highlightTopSelectedItem(tvModifyProductPartTable);
                        }
                    }
                }
            }
        });

        // button action event to add selected part to temp associated parts list
        btnModifyProductAdd.setOnAction(event -> {
            if (!(tvModifyProductPartTable.getSelectionModel().getSelectedItems().isEmpty())) {
                assocPartsList.addAll(tvModifyProductPartTable.getSelectionModel().getSelectedItems());
                tvModifyProductAddedTable.setItems(assocPartsList);
            }
        });

        // button action event to remove part from temp associated parts list
        btnModifyProductRemoveAPart.setOnAction(event -> {
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
                assocPartsList.remove(tvModifyProductAddedTable.getSelectionModel().getSelectedItem());
            }
            ConfirmationPopupController.deleteOrRemove = false;
        });

        // button action event to save product to allproducts list
        btnModifyProductSave.setOnAction(event -> {
            // check fields for accuracy
            if (checkTextFields()) {
                // Trim (not round) the price to the penny
                double correctPartPrice = Double.parseDouble(tfModifyProductPrice.getText());
                correctPartPrice = truncateDecimal(correctPartPrice, 2).doubleValue();

                Product newProduct = new Product(
                        Integer.parseInt(tfModifyProductID.getText()),
                        tfModifyProductName.getText(),
                        correctPartPrice,
                        Integer.parseInt(tfModifyProductInv.getText()),
                        Integer.parseInt(tfModifyProductMin.getText()),
                        Integer.parseInt(tfModifyProductMax.getText()));
                newProduct.getAllAssociatedParts().setAll(assocPartsList);
                Inventory.updateProduct(indexOfProductToModify, newProduct);

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
    }

    /**
     * Helper Methods
     */

    // Set the text fields and table with product to modify
    public void bringInProductToModify(int index, Product product) {
        indexOfProductToModify = index;
        setProductToModify(product);
    }

    // Set the text fields and table with product to modify
    public void setProductToModify(Product product) {
        tfModifyProductID.setText(Integer.toString(product.getId()));
        tfModifyProductName.setText(product.getName());
        tfModifyProductInv.setText(Integer.toString(product.getStock()));
        tfModifyProductPrice.setText(Double.toString(product.getPrice()));
        tfModifyProductMin.setText(Integer.toString(product.getMin()));
        tfModifyProductMax.setText(Integer.toString(product.getMax()));

        tvModifyProductAddedTable.setItems(product.getAllAssociatedParts());
        assocPartsList = product.getAllAssociatedParts();
    }

    // Method to toggle the label indicating no part/product to delete
    public void unableToSearch(String label) {
        // show the label
        if(label == "parts search") {
            lbPartsUnableToSearch.setText(NO_PART_SEARCH_RESULTS);
            lbPartsUnableToSearch.setVisible(true);
        }

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
        tvModifyProductPartTable.setItems(Inventory.getAllParts());
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
                tfModifyProductInv.getText() == "" || tfModifyProductName.getText() == "" ||
                        tfModifyProductMax.getText() == "" || tfModifyProductMin.getText() == "" ||
                        tfModifyProductPrice.getText() == ""
        ) { allRight = false; }
        else {
            try {
                minVal = Integer.parseInt(tfModifyProductMin.getText());
            } catch(NumberFormatException nfx) {
                allRight = false;
            }

            try {
                maxVal = Integer.parseInt(tfModifyProductMax.getText());
            } catch(NumberFormatException nfx1) {
                allRight = false;
            }

            try {
                invVal = Integer.parseInt(tfModifyProductInv.getText());
            } catch(NumberFormatException nfx2) {
                allRight = false;
            }

            try {
                Double.parseDouble(tfModifyProductPrice.getText());
            } catch(NumberFormatException nfx3) {
                allRight = false;
            }

            if (!(minVal <= maxVal && invVal >= minVal && invVal <= maxVal)) {
                allRight = false;
            }

        }

        // Show the error message if everything is not correct
        if (!allRight) {
            lbModifyProductError.setVisible(true);
        }

        // After 2 seconds, make label invisible
        visibleLabel.setOnFinished(event -> {
            lbModifyProductError.setVisible(false);
        });
        visibleLabel.play();

        return allRight;
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert lbModifyProduct != null : "fx:id=\"lbModifyProduct\" was not injected: check your FXML file 'ModifyProducts.fxml'.";
        assert lbPartsUnableToSearch != null : "fx:id=\"lbPartsUnableToSearch\" was not injected: check your FXML file 'ModifyProducts.fxml'.";
        assert tfModifyProductPartSearch != null : "fx:id=\"tfModifyProductPartSearch\" was not injected: check your FXML file 'ModifyProducts.fxml'.";
        assert tvModifyProductPartTable != null : "fx:id=\"tvModifyProductPartTable\" was not injected: check your FXML file 'ModifyProducts.fxml'.";
        assert tvModifyProductPartID != null : "fx:id=\"tvModifyProductPartID\" was not injected: check your FXML file 'ModifyProducts.fxml'.";
        assert tvModifyProductPartName != null : "fx:id=\"tvModifyProductPartName\" was not injected: check your FXML file 'ModifyProducts.fxml'.";
        assert tvModifyProductPartInventoryLevel != null : "fx:id=\"tvModifyProductPartInventoryLevel\" was not injected: check your FXML file 'ModifyProducts.fxml'.";
        assert tvModifyProductPartPCPerUnit != null : "fx:id=\"tvModifyProductPartPCPerUnit\" was not injected: check your FXML file 'ModifyProducts.fxml'.";
        assert btnModifyProductAdd != null : "fx:id=\"btnModifyProductAdd\" was not injected: check your FXML file 'ModifyProducts.fxml'.";
        assert tvModifyProductAddedTable != null : "fx:id=\"tvModifyProductAddedTable\" was not injected: check your FXML file 'ModifyProducts.fxml'.";
        assert tvModifyProductAddedPartID != null : "fx:id=\"tvModifyProductAddedPartID\" was not injected: check your FXML file 'ModifyProducts.fxml'.";
        assert tvModifyProductAddedPartName != null : "fx:id=\"tvModifyProductAddedPartName\" was not injected: check your FXML file 'ModifyProducts.fxml'.";
        assert tvModifyProductAddedInventoryLevel != null : "fx:id=\"tvModifyProductAddedInventoryLevel\" was not injected: check your FXML file 'ModifyProducts.fxml'.";
        assert tvModifyProductAddedPCPerUnit != null : "fx:id=\"tvModifyProductAddedPCPerUnit\" was not injected: check your FXML file 'ModifyProducts.fxml'.";
        assert btnModifyProductRemoveAPart != null : "fx:id=\"btnModifyProductRemoveAPart\" was not injected: check your FXML file 'ModifyProducts.fxml'.";
        assert tfModifyProductID != null : "fx:id=\"tfModifyProductID\" was not injected: check your FXML file 'ModifyProducts.fxml'.";
        assert tfModifyProductName != null : "fx:id=\"tfModifyProductName\" was not injected: check your FXML file 'ModifyProducts.fxml'.";
        assert tfModifyProductInv != null : "fx:id=\"tfModifyProductInv\" was not injected: check your FXML file 'ModifyProducts.fxml'.";
        assert tfModifyProductPrice != null : "fx:id=\"tfModifyProductPrice\" was not injected: check your FXML file 'ModifyProducts.fxml'.";
        assert tfModifyProductMax != null : "fx:id=\"tfModifyProductMax\" was not injected: check your FXML file 'ModifyProducts.fxml'.";
        assert btnModifyProductSave != null : "fx:id=\"btnModifyProductSave\" was not injected: check your FXML file 'ModifyProducts.fxml'.";
        assert btnModifyProductCancel != null : "fx:id=\"btnModifyProductCancel\" was not injected: check your FXML file 'ModifyProducts.fxml'.";
        assert tfModifyProductMin != null : "fx:id=\"tfModifyProductMin\" was not injected: check your FXML file 'ModifyProducts.fxml'.";
        assert lbModifyProductError != null : "fx:id=\"lbModifyProductError\" was not injected: check your FXML file 'ModifyProducts.fxml'.";
    }
}
