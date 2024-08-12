package com.craft.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.craft.repository.entity.TeachersAddress;

public interface AddressRepository extends JpaRepository<TeachersAddress, Integer> {

}
