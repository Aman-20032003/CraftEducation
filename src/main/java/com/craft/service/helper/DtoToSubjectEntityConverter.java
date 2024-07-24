package com.craft.service.helper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.craft.controller.request.AddSubjectRequest;
import com.craft.repository.entity.Subject;

@Service
public class DtoToSubjectEntityConverter {

	 public Subject convertToEntity(AddSubjectRequest subjectRequest) {
		  Subject subject = Subject.builder().subjectName(subjectRequest.getSubjectName()).about(subjectRequest.getAbout()).build();
		return subject;
	  }
	  
	  public List<Subject> convertStremOfSubjectToEntity(List<AddSubjectRequest> subjects ){
		  return subjects.stream().map(this::convertToEntity).collect(Collectors.toList());
	  }
	
}
