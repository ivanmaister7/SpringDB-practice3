package com.springdb.demo.service;

import com.springdb.demo.model.Author;
import com.springdb.demo.model.Genre;
import com.springdb.demo.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    public Genre createGenre(Genre genre) {
        return genreRepository.save(genre);
    }

    public List<Genre> createAllGenre() {
        List<String> name = new ArrayList<>(Arrays.asList(
                "Наукова література" , "Класичні твори" , "Наукова фантастика" ,
                "Науково-популярні" , "Фентезі" , "Гумор і сатира" , "Військові романи" ,
                "Мемуари" , "Aвтобіографія " , "Поезія" , "Казки" , "Трилери"
        ));

        for(String s: name){
            Genre genre = new Genre();
            genre.setGenre(s);
            createGenre(genre);
        }
        return genreRepository.findAll();
    }

    public Genre getById(Long id) {
        return genreRepository.getById(id);
    }

    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    public boolean delete(Long id) {
        if (genreRepository.existsById(id)) {
            genreRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
