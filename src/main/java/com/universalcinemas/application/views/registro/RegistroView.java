package com.universalcinemas.application.views.registro;


import com.universalcinemas.application.data.user.User;
import com.universalcinemas.application.data.user.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

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
//        emailField.setPattern("^.+@example\\.com$");
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
        
        Button button2 = new Button("Acceder a mi cuenta");
        

        add(title,name,surname,birthDate,emailField,phoneNumber,password1,password2,button,button2);
        
        button2.addClickListener(e ->
	     button.getUI().ifPresent(ui ->
	           ui.navigate("/login"))
        );
        
        setHeightFull();
		setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);//puts button in vertical center
    }
	
	 private void register(String name, String surname, String birthDate, String phoneNumber, String email, String password1, String password2) {
		 
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
        	userService.registerUser(user);
            Notification.show("Te has registrado con éxito");  
        }
    }
}