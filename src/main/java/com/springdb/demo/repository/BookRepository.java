package com.springdb.demo.repository;

import com.springdb.demo.model.Book;
import com.springdb.demo.model.CoverType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByCoverType(CoverType type);

    // author
    @Query("select b from Book b inner join b.authors a where a.id= :id ")
    List<Book> findByAuthor(Long id);

    @Query("select distinct b from Book b inner join b.authors a where a.id in (:ids)")
    List<Book> findByAtLeastOneGroupAuthor(@Param("ids") List<Long> ids);

    @Query("select distinct b from Book b inner join b.authors a where a.id in (:ids) " +
            "group by b.id having count(b.id) >=" +
            "(select count (au.id) from Author au where au.id in (:ids))")
    List<Book> findByGroupAuthors(@Param("ids") List<Long> authorsId);

    // genre
    @Query("select b from Book b inner join b.genres g where g.id= :id ")
    List<Book> findByGenre(Long id);

    @Query("select distinct b from Book b inner join b.genres g where g.id in (:ids)")
    List<Book> findByAtLeastOneGroupGenre(@Param("ids") List<Long> ids);

    @Query("select distinct b from Book b inner join b.genres g where g.id in (:ids) " +
            "group by b.id having count(b.id) >=" +
            "(select count (gn.id) from Genre gn where gn.id in (:ids))")
    List<Book> findByGroupGenres(@Param("ids") List<Long> genresId);

    // publisher
    @Query("select b from Book b where b.publisher = :publisher ")
    List<Book> findByPublisher(String publisher);

    // price
    @Query("select b from Book b where b.price = :price")
    List<Book> findByPrice(int price);

    @Query("select b from Book b where b.price >= :minPrice and b.price <= :maxPrice")
    List<Book> findByPriceRange(int minPrice, int maxPrice);

    // pages
    @Query("select b from Book b where b.pageAmount = :page")
    List<Book> findByPage(int page);

    @Query("select b from Book b where b.pageAmount >= :minPage and b.pageAmount <= :maxPage")
    List<Book> findByPageRange(int minPage, int maxPage);

    @Query("select distinct b from Book b inner join b.authors a where" +
            " lower(b.title) like %:input% " +
            "or lower(a.fullName) like %:input% " +
            "or lower(b.description) like %:input% " +
            "or lower(b.publisher) like %:input% ")
    List<Book> findByText(String input);

}