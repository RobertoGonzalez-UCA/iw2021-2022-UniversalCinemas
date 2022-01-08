package com.universalcinemas.application.views.plan;

import java.util.Optional;

import javax.annotation.security.PermitAll;

import com.universalcinemas.application.data.film.Film;
import com.universalcinemas.application.data.film.FilmRepository;
import com.universalcinemas.application.data.film.FilmService;
import com.universalcinemas.application.data.plan.Plan;
import com.universalcinemas.application.data.plan.PlanRepository;
import com.universalcinemas.application.data.plan.PlanService;
import com.universalcinemas.application.views.MainLayout;
import com.universalcinemas.application.views.inicio.InicioView;
import com.universalcinemas.application.views.pago.PagoView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Plan")
@Route(value = "plan", layout = MainLayout.class)
@PermitAll
public class PlanView extends VerticalLayout implements HasUrlParameter<Integer> {
	private PlanService planService;
	
	@Override
	public void setParameter(BeforeEvent event, Integer planId) {
		Optional<Plan> plan = planService.findById(planId);
    	VerticalLayout verticalLayout = new VerticalLayout();
    	HorizontalLayout horizontalLayout = new HorizontalLayout();
    	Button btn = new Button("Proceder al pago");
    	btn.addClickListener(e -> UI.getCurrent().navigate(PagoView.class, plan.get().getId()));
    	btn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    	verticalLayout.add(new H1("Compra de entradas"));
    	verticalLayout.add(new H2("Plan seleccionado: " + plan.get().getName() + "."));
    	verticalLayout.add(new H3(plan.get().getDescription()));
    	verticalLayout.add(new H3("Precio mensual: " + plan.get().getPrice() + "€"));
    	verticalLayout.add(new Paragraph("La suscripción se renovará automáticamente cada mes. Para terminarla, diríjase a su perfil."));
    	verticalLayout.add(new Paragraph("Al proceder al pago, aceptas nuestros términos y condiciones."));
    	verticalLayout.add(btn);
    	add(verticalLayout);
	}
	
	public PlanView(PlanService planService) {
		this.planService = planService;
	}

}
