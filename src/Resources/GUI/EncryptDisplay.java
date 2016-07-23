package Resources.GUI;

import Cryptography.AES;
import Cryptography.Address;
import Cryptography.OutputJSON;
import ExceptionList.PasswordTooShort;
import  ExceptionList.NullCarrier;
import ImageHandle.LoadIMG;
import ImageHandle.Steganography;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.omg.CORBA.ExceptionList;
import org.steganography.error.InvalidTypeException;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static App.Main.primaryStage;

/**
 * Created by Wisdom on 22/07/2016.
 */
public class EncryptDisplay implements Initializable {


    @FXML
    JFXHamburger hamburger;

    Parent root;

    private static File file;
    private static File directory;

    @FXML
    JFXButton imagefile;

    @FXML
    Text warning;

    @FXML
    JFXPasswordField password;

    @FXML
    JFXSlider slider;

    public static StringBuilder address = new StringBuilder();
    public static String EncryptedAddresses;

    //finally creates wallet, in the selected file.
    public void createWallet() throws Exception{

        // PrK: %s WIF: %s PuK: %s Command: ", a[0], a[1], a[2])
        Address address = new Address();

        for(int i = 0; i < (int)this.slider.getValue(); i++) {
            String[] a = address.createNewAddress();
            this.address.append(a[1]+" "+a[2]+"\n");

        }


        //encrypts the address and save them in a static string, so it can be exported
        AES cryptography = new AES(this.address.toString(),  this.password.getText());
        this.EncryptedAddresses = cryptography.encrypt();


        LoadIMG img =   new LoadIMG(this.file);
        String fileName = this.file.toPath().getFileName().toString(), extension = "";

        extension = fileName.substring(fileName.lastIndexOf('.'), fileName.length()).toLowerCase();
        if(extension.equals(".jpg") || extension.equals(".jpeg")){
            //convert jpg to png, since the stegnography class can't handle jpeg images....
            img.saveBuffer(LoadIMG.Type.PNG);
            img.deleteBuffer();//delete the PNG file, to prevent duplicates
            this.file = new File(this.file.getParent()+"\\"+extension);
        }


        //finally, wait until the user selects an output directory, then write the steganography there
    }

    //finally save the wallet in the selected directory
    public void saveWallet(){
        try {

            //writes the data to the stegno file
            //finally, move the file to the output directory
            Steganography a = new Steganography(this.file, new File(this.directory.getAbsolutePath()+"\\Wallet_Image_"+this.file.getName()));
            a.setText(this.EncryptedAddresses);
            a.saveImage();

            //finall, create JSON file with all bitcoin addresses format { "0":"dgdgs", "1":"sdfsdf" }
            String json = this.file.getName().substring(0, this.file.getName().lastIndexOf('.')) + ".json";
            new OutputJSON(this.address.toString(), new File(this.directory.getAbsolutePath()+"\\Wallet_Image_"+json)).export();


            //Clear static setting, for new addresses.. if not, it will contain previous addresses
            this.address.delete(0, this.address.length());
            EncryptedAddresses = "";

        } catch (Exception e){

            System.out.print(e);

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
                warning.setText("Don't convert, crop or manipulate your image wallet in any way.");
                warning.setStyle("-fx-fill:#F44336; -fx-opacity:0.5;");
                warning.setVisible(true);

                try {
                    //create the wallet
                    createWallet();


                }catch (Exception ex){ //failed to create address.

                }
            }

        }else{ //saves output to selected directory
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Select Save directory");
            this.directory = chooser.showDialog(new Stage());
            if (this.directory != null){
                imagefile.setText("Image");

                //saves the wallet
                saveWallet();

                //need to check if it actually passed or failed...
                warning.setText("Success - Wallet created");
                warning.setStyle("-fx-fill:#00E676; -fx-opacity:0.5;");

            }
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
