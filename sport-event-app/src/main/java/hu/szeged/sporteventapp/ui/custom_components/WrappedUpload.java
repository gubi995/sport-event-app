package hu.szeged.sporteventapp.ui.custom_components;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.output.NullOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Upload;

import hu.szeged.sporteventapp.common.exception.NotAllowedFileTypeException;
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
	private static final long IMAGE_UPLOAD_LIMIT = 1000000l;
	private static final long VIDEO_UPLOAD_LIMIT = 30000000l;

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
		upload.interruptUpload();
		progressBar.setVisible(false);
		showErrorNotification("Upload failed");
	}

	@Override
	public void uploadFinished(Upload.FinishedEvent finishedEvent) {
		progressBar.setVisible(false);
	}

	@Override
	public void uploadStarted(Upload.StartedEvent startedEvent) {
		if (isImageTooBig(startedEvent) || isVideoTooBig(startedEvent)) {
			showErrorNotification("Too big file");
			upload.interruptUpload();
		}
		progressBar.setValue(0.0f);
		progressBar.setVisible(true);
	}

	private boolean isImageTooBig(Upload.StartedEvent startedEvent) {
		return (startedEvent.getMIMEType().equals(PNG) || startedEvent.getMIMEType().equals(JPG))
				&& startedEvent.getContentLength() > IMAGE_UPLOAD_LIMIT;
	}

	private boolean isVideoTooBig(Upload.StartedEvent startedEvent) {
		return (startedEvent.getMIMEType().equals(MP4) || startedEvent.getMIMEType().equals(OGG))
				&& startedEvent.getContentLength() > VIDEO_UPLOAD_LIMIT;
	}

	@Override
	public void uploadSucceeded(Upload.SucceededEvent succeededEvent) {
		showInfoNotification("Upload successful");
		progressBar.setVisible(false);
	}

	@Override
	public void updateProgress(final long readBytes, final long contentLength) {
		progressBar.setValue(readBytes / (float) contentLength);
	}

	@Override
	public OutputStream receiveUpload(final String filename, final String mimeType) {

		if (filename == null || filename.isEmpty()) {
			showWarningNotification("You must first select a file to upload.");
			return new NullOutputStream();
		}

		try {
			if (allowedMimeTypes.contains(mimeType)) {
				StringBuilder webAppPathBuilder = new StringBuilder(
						VaadinService.getCurrent().getBaseDirectory().getAbsolutePath());
				webAppPathBuilder.append("/VAADIN/themes/mytheme/");
				webAppPathBuilder.append(selectProperFolder(mimeType));
				webAppPathBuilder.append(filename);
				return new FileOutputStream(new File(webAppPathBuilder.toString()));

			} else {
				throw new NotAllowedFileTypeException();
			}
		} catch (FileNotFoundException e) {
			showErrorNotification("File not found");
		} catch (NotAllowedFileTypeException e) {
			showWarningNotification("Not allowed file format");
		} catch (UnsupportedFileTypeException e) {
			showWarningNotification("Unsupported file type");
		}
		upload.interruptUpload();
		return new NullOutputStream();
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
