package io.archiveservice.service;

import io.archiveservice.ArchiveInputDTO;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;

public abstract class AbstractArchiveService {

	protected String getFileWithExtension(ArchiveInputDTO archiveInputDTO) {
		Optional<String> fileName = Optional.of(archiveInputDTO)
				.map(ArchiveInputDTO::getFileName);
		if (fileName.isPresent()) {
			if (fileName.get().endsWith("." + getArchiveExtension())) {
				return fileName.get();
			}

			return fileName.get() + "." + getArchiveExtension();
		} else {
			return RandomStringUtils.randomAlphabetic(10) + "." + getArchiveExtension();
		}
	}
	protected abstract String getArchiveExtension();
}
