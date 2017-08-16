package hu.szeged.sporteventapp.common;

import java.io.File;
import java.io.OutputStream;

import com.vaadin.server.FileResource;
import com.vaadin.ui.Image;
import com.vaadin.ui.Upload;

class ImageUploader implements Upload.Receiver, Upload.SucceededListener {
	public File file;
	private Image image;
	private int counter;
	private int total;
	private boolean sleep;

	public ImageUploader(File file, Image image) {
		this.file = file;
		this.image = image;
	}

	public OutputStream receiveUpload(String filename, String mimeType) {
		return new OutputStream() {
			private static final int searchedByte = '\n';

			@Override
			public void write(final int b) {
				total++;
				if (b == searchedByte) {
					counter++;
				}
				if (sleep && total % 1000 == 0) {
					try {
						Thread.sleep(100);
					}
					catch (final InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
	}

	public void uploadSucceeded(Upload.SucceededEvent event) {
		image.setSource(new FileResource(file));
	}
};