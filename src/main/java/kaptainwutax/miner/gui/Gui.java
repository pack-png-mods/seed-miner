package kaptainwutax.miner.gui;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.*;

public abstract class Gui extends Stage {

	protected int screenWidth = 1920;
	protected int screenHeight = 1080;

	public Gui() {
		try {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			this.screenWidth = screenSize.width >> 1;
			this.screenHeight = screenSize.height >> 1;
		} catch(Exception e) {
			System.err.println("Couldn't retrieve screen dimensions.");
		}
	}

	protected abstract void initialize(StackPane pane);

	public void openGui() {
		StackPane pane = new StackPane();
		this.initialize(pane);
		this.setScene(new Scene(pane, this.screenWidth, this.screenHeight));
		this.show();
	}

	public void closeGui() {
		this.close();
	}

}