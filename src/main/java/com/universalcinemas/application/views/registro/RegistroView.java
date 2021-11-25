package com.universalcinemas.application.views.registro;

import com.universalcinemas.application.data.entity.SamplePerson;
import com.universalcinemas.application.data.service.SamplePersonService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.universalcinemas.application.views.MainLayout;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.component.icon.Icon;

@PageTitle("Registro")
@Route(value = "signup", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@Uses(Icon.class)
public class RegistroView extends Div {

    private TextField firstName = new TextField("Nombre");
    private TextField lastName = new TextField("Apellidos");
    private EmailField email = new EmailField("Email");
    private PasswordField password = new PasswordField("Contraseña");
    private PasswordField passwordConfirm = new PasswordField("Confirma tu contraseña");
    private DatePicker dateOfBirth = new DatePicker("Fecha de Nacimiento");
    private PhoneNumberField phone = new PhoneNumberField("Número de telefono");
    private Image urlProfileImage = new Image("https://dummyimage.com/300x200/000/fff", "DummyImage");

    private Button cancel = new Button("Cancelar");
    private Button save = new Button("Guardar");

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
        return new H3("Datos personales");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        email.setErrorMessage("Por favor, introduce un correo válido");
        formLayout.add(firstName, lastName, dateOfBirth, phone, email, password, passwordConfirm, urlProfileImage);
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
            countryCode.setPlaceholder("País");
            countryCode.setPattern("\\+\\d*");
            countryCode.setPreventInvalidInput(true);
            countryCode.setItems("+34");
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
    }

}
