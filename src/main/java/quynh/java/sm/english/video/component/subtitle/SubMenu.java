package quynh.java.sm.english.video.component.subtitle;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
	}
}
