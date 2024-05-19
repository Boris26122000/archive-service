package io.archiveservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.archiveservice.ArchiveDTO;
import io.archiveservice.ArchiveInputDTO;
import io.archiveservice.ArchiveResultDTO;
import io.archiveservice.service.ArchiveService;
import io.archiveservice.service.ArchiveStatisticService;
import io.archiveservice.service.exception.ArchiveInputValidationException;
import io.archiveservice.service.validator.ArchiveInputValidator;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class ArchiveServiceFacadeTest {

	private static final String TEST_ZIP = "test.zip";
	@Mock
	private ArchiveService archiveService;
	@Mock
	private ArchiveStatisticService archiveStatisticService;
	@Mock
	private ArchiveInputValidator archiveInputValidator;
	@InjectMocks
	private ArchiveServiceFacade archiveServiceFacade;

	@Test
	void testArchive_success() throws IOException {
		MultipartFile file1 = createMultipartFileFromResource("test/file1.txt");
		MultipartFile file2 = createMultipartFileFromResource("test/file2.txt");
		MultipartFile[] files = new MultipartFile[]{file1, file2};

		ArchiveInputDTO archiveInputDTO = ArchiveInputDTO.builder()
				.files(files)
				.fileName(TEST_ZIP).build();

		ArchiveResultDTO archiveResultDTO = ArchiveResultDTO.builder()
				.fileName(TEST_ZIP)
				.resource(null)
				.build();

		doNothing().when(archiveInputValidator).validateInput(any(ArchiveInputDTO.class));
		when(archiveService.archive(any(ArchiveDTO.class))).thenReturn(archiveResultDTO);
		doNothing().when(archiveStatisticService).saveArchiveStatistic(any(ArchiveInputDTO.class));

		ArchiveResultDTO result = archiveServiceFacade.archive(archiveInputDTO);

		assertNotNull(result);
		verify(archiveInputValidator, times(1)).validateInput(archiveInputDTO);
		verify(archiveService, times(1)).archive(any(ArchiveDTO.class));
		verify(archiveStatisticService, times(1)).saveArchiveStatistic(archiveInputDTO);
	}

	@Test
	void testArchive_validationFails() throws IOException {
		MultipartFile file1 = createMultipartFileFromResource("test/file1.txt");
		MultipartFile file2 = createMultipartFileFromResource("test/file2.txt");
		MultipartFile[] files = new MultipartFile[]{file1, file2};

		ArchiveInputDTO archiveInputDTO = ArchiveInputDTO.builder()
				.files(files)
				.fileName(TEST_ZIP).build();

		doThrow(new ArchiveInputValidationException("Validation failed"))
				.when(archiveInputValidator).validateInput(any(ArchiveInputDTO.class));

		try {
			archiveServiceFacade.archive(archiveInputDTO);
		} catch (ArchiveInputValidationException e) {
			verify(archiveInputValidator, times(1)).validateInput(archiveInputDTO);
			verify(archiveService, never()).archive(any(ArchiveDTO.class));
			verify(archiveStatisticService, never()).saveArchiveStatistic(any(ArchiveInputDTO.class));
		}
	}

	private MultipartFile createMultipartFileFromResource(String resourcePath) throws IOException {
		ClassPathResource resource = new ClassPathResource(resourcePath);
		return new MockMultipartFile(resourcePath, resource.getFilename(), null, Files.readAllBytes(resource.getFile().toPath()));
	}
}