package com.craft.logs.repository.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.experimental.SuperBuilder;

@Document(collection ="studentlogs")
@SuperBuilder
public class StudentLogs extends LogFields {

}
