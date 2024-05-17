package io.archiveservice.controller;

import java.util.zip.ZipFile;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("archive")
@RequiredArgsConstructor
public class FileArchiveController {

	@PostMapping(produces = "application/zip")
	private ZipFile archiveFiles(@RequestParam("files") MultipartFile[] files) {
		return null;
	}
}
