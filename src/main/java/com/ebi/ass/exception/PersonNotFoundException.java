package com.ebi.ass.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author MohamedRamadan
 *
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PersonNotFoundException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1039131913504202707L;

	public PersonNotFoundException(String message) {
		super(message);
	}
}
