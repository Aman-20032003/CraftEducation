 package com.craft.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
	private String houseNumber;
	private String city;
	private int pinCode;
	private String State;
	private String country;
}
