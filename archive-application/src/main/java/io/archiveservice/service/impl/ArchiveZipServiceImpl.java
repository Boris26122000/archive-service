package io.archiveservice.service.impl;

import static io.archiveservice.enums.ArchiveType.ZIP;

import io.archiveservice.ArchiveDTO;
import io.archiveservice.ArchiveResultDTO;
import io.archiveservice.service.ArchiveService;
import io.archiveservice.service.exception.ArchiveCreateException;
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

	@Override
	public ArchiveResultDTO archive(ArchiveDTO archiveInputDTO) {
		ByteArrayResource resource = getArchivedResource(archiveInputDTO);
		return ArchiveResultDTO.builder()
				.resource(resource)
				.fileName(getFileWithExtension(archiveInputDTO))
				.build();
	}

	private static ByteArrayResource getArchivedResource(ArchiveDTO archiveInputDTO) {
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
