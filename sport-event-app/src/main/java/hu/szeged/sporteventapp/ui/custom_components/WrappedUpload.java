package hu.szeged.sporteventapp.ui.custom_components;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Upload;

import hu.szeged.sporteventapp.common.exception.UnsupportedFileTypeException;
import hu.szeged.sporteventapp.common.util.ResourceUtil;
import hu.szeged.sporteventapp.ui.views.INotifier;

@SpringComponent
public class WrappedUpload extends CustomComponent implements INotifier, Upload.Receiver, Upload.StartedListener,
		Upload.ProgressListener, Upload.FailedListener, Upload.SucceededListener, Upload.FinishedListener {

	public static final String JPG = "image/jpeg";
	public static final String PNG = "image/png";
	public static final String OGG = "video/ogg";
	public static final String MP4 = "video/mp4";

	private Upload upload;
	private ProgressBar progressBar;

	private Set<String> allowedMimeTypes;
	private String context;

	@Autowired
	public WrappedUpload() {
		initComponent();
		initContent();
	}

	private void initComponent() {
		progressBar = new ProgressBar();
		upload = new Upload();
		upload.setReceiver(this);
		upload.addStartedListener(this);
		upload.addProgressListener(this);
		upload.addFailedListener(this);
		upload.addFinishedListener(this);
		upload.addSucceededListener(this);
	}

	public Upload getUpload() {
		return upload;
	}

	public void setAllowedMimeType(String... allowedMimeTypes) {
		this.allowedMimeTypes = new HashSet<>(Arrays.asList(allowedMimeTypes));
	}

	public void setContext(String context) {
		this.context = context;
	}

	private void initContent() {
		progressBar.setVisible(false);
		setCompositionRoot(new MVerticalLayout().withMargin(false).add(upload, progressBar));
	}

	@Override
	public void uploadFailed(Upload.FailedEvent failedEvent) {
		progressBar.setVisible(false);
		showErrorNotification("Upload failed");
	}

	@Override
	public void uploadFinished(Upload.FinishedEvent finishedEvent) {
		progressBar.setVisible(false);
		showInfoNotification("Upload successful");
	}

	@Override
	public void uploadStarted(Upload.StartedEvent startedEvent) {
		progressBar.setVisible(true);
	}

	@Override
	public void uploadSucceeded(Upload.SucceededEvent succeededEvent) {
		progressBar.setVisible(false);
	}

	@Override
	public void updateProgress(final long readBytes, final long contentLength) {
		progressBar.setValue(readBytes / (float) contentLength);
	}

	@Override
	public OutputStream receiveUpload(final String filename, final String mimeType) {

		if (allowedMimeTypes.contains(mimeType)) {
			try {
				StringBuilder webAppPathBuilder = new StringBuilder(
						VaadinService.getCurrent().getBaseDirectory().getAbsolutePath());
				webAppPathBuilder.append("/VAADIN/themes/mytheme/");
				webAppPathBuilder.append(selectProperFolder(mimeType));
				webAppPathBuilder.append(filename);
				return new FileOutputStream(new File(webAppPathBuilder.toString()));
			} catch (UnsupportedFileTypeException ufte) {
				showErrorNotification("Unsupported file type");
				return null;
			} catch (FileNotFoundException fnf) {
				showErrorNotification("File not found");
				return null;
			}

		} else {
			showErrorNotification("Not allowed file format");
			return null;
		}
	}

	private String selectProperFolder(String mimeType) throws UnsupportedFileTypeException {
		switch (mimeType) {
		case JPG:
			return ResourceUtil.IMAGES + context;
		case PNG:
			return ResourceUtil.IMAGES + context;
		case OGG:
			return ResourceUtil.VIDEOS;
		case MP4:
			return ResourceUtil.VIDEOS;
		default:
			throw new UnsupportedFileTypeException();
		}
	}
}
