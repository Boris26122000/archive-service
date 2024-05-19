package io.archiveservice;

import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.ByteArrayResource;

@Data
@Builder
public class ArchiveResultDTO {

	private String fileName;
	private ByteArrayResource resource;

}
