package org.dareon.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter
{

    @Autowired
    private UserDetailsService userService;

    @Autowired
    public void configureAuth(AuthenticationManagerBuilder auth) throws Exception
    {
	auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
	http.authorizeRequests().antMatchers("/admin/**").hasAuthority("REPO_CREATE_PRIVILEGE").anyRequest().authenticated().and()
		.formLogin().loginPage("/login").usernameParameter("email").permitAll().and().logout()
		.logoutSuccessUrl("/login?logout").permitAll();
	http.authorizeRequests().antMatchers("/webjars/**").permitAll();
	http.csrf().disable();
    }

}
