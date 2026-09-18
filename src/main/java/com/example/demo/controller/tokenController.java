package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.demo.model.AuthenticationReq;
import com.example.demo.model.TokenInfo;
import com.example.demo.service.JwtUtilService;
@RestController
@RequestMapping("/api")

public class tokenController {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	UserDetailsService usuarioDetailsService;
	@Autowired
	private JwtUtilService jwtUtilService;


	  @PostMapping("/autenticarToken")
	  public ResponseEntity<TokenInfo> authenticate(@RequestBody AuthenticationReq req) {
	    authenticationManager.authenticate(
	      new UsernamePasswordAuthenticationToken(req.getUsuario(), req.getClave())
	    );
	    final UserDetails userDetails = usuarioDetailsService.loadUserByUsername(req.getUsuario());
	    final String jwt = jwtUtilService.generateToken(userDetails);
	    return ResponseEntity.ok(new TokenInfo(jwt));
	  }
}
