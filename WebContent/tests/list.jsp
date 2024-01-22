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

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <link href="https://fonts.googleapis.com/css?family=Roboto:100,300,400,500,700,900|Material+Icons" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/vuetify/dist/vuetify.min.css" rel="stylesheet">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui">
  <title>Tests</title>
</head>
<body>
<v-app id="app"><template>

  <sidebar
    username="${sessionScope.teacher.name}"
    email="${sessionScope.teacher.mail}"
  ></sidebar>

  <v-content>
    <titlebar
            <%--username="${sessionScope.teacher.name}"
            email="${sessionScope.teacher.mail}"--%>
    >
      Tests
    </titlebar>

    <v-layout>
      <v-flex> <%--element cu marime variabila--%>
        <v-card flat> <%--div--%>
          <v-layout>
            <v-flex  px-4>
          <v-list two-line>
            <v-list-tile
                    v-if="loadingFailed"
                    avatar
            >
              <v-list-tile-avatar>
                <v-icon medium>error</v-icon>
              </v-list-tile-avatar>
              <v-list-tile-content>
                <v-list-tile-title>Could not load tests, sorry.</v-list-tile-title>
              </v-list-tile-content>
            </v-list-tile>

            <%--add--%>
            <div v-else>
              <v-list-tile
                      avatar
                      v-on:click="gotoNew('<%=request.getContextPath()%>/tests/new')"
              >
                <v-list-tile-avatar>
                  <v-icon medium>add</v-icon>
                </v-list-tile-avatar>
                <v-list-tile-content>
                  <v-list-tile-title>Create new test</v-list-tile-title>
                </v-list-tile-content>
              </v-list-tile>
              <v-divider></v-divider>
            </div>

            <v-list-tile
              v-for="test in tests" <%--foreach--%>
              :key="test.id"
              avatar
              v-on:click="gotoId('<%=request.getContextPath()%>/tests/test.jsp', test.id)"<%-- how does the redirecting work--%>
              >
                <v-list-tile-avatar>
                  <v-icon medium>ballot</v-icon>
                </v-list-tile-avatar>
                <v-list-tile-content>
                  <v-list-tile-title>{{ test.name }}</v-list-tile-title>
                </v-list-tile-content>
            </v-list-tile>
          </v-list>
            </v-flex>
          </v-layout>
        </v-card>
      </v-flex>
    </v-layout>
  </v-content>
</template>
</v-app>

<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
<script src = "https://cdn.rawgit.com/abdmob/x2js/master/xml2json.js"></script>
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<script src="https://cdn.jsdelivr.net/npm/vuetify/dist/vuetify.js"></script>
<script>apiUrl = "<%=request.getContextPath()%>"</script>
<script src="<%=request.getContextPath()%>/vue_components/sidebar.js"></script>
<script src="<%=request.getContextPath()%>/vue_components/titlebar.js"></script>
<script src="<%=request.getContextPath()%>/tests/list.js"></script>
<script src="<%=request.getContextPath()%>/functions.js"></script>
</body>
</html>