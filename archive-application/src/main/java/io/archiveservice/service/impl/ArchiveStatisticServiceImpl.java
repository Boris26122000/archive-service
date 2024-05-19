package io.archiveservice.service.impl;

import io.archiveservice.ArchiveInputDTO;
import io.archiveservice.entity.ArchiveStatistic;
import io.archiveservice.repository.ArchiveStatisticRepository;
import io.archiveservice.service.ArchiveStatisticService;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArchiveStatisticServiceImpl implements ArchiveStatisticService {

	private final ArchiveStatisticRepository archiveStatisticRepository;

	@Override
	public void saveArchiveStatistic(ArchiveInputDTO archiveInputDTO) {
		Optional<String> ipAddress = Optional.of(archiveInputDTO)
				.map(ArchiveInputDTO::getIpAddress);
		if (ipAddress.isEmpty()) {
			log.warn("IP address is empty for, skipping statistic");
			return;
		}

		Optional<ArchiveStatistic> existingArchiveStatistic = archiveStatisticRepository.findByIpAddressAndUsageDate(ipAddress.get(), LocalDate.now());
		if (existingArchiveStatistic.isPresent()) {
			existingArchiveStatistic.get().setArchiveUsageCount(existingArchiveStatistic.get().getArchiveUsageCount() + 1);
			archiveStatisticRepository.save(existingArchiveStatistic.get());
		} else {
			archiveStatisticRepository.save(ArchiveStatistic.builder()
					.ipAddress(ipAddress.get())
					.usageDate(LocalDate.now())
					.archiveUsageCount(1)
					.build());
		}
	}
}
