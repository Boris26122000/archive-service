package io.archiveservice.service.validator;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import io.archiveservice.ArchiveInputDTO;
import io.archiveservice.service.exception.ArchiveInputValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class ArchiveInputValidatorTest {

	@Mock
	private ArchiveInputDTO archiveInputDTO;
	@Mock
	private MultipartFile file1;
	@InjectMocks
	private ArchiveInputValidator archiveInputValidator;

	@Value("${archive.zip.max-upload-size}")
	private long zipMaxFileUploadSize;

	@Test
	public void testValidateInput_NullInput() {
		assertThrows(ArchiveInputValidationException.class, () -> archiveInputValidator.validateInput(null), "Input can not be empty to create archive");
	}

	@Test
	public void testValidateInput_EmptyFiles() {
		when(archiveInputDTO.getFiles()).thenReturn(new MultipartFile[]{});
		assertThrows(ArchiveInputValidationException.class, () -> archiveInputValidator.validateInput(archiveInputDTO), "Input can not be empty to create archive");
	}

	@Test
	public void testValidateInput_ExceedsMaxSize() {
		when(archiveInputDTO.getFiles()).thenReturn(new MultipartFile[]{file1});
		when(file1.getOriginalFilename()).thenReturn("file.txt");
		when(file1.getSize()).thenReturn(1048577L);

		assertThrows(ArchiveInputValidationException.class, () -> archiveInputValidator.validateInput(archiveInputDTO), "Max upload size is " + zipMaxFileUploadSize + " bytes, file: file.txt can not be archived");
	}

	@Test
	public void testValidateInput_EmptyFile() {
		when(archiveInputDTO.getFiles()).thenReturn(new MultipartFile[]{file1});
		when(file1.getOriginalFilename()).thenReturn("file.txt");
		when(file1.getSize()).thenReturn(0L);

		assertThrows(ArchiveInputValidationException.class, () -> archiveInputValidator.validateInput(archiveInputDTO), "Input file(s) can not be empty");
	}
}
