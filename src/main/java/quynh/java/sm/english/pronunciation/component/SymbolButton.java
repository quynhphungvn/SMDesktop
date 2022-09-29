package quynh.java.sm.english.pronunciation.component;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import quynh.java.sm.english.dao.PronunciationDao;
import quynh.java.sm.english.model.Symbol;
import quynh.java.sm.english.model.SymbolInfo;

public class SymbolButton extends Button{
	private PronunciationDao pronunciationDao;
	private VideoOnlinePlayer videoPlayer;
	
	public SymbolButton(PronunciationDao pronunciationDao, VideoOnlinePlayer videoPlayer, Symbol symbol) {
		super();
		this.pronunciationDao = pronunciationDao;
		this.videoPlayer = videoPlayer;
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
        videoPlayer.createPlayer(videoEmbededUrl);
    }
}
