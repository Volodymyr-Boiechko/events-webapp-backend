package com.boiechko.eventswebapp.util;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.util.Objects;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JacksonUtils {

  public static final ObjectMapper OBJECT_MAPPER;

  static {
    OBJECT_MAPPER =
        new ObjectMapper()
            .registerModule(
                new SimpleModule()
                    .addDeserializer(boolean.class, new BooleanDeserializer())
                    .addDeserializer(Boolean.class, new BooleanDeserializer())
                    .addSerializer(Boolean.class, new BooleanSerializer()))
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public static <T> T deserialize(final String json, final Class<T> objectClass) {
    if (Objects.isNull(json)) {
      return null;
    }
    try {
      return OBJECT_MAPPER.readValue(json, objectClass);
    } catch (final IOException e) {
      throw new RuntimeException("Cannot deserialize: " + json, e);
    }
  }

  public static <T> T deserialize(final String json, final TypeReference<T> type) {
    if (Objects.isNull(json)) {
      return null;
    }
    try {
      return OBJECT_MAPPER.readValue(json, type);
    } catch (final IOException e) {
      throw new RuntimeException("Cannot deserialize: " + json, e);
    }
  }

  public static String serialize(final Object object) {
    if (Objects.isNull(object)) {
      return null;
    }
    try {
      return OBJECT_MAPPER.writeValueAsString(object);
    } catch (final IOException e) {
      throw new RuntimeException("Cannot serialize: " + object.toString(), e);
    }
  }

  public static class BooleanDeserializer extends JsonDeserializer<Boolean> {

    @Override
    public Boolean deserialize(
        final JsonParser jsonParser, final DeserializationContext deserializationContext)
        throws IOException, JacksonException {
      return jsonParser.getBooleanValue();
    }

    @Override
    public Boolean getNullValue(final DeserializationContext deserializationContext) {
      return Boolean.FALSE;
    }
  }

  public static class BooleanSerializer extends JsonSerializer<Boolean> {

    @Override
    public void serialize(
        final Boolean value, final JsonGenerator generator, final SerializerProvider provider)
        throws IOException {
      generator.writeBoolean(Objects.isNull(value) ? Boolean.FALSE : value);
    }
  }
}
