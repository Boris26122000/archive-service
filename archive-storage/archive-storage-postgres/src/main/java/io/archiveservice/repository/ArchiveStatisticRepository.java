package io.archiveservice.repository;

import io.archiveservice.entity.ArchiveStatistic;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchiveStatisticRepository extends CrudRepository<ArchiveStatistic, Integer> {

	Optional<ArchiveStatistic> findByIpAddressAndUsageDate(String ipAddress, LocalDate localDate);
}
