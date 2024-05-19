package io.archiveservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ArchiveType {
	ZIP("zip");

	private final String name;
}
