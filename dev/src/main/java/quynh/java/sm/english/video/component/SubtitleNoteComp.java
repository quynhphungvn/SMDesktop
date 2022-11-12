package quynh.java.sm.english.video.component;

import java.util.List;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import quynh.java.sm.english.model.SubtitleBlockTimer;

public class SubtitleNoteComp extends VBox {
	private VBox listSubBox = new VBox();
	private ScrollPane listSubScrollPane = new ScrollPane(listSubBox);
	public SubtitleNoteComp() {
		this.getChildren().addAll(listSubScrollPane);
	}
	public SubtitleNoteComp(List<SubtitleBlockTimer> subtitleBlockList) {
		this();
		showListSub(subtitleBlockList);
	}
	public void showListSub(List<SubtitleBlockTimer> subtitleBlockList) {
		for (SubtitleBlockTimer subBlockTimer : subtitleBlockList) {
			Label subLabel = new Label(subBlockTimer.getContent());
			subLabel.setStyle("-fx-padding: 5px; -fx-margin: 0 5px; -fx-border-radius: 5px;-fx-background-color: white;");
			subLabel.setOnDragOver(e -> {
				
			});
			subLabel.setOnMouseEntered(e -> {
				subLabel.setCursor(Cursor.HAND);
				subLabel.setStyle("-fx-padding: 5px; -fx-margin: 0 5px; -fx-border-radius: 5px;-fx-background-color: #aaa;");
			});
			subLabel.setOnMouseExited(e -> {
				subLabel.setStyle("-fx-padding: 5px; -fx-margin: 0 5px; -fx-border-radius: 5px;-fx-background-color: white;");
			});
			
			listSubBox.getChildren().add(subLabel);
		}
	}
}
