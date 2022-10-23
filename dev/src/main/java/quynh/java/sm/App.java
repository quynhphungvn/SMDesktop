package quynh.java.sm;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {
	
    @Override
    public void start(Stage stage) {
        stage.setScene(initEnglishScene(stage));
        stage.show();      
    }
    public Scene initEnglishScene(Stage stage) {
    	HBox root = new HBox();
    	var scene = new Scene(root, 1750, 850);
    	VBox leftbarBox = new VBox();
    	VBox mainContent = new VBox();
    	ManagePage manageBox = new ManagePage();
    	EnglishLearningPage englishBox = new EnglishLearningPage();
    	Button manageBtn = new Button("Manage");
    	manageBtn.setOnAction(e -> {
    		mainContent.getChildren().clear();
    		mainContent.getChildren().add(manageBox);
    	});
    	Button englishBtn = new Button("English");
    	englishBtn.setOnAction(e -> {
    		mainContent.getChildren().clear();
    		mainContent.getChildren().add(englishBox);
    	});
    	
    	leftbarBox.getChildren().addAll(manageBtn, englishBtn);
    	leftbarBox.setPrefWidth(90);
    	leftbarBox.setBorder(new Border(new BorderStroke(Color.BLACK, 
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    	
    	root.getChildren().addAll(leftbarBox, mainContent);   	
    	scene.setFill(Color.web("#aaa"));
    	return scene;
    }
    
    public static void main(String[] args) {
        launch();
    }

}