package com.egs.account.exception;

/**
 * @author Hayk_Mkhitaryan
 */
public class EmailExistsException extends Exception {

	private static final long serialVersionUID = 3080164609056606991L;

	private String message;

	public EmailExistsException(String s) {
		this.message = s;
	}
}
