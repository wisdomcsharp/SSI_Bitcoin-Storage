package Resources.GUI;

import Cryptography.OutputJSON;
import ExceptionList.NullCarrier;
import ImageHandle.Steganography;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.steganography.error.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import Cryptography.AES;

import static App.Main.primaryStage;

/**
 * Created by Wisdom on 22/07/2016.
 */
public class DecryptDisplay implements Initializable {


    @FXML
    JFXHamburger hamburger;

    Parent root;

    @FXML
    Button imagefile;

    @FXML
    JFXPasswordField password;

    private static File file;
    private static File directory;

    @FXML
    Text warning;

    //opens the selected wallet, and tries to decrypt it.
    //finally save the wallet in the selected directory
    public void saveWallet() throws NullCarrier, IOException, InvalidTypeException, HidingComponentSizeException, CarrierInitializationException, CarrierHidingSchemeException, CarrierAccessSchemeException, CompatibilityException, CarrierSizeException, RevealingException, HidingComponentInitializationException {

        //load the encrypted image file, with the save directory
        Steganography readData = new Steganography(file, directory);

        //get the encrypted text from the image
        String encryptedText = readData.getText();

        String unEncryptedData = null;

       //unencrypt the text using the provided password
        try {
            unEncryptedData =  new AES(encryptedText, this.password.getText()).decrypt();

            //finall, create JSON file with all bitcoin addresses format { "0":"dgdgs", "1":"sdfsdf" }
            String json = this.file.getName().substring(0, this.file.getName().lastIndexOf('.')) + ".json";
            new OutputJSON(unEncryptedData, new File(this.directory.getAbsolutePath()+"\\result_"+json), true).export();



        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    @FXML
    public void imageAction(ActionEvent action){

        //check if image has not been selected
        if(!imagefile.getText().toLowerCase().equals("save directory")) {
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Image files", "*.jpg", "*.jpeg", "*.png", "*.bmp");
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Open Image File");
            chooser.getExtensionFilters().add(filter);
            this.file = chooser.showOpenDialog(new Stage());
            if (this.file != null) {
                //display warning about not converting image or edit
                imagefile.setText("Save Directory");
            }
            warning.setVisible(false);

        }else{ //saves output to selected directory
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Select Save directory");
            directory = chooser.showDialog(new Stage());
            if (this.directory != null){
                imagefile.setText("Image");

                //need to check if it actually passed or failed...
                warning.setText("Success - Wallet created");
                warning.setStyle("-fx-fill:#00E676; -fx-opacity:0.5;");
                try {
                    saveWallet();
                }catch (Exception e){
                    //someone went wrong
                }
            }
            warning.setVisible(true);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        HamburgerSlideCloseTransition burgerTask = new HamburgerSlideCloseTransition(hamburger);
        burgerTask.setRate(-1);
        burgerTask.setRate(burgerTask.getRate() * -1);
        burgerTask.play();

        hamburger.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_PRESSED, new EventHandler<Event>(){

            @Override
            public void handle(Event event) {
                try{
                    root = new FXMLLoader().load(getClass().getResource("/Resources/GUI/PrimaryScene.fxml"));
                }catch (IOException ex){

                }
                primaryStage.setScene(new Scene(root, primaryStage.getScene().getWidth(), primaryStage.getScene().getHeight()));
                primaryStage.show();

            }

        });

    }
}
