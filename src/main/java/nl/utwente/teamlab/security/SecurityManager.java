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
import nl.utwente.teamlab.model.Teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@SuppressWarnings("unused")
public class SecurityManager {

    public static final String HOMEPAGE = "/courses";
    static String fakeName = "fakePerson", fakeMail = "test@mail";
	public static final boolean MASTER_SECURITY_OVERRIDE = false;
	public static final String AUTHORIZED_EMAIL_DOMAIN = "utwente.nl";

	//what is the purpose of this method?
	public static boolean isAuthorized(HttpServletRequest req) {  // FIXME: Find a better name for this method.
		if (MASTER_SECURITY_OVERRIDE) {
			return true;
		}
		HttpSession userSession = req.getSession(false);
		if (userSession != null && userSession.getAttribute("userEmail") != null) {
			return ((String)(userSession.getAttribute("userEmail"))).endsWith(AUTHORIZED_EMAIL_DOMAIN);
		} else {
			return false;
		}
	}

	public static Teacher getUser (HttpServletRequest req) {
		if (MASTER_SECURITY_OVERRIDE) {
			return new Teacher(fakeMail, fakeName);
		}
		HttpSession userSession = req.getSession(false);
		if (userSession == null) {
			if (MASTER_SECURITY_OVERRIDE) {
				return new Teacher(fakeMail, fakeName);
			} else {
				throw new IllegalStateException("Can't get email because request doesn't belong to any session.");
			}
		} else {
			if (userSession.getAttribute("userEmail") == null) {
				throw new IllegalStateException("Can't get user because they are not logged in.");
			} else {
				return (Teacher)(userSession.getAttribute("teacher"));
			}
		}
	}
	
	public static String getUserEmail(HttpServletRequest req) {
		if (MASTER_SECURITY_OVERRIDE) {
			return fakeMail;
		}
		HttpSession userSession = req.getSession(false);
		if (userSession == null) {
			if (MASTER_SECURITY_OVERRIDE) {
				return fakeMail;
			} else { 
				throw new IllegalStateException("Can't get email because request doesn't belong to any session.");
			}
		} else {
			if (userSession.getAttribute("userEmail") == null) {
				throw new IllegalStateException("Can't get email because user isn't logged in.");
			} else {
				return (String)(userSession.getAttribute("userEmail"));
			}
		}
	}
	
	public static String getUserName(HttpServletRequest req) {
		HttpSession userSession = req.getSession(false);
		if (userSession == null) {
			if (MASTER_SECURITY_OVERRIDE) {
				return fakeName;
			}
			throw new IllegalStateException("Can't get name because request doesn't belong to any session.");
		} else {
			if (userSession.getAttribute("userFullName") == null) {
				if (MASTER_SECURITY_OVERRIDE) {
					return fakeName;
				} else {
					throw new IllegalStateException("Can't get name because user isn't logged in.");
				}
			} else {
				return (String)(userSession.getAttribute("userFullName"));
			}
		}
	}
	
}
