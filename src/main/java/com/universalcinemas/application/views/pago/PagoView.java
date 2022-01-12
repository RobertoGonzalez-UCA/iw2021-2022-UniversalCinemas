package com.universalcinemas.application.views.pago;

import java.util.Optional;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;

import com.universalcinemas.application.data.city.City;
import com.universalcinemas.application.data.city.CityService;
import com.universalcinemas.application.data.country.Country;
import com.universalcinemas.application.data.country.CountryService;
import com.universalcinemas.application.data.plan.Plan;
import com.universalcinemas.application.data.plan.PlanService;
import com.universalcinemas.application.data.province.Province;
import com.universalcinemas.application.data.province.ProvinceService;
import com.universalcinemas.application.data.user.User;
import com.universalcinemas.application.data.user.UserService;
import com.universalcinemas.application.security.SecurityService;
import com.universalcinemas.application.views.MainLayout;
import com.universalcinemas.application.views.inicio.InicioView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Aside;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Pago")
@Route(value = "checkout", layout = MainLayout.class)
@RolesAllowed({"ROLE_admin", "ROLE_operator", "ROLE_user"})
public class PagoView extends VerticalLayout implements HasUrlParameter<Integer> {

	private static final long serialVersionUID = 1L;
	private PlanService planService;
	private CountryService countryService;
	private SecurityService securityService;
	private UserService userService;
	
	private ComboBox<Country> country;
	private TextField province;
	private TextField city;
	private TextField address;
	private IntegerField postcode;
	
	private TextField cardName;
	private IntegerField cardNumber;
	private IntegerField cardSecurityNumber;
	private DatePicker cardExpirationDate;

    @Autowired
    public PagoView(PlanService planService, CountryService countryService, SecurityService securityService, UserService userService) {
		this.planService = planService;
		this.countryService = countryService;
		this.securityService = securityService;
		this.userService = userService;
    }
    
    private FormLayout crearFormularioDireccion() {
    	FormLayout formAddress = new FormLayout();
		country = new ComboBox<Country>("País");
		country.setItems(countryService.findAll()); // list/set of possible countries.
		country.setItemLabelGenerator(country -> country.getName());
		country.setValue(countryService.findByName("España").get());
		province = new TextField("Provincia");
		city = new TextField("Ciudad");
		address = new TextField("Dirección");
		address.setRequired(true);
		postcode = new IntegerField("Código postal");
		formAddress.add(country, province, city, postcode, address);
		return formAddress;
    }
    
    private FormLayout crearFormularioTarjeta() {
    	FormLayout formCreditcard = new FormLayout();
    	cardName = new TextField("Nombre del titular");
    	cardName.setRequired(true);
		cardNumber = new IntegerField("Número de tarjeta");
		cardSecurityNumber = new IntegerField("Código de seguridad");
		cardExpirationDate = new DatePicker("Fecha de caducidad");
		cardExpirationDate.setRequired(true);
		formCreditcard.add(cardName, cardNumber, cardSecurityNumber, cardExpirationDate);
		return formCreditcard;
    }

	@Override
	public void setParameter(BeforeEvent event, Integer planId) {
		User currentUser = securityService.getAuthenticatedUser().get();
		Optional<Plan> plan = planService.findById(planId);
		Button btn = new Button("Pagar");
		btn.addClickListener(e -> {
			try {
				if (country.isEmpty()) {
			        Notification.show("Introduce el país");
			    } else if (province.isEmpty()) {
			        Notification.show("Introduce la provincia");
			    } else if (city.isEmpty()) {
			        Notification.show("Introduce la ciudad");
			    } else if (postcode.isEmpty()) {
			        Notification.show("Introduce el código postal");
			    } else if (address.isEmpty()) {
			        Notification.show("Introduce la dirección");
			    } else if (cardName.isEmpty()) {
			        Notification.show("Introduce el nombre del titular de la tarjeta");
			    } else if (cardNumber.isEmpty()) {
			        Notification.show("Introduce el número de la tarjeta");
			    } else if (cardSecurityNumber.isEmpty()) {
			        Notification.show("Introduce el número de seguridad de la tarjeta");
			    } else if (cardExpirationDate.isEmpty()) {
			        Notification.show("Introduce la fecha de caducidad de la tarjeta");
			    } else {
			    	currentUser.setPlan(plan.get());
			    	userService.actualizarUsuario(currentUser);
			    	UI.getCurrent().navigate(InicioView.class);
			    }
			} catch (Exception exception) {
				Notification.show("Ocurrió un error al procesar el pago. " + exception.getMessage());
			}
		});
    	btn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		VerticalLayout verticalLayout = new VerticalLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        verticalLayout.add(new H2("Pago"));
        verticalLayout.add(new H3("Dirección de facturación"));
		VerticalLayout formularioDireccion = new VerticalLayout(crearFormularioDireccion());
		verticalLayout.add(formularioDireccion);
        verticalLayout.add(new H3("Información de pago"));
		VerticalLayout formularioTarjeta = new VerticalLayout(crearFormularioTarjeta());
		verticalLayout.add(formularioTarjeta);
		verticalLayout.add(btn);
		Aside pedido = new Aside(new H3("Pedido"));
		pedido.add(new Paragraph(plan.get().getName() + " (mensual) " + plan.get().getPrice().toString() + "€"));
		pedido.setWidth("500px");
		horizontalLayout.add(verticalLayout, pedido);
		add(horizontalLayout);
		
	}

}
