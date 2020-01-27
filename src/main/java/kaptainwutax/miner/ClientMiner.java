package kaptainwutax.miner;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import kaptainwutax.miner.gui.Gui;
import kaptainwutax.miner.net.connection.Listener;

public class ClientMiner extends Application {

    private Gui gui = new ClientGui();
    Listener listener = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.gui.openGui();
    }

    public class ClientGui extends Gui {
        TextField ip = null;
        TextField port = null;
        Button button = null;

        @Override
        protected void initialize(StackPane pane) {
            ip = new TextField("Enter ip...");
            ip.setMaxWidth(200.0D);
            ip.setTranslateY(-100.0d);

            port = new TextField("Enter port...");
            port.setMaxWidth(200.0D);
            port.setTranslateY(-50.0d);

            button = new Button("Connect");

            button.setOnMouseClicked(event -> {
                listener = new Listener(ip.getText(), Integer.parseInt(port.getText()));

                if(listener.isConnected()) {
                    button.setDisable(true);
                }
            });

            pane.getChildren().add(ip);
            pane.getChildren().add(port);
            pane.getChildren().add(button);
        }
    }

}
