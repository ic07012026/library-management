package com.example.library;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookJsonSerializerTest {
    private final BookJsonSerializer serializer = new BookJsonSerializer();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void serializesBookPropertiesAsJson() throws Exception {
        Book book = new Book("978-0134685991", "Effective Java", "Joshua Bloch", 2018);

        JsonNode json = objectMapper.readTree(serializer.serialize(book));

        assertEquals("978-0134685991", json.get("isbn").asText());
        assertEquals("Effective Java", json.get("title").asText());
        assertEquals("Joshua Bloch", json.get("author").asText());
        assertEquals(2018, json.get("publicationYear").asInt());
        assertFalse(json.get("borrowed").asBoolean());
        assertEquals(0, json.get("borrowCount").asInt());
    }

    @Test
    void serializesCurrentBorrowStatusAndBorrowCount() throws Exception {
        Book book = new Book("978-0134685991", "Effective Java", "Joshua Bloch", 2018);
        book.borrow();

        JsonNode json = objectMapper.readTree(serializer.serialize(book));

        assertTrue(json.get("borrowed").asBoolean());
        assertEquals(1, json.get("borrowCount").asInt());
    }

    @Test
    void serializesBookListsAsJsonArrays() throws Exception {
        Book first = new Book("978-0134685991", "Effective Java", "Joshua Bloch", 2018);
        Book second = new Book("978-0596009205", "Head First Java", "Kathy Sierra", 2005);

        JsonNode json = objectMapper.readTree(serializer.serializeAll(List.of(first, second)));

        assertTrue(json.isArray());
        assertEquals(2, json.size());
        assertEquals("Effective Java", json.get(0).get("title").asText());
        assertEquals("Head First Java", json.get(1).get("title").asText());
    }
}
