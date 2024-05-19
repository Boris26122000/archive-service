package io.archiveservice.api.controller;

import io.archiveservice.ArchiveInputDTO;
import io.archiveservice.ArchivedResourceDTO;
import io.archiveservice.service.ArchiveService;
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

	private final ArchiveService archiveService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "application/zip")
	private ResponseEntity<Resource> archiveFiles(
			@RequestPart MultipartFile[] files,
			@RequestPart(required = false) String fileName) {

		ArchivedResourceDTO archivedResourceDTO = archiveService.archive(ArchiveInputDTO.builder()
				.files(files)
				.fileName(fileName)
				.build());

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", archivedResourceDTO.getFileName()));
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

		return ResponseEntity.ok()
				.headers(headers)
				.contentLength(archivedResourceDTO.getResource().contentLength())
				.body(archivedResourceDTO.getResource());
	}
}
