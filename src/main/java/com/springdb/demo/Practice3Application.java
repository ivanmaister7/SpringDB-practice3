package com.springdb.demo;

import com.springdb.demo.model.Book;
import com.springdb.demo.service.AuthorService;
import com.springdb.demo.service.BookService;
import com.springdb.demo.service.BookServiceCriteria;
import com.springdb.demo.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@RestController
@RequestMapping("/")
@SpringBootApplication
public class Practice3Application {
    @Autowired
    AuthorService authorService;
    @Autowired
    GenreService genreService;
    @Autowired
    BookService bookService;
    @Autowired
    BookServiceCriteria bookServiceCriteria;

    @GetMapping
    List<Book> test(){
        return bookService.findAll();
    }

    @GetMapping("/1")
    List<Book> testCriteria(){
        return bookService.findByText("РАн");
    }
    @GetMapping("/11")
    List<Book> testCriteria11(){
        return bookService.findByPageRange(0, 350);
    }
    @GetMapping("/2")
    List<Book> testCriteriaPage(){
        return intersect(
                intersect(bookService.findByText("РАн"),
                        bookService.findByPageRange(0, 350)),
        bookService.findByPriceRange(1000,2000));
    }
    private <T> List<T> intersect(List<T> l1, List<T> l2){
        return l1.stream()
                .distinct()
                .filter(l2::contains)
                .collect(Collectors.toList());
    }
    public static void main(String[] args) {
        SpringApplication.run(Practice3Application.class, args);
    }

}
