package com.universalcinemas.application.views.dashboard;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.TreeMap;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.javaparser.ParseException;
import com.universalcinemas.application.data.business.Business;
import com.universalcinemas.application.data.business.BusinessService;
import com.universalcinemas.application.data.film.FilmService;
import com.universalcinemas.application.data.genre.Genre;
import com.universalcinemas.application.data.genre.GenreService;
import com.universalcinemas.application.data.plan.Plan;
import com.universalcinemas.application.data.plan.PlanService;
import com.universalcinemas.application.data.session.SessionService;
import com.universalcinemas.application.data.ticket.TicketService;
import com.universalcinemas.application.data.user.UserService;
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
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Panel de estadísticas")
@Route(value = "dashboard", layout = MainLayout.class)
@RolesAllowed({ "ROLE_admin", "ROLE_operator" })
public class DashboardView extends VerticalLayout implements HasUrlParameter<Integer> {

	private static final long serialVersionUID = 1L;
	private BusinessService businessService;
	private FilmService filmService;
	private GenreService genreService;
	private UserService userService;
	private TicketService ticketService;
	private PlanService planService;
	private SessionService sessionService;

	public DashboardView(@Autowired BusinessService businessService, FilmService filmService, GenreService genreService,
			UserService userService, TicketService ticketService, PlanService planService, SessionService sessionService) throws ParseException {
		this.businessService = businessService;
		this.filmService = filmService;
		this.genreService = genreService;
		this.userService = userService;
		this.ticketService = ticketService;
		this.planService = planService;
		this.sessionService = sessionService;
	}

	@Override
	public void setParameter(BeforeEvent event, Integer parameter) {
		// Manejo de la excepción (REVISAR)
		try {
			Optional<Business> BusinessFromBackend = businessService.get(parameter);
			if (BusinessFromBackend.isEmpty()) {
				UI.getCurrent().navigate(InicioView.class);
				Notification.show("No se pudo encontrar ese negocio");
			}
		} catch (NoSuchElementException e) {
			UI.getCurrent().navigate(InicioView.class);
			Notification.show("No se pudo encontrar ese negocio");
		}

		// ------------------------------------------------

		Business business = businessService.getBusinessById(parameter);
		addClassName("dashboard-view");

		HorizontalLayout info = new HorizontalLayout();
		info.setWidthFull();
		info.add(createBusinessCard(business));
		info.add(createFilmsCard(business));

		int users = userService.count(), numTickets = ticketService.count(), suscriptions = 0, numSessions = sessionService.count();

		Iterable<Plan> plan = planService.findAll();
		Iterator<Plan> itPlan = plan.iterator();

		while (itPlan.hasNext()) {
			suscriptions += userService.countByPlan_Id(itPlan.next().getId());
		}

		HorizontalLayout board = new HorizontalLayout();
		board.add(createHighlight("Usuarios", String.valueOf(users), users - 3),
				createHighlight("Entradas vendidas", String.valueOf(numTickets), numTickets - 16),
				createHighlight("Suscripciones a planes", String.valueOf(suscriptions), suscriptions - 2),
				createHighlight("Número de sesiones", String.valueOf(numSessions), numSessions - 4));
		board.setWidthFull();

		add(info, new Hr(), board, new Hr());
	}

	private Aside createBusinessCard(Business business) {
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
			UI.getCurrent().getPage().open("https://goo.gl/maps/GDHRxqihzkriXbdi6", "_blank"); // This has to redirect a
																								// external page
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

	private Aside createFilmsCard(Business business) {
		Collection<Genre> genres = genreService.findAll();
		Iterator<Genre> it = genres.iterator();
		Map<String, Integer> genresSorted = new TreeMap<String, Integer>(Collections.reverseOrder());

		Integer genreId = null;
		while (it.hasNext()) {
			genreId = it.next().getId();
			genresSorted.put(String.valueOf(filmService.countByGenre_Id(genreId)), genreId);
		}

		Aside aside = new Aside();
		aside.addClassNames("bg-contrast-5", "box-border", "p-l", "rounded-l", "sticky");
		aside.setWidth("40%");

		Header headerSection = new Header();
		headerSection.addClassNames("flex", "items-center", "justify-between", "mb-m");

		H3 header = new H3("Géneros más populares");
		header.addClassNames("m-0");
		headerSection.add(header);

		UnorderedList ul = new UnorderedList();
		ul.addClassNames("list-none", "m-0", "p-0", "flex", "flex-col", "gap-m");

		for (Map.Entry<String, Integer> entry : genresSorted.entrySet()) {
			ul.add(createListItem(
					String.valueOf(genreService.getName(entry.getValue()) + " con " + entry.getKey() + " películas")));
		}

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

	private Component createHighlight(String title, String value, Integer percentage) {
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
}