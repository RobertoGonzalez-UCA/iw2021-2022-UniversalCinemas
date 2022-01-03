package com.universalcinemas.application.views.home;

import java.util.Set;

import javax.annotation.security.PermitAll;

import com.github.javaparser.ParseException;
import com.universalcinemas.application.data.film.Film;
import com.universalcinemas.application.data.film.FilmRepository;
import com.universalcinemas.application.data.film.FilmService;
import com.universalcinemas.application.data.genre.Genre;
import com.universalcinemas.application.security.SecurityService;
import com.universalcinemas.application.views.MainLayout;
import com.universalcinemas.application.views.pelicula.PeliculaView;
import com.universalcinemas.application.views.planes.PlanesView;
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

@PageTitle("Inicio")
@Route(value = "home", layout = MainLayout.class)
@PermitAll
public class HomeView extends VerticalLayout {

    public HomeView(FilmRepository filmrepository) throws ParseException { 		
        //Layouts
        VerticalLayout verticalLayout1 = new VerticalLayout();
        HorizontalLayout horizontalLayout1 = new HorizontalLayout();
        
        Iterable<Film> films = filmrepository.findAllByOrderByReleasedateDesc();

        for(Film f: films)
        {
        	VerticalLayout verticalLayout2 = new VerticalLayout();
        	Image img=new Image(f.getFilmPoster(), f.getFilmPoster());
            img.setWidth("200px");
            img.addClickListener(e -> UI.getCurrent().navigate(PeliculaView.class, f.getId()));
            verticalLayout2.add(img);
        	verticalLayout2.add(f.getName());
        	horizontalLayout1.add(verticalLayout2);
        }
        
        verticalLayout1.add("Ultimos estrenos");
        verticalLayout1.add(horizontalLayout1);
        
        VerticalLayout verticalLayout3 = new VerticalLayout();
        HorizontalLayout horizontalLayout2 = new HorizontalLayout();
        
        films = filmrepository.findByGenre_Id(1);

        for(Film f: films)
        {
        	VerticalLayout verticalLayout2 = new VerticalLayout();
        	Image img=new Image(f.getFilmPoster(), f.getFilmPoster());
            img.setWidth("200px");
            img.addClickListener(e -> UI.getCurrent().navigate(PeliculaView.class, f.getId()));
            verticalLayout2.add(img);
        	verticalLayout2.add(f.getName());
        	horizontalLayout2.add(verticalLayout2);
        }
        
        verticalLayout3.add("Peliculas de acción");
        verticalLayout3.add(horizontalLayout2);
        add(verticalLayout1, verticalLayout3);
    }
}