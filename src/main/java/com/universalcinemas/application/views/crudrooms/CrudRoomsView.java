package com.universalcinemas.application.views.crudrooms;

import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.artur.helpers.CrudServiceDataProvider;

import com.universalcinemas.application.data.business.Business;
import com.universalcinemas.application.data.business.BusinessService;
import com.universalcinemas.application.data.room.Room;
import com.universalcinemas.application.data.room.RoomService;
import com.universalcinemas.application.views.MainLayout;
import com.universalcinemas.application.views.crudrooms.CrudRoomsView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Panel rooms")
@Route(value = "crudrooms/:RoomID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed({"ROLE_admin", "ROLE_operator"})
public class CrudRoomsView extends Div implements BeforeEnterObserver {

	private static final long serialVersionUID = 1L;
	private final String FILM_ID = "RoomID";
	private final String FILM_EDIT_ROUTE_TEMPLATE = "crudrooms/%d/edit";

	private Grid<Room> grid = new Grid<>(Room.class, false);

	private IntegerField num_rows;
	private IntegerField num_columns;
	private IntegerField num_room;
	private ComboBox<Business> business;

	private Button cancel = new Button("Cancelar");
	private Button save = new Button("Guardar");
	private Button delete = new Button("Eliminar");

	private BeanValidationBinder<Room> binder;

	private Room room;

	private RoomService roomService;
	private BusinessService businessService;
	
	@SuppressWarnings("deprecation")
	public CrudRoomsView(@Autowired RoomService roomService, BusinessService businessService) {
		this.roomService = roomService;
		this.businessService = businessService;
		addClassNames("crud-rooms-view-view", "flex", "flex-col", "h-full");
		// Create UI
		SplitLayout splitLayout = new SplitLayout();
		splitLayout.setSizeFull();

		createGridLayout(splitLayout);
		createEditorLayout(splitLayout);

		add(splitLayout);

		// Configure Grid
		grid.addColumn("num_room").setAutoWidth(true).setHeader("Número de la sala");
		grid.addColumn("num_rows").setAutoWidth(true).setHeader("Número de filas");
		grid.addColumn("num_columns").setAutoWidth(true).setHeader("Número de columnas");
		grid.addColumn(room -> {return room.getBusiness().getName();}).setAutoWidth(true).setHeader("Negocio");
		
		grid.setDataProvider(new CrudServiceDataProvider<>(roomService));
		grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
		grid.setHeightFull();

		// when a row is selected or deselected
		grid.asSingleSelect().addValueChangeListener(event -> {
			if (event.getValue() != null) {
				UI.getCurrent().navigate(String.format(FILM_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
				delete.setVisible(true);
			} else {
				clearForm();
				UI.getCurrent().navigate(CrudRoomsView.class);
				delete.setVisible(false);
			}
		});
		
		// Configure Form
		binder = new BeanValidationBinder<>(Room.class);
		binder.bindInstanceFields(this);

		cancel.addClickListener(e -> {
			clearForm();
			refreshGrid();
		});
		
		save.addClickListener(e -> {
			try {
				if (this.room == null) {
					this.room = new Room();
				} 
				if (num_room.isEmpty()) {
			        Notification.show("Introduce el número de la sala");
			    } else if (num_rows.isEmpty()) {
			        Notification.show("Introduce el número de filas de la sala");
			    } else if (num_columns.isEmpty()) {
			        Notification.show("Introduce el número de columnas de la sala");
			    } else if (business.isEmpty()) {
			        Notification.show("Introduce el negocio");
			    } else {
			    	//Room room_exists = roomService.loadRoomByName(num_rows.getValue());
			        //if(room_exists.getName() == null) {
						binder.writeBean(this.room);
						roomService.update(this.room);
						clearForm();
						refreshGrid();
		
						Notification.show("Sala guardado correctamente.");
		
						UI.getCurrent().navigate(CrudRoomsView.class);
		        	//}
		        	//else {
		            //    Notification.show("Room ya registrada."); 
		        	//}
			    }
			} catch (ValidationException validationException) {
				Notification.show("Ocurrió un error al guardar los datos de la sala.");
			}
		});

		delete.addClickListener(e -> {
			try {
				binder.getBean();
				roomService.delete(this.room.getId());

				clearForm();
				refreshGrid();
				
				Notification.show("Sala eliminado correctamente.");
				
				UI.getCurrent().navigate(CrudRoomsView.class);
			} catch (Exception exception) {
				Notification.show("Ocurrió un error al borrar los datos de la sala.");
			}
		});
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		Optional<Integer> RoomId = event.getRouteParameters().getInteger(FILM_ID);
		if (RoomId.isPresent()) {
			Optional<Room> RoomFromBackend = roomService.get(RoomId.get());
			if (RoomFromBackend.isPresent()) {
				populateForm(RoomFromBackend.get());
			} else {
				Notification.show("No se pudo encontrar esa sala");

				// when a row is selected but the data is no longer available
				refreshGrid();
				event.forwardTo(CrudRoomsView.class);
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
		num_room = new IntegerField("Número de la sala");
		num_rows = new IntegerField("Número de filas");
		num_columns = new IntegerField("Número de columnas");
		business = new ComboBox<Business>("Negocio");
		business.setItems(businessService.findAll()); // list/set of possible cities.
		business.setItemLabelGenerator(business -> business.getName() + " " + business.getId());
		Component[] fields = new Component[] { num_room, num_columns, num_rows, business };

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

	private void populateForm(Room value) {
		this.room = value;
		binder.readBean(this.room);
	}
}