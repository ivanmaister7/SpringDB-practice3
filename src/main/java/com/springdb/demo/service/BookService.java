package com.springdb.demo.service;

import com.springdb.demo.model.*;
import com.springdb.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService{

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private GenreService genreService;

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> createRandomBooks() {
        List<String> bookName = new ArrayList<>(Arrays.asList(
                "A Doll's House by Henrik Ibsen","Complete Poems ","Anna Karenina","Beloved ",
                "Berlin Alexanderplatz ","Blindness ","The Book of Disquiet ",
                "The Book of Job, Israel.","The Brothers Karamazov ","Buddenbrooks ",
                "Canterbury Tales ","The Castle","A Sentimental Education ","Absalom, Absalom! ",
                "The Adventures of Huckleberry Finn ","The Aeneid ","Children of Gebelawi ",
                "Collected Fictions ","The Complete Stories"
        ));
        List<String> bookPubl = new ArrayList<>(Arrays.asList(
               "А-ба-ба-га-ла-ма-га", "Видавництво Старого Лева", "Видавничий дім Школа",
                "Віват", "Клуб Сімейного Дозвілля", "Лабораторіz", "Наш Формат", "Ранок"
        ));
        for(String s : bookName){
            Book b = new Book();
            b.setTitle(s);
            b.setDescription("...");
            b.setPageAmount(new Random().nextInt(700) + 200);
            b.setPrice(new Random().nextInt(2000) + 100);
            b.setPublisher("");
            b.setCoverType(new Random().nextInt() % 2 == 0? CoverType.HARD: CoverType.PAPER);
            b.setPublisher(bookPubl.get(new Random().nextInt(bookPubl.size())));
            int authorIndex = new Random().nextInt(authorService
                    .findAll()
                    .size() - 3);
            b.setAuthors(authorService
                    .findAll()
                    .subList(authorIndex, authorIndex + 1 + new Random().nextInt(3)));

            authorIndex = new Random().nextInt(genreService
                    .findAll()
                    .size() - 3);
            b.setGenres(genreService
                    .findAll()
                    .subList(authorIndex, authorIndex + 1 + new Random().nextInt(3)));

            createBook(b);
        }
        return bookRepository.findAll();
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public List<Book> findAllByAuthor(Long authorId) {
        return bookRepository.findByAuthor(authorId);
    }

    public List<Book> findAllByAtLeastOneAuthor(List<Long> authorsId) {
        return bookRepository.findByAtLeastOneGroupAuthor(authorsId);
    }

    public List<Book> findAllByAuthorGroup(List<Long> authorsId) {
        return bookRepository.findByGroupAuthors(authorsId);
    }

    public List<Book> findAllByGenre(Long genreId) {
        return bookRepository.findByGenre(genreId);
    }

    public List<Book> findAllByAtLeastOneGenre(List<Long> genreIds) {
        return bookRepository.findByAtLeastOneGroupGenre(genreIds);
    }

    public List<Book> findAllByGenreGroup(List<Long> genreIds) {
        return bookRepository.findByGroupGenres(genreIds);
    }

    public List<Book> findByPublisher(String publisher) {
        return bookRepository.findByPublisher(publisher);
    }

    public List<Book> findByPrice(int price) {
        return bookRepository.findByPrice(price);
    }

    public List<Book> findByPriceRange(int minPrice, int maxPrice) {
        return bookRepository.findByPriceRange(minPrice, maxPrice);
    }

    public List<Book> findByPage(int pageAmount) {
        return bookRepository.findByPage(pageAmount);
    }

    public List<Book> findByPageRange(int minPage, int maxPage) {
        return bookRepository.findByPageRange(minPage, maxPage);
    }

    public List<Book> findByText(String input) {
        return bookRepository.findByText(input.toLowerCase());
    }

    public List<Book> search(SearchType stype,
                             String findText,
                             int minPage, int maxPage,
                             int minPrice, int maxPrice,
                             CoverType type,
                             List<Long> authors,
                             List<Long> genres){
        if (stype == SearchType.AND)
            return searchAnd( findText,
                    minPage,  maxPage,
                    minPrice,  maxPrice,
                    type,
                    authors,
                    genres);
        return searchOr( findText,
                minPage,  maxPage,
                minPrice,  maxPrice,
                type,
                authors,
                genres);
    }

    private List<Book> searchAnd(String findText,
                                int minPage, int maxPage,
                                int minPrice, int maxPrice,
                                CoverType type,
                                List<Long> authors,
                                List<Long> genres) {
        List<Book> temp = null;
        if(type!=null){
            temp = bookRepository.findByCoverType(type);
        }
        if(authors!=null && !authors.isEmpty()){
            if(temp==null){
                temp = bookRepository.findByAtLeastOneGroupAuthor(authors);
            }
            else {
                temp = intersect(temp,bookRepository
                        .findByAtLeastOneGroupAuthor(authors));
            }
        }
        if(genres!=null && !genres.isEmpty()){
            if(temp==null){
                temp = bookRepository.findByAtLeastOneGroupGenre(genres);
            }
            else {
                temp = intersect(temp,bookRepository
                        .findByAtLeastOneGroupGenre(genres));
            }
        }
        List<Book> res =
                intersect(
                        intersect(bookRepository.findByText(findText),
                                bookRepository.findByPageRange(minPage, maxPage)),
                        bookRepository.findByPriceRange(minPrice,maxPrice));
        return temp == null? res : intersect(res, temp);
    }
    private List<Book> searchOr(String findText,
                               int minPage, int maxPage,
                               int minPrice, int maxPrice,
                               CoverType type,
                               List<Long> authors,
                               List<Long> genres) {
        List<Book> res = bookRepository.findByPageRange(minPage, maxPage);
        res.addAll(bookRepository.findByPriceRange(minPrice, maxPrice));
        if(type!=null){
            res.addAll(bookRepository.findByCoverType(type));
        }
        if(authors!=null){
            res.addAll(bookRepository.findByAtLeastOneGroupAuthor(authors));
        }
        if(genres!=null ){
            res.addAll(bookRepository.findByAtLeastOneGroupGenre(genres));

        }
        if(!findText.isEmpty()){
            res.addAll(bookRepository.findByText(findText));
        }
        return res.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList());
    }

    private <T> List<T> intersect(List<T> l1, List<T> l2){
        return l1.stream()
                .distinct()
                .filter(l2::contains)
                .collect(Collectors.toList());
    }
}
