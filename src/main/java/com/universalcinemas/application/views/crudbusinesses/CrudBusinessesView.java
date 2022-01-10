package com.universalcinemas.application.views.crudbusinesses;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.UriUtils;
import org.vaadin.artur.helpers.CrudServiceDataProvider;

import com.universalcinemas.application.data.business.Business;
import com.universalcinemas.application.data.business.BusinessService;
import com.universalcinemas.application.data.city.City;
import com.universalcinemas.application.data.city.CityService;
import com.universalcinemas.application.views.MainLayout;
import com.universalcinemas.application.views.crudbusinesses.CrudBusinessesView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import elemental.json.Json;

@PageTitle("Panel negocios")
@Route(value = "crudbusinesses/:BusinessID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("ROLE_admin")
public class CrudBusinessesView extends Div implements BeforeEnterObserver {

	private static final long serialVersionUID = 1L;
	private final String FILM_ID = "BusinessID";
	private final String FILM_EDIT_ROUTE_TEMPLATE = "crudbusinesses/%d/edit";

	private Grid<Business> grid = new Grid<>(Business.class, false);

	private TextField name;
	private TextField street;
	private ComboBox<City> city;

	private Button cancel = new Button("Cancelar");
	private Button save = new Button("Guardar");
	private Button delete = new Button("Eliminar");

	private BeanValidationBinder<Business> binder;

	private Business business;

	private BusinessService businessService;
	private CityService cityService;
	
	@SuppressWarnings("deprecation")
	public CrudBusinessesView(@Autowired BusinessService businessService, CityService cityService) {
		this.businessService = businessService;
		this.cityService = cityService;
		addClassNames("crud-businesses-view-view", "flex", "flex-col", "h-full");
		// Create UI
		SplitLayout splitLayout = new SplitLayout();
		splitLayout.setSizeFull();

		createGridLayout(splitLayout);
		createEditorLayout(splitLayout);

		add(splitLayout);

		// Configure Grid
		grid.addColumn("name").setAutoWidth(true).setHeader("Nombre");
		grid.addColumn("street").setAutoWidth(true).setHeader("Calle");
		grid.addColumn(business -> {return business.getCity().getName();}).setAutoWidth(true).setHeader("Ciudad");
		
		grid.setDataProvider(new CrudServiceDataProvider<>(businessService));
		grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
		grid.setHeightFull();

		// when a row is selected or deselected
		grid.asSingleSelect().addValueChangeListener(event -> {
			if (event.getValue() != null) {
				UI.getCurrent().navigate(String.format(FILM_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
				delete.setVisible(true);
			} else {
				clearForm();
				UI.getCurrent().navigate(CrudBusinessesView.class);
				delete.setVisible(false);
			}
		});
		
		// Configure Form
		binder = new BeanValidationBinder<>(Business.class);
		binder.bindInstanceFields(this);

		cancel.addClickListener(e -> {
			clearForm();
			refreshGrid();
		});
		
		save.addClickListener(e -> {
			try {
				if (this.business == null) {
					this.business = new Business();
				}
				if (name.isEmpty()) {
			        Notification.show("Introduce el nombre del negocio");
			    } else if (street.isEmpty()) {
			        Notification.show("Introduce el street del negocio");
			    } else if (city.isEmpty()) {
			        Notification.show("Introduce la ciudad");
			    } else {
			    	//Business business_exists = businessService.loadBusinessByName(name.getValue());
			        //if(business_exists.getName() == null) {
						binder.writeBean(this.business);
						businessService.update(this.business);
						clearForm();
						refreshGrid();
		
						Notification.show("Negocio guardado correctamente.");
		
						UI.getCurrent().navigate(CrudBusinessesView.class);
		        	//}
		        	//else {
		            //    Notification.show("Negocio ya registrada."); 
		        	//}
			    }
			} catch (ValidationException validationException) {
				Notification.show("Ocurrió un error al guardar los datos del negocio.");
			}
		});

		delete.addClickListener(e -> {
			try {
				binder.getBean();
				businessService.delete(this.business.getId());

				clearForm();
				refreshGrid();
				
				Notification.show("Negocio eliminado correctamente.");
				
				UI.getCurrent().navigate(CrudBusinessesView.class);
			} catch (Exception exception) {
				Notification.show("Ocurrió un error al borrar los datos del negocio.");
			}
		});
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		Optional<Integer> BusinessId = event.getRouteParameters().getInteger(FILM_ID);
		if (BusinessId.isPresent()) {
			Optional<Business> BusinessFromBackend = businessService.get(BusinessId.get());
			if (BusinessFromBackend.isPresent()) {
				populateForm(BusinessFromBackend.get());
			} else {
				Notification.show("No se pudo encontrar ese negocio");

				// when a row is selected but the data is no longer available
				refreshGrid();
				event.forwardTo(CrudBusinessesView.class);
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
		name = new TextField("Nombre");
		street = new TextField("Calle");
		city = new ComboBox<City>("Ciudad");
		city.setItems(cityService.findAll()); // list/set of possible cities.
		city.setItemLabelGenerator(city -> city.getName());
		Component[] fields = new Component[] { name, street, city };

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

	private void populateForm(Business value) {
		this.business = value;
		binder.readBean(this.business);
	}
}
