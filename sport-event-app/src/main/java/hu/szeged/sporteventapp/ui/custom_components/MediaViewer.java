package hu.szeged.sporteventapp.ui.custom_components;

import static hu.szeged.sporteventapp.ui.constants.ViewConstants.PICTURES;
import static hu.szeged.sporteventapp.ui.constants.ViewConstants.VIDEOS;

import javax.annotation.PostConstruct;

import com.vaadin.spring.annotation.UIScope;
import hu.szeged.sporteventapp.backend.data.entity.Album;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import hu.szeged.sporteventapp.ui.views.INotifier;

@UIScope
@SpringComponent
public class MediaViewer extends VerticalLayout implements INotifier {

	public static final String CAPTION = "Media";
	private static final String ARROW_BUTTON_STYLE = "arrow-style";

	private final MediaPresenter presenter;

	private Image displayedImage;
	private Video displayedVideo;
	private Button pictureButton;
	private Button videoButton;
	private NativeButton leftButton;
	private NativeButton rightButton;
	private Label captionLabel;
	private VerticalLayout mediaContainer;

	@Autowired
	public MediaViewer(MediaPresenter presenter) {
		this.presenter = presenter;
		setSizeFull();
		initComponent();
		initMainContent();
		initStartState();
	}

	@PostConstruct
	private void initPresenter() {
		presenter.setMediaViewer(this);
	}

	private void initComponent() {
		displayedImage = new Image();
		displayedVideo = new Video();
		pictureButton = new Button(PICTURES);
		videoButton = new Button(VIDEOS);
		leftButton = new NativeButton();
		rightButton = new NativeButton();
		captionLabel = new Label("Caption");
		captionLabel.addStyleName(ValoTheme.LABEL_H2);
		mediaContainer = new VerticalLayout();
		mediaContainer.setSizeFull();
		mediaContainer.setMargin(false);
		initButtons();
	}

	private void initButtons() {
		pictureButton.setIcon(VaadinIcons.CAMERA);
		pictureButton.addClickListener(c -> switchToPictureState());
		videoButton.setIcon(VaadinIcons.MOVIE);
		videoButton.addClickListener(c -> switchToVideoState());
		leftButton.setIcon(VaadinIcons.CHEVRON_LEFT);
		leftButton.addStyleName(ARROW_BUTTON_STYLE);
		rightButton.setIcon(VaadinIcons.CHEVRON_RIGHT);
		rightButton.addStyleName(ARROW_BUTTON_STYLE);
	}

	private void initMainContent() {
		final CssLayout buttonHolder = createButtonHolder();
		final HorizontalLayout mediaContent = createMiddleContent();

		addComponents(buttonHolder, mediaContent, captionLabel);
		setComponentAlignment(buttonHolder, Alignment.MIDDLE_CENTER);
		setComponentAlignment(captionLabel, Alignment.MIDDLE_CENTER);
		setExpandRatio(buttonHolder, 0.1f);
		setExpandRatio(mediaContent, 0.8f);
		setExpandRatio(captionLabel, 0.1f);
	}

	private CssLayout createButtonHolder() {
		CssLayout cssLayout = new CssLayout();
		cssLayout.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		cssLayout.addComponents(pictureButton, videoButton);
		return cssLayout;
	}

	private HorizontalLayout createMiddleContent() {
		MHorizontalLayout layout = new MHorizontalLayout();
		layout.withFullSize().add(leftButton, mediaContainer, rightButton).withExpand(leftButton, 0.1f)
				.withExpand(mediaContainer, 0.8f).withExpand(rightButton, 0.1f)
				.withAlign(leftButton, Alignment.MIDDLE_LEFT).withAlign(rightButton, Alignment.MIDDLE_RIGHT);
		return layout;
	}

	private void initStartState() {
		switchToPictureState();
	}

	private void switchToPictureState() {
		videoButton.removeStyleName(ValoTheme.BUTTON_PRIMARY);
		pictureButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		redrawMediaContainer(displayedImage);
	}

	private void switchToVideoState() {
		pictureButton.removeStyleName(ValoTheme.BUTTON_PRIMARY);
		videoButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		redrawMediaContainer(displayedVideo);
	}

	private void redrawMediaContainer(Component component) {
		mediaContainer.removeAllComponents();
		mediaContainer.addComponent(component);
		mediaContainer.setComponentAlignment(component, Alignment.MIDDLE_CENTER);
	}

	public void setMedia(Album album){
		presenter.setPictures(album.getPictures());
		presenter.setVideos(album.getVideos());
	}
}
