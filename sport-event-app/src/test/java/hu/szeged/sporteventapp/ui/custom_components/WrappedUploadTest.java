package hu.szeged.sporteventapp.ui.custom_components;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WrappedUploadTest {

	WrappedUpload upload;

	@Before
	public void setUp() throws Exception {
		upload = new WrappedUpload();
	}

	@Test
	public void receiveUploadCallWithExpectedData() throws Exception {

	}

	@Test
	public void receiveUploadCallWithNullFileName() throws Exception {

	}

	@Test
	public void receiveUploadCallWithNotAllowedFileType() throws Exception {

	}

	@Test
	public void receiveUploadCallWithUnsupportedFileType() throws Exception {

	}
}