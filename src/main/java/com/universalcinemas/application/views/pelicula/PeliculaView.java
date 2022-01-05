package com.universalcinemas.application.views.pelicula;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.annotation.security.PermitAll;

import com.universalcinemas.application.data.business.Business;
import com.universalcinemas.application.data.film.Film;
import com.universalcinemas.application.data.film.FilmRepository;
import com.universalcinemas.application.data.film.FilmService;
import com.universalcinemas.application.data.session.Session;
import com.universalcinemas.application.data.user.User;
import com.universalcinemas.application.views.MainLayout;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.server.ClassResource;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.flow.component.html.Div;

@PageTitle("Pelicula")
@Route(value = "pelicula", layout = MainLayout.class)
@PermitAll
public class PeliculaView extends VerticalLayout implements HasUrlParameter<Integer> {
	private static final long serialVersionUID = 1L;
	private static DateTimeFormatter formatoFecha = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).localizedBy(Locale.forLanguageTag("es-ES"));
	
	private FilmRepository filmRepository;
	private FilmService filmService;
	
	private Session sesionElegida;
	private List<Session> sesiones;
	private List<Business> cines;
	
	@Override
	public void setParameter(BeforeEvent event, Integer filmId) {
		sesionElegida = null;
		Optional<Film> film = filmRepository.findById(filmId);
    	VerticalLayout verticalLayout = new VerticalLayout();
    	HorizontalLayout horizontalLayout = new HorizontalLayout();
    	Button btn = new Button("Comprar entrada");
 		btn.addClickListener(e -> {
 	    	Dialog compraDialogo = new Dialog();
 			sesiones = filmService.obtenerSessionsPelicula(filmId, LocalDate.now());
 			cines = filmService.obtenerBusinessSesiones(sesiones);
 	    	compraDialogo.add(createDialogLayout(compraDialogo));
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
	
	private VerticalLayout createDialogLayout(Dialog dialog) {
//		LocalDate hoy = LocalDate.now();
		VerticalLayout dialogLayout = new VerticalLayout();
		HorizontalLayout buttonLayout = new HorizontalLayout();
		
		ComboBox<Business> elegirCine = new ComboBox<>("Elegir cine");
		ComboBox<Session> fechas = new ComboBox<>("Elegir sesión");
//		DatePicker fechaPelicula = new DatePicker("Día");
//		TimePicker horaPelicula = new TimePicker("Hora");
//		IntegerField cantidadEntradas = new IntegerField();

		Button cancelarCompraButton = new Button("Cancelar", e -> {
			dialog.close();
			sesionElegida = null;
		});
		
		Button elegirAsientoButton = new Button("Elegir asientos", e -> {
			dialog.close();
			Dialog dialog2 = new Dialog();
			dialog2.setWidth("800px");
			createDialogLayout2(dialog2);
		});
//		elegirAsientoButton.addClickListener(e -> 
//		elegirAsientoButton.getUI().ifPresent(ui ->
//	        ui.navigate("/chooseseat"))
//		);
		
//		VerticalLayout dialogLayout2 = createDialogLayout(dialog);
//		dialog.add(dialogLayout2);
//		Button button22 = new Button("Show dialog", e -> dialog.open());
//		add(dialog, button22);
		
		elegirAsientoButton.setEnabled(false);
		elegirAsientoButton.setIcon(new Icon(VaadinIcon.ARROW_RIGHT));
		elegirAsientoButton.setIconAfterText(true);
		elegirAsientoButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		
		cancelarCompraButton.setIcon(new Icon(VaadinIcon.ARROW_LEFT));
		cancelarCompraButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
		
		elegirCine.setItems(cines);
		elegirCine.setItemLabelGenerator(cine -> cine.getName());
		
//		fechaPelicula.setMin(hoy);
		fechas.setItemLabelGenerator(sesion -> sesion.getDate_time().format(formatoFecha));
		
//		cantidadEntradas.setLabel("Número de entradas");
//		cantidadEntradas.setMin(1);
//		cantidadEntradas.setValue(1);
//		cantidadEntradas.setHasControls(true);
		
		buttonLayout.add(cancelarCompraButton, elegirAsientoButton);
		dialogLayout.add(buttonLayout, elegirCine);
		
		elegirCine.addValueChangeListener(e -> {
			List<Session> sesionesCine = filmService.filtrarPorCine(sesiones, e.getValue().getId());
			fechas.setItems(sesionesCine);
			dialogLayout.add(fechas);
		});
		
		fechas.addValueChangeListener(e -> {
			sesionElegida = e.getValue();
			elegirAsientoButton.setEnabled(true);
		});
		
		dialog.setCloseOnEsc(false);
		dialog.setCloseOnOutsideClick(false);
		
		return dialogLayout;
    }
	
	private VerticalLayout createDialogLayout2(Dialog dialog) {
		VerticalLayout verticalLayout = new VerticalLayout();
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		H2 title = new H2("ELEGIR ASIENTO");
		Div divColor = new Div();
		divColor.getElement().getStyle().set("background","linear-gradient(to bottom, red, black)");
		divColor.getElement().getStyle().set("height","30px");
		divColor.getElement().getStyle().set("margin-top", "10px");
		divColor.getElement().getStyle().set("margin-bottom", "10px");


		title.getElement().getStyle().set("text-align", "center");
//		title.getElement().getStyle().set("margin-bottom", "30px");

    	dialog.add(title,divColor);

    	dialog.setMaxWidth("900px");
    	dialog.setMaxHeight("700px");
    	
		for(int i = 0; i < 6; i++) {
			HtmlComponent br = new HtmlComponent("br");
			for (int j = 0; j < 12; j++) {
				Image img2= new Image("https://p.kindpng.com/picc/s/111-1114911_person-icon-png-download-icono-usuario-png-transparent.png","User Image");
				img2.setHeight("40px");
				img2.setWidth("40px");
				if(j == 0)
					img2.getElement().getStyle().set("margin-left", "100px");
				img2.getElement().getStyle().set("margin-right", "2px");

				dialog.add(img2);
				if(j == 3 || j == 7){
					Div d = new Div();
					d.getElement().getStyle().set("display", "inline");
					d.getElement().getStyle().set("height", "700px");
					d.getElement().getStyle().set("width", "2px");
					d.getElement().getStyle().set("margin", "0 auto");
					d.getElement().getStyle().set("padding", "0");
					d.getElement().getStyle().set("border-left", "25px solid #000");
					dialog.add(d);
				}
			}
			dialog.add(br);
		}
		
		
		
    	horizontalLayout.add(verticalLayout);
//    	dialog.add(title,img,img2,img3,img4);
		dialog.open();
    	//add(title,horizontalLayout);
    	
    	setHeightFull();
		setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);//puts button in vertical center
		
		dialog.setCloseOnEsc(false);
		dialog.setCloseOnOutsideClick(false);
		
		Button btnConfirmar = new Button("Confirmar");
//		btnConfirmar.getElement().getStyle().set(null, null);
		dialog.add(btnConfirmar);
		btnConfirmar.addClickListener(e -> {
 			dialog.close(); 
 		});
		
		return verticalLayout;
	}
}
