package com.universalcinemas.application.data.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import com.vaadin.flow.component.notification.Notification;

@Service
public class UserService extends CrudService<User, Integer> implements UserDetailsService {

	private UserRepository repository;
	private PasswordEncoder encoder;

	@Autowired
	public UserService(PasswordEncoder encoder, UserRepository repository) {
		this.repository = repository;
		this.encoder = encoder;
	}

	@Override
	public User loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> user = repository.findByEmail(email);
		if (user.isPresent()) {
			return user.get();
		} else {
			throw new UsernameNotFoundException(email);
		}
	}

	public void registerUser(User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		Notification.show(user.getPassword());
//		Notification.show(user.toString());
		repository.save(user);
//		Notification.show(user.toString());
	}

	public boolean activateUser(String email, String key) {

		Optional<User> user = repository.findByEmail(email);

		if (user.isPresent() && !key.isEmpty()) {
			repository.save(user.get());
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected JpaRepository<User, Integer> getRepository() {
		return null;
	}
	
	public User obtenerDatosUsuario(int id) {
//		Optional<User> usuario = repository.findById(id);
//		return usuario.isPresent() ? usuario.get() : null;
		return repository.findById(id).get();
	}
	
	public void actualizarUsuario(User usuario) {
		repository.save(usuario);
	}
}
