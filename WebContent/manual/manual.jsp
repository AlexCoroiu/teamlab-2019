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
    ></sidebar>
    <v-content>
        <titlebar>
            Manual
        </titlebar>
        <v-container>
            <v-layout row wrap>
                <v-flex xs12>
                    <v-expansion-panel>
                        <v-expansion-panel-content>
                            <template v-slot:header>
                            <div>
                            <span class="title">Your courses and tests</span><br/><br/>
                            </div>
                            </template>
                            <v-card>
                                <v-card-text><div>
                            <span>
                                1.<b>Click on the "Courses"/"Tests" tab in the sidebar.</b><br/><br/>
                                You can see the courses/tests you have created from newest to oldest.
                            </span>
                            </div>
                                </v-card-text>
                            </v-card>
                        </v-expansion-panel-content>

                        <v-expansion-panel-content>
                            <template v-slot:header>
                                <div>
                                    <span class="title">Creating a test</span><br/><br/>
                                </div>
                            </template>
                            <v-card>
                                <v-card-text><div>
                            <span>
                                1.<b>From the list of tests, click on "Create new test".</b>
                                A new empty test with the name "Untitled test" is created.<br/>
                                2.<b>Name the test.</b><br/>
                                3.<b>Click on "NEW COMPONENT" to add components.</b>
                                You can structure your test in components however you want.
                                It is important that it includes all the topics that will be studied
                                together by the group.<br/>
                                For example, a module that contains two important threads for the group work:
                                design and programming can be translated into two components:
                                design and programming or into several components representing
                                subsections of the two threads.<br/>
                                A component is saved only if it has questions in it.
                                On the right panel you can see suggested questions.<br/>
                                4.<b>Tick and untick questions to add or remove them from a component.</b><br/><br/>
                                If there are no questions suggested for your,
                                or none of them actually fits your component, you can add a new question yourself.
                            </span>
                                </div>
                                </v-card-text>
                            </v-card>
                        </v-expansion-panel-content>

                        <v-expansion-panel-content>
                            <template v-slot:header>
                                <div>
                                    <span class="title">Creating a question</span><br/><br/>
                                </div>
                            </template>
                            <v-card>
                                <v-card-text><div>
                            <span>
                                1. <b>Click on "NEW QUESTION" during the creation of a test.</b><br/>
                                2. <b>Add the text of the question.</b><br/>
                                3. <b>Select the type of the question. </b>
                                At the moment only the "checklist" and "multiple choice" are working.<br/>
                                4. <b>Add the possible answers. </b>
                                You can add up to six for "checklist" and "multiple choice" questions <br/>
                                5. Additionally, <b>select the correct answer(s) </b> for "multiple choice" questions
                                and "checklist", respectively. <br/><br/>
                                When a question si created, it is added to the shared questions databank.
                                It can then be suggested to you or other users when creating a test.
                            </span>
                                </div>
                                </v-card-text>
                            </v-card>
                        </v-expansion-panel-content>

                        <v-expansion-panel-content>
                            <template v-slot:header>
                                <div>
                                    <span class="title">Your test</span><br/><br/>
                                </div>
                            </template>
                            <v-card>
                                <v-card-text><div>
                            <span>
                                1.<b>From the list of tests, click on one of them.</b><br/><br/>
                                On the test page you can see the test containing
                                the knowledge questions created by you.<br/>
                                Besides these questions, the test also contains the predefined motivation part:<br/>
                                <i>I am excited about the material of this module.<br/>
                                I intend to dedicate plenty of time to working for this module.<br/>
                                I am confident I will pass the module.<br/>
                                I have certain circumstances that may prevent me from properly working on the module.<br/>
                                I expect my experience with this module to be a positive one.<br/></i><br/>
                                You can edit your knowledge questions from the test here.
                                The editing has the same functionality as the creation process.
                            </span>
                                </div>
                                </v-card-text>
                            </v-card>
                        </v-expansion-panel-content>

                        <v-expansion-panel-content>
                            <template v-slot:header>
                                <div>
                                    <span class="title">Creating a course</span><br/><br/>
                                </div>
                            </template>
                            <v-card>
                                <v-card-text><div>
                            <span>
                                 1.<b>From the list of courses, click on "Create new course".</b>
                                A new empty test with the name "Untitled course" is created.<br/>
                                2.<b>Name the course.</b><br/>
                                3.<b>Add the participating studies to the course</b>
                                You can add studies as it is relevant to you.
                                You can either specify all study programmes,
                                or just generally define the background of the students as
                                "social science"/"technical science", "minor' or "retake".<br/>
                                4.<b>Check the course switch.</b>
                                It should be <i>off</i> while you want to collect data before the start of the course
                                through the assessment test. The results of the test will be used for grouping students.
                                During the test, the switch should be <i>on</i> to collect progress data.
                                There is no visualisation of the progress data at the moment,
                                the feature is nto fully implemented.<br/>
                                5.<b>Click on the test dropdown and select one of them to add one to your course.</b>
                                The tests that you can see in the dropdown are all your personal tests.<br/><br/>
                                If you do not have any tests created or none of them fit your course,
                                you can create a new test from the dropdown.
                                <b>Click on "Create new test"</b> and you will be redirected to the test creation page.
                            </span>
                                </div>
                                </v-card-text>
                            </v-card>
                        </v-expansion-panel-content>

                        <v-expansion-panel-content>
                            <template v-slot:header>
                                <div>
                                    <span class="title">Your course</span><br/><br/>
                                </div>
                            </template>
                            <v-card>
                                <v-card-text><div>
                            <span>
                                1.<b>From the list of courses, click on one of them.</b><br/>
                                2.<b>Check out the overview, students and groups in your course.</b>
                                If there are no students enrolled in the course
                                the "OVERVIEW" and "GROUPS" tabs are not available.<br/><br/>
                                You can edit your course here with the same functionality as the creation process.
                            </span>
                                </div>
                                </v-card-text>
                            </v-card>
                        </v-expansion-panel-content>

                        <v-expansion-panel-content>
                            <template v-slot:header>
                                <div>
                                    <span class="title">Students</span><br/><br/>
                                </div>
                            </template>
                            <v-card>
                                <v-card-text><div>
                            <span>
                                1.<b>From a course, click on the "STUDENTS" tab.</b><br/>
                                2.<b>Click on the "GET LINK" to get the student link for the course</b>.<br/><br/>
                                Here you can see all the students in the course with their individual assessment results.
                                Students appear here as they complete the test of the course.
                            </span>
                                </div>
                                </v-card-text>
                            </v-card>
                        </v-expansion-panel-content>

                        <v-expansion-panel-content>
                            <template v-slot:header>
                                <div>
                                    <span class="title">Overview</span><br/><br/>
                                </div>
                            </template>
                            <v-card>
                                <v-card-text><div>
                            <span>
                                1.<b>From a course, click on the "OVERVIEW" tab.</b><br/><br/>
                                Here you can see the course demographics based on the student assessment results.
                                You can see the chart representation of all the collected variables:
                                gender, nationality, Belbin role, study, motivation and knowledge levels.
                            </span>
                                </div>
                                </v-card-text>
                            </v-card>
                        </v-expansion-panel-content>

                        <v-expansion-panel-content>
                            <template v-slot:header>
                                <div>
                                    <span class="title">Groups</span><br/><br/>
                                </div>
                            </template>
                            <v-card>
                                <v-card-text><div>
                            <span>
                                1.<b>From a course, click on the "GROUPS" tab.</b><br/><br/>
                                Here you can see all the groups in the course.
                                In order for this to happen you need to firstly create groups.<br/><br/>
                                2.Optionally, you can <b>edit groups by adding and removing students.</b>
                                When adding students to a group you will see the list of students
                                that are not part of any group.<br/>
                                3.During the course, you can <b>click on the edit icon to
                                add grade and performance values to each group</b>.<br/>
                                4.<b>Click on the "EXPORT" button to save the groups in CSV format</b>.<br/><br/>

                                Groups can be completely redone at any time during the course.
                            </span>
                                </div>
                                </v-card-text>
                            </v-card>
                        </v-expansion-panel-content>

                        <v-expansion-panel-content>
                            <template v-slot:header>
                                <div>
                                    <span class="title">Creating groups</span><br/><br/>
                                </div>
                            </template>
                            <v-card>
                                <v-card-text><div>
                            <span>
                                1.<b>Go to groups page when there are no groups created
                                or click the "REDO GROUPING" button</b>.<br/>
                                2.<b>Click on "NEW RULE" to add a new grouping rule.</b><br/>
                                3.<b>Select an operation</b>.
                                The assistant on the sidebar explains the meaning of each operation.<br/>
                                4.<b>Select a variable.</b><br/>
                                5.<b>For the "cluster" and "distribute" select one or more values.</b><br/>
                                6.When you are done with adding rules, <b>click on "MAKE GROUPING"</b>.<br/><br/>
                                The automatic grouping will be made and the page will be refreshed.
                                You can go back to the groups tab to see the created groups.
                            </span>
                                </div>
                                </v-card-text>
                            </v-card>
                        </v-expansion-panel-content>
                    </v-expansion-panel>
                    You can find the repository of this project
                    containing the code, documentation, design and report at:
                    https://git.snt.utwente.nl/s1841742/dp9
                </v-flex>
            </v-layout>
        </v-container>
    </v-content>
</template>
</v-app>
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<script src="https://cdn.jsdelivr.net/npm/vuetify/dist/vuetify.js"></script>
<script>apiUrl = "<%=request.getContextPath()%>"</script>
<script src="<%=request.getContextPath()%>/vue_components/sidebar.js"></script>
<script src="<%=request.getContextPath()%>/vue_components/titlebar.js"></script>
<script src="<%=request.getContextPath()%>/manual/manual.js"></script>
</body>
</html>