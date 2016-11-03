package com.pimp.config;

import com.pimp.commons.filters.SimpleCorsFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);

        http
                .authorizeRequests().anyRequest().permitAll()
            .and()
                .addFilterBefore(new SimpleCorsFilter(), SecurityContextPersistenceFilter.class).antMatcher("/api/*");
    }
}
