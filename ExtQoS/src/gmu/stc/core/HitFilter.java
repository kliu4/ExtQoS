package gmu.stc.core;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Servlet Filter implementation class MyFilter
 */
@WebFilter("/MyFilter")
public class HitFilter implements Filter {

	private FilterConfig filterConfig = null;
	public static final String WEBSITE_ATTRIBUTE = "website";
    /**
     * Default constructor. 
     */
    public HitFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
		this.filterConfig = null;
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@SuppressWarnings("null")
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		if (filterConfig == null)
	         return;
		ServletContext sc = filterConfig.getServletContext();
	    sc.getRealPath("/");
	    HttpServletRequest req = (HttpServletRequest)request;
	    req.getSession(true);    
	    if(request.getAttribute(WEBSITE_ATTRIBUTE)==null){
	    	
	    	WebSite website = new WebSite(sc.getRealPath("/"));
	    	request.setAttribute(WEBSITE_ATTRIBUTE, website);
	    }
	    	
	    

	    
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
		this.filterConfig = fConfig;
	}

}
