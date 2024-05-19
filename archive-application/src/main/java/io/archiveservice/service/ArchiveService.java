package io.archiveservice.service;

import io.archiveservice.ArchiveDTO;
import io.archiveservice.ArchiveResultDTO;

public interface ArchiveService {

	ArchiveResultDTO archive(ArchiveDTO archiveInputDTO);
}
