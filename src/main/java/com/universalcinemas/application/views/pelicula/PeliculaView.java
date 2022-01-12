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
import javax.annotation.security.RolesAllowed;

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
import com.vaadin.flow.component.UI;
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
@RolesAllowed({"ROLE_admin", "ROLE_operator", "ROLE_user"})
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
		
		VerticalLayout dialogLayout = new VerticalLayout();
		HorizontalLayout buttonLayout = new HorizontalLayout();
		
		ComboBox<Business> elegirCine = new ComboBox<>("Elegir cine");
		ComboBox<Session> fechas = new ComboBox<>("Elegir sesión");
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
		

		fechas.setItemLabelGenerator(sesion -> sesion.getDate_time().format(formatoFecha));
		
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
		boolean[][] matrizAsientos = new boolean[6][12];
		
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
        	
        }
  
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
				
				if(img2.getElement().getStyle().get("background-color") == "white" ) {
					img2.addClickListener((e -> {
						img2.getElement().getStyle().set("background-color", "green");
						matrizAsientos[num_row][num_col] = true;

//		 				Ticket ticket = new Ticket(12.,0,sesionElegida);
//		 				ticketService.saveNewTicket(ticket);
		 			
//		 				Seats seats2 = new Seats(num_row,num_col,ticket);
//		 				seatsService.saveNewOccupiedSeat(seats2);
						
		 			}));
				}
				else if(img2.getElement().getStyle().get("background-colo") == "green") {
					img2.addClickListener(e -> {
						img2.getElement().getStyle().set("background-color", "white");
						matrizAsientos[num_row][num_col] = false;
					});
				}

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
        setJustifyContentMode(JustifyContentMode.CENTER);
		
		dialog.setCloseOnEsc(false);
		
		Button btnConfirmar = new Button("Confirmar");
		Button btnCancelar = new Button("Cancelar");
		Dialog compra = new Dialog();
		
		btnCancelar.setIcon(new Icon(VaadinIcon.ARROW_LEFT));
		btnCancelar.addThemeVariants(ButtonVariant.LUMO_ERROR);
		
		btnConfirmar.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		btnConfirmar.setIconAfterText(true);
		btnConfirmar.setIcon(new Icon(VaadinIcon.ARROW_RIGHT));
		
		btnCancelar.addClickListener(e -> {
			dialog.close();
			compra.add(createDialogLayout(compra));
			compra.open();
 		});
		
		btnConfirmar.addClickListener(e -> {
			boolean encontrado = false;
			
			for(int i = 0; i < matrizAsientos.length && !encontrado; i++)
				for(int j = 0; j < matrizAsientos[i].length && !encontrado; j++)
					if(matrizAsientos[i][j])
						encontrado = true;
			
			if(encontrado) {
				Ticket ticket = new Ticket(12., 0, sesionElegida);
				int cantidad = 0;
				ticketService.saveNewTicket(ticket);
				for(int i = 0; i < matrizAsientos.length; i++)
					for(int j = 0; j < matrizAsientos[i].length; j++)
						if(matrizAsientos[i][j]) {
							seatsService.saveNewOccupiedSeat(new Seats(i, j, ticket));
							cantidad++;
						}
							
				String mensaje = "Compraste con éxito ";
				mensaje += cantidad > 1 ? "tus tickets." : "tu ticket";
				Notification.show(mensaje);
				dialog.close();
				Dialog cerrar = new Dialog();
				cerrar.add(createDialogLayout3(cerrar));
				cerrar.open();
			}
			else
				Notification.show("No seleccionaste ningún asiento");
 		});

		dialog.add(btnCancelar, btnConfirmar);
		
		dialog.setCloseOnOutsideClick(false);
		
		return verticalLayout;
	}
	
	private HorizontalLayout createDialogLayout3(Dialog dialog) {
		Button aceptar = new Button("Continuar", e -> {
			dialog.close();
			UI.getCurrent().navigate("");
		});
		
		Button descargar = new Button("Descargar entrada", e -> {
			dialog.close();
			UI.getCurrent().navigate("");
		});
		
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
        anchor.add(descargar);
        
        aceptar.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        descargar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        descargar.setIcon(new Icon(VaadinIcon.DOWNLOAD));
        
        return new HorizontalLayout(anchor, aceptar);
	}
}
