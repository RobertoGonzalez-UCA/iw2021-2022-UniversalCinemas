package com.universalcinemas.application.views.crudfilms;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.UriUtils;
import org.vaadin.artur.helpers.CrudServiceDataProvider;

import com.universalcinemas.application.data.film.Film;
import com.universalcinemas.application.data.film.FilmService;
import com.universalcinemas.application.data.genre.Genre;
import com.universalcinemas.application.data.genre.GenreService;
import com.universalcinemas.application.views.MainLayout;
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

@PageTitle("Panel pel??culas")
@Route(value = "crudfilms/:FilmID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed({"ROLE_admin", "ROLE_operator"})
public class CrudFilmsView extends Div implements BeforeEnterObserver {

	private static final long serialVersionUID = 1L;
	private final String FILM_ID = "FilmID";
	private final String FILM_EDIT_ROUTE_TEMPLATE = "crudfilms/%d/edit";

	private Grid<Film> grid = new Grid<>(Film.class, false);

	private TextField name;
	private TextField director;
	private TextField synopsis;
	private DatePicker releasedate;
	private IntegerField agerating;
	private NumberField rating;
	private TextField filmposter;
	private ComboBox<Genre> genre;

	private Button cancel = new Button("Cancelar");
	private Button save = new Button("Guardar");
	private Button delete = new Button("Eliminar");

	private BeanValidationBinder<Film> binder;

	private Film film;

	private FilmService filmService;
	private GenreService genreService;

	@SuppressWarnings("deprecation")
	public CrudFilmsView(@Autowired FilmService filmService, GenreService genreService) {
		this.filmService = filmService;
		this.genreService = genreService;
		addClassNames("crud-films-view-view", "flex", "flex-col", "h-full");
		// Create UI
		SplitLayout splitLayout = new SplitLayout();
		splitLayout.setSizeFull();

		createGridLayout(splitLayout);
		createEditorLayout(splitLayout);

		add(splitLayout);

		// Configure Grid
		grid.addColumn("name").setAutoWidth(true).setHeader("Nombre");
		grid.addColumn("director").setAutoWidth(true).setHeader("Director");
		grid.addColumn("synopsis").setAutoWidth(true).setHeader("Sinopsis");
		grid.addColumn("releasedate").setAutoWidth(true).setHeader("Fecha de estreno");
		grid.addColumn("agerating").setAutoWidth(true).setHeader("Edad m??nima");
		grid.addColumn("rating").setAutoWidth(true).setHeader("Puntuaci??n");
		grid.addColumn(film -> {return film.getGenre().getName();}).setAutoWidth(true).setHeader("G??nero");
		TemplateRenderer<Film> filmposterRenderer = TemplateRenderer.<Film>of(
				"<span style='border-radius: 50%; overflow: hidden; display: flex; align-items: center; justify-content: center; width: 64px; height: 64px'><img style='max-width: 100%' src='[[item.filmposter]]' /></span>")
				.withProperty("filmposter", Film::getFilmposter);
		grid.addColumn(filmposterRenderer).setHeader("P??ster de la pel??cula").setWidth("96px").setFlexGrow(0);
		grid.addColumn("filmposter").setAutoWidth(true).setHeader("URL del p??ster");

		grid.setDataProvider(new CrudServiceDataProvider<>(filmService));
		grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
		grid.setHeightFull();

		// when a row is selected or deselected
		grid.asSingleSelect().addValueChangeListener(event -> {
			if (event.getValue() != null) {
				UI.getCurrent().navigate(String.format(FILM_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
				delete.setVisible(true);
			} else {
				clearForm();
				UI.getCurrent().navigate(CrudFilmsView.class);
				delete.setVisible(false);
			}
		});
		
		// Configure Form
		binder = new BeanValidationBinder<>(Film.class);
		binder.bindInstanceFields(this);

		cancel.addClickListener(e -> {
			clearForm();
			refreshGrid();
		});
		
		save.addClickListener(e -> {
			try {
				if (this.film == null) {
					this.film = new Film();
				}
				if (name.isEmpty()) {
			        Notification.show("Introduce el nombre de la pel??cula");
			    } else if (director.isEmpty()) {
			        Notification.show("Introduce el director de la pel??cula");
			    } else if (synopsis.isEmpty()) {
			    	Notification.show("Introduce una sinopsis de la pel??cula");
			    } else if (releasedate.getValue() == null) {
			        Notification.show("Introduce la fecha en la que se estren?? la pel??cula");
			    } else if (agerating.isEmpty()) {
			        Notification.show("Introduce la edad m??nima");
			    } else if (rating.isEmpty()) {
			        Notification.show("Introduce la puntuaci??n de la pel??cula");
				} else if (genre.isEmpty()) {
			        Notification.show("Introduce el g??nero de la pel??cula");
				} else if (filmposter.isEmpty()) {
					Notification.show("Introduce la URL del p??ster");
				} else {
			    	//Film film_exists = filmService.loadFilmByName(name.getValue());
			        //if(film_exists.getName() == null) {
						binder.writeBean(this.film);
						filmService.update(this.film);
						clearForm();
						refreshGrid();
		
						Notification.show("Pel??cula guardada correctamente.");
		
						UI.getCurrent().navigate(CrudFilmsView.class);
		        	//}
		        	//else {
		            //    Notification.show("Pel??cula ya registrada."); 
		        	//}
			    }
			} catch (ValidationException validationException) {
				Notification.show("Ocurri?? un error al guardar los datos de la pel??cula.");
			}
		});

		delete.addClickListener(e -> {
			try {
				binder.getBean();
				filmService.delete(this.film.getId());

				clearForm();
				refreshGrid();
				
				Notification.show("Pel??cula eliminada correctamente.");
				
				UI.getCurrent().navigate(CrudFilmsView.class);
			} catch (Exception exception) {
				Notification.show("No se puede borrar la pel??cula ya que hay sesiones que est??n asociadas a ella.");
			}
		});
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		Optional<Integer> FilmId = event.getRouteParameters().getInteger(FILM_ID);
		if (FilmId.isPresent()) {
			Optional<Film> FilmFromBackend = filmService.get(FilmId.get());
			if (FilmFromBackend.isPresent()) {
				populateForm(FilmFromBackend.get());
			} else {
				Notification.show("No se pudo encontrar esa pel??cula");

				// when a row is selected but the data is no longer available
				refreshGrid();
				event.forwardTo(CrudFilmsView.class);
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
		director = new TextField("Director");
		synopsis = new TextField("Sinopsis");
		releasedate = new DatePicker("Fecha de salida");
		agerating = new IntegerField("Edad m??nima");
		rating = new NumberField("Puntuaci??n");
		genre = new ComboBox<Genre>("G??nero");
		genre.setItems(genreService.findAll()); // list/set of possible cities.
		genre.setItemLabelGenerator(genre -> genre.getName() + " " + genre.getId());
		filmposter = new TextField("URL del p??ster");
		Component[] fields = new Component[] { name, director, synopsis, releasedate, agerating, rating, genre, filmposter };

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

	private void populateForm(Film value) {
		this.film = value;
		binder.readBean(this.film);
	}
}