package quynh.java.sm.english.video.component;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import quynh.java.sm.english.utils.VideoOfflineFilter;

public class VideoSourceComp extends VBox {
	private String userRootPath = "/home/qin/Desktop/Data/videos";
	private HBox groupChoiceBox = new HBox();
	private ListView<String> listVideo = new ListView();
	private ScrollPane listVideoBox = new ScrollPane(listVideo);
	ChoiceBox<String> groupChoice = new ChoiceBox<String>();
	
	VideoWatchingScreen videoWatchingTabContent;
	
	public VideoSourceComp(VideoWatchingScreen videoWatchingTabContent) {
		this.videoWatchingTabContent = videoWatchingTabContent;
		String[] groups = VideoOfflineFilter.filterGroups(userRootPath);
		ObservableList<String> groupObs = FXCollections.observableArrayList(groups);
		groupChoice.setItems(groupObs.sorted());
		ChangeListener<String> changeListener = new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable,
		            String oldGroupName, String newGroupName) {
		        if (newGroupName != null) {        	
		        	createVideoListOfGroup(userRootPath, newGroupName);
		        }
		    }
		};
		groupChoice.getSelectionModel().selectedItemProperty().addListener(changeListener);
		groupChoiceBox.getChildren().add(groupChoice);
		this.getChildren().addAll(groupChoiceBox, listVideoBox);
	}
	public void createVideoListOfGroup(String userRootPath, String groupName) {
		String groupPath = userRootPath + "/" + groupName;
		String[] videoNames = VideoOfflineFilter.filterVideosInGroup(groupPath);
		ObservableList<String> videos = FXCollections.observableArrayList(videoNames); 
		listVideo.setItems(videos.sorted());
	    listVideo.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	    listVideo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldVideoName, String newVideoName) {
            	String videoUri = groupPath + "/" + newVideoName;
        		videoWatchingTabContent.playVideo(videoUri);       		           		
        		videoWatchingTabContent.createSubtitleListForVideo(videoUri);
            }
        });
	}
}
