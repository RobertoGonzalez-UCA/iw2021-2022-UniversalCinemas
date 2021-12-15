package com.universalcinemas.application.views.planes;

import javax.annotation.security.PermitAll;

import com.universalcinemas.application.security.SecurityService;
import com.universalcinemas.application.views.MainLayout;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Planes")
@Route(value = "plans", layout = MainLayout.class)
@PermitAll
@Tag("planes-view")
@JsModule("./views/planes/planes-view.ts")
public class PlanesView extends VerticalLayout {

	@Id
	private Select<String> sortBy;

	public PlanesView() {
		// Botones de prueba, pero NO CARGA LA VISTA, no sé por qué ! (Roberto)
		TextField name = new TextField("Your name");
		Button sayHello = new Button("Say hello");
		sayHello.addClickListener(e -> {
			Notification.show("Hello " + name.getValue());
		});

		Button logout = new Button("Cerrar sesión");
		logout.addClickListener(e -> {
			SecurityService.logout();
		});
	}
}