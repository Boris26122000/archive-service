package io.archiveservice.service.impl;

import io.archiveservice.ArchiveDTO;
import io.archiveservice.ArchiveInputDTO;
import io.archiveservice.ArchiveResultDTO;
import io.archiveservice.service.ArchiveService;
import io.archiveservice.service.ArchiveStatisticService;
import io.archiveservice.service.validator.ArchiveInputValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArchiveServiceFacade {

	private final ArchiveService archiveService;
	private final ArchiveStatisticService archiveStatisticService;
	private final ArchiveInputValidator archiveInputValidator;

	public ArchiveResultDTO archive(ArchiveInputDTO archiveInputDTO) {
		archiveInputValidator.validateInput(archiveInputDTO);

		ArchiveResultDTO archiveResultDTO = archiveService.archive(ArchiveDTO.builder()
				.files(archiveInputDTO.getFiles())
				.fileName(archiveInputDTO.getFileName())
				.build());

		archiveStatisticService.saveArchiveStatistic(archiveInputDTO);

		return archiveResultDTO;
	}
}
