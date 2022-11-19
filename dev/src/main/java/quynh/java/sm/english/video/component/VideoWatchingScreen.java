package quynh.java.sm.english.video.component;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import quynh.java.sm.english.model.SubtitleBlockTimer;
import quynh.java.sm.english.utils.FileReaderSupport;

public class VideoWatchingScreen extends HBox {
	
	private VlcPlayer vlcPlayer = new VlcPlayer();
	//video list component
	
	private ListView<String> listVideo = new ListView();
	private HBox playerBox = new HBox(vlcPlayer.createPlayer());
	//subtitle component
	private VideoSourceComp videoSourceComp = new VideoSourceComp(this);
	private SubtitleWordComp subtitleWordComp = new SubtitleWordComp(vlcPlayer);
	private SubtitleNoteComp subtitleNoteComp = new SubtitleNoteComp();
	
	//data
	public VideoWatchingScreen() {	
		this.setPrefSize(1700, 800);
		playerBox.setPrefSize(900, 750);	
		initScreen();
	}
	
	public void initScreen() {
		
		TabPane srcTabPane = new TabPane();
		srcTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
		VBox srcVideoTabContent = new VBox();	
		VBox srcSubTabContent = new VBox();
		VBox srcSubNoteTabContent = new VBox();
		Tab videoTab = new Tab("Videos");
		videoTab.setContent(srcVideoTabContent);
		Tab subtitleTab = new Tab("Subtitle");
		subtitleTab.setContent(srcSubTabContent);
		Tab noteTab = new Tab("Note");
		noteTab.setContent(srcSubNoteTabContent);
		srcTabPane.getTabs().addAll(videoTab, subtitleTab, noteTab);
		//
		srcVideoTabContent.getChildren().addAll(videoSourceComp);
		 
		srcSubTabContent.getChildren().addAll(subtitleWordComp);
		srcSubNoteTabContent.getChildren().add(subtitleNoteComp);
		this.getChildren().addAll(playerBox, srcTabPane);	
	}
	
	
	public void playVideo(String videoUri) {
		vlcPlayer.playVideo(videoUri);
	}
	public void createSubtitleListForVideo(String videoUri) {
		String subFilePath = videoUri.substring(0, videoUri.lastIndexOf(".")) + ".srt";
		String subtitles =  FileReaderSupport.readStringFromFile(subFilePath);
		List<SubtitleBlockTimer> listSubBlockTimer = convertSubToBlockTimer(subtitles);
		subtitleWordComp.createSubtitleView(listSubBlockTimer);
		//subtitleNoteComp.showListSub(listSubBlockTimer);
	}
	public List<SubtitleBlockTimer> convertSubToBlockTimer(String subString) {
		List<SubtitleBlockTimer> subs = new ArrayList<SubtitleBlockTimer>();
		String[] subContent = null;
		boolean windowCFLF = subString.contains("\r\n\r\n");
		if (windowCFLF)
			subContent = subString.split("\r\n\r\n");
		else
			subContent = subString.split("\n\n");
		if (subContent.length > 0) {
			for (String subBlock : subContent) {
				if (!subBlock.isEmpty()) {
					String[] subBlockParts = null;
					if (windowCFLF)
						subBlockParts = subBlock.split("\r\n");
					else
						subBlockParts = subBlock.split("\n");
					SubtitleBlockTimer subBlockObj = new SubtitleBlockTimer();
					subBlockObj.setIndex(Integer.parseInt(subBlockParts[0]));
					subBlockObj.setTime(subBlockParts[1]);
					String tempContent = "";
					if (subBlockParts.length > 3)
						for (int i = 2; i < subBlockParts.length; i++)
							tempContent = tempContent + subBlockParts[i].trim() + " ";
					else
						tempContent = subBlockParts[2];
					subBlockObj.setContent(tempContent);
					subs.add(subBlockObj);
				}
			}
		}
		return subs;
	}
}
