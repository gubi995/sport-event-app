package hu.szeged.sporteventapp.ui.custom_components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

import hu.szeged.sporteventapp.backend.data.entity.Album;
import hu.szeged.sporteventapp.backend.data.entity.Picture;
import hu.szeged.sporteventapp.backend.data.entity.Video;
import hu.szeged.sporteventapp.common.util.ResourceUtil;

@UIScope
@SpringComponent
public class MediaPresenter implements Serializable {

	private List<Picture> pictures;
	private List<Video> videos;
	private MediaViewer mediaViewer;

	private int picturePosition;
	private int videoPosition;

	@Autowired
	public MediaPresenter() {
		picturePosition = 0;
		videoPosition = 0;
	}

	public void setMediaViewer(MediaViewer mediaViewer) {
		this.mediaViewer = mediaViewer;
	}

	public void setImageContent() {
		if (!pictures.isEmpty()) {
			Picture currentPicture = pictures.get(picturePosition);
			mediaViewer.getDisplayedImage().setSource(ResourceUtil.setEventImageResource(currentPicture.getName()));
			mediaViewer.getCaptionLabel().setValue(getFileNameWithoutExtension(currentPicture.getName()));
		} else {
			mediaViewer.showNoFileUploadedYet();
		}
		checkNextAndPreviousMedia();
	}

	public void setVideoContent() {
		if (!videos.isEmpty()) {
			Video currentVideo = videos.get(videoPosition);
			mediaViewer.getDisplayedVideo().setSource(ResourceUtil.setVideoResource(currentVideo.getName()));
			mediaViewer.getCaptionLabel().setValue(getFileNameWithoutExtension(currentVideo.getName()));
		} else {
			mediaViewer.showNoFileUploadedYet();
		}
		checkNextAndPreviousMedia();
	}

	public void setAlbum(Album album) {
		pictures = new ArrayList<>(album.getPictures());
		videos = new ArrayList<>(album.getVideos());
	}

	public void initVideoContent() {
		videoPosition = 0;
		setVideoContent();
	}

	public void initPictureContent() {
		picturePosition = 0;
		setImageContent();
	}

	private String getFileNameWithoutExtension(String filename) {
		return filename.split("\\.")[0];
	}

	private void checkNextAndPreviousMedia() {
		nextMediaIsExist();
		previousMediaIsExist();
	}

	private void nextMediaIsExist() {
		if (picturePosition == pictures.size() || videoPosition == videos.size()) {
			mediaViewer.getRightButton().setEnabled(false);
			mediaViewer.getRightButton().removeStyleName(mediaViewer.ARROW_HOVER_STYLE);
		} else {
			mediaViewer.getRightButton().setEnabled(true);
			mediaViewer.getRightButton().addStyleName(mediaViewer.ARROW_HOVER_STYLE);
		}
	}

	private void previousMediaIsExist() {
		if (picturePosition == 0 || videoPosition == 0) {
			mediaViewer.getLeftButton().setEnabled(false);
			mediaViewer.getLeftButton().removeStyleName(mediaViewer.ARROW_HOVER_STYLE);
		} else {
			mediaViewer.getLeftButton().setEnabled(true);
			mediaViewer.getLeftButton().addStyleName(mediaViewer.ARROW_HOVER_STYLE);
		}
	}
}
