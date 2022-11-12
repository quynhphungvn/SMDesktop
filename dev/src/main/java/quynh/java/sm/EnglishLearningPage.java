package quynh.java.sm;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.FlowPane;
import quynh.java.sm.english.dao.PronunciationDao;
import quynh.java.sm.english.pronunciation.component.PronunciationTabContent;
import quynh.java.sm.english.pronunciation.component.VideoOnlinePlayer;
import quynh.java.sm.english.video.component.VideoWatchingScreen;

public class EnglishLearningPage extends FlowPane{
	
	public EnglishLearningPage() {
		TabPane englishTabPane = new TabPane();
    	englishTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    	
    	Tab symbolTab = new Tab("Pronunciation");
    	symbolTab.setContent(new PronunciationTabContent());
    	
    	Tab videoTab = new Tab("Video Watching");
    	videoTab.setContent(new VideoWatchingScreen());
    	
    	englishTabPane.getTabs().addAll(symbolTab, videoTab);
    	this.getChildren().add(englishTabPane);
	}
}
