package com.br.melo.exception;

public class RestricaoException extends Exception {

	private static final long serialVersionUID = 1L;
	private String msg;

	public RestricaoException(String msg) {
		setMsg(msg);
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
