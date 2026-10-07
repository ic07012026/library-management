package com.example.library;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;

public final class BookJsonSerializer {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String serialize(Book book) throws JsonProcessingException {
        return objectMapper.writeValueAsString(
            Objects.requireNonNull(book, "Le livre ne peut pas être null."));
    }

    public String serializeAll(List<Book> books) throws JsonProcessingException {
        return objectMapper.writeValueAsString(
            Objects.requireNonNull(books, "La liste ne peut pas être null."));
    }
}
