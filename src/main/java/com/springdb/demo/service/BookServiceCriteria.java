package com.springdb.demo.service;

import com.springdb.demo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
public class BookServiceCriteria {

    private EntityManager em;
    private CriteriaBuilder cb;
    private CriteriaQuery<Book> query;
    private Root<Book> book;
    @Autowired
    public BookServiceCriteria(EntityManager em) {
        this.em = em;
        this.cb = em.getCriteriaBuilder();
        this.query = cb.createQuery(Book.class);
        this.book = query.from(Book.class);
    }

    // author
    public List<Book> findAllByAuthor(Long id) {
        this.query = cb.createQuery(Book.class);
        this.book = query.from(Book.class);
        ListJoin<Book, Author> authors = book.join(Book_.authors);
        query.select(book)
                .where(cb.equal(authors.get(Author_.ID),id));

        return em.createQuery(query).getResultList();
    }

    public List<Book> findAllByAtLeastOneAuthor(List<Long> ids){
        this.query = cb.createQuery(Book.class);
        this.book = query.from(Book.class);
        ListJoin<Book, Author> authors = book.join(Book_.authors);
        query.select(book)
                .where(authors.get(Author_.ID)
                        .in(ids));

        return em.createQuery(query).getResultList();
    }

    public List<Book> findAllByAuthorGroup(List<Long> ids){
        this.query = cb.createQuery(Book.class);
        this.book = query.from(Book.class);
        ListJoin<Book, Author> authors = book.join(Book_.authors);
        query.select(book)
                .where(authors.get(Author_.ID)
                        .in(ids))
                .groupBy(book.get(Book_.ID))
                .having(cb.ge(cb
                        .count(book), ids.size()));

        return em.createQuery(query).getResultList();
    }

    //genre
    public List<Book> findAllByGenre(Long id) {
        this.query = cb.createQuery(Book.class);
        this.book = query.from(Book.class);
        ListJoin<Book, Genre> genres = book.join(Book_.genres);
        query.select(book)
                .where(cb.equal(genres.get(Genre_.ID),id));

        return em.createQuery(query).getResultList();
    }

    public List<Book> findAllByAtLeastOneGenre(List<Long> ids) {
        this.query = cb.createQuery(Book.class);
        this.book = query.from(Book.class);
        ListJoin<Book, Genre> genres = book.join(Book_.genres);
        query.select(book)
                .where(genres.get(Genre_.ID)
                        .in(ids));

        return em.createQuery(query).getResultList();
    }

    public List<Book> findAllByGenreGroup(List<Long> ids) {
        this.query = cb.createQuery(Book.class);
        this.book = query.from(Book.class);
        ListJoin<Book, Genre> genres = book.join(Book_.genres);
        query.select(book)
                .where(genres.get(Genre_.ID)
                        .in(ids))
                .groupBy(book.get(Book_.ID))
                .having(cb.ge(cb
                        .count(book), ids.size()));

        return em.createQuery(query).getResultList();
    }

    //publisher
    public List<Book> findByPublisher(String publisher) {
        this.query = cb.createQuery(Book.class);
        this.book = query.from(Book.class);
        query.select(book)
                .where(cb.equal(book.get(Book_.PUBLISHER), publisher));

        return em.createQuery(query).getResultList();
    }

    //price
    public List<Book> findByPrice(int price) {
        this.query = cb.createQuery(Book.class);
        this.book = query.from(Book.class);
        query.select(book)
                .where(cb.equal(book.get(Book_.PRICE), price));

        return em.createQuery(query).getResultList();
    }

    public List<Book> findByPriceRange(int minPrice, int maxPrice) {
        this.query = cb.createQuery(Book.class);
        this.book = query.from(Book.class);
        query.select(book)
                .where(cb.and(
                        cb.ge(book.get(Book_.PRICE), minPrice),
                        cb.le(book.get(Book_.PRICE), maxPrice)));

        return em.createQuery(query).getResultList();
    }

    public List<Book> findByPage(int pageAmount) {
        this.query = cb.createQuery(Book.class);
        this.book = query.from(Book.class);
        query.select(book)
                .where(cb.equal(book.get(Book_.PAGE_AMOUNT), pageAmount));

        return em.createQuery(query).getResultList();
    }

    public List<Book> findByPageRange(int minPage, int maxPage) {
        this.query = cb.createQuery(Book.class);
        this.book = query.from(Book.class);
        query.select(book)
                .where(cb.and(
                        cb.ge(book.get(Book_.PAGE_AMOUNT), minPage),
                        cb.le(book.get(Book_.PAGE_AMOUNT), maxPage)));

        return em.createQuery(query).getResultList();
    }

    public List<Book> findByText(String input) {
        this.query = cb.createQuery(Book.class);
        this.book = query.from(Book.class);
        ListJoin<Book, Author> authors = book.join(Book_.authors);
        query.select(book)
                .distinct(true)
                .where(cb.or(
                        cb.or(cb.like(cb.lower(book.get(Book_.TITLE)),
                                "%"+input.toLowerCase()+"%"),
                                cb.like(cb.lower(book.get(Book_.DESCRIPTION)),
                                        "%"+input.toLowerCase()+"%")),
                                cb.or(cb.like(cb.lower(authors.get(Author_.FULL_NAME)),
                                        "%"+input.toLowerCase()+"%"),
                                        cb.like(cb.lower(book.get(Book_.PUBLISHER)),
                                                "%"+input.toLowerCase()+"%"))));

        return em.createQuery(query).getResultList();
    }

}
