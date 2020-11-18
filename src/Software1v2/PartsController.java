/**
 * @author Jessie Burton for 'Parts.fxml' Controller Class
 */
package Software1v2;


import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PartsController implements Initializable {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;


    @FXML // fx:id="lbAddPartMainLabel"
    private Label lbAddPartMainLabel; // Value injected by FXMLLoader

    // set the label to show if the scene is set to Add Part or Modify Part
    public void setLbAddPartMainLabel(String label) {
        lbAddPartMainLabel.setText(label);
    }

    @FXML // fx:id="rbInHouse"
    private RadioButton rbInHouse; // Value injected by FXMLLoader

    @FXML // fx:id="rbOutSourced"
    private RadioButton rbOutSourced; // Value injected by FXMLLoader

    @FXML // fx:id="lbAddPartMIDCN"
    private Label lbAddPartMIDCN; // Value injected by FXMLLoader

    @FXML // fx:id="tfAddPartID"
    private TextField tfAddPartID; // Value injected by FXMLLoader

    @FXML // fx:id="tfAddPartName"
    private TextField tfAddPartName; // Value injected by FXMLLoader

    @FXML // fx:id="tfAddPartInventory"
    private TextField tfAddPartInventory; // Value injected by FXMLLoader

    @FXML // fx:id="tfAddPartPriceCost"
    private TextField tfAddPartPriceCost; // Value injected by FXMLLoader

    @FXML // fx:id="tfAddPartMax"
    private TextField tfAddPartMax; // Value injected by FXMLLoader

    @FXML // fx:id="tfAddPartMIDCN"
    private TextField tfAddPartMIDCN; // Value injected by FXMLLoader

    @FXML // fx:id="btnAddPartSave"
    private Button btnAddPartSave; // Value injected by FXMLLoader

    @FXML // fx:id="btnAddPartCancel"
    private Button btnAddPartCancel; // Value injected by FXMLLoader

    @FXML // fx:id="tfAddPartMin"
    private TextField tfAddPartMin; // Value injected by FXMLLoader

    @FXML // fx:id="lbErrorText"
    private Label lbErrorText; // Value injected by FXMLLoader

    // boolean to identify if scene is set to modify or not
    public static boolean isModify = false;

    // create InHouse Part with null value set
    public InHouse correctInHousePart = null;
    // create OutSourced with null value set
    public OutSourced correctOutSourcedPart = null;

    // boolean to indicate if all the fields checked are correct
    public static boolean fieldsCorrect = false;

    // variable to hard the part index for list
    int partIndex = 0;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Go back to main screen
        btnAddPartCancel.setOnAction(event -> {
            PartsController.isModify = false;
            Parent partsSceneParent = null;
            try {
                partsSceneParent = FXMLLoader.load(getClass().getResource("/Resources/mainPane.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene partsScene = new Scene(partsSceneParent, 800, 350);
            Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            primaryStage.setScene(partsScene);
        });

        // save part created and checked to the allparts list
        btnAddPartSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                fieldsCorrect = checkTextFields();
                if (fieldsCorrect) {
                    int correctPartID = Integer.parseInt(tfAddPartID.getText());
                    int correctPartInv = Integer.parseInt(tfAddPartInventory.getText());
                    int correctPartMin = Integer.parseInt(tfAddPartMin.getText());
                    int correctPartMax = Integer.parseInt(tfAddPartMax.getText());
                    double correctPartPrice = Double.parseDouble(tfAddPartPriceCost.getText());

                    // Trim (not round) the price to the penny
                    correctPartPrice = truncateDecimal(correctPartPrice, 2).doubleValue();

                    if (isModify){
                        if (rbInHouse.isSelected()) {
                            int correctPartMach = Integer.parseInt(tfAddPartMIDCN.getText());
                            correctInHousePart = new InHouse(correctPartID, tfAddPartName.getText(), correctPartPrice, correctPartInv, correctPartMin, correctPartMax,correctPartMach);
                            Inventory.updatePart(partIndex, correctInHousePart);
                        } else {
                            correctOutSourcedPart = new OutSourced(correctPartID, tfAddPartName.getText(), correctPartPrice, correctPartInv, correctPartMin, correctPartMax,tfAddPartMIDCN.getText());
                            Inventory.updatePart(partIndex, correctOutSourcedPart);
                        }
                    } else {
                        if (rbInHouse.isSelected()) {
                            int correctPartMach = Integer.parseInt(tfAddPartMIDCN.getText());
                            correctInHousePart = new InHouse(correctPartID, tfAddPartName.getText(), correctPartPrice, correctPartInv, correctPartMin, correctPartMax,correctPartMach);
                            Inventory.addPart(correctInHousePart);
                        } else {
                            correctOutSourcedPart = new OutSourced(correctPartID, tfAddPartName.getText(), correctPartPrice, correctPartInv, correctPartMin, correctPartMax,tfAddPartMIDCN.getText());
                            Inventory.addPart(correctOutSourcedPart);
                        }
                }
                    // Go back to main scene after saving part
                    Parent partsSceneParent = null;
                    try {
                        partsSceneParent = FXMLLoader.load(getClass().getResource("/Resources/mainPane.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    isModify = false;
                    Scene partsScene = new Scene(partsSceneParent, 800, 350);
                    Stage primaryStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
                    primaryStage.setScene(partsScene);
                }
            }
        });

        // toggle Radio Button event listeners to change labels in form
        rbInHouse.setOnAction(event -> {
            toggleLabelByRB();
        });

        // toggle Radio Butoon event listeners to change labels in form
        rbOutSourced.setOnAction(event -> {
            toggleLabelByRB();
        });

        // Auto Generate Unique Part ID if on Add Part, Selected part  ID if on Modify part
        tfAddPartID.setText(addPartIDAutoGenOrSet());
    }


    /**
     * Helper Methods
     */

    // auto generate partID
    private String addPartIDAutoGenOrSet() {
        if(isModify) {
            return null;
        } else {
            int sizePlusOne = Inventory.getAllParts().size() + 1;
            return Integer.toString(sizePlusOne);
        }
    }

    // set the main level to modify part
    public void changeToModify() {
        String modify = "Modify Part";
        setLbAddPartMainLabel(modify);
    }

    // toggle the label field text based on rabio button
    public void toggleLabelByRB() {
        if(rbInHouse.isSelected()) {
            lbAddPartMIDCN.setText("Machine ID");
        } else if (rbOutSourced.isSelected()) {
            lbAddPartMIDCN.setText("Company Name");
        }
    }

    // Gets the selected part to modify and populate the text fields for updating
    public void populatePartForModification(Part part) {
        setIndex(part);
        tfAddPartID.setText(Integer.toString(part.getId()));
        tfAddPartMin.setText(Integer.toString(part.getMin()));
        tfAddPartPriceCost.setText(Double.toString(part.getPrice()));
        tfAddPartMax.setText(Integer.toString(part.getMax()));
        if (part instanceof InHouse){
            tfAddPartMIDCN.setText(Integer.toString(((InHouse) part).getMachineId()));
            rbInHouse.setSelected(true);
            toggleLabelByRB();
        } else if (part instanceof OutSourced){
            tfAddPartMIDCN.setText(((OutSourced) part).getCompanyName());
            rbOutSourced.setSelected(true);
            toggleLabelByRB();
        }
        tfAddPartName.setText(part.getName());
        tfAddPartInventory.setText(Integer.toString(part.getStock()));
    }

    // set the part index from the allparts list
    private void setIndex(Part part) {
        partIndex = Inventory.getAllParts().indexOf(part);
    }

    // Truncate the price if it longer than 2 decimal places - No rounding
    public static BigDecimal truncateDecimal(double x,int numberOfDecimals)
    {
        if ( x > 0) {
            return new BigDecimal(String.valueOf(x)).setScale(numberOfDecimals, RoundingMode.FLOOR);
        } else {
            return new BigDecimal(String.valueOf(x)).setScale(numberOfDecimals, RoundingMode.CEILING);
        }
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
            tfAddPartMin.getText() == "" || tfAddPartInventory.getText() == "" ||
            tfAddPartMax.getText() == "" || tfAddPartName.getText() == "" ||
            tfAddPartMIDCN.getText() == "" || tfAddPartPriceCost.getText() == ""
            ) { allRight = false; }
        else {
            try {
                minVal = Integer.parseInt(tfAddPartMin.getText());
            } catch(NumberFormatException nfx) {
                allRight = false;
            }

            try {
                maxVal = Integer.parseInt(tfAddPartMax.getText());
            } catch(NumberFormatException nfx1) {
                allRight = false;
            }

            try {
                invVal = Integer.parseInt(tfAddPartInventory.getText());
            } catch(NumberFormatException nfx2) {
                allRight = false;
            }

            if (rbInHouse.isSelected()) {
                try {
                    Integer.parseInt(tfAddPartMIDCN.getText());
                } catch (NumberFormatException nfx3) {
                    allRight = false;
                }
            }

            try {
                Double.parseDouble(tfAddPartPriceCost.getText());
            } catch(NumberFormatException nfx3) {
                allRight = false;
            }

            if (!(minVal <= maxVal && invVal >= minVal && invVal <= maxVal)) {
                allRight = false;
            }

        }

        // Show the error message if everything is not correct
        if (!allRight) {
            lbErrorText.setVisible(true);
        }

        // After 2 seconds, make label invisible
        visibleLabel.setOnFinished(event -> {
            lbErrorText.setVisible(false);
        });
        visibleLabel.play();

        return allRight;
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert rbInHouse != null : "fx:id=\"rbInHouse\" was not injected: check your FXML file 'Parts.fxml'.";
        assert rbOutSourced != null : "fx:id=\"rbOutSourced\" was not injected: check your FXML file 'Parts.fxml'.";
        assert lbAddPartMIDCN != null : "fx:id=\"lbAddPartMIDCN\" was not injected: check your FXML file 'Parts.fxml'.";
        assert tfAddPartID != null : "fx:id=\"tfAddPartID\" was not injected: check your FXML file 'Parts.fxml'.";
        assert tfAddPartName != null : "fx:id=\"tfAddPartName\" was not injected: check your FXML file 'Parts.fxml'.";
        assert tfAddPartInventory != null : "fx:id=\"tfAddPartInventory\" was not injected: check your FXML file 'Parts.fxml'.";
        assert tfAddPartPriceCost != null : "fx:id=\"tfAddPartPriceCost\" was not injected: check your FXML file 'Parts.fxml'.";
        assert tfAddPartMax != null : "fx:id=\"tfAddPartMax\" was not injected: check your FXML file 'Parts.fxml'.";
        assert tfAddPartMIDCN != null : "fx:id=\"tfAddPartMIDCN\" was not injected: check your FXML file 'Parts.fxml'.";
        assert btnAddPartSave != null : "fx:id=\"btnAddPartSave\" was not injected: check your FXML file 'Parts.fxml'.";
        assert btnAddPartCancel != null : "fx:id=\"btnAddPartCancel\" was not injected: check your FXML file 'Parts.fxml'.";

    }

}
