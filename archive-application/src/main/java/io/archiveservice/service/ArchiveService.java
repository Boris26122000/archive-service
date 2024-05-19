package io.archiveservice.service;

import io.archiveservice.ArchiveInputDTO;
import io.archiveservice.ArchivedResourceDTO;

public interface ArchiveService {

	ArchivedResourceDTO archive(ArchiveInputDTO archiveInputDTO);
}
