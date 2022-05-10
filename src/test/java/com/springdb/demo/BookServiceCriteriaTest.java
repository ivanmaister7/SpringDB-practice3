package com.springdb.demo;

import com.springdb.demo.model.Book;
import com.springdb.demo.service.BookService;
import com.springdb.demo.service.BookServiceCriteria;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BookServiceCriteriaTest {

    @Autowired
    private BookService bookService;
    @Autowired
    private BookServiceCriteria bookServiceCriteria;

    private void sizeAssert(List<Book> books, List<Book> booksCriteria) {
        assertEquals(books.size(), booksCriteria.size());
    }
    private void equalAssert(List<Book> books, List<Book> booksCriteria) {
        assertEquals(books.stream()
                        .map(Book::getId)
                        .sorted()
                        .collect(Collectors.toList()),
                booksCriteria.stream()
                        .map(Book::getId)
                        .sorted()
                        .collect(Collectors.toList()));
    }

    @Test
    void findAllByAuthorTest() {
        List<Book> books = bookService.findAllByAuthor(3L);
        List<Book> booksCriteria = bookServiceCriteria.findAllByAuthor(3L);
        sizeAssert(books, booksCriteria);
        equalAssert(books,booksCriteria);
    }

    @Test
    void findAllByAuthorGroupTest() {
        List<Book> books1 = bookService.findAllByAuthorGroup(asList(3L, 4L, 5L));
        List<Book> books2 = bookService.findAllByAuthorGroup(asList(4L, 5L));
        List<Book> booksCriteria1 = bookServiceCriteria.findAllByAuthorGroup(asList(3L, 4L, 5L));
        List<Book> booksCriteria2 = bookServiceCriteria.findAllByAuthorGroup(asList(4L, 5L));
        sizeAssert(books1,booksCriteria1);
        equalAssert(books1,booksCriteria1);
        sizeAssert(books2,booksCriteria2);
        equalAssert(books2,booksCriteria2);
    }

    @Test
    void findAllByAtLeastOneAuthorTest(){
        List<Book> books = bookService.findAllByAtLeastOneAuthor(asList(1L, 10L));
        List<Book> booksCriteria = bookServiceCriteria.findAllByAtLeastOneAuthor(asList(1L, 10L));
        sizeAssert(books,booksCriteria);
        equalAssert(books,booksCriteria);
    }

    @Test
    void findAllByPublisherTest() {
        List<Book> books = bookService.findByPublisher("Віват");
        List<Book> booksCriteria = bookServiceCriteria.findByPublisher("Віват");
        sizeAssert(books,booksCriteria);
        equalAssert(books,booksCriteria);
    }

    @Test
    void findAllByPageTest() {
        List<Book> books = bookService.findByPage(357);
        List<Book> booksCriteria = bookServiceCriteria.findByPage(357);
        sizeAssert(books,booksCriteria);
        equalAssert(books,booksCriteria);
        assertEquals("Complete Poems ", booksCriteria.get(0).getTitle());
    }

    @Test
    void findByPageRangeTest() {
        List<Book> books = bookService.findByPageRange(300, 500);
        List<Book> booksCriteria = bookServiceCriteria.findByPageRange(300, 500);
        assertEquals(9, books.size());
        sizeAssert(books,booksCriteria);
        equalAssert(books,booksCriteria);
    }

    @Test
    void findByTextTest() {
        assertEquals(bookService.findByText("book").size(),
                bookServiceCriteria.findByText("book").size());
        assertEquals(bookService.findByText("the").size(),
                bookServiceCriteria.findByText("the").size());
        assertEquals(bookService.findByText("РАн").size(),
                bookServiceCriteria.findByText("РАн").size());
        equalAssert(bookService.findByText("РАн"),
                bookServiceCriteria.findByText("РАн"));
    }

}