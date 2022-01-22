package com.universalcinemas.application.views.iniciosesion;

import com.universalcinemas.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Inicio sesión")
@Route(value = "login", layout = MainLayout.class)
@AnonymousAllowed
@Uses(Icon.class)
public class IniciosesionView extends LoginOverlay implements BeforeEnterObserver{

	private static final long serialVersionUID = 1L;


	public IniciosesionView() {
        setAction("login");

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("Universal Cinemas");
        i18n.getHeader().setDescription("Inicia sesión para disfrutar de nuestra cartelera");
        i18n.getForm().setTitle("Acceda a su cuenta");
        i18n.getForm().setUsername("Email");
        i18n.getForm().setPassword("Contraseña");
        i18n.getForm().setSubmit("Entrar");
        i18n.getErrorMessage().setTitle("Email o contraseña incorrectos");
        i18n.getErrorMessage().setMessage("Introduzca un usuario y contraseña válidos.");
        i18n.setAdditionalInformation(null);
        setI18n(i18n);

        setForgotPasswordButtonVisible(false);
        setOpened(true);
    }

    
    @Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		// inform the user about an authentication error
		if(beforeEnterEvent.getLocation()  
        .getQueryParameters()
        .getParameters()
        .containsKey("error")) {
            this.setError(true);
        }
	}
}
