package com.github.MathCunha16.integrationtests.controllers.withyaml.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;

public class YAMLMapper implements ObjectMapper { // Implementa a interface do RestAssured

	private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper;
	protected TypeFactory typeFactory;
	
	public YAMLMapper() {
		this.jacksonObjectMapper = new com.fasterxml.jackson.databind.ObjectMapper(new YAMLFactory());
		this.jacksonObjectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		this.typeFactory = TypeFactory.defaultInstance();
	}

	@Override
	public Object deserialize(ObjectMapperDeserializationContext context) {
		try {
			String dataToDeserialize = context.getDataToDeserialize().asString();
			Class<?> type = (Class<?>) context.getType();
			
			return jacksonObjectMapper.readValue(dataToDeserialize, typeFactory.constructType(type));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object serialize(ObjectMapperSerializationContext context) {
		try {
			return jacksonObjectMapper.writeValueAsString(context.getObjectToSerialize());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
}