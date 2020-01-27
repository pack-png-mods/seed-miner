package kaptainwutax.miner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import kaptainwutax.miner.gui.Gui;
import kaptainwutax.miner.net.connection.Listener;
import kaptainwutax.miner.net.connection.ServerListener;
import kaptainwutax.miner.net.packet.util.PacketRegistry;
import kaptainwutax.miner.packet.C2SLogin;
import kaptainwutax.miner.packet.FindSeeds;
import kaptainwutax.miner.packet.S2CDisconnect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerMiner extends Application {

    static {
        PacketRegistry.registerPacket(S2CDisconnect.class);
        PacketRegistry.registerPacket(C2SLogin.class);
        PacketRegistry.registerPacket(FindSeeds.class);
    }

    private Gui gui = new ServerGui();
    ServerListener serverListener = null;
    public static Map<Integer, UserEntry> entries = new HashMap<>();
    public static SeedSplitter seedSplitter = new SeedSplitter();

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
                            pane.getChildren().addAll(entry.button, entry.closeButton, entry.username, entry.task, entry.progress);
                        });

                        updateUserEntries(pane);
                    });

                    serverListener.onConnectionLost(listener -> {
                        UserEntry entry = entries.remove(listener.getListenerId());

                        if(entry != null) {
                            Platform.runLater(() -> {
                                pane.getChildren().removeAll(entry.button, entry.closeButton, entry.username, entry.task, entry.progress);
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
                entry.button.setTranslateX(50.0D);
                entry.closeButton.setTranslateY(y[0]);
                entry.closeButton.setTranslateX(90.0D);
                entry.username.setTranslateY(y[0]);
                entry.username.setTranslateX(-50.0D);
                entry.task.setTranslateY(y[0]);
                entry.task.setTranslateX(190.0D);
                entry.progress.setTranslateY(y[0]);
                entry.progress.setTranslateX(330.0D);
                y[0] += 50;
            });
        }
    }

    public class UserEntry {
        public Listener listener;
        public Button button;
        public Button closeButton;
        public Label username;
        public TextField task;
        public Label progress;

        public UserEntry(Listener listener) {
            this.listener = listener;
            this.button = new Button("ALLOW");
            this.closeButton = new Button("[X]");
            this.username = new Label("loading...");
            this.task = new TextField();
            this.task.setMaxWidth(150.0D);
            this.progress = new Label("Seed Group: 0");

            this.button.setOnMouseClicked(event -> {
                try {
                    seedSplitter.addUser(this.listener, this.task.getText());
                    this.button.setDisable(true);
                } catch(Exception ignored) {
                }
            });

            this.closeButton.setOnMouseClicked(event -> {
                this.listener.disconnect();
            });
        }
    }

}
