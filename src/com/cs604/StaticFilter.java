package com.cs604;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 * static filter to pass along static content like images and css
 */
@WebFilter("/StaticFilter")
public class StaticFilter implements Filter {
	private RequestDispatcher defaultRequestDispatcher;  

    public StaticFilter() {
    }

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		defaultRequestDispatcher.forward(request, response);
		}

	public void init(FilterConfig fConfig) throws ServletException {
		this.defaultRequestDispatcher = fConfig.getServletContext().getNamedDispatcher("default");
		}

}
