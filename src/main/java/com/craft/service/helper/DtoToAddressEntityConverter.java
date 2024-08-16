package com.craft.service.helper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.craft.controller.request.AddressRequest;
import com.craft.repository.entity.StudentAdddress;
import com.craft.repository.entity.TeachersAddress;
@Service
public class DtoToAddressEntityConverter {

	public TeachersAddress convertToEntity(AddressRequest addressDto) {
		TeachersAddress address = TeachersAddress.builder().houseNumber(addressDto.getHouseNumber())
				.city(addressDto.getCity())
				.pinCode(addressDto.getPinCode())
				.State(addressDto.getState())
				.country(addressDto.getCountry())
				.build();
		return address;
	}
	public List<TeachersAddress> convertAddressListToEntity(List<AddressRequest> addressDtoList){
		return addressDtoList.stream().map(this::convertToEntity).collect(Collectors.toList());
	}
	
	
	public StudentAdddress convertToStudentEntity(AddressRequest addressDto) {
		StudentAdddress address = StudentAdddress.builder().houseNumber(addressDto.getHouseNumber())
				.city(addressDto.getCity())
				.pinCode(addressDto.getPinCode())
				.State(addressDto.getState())
				.country(addressDto.getCountry())
				.build();
		return address;
	}
	public List<StudentAdddress> convertStudentAddressListToEntity(List<AddressRequest> addressDtoList){
		return addressDtoList.stream().map(this::convertToStudentEntity).collect(Collectors.toList());
	}
}
