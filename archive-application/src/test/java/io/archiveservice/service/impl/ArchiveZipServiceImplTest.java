package io.archiveservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.archiveservice.ArchiveDTO;
import io.archiveservice.ArchiveResultDTO;
import io.archiveservice.service.exception.ArchiveCreateException;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class ArchiveZipServiceImplTest {

	private static final String ARCHIVE_ZIP = "archive.zip";
	@InjectMocks
	private ArchiveZipServiceImpl archiveZipService;

	@Test
	void testArchive_success() throws IOException {
		MultipartFile file1 = createMultipartFileFromResource("test/file1.txt");
		MultipartFile file2 = createMultipartFileFromResource("test/file2.txt");
		MultipartFile file3 = createMultipartFileFromResource("test/screenshot.png");

		MultipartFile[] files = new MultipartFile[]{file1, file2, file3};
		ArchiveDTO archiveDTO = ArchiveDTO.builder()
				.files(files)
				.fileName(ARCHIVE_ZIP)
				.build();

		ArchiveResultDTO result = archiveZipService.archive(archiveDTO);

		assertNotNull(result);
		assertNotNull(result.getResource());
		assertEquals(ARCHIVE_ZIP, result.getFileName());
	}

	@Test
	void testArchive_failure() throws IOException {
		MultipartFile file1 = createMultipartFileFromResource("test/file1.txt");
		MultipartFile file2 = createFaultyMultipartFile();

		MultipartFile[] files = new MultipartFile[]{file1, file2};

		ArchiveDTO archiveDTO = ArchiveDTO.builder().files(files).build();

		assertThrows(ArchiveCreateException.class, () -> archiveZipService.archive(archiveDTO));
	}

	private MultipartFile createMultipartFileFromResource(String resourcePath) throws IOException {
		ClassPathResource resource = new ClassPathResource(resourcePath);
		return new MockMultipartFile(resourcePath, resource.getFilename(), null, Files.readAllBytes(resource.getFile().toPath()));
	}

	private MultipartFile createFaultyMultipartFile() {
		return new MockMultipartFile("faultyFile.txt", "faultyFile.txt", null, new byte[0]) {
			@Override
			public byte[] getBytes() throws IOException {
				throw new IOException("Test Exception");
			}
		};
	}
}