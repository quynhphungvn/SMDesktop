package quynh.java.sm.english.pronunciation.component;

import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import quynh.java.sm.english.dao.PronunciationDao;
import quynh.java.sm.english.model.Symbol;

public class SymbolTable extends VBox {
	private PronunciationDao pronunciationDao;
	private VideoOnlinePlayer videoPlayer;
	
	public SymbolTable(PronunciationDao pronunciationDao, VideoOnlinePlayer videoPlayer) {
		super();
		this.pronunciationDao = pronunciationDao;
		this.videoPlayer = videoPlayer;
		List<Symbol> symbols = pronunciationDao.findAllSymbol();
		initSymbolTable(symbols);
	}
	public void initSymbolTable(List<Symbol> symbols) {
		FlowPane consonant = new FlowPane();
    	FlowPane vowel = new FlowPane();
    	FlowPane dipthong = new FlowPane();
    	FlowPane rcolor = new FlowPane();

    	for (Symbol symbol : symbols) {
    		SymbolButton btn = new SymbolButton(pronunciationDao, videoPlayer, symbol);
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
    	
    	this.getChildren().addAll(vowelBox, consonantBox, dipthongBox, rColorBox);
	}
}
