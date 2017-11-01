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

	private static final String IMAGE_CONTEXT = "Image";
	private static final String VIDEO_CONTEXT = "Video";

	private List<Picture> pictures;
	private List<Video> videos;
	private MediaViewer mediaViewer;

	private int picturePosition;
	private int videoPosition;
	private int position = 0;
	private String currentContext;

	@Autowired
	public MediaPresenter() {
		picturePosition = 0;
		videoPosition = 0;
	}

	public void setMediaViewer(MediaViewer mediaViewer) {
		this.mediaViewer = mediaViewer;
	}

	public void setImageContent() {
		currentContext = IMAGE_CONTEXT;
		if (!pictures.isEmpty()) {
			loadPicture();
		} else {
			mediaViewer.showNoFileUploadedYet();
		}
		checkNavigationIsPossible(picturePosition, pictures);
	}

	public void setVideoContent() {
		currentContext = VIDEO_CONTEXT;
		if (!videos.isEmpty()) {
			loadVideo();
		} else {
			mediaViewer.showNoFileUploadedYet();
		}
		checkNavigationIsPossible(videoPosition, videos);
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

	private void loadPicture() {
		Picture currentPicture = pictures.get(picturePosition);
		mediaViewer.getDisplayedImage().setSource(ResourceUtil.setEventImageResource(currentPicture.getName()));
		mediaViewer.getCaptionLabel().setValue(getFileNameWithoutExtension(currentPicture.getName()));
	}

	private void loadVideo() {
		Video currentVideo = videos.get(videoPosition);
		mediaViewer.getDisplayedVideo().setSource(ResourceUtil.setVideoResource(currentVideo.getName()));
		mediaViewer.getCaptionLabel().setValue(getFileNameWithoutExtension(currentVideo.getName()));
	}

	private String getFileNameWithoutExtension(String filename) {
		return filename.split("\\.")[0];
	}

	private void checkNavigationIsPossible(int position, List media) {
		checkLeftNavigation(position);
		checkRightNavigation(position, media);
	}

	private void checkLeftNavigation(int position) {
		if (position > 0) {
			mediaViewer.getLeftButton().setEnabled(true);
			mediaViewer.getLeftButton().addStyleName(mediaViewer.ARROW_HOVER_STYLE);
		} else {
			mediaViewer.getLeftButton().setEnabled(false);
			mediaViewer.getLeftButton().removeStyleName(mediaViewer.ARROW_HOVER_STYLE);
		}
	}

	private void checkRightNavigation(int position, List media) {
		if (position < media.size() - 1) {
			mediaViewer.getRightButton().setEnabled(true);
			mediaViewer.getRightButton().addStyleName(mediaViewer.ARROW_HOVER_STYLE);
		} else {
			mediaViewer.getRightButton().setEnabled(false);
			mediaViewer.getRightButton().removeStyleName(mediaViewer.ARROW_HOVER_STYLE);
		}
	}

	public void navigateToLeft() {
		switch (currentContext) {
		case IMAGE_CONTEXT:
			--picturePosition;
			loadPicture();
			checkNavigationIsPossible(picturePosition, pictures);
			break;
		case VIDEO_CONTEXT:
			--videoPosition;
			loadVideo();
			checkNavigationIsPossible(videoPosition, videos);
			break;
		}
	}

	public void navigateToRight() {
		switch (currentContext) {
		case IMAGE_CONTEXT:
			++picturePosition;
			loadPicture();
			checkNavigationIsPossible(picturePosition, pictures);
			break;
		case VIDEO_CONTEXT:
			++videoPosition;
			loadVideo();
			checkNavigationIsPossible(videoPosition, videos);
			break;
		}
	}
}
