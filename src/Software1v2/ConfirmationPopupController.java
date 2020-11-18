/**
 * @author Jessie B. 'ConfirmationPopup.fxml' Controller Class
 */

package Software1v2;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ConfirmationPopupController implements Initializable {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnPopupYes"
    private Button btnPopupYes; // Value injected by FXMLLoader

    @FXML // fx:id="btnPopupNo"
    private Button btnPopupNo; // Value injected by FXMLLoader

    public static boolean deleteOrRemove = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // event handlers for popups

        // If clicked cancel the deletion/removal
        btnPopupNo.setOnAction(event -> {
            // set static variable to let other stage classes know delete or remove status
            deleteOrRemove = false;

            // close popup window
            Stage stage = (Stage) btnPopupNo.getScene().getWindow();
            stage.close();
        });

        // If clicked allow deletion/removal
        btnPopupYes.setOnAction(event -> {
            // set static variable to let other stage classes know delete or remove status
            deleteOrRemove = true;

            // close popup window
            Stage stage = (Stage) btnPopupYes.getScene().getWindow();
            stage.close();
        });
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnPopupYes != null : "fx:id=\"btnPopupYes\" was not injected: check your FXML file 'ConfirmationPopup.fxml'.";
        assert btnPopupNo != null : "fx:id=\"btnPopupNo\" was not injected: check your FXML file 'ConfirmationPopup.fxml'.";

    }
}
