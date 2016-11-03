package com.pimp.commons;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Kevin Goy
 */
public abstract class HttpFilter implements Filter {
  public HttpFilter() {
  }

  public void init(FilterConfig filterConfig) throws ServletException {
  }

  public void destroy() {
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    if(!(request instanceof HttpServletRequest)) {
      throw new IllegalArgumentException("request is not a http request");
    } else if(!(response instanceof HttpServletResponse)) {
      throw new IllegalArgumentException("response is not a http response");
    } else {
      this.doFilter((HttpServletRequest)request, (HttpServletResponse)response, chain);
    }
  }

  protected abstract void doFilter(HttpServletRequest var1, HttpServletResponse var2, FilterChain var3) throws IOException, ServletException;
}
