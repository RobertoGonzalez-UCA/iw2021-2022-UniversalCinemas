package com.universalcinemas.application.views.planes;

import javax.annotation.security.PermitAll;

import com.universalcinemas.application.data.plan.Plan;
import com.universalcinemas.application.data.plan.PlanService;
import com.universalcinemas.application.views.MainLayout;
import com.universalcinemas.application.views.plan.PlanView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Planes")
@Route(value = "plans", layout = MainLayout.class)
@PermitAll
public class PlanesView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	public PlanesView(PlanService planService) {
		
        Iterable<Plan> plans = planService.findAll();

        //Layouts
        VerticalLayout verticalLayout1 = new VerticalLayout();
        HorizontalLayout horizontalLayout1 = new HorizontalLayout();
        
        verticalLayout1.add(new H2("Elige plan"));
        verticalLayout1.add(new Paragraph("Aquí podrás elegir el plan que se ajuste más a tus necesidades"));
        
        for(Plan p: plans)
        {
        	Button btn = new Button("Elegir plan");
        	VerticalLayout verticalLayout2 = new VerticalLayout();
     		btn.addClickListener(e -> UI.getCurrent().navigate(PlanView.class, p.getId()));
     		
     		Image img = new Image("https://start.vaadin.com/images/empty-plant.png", "imagen tematica del plan");
            img.setWidth("200px");
            verticalLayout2.add(img);
        	verticalLayout2.add(new Label("Plan " + p.getName()));
        	verticalLayout2.add(new Label(p.getDescription()));
        	verticalLayout2.add(btn);
        	verticalLayout2.setWidth("50%");
        	verticalLayout2.setAlignItems(Alignment.CENTER);
        	horizontalLayout1.add(verticalLayout2);
        }
        
        verticalLayout1.add(horizontalLayout1);
        add(verticalLayout1);
        
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
	}
}