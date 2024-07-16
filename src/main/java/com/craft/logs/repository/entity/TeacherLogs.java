package com.craft.logs.repository.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.experimental.SuperBuilder;

@Document(collection = "teacherlogs")
@SuperBuilder
public class TeacherLogs extends LogFields {
}
