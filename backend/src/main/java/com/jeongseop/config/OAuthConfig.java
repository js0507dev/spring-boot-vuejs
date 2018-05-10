package com.jeongseop.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@Configuration
public class OAuthConfig {

	@Configuration
	@EnableResourceServer
	protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.headers().frameOptions().disable();
			
			http.authorizeRequests()
				.antMatchers("/api/**").hasRole("USER")
				.antMatchers("/**").permitAll();
		}
	}

	@Configuration
	@EnableAuthorizationServer
	protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
		@Bean
		public TokenStore JdbcTokenStore(DataSource dataSource) {
			return new JdbcTokenStore(dataSource);
		}

		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			clients
				.inMemory()
				.withClient("cli")
				.autoApprove(true)
				.authorizedGrantTypes("implicit")
				.scopes("read", "write");
		}
	}
}
