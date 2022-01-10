package com.universalcinemas.application.views;

import java.util.ArrayList;
import java.util.List;

import com.universalcinemas.application.security.SecurityService;
import com.universalcinemas.application.views.elegirasiento.ElegirasientoView;
import com.universalcinemas.application.views.inicio.InicioView;
import com.universalcinemas.application.views.iniciosesion.IniciosesionView;
import com.universalcinemas.application.views.perfilusuario.PerfilUsuarioView;
import com.universalcinemas.application.views.planes.PlanesView;
import com.universalcinemas.application.views.registro.RegistroView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;

/**
 * The main view is a top-level placeholder for other views.
 */
@PageTitle("Main")
public class MainLayout extends AppLayout {

	private static final long serialVersionUID = 1L;
	private SecurityService securityService;

	public static class MenuItemInfo {

        private String text;
        private String iconClass;
        private Class<? extends Component> view;

        public MenuItemInfo(String text, String iconClass, Class<? extends Component> view) {
            this.text = text;
            this.iconClass = iconClass;
            this.view = view;
        }

        public String getText() {
            return text;
        }

        public String getIconClass() {
            return iconClass;
        }

        public Class<? extends Component> getView() {
            return view;
        }

    }

    private H1 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        addToDrawer(createDrawerContent());
    }
    
    @SuppressWarnings("static-access")
	private Component createMenuBar() {
    	HorizontalLayout vLayoutMain = new HorizontalLayout();
    	vLayoutMain.setMargin(true);
    	
		MenuBar profileMenu = new MenuBar();
		MenuItem item = profileMenu.addItem("[User Name]");

		SubMenu profileSubMenu = item.getSubMenu();
		profileSubMenu.addItem("Perfil", e -> UI.getCurrent().navigate(PerfilUsuarioView.class));
		profileSubMenu.addItem("Elegir plan", e -> UI.getCurrent().navigate(PlanesView.class));
		profileSubMenu.add(new Hr());
		profileSubMenu.addItem("Cerrar sesión", e -> securityService.logout());
		
		vLayoutMain.add(profileMenu);

		return vLayoutMain;
    }

    private Component createHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addClassName("text-secondary");
        toggle.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        toggle.getElement().setAttribute("aria-label", "Menu toggle");
                
        viewTitle = new H1();
        viewTitle.addClassNames("m-0", "text-l");
                
        Header header = new Header(toggle,viewTitle, createMenuBar());
        header.addClassNames("bg-base", "border-b", "border-contrast-10", "box-border", "flex", "h-xl", "items-center",
                "w-full");
        return header;
    }

    private Component createDrawerContent() {
        H2 appName = new H2("UniversalCinemas App");
        appName.addClassNames("flex", "items-center", "h-xl", "m-0", "px-m", "text-m");

        com.vaadin.flow.component.html.Section section = new com.vaadin.flow.component.html.Section(appName,
                createNavigation(), createFooter());
        section.addClassNames("flex", "flex-col", "items-stretch", "max-h-full", "min-h-full");
        return section;
    }

	private Nav createNavigation() {
        Nav nav = new Nav();
        nav.addClassNames("border-b", "border-contrast-10", "flex-grow", "overflow-auto");
        nav.getElement().setAttribute("aria-labelledby", "views");

        H3 views = new H3("Views");
        views.addClassNames("flex", "h-m", "items-center", "mx-m", "my-0", "text-s", "text-tertiary");
        views.setId("views");

        // Wrap the links in a list; improves accessibility
        UnorderedList list = new UnorderedList();
        list.addClassNames("list-none", "m-0", "p-0");
        nav.add(list);

        for (RouterLink link : createLinks()) {
            ListItem item = new ListItem(link);
            list.add(item);
        }
                
        return nav;
    }

    private List<RouterLink> createLinks() {
        MenuItemInfo[] menuItems = new MenuItemInfo[]{ //
                new MenuItemInfo("Registro", "la la-user", RegistroView.class), //

                new MenuItemInfo("Inicio sesión", "la la-user", IniciosesionView.class), //

                new MenuItemInfo("Inicio", "la la-newspaper", InicioView.class), //

                new MenuItemInfo("Planes", "la la-th-list", PlanesView.class), //

                new MenuItemInfo("Elegir asiento", "la la-chair", ElegirasientoView.class), //

        };
        List<RouterLink> links = new ArrayList<>();
        for (MenuItemInfo menuItemInfo : menuItems) {
            links.add(createLink(menuItemInfo));

        }
              
        return links;
    }

    private static RouterLink createLink(MenuItemInfo menuItemInfo) {
        RouterLink link = new RouterLink();
        link.addClassNames("flex", "mx-s", "p-s", "relative", "text-secondary");
        link.setRoute(menuItemInfo.getView());

        Span icon = new Span();
        icon.addClassNames("me-s", "text-l");
        if (!menuItemInfo.getIconClass().isEmpty()) {
            icon.addClassNames(menuItemInfo.getIconClass());
        }

        Span text = new Span(menuItemInfo.getText());
        text.addClassNames("font-medium", "text-s");

        link.add(icon, text);
        return link;
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        layout.addClassNames("flex", "items-center", "my-s", "px-m", "py-xs");

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
