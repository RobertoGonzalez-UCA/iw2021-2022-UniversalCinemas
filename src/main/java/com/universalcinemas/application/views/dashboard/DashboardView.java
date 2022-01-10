package com.universalcinemas.application.views.dashboard;

import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.javaparser.ParseException;
import com.universalcinemas.application.data.business.Business;
import com.universalcinemas.application.data.business.BusinessService;
import com.universalcinemas.application.views.MainLayout;
import com.universalcinemas.application.views.inicio.InicioView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Panel de estadísticas")
@Route(value = "dashboard/:BusinessID", layout = MainLayout.class)
@RolesAllowed({ "ROLE_admin", "ROLE_operator" })
public class DashboardView extends VerticalLayout implements BeforeEnterObserver {

	private static final long serialVersionUID = 1L;
	private final String BUSINESS_ID = "BusinessID";
	private BusinessService businessService;

	public DashboardView(@Autowired BusinessService businessService) throws ParseException {
		this.businessService = businessService;
		
        addClassName("dashboard-view");

        HorizontalLayout board = new HorizontalLayout();
        board.add(createHighlight("Usuarios", "745", 33.7), createHighlight("Entradas vendidas", "54.6k", -112.45),
                createHighlight("Suscripciones a planes", "18%", 3.9), createHighlight("Edad media de los usuarios", "37", 1.0));
        board.setWidthFull();
        add(board);
	}
	
	private Component createHighlight(String title, String value, Double percentage) {
        VaadinIcon icon = VaadinIcon.ARROW_UP;
        String prefix = "";
        String theme = "badge";

        if (percentage == 0) {
            prefix = "±";
        } else if (percentage > 0) {
            prefix = "+";
            theme += " success";
        } else if (percentage < 0) {
            icon = VaadinIcon.ARROW_DOWN;
            theme += " error";
        }

        H2 h2 = new H2(title);
        h2.addClassNames("font-normal", "m-0", "text-secondary", "text-xs");

        Span span = new Span(value);
        span.addClassNames("font-semibold", "text-3xl");

        Icon i = icon.create();
        i.addClassNames("box-border", "p-xs");

        Span badge = new Span(i, new Span(prefix + percentage.toString()));
        badge.getElement().getThemeList().add(theme);

        VerticalLayout layout = new VerticalLayout(h2, span, badge);
        layout.addClassName("p-l");
        layout.setPadding(false);
        layout.setSpacing(false);
        return layout;
    }

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		Optional<Integer> BusinessId = event.getRouteParameters().getInteger(BUSINESS_ID);
		if (BusinessId.isPresent()) {
			Optional<Business> BusinessFromBackend = businessService.get(BusinessId.get());
			if (BusinessFromBackend.isEmpty()) {
				UI.getCurrent().navigate(InicioView.class);
				Notification.show("No se pudo encontrar ese negocio");
			}
		} else {
			UI.getCurrent().navigate(InicioView.class);
			Notification.show("No se pudo encontrar ese negocio");
		}
	}
}