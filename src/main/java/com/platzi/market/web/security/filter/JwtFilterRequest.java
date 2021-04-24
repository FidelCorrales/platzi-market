package com.platzi.market.web.security.filter;

import com.platzi.market.domain.service.PlatziUserDetailsService;
import com.platzi.market.web.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilterRequest extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private PlatziUserDetailsService platziUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        //Si lo que viene en el encabezado es un token
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            String jwt = authorizationHeader.substring(7);
            String username = jwtUtil.extractUsername(jwt);

            //Si el usuario no se ha logeado (no tiene autenticacion)
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = platziUserDetailsService.loadUserByUsername(username);

                if ( jwtUtil.validateToken(jwt, userDetails) ){
                    //levantar sesión para usuario
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    //agregar detalles de la conexion que esta recibion
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    //Asignar autenticacion para que la siguiente vez no tenga que pasar por este filtro
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        //filtro se evaluado con
        filterChain.doFilter(request, response);
    }
}
