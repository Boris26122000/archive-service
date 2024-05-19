package io.archiveservice;

import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.ByteArrayResource;

@Data
@Builder
public class ArchivedResourceDTO {

	private String fileName;
	private ByteArrayResource resource;

}
