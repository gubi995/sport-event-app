package hu.szeged.sporteventapp.ui.custom_components.exporter;

import static hu.szeged.sporteventapp.ui.constants.ViewConstants.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import hu.szeged.sporteventapp.common.converter.LocalDateTimeConverter;

public class CSVExporter extends Button implements StreamResource.StreamSource {

	private CSVFileBuilder fileBuilder;
	private FileDownloader fileDownloader;
	private static final String SEPARATOR = " | ";

	public CSVExporter() {
		fileDownloader = new FileDownloader(new StreamResource(this, getDownloadFileName()));
		fileDownloader.extend(this);
	}

	public void exportSportEvent(SportEvent sportEvent) {
		List<String> content = createContent(sportEvent);
		fileBuilder = new CSVFileBuilder(content);
	}

	private List<String> createContent(SportEvent sportEvent) {
		List<String> content = Lists.newArrayList();
		content.addAll(createMetaData(sportEvent));
		content.add("");
		content.addAll(createParticipantContent(sportEvent));
		return content;
	}

	private String getDownloadFileName() {
		return "exported-sport-event.csv";
	}

	private List<String> createParticipantContent(SportEvent sportEvent) {
		List<String> content = Lists.newArrayList();
		content.add(PARTICIPANTS); // caption
		content.add(USERNAME + SEPARATOR + REAL_NAME);
		List<String> participants = sportEvent.getParticipants().stream()
				.map(user -> user.getUsername() + SEPARATOR + user.getRealName()).collect(Collectors.toList());
		content.addAll(participants);
		return content;
	}

	private List<String> createMetaData(SportEvent sportEvent) {
		LocalDateTimeConverter timeConverter = new LocalDateTimeConverter();
		List<String> content = Lists.newArrayList();
		content.add(SPORT_EVENT); // caption
		content.add(NAME + SEPARATOR + sportEvent.getName());
		content.add(LOCATION + SEPARATOR + sportEvent.getLocation());
		content.add(MAX_PARTICIPANT + SEPARATOR + String.valueOf(sportEvent.getMaxParticipant()));
		content.add(START_DATE + SEPARATOR + timeConverter.convertLocalDateTimeToString(sportEvent.getStartDate()));
		content.add(END_DATE + SEPARATOR + timeConverter.convertLocalDateTimeToString(sportEvent.getEndDate()));
		content.add(ORGANIZER + SEPARATOR + sportEvent.getOrganizer().getUsername());
		content.add(SPORT_TYPE + SEPARATOR + sportEvent.getSportType());
		return content;
	}

	@Override
	public InputStream getStream() {
		try {
			return new FileInputStream(fileBuilder.getFile());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}