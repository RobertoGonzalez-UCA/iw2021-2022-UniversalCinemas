package com.universalcinemas.application.views.perfilusuario;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.universalcinemas.application.data.user.User;
import com.universalcinemas.application.data.user.UserService;
import com.universalcinemas.application.security.SecurityService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Perfil")
@Route(value = "perfilusuario")
@RolesAllowed({ "ROLE_admin", "ROLE_operator", "ROLE_user" })
public class PerfilUsuarioView extends VerticalLayout {
	private static final long serialVersionUID = 1L;
	private UserService servicioUsuario;
	private SecurityService securityService;

	@Autowired
	public PerfilUsuarioView(UserService servicioUsuario, SecurityService securityService) {
		this.servicioUsuario = servicioUsuario;
		this.securityService = securityService;

		User currentUser = securityService.getAuthenticatedUser().get();

		VerticalLayout formularioDatos = new VerticalLayout(
				crearFormularioDatos(servicioUsuario.obtenerDatosUsuario(currentUser.getId())));
		VerticalLayout formularioLogin = new VerticalLayout(
				crearFormularioLogin(servicioUsuario.obtenerDatosUsuario(currentUser.getId())));
		HorizontalLayout formularios = new HorizontalLayout(formularioDatos, formularioLogin);
		HorizontalLayout botonLayout = new HorizontalLayout(crearBotonInicio(), crearBotonCrud(currentUser));

		add(formularios, new Hr(), botonLayout);
	}

	private FormLayout crearFormularioDatos(User usuario) {
		FormLayout formulario = new FormLayout();
		TextField nombre = new TextField("Nombre");
		TextField apellidos = new TextField("Apellidos");
		TextField telefono = new TextField("Teléfono");
		DatePicker fechaNacimiento = new DatePicker("Nacimiento");
		Button modificar = new Button("Modificar");
		Dialog dialog = new Dialog();
		VerticalLayout dialogLayout = createDialogLayout(dialog);
		dialog.add(dialogLayout);

		modificar.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

		nombre.setValue(usuario.getUsername());
		apellidos.setValue(usuario.getSurname());
		telefono.setValue(usuario.getPhone());
		fechaNacimiento.setValue(usuario.getDateOfBirth());

		nombre.addValueChangeListener(e -> modificar.setEnabled(true));
		apellidos.addValueChangeListener(e -> modificar.setEnabled(true));
		telefono.addValueChangeListener(e -> modificar.setEnabled(true));
		fechaNacimiento.addValueChangeListener(e -> modificar.setEnabled(true));

		modificar.addClickListener(e -> {
			boolean x = false;

			if (nombre.getValue().compareTo(usuario.getUsername()) != 0) {
				usuario.setName(nombre.getValue());
				x = true;
			}
			if (apellidos.getValue().compareTo(usuario.getSurname()) != 0) {
				usuario.setSurname(apellidos.getValue());
				x = true;
			}

			if (telefono.getValue().compareTo(usuario.getPhone()) != 0) {
				usuario.setPhone(telefono.getValue());
				x = true;
			}
			if (fechaNacimiento.getValue().compareTo(usuario.getDateOfBirth()) != 0) {
				usuario.setDateOfBirth(fechaNacimiento.getValue());
				x = true;
			}
			if (x) {
				servicioUsuario.actualizarUsuario(usuario);
				dialog.open();
			}

		});

		formulario.add(nombre, apellidos, fechaNacimiento, telefono, modificar);
		formulario.setResponsiveSteps(new ResponsiveStep("0", 1), new ResponsiveStep("500px", 3));
		formulario.setColspan(nombre, 1);
		formulario.setColspan(apellidos, 2);

		return formulario;
	}

	private FormLayout crearFormularioLogin(User usuario) {
		FormLayout formulario = new FormLayout();
		EmailField email = new EmailField("Correo electrónico");
		PasswordField newPassword = new PasswordField("Contraseña para modificar correo electrónico");
		Button passButton = new Button("Modificar");
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		Dialog dialog = new Dialog();

		VerticalLayout dialogLayout = createDialogLayout(dialog);
		dialog.add(dialogLayout);

		passButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

		email.setValue(usuario.getEmail());

		email.addValueChangeListener(e -> passButton.setEnabled(true));

		passButton.addClickListener(e -> {
			boolean x = false;

			if (email.getValue().compareTo(usuario.getEmail()) != 0) {
				if (passwordEncoder.matches(newPassword.getValue(), usuario.getPassword())) {
					usuario.setEmail(email.getValue());
					x = true;
				} else
					mostrarError();
			}
			if (x) {
				servicioUsuario.actualizarUsuario(usuario);
				dialog.open();
			}
		});

		formulario.add(email, newPassword, passButton);
		formulario.setResponsiveSteps(new ResponsiveStep("0", 1), new ResponsiveStep("500px", 3));
		formulario.setColspan(email, 3);
		formulario.setColspan(newPassword, 2);

		return formulario;
	}

	private static VerticalLayout createDialogLayout(Dialog dialog) {
		H2 headline = new H2("Los datos han sido actualizados.");
		H4 midline = new H4("Los cambios se verán reflejados pronto.");
		Button okButton = new Button("OK", e -> {
			dialog.close();
			UI.getCurrent().getPage().reload();
		});
		Button homeButton = new Button("Volver al home", e -> {
			dialog.close();
			UI.getCurrent().navigate("");
		});

		okButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		HorizontalLayout buttonLayout = new HorizontalLayout(homeButton, okButton);
		buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

		VerticalLayout dialogLayout = new VerticalLayout(headline, midline, buttonLayout);
		dialogLayout.setPadding(false);
		dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
		dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");

		return dialogLayout;
	}

	private static Button crearBotonInicio() {
		Button homeButton = new Button("Volver al home", new Icon(VaadinIcon.HOME), e -> UI.getCurrent().navigate(""));
		homeButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

		return homeButton;
	}

	private static Button crearBotonCrud(User currentUser) {
		String user_role = currentUser.getRole().getName();

		if (user_role.equals("ROLE_operator")) {
			Button CrudButton1 = new Button("Panel de operador", new Icon(VaadinIcon.COG),
					e -> UI.getCurrent().navigate("/menuoperator"));
			CrudButton1.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

			return CrudButton1;
		} else if (user_role.equals("ROLE_admin")) {
			Button CrudButton2 = new Button("Panel de administrador", new Icon(VaadinIcon.COG),
					e -> UI.getCurrent().navigate("/menuadmin"));
			CrudButton2.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

			return CrudButton2;
		} else {
			Button CrudButton3 = new Button();
			CrudButton3.getElement().getStyle().set("display", "none");

			return CrudButton3;
		}
	}

	private static void mostrarError() {
		Dialog errorDialog = new Dialog();
		Label errorLine = new Label(
				"La contraseña proporcionada no coincide. Vuelve a introducirla para cambiar el correo, por favor.");
		Button okButton = new Button("OK", e -> errorDialog.close());
		VerticalLayout errorLayout = new VerticalLayout(errorLine, okButton);

		errorDialog.open();
		errorDialog.add(errorLayout);
	}
}
