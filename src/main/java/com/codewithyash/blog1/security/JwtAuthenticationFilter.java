package com.codewithyash.blog1.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		
		// get token
		
		String requestToken = request.getHeader("Authorization");
		
		// Print all request headers to debug
		request.getHeaderNames().asIterator().forEachRemaining(header -> 
		    System.out.println(header + " -> " + request.getHeader(header))
		);

		
		
		
		// Bearer 23456sdfg
		
		System.out.println(requestToken);
		
		String username=null;
		
		String token=null;
		
		if(requestToken!=null && requestToken.startsWith("Bearer"))
		{
			token = requestToken.substring(7);
			
			
			try 
			{	
			username = this.jwtTokenHelper.getUsernameFromToken(token);
			}
			catch (IllegalArgumentException e) 
			{
				System.out.println("Unable to get Jwt token");
			}
			catch (ExpiredJwtException e) {
				System.out.println("jwt token has expired");
			}
			catch (MalformedJwtException e) {
				System.out.println("Invalid jwt");
			}
			
		} else {
			
			System.out.println("Jwt token does not begin with Bearer");
		}
		
		// once we get the token , now validate
		
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null )
		{
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			
			if(this.jwtTokenHelper.validateToken(token, userDetails)) 
			{
				//shi chal rha hai 
				// authentication krna hai
				
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails,null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				
			} else 
			{
				System.out.println("Invalid jwt token");
			}
			
		} else {
			System.out.println("username id null or context is not null");
			
		}
		
		filterChain.doFilter(request, response);
		
	}

}
