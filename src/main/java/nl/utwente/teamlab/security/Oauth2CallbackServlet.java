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
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;

import nl.utwente.teamlab.DAO.DB;
import nl.utwente.teamlab.logic.Service;
import nl.utwente.teamlab.model.Teacher;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet(name = "oauth2callback", value = "/oauth2callback")
@SuppressWarnings("serial")
public class Oauth2CallbackServlet extends HttpServlet {
    DB db = DB.getInstance();

	private static final Collection<String> SCOPES = Arrays.asList("email", "profile");
	private static final String USERINFO_ENDPOINT = "https://www.googleapis.com/plus/v1/people/me/openIdConnect";
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	static final String REDIRECT_URI = "http://localhost:8080/oauth2callback";
	private static final String LOGIN_ERROR_PAGE = "loginError";
	private static final String LOGIN_ERROR_MESSAGE = "Generic security error.";
	private static final String UNAUTHORIZED_ACCOUNT_DOMAIN_MESSAGE = "Please login with a "
			+ SecurityManager.AUTHORIZED_EMAIL_DOMAIN + " account.";

	private GoogleAuthorizationCodeFlow flow;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

		// Ensure that this is no request forgery going on, and that the user
		// sending us this connect request is the user that was supposed to.
		if (req.getSession().getAttribute("state") == null
				|| !req.getParameter("state").equals((String) req.getSession().getAttribute("state"))) {
			req.setAttribute("failiureCause", LOGIN_ERROR_MESSAGE);
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			req.getRequestDispatcher(LOGIN_ERROR_PAGE).forward(req, resp);
			//req.getRequestDispatcher("security").forward(req, resp); // less hassle, but may be inconvenient
			return;
		}

		req.getSession().removeAttribute("state"); // Remove one-time use state.

		flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, LoginServlet.CLIENT_ID,
				LoginServlet.CLIENT_SECRET, SCOPES).build();

		final TokenResponse tokenResponse = flow.newTokenRequest(req.getParameter("code")).setRedirectUri(REDIRECT_URI)
				.execute();

		req.getSession().setAttribute("token", tokenResponse.toString()); // Keep
																			// track
																			// of
																			// the
																			// token.
		final Credential credential = flow.createAndStoreCredential(tokenResponse, null);
		final HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(credential);

		final GenericUrl url = new GenericUrl(USERINFO_ENDPOINT); // Make an
																	// authenticated
																	// request.
		final HttpRequest request = requestFactory.buildGetRequest(url);
		request.getHeaders().setContentType("application/json");

		final String jsonIdentity = request.execute().parseAsString();
		@SuppressWarnings("unchecked")
		HashMap<String, String> userIdResult = new ObjectMapper().readValue(jsonIdentity, HashMap.class);

		// Only let in users with google accounts on the authorized domain
		if (!userIdResult.get("email").endsWith(SecurityManager.AUTHORIZED_EMAIL_DOMAIN)) {
			req.setAttribute("failiureCause", UNAUTHORIZED_ACCOUNT_DOMAIN_MESSAGE);
			resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
			req.getRequestDispatcher(LOGIN_ERROR_PAGE).forward(req, resp);
			return;
		}

		// From this map, extract the relevant profile info and store it in the session.
		String userEmail = userIdResult.get("email");
		String userFullName = userIdResult.get("name");
		Teacher user = null;  // creates a new Teacher if this is their first security
		try {	// TODO idk how this will be handled
			user = db.getTeacher(userEmail, userFullName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		req.getSession().setAttribute("teacher", user);
		req.getSession().setAttribute("userEmail", userEmail);
		req.getSession().setAttribute("userFullName", userFullName);
		String redirectLocation = (String) req.getSession().getAttribute("requestedAddress");
		req.getSession().removeAttribute("requestedAddress");
		if (redirectLocation == null) redirectLocation = SecurityManager.HOMEPAGE;
		resp.sendRedirect(redirectLocation);
	}

	private void addUserIfNotExists(String email, String name) {

	}
}