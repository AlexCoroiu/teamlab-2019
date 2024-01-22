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

Vue.component ('sidebar', {
  props: ['username', 'email'],

  data: function () {
    return {
      menu: false,

      sidebar_items: [
        {title: 'Courses', icon: 'class', link: apiUrl + '/courses'},
        {title: 'Tests', icon: 'ballot', link: apiUrl + '/tests'}
      ],

      manual: [
        {title: 'Manual', icon: 'help', link: apiUrl +'/manual'}
      ]
    }
  },

  methods: {  //TODO separate file
    gotoPage (url) {
      if (url === window.location.href) return;
      else window.location.href = url
    }
  },

  template: `
    <v-navigation-drawer clipped enable-resize-watcher app>
      <v-toolbar flat class="white">
      <v-container fluid mx-2 px-2>
        <v-layout column wrap >
          <v-flex xs2 mb-3>
            <v-card
              v-if="username" 
              hover v-on:click="menu = !menu" style="border-radius: 3pt">
              <v-layout>
                <v-flex xs12 pa-2>
              <!--<v-container fluid ma0 pa0> -->
                  <v-layout row wrap align-center justify-center fill-height >
                    <v-flex xs4>
                      <v-menu
                        v-model="menu"
                        :close-on-content-click="false"
                        offset-y
                      >
                        <template v-slot:activator="{}">
                          <v-avatar color="teal" size="34pt" v-on="">
                            <span class="white--text headline">{{ username.charAt(0) }}</span>
                          </v-avatar>
                        </template>
                  
                        <v-card>
                          <v-card-title>
                            <span>Logged in as: {{ username }}<br/>{{ email }}</span>
                            <v-btn flat onclick="window.location.href = apiUrl + '/logout'">Logout</v-btn>
                          </v-card-title>
                        </v-card>
                      </v-menu>
                    </v-flex>
                    <v-flex xs8>
                      <!--<v-container fluid>-->
                        <v-layout column wrap>
                          <v-flex xs5>
                            <span class="body-2">{{ username }}</span>
                          </v-flex>
                          <v-flex xs1 my-1 pr-4>
                            <v-divider></v-divider>
                          </v-flex>
                          <v-flex xs5>
                            <span>University of Twente</span>
                          </v-flex>
                        </v-layout>
                      <!--</v-container>-->
                    </v-flex>
                  </v-layout>
              <!--</v-container>-->
                </v-flex>
              </v-layout>
            </v-card>
          </v-flex>
          <v-flex xs2>
            <!--<v-container fluid>-->
              <v-layout column wrap>
                <v-flex xs12>
                  <v-spacer></v-spacer>
                  <v-list>
                    <v-list-tile
                        v-for="item in sidebar_items"
                        :key="item.title"
                        @click="gotoPage(item.link)"
                    >
                      <v-list-tile-action>
                        <v-icon large>{{ item.icon }}</v-icon>
                      </v-list-tile-action>
              
                      <v-list-tile-content>
                        <v-list-tile-title>{{ item.title }}</v-list-tile-title>
                      </v-list-tile-content>
                    </v-list-tile>
                  </v-list>
                </v-flex>
              </v-layout>
            <!--</v-container>-->
          </v-flex>
          <v-flex xs6 ma-1 mt-4 pa-1>
            <v-divider v-if="!!$slots.default"></v-divider>
            <v-layout><v-flex xs12 mt-3><slot></slot></v-flex></v-layout>
          </v-flex>
          <v-spacer></v-spacer>
          <v-flex xs2>
          <v-divider></v-divider>
          <v-spacer></v-spacer>
            <v-layout column wrap> 
                <v-flex grow> 
                    <p><i>For more information check out the manual</i></p>
                    <v-list>
                    <v-list-tile
                        v-for="item in manual"
                        :key="item.title"
                        @click="gotoPage(item.link)"
                    >
                      <v-list-tile-action>
                        <v-icon large>{{ item.icon }}</v-icon>
                      </v-list-tile-action>
              
                      <v-list-tile-content>
                        <v-list-tile-title>{{ item.title }}</v-list-tile-title>
                      </v-list-tile-content>
                    </v-list-tile>
                  </v-list>
                </v-flex>
            </v-layout>
          </v-flex>
        </v-layout>
      </v-container>
      
        
        <!--<v-list>
          <v-list-tile>
            <v-list-tile-title class="title">
              &lt;!&ndash;Application Name and Logo&ndash;&gt;
            </v-list-tile-title>
          </v-list-tile>
        </v-list>-->
      </v-toolbar>

      <!--<v-divider></v-divider>-->

      
    </v-navigation-drawer>
  `,
});