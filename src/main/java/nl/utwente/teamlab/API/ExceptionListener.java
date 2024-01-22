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

package nl.utwente.teamlab.API;

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

import java.util.Arrays;
import java.util.logging.Logger;

public class ExceptionListener implements ApplicationEventListener {

    @Override
    public void onEvent(ApplicationEvent event) {

    }

    @Override
    public RequestEventListener onRequest(RequestEvent requestEvent) {
        return new ExceptionRequestEventListener();
    }

    public static class ExceptionRequestEventListener implements RequestEventListener{
        private final Logger logger;

        public ExceptionRequestEventListener(){
            logger = Logger.getLogger(getClass().getName());
        }

        @Override
        public void onEvent(RequestEvent event) {
            switch (event.getType()){
                case ON_EXCEPTION:
                    Throwable t = event.getException();
                    logger.severe("Found exception for requestType: " +
                            event.getType() + " -- " + (
                                (t != null && t.getCause() != null) ?
                                        (t.getCause().getMessage() + t.toString()) :
                                    t
                            )   +
                            "\n\nStacktrace:\n" +
                            Arrays.toString(t.getStackTrace()));
            }
        }
    }
}