package quynh.java.sm.english.video.component;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import quynh.java.sm.english.utils.CharacterUtils;

public class WordLabel extends Label{
	protected String textValue;
	protected boolean choosed = false;
	protected boolean knownState;

	public WordLabel(String textValue, boolean knownState, SubtitleWordComp subtitleWordComp) {	
		this.textValue = textValue;
		this.setText(textValue);
		this.knownState = knownState;
		if (!knownState) 
			setBackgroundOrange();
		else 
			setBackgroundWhite();
		this.setOnMouseClicked(e -> {
			subtitleWordComp.setInput(CharacterUtils.removeUnexpectedChar(textValue));
			subtitleWordComp.setupBackgroundAllWordChoose(textValue);
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
