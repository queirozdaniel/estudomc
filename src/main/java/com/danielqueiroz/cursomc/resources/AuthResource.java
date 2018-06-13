package com.danielqueiroz.cursomc.resources;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.danielqueiroz.cursomc.dto.EmailDTO;
import com.danielqueiroz.cursomc.security.JWTUtil;
import com.danielqueiroz.cursomc.security.UserSS;
import com.danielqueiroz.cursomc.services.AuthService;
import com.danielqueiroz.cursomc.services.UserService;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {

	@Autowired
	private JWTUtil jwtUtil;
	@Autowired
	private AuthService authService;
	
	@RequestMapping(value = "/refresh_token", method = RequestMethod.POST)
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		UserSS user = UserService.authenticated();
		String token = jwtUtil.generateToken(user.getUsername());
		response.addHeader("Authorization", "Bearer " + token);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "/forgot", method = RequestMethod.POST)
	public ResponseEntity<Void> forgot(@RequestBody EmailDTO emailDto) {
		authService.sendNewPassword(emailDto.getEmail());
		return ResponseEntity.noContent().build();
	}

}
