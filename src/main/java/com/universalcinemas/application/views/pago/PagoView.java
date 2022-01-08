package com.universalcinemas.application.views.pago;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;

import com.universalcinemas.application.data.film.FilmRepository;
import com.universalcinemas.application.data.plan.Plan;
import com.universalcinemas.application.data.plan.PlanRepository;
import com.universalcinemas.application.data.plan.PlanService;
import com.universalcinemas.application.views.MainLayout;
import com.universalcinemas.application.views.inicio.InicioView;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Aside;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Pago")
@Route(value = "checkout", layout = MainLayout.class)
@PermitAll
public class PagoView extends VerticalLayout implements HasUrlParameter<Integer> {
	private PlanService planService;
    /*private static final List<String> countries = new ArrayList<>();
    static {
        countries.add("España");
        countries.add("Portugal");
    }

    @Id
    private ComboBox countrySelect;
    @Id
    private ComboBox stateSelect;
*/
    @Autowired
    public PagoView(PlanService planService) {
        /*addClassNames("flex", "flex-col", "h-full");
        stateSelect.setVisible(false);
        countrySelect.setItems(countries);
        countrySelect.addValueChangeListener(e -> {
            stateSelect.setVisible(countrySelect.getValue().equals("United States"));
        });*/
		this.planService = planService;
    }
    
    private FormLayout crearFormularioDireccion() {
    	FormLayout formularioDireccion = new FormLayout();
		TextField pais = new TextField("País");
		pais.setRequired(true);
		TextField direccion = new TextField("Dirección");
		direccion.setRequired(true);
		TextField codigoPostal = new TextField("Código postal");
		codigoPostal.setRequired(true);
		TextField ciudad = new TextField("Ciudad");
		ciudad.setRequired(true);
		formularioDireccion.add(pais, direccion, codigoPostal, ciudad);
		return formularioDireccion;
    }
    
    private FormLayout crearFormularioTarjeta() {
    	FormLayout formularioTarjeta = new FormLayout();
		TextField nombreTarjeta = new TextField("Nombre del titular");
		nombreTarjeta.setRequired(true);
		TextField numero = new TextField("Número de tarjeta");
		numero.setRequired(true);
		TextField codigoSeguridad = new TextField("Código de seguridad");
		codigoSeguridad.setRequired(true);
		DatePicker fechaCaducidad = new DatePicker("Fecha de caducidad");
		fechaCaducidad.setRequired(true);
		formularioTarjeta.add(nombreTarjeta, numero, codigoSeguridad, fechaCaducidad);
		return formularioTarjeta;
    }

	@Override
	public void setParameter(BeforeEvent event, Integer planId) {
		Optional<Plan> plan = planService.findById(planId);
		Button btn = new Button("Pagar");
		btn.addClickListener(e -> UI.getCurrent().navigate(InicioView.class));
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
