package io.archiveservice.api.controller;

import io.archiveservice.ArchiveInputDTO;
import io.archiveservice.ArchiveResultDTO;
import io.archiveservice.service.detector.ClientIpDetector;
import io.archiveservice.service.impl.ArchiveServiceFacade;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("archive")
@RequiredArgsConstructor
public class FileArchiveController {

	private final ArchiveServiceFacade archiveServiceFacade;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "application/zip")
	private ResponseEntity<Resource> archiveFiles(HttpServletRequest request,
			@RequestPart MultipartFile[] files,
			@RequestPart(required = false) String fileName) {

		ArchiveInputDTO.ArchiveInputDTOBuilder archiveInputDTO = ArchiveInputDTO.builder()
				.files(files)
				.fileName(fileName);
		ClientIpDetector.getClientIp(request)
				.ifPresent(archiveInputDTO::ipAddress);
		ArchiveResultDTO archiveResultDTO = archiveServiceFacade.archive(archiveInputDTO.build());

		return ResponseEntity.ok()
				.headers(getHttpHeaders(archiveResultDTO))
				.contentLength(archiveResultDTO.getResource().contentLength())
				.body(archiveResultDTO.getResource());
	}

	private static HttpHeaders getHttpHeaders(ArchiveResultDTO archiveResultDTO) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", archiveResultDTO.getFileName()));
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return headers;
	}
}
