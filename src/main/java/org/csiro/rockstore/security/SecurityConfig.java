package org.csiro.rockstore.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.ui.ModelMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;


@Configuration
@EnableWebSecurity	
public  class SecurityConfig extends
		WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {		
		http.authorizeRequests()
		 .antMatchers("/collections.html","/subcollections.html","/samples.html").authenticated()	
		 .and()
		    .formLogin()
		    	.usernameParameter("j_username") // default is username
                .passwordParameter("j_password") // default is password
		    	.loginPage("/login.html").successHandler(new CustomSuccessHandler()).failureUrl("/index.html")		   		
		 .and()
		    .logout().logoutSuccessUrl("/index.html")
		    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		 .and()
		    .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
			.csrf().csrfTokenRepository(csrfTokenRepository());
		
		
	}
	
	private CsrfTokenRepository csrfTokenRepository() {
		  HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		  repository.setHeaderName("X-XSRF-TOKEN");
		  return repository;
		}
	
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	  auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");		  
	}	
	
	
	protected class CustomSuccessHandler implements AuthenticationSuccessHandler{
		
		@Override  
	    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,  
	                                        HttpServletResponse httpServletResponse,  
	                                        Authentication authentication)  
	            throws IOException, ServletException {  
			
			HttpSession session = httpServletRequest.getSession();
			User authUser = (User) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			session.setAttribute("username", authUser.getUsername());
			session.setAttribute("authorities", authentication.getAuthorities());

			// set our response to OK status
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			httpServletResponse.setContentType("text/html; charset=UTF-8");
			String referrer = httpServletRequest.getHeader("referer");
			Gson gson = new Gson();
			Map<String,String> result = new HashMap<String,String>();
			
			
			
			if(referrer.endsWith("/login.html")){								
				result.put("redirect", (httpServletRequest.getContextPath() + "/index.html"));				
			}else{
				result.put("redirect", (referrer));
			}
			
			httpServletResponse.getWriter().write(gson.toJson(result));
	    }  
	}  
		
	
}