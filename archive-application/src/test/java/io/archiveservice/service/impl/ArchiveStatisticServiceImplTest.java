package io.archiveservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.archiveservice.ArchiveInputDTO;
import io.archiveservice.entity.ArchiveStatistic;
import io.archiveservice.repository.ArchiveStatisticRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ArchiveStatisticServiceImplTest {

	private static final String IP_ADDRESS = "192.168.1.1";

	@Mock
	private ArchiveStatisticRepository archiveStatisticRepository;
	@InjectMocks
	private ArchiveStatisticServiceImpl archiveStatisticService;

	@Test
	void testSaveArchiveStatistic_withNewStatistic() {
		ArchiveInputDTO archiveInputDTO = ArchiveInputDTO.builder().ipAddress(IP_ADDRESS).build();

		when(archiveStatisticRepository.findByIpAddressAndUsageDate(IP_ADDRESS, LocalDate.now()))
				.thenReturn(Optional.empty());

		archiveStatisticService.saveArchiveStatistic(archiveInputDTO);

		verify(archiveStatisticRepository, times(1)).save(any(ArchiveStatistic.class));
	}

	@Test
	void testSaveArchiveStatistic_withExistingStatistic() {
		ArchiveInputDTO archiveInputDTO = ArchiveInputDTO.builder().ipAddress(IP_ADDRESS).build();

		ArchiveStatistic existingStatistic = ArchiveStatistic.builder()
				.ipAddress(IP_ADDRESS)
				.usageDate(LocalDate.now())
				.archiveUsageCount(1)
				.build();

		when(archiveStatisticRepository.findByIpAddressAndUsageDate(IP_ADDRESS, LocalDate.now()))
				.thenReturn(Optional.of(existingStatistic));

		archiveStatisticService.saveArchiveStatistic(archiveInputDTO);

		assertEquals(2, existingStatistic.getArchiveUsageCount());
		verify(archiveStatisticRepository, times(1)).save(existingStatistic);
	}

	@Test
	void testSaveArchiveStatistic_withEmptyIpAddress() {
		ArchiveInputDTO archiveInputDTO = ArchiveInputDTO.builder().build();

		archiveStatisticService.saveArchiveStatistic(archiveInputDTO);

		verify(archiveStatisticRepository, never()).findByIpAddressAndUsageDate(anyString(), any(LocalDate.class));
		verify(archiveStatisticRepository, never()).save(any(ArchiveStatistic.class));
	}
}