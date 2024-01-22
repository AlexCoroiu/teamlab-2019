/*
 *     TeamLab
 *     Copyright (C) 2019  Alexandra Coroiu, Ciprian Lăzăroaia
 *     a.coroiu@student.utwente.nl, c.lazaroaia@student.utwente.nl
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You have received a copy of the GNU Affero General Public License
 *     along with this program.  Alternatively, see <http://www.gnu.org/licenses/>.
 */

package nl.utwente.teamlab.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Used to redirect users to the login page if they try to
 * access the webapp without being logged in.
 *
 */
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rep = (HttpServletResponse) response;

        if (req.getServletPath().equals(new String("/"))) {
            rep.sendRedirect("/index.html");
        }

        // If override set, allow anything
        if (SecurityManager.MASTER_SECURITY_OVERRIDE) {
            chain.doFilter(request, response);
            return;
        }

//        System.out.println(req.getServletPath());
        if(!req.getServletPath().startsWith("/login") && !req.getServletPath().startsWith("/oauth2")
                && !req.getServletPath().startsWith("/logout") && !req.getServletPath().startsWith("/index")
                && !req.getServletPath().startsWith("/res/logo") && !req.getServletPath().startsWith("/favicon.")
                && !SecurityManager.isAuthorized(req)) {
            req.getSession().setAttribute("requestedAddress", req.getServletPath());
            rep.setStatus(HttpServletResponse.SC_SEE_OTHER);
            rep.sendRedirect("/index.html");
            return;
        } else {
            if (req.getServletPath().startsWith("/index") && SecurityManager.isAuthorized(req)) {
                rep.setStatus(HttpServletResponse.SC_SEE_OTHER);
                rep.sendRedirect("/courses");
                return;
            }
            chain.doFilter(request, response);
            return;
        }

		/*
		String path = req.getRequestURI().substring(req.getContextPath().length());
		if (path.startsWith("/static") || path.contains("login") || path.contains("oauth") || path.contains("homepage")) {
			chain.doFilter(request, response);
		} else {
			request.getRequestDispatcher("/controller" + path).forward(request, response);
		}*/
    }

    @Override
    public void destroy() {

    }
}
