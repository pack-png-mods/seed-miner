package kaptainwutax.miner;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import kaptainwutax.miner.gui.Gui;
import kaptainwutax.miner.net.connection.Listener;
import kaptainwutax.miner.net.connection.ServerListener;

public class ServerMiner extends Application {

    private Gui gui = new ServerGui();
    ServerListener serverListener = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.gui.openGui();
    }

    public class ServerGui extends Gui {
        TextField port = null;
        Button button = null;

        @Override
        protected void initialize(StackPane pane) {
            port = new TextField("Enter port...");
            port.setMaxWidth(200.0D);
            port.setTranslateY(-50.0d);

            button = new Button("Host");

            button.setOnMouseClicked(event -> {
                serverListener = new ServerListener(Integer.parseInt(port.getText()));

                if(serverListener.isConnected()) {
                    button.setDisable(true);
                }
            });

            pane.getChildren().add(port);
            pane.getChildren().add(button);
        }
    }

}
