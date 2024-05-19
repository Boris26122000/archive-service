package io.archiveservice.service.impl;

import io.archiveservice.ArchiveDTO;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;

public abstract class AbstractArchiveService {

	protected String getFileWithExtension(ArchiveDTO archiveInputDTO) {
		Optional<String> fileName = Optional.of(archiveInputDTO)
				.map(ArchiveDTO::getFileName);
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
