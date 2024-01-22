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
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

@WebServlet(name = "security", value = "/security")
@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {

  private static final Collection<String> SCOPES = Arrays.asList("email", "profile");
  private static final JsonFactory JSON_FACTORY = new JacksonFactory();
  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
  static final String CLIENT_ID = "394769163074-eq4q9cet1mvtugpgcfg4s5pj77g9rkju.apps.googleusercontent.com";
  static final String CLIENT_SECRET = "lqVWn1j9DntzHU5HiAid92Hi";  // TODO: Is having this here safe?!

  private GoogleAuthorizationCodeFlow flow;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException {

    String state = new BigInteger(130, new SecureRandom()).toString(32);  // prevent request forgery
    req.getSession().setAttribute("state", state);

    if (req.getAttribute("loginDestination") != null) {
      req
          .getSession()
          .setAttribute("loginDestination", (String) req.getAttribute("loginDestination"));
    } else {
    	req.getSession().setAttribute("loginDestination", "homepage");
    }

    flow = new GoogleAuthorizationCodeFlow.Builder(
        HTTP_TRANSPORT,
        JSON_FACTORY,
        CLIENT_ID,
        CLIENT_SECRET,
        SCOPES)
        .build();

    // Callback url should be the one registered in Google Developers Console
    String url =
        flow.newAuthorizationUrl()
            .setRedirectUri(Oauth2CallbackServlet.REDIRECT_URI)
            .setState(state)            // Prevent request forgery
            .build();
    resp.sendRedirect(url);
  }
}