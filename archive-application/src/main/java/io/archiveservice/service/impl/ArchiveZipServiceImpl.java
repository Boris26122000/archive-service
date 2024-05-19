package io.archiveservice.service.impl;

import static io.archiveservice.enums.ArchiveType.ZIP;

import io.archiveservice.ArchiveInputDTO;
import io.archiveservice.ArchivedResourceDTO;
import io.archiveservice.service.AbstractArchiveService;
import io.archiveservice.service.ArchiveService;
import io.archiveservice.service.exception.ArchiveCreateException;
import io.archiveservice.service.validator.ArchiveInputValidator;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArchiveZipServiceImpl extends AbstractArchiveService implements ArchiveService {

	private final ArchiveInputValidator archiveInputValidator;

	@Override
	public ArchivedResourceDTO archive(ArchiveInputDTO archiveInputDTO) {
		archiveInputValidator.validateInput(archiveInputDTO);

		ByteArrayResource resource = getArchivedResource(archiveInputDTO);
		return ArchivedResourceDTO.builder()
				.resource(resource)
				.fileName(getFileWithExtension(archiveInputDTO))
				.build();
	}

	private static ByteArrayResource getArchivedResource(ArchiveInputDTO archiveInputDTO) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ZipOutputStream zos = new ZipOutputStream(baos)) {

			for (int i = 0; i < archiveInputDTO.getFiles().length; i++) {
				MultipartFile file = archiveInputDTO.getFiles()[i];
				ZipEntry zipEntry = new ZipEntry(file.getOriginalFilename());
				zos.putNextEntry(zipEntry);
				zos.write(file.getBytes());
				zos.closeEntry();
			}
			zos.finish();

			return new ByteArrayResource(baos.toByteArray());
		} catch (IOException e) {
			log.error("An error occurred while creating archive", e);
			throw new ArchiveCreateException("An error occurred while creating archive");
		}
	}

	@Override
	protected String getArchiveExtension() {
		return ZIP.getName();
	}
}
