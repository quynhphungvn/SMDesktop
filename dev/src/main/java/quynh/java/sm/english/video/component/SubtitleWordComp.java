package quynh.java.sm.english.video.component;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import quynh.java.sm.english.dao.VideoDao;
import quynh.java.sm.english.model.SubtitleBlockTimer;
import quynh.java.sm.english.utils.CharacterUtils;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class SubtitleWordComp extends VBox{
	private List<WordLabel> wordLabels = new ArrayList<WordLabel>();
	private MenuLookUp menuLookUp = new MenuLookUp();
	private VBox subtitleList = new VBox();
	private VideoDao videoDao = new VideoDao();
	private ScrollPane scrollPaneSubtitleList;
	private VlcPlayer vlcPlayer;
	
	public SubtitleWordComp(VlcPlayer vlcPlayer) {
		this.vlcPlayer = vlcPlayer;
		subtitleList.setPrefSize(300, 700);
		this.scrollPaneSubtitleList = new ScrollPane(subtitleList);	
		this.getChildren().addAll(menuLookUp, scrollPaneSubtitleList);
	}
	public SubtitleWordComp(List<SubtitleBlockTimer> listBlock, VlcPlayer vlcPlayer) {
		this(vlcPlayer);
		createSubtitleView(listBlock);
	}

	public void createSubtitleView(List<SubtitleBlockTimer> listBlock) {
		subtitleList.getChildren().clear();
		
		String wordKnowns = videoDao.findWordsKnown(1);
		for (SubtitleBlockTimer sub : listBlock) {
			HBox subBlock = new HBox();
			Button playBtn = new Button(">");
			String[] time = sub.getTime().split(",");
			playBtn.setOnAction(e -> {
				EmbeddedMediaPlayer emp = vlcPlayer.getEmbeddedMediaPlayer();			
				long timePosition = getTimeFromString(time[0]);
				vlcPlayer.playVideoAt(timePosition);
				
			});			
			FlowPane wordBlock = new FlowPane();
			wordBlock.setPrefWidth(280);
			String[] words = sub.getContent().split(" ");			
			for (String word : words) {				
				WordLabel wordLabel = new WordLabel(word, isWordKnown(wordKnowns, word), this);
				wordLabels.add(wordLabel);
				wordBlock.getChildren().addAll(wordLabel);
			}
			subBlock.getChildren().addAll(playBtn, wordBlock);
			subtitleList.getChildren().add(subBlock);
		}
	}
	public long getTimeFromString(String time) {
		LocalTime localTime = LocalTime.parse(time);
		long position =  localTime.toSecondOfDay() * 1000;
		return position;
	}
	public boolean isWordKnown(String wordKnown, String wordRaw) {
		String word = CharacterUtils.removeUnexpectedChar(wordRaw);
		return wordKnown.contains(" " + word + " ");
	}
	public void setupBackgroundAllWordChoose(String wordChanged) {
		for (WordLabel wordLabel : wordLabels) {
			if (compareWordLabelText(wordLabel, wordChanged)) {
				wordLabel.setBackgroundGray();
				wordLabel.choosed = true;
			} else {
				wordLabel.choosed = false;
				if (wordLabel.knownState)
					wordLabel.setBackgroundWhite();
				else
					wordLabel.setBackgroundOrange();
			}
		}
	}

	public boolean compareWordLabelText(WordLabel wordLabel, String text) {
		return CharacterUtils.removeUnexpectedChar(wordLabel.getText())
				.equals(CharacterUtils.removeUnexpectedChar(text));
	}
	public void changeWordLabelToKnown(String word) {
		for (WordLabel wordLabel : wordLabels) {
			if (CharacterUtils.removeUnexpectedChar(wordLabel.getText()).equals(word)) {
				wordLabel.setKnownState(true);
				wordLabel.setChoosedState(false);
				wordLabel.setBackgroundWhite();
			}
		}
	}
	public void removeWordLabelFromKnown(String word) {
		for (WordLabel wordLabel : wordLabels) {
			if (CharacterUtils.removeUnexpectedChar(wordLabel.getText()).equals(word))
			{
				wordLabel.setKnownState(false);
				wordLabel.setChoosedState(false);
				wordLabel.setBackgroundOrange();
			}
		}
	}
	public void setInput(String word) {
		menuLookUp.setInput(word);
	}
	private class MenuLookUp extends VBox{
		private VideoDao videoDao = new VideoDao();
		private TextField wordInput;
		private Button addWordBtn;
		private Button deleteWordBtn;
		private Button clearBtn;
		private Button cambBtn;
		private Button tratuBtn;
		private Button googleBtn;
		private Button glishBtn;
		
		public MenuLookUp() {
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
			googleBtn.setOnAction(e -> {
				try {
					new ProcessBuilder("x-www-browser", "https://www.google.com/search?tbm=isch&q=" + wordInput.getText().trim()).start();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			cambBtn.setOnAction(e -> {
				try {
					new ProcessBuilder("x-www-browser", "https://dictionary.cambridge.org/vi/dictionary/english/" + wordInput.getText().trim()).start();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			tratuBtn.setOnAction(e -> {
				try {
					new ProcessBuilder("x-www-browser", "http://tratu.coviet.vn/hoc-tieng-anh/tu-dien/lac-viet/A-V/" + wordInput.getText().trim() +".html").start();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			glishBtn.setOnAction(e -> {
				try {
					new ProcessBuilder("x-www-browser", "https://youglish.com/pronounce/" + wordInput.getText().trim() + "/english?").start();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
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

		public void setInput(String word) {
			this.wordInput.setText(word);		
		}

		public void addWordToKnown(String word) {
			videoDao.addWordToKnown(word);
			changeWordLabelToKnown(word);
		}
		public void removeWordFromKnownList(String word) {
			videoDao.updateWordToUnknown(word);
			removeWordLabelFromKnown(word);
		}
	}
}
