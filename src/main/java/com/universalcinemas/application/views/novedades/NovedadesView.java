package com.universalcinemas.application.views.novedades;

import java.util.Set;

import javax.annotation.security.PermitAll;

import com.github.javaparser.ParseException;
import com.universalcinemas.application.data.film.Film;
import com.universalcinemas.application.data.film.FilmService;
import com.universalcinemas.application.security.SecurityService;
import com.universalcinemas.application.views.MainLayout;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Novedades")
@Route(value = "news", layout = MainLayout.class)
@PermitAll
@Tag("novedades-view")
@JsModule("./views/novedades/novedades-view.ts")
public class NovedadesView extends LitTemplate implements HasComponents, HasStyle {

    public NovedadesView(FilmService filmservice) throws ParseException {
        Set<Film> films = filmservice.getFilms();

        //Layouts
        VerticalLayout verticalLayout1 = new VerticalLayout();
        verticalLayout1.setWidth("30%");

        //Mostrar todos los nombres de los eventos
        for(Film f: films)
        {
        	verticalLayout1.add(f.getName());
        }

        add(verticalLayout1);
    }
}