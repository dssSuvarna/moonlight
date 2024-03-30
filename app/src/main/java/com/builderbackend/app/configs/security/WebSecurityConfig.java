package com.builderbackend.app.configs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.builderbackend.app.configs.CustomCorsConfiguration;
import com.builderbackend.app.repositories.UserRepository;
import com.builderbackend.app.models.User;

import java.util.Enumeration;

import org.springframework.util.StringUtils;

//import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.function.Supplier;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;

import java.io.IOException;

/*
 * This is our main security config.
 * 
 * We are providing a custome userDetailsService to our DaoAuthenticationProvider since we are using a 
 * custome user table.
 *   
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    // should this be the interface name instead? "UserDetailsService"
    @Autowired
    MyDatabaseUserDetailsService myDatabaseUserDetailsService;

    @Autowired
    private CustomCorsConfiguration customCorsConfiguration;

    @Autowired
    UserRepository userRepository;

    @Value("${login.path}")
    String loginPath;

    @Bean
    public UserDetailsService userDetailsService() {
        return myDatabaseUserDetailsService;
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
    }

    // todo: address the warnings related to `.cors()` and `.and()`

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(customCorsConfiguration.corsConfigurationSource())
                .and()
                .csrf((csrf) -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler()))
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/error", "/login", "/welcome", "/api/is-authenticated", "/ForgotPassword/**", "/logo/**", "/uploads/logos/**")
                        .permitAll()
                        .requestMatchers("/Employee/**").hasAuthority("ROLE_employee_admin")
                        // .anyRequest().authenticated()
                        .requestMatchers("/uploads/**", "password/**").authenticated()
                        .requestMatchers("/projectInfo/**", "/client-business/**", "client-selections/**",
                                "/client-document-sharing/**", "/client-project-update/**", "client-todo-list/**",
                                "client-notification/**", "clientPortal-user/**", "/clientMessages/**",
                                "/client-calander-event/**")
                        .hasAuthority("ROLE_client")
                        .anyRequest().hasAnyAuthority("ROLE_employee", "ROLE_employee_admin"))
                .formLogin(form -> form
                        .loginPage(loginPath)
                        .loginProcessingUrl("/login")
                        .successHandler((request, response, authentication) -> {
                            // todo: move to a seprate function

                            // get user
                            User user = userRepository.getReferenceByUserName(request.getParameter("username"));
                            if (user != null) {
                                // Create new cookies
                                Cookie userIdCookie = new Cookie("userId", user.getUserId());
                                Cookie businessIdCookie = new Cookie("businessId", user.getBusiness().getBusinessId());

                                // Set cookie properties
                                userIdCookie.setMaxAge(7 * 24 * 60 * 60); // Set cookie expiration to 7 days
                                businessIdCookie.setMaxAge(7 * 24 * 60 * 60); // Set cookie expiration to 7 days

                                userIdCookie.setHttpOnly(false); // we need the cookie to be accessable by js
                                businessIdCookie.setHttpOnly(false); // we need the cookie to be accessable by js

                                userIdCookie.setPath("/"); // Set the cookie path
                                businessIdCookie.setPath("/"); // Set the cookie path

                                // userIdCookie.setSecure(true); // If your application is served over HTTPS,
                                // this ensures the cookie is only sent over HTTPS.

                                // Add the cookie to the response
                                response.addCookie(userIdCookie);
                                response.addCookie(businessIdCookie);
                            }

                            response.setStatus(HttpServletResponse.SC_OK);
                        })
                        // .defaultSuccessUrl("http://localhost:5000/projects")
                        .failureHandler((request, response, exception) -> {
                            // specifying that we should send a 401 status code on login failures
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 status code
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write("{\"error\": \"Authentication failed\"}");
                        })
                        .permitAll())
                .logout((logout) -> logout
                        .logoutSuccessUrl("/")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            // clear cookies on logout
                            Cookie jsessionId = new Cookie("JSESSIONID", "");
                            jsessionId.setPath("/");
                            jsessionId.setMaxAge(0);
                            jsessionId.setHttpOnly(true);

                            Cookie userIdCookie = new Cookie("userId", "");
                            userIdCookie.setPath("/");
                            userIdCookie.setMaxAge(0);
                            userIdCookie.setHttpOnly(false);

                            Cookie businessIdCookie = new Cookie("businessId", "");
                            businessIdCookie.setPath("/");
                            businessIdCookie.setMaxAge(0);
                            businessIdCookie.setHttpOnly(false);

                            response.addCookie(jsessionId);
                            response.addCookie(userIdCookie);
                            response.addCookie(businessIdCookie);
                        }));
        return http.build();
    }

    // cors config is now in customCorsConfigurationClass
    /*
     * @Bean
     * public CorsConfigurationSource corsConfigurationSource() {
     * final CorsConfiguration configuration = new CorsConfiguration();
     * configuration.setAllowedOrigins(Arrays.asList("http://localhost:5000"));
     * configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE",
     * "OPTIONS"));
     * configuration.setAllowCredentials(true);
     * configuration.setAllowedHeaders(Arrays.asList("Authorization",
     * "Cache-Control", "Content-Type", "x-xsrf-token", "'X-XSRF-TOKEN"));
     * final UrlBasedCorsConfigurationSource source = new
     * UrlBasedCorsConfigurationSource();
     * source.registerCorsConfiguration("/**", configuration);
     * return source;
     * }
     */

}

// I am adding the default CSRF configs for Single-Page-Applications (SPA)
// https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#:~:text=can%20be%20used%3A-,Configure%20CSRF%20for%20Single%2DPage%20Application,-Java

final class SpaCsrfTokenRequestHandler extends CsrfTokenRequestAttributeHandler {
    private final CsrfTokenRequestHandler delegate = new XorCsrfTokenRequestAttributeHandler();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
        /*
         * Always use XorCsrfTokenRequestAttributeHandler to provide BREACH protection
         * of
         * the CsrfToken when it is rendered in the response body.
         */
        this.delegate.handle(request, response, csrfToken);
    }

    @Override
    public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
        /*
         * If the request contains a request header, use
         * CsrfTokenRequestAttributeHandler
         * to resolve the CsrfToken. This applies when a single-page application
         * includes
         * the header value automatically, which was obtained via a cookie containing
         * the
         * raw CsrfToken.
         */
        if (StringUtils.hasText(request.getHeader(csrfToken.getHeaderName()))) {
            return super.resolveCsrfTokenValue(request, csrfToken);
        }
        /*
         * In all other cases (e.g. if the request contains a request parameter), use
         * XorCsrfTokenRequestAttributeHandler to resolve the CsrfToken. This applies
         * when a server-side rendered form includes the _csrf request parameter as a
         * hidden input.
         */
        return this.delegate.resolveCsrfTokenValue(request, csrfToken);
    }
}

final class CsrfCookieFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        // Render the token value to a cookie by causing the deferred token to be loaded
        csrfToken.getToken();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println("Header Name - " + headerName + ", Value - " + request.getHeader(headerName));
        }
        filterChain.doFilter(request, response);
    }
}
