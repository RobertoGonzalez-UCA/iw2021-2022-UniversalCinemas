package com.universalcinemas.application.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

import junit.framework.Assert;

class RegistroViewTest extends TextFieldElement{

	@Test
	public void fillForm() {
	    //$(TextFieldElement.class).id("vaadin-text-field-input-0").setValue("John");
	    //$(TextFieldElement.class).id("vaadin-text-field-input-1").setValue("Doe");
	    $(ButtonElement.class).id("button").click();
	    assertEquals("Introduce tu nombre", $(DivElement.class).id("result"));
	}
}