package kaptainwutax.miner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import kaptainwutax.miner.gui.Gui;
import kaptainwutax.miner.net.connection.Listener;
import kaptainwutax.miner.net.packet.util.PacketRegistry;
import kaptainwutax.miner.packet.C2SLogin;
import kaptainwutax.miner.packet.S2CDisconnect;

public class ClientMiner extends Application {

    static {
        PacketRegistry.registerPacket(S2CDisconnect.class);
        PacketRegistry.registerPacket(C2SLogin.class);
    }

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
        TextField username = null;
        TextField ip = null;
        TextField port = null;
        Button button = null;
        Label status = null;

        @Override
        protected void initialize(StackPane pane) {
            username = new TextField("Enter username...");
            username.setMaxWidth(200.0D);
            username.setTranslateY(-150.0D);
            username.setTranslateX(-300.0D);

            ip = new TextField("Enter ip...");
            ip.setMaxWidth(200.0D);
            ip.setTranslateY(-100.0D);
            ip.setTranslateX(-300.0D);

            port = new TextField("Enter port...");
            port.setMaxWidth(200.0D);
            port.setTranslateY(-50.0D);
            port.setTranslateX(-300.0D);

            button = new Button("Connect");
            button.setTranslateX(-300.0D);

            button.setOnMouseClicked(event -> {
                if(!username.getText().isEmpty()) {
                    try {
                        listener = new Listener(ip.getText(), Integer.parseInt(port.getText()));
                    } catch(NumberFormatException e) {
                        status.setText("Invalid port.");
                    }
                } else {
                    status.setText("Invalid username.");
                }

                if(listener != null && listener.isConnected()) {
                    listener.onConnectionLost(listener1 -> {
                        button.setDisable(false);

                        Platform.runLater(() -> {
                            status.setText("Connection lost.");
                        });

                        listener = null;
                    });

                    listener.onConnectionEstablished(listener1 -> {
                        listener1.sendPacket(new C2SLogin(username.getText()));
                    });

                    button.setDisable(true);
                    listener.start();
                    status.setText("");
                } else if(listener != null && !listener.isConnected()) {
                    status.setText("Connection lost.");
                    listener = null;
                }
            });

            status = new Label();
            status.setTextFill(Color.RED);
            status.setTranslateY(30.0D);
            status.setTranslateX(-300.0D);

            pane.getChildren().addAll(username, ip, port, button, status);

            this.setOnCloseRequest(event -> {
                System.out.println("closing");
                if(listener != null)listener.disconnect();
            });
        }
    }

}
