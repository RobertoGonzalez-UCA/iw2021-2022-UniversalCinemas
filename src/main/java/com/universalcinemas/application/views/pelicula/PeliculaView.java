package com.universalcinemas.application.views.pelicula;

import java.util.Optional;

import javax.annotation.security.PermitAll;

import com.universalcinemas.application.data.business.Business;
import com.universalcinemas.application.data.film.Film;
import com.universalcinemas.application.data.film.FilmRepository;
import com.universalcinemas.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.timepicker.TimePicker;
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
    	Dialog compraDialogo = new Dialog();
    	compraDialogo.add(createDialogLayout(compraDialogo));
 		btn.addClickListener(e -> {
 			compraDialogo.open();
// 			 UI.getCurrent().navigate(HomeView.class);
 			 
 		});
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
	
	private static VerticalLayout createDialogLayout(Dialog dialog) {
		VerticalLayout dialogLayout = new VerticalLayout();
		HorizontalLayout buttonLayout = new HorizontalLayout();
		ComboBox<Business>  elegirCine = new ComboBox<>("Elegir cine");
//		elegirCine.setItemLabelGenerator();
		DatePicker fechaPelicula = new DatePicker("Día");
		TimePicker horaPelicula = new TimePicker("Hora");
//		IntegerField cantidadEntradas = new IntegerField();
		Button cancelarCompraButton = new Button("Cancelar", e -> dialog.close());
		Button elegirAsientoButton = new Button("Elegir asientos");
		
//		cantidadEntradas.setLabel("Número de entradas");
//		cantidadEntradas.setMin(1);
//		cantidadEntradas.setValue(1);
//		cantidadEntradas.setHasControls(true);
		
		buttonLayout.add(cancelarCompraButton, elegirAsientoButton);
		dialogLayout.add(elegirCine, fechaPelicula, horaPelicula, buttonLayout);
		
		return dialogLayout;
    }
}
