package com.universalcinemas.application.views.menuadmin;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;

import com.universalcinemas.application.data.user.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Menu Administrador")
@Route(value = "menuadmin")
@RolesAllowed("ROLE_admin")
public class MenuAdminView extends VerticalLayout{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	UserService userService;
	
	@Autowired
	public MenuAdminView() {
		VerticalLayout mainLayout = new VerticalLayout();
		HorizontalLayout buttonLayout = new HorizontalLayout();
		VerticalLayout labelLayout = new VerticalLayout(new H4("Menú de administrador"));
		VerticalLayout volverLayout = new VerticalLayout(crearBoton("Volver al home","news"));
		
		buttonLayout.add(crearBoton("Peliculas","crudfilms"), crearBoton("Usuarios","crudusers"), crearBoton("Negocios","crudbusinesses"), crearBoton("Planes","crudplans"), crearBoton("Salas","crudrooms"), crearBoton("Sesiones","crudsessions"));
		mainLayout.add(buttonLayout);
		mainLayout.setAlignItems(Alignment.CENTER);
		labelLayout.setAlignItems(Alignment.CENTER);
		volverLayout.setAlignItems(Alignment.CENTER);
		
		add(labelLayout,
			mainLayout,
			volverLayout);
	}
	
	private Button crearBoton(String texto, String vista) {
		if(texto == "Volver al home") {
			Button boton = new Button(texto, new Icon(VaadinIcon.HOME), e -> UI.getCurrent().navigate(vista));
			boton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
			return boton;
		}
		else {
			Button boton = new Button(texto, e -> UI.getCurrent().navigate(vista));
			return boton;
		}
	}
	
}
