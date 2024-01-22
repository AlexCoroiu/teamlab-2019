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
  <title>Courses</title>
</head>
<body>
<v-app id="app"><template>
  <sidebar
    username="${sessionScope.teacher.name}"
    email="${sessionScope.teacher.mail}"
  ></sidebar>

  <v-content>
    <titlebar>
      Courses
    </titlebar>

    <v-layout>
      <v-flex>
        <v-card>
          <v-list two-line>
            <v-list-tile
                    v-if="loadingFailed"
                    avatar
            >
              <v-list-tile-avatar>
                <v-icon medium>error</v-icon>
              </v-list-tile-avatar>
              <v-list-tile-content>
                <v-list-tile-title>Could not load courses, sorry.</v-list-tile-title>
              </v-list-tile-content>
            </v-list-tile>

            <div v-else>
              <v-list-tile
                      avatar
                      @click="gotoNew('<%=request.getContextPath()%>/courses/new')"
              >
                <v-list-tile-avatar>
                  <v-icon medium>add</v-icon>
                </v-list-tile-avatar>
                <v-list-tile-content>
                  <v-list-tile-title>Create new course</v-list-tile-title>

                </v-list-tile-content>
              </v-list-tile>
              <v-divider></v-divider>
            </div>

            <v-list-tile
              v-for="course in courses"
              :key="course.id"
              avatar
              @click="gotoId('<%=request.getContextPath()%>/courses/id', course.id)"
              >
                <v-list-tile-avatar>
                  <v-icon medium>class</v-icon>
                </v-list-tile-avatar>
                <v-list-tile-content>
                  <v-list-tile-title>{{ course.name }}<%-- (id: {{ course.id }})--%></v-list-tile-title>
                  <%--<v-list-tile-sub-title><v-icon small>person</v-icon> {{ course.participants }} students</v-list-tile-sub-title> &lt;%&ndash; whats this?&ndash;%&gt;--%>
                </v-list-tile-content>
            </v-list-tile>
          </v-list>
        </v-card>
      </v-flex>
    </v-layout>
  </v-content>
</template>
</v-app>
<%--TODO maybe make this a separate thing since we need this in everything--%>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
<script src = "https://cdn.rawgit.com/abdmob/x2js/master/xml2json.js"></script>
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<script src="https://cdn.jsdelivr.net/npm/vuetify/dist/vuetify.js"></script>
<script>apiUrl = "<%=request.getContextPath()%>"</script>
<script src="<%=request.getContextPath()%>/vue_components/sidebar.js"></script>
<script src="<%=request.getContextPath()%>/vue_components/titlebar.js"></script>
<script src="<%=request.getContextPath()%>/courses/list.js"></script>
<script src="<%=request.getContextPath()%>/functions.js"></script>
</body>
</html>