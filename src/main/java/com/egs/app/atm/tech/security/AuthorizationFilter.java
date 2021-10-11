package com.egs.app.atm.tech.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AuthorizationFilter extends BasicAuthenticationFilter {
    Environment environment;

    public AuthorizationFilter(AuthenticationManager authenticationManager,
                               Environment environment) {
        super(authenticationManager);
        this.environment = environment;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authorizationHeader = request.getHeader(environment.getProperty("authorization.token.header.name"));
        if (authorizationHeader == null || !authorizationHeader.startsWith(environment.getProperty("authorization.token.header.prefix"))) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);


    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(environment.getProperty("authorization.token.header.name"));
        if (authorizationHeader == null) {
            return null;
        }
        String token = authorizationHeader.replace(environment.getProperty("authorization.token.header.prefix"), "");
        String userId = Jwts.parser()
                .setSigningKey(environment.getProperty("token.secret") + (request.getHeader("x-forwarded-for")==null?request.getRemoteAddr():request.getHeader("x-forwarded-for")))
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        if (userId == null) {
            return null;
        }
        JwtParser jwtParser = Jwts.parser().setSigningKey(environment.getProperty("token.secret") + (request.getHeader("x-forwarded-for")==null?request.getRemoteAddr():request.getHeader("x-forwarded-for")));

        Jws claimsJws = jwtParser.parseClaimsJws(token);
        Claims claims = (Claims) claimsJws.getBody();
        Collection authorities =
                Arrays.stream(claims.get("scopes").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
        updatedAuthorities.addAll(authorities);
        return new UsernamePasswordAuthenticationToken(userId, null, updatedAuthorities);
    }
}