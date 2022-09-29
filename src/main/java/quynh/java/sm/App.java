package quynh.java.sm;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import quynh.java.sm.english.dao.PronunciationDao;
import quynh.java.sm.english.pronunciation.component.SymbolTable;
import quynh.java.sm.english.pronunciation.component.VideoOnlinePlayer;
import quynh.java.sm.english.video.component.VideoWatchingTabContent;

/**
 * JavaFX App
 */
public class App extends Application {
	private PronunciationDao pronunciationDao;
	private VideoOnlinePlayer symbolPlayer;
	public App() {
		super();
		pronunciationDao = new PronunciationDao();
		symbolPlayer = new VideoOnlinePlayer();	
	}
    @Override
    public void start(Stage stage) {
        stage.setScene(initEnglishScene());
        stage.show();      
    }
    public Scene initEnglishScene() {
    	HBox root = new HBox();
    	root.getChildren().addAll(initLeftBar(), initMainContent());
    	
    	var scene = new Scene(root, 1800, 768);
    	scene.setFill(Color.web("#aaa"));
    	return scene;
    }
    public VBox initLeftBar() {
    	VBox vbox = new VBox(new Button("left bar"));
    	vbox.setBorder(new Border(new BorderStroke(Color.BLACK, 
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    	return vbox;
    }
    public VBox initMainContent() {
    	TabPane englishTabPane = new TabPane();
    	englishTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    	
    	Tab symbolTab = new Tab("Pronunciation");
    	symbolTab.setContent(symbolTabContent());
    	Tab videoTab = new Tab("Video Watching");
    	videoTab.setContent(new VideoWatchingTabContent());
    	
    	englishTabPane.getTabs().addAll(symbolTab, videoTab);
    	VBox vbox = new VBox(englishTabPane);
    	return vbox;
    }
    public HBox symbolTabContent() {
    	HBox hbox = new HBox();
    	hbox.getChildren().addAll(symbolPlayer, new SymbolTable(pronunciationDao, symbolPlayer));
    	return hbox;
    }
    public static void main(String[] args) {
        launch();
    }

}