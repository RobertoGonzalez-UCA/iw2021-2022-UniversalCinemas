package com.universalcinemas.application.test;

import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.universalcinemas.application.data.user.User;
import com.universalcinemas.application.data.user.UserService;
import com.universalcinemas.application.views.registro.RegistroView;

import org.springframework.test.context.junit4.SpringRunner;

import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

import junit.framework.Assert;

@RunWith(SpringRunner.class) 
@SpringBootTest
class RegistroViewTest extends TextFieldElement{
/*	String name;
	String surname;
	String birthDate;
	String phoneNumber;
	String email;
	String password1;
	String password2;
	@Autowired
    private RegistroView registroView;
	private UserService userService;
	
	@Before 
	public void setupData() {
		name = "Juan";
		surname = "Pérez";
		birthDate = "10/10/2000";
		email = "asd@gmail.com";
		password1 = "123";
		password2 = "123";
		registroView = mock(RegistroView.class);
	}


    @Test
    public void registroTest() {
        registroView.register(name, surname, birthDate, phoneNumber, email, password1, password2);
        List<User> users = userService.findAll();

        //Assert.assertFalse(form.isVisible());
        //Assert.assertTrue(form.isVisible());
        Assert.assertEquals(users.get(users.size() - 1).getName(), name);
    }
*/
}