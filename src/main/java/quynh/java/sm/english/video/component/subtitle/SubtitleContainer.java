package quynh.java.sm.english.video.component.subtitle;

import javafx.scene.layout.VBox;

public class SubtitleContainer extends VBox{

	public SubtitleContainer(String subtitle) {
		super();
		initSubtitleContainer();
	}
	public void initSubtitleContainer() {
		SubMenu subMenu = new SubMenu();
		VBox listPhrase = this.createPhraseBox();
		this.getChildren().addAll(subMenu, new VBox(new WordLabel("name", false, false, subMenu)), listPhrase);
	}
	public VBox createPhraseBox() {
		return new VBox();
	}
}
