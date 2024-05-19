package io.archiveservice;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class ArchiveDTO {

	private MultipartFile[] files;
	private String fileName;

}
