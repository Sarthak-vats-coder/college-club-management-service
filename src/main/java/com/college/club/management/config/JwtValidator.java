package com.college.club.management.config;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.college.club.management.entities.User;
import com.college.club.management.repositories.UserRepository;
import com.mongodb.lang.NonNull;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtValidator extends OncePerRequestFilter {

	JwtProvider jwtProvider;
	UserRepository userRepository;

	public JwtValidator(JwtProvider jwtProvider, UserRepository userRepository) {
		this.jwtProvider = jwtProvider;
		this.userRepository = userRepository;

	}

	public Cookie getAuthCookie(HttpServletRequest request) {
		if (request == null || request.getCookies() == null || request.getCookies().length == 0
				|| request.getCookies()[0].getValue() == null)
			return null;

		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("auth_token"))
				return cookie;
		}
		return null;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {

		Cookie authCookie = getAuthCookie(request);

		if (authCookie == null) {
			System.out.println("you don't have cookie");
			filterChain.doFilter(request, response);
			return;
		}
		System.out.println("You seem to have cookie, so lets check");
		final String jwt = authCookie.getValue();
		String username = jwtProvider.extractUsername(jwt);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (username != null && authentication == null) {
			Optional<User> user = userRepository.findByUsername(username);
			if (user.isPresent() && jwtProvider.validateToken(jwt, username)) {
				var authorities = user.get().getRoles().stream()
						.map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName())).collect(Collectors.toList());
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.get(),
						null, authorities);

				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}

		filterChain.doFilter(request, response);

	}

}
