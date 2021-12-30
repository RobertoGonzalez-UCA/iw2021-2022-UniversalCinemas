package com.universalcinemas.application.views.planes;

import javax.annotation.security.PermitAll;

import com.universalcinemas.application.security.SecurityService;
import com.universalcinemas.application.views.MainLayout;
import com.universalcinemas.application.views.novedades.NovedadesView;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
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

	public PlanesView() {
		
        VerticalLayout plansLayout = new VerticalLayout();
        HorizontalLayout imagesLayout = new HorizontalLayout();
        HorizontalLayout buttonsLayout = new HorizontalLayout();

        Image img1=new Image("images/hola.gif", "images/hola.gif");
        img1.setWidth("300px");
        
        Button plan1 = new Button("Elegir plan");
		plan1.addClickListener(e -> UI.getCurrent().navigate(NovedadesView.class));

        Image img2=new Image("images/hola.gif","images/hola.gif");
        img2.setWidth("300px");
        
        Button plan2 = new Button("Elegir plan");
		plan2.addClickListener(e -> UI.getCurrent().navigate(NovedadesView.class));

        Image img3=new Image("images/hola.gif","images/hola.gif");
        img3.setWidth("300px");
        
        Button plan3 = new Button("Elegir plan");
		plan3.addClickListener(e -> UI.getCurrent().navigate(NovedadesView.class));

        Image img4=new Image("images/hola.gif","images/hola.gif");
        img4.setWidth("300px");
        
		Button plan4 = new Button("Elegir plan");
		plan4.addClickListener(e -> UI.getCurrent().navigate(NovedadesView.class));

		Button logout = new Button("Cerrar sesión");
		logout.addClickListener(e -> {
			SecurityService.logout();
		});

        imagesLayout.add(img1, img2, img3);
        buttonsLayout.add(plan1, plan2, plan3);
        plansLayout.add(imagesLayout, buttonsLayout);
        plansLayout.setHorizontalComponentAlignment(Alignment.CENTER, imagesLayout, buttonsLayout);
        add(plansLayout);
	}
}