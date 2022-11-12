package quynh.java.sm.english.video.component;

import java.nio.ByteBuffer;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CallbackVideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurfaceAdapters;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.RenderCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.format.RV32BufferFormat;

public class VlcPlayer {
	private MediaPlayerFactory mediaPlayerFactory;
    private EmbeddedMediaPlayer embeddedMediaPlayer;
    private WritableImage videoImage;
    private PixelBuffer<ByteBuffer> videoPixelBuffer;
    private ImageView videoImageView;
    
    public VlcPlayer() {
        mediaPlayerFactory = new MediaPlayerFactory();
        embeddedMediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
        embeddedMediaPlayer.videoSurface().set(new FXCallbackVideoSurface());
        videoImageView = new ImageView();
    }
    public VBox createPlayer() {
    	VBox playerBox = new VBox();  
    	playerBox.setPrefWidth(900);
    	videoImageView.setPreserveRatio(true);
    	videoImageView.fitWidthProperty().bind(playerBox.widthProperty());
    	videoImageView.fitHeightProperty().bind(playerBox.heightProperty());
    	playerBox.getChildren().addAll(videoImageView, createMenuControls());
    	return playerBox;
    }
    public EmbeddedMediaPlayer getEmbeddedMediaPlayer() {
    	return this.embeddedMediaPlayer;
    }
    private FlowPane createMenuControls() {
    	Button pauseBtn = new Button("Pause");
    	Button nextBtn = new Button(">>");
    	Button prevBtn = new Button("<<");
    	Button replay = new Button("R");
    	Button speedUp = new Button("^");
    	Button speedDown = new Button("_");
    	pauseBtn.setOnAction(e -> {
    		embeddedMediaPlayer.controls().pause();
    	});
    	nextBtn.setOnAction(e -> {
    		long length = embeddedMediaPlayer.status().length();
    		float curPos = embeddedMediaPlayer.status().position() * length;
    		embeddedMediaPlayer.controls().setTime((long) (curPos + 5000));    		
    	});
    	prevBtn.setOnAction(e -> {	
    		long length = embeddedMediaPlayer.status().length();
    		float curPos = embeddedMediaPlayer.status().position() * length;
    		embeddedMediaPlayer.controls().setTime((long) (curPos - 5000));
    	});
    	replay.setOnAction(e -> {
    		String mrl = embeddedMediaPlayer.media().info().mrl();
    		embeddedMediaPlayer.media().play(mrl);
    	});
    	speedUp.setOnAction(e -> {
    		float rate = embeddedMediaPlayer.status().rate();
    		if (rate <= 1.5)
    			embeddedMediaPlayer.controls().setRate(rate + 0.15f);
    	});
    	speedDown.setOnAction(e -> {
    		float rate = embeddedMediaPlayer.status().rate();
    		if (rate > 0.5)
    			embeddedMediaPlayer.controls().setRate(rate - 0.15f);
    	});
    	FlowPane fp = new FlowPane();
    	fp.getChildren().addAll(pauseBtn, prevBtn, nextBtn, replay, speedUp, speedDown);
    	return fp;
    }
    public void playVideo(String url) {
    	embeddedMediaPlayer.media().play(url);
    	embeddedMediaPlayer.controls().setRepeat(true);
    }
    public void playVideoAt(long time) {
    	embeddedMediaPlayer.controls().setTime(time);
    }
	private class FXCallbackVideoSurface extends CallbackVideoSurface {
        FXCallbackVideoSurface() {
            super(new FXBufferFormatCallback(), new FXRenderCallback(), true, VideoSurfaceAdapters.getVideoSurfaceAdapter());
        }
    }

    private class FXBufferFormatCallback implements BufferFormatCallback {
        private int sourceWidth;
        private int sourceHeight;

        @Override
        public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
            this.sourceWidth = sourceWidth;
            this.sourceHeight = sourceHeight;
            return new RV32BufferFormat(sourceWidth, sourceHeight);
        }

        @Override
        public void allocatedBuffers(ByteBuffer[] buffers) {
            assert buffers[0].capacity() == sourceWidth * sourceHeight * 4;
            PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteBgraPreInstance();
            videoPixelBuffer = new PixelBuffer<>(sourceWidth, sourceHeight, buffers[0], pixelFormat);
            videoImage = new WritableImage(videoPixelBuffer);
            videoImageView.setImage(videoImage);
        }
    }

    private class FXRenderCallback implements RenderCallback {
        @Override
        public void display(MediaPlayer mediaPlayer, ByteBuffer[] nativeBuffers, BufferFormat bufferFormat) {
            Platform.runLater(() -> {
                videoPixelBuffer.updateBuffer(pb -> null);
            });
        }
    }
}
