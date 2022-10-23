package quynh.java.sm.english.pronunciation.component;

import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;


public class VideoOnlinePlayer extends BorderPane {
	private WebView webview;
    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

	public VideoOnlinePlayer() {
		this.mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		webview = new WebView();
		createPlayer("https://www.youtube.com/embed/TT6KRHoimiQ");
	}
	public void createPlayer(String youtubeUrl) {
        webview.getEngine().load(youtubeUrl);
        webview.setPrefSize(889, 500);
        this.setCenter(webview);	
	}
}
