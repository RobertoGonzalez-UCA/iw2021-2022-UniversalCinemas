package com.universalcinemas.application.views.pelicula;

import java.util.Optional;

import javax.annotation.security.PermitAll;

import com.universalcinemas.application.data.film.Film;
import com.universalcinemas.application.data.film.FilmRepository;
import com.universalcinemas.application.data.plan.Plan;
import com.universalcinemas.application.data.plan.PlanRepository;
import com.universalcinemas.application.security.SecurityService;
import com.universalcinemas.application.views.MainLayout;
import com.universalcinemas.application.views.home.HomeView;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Pelicula")
@Route(value = "pelicula", layout = MainLayout.class)
@PermitAll
public class PeliculaView extends VerticalLayout implements HasUrlParameter<Integer> {
	private FilmRepository filmRepository;
	@Override
	public void setParameter(BeforeEvent event, Integer filmId) {
		Optional<Film> film = filmRepository.findById(filmId);
    	VerticalLayout verticalLayout = new VerticalLayout();
    	HorizontalLayout horizontalLayout = new HorizontalLayout();
    	Button btn = new Button("Comprar entrada");
 		btn.addClickListener(e -> UI.getCurrent().navigate(HomeView.class));
    	Image img=new Image(film.get().getFilmPoster(), film.get().getFilmPoster());
        img.setWidth("350px");
        horizontalLayout.add(img);
    	verticalLayout.add(new Label(film.get().getName()));
    	verticalLayout.add(new Label(film.get().getSynopsis()));
    	verticalLayout.add(btn);
    	verticalLayout.setWidth("30%");
    	horizontalLayout.add(verticalLayout);
    	add(horizontalLayout);
	}
	
	public PeliculaView(FilmRepository filmRepository) {
		this.filmRepository = filmRepository;
	}
}
