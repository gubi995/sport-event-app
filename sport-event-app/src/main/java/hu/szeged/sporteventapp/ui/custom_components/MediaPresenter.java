package hu.szeged.sporteventapp.ui.custom_components;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

import hu.szeged.sporteventapp.backend.data.entity.Picture;
import hu.szeged.sporteventapp.backend.data.entity.Video;

@UIScope
@SpringComponent
public class MediaPresenter implements Serializable {

	private List<Picture> pictures;
	private List<Video> videos;
	private MediaViewer mediaViewer;

	private int position;

	@Autowired
	public MediaPresenter() {
		position = 0;
	}

	public void setMediaViewer(MediaViewer mediaViewer) {
		this.mediaViewer = mediaViewer;
	}

	public void setPictures(List<Picture> pictures) {
		this.pictures = pictures;
	}

	public void setVideos(List<Video> videos) {
		this.videos = videos;
	}
}
