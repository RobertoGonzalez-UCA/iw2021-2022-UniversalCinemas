package com.universalcinemas.application.views.crudplans;

import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.artur.helpers.CrudServiceDataProvider;

import com.universalcinemas.application.data.plan.Plan;
import com.universalcinemas.application.data.plan.PlanService;
import com.universalcinemas.application.data.genre.Genre;
import com.universalcinemas.application.data.genre.GenreService;
import com.universalcinemas.application.views.MainLayout;
import com.universalcinemas.application.views.crudplans.CrudPlansView;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Panel plans")
@Route(value = "crudplans/:PlanID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("ROLE_admin")
public class CrudPlansView extends Div implements BeforeEnterObserver {

	private static final long serialVersionUID = 1L;
	private final String FILM_ID = "PlanID";
	private final String FILM_EDIT_ROUTE_TEMPLATE = "crudplans/%d/edit";

	private Grid<Plan> grid = new Grid<>(Plan.class, false);

	private TextField name;
	private TextField description;
	private IntegerField percent;
	private IntegerField price;
	private ComboBox<Genre> genre;

	private Button cancel = new Button("Cancelar");
	private Button save = new Button("Guardar");
	private Button delete = new Button("Eliminar");

	private BeanValidationBinder<Plan> binder;

	private Plan plan;

	private PlanService planService;
	private GenreService genreService;
	
	@SuppressWarnings("deprecation")
	public CrudPlansView(@Autowired PlanService planService, GenreService genreService) {
		this.planService = planService;
		this.genreService = genreService;
		addClassNames("crud-plans-view-view", "flex", "flex-col", "h-full");
		// Create UI
		SplitLayout splitLayout = new SplitLayout();
		splitLayout.setSizeFull();

		createGridLayout(splitLayout);
		createEditorLayout(splitLayout);

		add(splitLayout);

		// Configure Grid
		grid.addColumn("name").setAutoWidth(true).setHeader("Nombre");
		grid.addColumn("description").setAutoWidth(true).setHeader("Descripción");
		grid.addColumn("percent").setAutoWidth(true).setHeader("Porcentaje de descuento");
		grid.addColumn("price").setAutoWidth(true).setHeader("Precio");
		grid.addColumn(plan -> {return plan.getGenre().getName();}).setAutoWidth(true).setHeader("Género");
		
		grid.setDataProvider(new CrudServiceDataProvider<>(planService));
		grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
		grid.setHeightFull();

		// when a row is selected or deselected
		grid.asSingleSelect().addValueChangeListener(event -> {
			if (event.getValue() != null) {
				UI.getCurrent().navigate(String.format(FILM_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
				delete.setVisible(true);
			} else {
				clearForm();
				UI.getCurrent().navigate(CrudPlansView.class);
				delete.setVisible(false);
			}
		});
		
		// Configure Form
		binder = new BeanValidationBinder<>(Plan.class);
		binder.bindInstanceFields(this);

		cancel.addClickListener(e -> {
			clearForm();
			refreshGrid();
		});
		
		save.addClickListener(e -> {
			try {
				if (this.plan == null) {
					this.plan = new Plan();
				}
				if (name.isEmpty()) {
			        Notification.show("Introduce el nombre del plan");
			    } else if (description.isEmpty()) {
			        Notification.show("Introduce la description del plan");
			    } else if (percent.isEmpty()) {
			        Notification.show("Introduce el porcentaje");
			    } else if (price.isEmpty()) {
			        Notification.show("Introduce el precio");
			    } else if (genre.isEmpty()) {
			        Notification.show("Introduce el género");
			    } else {
			    	//Plan plan_exists = planService.loadPlanByName(name.getValue());
			        //if(plan_exists.getName() == null) {
						binder.writeBean(this.plan);
						planService.update(this.plan);
						clearForm();
						refreshGrid();
		
						Notification.show("Plan guardado correctamente.");
		
						UI.getCurrent().navigate(CrudPlansView.class);
		        	//}
		        	//else {
		            //    Notification.show("Plan ya registrada."); 
		        	//}
			    }
			} catch (ValidationException validationException) {
				Notification.show("Ocurrió un error al guardar los datos del plan.");
			}
		});

		delete.addClickListener(e -> {
			try {
				binder.getBean();
				planService.delete(this.plan.getId());

				clearForm();
				refreshGrid();
				
				Notification.show("Plan eliminado correctamente.");
				
				UI.getCurrent().navigate(CrudPlansView.class);
			} catch (Exception exception) {
				Notification.show("No se puede borrar el el plan ya que hay usuarios que están suscritos a él.");
			}
		});
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		Optional<Integer> PlanId = event.getRouteParameters().getInteger(FILM_ID);
		if (PlanId.isPresent()) {
			Optional<Plan> PlanFromBackend = planService.get(PlanId.get());
			if (PlanFromBackend.isPresent()) {
				populateForm(PlanFromBackend.get());
			} else {
				Notification.show("No se pudo encontrar ese plan");

				// when a row is selected but the data is no longer available
				refreshGrid();
				event.forwardTo(CrudPlansView.class);
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
		description = new TextField("Descripción");
		genre = new ComboBox<Genre>("Género");
		genre.setItems(genreService.findAll()); // list/set of possible cities.
		genre.setItemLabelGenerator(genre -> genre.getName());
		percent = new IntegerField("Porcentaje");
		price = new IntegerField("Precio");
		Component[] fields = new Component[] { name, description, percent, price, genre };

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

	private void populateForm(Plan value) {
		this.plan = value;
		binder.readBean(this.plan);
	}
}
