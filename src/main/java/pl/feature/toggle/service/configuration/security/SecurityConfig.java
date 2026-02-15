package pl.feature.toggle.service.configuration.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.context.annotation.RequestScope;
import pl.feature.toggle.service.model.security.actor.ActorProvider;
import pl.feature.toggle.service.model.security.correlation.CorrelationProvider;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
class SecurityConfig {

    private static final String ROLE_PREFIX = "ROLE_";
    private static final String REALM_ACCESS_CLAIM = "realm_access";
    private static final String ROLES_CLAIM = "roles";

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().permitAll()
                )
//                .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()))
                .build();
    }

    @Bean
    Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        return jwt -> new JwtAuthenticationToken(jwt, extractRealmRoles(jwt));
    }

    @Bean
    ActorProvider actorProvider() {
        return new JwtActorProvider();
    }

    @Bean
    @RequestScope
    CorrelationProvider correlationProvider(HttpServletRequest request) {
        return new HttpCorrelationProvider(request);
    }

    private Collection<SimpleGrantedAuthority> extractRealmRoles(Jwt jwt) {
        Object claim = jwt.getClaim(REALM_ACCESS_CLAIM);
        if (!(claim instanceof Map<?, ?> realmAccess)) {
            return Set.of();
        }

        Object roles = realmAccess.get(ROLES_CLAIM);
        if (!(roles instanceof Collection<?> roleList)) {
            return Set.of();
        }

        return roleList.stream()
                .map(Object::toString)
                .map(r -> ROLE_PREFIX + r)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableSet());
    }

}
