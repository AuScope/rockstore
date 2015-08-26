package org.csiro.rockstore.security;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
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
		 .antMatchers("/restricted/**").authenticated()
		 .and()
		    .formLogin()
		    	.usernameParameter("j_username") // default is username
                .passwordParameter("j_password") // default is password
		    	.loginPage("/views/login.html").successHandler(new CustomSuccessHandler()).failureUrl("/views/login.html?failure")		   		
		 .and()
		    .logout().logoutSuccessUrl("/")
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
	 
		auth.ldapAuthentication()
        .userDnPatterns("ou=Users").userSearchFilter("(&(objectClass=inetOrgPerson)(uid={0}))")  
        .groupRoleAttribute("cn").groupSearchBase("ou=Groups").groupSearchFilter("(&(memberUid={1}))")
        .contextSource(getLdapContextSource());            
          
		
	}	
	
	private LdapContextSource getLdapContextSource() throws Exception {
        LdapContextSource cs = new LdapContextSource();
        cs.setUrl("ldaps://cg-admin.arrc.csiro.au:636");
        cs.setBase("dc=arrc,dc=csiro,dc=au");     
        cs.afterPropertiesSet();
        return cs;
    }
	
	
	protected class CustomSuccessHandler implements AuthenticationSuccessHandler{
		
		@Override  
	    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,  
	                                        HttpServletResponse httpServletResponse,  
	                                        Authentication authentication)  
	            throws IOException, ServletException {  
			
			HttpSession session = httpServletRequest.getSession();
			LdapUserDetailsImpl authUser = (LdapUserDetailsImpl) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			session.setAttribute("username", authUser.getUsername());
			session.setAttribute("authorities", authentication.getAuthorities());

			// set our response to OK status
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			httpServletResponse.setContentType("text/html; charset=UTF-8");			
			Gson gson = new Gson();
			Map<String,String> result = new HashMap<String,String>();
			
			if(!checkRole(authUser.getAuthorities(),"ROLE_ROCKSTORE")){
				result.put("restricted", "Missing required permission");
				SecurityContextHolder.getContext().setAuthentication(null);
			}else{
				result.put("name", authUser.getUsername());
			}
			httpServletResponse.getWriter().write(gson.toJson(result));
	    }  
		
		private boolean checkRole(Collection<? extends GrantedAuthority> authorities,String role){
			for(GrantedAuthority g:authorities){
				if(g.getAuthority().equals(role)){
					return true;
				}
			}			
			return false;
		}
	}  
		
	
}