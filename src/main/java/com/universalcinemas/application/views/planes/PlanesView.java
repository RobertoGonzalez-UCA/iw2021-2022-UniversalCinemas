package com.universalcinemas.application.views.planes;

import javax.annotation.security.PermitAll;

import com.universalcinemas.application.data.film.Film;
import com.universalcinemas.application.data.plan.Plan;
import com.universalcinemas.application.data.plan.PlanRepository;
import com.universalcinemas.application.security.SecurityService;
import com.universalcinemas.application.views.MainLayout;
import com.universalcinemas.application.views.novedades.NovedadesView;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Planes")
@Route(value = "plans", layout = MainLayout.class)
@PermitAll
public class PlanesView extends VerticalLayout {

	public PlanesView(PlanRepository planrepository) {
		
        Iterable<Plan> plans = planrepository.findAll();

        //Layouts
        VerticalLayout verticalLayout1 = new VerticalLayout();
        HorizontalLayout horizontalLayout1 = new HorizontalLayout();

        for(Plan p: plans)
        {
        	Button btn = new Button("Elegir plan");
     		btn.addClickListener(e -> UI.getCurrent().navigate(NovedadesView.class));
        	VerticalLayout verticalLayout2 = new VerticalLayout();
        	verticalLayout2.add(new Label(p.getName()));
        	verticalLayout2.add(new Label(p.getDescription()));
        	verticalLayout2.add(btn);
        	verticalLayout2.setWidth("50%");
        	verticalLayout2.setAlignItems(Alignment.CENTER);
        	horizontalLayout1.add(verticalLayout2);
        }
        
        verticalLayout1.add(horizontalLayout1);
        add(verticalLayout1);
	}
}