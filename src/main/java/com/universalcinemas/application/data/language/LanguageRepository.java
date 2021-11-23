package com.universalcinemas.application.data.language;

import com.universalcinemas.application.data.language.Language;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;

public interface LanguageRepository extends JpaRepository<Language, Integer> {

}