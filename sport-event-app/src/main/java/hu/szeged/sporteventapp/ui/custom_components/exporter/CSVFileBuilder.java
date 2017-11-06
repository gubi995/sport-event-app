package hu.szeged.sporteventapp.ui.custom_components.exporter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.google.common.io.Files;

public class CSVFileBuilder implements Serializable {

	private File file;
	private List<String> content;

	public CSVFileBuilder(List<String> content) {
		this.content = content;
	}

	public File getFile() {
		try {
			initTempFile();
			writeToFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	private void initTempFile() throws IOException {
		if (file != null) {
			file.delete();
		}
		file = createTempFile();
	}

	protected void writeToFile() {
		try (BufferedWriter writer = Files.newWriter(file, StandardCharsets.UTF_8)) {
			for (String line : content) {
				writer.write(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private File createTempFile() throws IOException {
		return File.createTempFile(getFileName(), getFileExtension());
	}

	private String getFileName() {
		return "tmp";
	}

	protected String getFileExtension() {
		return ".csv";
	}
}