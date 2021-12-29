package com.universalcinemas.application.views.perfilusuario;

import java.time.LocalDate;
import java.util.Date;

import javax.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;

import com.universalcinemas.application.data.user.User;
import com.universalcinemas.application.data.user.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
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
public class PerfilUsuarioView extends HorizontalLayout {
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
		add(formularioDatos, formularioLogin);
		
	}
	
	private FormLayout crearFormularioDatos(User usuario) {
		FormLayout formulario = new FormLayout();
		TextField nombre = new TextField("Nombre");
		TextField apellidos = new TextField("Apellidos");
		TextField telefono = new TextField("Telefono");
		DatePicker fechaNacimiento = new DatePicker("Nacimiento");
		Button modificar = new Button("Modificar");
		
		//modificar.setEnabled(false);
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
			boolean x = true;
			
			if(nombre.getValue().compareTo(usuario.getName()) != 0) {
				usuario.setName(nombre.getValue());
				x = true;
			}
			if(apellidos.getValue().compareTo(usuario.getSurname()) != 0) {
				usuario.setSurname(apellidos.getValue());
				x = true;
			}
//			if(email.getValue().compareTo(usuario.getEmail()) != 0) {
//				usuario.setEmail(email.getValue());
//				x = true;
//			}
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
		
		passButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		
		email.setValue(usuario.getEmail());
		
		email.addValueChangeListener(e -> passButton.setEnabled(true));
		
		email.addValueChangeListener(e -> passButton.setEnabled(true));
		
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
}
