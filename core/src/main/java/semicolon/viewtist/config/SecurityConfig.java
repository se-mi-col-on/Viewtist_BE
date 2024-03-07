package semicolon.viewtist.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import semicolon.viewtist.jwt.AuthenticationFilter;
import semicolon.viewtist.oauth.handler.OAuth2SuccessHandler;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private final OAuth2SuccessHandler oAuth2SuccessHandler;
  private final DefaultOAuth2UserService defaultOAuth2UserService;
  private final AuthenticationFilter authenticationFilter;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .httpBasic(AbstractHttpConfigurer::disable)
        .headers(c -> c.frameOptions(
            HeadersConfigurer.FrameOptionsConfig::disable).disable())
        .sessionManagement(c ->
            c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        .authorizeHttpRequests((request) ->
            request.requestMatchers(
                    new AntPathRequestMatcher("/**")
                ).permitAll()
                .anyRequest().authenticated()
        )
        .oauth2Login(oauth2 -> oauth2
            .authorizationEndpoint(endpoint -> endpoint.baseUri("/api/users/oauth2"))
            .redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/callback/*"))
            .userInfoEndpoint(endpoint -> endpoint.userService(defaultOAuth2UserService))
            .successHandler(oAuth2SuccessHandler)
        )

        .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}



