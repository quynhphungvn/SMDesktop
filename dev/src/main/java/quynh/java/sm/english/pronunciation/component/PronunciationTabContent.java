package quynh.java.sm.english.pronunciation.component;

import java.util.List;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import quynh.java.sm.english.dao.PronunciationDao;
import quynh.java.sm.english.model.Symbol;
import quynh.java.sm.english.model.SymbolInfo;

public class PronunciationTabContent extends HBox {
	private PronunciationDao pronunciationDao = new PronunciationDao();
	private VideoOnlinePlayer symbolPlayer = new VideoOnlinePlayer();

	public PronunciationTabContent() {
		List<Symbol> symbols = pronunciationDao.findAllSymbol();
		this.getChildren().addAll(symbolPlayer, initSymbolTable(symbols));
	}
	public VBox initSymbolTable(List<Symbol> symbols) {
		VBox symbolTable = new VBox();
		FlowPane consonant = new FlowPane();
		FlowPane vowel = new FlowPane();
		FlowPane dipthong = new FlowPane();
		FlowPane rcolor = new FlowPane();

		for (Symbol symbol : symbols) {
			SymbolButton btn = new SymbolButton(symbol);
			if (symbol.getType().equals("vowel")) {
				vowel.getChildren().add(btn);
			} else if (symbol.getType().equals("diphthong")) {
				dipthong.getChildren().add(btn);
			} else if (symbol.getType().equals("consonant")) {
				consonant.getChildren().add(btn);
			} else {
				rcolor.getChildren().add(btn);
			}
		}
		HBox consonantBox = new HBox();
		consonantBox.getChildren().addAll(new Label("Consonant"), consonant);
		HBox vowelBox = new HBox();
		vowelBox.getChildren().addAll(new Label("vowel"), vowel);
		HBox dipthongBox = new HBox();
		dipthongBox.getChildren().addAll(new Label("Dipthong"), dipthong);
		HBox rColorBox = new HBox();
		rColorBox.getChildren().addAll(new Label("R Color"), rcolor);

		symbolTable.getChildren().addAll(vowelBox, consonantBox, dipthongBox, rColorBox);
		return symbolTable;
	}

	public class SymbolButton extends Button {

		public SymbolButton(Symbol symbol) {
			super();
			this.initButton(symbol);
		}

		public void initButton(Symbol symbol) {
			this.setText(symbol.getContent());
			this.setOnAction(e -> {
				SymbolInfo symbolInfo = pronunciationDao.findSymbolById(symbol.getId(), 1);
				setUpSymbolUi(symbolInfo);
			});
			this.setOnMouseEntered(e -> {
				this.setCursor(Cursor.HAND);
			});
		}
		public void setUpSymbolUi(SymbolInfo symbolInfo) {
			String videoUrl = symbolInfo.getVideoUrl();
			String[] videoId = videoUrl.split("v=");
			String videoEmbededUrl = "https://www.youtube.com/embed/" + videoId[1];
			symbolPlayer.createPlayer(videoEmbededUrl);
		}
	}

}
