package com.universalcinemas.application.views.pelicula;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.annotation.security.PermitAll;

import com.universalcinemas.application.data.business.Business;
import com.universalcinemas.application.data.film.Film;
import com.universalcinemas.application.data.film.FilmService;
import com.universalcinemas.application.data.seats.Seats;
import com.universalcinemas.application.data.seats.SeatsService;
import com.universalcinemas.application.data.session.Session;
import com.universalcinemas.application.data.ticket.Ticket;
import com.universalcinemas.application.data.ticket.TicketService;
import com.universalcinemas.application.views.MainLayout;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;

@PageTitle("Pelicula")
@Route(value = "pelicula", layout = MainLayout.class)
@PermitAll
public class PeliculaView extends VerticalLayout implements HasUrlParameter<Integer> {
	private static final long serialVersionUID = 1L;
	private static DateTimeFormatter formatoFecha = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).localizedBy(Locale.forLanguageTag("es-ES"));
	
	private FilmService filmService;
	private SeatsService seatsService;
	private TicketService ticketService;
	
	private Session sesionElegida;
	private List<Session> sesiones;
	private List<Business> cines;
	
	@Override
	public void setParameter(BeforeEvent event, Integer filmId) {
		sesionElegida = null;
		Optional<Film> film = filmService.findById(filmId);
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
    	Image img=new Image(film.get().getFilmposter(), film.get().getFilmposter());
        img.setWidth("350px");
        horizontalLayout.add(img);
    	verticalLayout.add(new Label(film.get().getName()));
    	verticalLayout.add(new Label(film.get().getSynopsis()));
    	verticalLayout.add(btn);
    	verticalLayout.setWidth("30%");
    	horizontalLayout.add(verticalLayout);
    	add(horizontalLayout);
	}
	
	public PeliculaView(FilmService filmService, SeatsService seatsService, TicketService ticketService) {
		this.filmService = filmService;
		this.seatsService = seatsService;
		this.ticketService = ticketService;
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

    	dialog.add(title,divColor);

    	dialog.setMaxWidth("900px");
    	dialog.setMaxHeight("700px");
    	
    	Iterable<Ticket> ticket2 = ticketService.findBySessionId(sesionElegida.getId());
    	List<Seats> seats = new ArrayList<Seats>();
    	
    	for(Ticket t: ticket2)
        {
    		Iterable<Seats> seats2 = seatsService.getAllOccupiedSeatsByTicketId(t.getId());
    		
    		for(Seats s: seats2) {
            	System.out.println(s.getId());
    			seats.add(s);
    		}
        	
        	//tickets_id.add(t.getId());
        	//seats_id.add(seatsService.getAllOccupiedSeatsByTicketId(t.getId()));
        	//System.out.println(t.getId());
        }
    	
    	
//    	Iterable<Seats> seats = seatsService.getAllOccupiedSeatsByTicketId(tickets_id.get(0));
//    	List<Integer> seats_id = new ArrayList<Integer>();
//    	
//    	for(Seats s: seats)
//        {
//    		seats_id.add(s.getId());
//        	System.out.println(s.getId());
//        }
    	
		//seats.add(seatsService.getAllOccupiedSeatsByTicketId(tickets_id.get(i)));

//    	for(int i = 0; i < tickets_id.size(); i++) {
//    		Seats seat = new Seats();
//    		seat = seatsService.getAllOccupiedSeatsByTicketId(tickets_id.get(i));
//    		seats_id.add();
//    		System.out.println(seatsService.getAllOccupiedSeatsByTicketId(tickets_id.get(i)));
//    		System.out.println(seats.get(i).getId());
//    	}
    	
    	//List<Seats> seats = seatsRepository.findAll();		
		List<Integer> cols = new ArrayList<Integer>();
		List<Integer> rows = new ArrayList<Integer>();
		
		for (int i = 0; i < seats.size(); i++) {
			cols.add(seats.get(i).getCol());
			rows.add(seats.get(i).getRow());
        }
		
		for(int i = 0; i < 6; i++) {
			HtmlComponent br = new HtmlComponent("br");
			for (int j = 0; j < 12; j++) {	
				Image img2 = new Image("https://upload.wikimedia.org/wikipedia/commons/9/99/Sample_User_Icon.png","User Image");	

				
				for(int r = 0; r < rows.size(); r++) {
					for(int c = 0; c < cols.size(); c++) {
						//System.out.println(i + " " + j + " " + r + " " + c + " " + rows.get(r) + " " +  cols.get(c));
						if (i == rows.get(r) && j == cols.get(c) && r == c) 
			 				img2.getElement().getStyle().set("background-color", "red");
					}
				}
				if(img2.getElement().getStyle().get("background-color") == null )
					img2.getElement().getStyle().set("background-color", "white");

	 			img2.setHeight("40px");
				img2.setWidth("40px");
	 			
				int num_row = i;
				int num_col = j;
				
				img2.addClickListener((e -> {
		 			img2.getElement().getStyle().set("background-color", "green");

		 			Ticket ticket = new Ticket(12.,0,sesionElegida);
		 			ticketService.saveNewTicket(ticket);
		 			
		 			Seats seats2 = new Seats(num_row,num_col,ticket);
		 			seatsService.saveNewOccupiedSeat(seats2);
		 		}));
				

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
		dialog.open();
    	
    	setHeightFull();
		setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);//puts button in vertical center
		
		dialog.setCloseOnEsc(false);
		
		Button btnConfirmar = new Button("Confirmar");
		Anchor anchor = new Anchor(new StreamResource("Entrada.pdf", new InputStreamFactory() {
            @Override
            public InputStream createInputStream() {
                File file = new File("entradas/Entrada.pdf");
                try {
                    return new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    // TODO: handle FileNotFoundException somehow
                    throw new RuntimeException(e);
                }
            }
        }), "");
        anchor.getElement().setAttribute("download", true);
        anchor.add(btnConfirmar);

		dialog.add(anchor);
		btnConfirmar.addClickListener(e -> {
			Notification.show("Compraste con éxito tu/s ticket/s");
 			dialog.close(); 
 		});
		
		dialog.setCloseOnOutsideClick(true);
		
		return verticalLayout;
	}
}
