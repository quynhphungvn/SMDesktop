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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {
	private VBox mainContent = new VBox();
    @Override
    public void start(Stage stage) {
        stage.setScene(initEnglishScene(stage));
        stage.show();      
    }
    public Scene initEnglishScene(Stage stage) {
    	HBox root = new HBox();
    	root.getChildren().addAll(createLeftBar(), mainContent);   	
    	var scene = new Scene(root, 1750, 850);
    	scene.setFill(Color.web("#aaa"));
    	return scene;
    }
    public VBox createLeftBar() {
    	VBox leftbarBox = new VBox();
    	Button manageBtn = createBtnControl("Manage", new ManagePage());
    	Button englishBtn = createBtnControl("English", new EnglishLearningPage());
    	leftbarBox.getChildren().addAll(manageBtn, englishBtn);
    	leftbarBox.setPrefWidth(90);
    	leftbarBox.setBorder(new Border(new BorderStroke(Color.BLACK, 
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    	return leftbarBox;
    }
    public Button createBtnControl(String btnName, Pane paneToShow) {
    	Button btn = new Button(btnName);
    	btn.setOnAction(e -> {    		
    		mainContent.getChildren().clear();
    		mainContent.getChildren().add(paneToShow);
    	});
    	return btn;
    }
    
    public static void main(String[] args) {
        launch();
    }

}