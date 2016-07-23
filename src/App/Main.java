package App;/**
 * Created by Wisdom on 21/07/2016.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;

        Parent root = new FXMLLoader().load(getClass().getResource("/Resources/GUI/PrimaryScene.fxml"));
        primaryStage.setScene(new Scene(root));

        primaryStage.setTitle("CryptoStandard - Security Standard Implementation");
        primaryStage.setMinHeight(450);
        primaryStage.setMinWidth(450);
        primaryStage.show();
    }
}





















/*
      if (line.toLowerCase().equals("address")) {

                Address address = new Address();
                String[] a = address.createNewAddress();
                System.out.print(String.format("\nPrK: %s\nWIF: %s\nPuK: %s\n\nCommand: ", a[0], a[1], a[2]));
            }
            if (line.toLowerCase().equals("aes")) {

                AES cryptography = new AES("AZna6u0teMgJ6PXTxiBGtquzTlG/f8XeHag6qzt0EjvnAVqNiQ56i7019U3Y32NR", "7D24A35BDFB017C9E8C5701A1BB4DBF6");

                String data = cryptography.decrypt();
                System.out.print(data);

            }
            if (line.toLowerCase().equals("write")) {

                File png = new File("C:\\Users\\Wisdom\\Desktop\\StegoTemplate.png");
                LoadIMG img =   new LoadIMG(png);
              //  img.saveBuffer(LoadIMG.Type.PNG);

               img.saveBuffer(LoadIMG.Type.PNG);
                File nfile = new File("C:\\Users\\Wisdom\\Desktop\\StegoTemplate.png");

SimpleExample a = new SimpleExample(png);

                a.setText("AIHD/3NzSNh/KbSCjYK5ajqb9mAeg1qIlMakaY7lBm2xVa1I1Kl9vYB0VRc+jFXdLg40N5SkPdtaGkZRv0FuGs58v+MTNIfV/67jtv7Wrue+LknnMlj4aiz93QYcIqGTGJs52yGb8V/rI2cOmJ91EIVISju714olteDINIVfjzs7qXuvWmRr/7DGQvTspfgesYScRBtJ/ymDrUk/3x1BS3x99sYwG+X1YZ/QtgxNIruDdCiPu7BivqOUoTVWFNuiWGzSyOS95JxZAcYJxqccYuaQvAdyOyt3BDqH02TaFFT+n11oRIDcOG/he/yMAAAQeqTWP+33L1atlqdXeONV1RRkNdb8Ax2P9zcjoKB75TmTSCHGxhyFFbpgsbE5r7WHoP2NCxl4yZTnXIINyHAHJuRQznRmGDj0CqbjXb1w/ZhAJhSG5JBTANHGGw8umbxJGWBd3HpJni8k5mpo7NZCPRYf1DuJ15RuPhvCc1MTq6V5JiP8JDcwBkYqXwPc6wlF9liaQYgN912CUm40KY/rqxB0/QkCY6hivDMsEglvgJzYP030WcZpy+cLC2Ylr47RXdUs2MygKJ+Z53wQgWs5mpEQHJjtfphfpyC1KSDTqoH6PVxsOeX6HogYuZW/OjZ3YpZoy06ac2ObMHHtyJE5c6n0NIM24Tv1hjco8zREPAOvejjW4Bp9Bnh0Uk65qJCZtslBhVQH/T8V7lXHvMfO3f6I6TEG2/8ZHqoKXChe2T4vru4c7cYdFqKa73vWAT4kzchui5Y+SaNXpcVmk9K9u+dxftCZU2IHBwiXUsi3jZPoFmyNDq285m0cFPI+zZMSJT5TKkhWB7AG3s79rHb6s8gu8wKYgfEuT8PanehyQr69ZibAS2hOPY5Yml4BZG+vYi9R8s2i5wKaU4pc2u/DO5DzTPNRO2kIGihtFYNql1l9gXq7nldD9t+pf4eO3HkzSLuDTh9AVIN2IkysE+6lfkgswuri/+dsT9G1IYqg7+1NvJQJ55YbFYr/she+nhUqjBg3c6vC/HRAToFFAmSxUqdu1DPrCaeKZVwWtHuddhaGUWaPLOYFK14I8LOY1iq5BmOqJL+SOuY8AjuVteCM7XisFik6ax0x9+lLE9o5OxVwcsKOyHsqo4qbOs2UBf+zhxk1HCw8tWvKoJ4NovX0skrW8uFjs8yO8maeQghnuBIp9nhes157EuZnXvffT7TX5yHcrnwewApSb99d3967CIiztterllnWl9ftqd+Hg/X/tJrPSjUzmFehDjU56VGUUPIZtAZrCiv8tb9cDrshL4CJMYcT0e4uZ4GFicYt6CD/cQqeVeHWISiPljSCOacBBU5DwhHUpZQ7RocCY2HLYULq7cb19DxsXwUtX2WIsT5OASHvzz/m7+VV30fGH+3Ovmd8xsSvMgEhuQdbvTPQpqRoYc07VTSN3l8uW34w0MbI7jEwDtIPqqkZMoJ24bRSPt4R65Bc8BuUHZfNPWlLkxVK7BAJLNN2iC3jRpQs/ayLb0tXucz/z3ttBJlbshXttR4k7M//ZrkWrXCEqXxWzjCGaRyd2Ztq6eU8du4qHRqum2WxhrUrqTlO5tO6sw2vsimzXy663KDHjIr5n4Iz0Ims/0F+RyJ87lizQvm/ZH7hj9JVyMzzkrfWqIhzkcwYykPsCsUxWgf1nbzcMD5+ws6mfVtSHZQYMc3ZgsNsuQNDCeSkuQa3ESjiK4SOymb7gOEAhdn5smo4rga6bgP/AeA1P66BFw1QXIkAXQfiFt2W9cStpr8AtWA3exMB0xFME1CUiWkp4a/Uj4nZ51auFHFUqeI110v+PNTMtxABAmrZQ6NAOupmdNrv5frLsrBzhgEib9oHnGNPtlBe807meKLifdRx2jY2IkhCTP1WBttDmjRzMPQ3KXdrevppGynGGhFbyI/4epXsT5kj2QpXCQ==");
                        a.saveImage();

                        System.out.print(a.getText());

                        System.out.println("\n\ndone");

                        }
 */
