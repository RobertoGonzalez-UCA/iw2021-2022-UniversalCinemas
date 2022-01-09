package com.universalcinemas.application.views.crudfilms;

import javax.annotation.security.PermitAll;

import com.universalcinemas.application.views.MainLayout;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Crud Peliculas")
@Route(value = "crudfilms", layout = MainLayout.class)
@PermitAll
public class CrudFilmsView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	public CrudFilmsView(){ 		
    	VerticalLayout vLayoutMain = new VerticalLayout();
    	FormLayout form = new FormLayout();
    	vLayoutMain.add(new H1("Añadir película"));
    	TextField agerating = new TextField("Edad mínima");
    	TextField director = new TextField("Director");
    	TextField name = new TextField("Nombre");
    	TextField rating = new TextField("Puntuación");
    	TextField releasedate = new TextField("Fecha de lanzamiento");
    	TextField synopsis = new TextField("Sinopsis");
    	TextField trailerurl = new TextField("URL del trailer");
    	TextField filmposter = new TextField("URL del póster");
    	TextField genre_id = new TextField("ID del género");
    	form.add(agerating, director, name, rating, releasedate, synopsis, trailerurl, filmposter, genre_id);
    	vLayoutMain.add(form);
    	add(vLayoutMain);
    }
}