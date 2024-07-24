package com.craft.service.helper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.craft.controller.request.TeachersSubjectRequest;
import com.craft.repository.entity.TeachersSubject;
@Service
public class DtoToTeachersSubjectEntityConverter {
  public TeachersSubject convertToEntity(TeachersSubjectRequest subjectRequest) {
	  TeachersSubject teachersSubject = TeachersSubject.builder().subjectName(subjectRequest.getSubjectName()).build();
	return teachersSubject;
  }
  
  public List<TeachersSubject> convertStremOfTeachersSubjectListToEntity(List<TeachersSubjectRequest> teachersSubjects ){
	  return teachersSubjects.stream().map(this::convertToEntity).collect(Collectors.toList());
  }
}
