// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.servlets;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.FilterChain;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.Filter;

public final class GZIPFilter implements Filter
{
    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException, ServletException {
        if (req instanceof HttpServletRequest) {
            final HttpServletRequest request = (HttpServletRequest)req;
            final HttpServletResponse response = (HttpServletResponse)res;
            final String ae = request.getHeader("accept-encoding");
            if (ae != null && ae.toLowerCase().indexOf("gzip") != -1) {
                final GZIPResponseWrapper wrappedResponse = new GZIPResponseWrapper(response);
                chain.doFilter(req, (ServletResponse)wrappedResponse);
                wrappedResponse.finishResponse();
                return;
            }
            chain.doFilter(req, res);
        }
    }
    
    public void init(final FilterConfig filterConfig) {
    }
    
    public void destroy() {
    }
}
