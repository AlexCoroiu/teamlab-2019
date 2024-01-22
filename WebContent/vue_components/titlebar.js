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

Vue.component ('titlebar', {
  props: ['username', 'email'],

  data: function () {
    return {
      menu: false
    }
  },

  template: `
    <v-card>
        <v-card-title class="indigo white--text headline">
          <slot></slot>
          <v-spacer></v-spacer>
            <!--<template v-if="username">
              <v-menu
                v-model="menu"
                :close-on-content-click="false"
                offset-y
              >
                <template v-slot:activator="{ on }">
                  <v-avatar color="teal" size="32pt" v-on="on">
                    <span class="white&#45;&#45;text">{{ username.charAt(0) }}</span>
                  </v-avatar>
                </template>
          
                <v-card>
                  <v-card-title>
                    <span>Logged in as: {{ username }}<br/>{{ email }}</span>
                    <v-btn flat onclick="window.location.href = apiUrl + '/logout'">Logout</v-btn>
                  </v-card-title>
                </v-card>
              </v-menu>
            </template>-->
        </v-card-title>
    </v-card>
  `
});