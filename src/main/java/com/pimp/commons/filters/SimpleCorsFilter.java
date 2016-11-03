package com.pimp.commons.filters;

import com.pimp.commons.filters.HttpFilter;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Kevin Goy
 */
public class SimpleCorsFilter extends HttpFilter {
  public SimpleCorsFilter() {
  }

  public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException,
      ServletException {
    if(request.getHeader("ORIGIN") != null) {
      response.setHeader("Access-Control-Allow-Origin", request.getHeader("ORIGIN"));
    }

    response.setHeader("Access-Control-Allow-Methods", "POST, GET, PATCH, OPTIONS, DELETE, PUT");
    response.setHeader("Access-Control-Max-Age", "3600");
    response.setHeader("Access-Control-Allow-Headers", "x-auth-token, Authorization, Content-Type");
    response.setHeader("Access-Control-Expose-Headers", "x-auth-token");
    response.setHeader("Access-Control-Allow-Credentials", "true");
    chain.doFilter(request, response);
  }

}
