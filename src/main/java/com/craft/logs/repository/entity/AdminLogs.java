package com.craft.logs.repository.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.experimental.SuperBuilder;

@Document(collection = "adminlogs")
@SuperBuilder
public class AdminLogs extends LogFields {

}
