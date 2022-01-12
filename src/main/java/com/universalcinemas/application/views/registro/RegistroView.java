package com.universalcinemas.application.views.registro;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.universalcinemas.application.data.user.User;
import com.universalcinemas.application.data.user.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Component
@Scope("prototype")
@PageTitle("Registro")
@Route(value = "signup")
@AnonymousAllowed
@Uses(Icon.class)
public class RegistroView extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private final UserService userService;

    public RegistroView(UserService userService) {
        
		this.userService = userService;
				
		Page page = UI.getCurrent().getPage();
	    page.executeJs("document.getElementById('ROOT-2521314').id = 'root-signup';"
	    			+ "document.getElementById('outlet').id = 'outlet-signup';"
	    			+ "document.getElementsByTagName('vaadin-vertical-layout')[0].setAttribute('id', 'vaadin-vertical-layout-signup');"
	    			+ "document.getElementsByTagName('body')[0].setAttribute('id', 'body-signup');");
	    
		H2 title = new H2("Universal Cinemas");
		title.setId("h2-signup");
		
		Paragraph second_title = new Paragraph("Regístrate e inicia sesión para disfrutar de nuestra cartelera");
		second_title.setId("p-signup");
		
		H3 third_title = new H3("Regístrese");
		third_title.setId("h3-signup");

		TextField name = new TextField("Nombre");
        TextField surname = new TextField("Apellidos");
        
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setId("horizontalLayout-signup");
        horizontalLayout.add(name,surname);
        
        DatePicker birthDate = new DatePicker("Fecha nacimiento");
        EmailField emailField = new EmailField();
        emailField.setLabel("Email");
        emailField.getElement().setAttribute("name", "email");
        emailField.setPlaceholder("username@example.com");
        emailField.setErrorMessage("Por favor, introduce un correo válido.");
        emailField.setClearButtonVisible(true);
        emailField.setPattern("^(.+)@(.+)$");
        
        HorizontalLayout horizontalLayout2 = new HorizontalLayout();
        horizontalLayout2.setId("horizontalLayout-signup");
        horizontalLayout2.add(birthDate,emailField);
        
        TextField phoneNumber = new TextField("Número de teléfono");
        PasswordField password1 = new PasswordField("Contraseña");
        PasswordField password2 = new PasswordField("Confirma contraseña");
        
        HorizontalLayout horizontalLayout3 = new HorizontalLayout();
        horizontalLayout3.add(phoneNumber);
        
        HorizontalLayout horizontalLayout4 = new HorizontalLayout();
        horizontalLayout4.setId("horizontalLayout-signup");
        horizontalLayout4.add(password1,password2);
        
        Button button = new Button("Registrarme", event -> register(
        		name.getValue(),
        		surname.getValue(),
        		birthDate.getValue() == null ? "" : birthDate.getValue().toString(),
				phoneNumber.getValue(),	
				emailField.getValue(),
                password1.getValue(),
                password2.getValue()
        ));
        button.getElement().getStyle().set("background-color", "var(--lumo-primary-color)");

        
        HorizontalLayout horizontalLayout5 = new HorizontalLayout();
        horizontalLayout5.setId("horizontalLayout-signup");
        Button button2 = new Button("Acceder a mi cuenta");
        button2.getElement().getStyle().set("background-color", "var(--lumo-primary-color)");
        horizontalLayout5.add(button,button2);

        add(title,second_title, third_title,horizontalLayout,horizontalLayout2,horizontalLayout3,horizontalLayout4,horizontalLayout5);
        
        button2.addClickListener(e ->
	     button.getUI().ifPresent(ui ->
	           ui.navigate("/login"))
        );
        
		setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);//puts button in vertical center
    }
	
	 private void register(String name, String surname, String birthDate, String phoneNumber, String email, String password1, String password2) {
		 
		 if (name.trim().isEmpty()) {
	        	Notification notification_name_error = Notification.show("Introduce tu nombre");
	        	notification_name_error.addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else if (surname.trim().isEmpty()) {
        	Notification notification_surname_error = Notification.show("Introduce tus apellidos");
        	notification_surname_error.addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else if (birthDate.toString().trim() == "") {
        	Notification notification_birthDate_error = Notification.show("Introduce tu fecha de nacimiento");
        	notification_birthDate_error.addThemeVariants(NotificationVariant.LUMO_ERROR); 
        } else if (email.trim().isEmpty()) {
        	Notification notification_email_error = Notification.show("Introduce tu email");
        	notification_email_error.addThemeVariants(NotificationVariant.LUMO_ERROR); 
        } else if (phoneNumber.trim().isEmpty()) {
        	Notification notification_phone_error = Notification.show("Introduce tu teléfono");
        	notification_phone_error.addThemeVariants(NotificationVariant.LUMO_ERROR); 
        } else if (password1.trim().isEmpty()) {
        	Notification notification_password1_error = Notification.show("Introduce la contraseña");
        	notification_password1_error.addThemeVariants(NotificationVariant.LUMO_ERROR); 
        } else if (password2.trim().isEmpty()) {
        	Notification notification_password2_error = Notification.show("Introduce la contraseña de confirmación");
        	notification_password2_error.addThemeVariants(NotificationVariant.LUMO_ERROR); 
        } else if (!password1.equals(password2)) {
        	Notification notification_password_error = Notification.show("Las contraseñas no coinciden");
        	notification_password_error.addThemeVariants(NotificationVariant.LUMO_ERROR); 
        } else {
        	User user_exists = userService.loadUserByEmail(email);
        	if(user_exists.getEmail() == null) {
            	User user = new User(name,surname,email,birthDate,phoneNumber,password1);
            	userService.registerUser(user);
            	Notification notification_success = Notification.show("Te has registrado con éxito");
            	notification_success.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        	}else {
        		Notification notification_error = Notification.show("El usuario ya existe. Introduce otro correo.");
        		notification_error.addThemeVariants(NotificationVariant.LUMO_ERROR); 
        	}
        }
    }
}