package com.universalcinemas.application.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.universalcinemas.application.views.iniciosesion.IniciosesionView;
import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;

@EnableWebSecurity 
@Configuration
public class SecurityConfig extends VaadinWebSecurityConfigurerAdapter { 
	
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(11);
	}
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);

        setLoginView(http, IniciosesionView.class); 
    }

    /**
     * Allows access to static resources, bypassing Spring security.
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/images/**"); 
        super.configure(web);
    }

    /**
     * Demo UserDetailService which only provides two hardcoded
     * in memory users and their roles.
     * NOTE: This should not be used in real-world applications.
     */
//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        return new InMemoryUserDetailsManager(User.withUsername("user") 
//            .password("{noop}userpass")
//            .roles("USER")
//            .build());
//    }
}