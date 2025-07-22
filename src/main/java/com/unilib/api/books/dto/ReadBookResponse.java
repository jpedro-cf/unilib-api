package com.unilib.api.books.dto;

import com.unilib.api.books.Book;

public record ReadBookResponse(String url, Book book) {
}
