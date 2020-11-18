/**
 *@author Jessie B Main.java
 *
 * A detailed description of a logical or runtime error that you corrected in the code and a detailed
 * description of how you corrected it above the line of code you are referring to. See line - 144 in MainControl.java
 *
 *  A compatible feature suitable to your application that would extend functionality to the next version
 *  if you were to update the application - Answered Here:
 *  The first product or application that comes to mind is that this program can be used as a checkout terminals
 *  parts lookup application at a store like Auto Parts. The compatible feature needed that would extend the
 *  functionality would be add database connectivity to CRUD the parts and products data off of.
 *
 */

package Software1v2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    // starts javafx program
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../Resources/mainPane.fxml"));
        primaryStage.setTitle("Jessie Burton Software 1 Project");
        primaryStage.setScene(new Scene(root, 800, 350));
        primaryStage.show();
    }

    // entry point of the program
    public static void main(String[] args) {
        launch(args);
    }
}
