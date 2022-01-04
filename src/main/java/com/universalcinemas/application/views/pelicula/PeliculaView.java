package com.universalcinemas.application.views.pelicula;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.annotation.security.PermitAll;

import com.universalcinemas.application.data.business.Business;
import com.universalcinemas.application.data.film.Film;
import com.universalcinemas.application.data.film.FilmRepository;
import com.universalcinemas.application.data.film.FilmService;
import com.universalcinemas.application.data.session.Session;
import com.universalcinemas.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Pelicula")
@Route(value = "pelicula", layout = MainLayout.class)
@PermitAll
public class PeliculaView extends VerticalLayout implements HasUrlParameter<Integer> {
	private static final long serialVersionUID = 1L;
	private FilmRepository filmRepository;
	private FilmService filmService;
	@Override
	public void setParameter(BeforeEvent event, Integer filmId) {
		Optional<Film> film = filmRepository.findById(filmId);
    	VerticalLayout verticalLayout = new VerticalLayout();
    	HorizontalLayout horizontalLayout = new HorizontalLayout();
    	Button btn = new Button("Comprar entrada");
    	Dialog compraDialogo = new Dialog();
 		btn.addClickListener(e -> {
 			List<Session> listaSesiones = filmService.obtenerSessionsPelicula(filmId, LocalDate.now());
 			List<Business> listaCines = filmService.obtenerBusinessSesiones(listaSesiones);
 	    	compraDialogo.add(createDialogLayout(compraDialogo, listaCines));
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
	
	public PeliculaView(FilmRepository filmRepository, FilmService filmService) {
		this.filmRepository = filmRepository;
		this.filmService = filmService;
	}
	
	private static VerticalLayout createDialogLayout(Dialog dialog, List<Business> listaCines) {
		LocalDate hoy = LocalDate.now();
		VerticalLayout dialogLayout = new VerticalLayout();
		HorizontalLayout buttonLayout = new HorizontalLayout();
		
		ComboBox<Business> elegirCine = new ComboBox<>("Elegir cine");
		DatePicker fechaPelicula = new DatePicker("Día");
		TimePicker horaPelicula = new TimePicker("Hora");
//		IntegerField cantidadEntradas = new IntegerField();

		Button cancelarCompraButton = new Button("Cancelar", e -> dialog.close());
		Button elegirAsientoButton = new Button("Elegir asientos");
		
		elegirCine.setItems(listaCines);
		elegirCine.setItemLabelGenerator(cine -> cine.getName());
		
		fechaPelicula.setMin(hoy);
		fechaPelicula.setValue(hoy);
		
//		cantidadEntradas.setLabel("Número de entradas");
//		cantidadEntradas.setMin(1);
//		cantidadEntradas.setValue(1);
//		cantidadEntradas.setHasControls(true);
		
		buttonLayout.add(cancelarCompraButton, elegirAsientoButton);
		dialogLayout.add(elegirCine, fechaPelicula, horaPelicula, buttonLayout);
		
		dialog.setCloseOnEsc(false);
		dialog.setCloseOnOutsideClick(false);
		
		return dialogLayout;
    }
}
