package com.springdb.demo.service;

import com.springdb.demo.model.Author;
import com.springdb.demo.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public Author createAuthor(Author author) {
        return authorRepository.save(author);
    }

    public Author createRandomAuthor() {
        StringBuilder fullname = new StringBuilder();
        List<String> surname = new ArrayList<>(Arrays.asList(
                "Мельник ","Шевченко","Коваленко","Бондаренко","Бойко","Ткаченко","Кравченко",
                "Ковальчук","Коваль","Олійник","Шевчук","Поліщук","Іванова","Ткачук","Савченко",
                "Бондар","Марченко","Руденко","Мороз","Лисенко","Петренко","Клименко","Павленко",
                "Кравчук","Іванов","Кузьменко","Пономаренко","Савчук","Василенко","Левченко"
        ));
        List<String> name = new ArrayList<>(Arrays.asList(
                "Анна", "Софія", "Марія", "Вікторія", "Дар’я"
                , "Вероніка", "Поліна", "Артем", "Олександр"
                , "Максим", "Богдан", "Назар", "Дмитро"
        ));
        List<String> fatherName = new ArrayList<>(Arrays.asList(
                "Матвійович", "Максимович", "Артемович", "Данилович", "Владиславович",
                "Олександрович", "Давидович", "Маркоович", "Дмитроович", "Денисович",
                "Тимофійович", "Романович", "Андрійович", "Юрійович"
        ));

        fullname.append(surname.get(new Random().nextInt(surname.size())))
                .append(" ")
                .append(name.get(new Random().nextInt(name.size())))
                .append(" ")
                .append(fatherName.get(new Random().nextInt(fatherName.size())));

        Author author = new Author();
        author.setFullName(fullname.toString());

        return createAuthor(author);
    }

    public Author getById(Long id) {
        return authorRepository.getById(id);
    }

    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    public boolean delete(Long id) {
        if (authorRepository.existsById(id)) {
            authorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
