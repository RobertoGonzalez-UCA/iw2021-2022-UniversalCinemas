package com.universalcinemas.application.views.inicio;

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
import com.vaadin.flow.component.html.H1;
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
public class InicioView extends VerticalLayout {

    public InicioView(FilmService filmService) throws ParseException { 		
        //Layouts
        VerticalLayout vLayoutMain = new VerticalLayout();
    	HorizontalLayout hLayoutAux = new HorizontalLayout();
        
        Iterable<Film> films = filmService.findAllByOrderByReleasedateDesc();

        for(Film f: films)
        {
        	VerticalLayout vLayoutAux = new VerticalLayout();
        	Image img=new Image(f.getFilmPoster(), f.getFilmPoster());
            img.setWidth("200px");
            img.addClickListener(e -> UI.getCurrent().navigate(PeliculaView.class, f.getId()));
            vLayoutAux.add(img);
            vLayoutAux.add(f.getName());
            hLayoutAux.add(vLayoutAux);
        }
        
        vLayoutMain.add(new H1("Ultimos estrenos"));
        vLayoutMain.add(hLayoutAux);
        
        HorizontalLayout hLayoutAux2 = new HorizontalLayout();
        
        films = filmService.findByGenre_Id(1);

        for(Film f: films)
        {
        	VerticalLayout vLayoutAux = new VerticalLayout();
        	Image img=new Image(f.getFilmPoster(), f.getFilmPoster());
            img.setWidth("200px");
            img.addClickListener(e -> UI.getCurrent().navigate(PeliculaView.class, f.getId()));
            vLayoutAux.add(img);
            vLayoutAux.add(f.getName());
            hLayoutAux2.add(vLayoutAux);
        }
        
        vLayoutMain.add(new H1("Peliculas de acción"));
        vLayoutMain.add(hLayoutAux2);
        add(vLayoutMain);
    }
}