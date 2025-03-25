package com.linkmoa.source.domain.directory.exception;

import com.linkmoa.source.domain.directory.error.DirectoryErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DirectoryException extends RuntimeException {
	private final DirectoryErrorCode directoryErrorCode;
}
