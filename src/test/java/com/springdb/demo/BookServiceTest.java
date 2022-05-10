package com.springdb.demo;

import com.springdb.demo.model.Book;
import com.springdb.demo.model.CoverType;
import com.springdb.demo.model.SearchType;
import com.springdb.demo.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Test
    void findAllTest() {
        List<Book> books = bookService.findAll();
        assertEquals(19, books.size());
    }

    @Test
    void findAllByAuthorTest() {
        List<Book> books = bookService.findAllByAuthor(3L);
        assertEquals(4, books.size());
        assertEquals(asList(1L, 10L, 15L, 18L),
                books.stream()
                .map(Book::getId)
                .collect(Collectors.toList()));
    }

    @Test
    void findAllByAuthorGroupTest() {
        List<Book> books1 = bookService.findAllByAuthorGroup(asList(3L, 4L, 5L));
        assertEquals(2, books1.size());
        List<Book> books2 = bookService.findAllByAuthorGroup(asList(4L, 5L));
        assertEquals(4, books2.size());
        assertEquals(asList(9L, 10L, 17L, 18L),
                books2.stream()
                        .map(Book::getId)
                        .collect(Collectors.toList()));
    }

    @Test
    void findAllByAtLeastOneAuthorTest(){
        List<Book> books = bookService.findAllByAtLeastOneAuthor(asList(1L, 10L));
        Set<Book> temp = bookService.findAllByAuthor(1L).stream().collect(Collectors.toSet());
        temp.addAll(bookService.findAllByAuthor(10L));
        assertEquals(temp.stream().map(Book::getId).sorted().collect(Collectors.toList()),
                    books.stream().map(Book::getId).sorted().collect(Collectors.toList()));
        assertEquals(temp.size(), books.size());
    }

    @Test
    void findAllByPublisherTest() {
        List<Book> books = bookService.findByPublisher("Віват");
        assertEquals(2, books.size());
        assertEquals(asList(16L, 19L),books.stream()
                                        .map(Book::getId)
                                        .sorted()
                                        .collect(Collectors.toList()));
    }

    @Test
    void findAllByPageTest() {
        List<Book> books = bookService.findByPage(357);
        assertEquals(1, books.size());
        assertEquals("Complete Poems ", books.get(0).getTitle());
    }

    @Test
    void findByPageRangeTest() {
        List<Book> books = bookService.findByPageRange(300, 500);
        assertEquals(9, books.size());
        assertEquals(asList(11L, 7L, 13L, 16L, 2L, 19L, 12L, 1L, 9L)
                        .stream()
                        .sorted()
                        .collect(Collectors.toList()),
                books.stream()
                        .map(Book::getId)
                        .sorted()
                        .collect(Collectors.toList()));
    }

    @Test
    void findByTextTest() {
        assertEquals(2, bookService.findByText("book").size());
        assertEquals(7, bookService.findByText("the").size());
        List<Book> books = bookService.findByText("РАн");
        assertEquals(7, books.size());
        assertEquals(asList(1L, 4L, 6L, 14L, 16L, 8L, 17L)
                        .stream()
                        .sorted()
                        .collect(Collectors.toList()),
                books.stream()
                        .map(Book::getId)
                        .sorted()
                        .collect(Collectors.toList()));

    }
    @Test
    void searchAndTest() {
        List<Book> books = bookService.search(
                SearchType.AND,
                "РАн",
                0, 350,
                1000, 2000,
                null,
                null,
                null);
        assertEquals(2, books.size());
        books = bookService.search(
                SearchType.AND,
                "",
                0, 10000,
                1000, 2100,
                CoverType.HARD,
                asList(3L,4L),
                null);
        assertEquals(2, books.size());
    }
    @Test
    void searchOrTest() {
        List<Book> books = bookService.search(
                SearchType.OR,
                "вРАн",
                0, 0,
                0, 250,
                null,
                null,
                null);
        assertEquals(4, books.size());
        books = bookService.search(
                SearchType.OR,
                "",
                600, 800,
                0, 0,
                CoverType.HARD,
                null,
                asList(2L));
        assertEquals(9, books.size());
    }
}