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
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Aside;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
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
		Business business = businessService.getBusinessById(1);
		addClassName("dashboard-view");

		VerticalLayout businessInfo = new VerticalLayout();
		businessInfo.add(createAside(business));

		HorizontalLayout board = new HorizontalLayout();
		board.add(createHighlight("Usuarios", "745", 33.7), createHighlight("Entradas vendidas", "54.6k", -112.45),
				createHighlight("Suscripciones a planes", "18%", 3.9),
				createHighlight("Edad media de los usuarios", "37", 1.0));
		board.setWidthFull();

		add(businessInfo, board);
	}

	private Aside createAside(Business business) {
		Aside aside = new Aside();
		aside.addClassNames("bg-contrast-5", "box-border", "p-l", "rounded-l", "sticky");
		aside.setWidth("25%");
		
		Header headerSection = new Header();
		headerSection.addClassNames("flex", "items-center", "justify-between", "mb-m");
		
        Button viewMap = new Button("Ver mapa");
        viewMap.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        viewMap.getStyle().set("color", "var(--lumo-error-text-color)");
        
        viewMap.addClickListener(e -> {
            Notification.show("Abriendo mapa...");
            UI.getCurrent().getPage().open("https://goo.gl/maps/GDHRxqihzkriXbdi6", "_blank");	// This has to redirect a external page
        });
        
		H3 header = new H3(business.getName());
		header.addClassNames("m-0");
		headerSection.add(header, viewMap);

		UnorderedList ul = new UnorderedList();
		ul.addClassNames("list-none", "m-0", "p-0", "flex", "flex-col", "gap-m");

		ul.add(createListItem(business.getCountryName()));
		ul.add(createListItem(business.getProvinceName() + ", " + business.getCityName()));
		ul.add(createListItem(business.getStreet()));

		aside.add(headerSection, ul);
		return aside;
	}
	
	private ListItem createListItem(String primary) {
        ListItem item = new ListItem();
        item.addClassNames("flex", "justify-between");

        Div subSection = new Div();
        subSection.addClassNames("flex", "flex-col");
        subSection.add(new Span(primary));

        item.add(subSection);
        return item;
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