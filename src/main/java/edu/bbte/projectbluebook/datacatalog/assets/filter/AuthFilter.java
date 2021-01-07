package edu.bbte.projectbluebook.datacatalog.assets.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bbte.projectbluebook.datacatalog.assets.model.TokenInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

@Component
@PropertySource("classpath:application.properties")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        // Check authorization header
        boolean hasActiveToken = false;
        TokenInfoResponse.RoleEnum role = null;
        TokenInfoResponse tokenInfoResponse = null;
        String authorization = httpServletRequest.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String authToken = authorization.substring(7);
            tokenInfoResponse = getTokenInfo(authToken);
            if (tokenInfoResponse.getActive()) {
                hasActiveToken = true;
                role = tokenInfoResponse.getRole();
            }
        }

        // Check for endpoints

        String method = httpServletRequest.getMethod().toUpperCase(new Locale("en", "us"));
        // Delete asset - owner or admin
        if ("DELETE".equals(method) && httpServletRequest.getRequestURI().startsWith("/assets")
            && !httpServletRequest.getRequestURI().contains("/tags/")) {
            if (!hasActiveToken) {
                sendResponse(401, "Unauthorized, token not active", httpServletResponse);
            } else if (role.equals(TokenInfoResponse.RoleEnum.ADMIN)) {
                httpServletRequest.setAttribute("userId", tokenInfoResponse.getUserId());
                httpServletRequest.setAttribute("role", "admin");
                chain.doFilter(httpServletRequest, httpServletResponse);
            } else if (role.equals(TokenInfoResponse.RoleEnum.USER)) {
                httpServletRequest.setAttribute("userId", tokenInfoResponse.getUserId());
                httpServletRequest.setAttribute("role", "user");
                chain.doFilter(httpServletRequest, httpServletResponse);
            } else {
                sendResponse(401, "Unauthorized, token info error, " + tokenInfoResponse.getUserId()
                    + " " + tokenInfoResponse.getRole() + " " + role, httpServletResponse);
            }
            return;
        }

        // Patch asset - owner or admin
        if ("PATCH".equals(method)) {
            if (!hasActiveToken) {
                sendResponse(401, "Unauthorized, token not active", httpServletResponse);
            } else if (role.equals(TokenInfoResponse.RoleEnum.ADMIN)) {
                httpServletRequest.setAttribute("userId", tokenInfoResponse.getUserId());
                httpServletRequest.setAttribute("role", "admin");
                chain.doFilter(httpServletRequest, httpServletResponse);
            } else if (role.equals(TokenInfoResponse.RoleEnum.USER)) {
                httpServletRequest.setAttribute("userId", tokenInfoResponse.getUserId());
                httpServletRequest.setAttribute("role", "user");
                chain.doFilter(httpServletRequest, httpServletResponse);
            } else {
                sendResponse(401, "Unauthorized, token info error, " + tokenInfoResponse.getUserId()
                    + " " + tokenInfoResponse.getRole() + " " + role, httpServletResponse);
            }
            return;
        }


        // Add tag - owner or admin
        if ("POST".equals(method)) {
            if (!hasActiveToken) {
                sendResponse(401, "Unauthorized, token not active", httpServletResponse);
            } else if (role.equals(TokenInfoResponse.RoleEnum.ADMIN)) {
                httpServletRequest.setAttribute("userId", tokenInfoResponse.getUserId());
                httpServletRequest.setAttribute("role", "admin");
                chain.doFilter(httpServletRequest, httpServletResponse);
            } else if (role.equals(TokenInfoResponse.RoleEnum.USER)) {
                httpServletRequest.setAttribute("userId", tokenInfoResponse.getUserId());
                httpServletRequest.setAttribute("role", "user");
                chain.doFilter(httpServletRequest, httpServletResponse);
            } else {
                sendResponse(401, "Unauthorized, token info error, " + tokenInfoResponse.getUserId()
                    + " " + tokenInfoResponse.getRole() + " " + role, httpServletResponse);
            }
            return;
        }

        // Remove tag - owner or admin

        if ("DELETE".equals(method) && httpServletRequest.getRequestURI().startsWith("/assets")
            && httpServletRequest.getRequestURI().contains("/tags/")) {
            if (!hasActiveToken) {
                sendResponse(401, "Unauthorized, token not active", httpServletResponse);
            } else if (role.equals(TokenInfoResponse.RoleEnum.ADMIN)) {
                httpServletRequest.setAttribute("userId", tokenInfoResponse.getUserId());
                httpServletRequest.setAttribute("role", "admin");
                chain.doFilter(httpServletRequest, httpServletResponse);
            } else if (role.equals(TokenInfoResponse.RoleEnum.USER)) {
                httpServletRequest.setAttribute("userId", tokenInfoResponse.getUserId());
                httpServletRequest.setAttribute("role", "user");
                chain.doFilter(httpServletRequest, httpServletResponse);
            } else {
                sendResponse(401, "Unauthorized, token info error, " + tokenInfoResponse.getUserId()
                    + " " + tokenInfoResponse.getRole() + " " + role, httpServletResponse);
            }
            return;
        }

        // Everything else
        if (!hasActiveToken) {
            httpServletRequest.setAttribute("userId", "");
            httpServletRequest.setAttribute("role", "");
            chain.doFilter(httpServletRequest, httpServletResponse);
        } else if (role.equals(TokenInfoResponse.RoleEnum.ADMIN)) {
            httpServletRequest.setAttribute("userId", tokenInfoResponse.getUserId());
            httpServletRequest.setAttribute("role", "admin");
            chain.doFilter(httpServletRequest, httpServletResponse);
        } else if (role.equals(TokenInfoResponse.RoleEnum.USER)) {
            httpServletRequest.setAttribute("userId", tokenInfoResponse.getUserId());
            httpServletRequest.setAttribute("role", "user");
            chain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

    private static TokenInfoResponse getTokenInfo(String token) throws IOException {

        URL url = new URL("http://datacatalogregistryuserhandletest.azurewebsites.net/token_info");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        OutputStream os = connection.getOutputStream();
        os.write(token.getBytes());
        os.flush();
        os.close();
        StringBuffer response = new StringBuffer();
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;


            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.toString(), TokenInfoResponse.class);
    }

    private void sendResponse(int statusCode, String message, HttpServletResponse httpServletResponse)
        throws IOException {
        httpServletResponse.setStatus(statusCode);
        httpServletResponse.getWriter().write(message);
    }
}
