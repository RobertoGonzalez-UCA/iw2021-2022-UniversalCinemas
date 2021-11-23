package com.universalcinemas.application.data.filmlanguage;

import com.universalcinemas.application.data.filmlanguage.FilmLanguage;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;

public interface FilmLanguageRepository extends JpaRepository<FilmLanguage, Integer> {

}
