<%@ page contentType="text/html;charset=UTF-8" language="java" session="true"%>
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

<html>
<head>
    <meta charset="UTF-8">
    <link href="https://fonts.googleapis.com/css?family=Roboto:100,300,400,500,700,900|Material+Icons" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/vuetify/dist/vuetify.min.css" rel="stylesheet">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui">
    <title>View course</title>
</head>
<body>
<v-app id="app"><template>
    <sidebar
        username="${sessionScope.teacher.name}"
        email="${sessionScope.teacher.mail}"
    >
        <%--<p class="body-2">Tips</p>--%>

        <p class="body-1" style="white-space: pre-wrap;"
        >{{ assistant.courseText }}</p>

        <p class="body-1" style="white-space: pre-wrap;"
           v-if="editMode"
        >{{ assistant.courseTextFields }}</p>

        <p class="body-1" style="white-space: pre-wrap;"
           v-if="!anyStudents"
        >{{ assistant.courseTextNoStudents }}</p>

        <p class="body-1" style="white-space: pre-wrap;"
           v-if="anyStudents && selectedTab === 0"
        >{{ assistant.overviewText }}</p>

        <p class="body-1" style="white-space: pre-wrap;"
           v-if="anyStudents && selectedTab === 1 "
        >{{ assistant.studentsText }}</p>

        <p class="body-1" style="white-space: pre-wrap;"
           v-if="anyStudents && (selectedTab === 2 && anyGroups)"
        >{{ assistant.groupsText }}</p>

        <p class="body-1" style="white-space: pre-wrap;"
           v-if="anyStudents && (selectedTab === 2 && (!anyGroups || expandRules))"
        >{{ assistant.groupsTextNoGrouping }}</p>
    </sidebar>

    <v-content>
        <titlebar
        >
            <%-- Can't currently change the name on the backend, so only enabled for adding --%>
            <span v-if="!editMode" v-on:click="editMode = true">{{ courseName }}</span>
            <%--<v-form--%>
                    <%--v-else--%>
                    <%--ref="form"--%>
                    <%--v-model="validation.valid"--%>
                    <%--pa-0--%>
                    <%--ma-0--%>
            <%-->--%>
                <v-text-field
                    v-else
                    v-model="courseName"
                    :rules="validation.name"
                    v-on:blur="setName()"
                    solo
                    ></v-text-field>
            <%--</v-form>--%>
        </titlebar>

        <v-container fluid grid-list-lg white>
            <v-alert
                    :value="loadingFailed"
                    type="error"
            >
                Could not load course.
            </v-alert>
            <v-layout
                wrap
                row
                justify-start
                v-show="!loadingFailed"
                >
                <v-flex xs6>
                    <v-card flat>
                        <v-autocomplete
                            v-model="selectedTest"
                            :items="tests"
                            item-text="name"
                            item-value="id"
                            <%--:readonly="!editMode"--%>
                            label="Test"
                            >
                            <template v-slot:prepend-item>
                                <v-list-tile v-on:click="goCreateNewTest()" ripple
                                >
                                    <v-list-tile-action>
                                        <v-icon :color="'indigo darken-4'">add</v-icon>
                                    </v-list-tile-action>
                                    <v-list-tile-content>
                                        <v-list-tile-title>Create new test</v-list-tile-title>
                                    </v-list-tile-content>
                                </v-list-tile>
                                <v-divider class="mt-1"></v-divider>
                            </template>
                        </v-autocomplete>
                    </v-card>
                </v-flex>
                <v-flex xs6>
                    <v-card flat>
                        <v-combobox
                            v-model="selectedStudies"
                            <%--:items="studies"--%>
                            :search-input.sync="searchStudy"
                            hide-selected
                            <%--hint="Maximum of 5 tags"--%>
                            label="Participating studies"
                            multiple
                            <%--:readonly="!editMode"--%>
                            v-on:blur="setStudies()"
                            <%--persistent-hint--%>
                            small-chips
                            >
                            <template v-slot:no-data>
                                <v-list-tile>
                                    <v-list-tile-content>
                                        <v-list-tile-title>
                                            Press <kbd>enter</kbd> to create "<strong>{{ searchStudy }}</strong>"
                                        </v-list-tile-title>
                                    </v-list-tile-content>
                                </v-list-tile>
                            </template>
                        </v-combobox>
                    </v-card>
                </v-flex>
                <v-flex xs12>
                    <v-switch v-model="status"
                              label="Collect progress from students instead of prior knowledge"
                    ></v-switch>
                </v-flex>
                <v-flex xs12 <%--v-if="!editMode"--%>>
                    <v-tabs
                            v-model="selectedTab"
                            centered
                            icons-and-text
                    >
                        <v-tab v-if="anyStudents">
                            Overview
                            <v-icon>public</v-icon>
                        </v-tab>
                        <v-tab>
                            Students
                            <v-icon>face</v-icon>
                        </v-tab>
                        <v-tab v-if="anyStudents">
                            Groups
                            <v-icon>group</v-icon>
                        </v-tab>
                        <v-tab-item v-if="anyStudents">
                            <v-card flat>
                                <v-container
                                        fluid
                                        grid-list-lg
                                >
                                    <v-layout row wrap justify-start>
                                        <v-flex xs12 sm12 md6 lg6 xl4
                                                v-for="(chart, index) in charts" <%--add chart.name--%>
                                                :key="index">
                                            <v-layout column wrap>
                                                <v-flex xs11>
                                                    <canvas :id="'chart-' + index"
                                                            width="90%"
                                                            height="90%"
                                                    ></canvas>
                                                </v-flex>
                                                <v-flex xs1><p class="body-2 text-xs-center">{{ chart.name }}</p></v-flex>
                                            </v-layout>
                                        </v-flex>
                                    </v-layout>
                                </v-container>
                            </v-card>

                            <%--<canvas--%>
                                    <%--v-for="(chart, index) in charts"--%>
                                    <%--:key="index"--%>
                                    <%--:id="'chart-' + index"--%>
                                    <%--width="100"--%>
                                    <%--height="50"--%>
                            <%--></canvas>--%>
                        </v-tab-item>
                        <v-tab-item
                        >
                            <v-card flat>
                                <v-container
                                    fluid
                                    grid-list-lg
                                    >
                                    <v-layout row wrap justify-start>
                                        <!-- Message if no students -->
                                        <v-flex xs12 v-if="!anyStudents">
                                            <span class="h4">No students for now.</span>
                                        </v-flex>

                                        <!-- Actions -->
                                        <v-flex shrink xs12>
                                            <v-layout justify-start row wrap>
                                                <v-flex xs12 sm6 md6 shrink>
                                                    <v-menu
                                                            v-model="shareLinkBox"
                                                            :close-on-content-click="false"
                                                            offset-y
                                                    >
                                                        <template v-slot:activator="{ on }">
                                                            <span>Share with students: </span>
                                                            <v-btn outline style="border-radius: 3pt"
                                                                   v-on="on"
                                                                   :disabled="!selectedTest"
                                                            >
                                                                <span v-if="selectedTest">
                                                                    Get link
                                                                </span>
                                                                <span v-if="!selectedTest">
                                                                    Select a test first
                                                                </span>
                                                            </v-btn>
                                                        </template>

                                                        <v-card flat class="ma-2 py-2 px-3">
                                                            <span>Students can take the test at this link:</span><br/>
                                                            <v-text-field
                                                                    id="shareLinkField"
                                                                    v-model="shareLink"
                                                                    :append-icon="'file_copy'"
                                                                    @click:append="copyLinkToClipboard()"
                                                                    readonly
                                                            ></v-text-field>
                                                            <v-alert
                                                                    v-model="shareLinkAlert"
                                                                    color="info"
                                                                    icon="info"
                                                                    outline
                                                            >
                                                                Copied link to clipboard.
                                                            </v-alert>
                                                        </v-card>
                                                    </v-menu>
                                                </v-flex>
                                                <%--<v-spacer></v-spacer>--%>
                                                <%--<v-flex grow></v-flex>--%>
                                            </v-layout>
                                            <v-flex grow><v-spacer></v-spacer></v-flex>
                                        </v-flex>

                                        <!-- Filter bar -->
                                        <v-flex xs12 sm12 v-if="anyStudents">
                                            <v-layout row wrap>
                                                <v-flex xs12 sm3>
                                                    <v-text-field label="Filter" clearable
                                                                  v-model="studentsFilter"
                                                    ></v-text-field>
                                                </v-flex>
                                            </v-layout>
                                        </v-flex>

                                        <!-- Students list -->
                                        <v-flex xs12 sm6 md6 lg4 xl4
                                                v-for="student in filteredStudents"
                                                :key="student.id">
                                            <%--<v-card color="cyan darken-2" class="white--text">
                                                <v-layout>
                                                    <v-flex xs5>
                                                        <v-img
                                                                src="https://cdn.vuetifyjs.com/images/cards/foster.jpg"
                                                                height="125px"
                                                                contain
                                                        ></v-img>
                                                    </v-flex>
                                                    <v-flex xs7>
                                                        <v-card-title primary-title>
                                                            <div>
                                                                <div class="headline">Supermodel</div>
                                                                <div>Foster the People</div>
                                                                <div>(2014)</div>
                                                            </div>
                                                        </v-card-title>
                                                    </v-flex>
                                                </v-layout>
                                                <v-divider light></v-divider>
                                                <v-card-actions class="pa-3">
                                                    Rate this album
                                                    <v-spacer></v-spacer>
                                                    <v-icon>star_border</v-icon>
                                                    <v-icon>star_border</v-icon>
                                                    <v-icon>star_border</v-icon>
                                                    <v-icon>star_border</v-icon>
                                                    <v-icon>star_border</v-icon>
                                                </v-card-actions>
                                            </v-card>--%>
                                            <v-card>
                                                <v-card-title primary-title>
                                                    <div>
                                                        <span class="title">{{ student.name }}</span><br/>
                                                        <span class="grey--text">
                                                            {{ student.id }}
                                                        </span><br/>
                                                                        <span>
                                                            Study: {{ student.study }}
                                                        </span><br/>
                                                                        <span>
                                                            Knowledge: {{ student.knowledge }}
                                                        </span><br/>
                                                                        <span>
                                                            Motivation: {{ student.motivation }}
                                                        </span><br/>
                                                                        <span>
                                                            Belbin role: {{ student.belbin }}
                                                        </span><br/>
                                                                        <span>
                                                            Nationality: {{ student.nationality }}
                                                        </span><br/>
                                                                        <span>
                                                            Gender: {{ student.gender }}
                                                        </span>
                                                    </div>
                                                </v-card-title>
                                            </v-card>
                                        </v-flex>
                                    </v-layout>
                                </v-container>
                            <%--<v-list two-line>
                                <v-list-tile
                                    v-for="student in students"
                                    :key="student.id"
                                    avatar
                                >
                                    <v-list-tile-avatar><v-icon large>person</v-icon></v-list-tile-avatar>
                                    <v-list-tile-content>
                                        <v-list-tile-title>{{ student.name }}</v-list-tile-title>
                                        <v-list-tile-sub-title>
                                            <v-icon>school</v-icon>&nbsp;{{ student.study }}
                                            &nbsp;&nbsp;
                                            <v-icon>poll</v-icon>&nbsp;{{ student.knowledge }}
                                            &nbsp;&nbsp;
                                            <v-icon>sentiment_dissatisfied</v-icon>&nbsp;{{ student.motivation }}
                                            &nbsp;&nbsp;
                                            <v-icon>group_work</v-icon>&nbsp;{{ student.belbin }}
                                            &nbsp;&nbsp;
                                            <v-icon>public</v-icon>&nbsp;{{ student.nationality }}
                                            &nbsp;&nbsp;
                                            <v-icon>star_half</v-icon>&nbsp;{{ student.gender }}
                                        </v-list-tile-sub-title>
                                    </v-list-tile-content>
                                </v-list-tile>
                            </v-list>--%>
                            </v-card>
                        </v-tab-item>
                        <v-tab-item v-if="anyStudents"
                        >
                            <groupmaking :course-id="courseId" :expanded="!anyGroups || expandRules" v-bind:choices="groupingValues"></groupmaking> <!-- FIXME this somehow breaks this objects?? -->
                            <br/>
                            <v-subheader>
                                Groups
                                <v-flex xs12 sm6 md6 shrink>
                                    <v-btn outline color="primary" style="border-radius: 3pt"
                                           :disabled="!anyGroups"
                                           v-on:click="downloadGroupsCsv()"
                                    >
                                        Export
                                    </v-btn>
                                    <v-btn v-if="anyGroups"
                                           outline color="secondary" style="border-radius: 3pt"
                                           v-on:click="expandRules = !expandRules">
                                        <span v-if="!expandRules">Redo grouping</span>
                                        <span v-else>Cancel regrouping</span>
                                    </v-btn>
                                </v-flex>
                            </v-subheader>
                            <v-container
                                    fluid
                                    grid-list-lg
                            >
                                <v-layout row wrap justify-start fill-height align-start>
                                    <v-flex xs12 sm12 md12
                                            v-if="!anyGroups">
                                        No groups yet.
                                    </v-flex>
                                    <v-flex xs12 sm12 md6 lg6 xl4
                                            v-for="group in groups"
                                            :key="group.id">
                                        <v-card height="100%">
                                            <v-card-title primary-title class="ml-2">
                                                    <v-layout row wrap fill-height align-center>
                                                        <v-flex grow><span class="title">Group {{ group.name }}</span></v-flex>
                                                        <v-flex shrink>
                                                            <span class="subheading" style="color: grey" <%--v-show="!group.editGrade"--%>>
                                                                grade: {{ group.grade }}
                                                            </span>
                                                            <%--<v-text-field v-model="group.grade" v-show="group.editGrade" solo height="10pt">
                                                            </v-text-field>--%>
                                                            <v-btn icon flat v-on:click="editGrade(group)">
                                                                <v-icon>edit</v-icon>
                                                            </v-btn>
                                                        </v-flex>
                                                        <v-flex shrink>
                                                            <span class="subheading" style="color: grey">
                                                                performance: {{ group.performance }}
                                                            </span>
                                                            <v-btn icon flat v-on:click="editPerformance(group)">
                                                                <v-icon>edit</v-icon>
                                                            </v-btn>
                                                        </v-flex>
                                                        <v-flex xs12>
                                                            <span class="subtitle grey--text">{{ group.students.length }} people</span><br/>
                                                            <div v-for="student in group.students"
                                                                 :key="student.id"
                                                                 class="body-2"
                                                            >
                                                                <v-btn flat icon v-on:click="removeFromGroup(group, student)">
                                                                    <v-icon small color="pink darken-2">remove</v-icon>
                                                                </v-btn>
                                                                <span>{{ student.name }} ({{ student.id }})</span>
                                                                <br/>
                                                            </div>
                                                        </v-flex>
                                                    </v-layout>
                                                    <br/>
                                            </v-card-title>
                                            <v-card-actions>
                                                <div>
                                                    <v-menu
                                                            v-model="group.addMenu"
                                                            :close-on-content-click="true"
                                                            offset-y
                                                    >
                                                        <template v-slot:activator="{ on }">
                                                            <v-avatar v-on="on">
                                                                <v-btn flat icon color="teal"><v-icon>add</v-icon></v-btn>
                                                            </v-avatar>
                                                        </template>

                                                        <v-card>
                                                            <v-card-title>
                                                                <v-list>
                                                                    <v-list-tile v-for="(student, i) in ungroupedStudents" :key="i">
                                                                        <v-list-tile-content>
                                                                            <v-list-tile-title>
                                                                                <span>{{ student.name }} ({{ student.id }})</span>
                                                                            </v-list-tile-title>
                                                                        </v-list-tile-content>
                                                                        <v-list-tile-action>
                                                                            <v-btn flat icon color="teal" v-on:click="addToGroup(student, group.id)">
                                                                                <v-icon left>add</v-icon>
                                                                            </v-btn>
                                                                        </v-list-tile-action>
                                                                    </v-list-tile>
                                                                    <v-list-tile v-if="ungroupedStudents.length === 0">
                                                                        <span class="body-2">No ungrouped students.</span>
                                                                    </v-list-tile>
                                                                </v-list>
                                                            </v-card-title>
                                                        </v-card>
                                                    </v-menu>
                                                    <%--<v-spacer/>
                                                    <v-dialog v-model="groupPerformanceDialog" max-width="320">
                                                        <template v-slot:activator="{}">
                                                            <v-btn flat icon v-on:click="showGroupPerformanceDialog()" style="border-radius: 3pt">
                                                                <v-icon>comment</v-icon>
                                                            </v-btn>
                                                        </template>
                                                        <v-layout>
                                                            <v-flex class="white" py-1 px-2>
                                                                <v-card flat>
                                                                    <v-textarea
                                                                            label="Details about group's performance"
                                                                            ref="groupPerformanceTextarea"
                                                                            v-model="group.performance"
                                                                    >
                                                                    </v-textarea>
                                                                    <v-card-actions>
                                                                        <v-spacer></v-spacer>
                                                                        <v-btn color="secondary" flat v-on:click="showGroupPerformanceDialog(false)">Cancel</v-btn>
                                                                        <v-btn color="primary" v-on:click="setGroupPerformance(group)" outline>Save</v-btn>
                                                                    </v-card-actions>
                                                                </v-card>
                                                            </v-flex>
                                                        </v-layout>
                                                    </v-dialog>--%>
                                                </div>
                                            </v-card-actions>
                                        </v-card>
                                    </v-flex>
                                </v-layout>
                            </v-container>
                            <%--<v-list two-line>
                                <v-list-tile
                                    v-for="group in groups"
                                    :key="group.id"
                                    avatar
                                >
                                    <v-list-tile-avatar><v-icon large>group_work</v-icon></v-list-tile-avatar>
                                    <v-list-tile-title>
                                        Group {{ group.name }}
                                        <v-spacer></v-spacer>
                                        {{ group.students.length }}&nbsp;<v-icon>people</v-icon>
                                    </v-list-tile-title>
                                    <v-list-tile-sub-title>
                                        <span v-for="student in group.students" :key="student.id">
                                            {{ student.name }} ({{ student.id }})
                                            <v-btn v-if="editMode" flat icon color="red" v-on:click="removeFromGroup(group, student)">
                                              <v-icon>remove</v-icon>
                                            </v-btn>
                                        </span>
                                        <span>
                                            <v-menu
                                                    v-model="group.addMenu"
                                                    :close-on-content-click="false"
                                                    offset-y
                                            >
                                                <template v-slot:activator="{ on }">
                                                  <v-avatar color="teal" size="32pt" v-on="on">
                                                    <span class="white--text">+</span>
                                                  </v-avatar>
                                                </template>

                                                <v-card v-if="editMode">
                                                  <v-card-title>
                                                    <v-list>
                                                        <v-list-tile v-for="(student, i) in ungroupedStudents" :key="i">
                                                            <v-list-tile-title>
                                                                <span>{{ student.name }} ({{ student.id }})</span>
                                                                <v-btn flat color="teal" v-on:click="addToGroup(student)">
                                                                    <v-icon left>add</v-icon> Add to group
                                                                </v-btn>
                                                            </v-list-tile-title>
                                                        </v-list-tile>
                                                    </v-list>
                                                  </v-card-title>
                                                </v-card>
                                              </v-menu>
                                        </span>
                                    </v-list-tile-sub-title>
                                </v-list-tile>
                            </v-list>--%>
                        </v-tab-item>
                    </v-tabs>
                </v-flex>
                <v-flex xs12>
                    <v-card flat style="color:lightgrey !important">
                        <v-card-text style="height: 32px; position:relative">
                            <%--<v-btn
                                    v-if="!editMode"
                                    absolute
                                    dark
                                    fab
                                    bottom
                                    right
                                    color="orange"
                                    v-on:click="editMode = true"
                            >
                                <v-icon>edit</v-icon>
                            </v-btn>--%>
                            <%--<v-btn
                                    v-if="editMode"
                                    absolute
                                    dark
                                    fab
                                    bottom
                                    right
                                    color="pink"
                                    :disabled="!checkValidation()"
                                    v-on:click="sendCourse()"
                            >
                                <v-icon>save</v-icon>
                            </v-btn>--%>
                        </v-card-text>
                    </v-card>
                </v-flex>
            </v-layout>
        </v-container>
    </v-content>
</template></v-app>

<script src="https://cdn.jsdelivr.net/npm/chart.js@2.8.0/dist/Chart.js"></script>
<%--<script src="https://unpkg.com/vue-chartjs/dist/vue-chartjs.js"></script>--%>
<script src="https://unpkg.com/axios/dist/axios.js"></script>
<script src = "https://cdn.rawgit.com/abdmob/x2js/master/xml2json.js"></script>
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<script src="https://cdn.jsdelivr.net/npm/vuetify/dist/vuetify.js"></script>
<script>apiUrl = "<%=request.getContextPath()%>"</script>
<script src="<%=request.getContextPath()%>/vue_components/sidebar.js"></script>
<script src="<%=request.getContextPath()%>/functions.js"></script>
<script src="<%=request.getContextPath()%>/vue_components/titlebar.js"></script>
<script src="<%=request.getContextPath()%>/vue_components/groupmaking.js"></script>
<script src="<%=request.getContextPath()%>/courses/course.js"></script>
</body>
</html>
