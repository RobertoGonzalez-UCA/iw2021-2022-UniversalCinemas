package com.universalcinemas.application.views.novedades;

import javax.annotation.security.PermitAll;

import com.universalcinemas.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Novedades")
@Route(value = "news", layout = MainLayout.class)
@PermitAll
public class NovedadesView extends VerticalLayout {
	
	public class IconBox extends VerticalLayout
	{
		public IconBox(String icon, String text)
		{
			VerticalLayout mainVerticalLayout = new VerticalLayout();
			mainVerticalLayout.setAlignItems(Alignment.CENTER);
			mainVerticalLayout.add(new Icon(icon));
			mainVerticalLayout.add(new Label(text));
			add(mainVerticalLayout);
		}
	}

	public NovedadesView() {
		// Layouts
		VerticalLayout mainVerticalLayout = new VerticalLayout();
		VerticalLayout verticalLayout1 = new VerticalLayout();
		VerticalLayout verticalLayout2 = new VerticalLayout();
		HorizontalLayout horizontalLayout1 = new HorizontalLayout();
		HorizontalLayout horizontalLayout2 = new HorizontalLayout();
		HorizontalLayout horizontalLayout3 = new HorizontalLayout();

		// Main block
		mainVerticalLayout.add(new H1("Entérate de lo último"));
		mainVerticalLayout.add(new Paragraph(
				"¡Siempre te mantendremos informado de las últimas noticias de nuestro cine, sea lo que sea!"));
		mainVerticalLayout.add(new Paragraph(
				"Nos gusta tener clientes que se comprometan con el mundo cinematográfico, no solo ofrecemos una entrada con palomitas. Queremos darte una de las mejores experiencias y para ello necesitamos que conozcas nuestras ventajas."));

		// Block 1
		verticalLayout1.add(new H1("Nuestras ventajas"));
		verticalLayout1.add(new Paragraph(
				"Tenemos a tu disposición nuestras mejores ventajas para ofrecerte una experiencia incríeble."));
		horizontalLayout3.add(new IconBox("accessibility", "Accesibilidad"));
		horizontalLayout3.add(new IconBox("cluster", "Protocolo COVID"));
		horizontalLayout3.add(new IconBox("cutlery", "Restaurante"));
		horizontalLayout3.add(new IconBox("flash", "Descuento"));
		Image img1 = new Image(
				"https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2670&q=80",
				"personas viend una pelicula");
		img1.setWidth("320px");
		img1.setHeight("240px");

		verticalLayout1.add(horizontalLayout3);
		horizontalLayout1.add(verticalLayout1);
		horizontalLayout1.add(img1);
		mainVerticalLayout.add(horizontalLayout1);

		// Block 2
		verticalLayout2.add(new H1("Protocolo COVID"));
		verticalLayout2.add(new Paragraph(
				"Lo primero es lo primero, tú. Para ello necesitamos mostrarte nuestro protocolo COVID."));
		verticalLayout2.add(new Paragraph(
				"Este protocolo ha sido diseñado con la máxima seguridad para ofrecerte la más segura experiencia cinematográfica que se puede tener en la actualidad."));
		verticalLayout2.add(new Button("Ver protocolo"));
		Image img2 = new Image(
				"https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2670&q=80",
				"personas viend una pelicula");
		img2.setWidth("320px");
		img2.setHeight("240px");

		horizontalLayout2.add(img2);
		horizontalLayout2.add(verticalLayout2);
		mainVerticalLayout.add(horizontalLayout2);
		add(mainVerticalLayout);
	}
}