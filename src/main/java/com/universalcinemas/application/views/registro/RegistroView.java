package com.universalcinemas.application.views.registro;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.universalcinemas.application.data.user.User;
import com.universalcinemas.application.data.user.UserRepository;
import com.universalcinemas.application.data.user.UserService;
import com.universalcinemas.application.views.MainLayout;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.html.H2;

@PageTitle("Registro")
@Route(value = "signup")
@AnonymousAllowed
@Uses(Icon.class)
public class RegistroView extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private final UserService userService;

    public RegistroView(UserService userService) {
        
		this.userService = userService;
		
		H2 title = new H2("Registro");
        TextField name = new TextField("Nombre");
        TextField surname = new TextField("Apellidos");
        DatePicker birthDate = new DatePicker("Fecha nacimiento");
        EmailField emailField = new EmailField();
        emailField.setLabel("Email address");
        emailField.getElement().setAttribute("name", "email");
        emailField.setPlaceholder("username@example.com");
        emailField.setErrorMessage("Please enter a valid example.com email address");
        emailField.setClearButtonVisible(true);
        emailField.setPattern("^.+@example\\.com$");
        TextField phoneNumber = new TextField("Número de teléfono");
        PasswordField password1 = new PasswordField("Contraseña");
        PasswordField password2 = new PasswordField("Confirma contraseña");
        Button button = new Button("Registrarme", event -> register(
        		name.getValue(),
        		surname.getValue(),
        		birthDate.getValue() == null ? "" : birthDate.getValue().toString(),
				phoneNumber.getValue(),	
				emailField.getValue(),
                password1.getValue(),
                password2.getValue()
        ));
        

        add(title,name,surname,birthDate,emailField,phoneNumber,password1,password2,button);
        
        setHeightFull();
		setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);//puts button in vertical center
    }
	
	 private void register(String name, String surname, String birthDate, String email, String phoneNumber, String password1, String password2) {
		 
		 if (name.trim().isEmpty()) {
	            Notification.show("Introduce tu nombre");
	        } else if (surname.trim().isEmpty()) {
	            Notification.show("Introduce tus apellidos");
	        } else if (birthDate.toString().trim() == "") {
	            Notification.show("Introduce tu fecha de nacimiento");
	        } else if (email.trim().isEmpty()) {
	            Notification.show("Introduce tu email");
	        } else if (phoneNumber.trim().isEmpty()) {
	            Notification.show("Introduce tu teléfono");
	        } else if (password1.trim().isEmpty()) {
	            Notification.show("Introduce una contraseña");
	        } else if (password2.trim().isEmpty()) {
	            Notification.show("Introduce la contraseña de confirmación");
	        } else if (!password1.equals(password2)) {
	            Notification.show("Las contraseñas no coinciden");
	        } else {

	        	User user = new User(name,surname,email,birthDate,phoneNumber,password1);
	    		//System.out.println(user.toString());		
	        	userService.registerUser(user);
	        	
	            Notification.show("Te has registrado con éxito");
	        }
	    }
}