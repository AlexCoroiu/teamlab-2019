<%@page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%--
  ~     TeamLab
  ~     Copyright (C) 2019  Alexandra Coroiu, Ciprian Lăzăroaia
  ~     a.coroiu@student.utwente.nl, c.lazaroaia@student.utwente.nl
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU Affero General Public License as published
  ~     by the Free Software Foundation, either version 3 of the License, or
  ~     (at your option) any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU Affero General Public License for more details.
  ~
  ~     You have received a copy of the GNU Affero General Public License
  ~     along with this program.  Alternatively, see <http://www.gnu.org/licenses/>.
  --%>

<%--<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>--%>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <link href="https://fonts.googleapis.com/css?family=Roboto:100,300,400,500,700,900|Material+Icons" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/vuetify/dist/vuetify.min.css" rel="stylesheet">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui">
  <title>Not logged in</title>
</head>
<body>
<v-app id="app">
  <template>
    <sidebar></sidebar>
    <v-content>
        <v-card
          class="white--text grey darken-3"
        >
          <v-flex pt-3 pl-3>
            You are <span v-if="!loggedIn">not</span> logged in.<br/>
            <span v-if="loggedIn">Logged in as: ${sessionScope.userEmail}</span>
          </v-flex>
        </v-card>
    </v-content>
  </template>
</v-app>
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<script src="https://cdn.jsdelivr.net/npm/vuetify/dist/vuetify.js"></script>
<script src="<%=request.getContextPath()%>/vue_components/sidebar.js"></script>
<script>
  var app = new Vue ({
    el: "#app",
    data: {
      loggedIn: ${sessionScope.userEmail != null}
    }
  })
</script>
</body>
</html>