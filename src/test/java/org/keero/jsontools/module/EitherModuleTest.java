package org.keero.jsontools.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Either;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EitherModuleTest {
  record EitherTestModel(Either<Integer, String> field) {}

  ObjectMapper mapper;

  @BeforeEach
  void setup() {
    EitherModule module = new EitherModule();
    mapper = new ObjectMapper(new JsonFactory()).registerModule(module);
  }

  @Test
  void apa() throws JsonMappingException, JsonProcessingException {
    EitherTestModel value =
        new ObjectMapper(new JsonFactory())
            .registerModule(new EitherModule())
            .readValue("{\"field\": \"some string\"}", EitherTestModel.class);

    String json =
        new ObjectMapper(new JsonFactory())
            .registerModule(new EitherModule())
            .writeValueAsString(new EitherTestModel(Either.left(1234)));
    System.out.println(value);
    System.out.println(json);
  }

  @Test
  void testWillDeserializeEitherLeft() throws JsonProcessingException {
    EitherTestModel value = mapper.readValue("{\"field\": 1234}", EitherTestModel.class);
    assertTrue(value.field.isLeft());
    assertEquals(1234, value.field.getLeft());
  }

  @Test
  void testWillDeserializeEitherRight() throws JsonProcessingException {
    EitherTestModel value = mapper.readValue("{\"field\": \"apabepa\"}", EitherTestModel.class);
    assertTrue(value.field.isRight());
    assertEquals("apabepa", value.field.get());
  }

  @Test
  void testWillThrowOnWrongType() throws JsonMappingException, JsonProcessingException {
    assertThrows(
        JsonMappingException.class,
        () -> mapper.readValue("{\"eitherField\": [\"apabepa\"]}", EitherTestModel.class));
  }

  @Test
  void testWillSerializeEitherLeft() throws JsonProcessingException {
    EitherTestModel obj = new EitherTestModel(Either.left(1234));
    String json = mapper.writeValueAsString(obj);
    assertEquals("{\"field\":1234}", json);
  }

  @Test
  void testWillSerializeEitherRight() throws JsonProcessingException {
    EitherTestModel obj = new EitherTestModel(Either.right("apabepa"));
    String json = mapper.writeValueAsString(obj);
    assertEquals("{\"field\":\"apabepa\"}", json);
  }

  @Test
  void testWillSerializeToNullWhenNotSet() throws JsonProcessingException {
    EitherTestModel obj = new EitherTestModel(null);
    String json = mapper.writeValueAsString(obj);
    assertEquals("{\"field\":null}", json);
  }
}
