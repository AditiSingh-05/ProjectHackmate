package com.example.HackMateBackend.jwtauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j //why added it now didn't add it before
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException
                         ) throws IOException, ServletException{
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        final Map<String,Object> map = new HashMap<>();
        map.put("status",HttpServletResponse.SC_UNAUTHORIZED);
        map.put("error","Unauthorized");
        map.put("message","You need to login first");
        map.put("path",request.getServletPath()); //The endpoint the user tried to reach.

        //Java object to json
//        When you call mapper.writeValue(response.getOutputStream(), body);, the ObjectMapper:
//        Takes the body map (your response data).
//        Converts it into a JSON string.
//        Writes it directly to the HTTP response output stream, so the client gets well-formatted JSON.
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), map);


    }


}

//each code word by word
//MediaType.APPLICATION_JSON_VALUE:
//A constant for "application/json", the standard content type for JSON.