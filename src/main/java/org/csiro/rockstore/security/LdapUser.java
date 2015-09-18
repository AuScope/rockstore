package org.csiro.rockstore.security;
import java.util.Collection;

import org.csiro.rockstore.entity.postgres.UserPermission;
import org.csiro.rockstore.entity.service.UserPermissionEntityService;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetails;

  public class LdapUser implements LdapUserDetails {

     private static final long serialVersionUID = 1L;

     private LdapUserDetails details;     
     private String name;
     private String userName;
     private String email;     
     private UserPermission userPermission;
     UserPermissionEntityService userPermissionEntityService;

     public LdapUser(LdapUserDetails details, String name, String email) {
         this.details = details;
         this.name = name;
         this.email = email;
         this.userName = details.getUsername();
         userPermissionEntityService = new UserPermissionEntityService(); 
         this.userPermission = userPermissionEntityService.searchUser(details.getUsername());
     }

     public boolean isEnabled() {
         return details.isEnabled();
     }
     
     public UserPermission getUserPermission(){
    	 return userPermission;
     }
     
     public String getName(){
    	 return name;
     }
     
     public String getUserName(){
    	 return userName;
     }
     
     public String getEmail(){
    	 return email;
     }

     public String getDn() {
         return details.getDn();
     }

     public Collection<? extends GrantedAuthority> getAuthorities() {
         return details.getAuthorities();
     }

     public String getPassword() {
         return details.getPassword();
     }

     public String getUsername() {
         return details.getUsername();
     }

     public boolean isAccountNonExpired() {
         return details.isAccountNonExpired();
     }

     public boolean isAccountNonLocked() {
         return details.isAccountNonLocked();
     }

     public boolean isCredentialsNonExpired() {
         return details.isCredentialsNonExpired();
     }
  
  }