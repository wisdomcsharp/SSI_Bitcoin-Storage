package Resources.GUI;

import App.Main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.sun.xml.internal.ws.dump.LoggingDumpTube;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.FocusModel;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.event.ChangeListener;
import javax.swing.text.Position;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static App.Main.primaryStage;

/**
 * Created by Wisdom on 21/07/2016.
 */
public class PrimaryScene implements Initializable {

    @FXML
    JFXHamburger hamburger;
    @FXML
    JFXDrawer drawer;

    @FXML
    JFXButton button;

    @FXML
    AnchorPane drawerContainer;

    @FXML
    AnchorPane anchordecrypt;

    @FXML
    JFXListView listview;

    @FXML
    ImageView image;

    Parent root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        listview.getItems().addAll("Open Wallet","New Addresses");
        listview.getSelectionModel().selectedIndexProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                try {

                    if (newValue.equals(0)) {
                        root = new FXMLLoader().load(getClass().getResource("/Resources/GUI/DecryptDisplay.fxml"));
                    } else if (newValue.equals(1)) {
                        root = new FXMLLoader().load(getClass().getResource("/Resources/GUI/EncryptDisplay.fxml"));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                primaryStage.setScene(new Scene(root, primaryStage.getScene().getWidth(), primaryStage.getScene().getHeight()));
                primaryStage.show();

            }
        });



        drawer.setSidePane(drawerContainer);
        HamburgerSlideCloseTransition burgerTask = new HamburgerSlideCloseTransition(hamburger);
        burgerTask.setRate(-1);

        hamburger.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_PRESSED, new EventHandler<Event>(){

            @Override
            public void handle(Event event) {
                burgerTask.setRate(burgerTask.getRate() * -1);
                burgerTask.play();

                if(drawer.isHidden()){
                    drawer.open();
                }else drawer.close();


            }

        });


    }
}
