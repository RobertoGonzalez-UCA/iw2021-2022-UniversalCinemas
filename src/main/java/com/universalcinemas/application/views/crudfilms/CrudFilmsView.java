package com.universalcinemas.application.views.crudfilms;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.UriUtils;
import org.vaadin.artur.helpers.CrudServiceDataProvider;

import com.universalcinemas.application.data.city.City;
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

@PageTitle("Panel películas")
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
	private Upload filmposter;
	private Image filmposterPreview;
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
		grid.addColumn("agerating").setAutoWidth(true).setHeader("Edad mínima");
		grid.addColumn("rating").setAutoWidth(true).setHeader("Puntuación");
		grid.addColumn(film -> {return film.getGenre().getName();}).setAutoWidth(true).setHeader("Género");
		TemplateRenderer<Film> filmposterRenderer = TemplateRenderer.<Film>of(
				"<span style='border-radius: 50%; overflow: hidden; display: flex; align-items: center; justify-content: center; width: 64px; height: 64px'><img style='max-width: 100%' src='[[item.filmposter]]' /></span>")
				.withProperty("filmposter", Film::getFilmposter);
		grid.addColumn(filmposterRenderer).setHeader("Póster de la película").setWidth("96px").setFlexGrow(0);

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

		attachImageUpload(filmposter, filmposterPreview);

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
			        Notification.show("Introduce el nombre de la película");
			    } else if (director.isEmpty()) {
			        Notification.show("Introduce el director de la película");
			    } else if (synopsis.isEmpty()) {
			    	Notification.show("Introduce una sinopsis de la película");
			    } else if (releasedate.getValue() == null) {
			        Notification.show("Introduce la fecha en la que se estrenó la película");
			    } else if (agerating.isEmpty()) {
			        Notification.show("Introduce la edad mínima");
			    } else if (rating.isEmpty()) {
			        Notification.show("Introduce la puntuación de la película");
				} else if (genre.isEmpty()) {
			        Notification.show("Introduce el género de la película");
				}
				else {
			    	//Film film_exists = filmService.loadFilmByName(name.getValue());
			        //if(film_exists.getName() == null) {
						binder.writeBean(this.film);
						this.film.setFilmposter(filmposterPreview.getSrc());
						filmService.update(this.film);
						clearForm();
						refreshGrid();
		
						Notification.show("Película guardada correctamente.");
		
						UI.getCurrent().navigate(CrudFilmsView.class);
		        	//}
		        	//else {
		            //    Notification.show("Película ya registrada."); 
		        	//}
			    }
			} catch (ValidationException validationException) {
				Notification.show("Ocurrió un error al guardar los datos de la película.");
			}
		});

		delete.addClickListener(e -> {
			try {
				binder.getBean();
				filmService.delete(this.film.getId());

				clearForm();
				refreshGrid();
				
				Notification.show("Película eliminada correctamente.");
				
				UI.getCurrent().navigate(CrudFilmsView.class);
			} catch (Exception exception) {
				Notification.show("Ocurrió un error al borrar los datos de la película.");
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
				Notification.show("No se pudo encontrar esa película");

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
		agerating = new IntegerField("Edad mínima");
		rating = new NumberField("Puntuación");
		genre = new ComboBox<Genre>("Género");
		genre.setItems(genreService.findAll()); // list/set of possible cities.
		genre.setItemLabelGenerator(genre -> genre.getName() + " " + genre.getId());
		Label filmposterLabel = new Label("Póster de la película");
		filmposterPreview = new Image();
		filmposterPreview.setWidth("100%");
		filmposter = new Upload();
		filmposter.getStyle().set("box-sizing", "border-box");
		filmposter.getElement().appendChild(filmposterPreview.getElement());
		Component[] fields = new Component[] { name, director, synopsis, releasedate, agerating, rating, genre, filmposterLabel,
				filmposter };

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

	private void attachImageUpload(Upload upload, Image preview) {
		ByteArrayOutputStream uploadBuffer = new ByteArrayOutputStream();
		upload.setAcceptedFileTypes("image/*");
		upload.setReceiver((fileName, mimeType) -> {
			return uploadBuffer;
		});
		upload.addSucceededListener(e -> {
			String mimeType = e.getMIMEType();
			String base64ImageData = Base64.getEncoder().encodeToString(uploadBuffer.toByteArray());
			String dataUrl = "data:" + mimeType + ";base64,"
					+ UriUtils.encodeQuery(base64ImageData, StandardCharsets.UTF_8);
			upload.getElement().setPropertyJson("files", Json.createArray());
			preview.setSrc(dataUrl);
			uploadBuffer.reset();
		});
		preview.setVisible(false);
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
		this.filmposterPreview.setVisible(value != null);
		if (value == null) {
			this.filmposterPreview.setSrc("");
		} else {
			this.filmposterPreview.setSrc(value.getFilmposter());
		}

	}
}