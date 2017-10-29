package hu.szeged.sporteventapp.ui.views.eventviews;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Upload;

import hu.szeged.sporteventapp.backend.data.entity.Picture;
import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.backend.data.entity.Video;
import hu.szeged.sporteventapp.backend.repositories.PictureRepository;
import hu.szeged.sporteventapp.backend.repositories.VideoRepository;
import hu.szeged.sporteventapp.backend.service.UserService;
import hu.szeged.sporteventapp.ui.AbstractPresenter;
import hu.szeged.sporteventapp.ui.custom_components.WrappedUpload;

@UIScope
@SpringComponent
public class SingleEventPresenter extends AbstractPresenter<SingleEventView> {

	private final VideoRepository videoRepository;
	private final PictureRepository pictureRepository;
	private SportEvent sportEvent;

	@Autowired
	public SingleEventPresenter(UserService userService, VideoRepository videoRepository,
			PictureRepository pictureRepository) {
		super(userService);
		this.videoRepository = videoRepository;
		this.pictureRepository = pictureRepository;
	}

	public void setSportEvent(SportEvent sportEvent) {
		this.sportEvent = sportEvent;
	}

	public boolean currentUserIsOrganizer(User organizer) {
		return sessionUser.getUsername().equals(organizer.getUsername())
				&& sessionUser.getEmail().equals(organizer.getEmail());
	}

	public void saveMedia(Upload.SucceededEvent event) {
		if (event.getMIMEType().equals(WrappedUpload.JPG) || event.getMIMEType().equals(WrappedUpload.PNG)) {
			pictureRepository.save(new Picture(event.getFilename(), sportEvent.getAlbum()));
		} else if (event.getMIMEType().equals(WrappedUpload.MP4) || event.getMIMEType().equals(WrappedUpload.OGG)) {
			videoRepository.save(new Video(event.getFilename(), sportEvent.getAlbum()));
		}
	}

	@Override
	public void enter() {
		super.enter();
	}
}
