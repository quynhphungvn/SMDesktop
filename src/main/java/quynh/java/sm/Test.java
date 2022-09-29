package quynh.java.sm;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Test extends Application {

	@Override
	public void start(Stage stage) {
		Button btn = new Button("text");
		btn.setOnAction(new EventHandler<ActionEvent>() {			
			@Override
			public void handle(ActionEvent event) {
				getHostServices().showDocument("http://www.yahoo.com");
				
			}
		});
		stage.setScene(new Scene(new HBox(btn), 1366, 768));
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
