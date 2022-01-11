package com.universalcinemas.application.views.crudsessions;

import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.artur.helpers.CrudServiceDataProvider;

import com.universalcinemas.application.data.film.Film;
import com.universalcinemas.application.data.film.FilmService;
import com.universalcinemas.application.data.room.Room;
import com.universalcinemas.application.data.room.RoomService;
import com.universalcinemas.application.data.session.Session;
import com.universalcinemas.application.data.session.SessionService;
import com.universalcinemas.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Panel sesiones")
@Route(value = "crudsessions/:SessionID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed({"ROLE_admin", "ROLE_operator"})
public class CrudSessionsView extends Div implements BeforeEnterObserver {

	private static final long serialVersionUID = 1L;
	private final String FILM_ID = "SessionID";
	private final String FILM_EDIT_ROUTE_TEMPLATE = "crudsessions/%d/edit";

	private Grid<Session> grid = new Grid<>(Session.class, false);

	private DateTimePicker date_time;
	private ComboBox<Film> film;
	private ComboBox<Room> room;

	private Button cancel = new Button("Cancelar");
	private Button save = new Button("Guardar");
	private Button delete = new Button("Eliminar");

	private BeanValidationBinder<Session> binder;

	private Session session;

	private SessionService sessionService;
	private FilmService filmService;
	private RoomService roomService;
	
	@SuppressWarnings("deprecation")
	public CrudSessionsView(@Autowired SessionService sessionService, FilmService filmService, RoomService roomService) {
		this.sessionService = sessionService;
		this.filmService = filmService;
		this.roomService = roomService;
		addClassNames("crud-sessions-view-view", "flex", "flex-col", "h-full");
		// Create UI
		SplitLayout splitLayout = new SplitLayout();
		splitLayout.setSizeFull();

		createGridLayout(splitLayout);
		createEditorLayout(splitLayout);

		add(splitLayout);

		// Configure Grid
		grid.addColumn("date_time").setAutoWidth(true).setHeader("Fecha y hora");
		grid.addColumn(session -> {return session.getFilm().getName();}).setAutoWidth(true).setHeader("Película");
		grid.addColumn(session -> {return session.getRoom().getNum_room();}).setAutoWidth(true).setHeader("Sala");
	
		grid.setDataProvider(new CrudServiceDataProvider<>(sessionService));
		grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
		grid.setHeightFull();

		// when a row is selected or deselected
		grid.asSingleSelect().addValueChangeListener(event -> {
			if (event.getValue() != null) {
				UI.getCurrent().navigate(String.format(FILM_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
				delete.setVisible(true);
			} else {
				clearForm();
				UI.getCurrent().navigate(CrudSessionsView.class);
				delete.setVisible(false);
			}
		});
		
		// Configure Form
		binder = new BeanValidationBinder<>(Session.class);
		binder.bindInstanceFields(this);

		cancel.addClickListener(e -> {
			clearForm();
			refreshGrid();
		});
		
		save.addClickListener(e -> {
			try {
				if (this.session == null) {
					this.session = new Session();
				} 
				if (date_time.isEmpty()) {
			        Notification.show("Introduce una fecha y una hora");
			    } else if (film.isEmpty()) {
			        Notification.show("Introduce una película");
			    } else if (room.isEmpty()) {
			        Notification.show("Introduce una sala");
			    } else {
			    	//Session session_exists = sessionService.loadSessionByName(date_time.getValue());
			        //if(session_exists.getName() == null) {
						binder.writeBean(this.session);
						sessionService.update(this.session);
						clearForm();
						refreshGrid();
		
						Notification.show("Sesión guardada correctamente.");
		
						UI.getCurrent().navigate(CrudSessionsView.class);
		        	//}
		        	//else {
		            //    Notification.show("Session ya registrada."); 
		        	//}
			    }
			} catch (ValidationException validationException) {
				Notification.show("Ocurrió un error al guardar los datos de la sesión.");
			}
		});

		delete.addClickListener(e -> {
			try {
				binder.getBean();
				sessionService.delete(this.session.getId());

				clearForm();
				refreshGrid();
				
				Notification.show("Sala eliminado correctamente.");
				
				UI.getCurrent().navigate(CrudSessionsView.class);
			} catch (Exception exception) {
				Notification.show("Ocurrió un error al borrar los datos de la sesión.");
			}
		});
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		Optional<Integer> SessionId = event.getRouteParameters().getInteger(FILM_ID);
		if (SessionId.isPresent()) {
			Optional<Session> SessionFromBackend = sessionService.get(SessionId.get());
			if (SessionFromBackend.isPresent()) {
				populateForm(SessionFromBackend.get());
			} else {
				Notification.show("No se pudo encontrar esa sesión");

				// when a row is selected but the data is no longer available
				refreshGrid();
				event.forwardTo(CrudSessionsView.class);
			}
		}
	}

	private void createEditorLayout(SplitLayout splitLayout) {
		Div editorLayoutDiv = new Div();
		editorLayoutDiv.setClassName("flex flex-col");
		editorLayoutDiv.setWidth("400px");

		Div editorDiv = new Div();
		editorDiv.setClassName("p-l flex-grow");
		editorLayoutDiv.add(editorDiv);

		FormLayout formLayout = new FormLayout();
		date_time = new DateTimePicker("Fecha y hora");
		film = new ComboBox<Film>("Película");
		film.setItems(filmService.findAll()); // list/set of possible cities.
		film.setItemLabelGenerator(film -> film.getName());
		room = new ComboBox<Room>("Sala");
		room.setItems(roomService.findAll()); // list/set of possible cities.
		room.setItemLabelGenerator(room -> "Sala " + room.getNum_room().toString() + " del cine " + room.getBusiness().getName());
		Component[] fields = new Component[] { date_time, film, room };

		for (Component field : fields) {
			((HasStyle) field).addClassName("full-width");
		}
		formLayout.add(fields);
		editorDiv.add(formLayout);
		createButtonLayout(editorLayoutDiv);

		splitLayout.addToSecondary(editorLayoutDiv);
	}

	private void createButtonLayout(Div editorLayoutDiv) {
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setClassName("w-full flex-wrap bg-contrast-5 py-s px-l");
		buttonLayout.setSpacing(true);

		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		delete.getStyle().set("margin-inline-start", "auto");
		delete.setVisible(false);

		buttonLayout.add(save, cancel, delete);
		editorLayoutDiv.add(buttonLayout);
	}

	private void createGridLayout(SplitLayout splitLayout) {
		Div wrapper = new Div();
		wrapper.setId("grid-wrapper");
		wrapper.setWidthFull();
		splitLayout.addToPrimary(wrapper);
		wrapper.add(grid);
	}

	private void refreshGrid() {
		grid.select(null);
		grid.getDataProvider().refreshAll();
	}

	private void clearForm() {
		populateForm(null);
	}

	private void populateForm(Session value) {
		this.session = value;
		binder.readBean(this.session);
	}
}