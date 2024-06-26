package com.rest.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/book")
public class BookController {
    @Autowired
    BookRepository bookRepository;

    @GetMapping
    public List<Book> getAllBookRecords() {
        return bookRepository.findAll();
    }

    @GetMapping(value = "{bookId}")
    public Book getBookById(@PathVariable(value = "bookId") long bookId) {
        return bookRepository.findById(bookId).get();
    }

    @PostMapping
    public Book createBook(@RequestBody @Validated Book bookRecord) {
        return bookRepository.save(bookRecord);
    }

    @PutMapping
    public Book updateBook(@RequestBody @Validated Book bookRecord) throws FileNotFoundException {
        if (bookRecord == null) {
            throw new FileNotFoundException("BookRecord or ID must be null");
        } else {
            Optional<Book> optionalBook = bookRepository.findById(bookRecord.getBookId());
            if (!optionalBook.isPresent()) {
                throw new FileNotFoundException("Book with ID: " + bookRecord.getBookId() + " does not exist.");
            }
            Book existingBookRecord = optionalBook.get();
            existingBookRecord.setName(bookRecord.getName());
            existingBookRecord.setSummery(bookRecord.getSummery());
            existingBookRecord.setRating(bookRecord.getRating());
            return bookRepository.save(existingBookRecord);
        }
    }

    @DeleteMapping(value = "{bookId}")
    public void deleteBookById(@PathVariable(value = "bookId") long bookId) throws FileNotFoundException {
        if (!bookRepository.findById(bookId).isPresent()) {
            throw new FileNotFoundException("Book id " + bookId + " not present");
        }
        bookRepository.deleteById(bookId);
    }


}
