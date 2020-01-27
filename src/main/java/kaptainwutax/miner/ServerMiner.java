package kaptainwutax.miner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import kaptainwutax.miner.gui.Gui;
import kaptainwutax.miner.net.connection.Listener;
import kaptainwutax.miner.net.connection.ServerListener;
import kaptainwutax.miner.net.packet.util.PacketRegistry;
import kaptainwutax.miner.packet.C2SLogin;
import kaptainwutax.miner.packet.S2CDisconnect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerMiner extends Application {

    static {
        PacketRegistry.registerPacket(S2CDisconnect.class);
        PacketRegistry.registerPacket(C2SLogin.class);
    }

    private Gui gui = new ServerGui();
    ServerListener serverListener = null;
    public static Map<Integer, UserEntry> entries = new HashMap<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.gui.openGui();
    }

    public class ServerGui extends Gui {
        public TextField port = null;
        public Button button = null;

        @Override
        protected void initialize(StackPane pane) {
            port = new TextField("Enter port...");
            port.setMaxWidth(200.0D);
            port.setTranslateY(-50.0d);
            port.setTranslateX(-300.0D);

            button = new Button("Host");
            button.setTranslateX(-300.0D);

            button.setOnMouseClicked(event -> {
                serverListener = new ServerListener(Integer.parseInt(port.getText()));

                if(serverListener.isConnected()) {
                    button.setDisable(true);

                    serverListener.onConnectionEstablished(listener -> {
                        UserEntry entry = new UserEntry(listener);
                        entries.put(listener.getListenerId(), entry);

                        Platform.runLater(() -> {
                            pane.getChildren().addAll(entry.button, entry.closeButton, entry.username);
                        });

                        updateUserEntries(pane);
                    });

                    serverListener.onConnectionLost(listener -> {
                        UserEntry entry = entries.remove(listener.getListenerId());

                        if(entry != null) {
                            Platform.runLater(() -> {
                                pane.getChildren().removeAll(entry.button, entry.closeButton, entry.username);
                            });
                        }

                        updateUserEntries(pane);
                    });

                    serverListener.start();
                }
            });

            pane.getChildren().add(port);
            pane.getChildren().add(button);

            this.setOnCloseRequest(event -> {
                System.out.println("closing");
                if(serverListener != null) {
                    serverListener.sendPacketToAll(new S2CDisconnect());
                    serverListener.disconnect();
                }
            });
        }

        protected void updateUserEntries(StackPane pane) {
            final int[] y = {-250};

            entries.forEach((id, entry) -> {
                entry.button.setTranslateY(y[0]);
                entry.button.setTranslateX(200.0D);
                entry.closeButton.setTranslateY(y[0]);
                entry.closeButton.setTranslateX(240.0D);
                entry.username.setTranslateY(y[0]);
                entry.username.setTranslateX(100.0D);
                y[0] += 50;
            });

            System.out.println(pane.getChildren());
        }
    }

    public class UserEntry {
        public Listener listener;
        public Button button;
        public Button closeButton;
        public Label username;

        public boolean allowed = false;

        public UserEntry(Listener listener) {
            this.listener = listener;
            this.button = new Button("ALLOW");
            this.closeButton = new Button("[X]");
            this.username = new Label("loading...");

            this.button.setOnMouseClicked(event -> {
                allowed = true;
                this.button.setDisable(true);
            });

            this.closeButton.setOnMouseClicked(event -> {
                this.listener.disconnect();
            });
        }
    }

}
