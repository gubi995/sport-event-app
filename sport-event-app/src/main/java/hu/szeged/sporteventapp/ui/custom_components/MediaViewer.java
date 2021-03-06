package hu.szeged.sporteventapp.ui.custom_components;

import static hu.szeged.sporteventapp.ui.constants.ViewConstants.PICTURES;
import static hu.szeged.sporteventapp.ui.constants.ViewConstants.VIDEOS;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import hu.szeged.sporteventapp.backend.data.entity.Album;
import hu.szeged.sporteventapp.ui.views.INotifier;

@SpringComponent
@ViewScope
public class MediaViewer extends VerticalLayout implements INotifier {

	public static final String CAPTION = "Media";
	public static final String ARROW_HOVER_STYLE = "arrow-hover-style";
	private static final String ARROW_BUTTON_STYLE = "arrow-style";
	private static final String MEDIA_CONTAINER_STYLE = "media-container";

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
		presenter.setMediaViewer(this);
		setSizeFull();
		initComponent();
		initMainContent();
		initStartState();
	}

	private void initComponent() {
		displayedImage = new Image();
		displayedImage.setResponsive(true);
		displayedVideo = new Video();
		displayedVideo.setResponsive(true);
		pictureButton = new Button(PICTURES);
		videoButton = new Button(VIDEOS);
		leftButton = new NativeButton();
		rightButton = new NativeButton();
		captionLabel = new Label("Caption");
		captionLabel.addStyleName(ValoTheme.LABEL_H2);
		mediaContainer = new VerticalLayout();
		initMediaContainer();
		initButtons();
	}

	private void initMediaContainer() {
		mediaContainer.addStyleName(MEDIA_CONTAINER_STYLE);
		mediaContainer.setSizeFull();
		mediaContainer.setMargin(false);
	}

	private void initButtons() {
		pictureButton.setIcon(VaadinIcons.CAMERA);
		pictureButton.addClickListener(c -> {
			switchToPictureState();
			presenter.initPictureContent();
		});
		videoButton.setIcon(VaadinIcons.MOVIE);
		videoButton.addClickListener(c -> {
			switchToVideoState();
			presenter.initVideoContent();
		});
		leftButton.setIcon(VaadinIcons.CHEVRON_LEFT);
		leftButton.addStyleName(ARROW_BUTTON_STYLE);
		leftButton.addClickListener(c -> presenter.navigateToLeft());
		rightButton.setIcon(VaadinIcons.CHEVRON_RIGHT);
		rightButton.addStyleName(ARROW_BUTTON_STYLE);
		rightButton.addClickListener(c -> presenter.navigateToRight());
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

	public void switchToPictureState() {
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

	public void showNoFileUploadedYet() {
		mediaContainer.removeAllComponents();
		MLabel label = new MLabel("No file uploaded yet.").withStyleName(ValoTheme.LABEL_H2, ValoTheme.LABEL_COLORED);
		captionLabel.setValue("");
		mediaContainer.addComponent(label);
		mediaContainer.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
	}

	public void setMedia(Album album) {
		presenter.setAlbum(album);
	}

	public Image getDisplayedImage() {
		return displayedImage;
	}

	public Video getDisplayedVideo() {
		return displayedVideo;
	}

	public Label getCaptionLabel() {
		return captionLabel;
	}

	public NativeButton getLeftButton() {
		return leftButton;
	}

	public NativeButton getRightButton() {
		return rightButton;
	}

	@Override
	public void attach() {
		super.attach();
		initStartState();
		presenter.initPictureContent();
	}
}
