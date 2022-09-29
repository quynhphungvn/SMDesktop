package quynh.java.sm.english.video.component;

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import quynh.java.sm.english.utils.TextFileReader;
import quynh.java.sm.english.utils.VideoOfflineFilter;
import quynh.java.sm.english.video.component.subtitle.SubMenu;
import quynh.java.sm.english.video.component.subtitle.WordLabel;

public class VideoWatchingTabContent extends HBox {
	private VlcPlayer vlcPlayer;
	private String userRootPath;
	private VBox videoBox;
	private VBox subBox;
	private TabPane srcTabPane;
	private VBox videoTabContent;
	private VBox subTabContent;
	private ChoiceBox<String> groupChoice;
	private ListView<String> listVideo;
	public VideoWatchingTabContent() {
		userRootPath = "/home/qin/Desktop/Data/videos";
		this.vlcPlayer = new VlcPlayer();
		this.srcTabPane = new TabPane();
		this.videoTabContent = new VBox();	
		this.subTabContent = new VBox();
		this.listVideo = new ListView();
		this.groupChoice = new ChoiceBox<String>();
		Tab videoTab = new Tab("Video");
		videoTab.setContent(this.videoTabContent);
		Tab subtitleTab = new Tab("Subtitle");
		subtitleTab.setContent(this.subTabContent);
		srcTabPane.getTabs().addAll(videoTab, subtitleTab);
    	this.getChildren().addAll(vlcPlayer.createPlayer(), srcTabPane);
		initContent();
	}
	
	public void initContent() {
		createVideoPickTabContent();
    	this.createGroupChoice();
	}
    public void createVideoPickTabContent() {   	
		this.videoTabContent.getChildren().addAll(this.groupChoice, this.listVideo);
		
	}
	public void createGroupChoice() {
		String[] groups = VideoOfflineFilter.filterGroups(userRootPath);
		ObservableList<String> groupObs = FXCollections.observableArrayList(groups);

		groupChoice.setItems(groupObs.sorted());

		ChangeListener<String> changeListener = new ChangeListener<String>() {

		    @Override
		    public void changed(ObservableValue<? extends String> observable,
		            String oldGroupValue, String newGroupValue) {
		        if (newGroupValue != null) {
		        	String groupUrl = userRootPath + "/" + newGroupValue;
		        	String[] videoNames = VideoOfflineFilter.filterVideosInGroup(groupUrl);
		        	createVideoList(videoNames, groupUrl);
		        }
		    }
		};
		groupChoice.getSelectionModel().selectedItemProperty().addListener(changeListener);
	}
	public void createVideoList(String[] videoNames, String groupPath ) {
		ObservableList<String> videos = FXCollections.observableArrayList(videoNames); 
		listVideo.setItems(videos.sorted());
	    listVideo.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	    listVideo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            	String videoUrl = groupPath + "/" + newValue;
        		vlcPlayer.playVideo(videoUrl);
        		String subFilePath = getSubtitleFileUrl(videoUrl);
        		List<String> subList = TextFileReader.readFile(subFilePath);
        		if (subList.size() > 0)
        			for (String s : subList) 
        				System.out.println(s);
        		else 
        			System.out.println("No text!");
            }
        });
	}
	public String getSubtitleFileUrl(String videoUrl)  {
		return videoUrl.substring(0, videoUrl.length() - 4) + ".srt";
	}
	public VBox createSubtitleTabContent() {
		VBox subtitleTabContent = new VBox();
		SubMenu subMenu = new SubMenu();
		VBox listPhrase = this.createPhraseBox();
		subtitleTabContent.getChildren().addAll(subMenu, new VBox(new WordLabel("name", false, false, subMenu)), listPhrase);
		return subtitleTabContent;
	}
	public VBox createPhraseBox() {
		return new VBox();
	}
}
