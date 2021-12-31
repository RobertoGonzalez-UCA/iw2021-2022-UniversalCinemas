package com.universalcinemas.application.views.perfilusuario;

import javax.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.universalcinemas.application.data.user.User;
import com.universalcinemas.application.data.user.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
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
@PermitAll
public class PerfilUsuarioView extends VerticalLayout {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	UserService servicioUsuario;
	@Autowired
	public PerfilUsuarioView(UserService servicioUsuario) {
		this.servicioUsuario = servicioUsuario;
		VerticalLayout formularioDatos = new VerticalLayout(crearFormularioDatos(servicioUsuario.obtenerDatosUsuario(1)));
		VerticalLayout formularioLogin = new VerticalLayout(crearFormularioLogin(servicioUsuario.obtenerDatosUsuario(1)));
		HorizontalLayout formularios = new HorizontalLayout(formularioDatos, formularioLogin);
		HorizontalLayout botonLayout = new HorizontalLayout(crearBotonInicio());
		//botonLayout.getStyle().set("align-items","center");
		add(formularios, botonLayout);
	}
	
	private FormLayout crearFormularioDatos(User usuario) {
		FormLayout formulario = new FormLayout();
		TextField nombre = new TextField("Nombre");
		TextField apellidos = new TextField("Apellidos");
		TextField telefono = new TextField("Telefono");
		DatePicker fechaNacimiento = new DatePicker("Nacimiento");
		Button modificar = new Button("Modificar");
		Dialog dialog = new Dialog();
        VerticalLayout dialogLayout = createDialogLayout(dialog);
        dialog.add(dialogLayout);

		modificar.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		
		nombre.setValue(usuario.getName());
		apellidos.setValue(usuario.getSurname());
		telefono.setValue(usuario.getPhone());
		fechaNacimiento.setValue(usuario.getDateOfBirth());
		
		nombre.addValueChangeListener(e -> modificar.setEnabled(true));
		apellidos.addValueChangeListener(e -> modificar.setEnabled(true));
		telefono.addValueChangeListener(e -> modificar.setEnabled(true));
		fechaNacimiento.addValueChangeListener(e -> modificar.setEnabled(true));
		
		modificar.addClickListener(e -> {
			boolean x = false;
			
			if(nombre.getValue().compareTo(usuario.getName()) != 0) {
				usuario.setName(nombre.getValue());
				x = true;
			}
			if(apellidos.getValue().compareTo(usuario.getSurname()) != 0) {
				usuario.setSurname(apellidos.getValue());
				x = true;
			}

			if(telefono.getValue().compareTo(usuario.getPhone()) != 0) {
				usuario.setPhone(telefono.getValue());
				x = true;
			}
			if(fechaNacimiento.getValue().compareTo(usuario.getDateOfBirth()) != 0) {
				usuario.setDateOfBirth(fechaNacimiento.getValue());
				x = true;
			}
			if(x) {
				servicioUsuario.actualizarUsuario(usuario);
				dialog.open();
			}
			
		});
		
		formulario.add(nombre, apellidos,
				fechaNacimiento, telefono, modificar);
		formulario.setResponsiveSteps(
				new ResponsiveStep("0",1),
				new ResponsiveStep("500px",3)
				);
		formulario.setColspan(nombre, 1);
		formulario.setColspan(apellidos, 2);
	
		return formulario;
	}
	private FormLayout crearFormularioLogin(User usuario) {
		FormLayout formulario = new FormLayout();
		EmailField email = new EmailField("Email");
		PasswordField newPassword = new PasswordField("Contraseña");
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
			
			if(email.getValue().compareTo(usuario.getEmail()) != 0) {
				if(passwordEncoder.matches(newPassword.getValue(), usuario.getPassword())) {
					usuario.setEmail(email.getValue());
					x = true;
				}
			}
			if(x) {
				servicioUsuario.actualizarUsuario(usuario);
				dialog.open();
			}
			else
				mostrarError();
		});
		
		formulario.add(email, 
				newPassword,
				passButton
				);
		formulario.setResponsiveSteps(
				new ResponsiveStep("0",1),
				new ResponsiveStep("500px",3)
				);
		formulario.setColspan(email, 3);
		formulario.setColspan(newPassword, 2);
		
		return formulario;
	}
	private static VerticalLayout createDialogLayout(Dialog dialog) {
        H2 headline = new H2("Los datos han sido actualizados.");
        H4 midline = new H4("Los cambios se verán reflejados pronto.");
        headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0")
                .set("font-size", "1.5em").set("font-weight", "bold");
        Button okButton = new Button("OK", e -> {
        	dialog.close();
        	UI.getCurrent().getPage().reload();
        });
        Button homeButton = new Button("Volver al home", e -> {
        	dialog.close();
        	UI.getCurrent().navigate("news");
        });
        
        okButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttonLayout = new HorizontalLayout(homeButton, okButton);
        buttonLayout
                .setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        VerticalLayout dialogLayout = new VerticalLayout(headline, midline,
                buttonLayout);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");

        return dialogLayout;
    }
	private static Button crearBotonInicio() {
		Button homeButton = new Button("Volver al home", new Icon(VaadinIcon.HOME), e -> UI.getCurrent().navigate("news"));
        homeButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        
        return homeButton;
	}
	private static void mostrarError() {
		Dialog errorDialog = new Dialog();
		Label errorLine = new Label("La contraseña proporcionada no coincide. Vuelve a introducirla para cambiar el correo, por favor.");
		//errorLine.getStyle().set("margin", "var(--lumo-space-m) 0 0 0")
        //.set("font-size", "1.5em").set("font-weight", "bold");
		Button okButton = new Button("OK", e -> errorDialog.close());
		VerticalLayout errorLayout = new VerticalLayout(errorLine, okButton);
		
		errorDialog.open();
		errorDialog.add(errorLayout);
	}
}
