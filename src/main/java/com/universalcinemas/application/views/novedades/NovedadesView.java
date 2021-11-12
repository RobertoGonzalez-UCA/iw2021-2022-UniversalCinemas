package com.universalcinemas.application.views.novedades;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.universalcinemas.application.views.MainLayout;

@PageTitle("Novedades")
@Route(value = "news", layout = MainLayout.class)
@Tag("novedades-view")
@JsModule("./views/novedades/novedades-view.ts")
public class NovedadesView extends LitTemplate implements HasComponents, HasStyle {

    @Id
    private Select<String> sortBy;

    public NovedadesView() {}
}