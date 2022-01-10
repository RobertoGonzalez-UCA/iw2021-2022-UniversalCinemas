package com.universalcinemas.application.views.crudusers;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.UriUtils;
import org.vaadin.artur.helpers.CrudServiceDataProvider;

import com.universalcinemas.application.data.user.User;
import com.universalcinemas.application.data.user.UserService;
import com.universalcinemas.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
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

@PageTitle("Panel usuarios")
@Route(value = "crudusers/:UserID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("ROLE_admin")
public class CrudUsersView extends Div implements BeforeEnterObserver {

	private final String USER_ID = "UserID";
	private final String USER_EDIT_ROUTE_TEMPLATE = "crudusers/%d/edit";

	private Grid<User> grid = new Grid<>(User.class, false);

	private TextField name;
	private TextField surname;
	private TextField email;
	private DatePicker dateofbirth;
	private TextField phonenumber;
	private Upload urlprofileimage;
	private Image urlprofileimagePreview;

	private Button cancel = new Button("Cancelar");
	private Button save = new Button("Guardar");
	private Button delete = new Button("Eliminar");

	private BeanValidationBinder<User> binder;

	private User user;

	private UserService UserService;

	public CrudUsersView(@Autowired UserService UserService) {
		this.UserService = UserService;
		addClassNames("crud-users-view-view", "flex", "flex-col", "h-full");
		// Create UI
		SplitLayout splitLayout = new SplitLayout();
		splitLayout.setSizeFull();

		createGridLayout(splitLayout);
		createEditorLayout(splitLayout);

		add(splitLayout);

		// Configure Grid
		grid.addColumn("name").setAutoWidth(true).setHeader("Nombre");
		grid.addColumn("surname").setAutoWidth(true).setHeader("Apellidos");
		grid.addColumn("email").setAutoWidth(true);
		grid.addColumn("dateofbirth").setAutoWidth(true).setHeader("Fecha de nacimiento");
		grid.addColumn("phonenumber").setAutoWidth(true).setHeader("Teléfono");
		TemplateRenderer<User> urlprofileimageRenderer = TemplateRenderer.<User>of(
				"<span style='border-radius: 50%; overflow: hidden; display: flex; align-items: center; justify-content: center; width: 64px; height: 64px'><img style='max-width: 100%' src='[[item.urlprofileimage]]' /></span>")
				.withProperty("urlprofileimage", User::getUrlprofileimage);
		grid.addColumn(urlprofileimageRenderer).setHeader("Imagen de perfil").setWidth("96px").setFlexGrow(0);

		grid.setDataProvider(new CrudServiceDataProvider<>(UserService));
		grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
		grid.setHeightFull();

		// when a row is selected or deselected
		grid.asSingleSelect().addValueChangeListener(event -> {
			if (event.getValue() != null) {
				UI.getCurrent().navigate(String.format(USER_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
				delete.setVisible(true);
			} else {
				clearForm();
				UI.getCurrent().navigate(CrudUsersView.class);
				delete.setVisible(false);
			}
		});

		// Configure Form
		binder = new BeanValidationBinder<>(User.class);
		binder.bindInstanceFields(this);

		attachImageUpload(urlprofileimage, urlprofileimagePreview);

		cancel.addClickListener(e -> {
			clearForm();
			refreshGrid();
		});

		save.addClickListener(e -> {
			try {
				if (this.user == null) {
					this.user = new User();
				}
				if (name.isEmpty()) {
			        Notification.show("Introduce tu nombre");
			    } else if (surname.isEmpty()) {
			        Notification.show("Introduce tus apellidos");
			    } else if (dateofbirth.getValue() == null) {
			        Notification.show("Introduce tu fecha de nacimiento");
			    } else if (email.isEmpty()) {
			    	Notification.show("Introduce tu email");
			    } else if (phonenumber.isEmpty()) {
			        Notification.show("Introduce tu teléfono");
			    } else {
			    	User user_exists = UserService.loadUserByEmail(email.getValue());
			        if(user_exists.getEmail() == null) {
						binder.writeBean(this.user);
						this.user.setUrlprofileimage(urlprofileimagePreview.getSrc());
						UserService.update(this.user);
						clearForm();
						refreshGrid();
		
						Notification.show("Usuario guardado correctamente.");
		
						UI.getCurrent().navigate(CrudUsersView.class);
		        	}
		        	else {
		                Notification.show("Usuario ya registrado."); 
		        	}
			    }
			} catch (ValidationException validationException) {
				Notification.show("Ocurrió un error al guardar los datos del usuario.");
			}
		});

		delete.addClickListener(e -> {
			try {
				binder.getBean();
				UserService.delete(this.user.getId());

				clearForm();
				refreshGrid();
				
				Notification.show("Usuario eliminado correctamente.");
				
				UI.getCurrent().navigate(CrudUsersView.class);
			} catch (Exception exception) {
				Notification.show("Ocurrió un error al borrar los datos del usuario.");
			}
		});
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		Optional<Integer> UserId = event.getRouteParameters().getInteger(USER_ID);
		if (UserId.isPresent()) {
			Optional<User> UserFromBackend = UserService.get(UserId.get());
			if (UserFromBackend.isPresent()) {
				populateForm(UserFromBackend.get());
			} else {
				Notification.show("No se pudo encontrar ese usuario");

				// when a row is selected but the data is no longer available
				refreshGrid();
				event.forwardTo(CrudUsersView.class);
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
		surname = new TextField("Apellidos");
		email = new TextField("Email");
		dateofbirth = new DatePicker("Fecha de nacimiento");
		phonenumber = new TextField("Teléfono");
		Label urlprofileimageLabel = new Label("Imagen de perfil");
		urlprofileimagePreview = new Image();
		urlprofileimagePreview.setWidth("100%");
		urlprofileimage = new Upload();
		urlprofileimage.getStyle().set("box-sizing", "border-box");
		urlprofileimage.getElement().appendChild(urlprofileimagePreview.getElement());
		Component[] fields = new Component[] { name, surname, email, dateofbirth, phonenumber, urlprofileimageLabel,
				urlprofileimage };

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

	private void populateForm(User value) {
		this.user = value;
		binder.readBean(this.user);
		this.urlprofileimagePreview.setVisible(value != null);
		if (value == null) {
			this.urlprofileimagePreview.setSrc("");
		} else {
			this.urlprofileimagePreview.setSrc(value.getUrlprofileimage());
		}

	}
}