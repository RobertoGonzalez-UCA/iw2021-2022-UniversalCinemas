package com.universalcinemas.application.views.perfilusuario;

import javax.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;

import com.universalcinemas.application.data.user.User;
import com.universalcinemas.application.data.user.UserService;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Perfil")
@Route(value = "perfilusuario")
@PermitAll
public class PerfilUsuarioView extends VerticalLayout {
	@Autowired
	public PerfilUsuarioView(UserService servicioUsuario) {
		add(crearFormulario(servicioUsuario.obtenerDatosUsuario(1)));
	}
	
	private static FormLayout crearFormulario(User usuario) {
		FormLayout formulario = new FormLayout();
		TextField nombre = new TextField("Nombre");
		TextField apellidos = new TextField("Apellidos");
		EmailField email = new EmailField("Email");
		TextField telefono = new TextField("Telefono");
		DatePicker nacimiento = new DatePicker("Nacimiento");

		nombre.setValue(usuario.getName());
		apellidos.setValue(usuario.getSurname());
		email.setValue(usuario.getEmail());
		telefono.setValue(usuario.getPhone());
		nacimiento.setValue(usuario.getDateOfBirth());
		
		formulario.add(nombre, apellidos, 
				email,
				telefono, nacimiento);
		formulario.setResponsiveSteps(
				new ResponsiveStep("0",1),
				new ResponsiveStep("500px",2)
				);
		formulario.setColspan(email, 2);

		return formulario;
	}
}
