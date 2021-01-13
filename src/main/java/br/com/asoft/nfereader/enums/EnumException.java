package br.com.asoft.nfereader.enums;

import org.springframework.http.HttpStatus;

public enum EnumException {
	MAX_SIZE("", HttpStatus.OK);

	private String description;
	private HttpStatus httpStatus;

	EnumException(String description, HttpStatus status) {
		this.description = description;
		this.httpStatus = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

}
