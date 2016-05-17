package com.buildit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import static org.springframework.http.HttpMethod.OPTIONS;

/**
 * Created by rain on 29.04.16.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    DataSource dataSource;

    @Autowired
    void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(
                        "select username, password, enabled from users where username = ?"
                )
                .authoritiesByUsernameQuery(
                        "select username, authority from authorities where username = ?"
                );

    }

    @Configuration
    static class WebFormsSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter{
        protected void configure(HttpSecurity http) throws Exception{
            http.authorizeRequests()
                    .antMatchers("/say*/**").authenticated()
                    .antMatchers("/welcome").authenticated()
                    .and()
                    .formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/welcome")
                    .and()
                    .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
        }
    }

    @Configuration
    @Order(1)
    static class WebApiSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter{
        protected void configure(HttpSecurity http) throws Exception{
            http.csrf().disable()
                    .antMatcher("/api/**").authorizeRequests()
                    .antMatchers(OPTIONS, "/api/**").permitAll()
                    .antMatchers("/api/**").authenticated()
                .and().httpBasic()
                .authenticationEntryPoint((req, res, exc) ->
                        res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not authorized")
                );
        }
    }

    @Configuration
    static class ClientWebConfig extends WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter{
        public void addViewControllers(ViewControllerRegistry registry){
            registry.addViewController("/login").setViewName("login");

        }
    }
}
