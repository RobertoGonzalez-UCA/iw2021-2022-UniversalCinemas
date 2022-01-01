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

import com.universalcinemas.application.data.user.User;
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
public class RegistroView extends Composite {

	private static final long serialVersionUID = 1L;
	private final UserService userService;

    public RegistroView(UserService userService) {
        this.userService = userService;
    }
	
	
	@Override
    protected Component initContent() {
        TextField name = new TextField("Nombre");
        TextField surname = new TextField("Apellidos");
        DatePicker birthDate = new DatePicker("Fecha nacimiento");
        EmailField email = new EmailField("Correo");
        PasswordField password1 = new PasswordField("Contraseña");
        PasswordField password2 = new PasswordField("Confirma contraseña");
        return new VerticalLayout(
                new H2("Registro"),
                name,
                surname,
                birthDate,
                email,
                password1,
                password2,
                new Button("Registrarme", event -> register(
                		name.getValue(),
                		surname.getValue(),/*
                		birthDate.getValue().toString(),*/
                		email.getValue(),
                        password1.getValue(),
                        password2.getValue()
                ))
        );
    }
	
	 private void register(String name, String surname,/* String birthDate, */String email, String password1, String password2) {
	        if (name.trim().isEmpty()) {
	            Notification.show("Introduce tu nombre");
	        } else if (surname.trim().isEmpty()) {
	            Notification.show("Introduce tus apellidos");
	        }/* else if (birthDate.toString().trim().isEmpty()) {
	            Notification.show("Introduce tu fecha de nacimiento");
	        }*/ else if (email.trim().isEmpty()) {
	            Notification.show("Introduce tu email");
	        } else if (password1.isEmpty()) {
	            Notification.show("Enter a password");
	        } else if (!password1.equals(password2)) {
	            Notification.show("Passwords don't match");
	        } else if (!password1.equals(password2)) {
	            Notification.show("Passwords don't match");
	        } else {
	        	User user = new User();
	        	userService.registerUser(user);
	            Notification.show("Check your email.");
	        }
	    }
	

   /*private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private EmailField email = new EmailField("Email address");
    private DatePicker dateOfBirth = new DatePicker("Birthday");
    private PhoneNumberField phone = new PhoneNumberField("Phone number");
    private TextField occupation = new TextField("Occupation");

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private Binder<SamplePerson> binder = new Binder(SamplePerson.class);

    public RegistroView(SamplePersonService personService) {
        addClassName("registro-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        binder.bindInstanceFields(this);
        clearForm();

        cancel.addClickListener(e -> clearForm());
        save.addClickListener(e -> {
            personService.update(binder.getBean());
            Notification.show(binder.getBean().getClass().getSimpleName() + " details stored.");
            clearForm();
        });
    }

    private void clearForm() {
        binder.setBean(new SamplePerson());
    }

    private Component createTitle() {
        return new H3("Personal information");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        email.setErrorMessage("Please enter a valid email address");
        formLayout.add(firstName, lastName, dateOfBirth, phone, email, occupation);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save);
        buttonLayout.add(cancel);
        return buttonLayout;
    }

    private static class PhoneNumberField extends CustomField<String> {
        private ComboBox<String> countryCode = new ComboBox<>();
        private TextField number = new TextField();

        public PhoneNumberField(String label) {
            setLabel(label);
            countryCode.setWidth("120px");
            countryCode.setPlaceholder("Country");
            countryCode.setPattern("\\+\\d*");
            countryCode.setPreventInvalidInput(true);
            countryCode.setItems("+354", "+91", "+62", "+98", "+964", "+353", "+44", "+972", "+39", "+225");
            countryCode.addCustomValueSetListener(e -> countryCode.setValue(e.getDetail()));
            number.setPattern("\\d*");
            number.setPreventInvalidInput(true);
            HorizontalLayout layout = new HorizontalLayout(countryCode, number);
            layout.setFlexGrow(1.0, number);
            add(layout);
        }

        @Override
        protected String generateModelValue() {
            if (countryCode.getValue() != null && number.getValue() != null) {
                String s = countryCode.getValue() + " " + number.getValue();
                return s;
            }
            return "";
        }

        @Override
        protected void setPresentationValue(String phoneNumber) {
            String[] parts = phoneNumber != null ? phoneNumber.split(" ", 2) : new String[0];
            if (parts.length == 1) {
                countryCode.clear();
                number.setValue(parts[0]);
            } else if (parts.length == 2) {
                countryCode.setValue(parts[0]);
                number.setValue(parts[1]);
            } else {
                countryCode.clear();
                number.clear();
            }
        }
    }*/

}