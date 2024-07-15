package com.craft.controller.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class AdminResponse implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message ;
	private boolean success;

}
