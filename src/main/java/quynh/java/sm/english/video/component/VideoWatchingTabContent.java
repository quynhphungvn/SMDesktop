package quynh.java.sm.english.video.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import quynh.java.sm.english.dao.VideoDao;
import quynh.java.sm.english.model.Phrase;
import quynh.java.sm.english.model.SubtitleTimer;
import quynh.java.sm.english.utils.CharacterUtils;
import quynh.java.sm.english.utils.FileReaderSupport;
import quynh.java.sm.english.utils.VideoOfflineFilter;

public class VideoWatchingTabContent extends HBox {
	private List<WordLabel> wordLabels;
	private VideoDao videoDao = new VideoDao();
	private VlcPlayer vlcPlayer = new VlcPlayer();
	private String userRootPath = "/home/qin/Desktop/Data/videos";
	//video list component
	
	private ListView<String> listVideo = new ListView();
	private HBox playerBox = new HBox(vlcPlayer.createPlayer());
	//subtitle component
	private SubMenu subMenu = new SubMenu();
	private VBox subtitleList;
	private ListView<Phrase> phraseListView = new ListView<Phrase>();
	private FlowPane phraseMeaningPane;
	private ScrollPane scrollPaneSubtitleList;
	//data
	
	public VideoWatchingTabContent() {	
		this.setPrefSize(1700, 800);
		playerBox.setPrefSize(900, 750);
		this.wordLabels = new ArrayList<WordLabel>();	
		this.subtitleList = new VBox(new Label("No sub"));
		subtitleList.setPrefSize(300, 700);
		this.scrollPaneSubtitleList = new ScrollPane(subtitleList);	
		this.phraseMeaningPane = new FlowPane();
		initScreen();
	}
	
	public void initScreen() {
		
		TabPane srcTabPane = new TabPane();
		VBox srcVideoTabContent = new VBox();	
		VBox srcSubTabContent = new VBox();
		Tab videoTab = new Tab("Video");
		videoTab.setContent(srcVideoTabContent);
		Tab subtitleTab = new Tab("Subtitle");
		subtitleTab.setContent(srcSubTabContent);
		srcTabPane.getTabs().addAll(videoTab, subtitleTab);
		//
		HBox groupChoiceBox = new HBox();
		VBox listVideoBox = new VBox();
		srcVideoTabContent.getChildren().addAll(groupChoiceBox, listVideoBox);
		ChoiceBox<String> groupChoice = new ChoiceBox<String>();
		 
		//
		VBox phraseBox = new VBox();
		phraseBox.setPrefSize(300, 500);
		VBox phraseContentBox = new VBox();
		VBox phraseMeaningBox = new VBox();
		phraseMeaningBox.setPrefSize(300, 200);
		phraseBox.getChildren().addAll(phraseContentBox, phraseMeaningBox);
		
		phraseContentBox.getChildren().add(phraseListView);
		ScrollPane phraseMeaningScroll = new ScrollPane(phraseMeaningPane);
		phraseMeaningScroll.setPrefSize(300, 200);
		phraseMeaningBox.getChildren().add(phraseMeaningScroll);
		
		VBox subMenuBox = new VBox();
		VBox subListBox = new VBox();
		
		srcSubTabContent.getChildren().addAll(subMenuBox, subListBox);
		
		subMenuBox.getChildren().add(subMenu);
		subListBox.getChildren().add(scrollPaneSubtitleList);
		
		this.getChildren().addAll(playerBox, srcTabPane, phraseBox);
		//set data to group choice
		groupChoiceBox.getChildren().add(groupChoice);
		listVideoBox.getChildren().add(listVideo);
		
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
        		playVideo(videoUri);       		           		
        		createSubtitleListForVideo(videoUri);
            }
        });
	}
	public void playVideo(String videoUri) {
		vlcPlayer.playVideo(videoUri);
	}
	public void createSubtitleListForVideo(String videoUri) {
		String subFilePath = videoUri.substring(0, videoUri.length() - 4) + ".srt"; 
		List<SubtitleTimer> listSubBlock =  FileReaderSupport.readSubtitleBlockFile(subFilePath);
		createSubtitleListBox(listSubBlock);
	}
	public void createSubtitleListBox(List<SubtitleTimer> listSubBlock) {
		subtitleList.getChildren().clear();
		String wordKnowns = videoDao.findWordsKnown(1);
		for (SubtitleTimer sub : listSubBlock) {
			FlowPane fp = new FlowPane();
			fp.setPrefWidth(280);
			String[] words = sub.getContent().split(" ");			
			for (String word : words) {				
				WordLabel wordLabel = new WordLabel(word, isWordKnown(wordKnowns, word));
				wordLabels.add(wordLabel);
				fp.getChildren().addAll(wordLabel);
			}
			subtitleList.getChildren().add(fp);
		}
		scrollPaneSubtitleList.setContent(subtitleList);
	}
	public boolean isWordKnown(String wordKnown, String wordRaw) {
		String word = CharacterUtils.removeUnexpectedChar(wordRaw);
		return wordKnown.contains(" " + word + " ");
	}
	
	public class WordLabel extends Label {
		private String textValue;
		private boolean choosed = false;
		private boolean knownState;
		
		public WordLabel(String textValue, boolean knownState) {	
			this.textValue = textValue;
			this.setText(textValue);
			this.knownState = knownState;
			if (!knownState) 
				setBackgroundOrange();
			else 
				setBackgroundWhite();
			this.setOnMouseClicked(e -> {
				choosed = true;
				subMenu.setupWordChoosed(CharacterUtils.removeUnexpectedChar(textValue));
				setupBackgroundAllWordChoose();
				setupPhraseLookup();
			});
			this.setOnMouseEntered(e -> {
				setCursor(Cursor.HAND);
				setBackgroundGray();
			});
			this.setOnMouseExited(e -> {
				if (!knownState)
					setBackgroundOrange();
				else 
					setBackgroundWhite();
				if (choosed) {
					setBackgroundGray();
				}
			});	
		}
		private void setupPhraseLookup() {
			List<Phrase> phraseList = videoDao.findPhraseByWord(this.textValue);
			System.out.println(phraseList.size());
			ObservableList<Phrase> phrases = FXCollections.observableArrayList(phraseList); 
			phraseListView.setItems(phrases.sorted());
			phraseListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			phraseListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Phrase>() {
	            @Override
	            public void changed(ObservableValue<? extends Phrase> observable, Phrase oldPhrase, Phrase newPhrase) {
	            	String meaning = videoDao.findMeaningOfPhrase(newPhrase.getId());
	            	phraseMeaningPane.getChildren().clear();
	            	Label textLb = new Label(meaning);
	            	textLb.setPrefWidth(280);
	            	textLb.setWrapText(true);
	            	phraseMeaningPane.getChildren().add(textLb);
	            	
	            }
	        });
		}
		public void setupBackgroundAllWordChoose() {
			for (WordLabel wordLabel : wordLabels) {
				if (compareWordLabelText(wordLabel, this.textValue)) {
					wordLabel.setBackgroundGray();
					wordLabel.choosed = true;
				}
				else {
					wordLabel.choosed = false;
					if (wordLabel.knownState)
						wordLabel.setBackgroundWhite();
					else 
						wordLabel.setBackgroundOrange();
				}
			}
		}
		public boolean compareWordLabelText(WordLabel wordLabel, String text) {
			return CharacterUtils.removeUnexpectedChar(wordLabel.getText()).equals(CharacterUtils.removeUnexpectedChar(text));
		}
		public void setBackgroundGray() {
			setStyle("-fx-padding: 5px; -fx-margin: 0 5px; -fx-border-radius: 5px;-fx-background-color: #aaa;");
		}
		public void setBackgroundOrange() {
			setStyle("-fx-padding: 5px; -fx-margin: 0 5px; -fx-border-radius: 5px;-fx-background-color: orange;");
		}
		public void setBackgroundWhite() {
			setStyle("-fx-padding: 5px; -fx-margin: 0 5px; -fx-border-radius: 5px;-fx-background-color: white;");
		}
		public boolean isChoosed() {
			return this.choosed;
		}
		public void setKnownState(boolean state) {
			this.knownState = state;
		}
		public void setChoosedState(boolean state) {
			this.choosed = state;
		}
	}
	public class SubMenu extends VBox {
		private TextField wordInput;
		private Button addWordBtn;
		private Button deleteWordBtn;
		private Button clearBtn;
		private Button cambBtn;
		private Button tratuBtn;
		private Button googleBtn;
		private Button glishBtn;
		
		public SubMenu() {
			this.wordInput = new TextField();
			this.addWordBtn = new Button("+");
			this.deleteWordBtn = new Button("-");
			this.clearBtn = new Button("clear");
			this.cambBtn = new Button("camb");
			this.tratuBtn = new Button("tratu");
			this.googleBtn = new Button("google");
			this.glishBtn = new Button("glish");
			HBox row1 = new HBox();
			row1.getChildren().addAll(wordInput, addWordBtn, deleteWordBtn, clearBtn);
			HBox row2 = new HBox();
			row2.getChildren().addAll(cambBtn, googleBtn, tratuBtn, glishBtn);
			this.getChildren().addAll(row1, row2);
		}

		public void setupWordChoosed(String word) {
			this.wordInput.setText(word);
			String linkYouglish = "https://youglish.com/pronounce/" + word + "/english?";
			String linkGoogle = "https://www.google.com/search?tbm=isch&q=" + word;
			String linkCambridge = "https://dictionary.cambridge.org/vi/dictionary/english/" + word;
			String linkTratu = "http://tratu.coviet.vn/hoc-tieng-anh/tu-dien/lac-viet/A-V/" + word +".html";
			googleBtn.setOnAction(new EventHandler<ActionEvent>() {			
				@Override
				public void handle(ActionEvent event) {
					try {
						new ProcessBuilder("x-www-browser", linkGoogle).start();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
			cambBtn.setOnAction(new EventHandler<ActionEvent>() {			
				@Override
				public void handle(ActionEvent event) {
					try {
						new ProcessBuilder("x-www-browser", linkCambridge).start();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}			
				}
			});
			tratuBtn.setOnAction(new EventHandler<ActionEvent>() {			
				@Override
				public void handle(ActionEvent event) {
					try {
						new ProcessBuilder("x-www-browser", linkTratu).start();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
			glishBtn.setOnAction(new EventHandler<ActionEvent>() {			
				@Override
				public void handle(ActionEvent event) {
					try {
						new ProcessBuilder("x-www-browser", linkYouglish).start();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			addWordBtn.setOnAction(e -> {
				String textContent = this.wordInput.getText().trim();
				if (!textContent.isEmpty()) 
					addWordToKnown(textContent);		
			});
			deleteWordBtn.setOnAction(e -> {
				String textContent = this.wordInput.getText().trim();
				removeWordFromKnownList(textContent);
			});
		}

		public void addWordToKnown(String word) {
			videoDao.addWordToKnown(word);
			for (WordLabel wordLabel : wordLabels) {
				if (CharacterUtils.removeUnexpectedChar(wordLabel.getText()).equals(word)) {
					wordLabel.setKnownState(true);
					wordLabel.setChoosedState(false);
					wordLabel.setBackgroundWhite();
				}
			}
		}
		public void removeWordFromKnownList(String word) {
			videoDao.updateWordToUnknown(word);
			for (WordLabel wordLabel : wordLabels) {
				if (CharacterUtils.removeUnexpectedChar(wordLabel.getText()).equals(word))
				{
					wordLabel.setKnownState(false);
					wordLabel.setChoosedState(false);
					wordLabel.setBackgroundOrange();
				}
			}
		}
	}
}
