package com.craft.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.craft.controller.request.GlobalAddressRequest;
import com.craft.repository.entity.Address;
@Service
public class DtoToEntityAddressConverterService {

	public Address convertToEntity(GlobalAddressRequest addressDto) {
		Address address = new Address();
		address.setHouseNumber(addressDto.getHouseNumber());
		address.setCity(addressDto.getCity());
		address.setPinCode(addressDto.getPinCode());
		address.setState(addressDto.getState());
		address.setCountry(addressDto.getCountry());
		return address;
	}
	public List<Address> convertAddressListToEntity(List<GlobalAddressRequest> addressDtoList){
		return addressDtoList.stream().map(this::convertToEntity).collect(Collectors.toList());
	}
	
}
