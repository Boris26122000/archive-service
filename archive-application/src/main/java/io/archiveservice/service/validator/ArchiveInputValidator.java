package io.archiveservice.service.validator;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

import io.archiveservice.ArchiveDTO;
import io.archiveservice.ArchiveInputDTO;
import io.archiveservice.service.exception.ArchiveInputValidationException;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ArchiveInputValidator {

	@Value("${archive.zip.max-upload-size:1048576}")
	private long zipMaxFileUploadSize;

	public void validateInput(ArchiveInputDTO archiveInputDTO) {
		if (isNull(archiveInputDTO) || isEmpty(archiveInputDTO.getFiles())) {
			throw new ArchiveInputValidationException("Input can not be empty to create archive");
		}

		Set<String> fileNames = new HashSet<>();
		for (MultipartFile file : archiveInputDTO.getFiles()) {
			if (isNull(file.getOriginalFilename()) || fileNames.contains(file.getOriginalFilename())) {
				throw new ArchiveInputValidationException(String.format("Impossible to create archive with duplicated file names: %s", file.getOriginalFilename()));
			}
			fileNames.add(file.getOriginalFilename());

			if (file.getSize() > zipMaxFileUploadSize) {
				throw new ArchiveInputValidationException(String.format("Max upload size is %s bytes, file: %s can not be archived", zipMaxFileUploadSize, file.getOriginalFilename()));
			}
			if (file.getSize() == 0) {
				throw new ArchiveInputValidationException("Input file(s) can not be empty");
			}
		}
	}

}
