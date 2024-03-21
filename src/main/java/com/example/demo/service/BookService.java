/**
 * @author  Ruvini Ramawickrama
 */
package com.example.demo.service;

import com.example.demo.model.Book;

import java.util.List;

public interface BookService {

    public List<Book> getBooks();

    public Book createBook(Book book);

}
