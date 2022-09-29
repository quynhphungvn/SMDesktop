package quynh.java.sm.english.video.component.subtitle;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import lombok.Data;

@Data
public class WordLabel extends Label {
	private SubMenu subMenu;
	private Boolean knownState;
	private Boolean choosedState;
	
	public WordLabel(String labelText, Boolean knownState, Boolean choosedState, SubMenu subMenu) {
		super();
		this.setText(labelText);
		this.knownState = knownState;
		this.choosedState = choosedState;
		initStyle();
		this.setOnMouseClicked(e -> {
			setBackgroundWordChoosed();
			subMenu.setupWordChoosed(labelText);
			this.choosedState = true;
		});
		this.setOnMouseEntered(e -> {
			this.setCursor(Cursor.HAND);
			this.setStyle("-fx-padding: 5px; border-radius: 5px;-fx-background-color:#aaa;");
		});
		this.setOnMouseExited(e -> {
			if (this.choosedState)
				setBackgroundWordChoosed();
			else
				setBackgroundForWord(this.knownState);
		});	
	}
	public void initStyle() {
		setBackgroundForWord(knownState);
	}
	public void setBackgroundWordChoosed() {
		this.setStyle("-fx-padding: 5px; border-radius: 5px;-fx-background-color:#aaa;");
	}
	public void setBackgroundForWord(boolean state) {
		if (!state)
			this.setStyle("-fx-padding: 5px; border-radius: 5px;-fx-background-color: orange;");
	}
	public void setupPhrase() {
		
	}
}
