<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <title>Take test</title>
</head>
<body>

<v-app id="app">
<template>
    <%--<v-card flat>
        <ul>
            <li>c: {{ c }}</li>
            <li>q: {{ q }}</li>
        </ul>
        <br/><br/>
    </v-card>--%>

    <v-container fluid class="grey lighten-5">
        <v-layout
            row wrap
        >
            <v-flex xs12>
                <%--Ask about motivation.--%>
                <v-card id="generalCard" v-if="generalBox.show" flat class="grey lighten-5">
                    <template>
                        <v-card-title>
                            <v-layout row wrap>
                                <%--<div>--%>
                                <v-flex
                                        xs6
                                >
                                    <v-text-field
                                            v-model="generalInfo.name"
                                            label="Full Name"
                                            disabled
                                            background-color="grey lighten-4"
                                            :rules="validation.name"
                                            solo
                                    ></v-text-field>
                                </v-flex>
                                <v-flex
                                        xs6
                                >
                                    <v-text-field
                                            v-model="generalInfo.studentNumber"
                                            label="Student number"
                                            :rules="validation.name"
                                            solo
                                    ></v-text-field>
                                </v-flex>
                                <v-flex xs6>
                                    <v-text-field
                                            v-model="generalInfo.email"
                                            initial="${sessionScope.teacher.mail}"
                                            disabled
                                            background-color="grey lighten-4"
                                            label="Email"
                                            :rules="validation.email"
                                            validate-on-blur
                                            solo
                                    >
                                    </v-text-field>
                                </v-flex>
                                <v-flex
                                        xs6
                                >
                                    <v-select
                                            v-model="generalInfo.belbin"
                                            label="Belbin role"
                                            :items="belbinOptions"
                                            item-text="name"
                                            item-value="value"
                                            :rules="validation.notNull"
                                            solo
                                            persistent-hint
                                            hint="<a href='https://www.idrlabs.com/team-role/test.php' target='_blank'>Click here</a> to learn about belbin roles."
                                    >
                                    </v-select>
                                </v-flex>
                                <v-flex
                                        xs6
                                >
                                    <v-select
                                            v-model="generalInfo.study"
                                            label="Study"
                                            :items="participatingStudies"
                                            :rules="validation.notNull"
                                            solo
                                    ></v-select>
                                </v-flex>
                                <v-flex
                                        xs6
                                >
                                    <v-autocomplete
                                            v-model="generalInfo.nationality"
                                            :items="countryCodes"
                                            label="Nationality"
                                            :rules="validation.notNull"
                                            solo
                                    ></v-autocomplete>
                                </v-flex>
                                <v-flex
                                        xs6
                                >
                                    <v-select
                                            v-model="generalInfo.gender"
                                            :items="['M', 'F', 'NB']"
                                            label="Gender"
                                            :rules="validation.notNull"
                                            solo
                                    ></v-select>
                                </v-flex>
                                <%--</div>--%>
                                <v-flex xs12>
                                    <v-btn primary
                                           v-on:click="showGeneral(false); showMotivation(true)"
                                    >
                                        Next
                                    </v-btn>
                                </v-flex>
                            </v-layout>
                        </v-card-title>
                    </template>
                </v-card>

                <%--Ask about motivation.--%>
                <v-card id="motivationCard" v-if="motivationBox.show" flat class="grey lighten-5">
                    <template>
                        <v-card-title>
                            <v-layout row wrap>
                                <v-flex xs12>
                                    <div v-for="question in motivationBox.questions">
                                        <v-layout row wrap>
                                            <v-flex
                                                    xs6 sm4
                                            >
                                                {{ question.text }}
                                            </v-flex>
                                            <v-flex
                                                    xs6 sm4
                                            >
                                                <v-select
                                                        v-model="question.answer"
                                                        :items="[1, 2, 3, 4, 5]"
                                                        solo
                                                ></v-select>
                                            </v-flex>
                                        </v-layout>
                                    </div>
                                </v-flex>
                                <v-flex
                                        xs6
                                >
                                    <v-btn primary
                                           v-on:click="showMotivation(false); askAboutComponent(components[0])"
                                    >
                                        Next
                                    </v-btn>
                                </v-flex>
                            </v-layout>
                        </v-card-title>
                    </template>
                </v-card>

                <%--Ask if they know this component--%>
                <v-card id="componentCard" v-if="componentBox.show" flat class="grey lighten-5">
                    <template>
                        <v-card-title>
                            <v-layout row wrap>
                                <v-flex xs12 justify-start>Do you have any experience working with <strong>{{ componentBox.component }}</strong>?</v-flex>
                                <v-flex xs12 sm3 md2 lg1>
                                    <v-btn  block
                                            primary
                                            v-on:click="askAboutComponent(false); nextQuestion()"
                                    >
                                        Yes
                                    </v-btn>
                                </v-flex>
                                <v-flex xs12 sm3 md2 lg1>
                                    <v-btn  block
                                            secondary
                                            v-on:click="skipComponent()"
                                    >
                                        No
                                    </v-btn>
                                </v-flex>
                                <v-flex grow></v-flex>
                            </v-layout>
                        </v-card-title>
                    </template>
                </v-card>

                <%--Current question--%>
                <v-card id="questionCard" v-if="questionBox.show" flat class="grey lighten-5">
                    <template>
                        <v-card-title>
                            <v-layout row wrap>
                                <v-flex xs12><span class="title">{{ questionBox.question.text }}</span></v-flex>
                                <v-flex xs12>
                                    <v-radio-group
                                            v-if="questionBox.question.type === 'radio'"
                                            v-model="questionBox.question.answer">
                                        <v-radio
                                                v-for="(choice, index) in questionBox.question.choices"
                                                v-if="choice"
                                                :key="choice"
                                                :label="choice"
                                                :value="index"
                                        ></v-radio>
                                    </v-radio-group>

                                    <div
                                            v-else-if="questionBox.question.type === 'checkbox'"
                                    >
                                        <v-checkbox
                                                v-for="(choice, index) in questionBox.question.choices"
                                                v-if="choice"
                                                :key="choice"
                                                :label="choice"
                                                :value="index"
                                                multiple
                                                v-model="questionBox.question.answer"
                                        ></v-checkbox>
                                    </div>

                                    <div
                                        v-else-if="questionBox.question.type === 'essay'"
                                    >
                                        <v-textarea v-model="questionBox.question.answer">
                                        </v-textarea>
                                    </div>

                                    <div
                                        v-else-if="questionBox.question.type === 'short'"
                                    >
                                        <v-text-field v-model="questionBox.question.answer">
                                        </v-text-field>
                                    </div>

                                    <div
                                        v-else-if="questionBox.question.type === 'file'"
                                    >
                                        <span class="body-2">File to upload: </span>
                                        <v-btn flat v-on:click="$refs.inputUpload.click()">
                                            <v-icon left>attached_document</v-icon>
                                            Select file
                                        </v-btn>
                                        <input v-show="false" ref="inputUpload" type="file"/>
                                    </div>
                                </v-flex>
                                <v-flex xs12>
                                    <v-btn primary
                                           v-on:click="nextQuestion()"
                                    >
                                        Next
                                    </v-btn>
                                </v-flex>
                            </v-layout>
                        </v-card-title>
                    </template>
                </v-card>

                <%--Ask if they used multiple components together or if they used CS skills in another field--%>
                <v-card id="extraCard" v-if="extraBox.show" flat class="grey lighten-5">
                    <template>
                        <v-card-title>
                            <v-layout row wrap>
                                <div>
                                    <v-flex
                                            xs6
                                    >
                                        Have you used at least two of these components together?
                                    </v-flex>
                                    <v-flex
                                            xs6
                                    >
                                        <v-select
                                                v-model="extraKnowledge.multipleComponents"
                                                :items="[true, false]"
                                                solo
                                        ></v-select>
                                    </v-flex>
                                </div>
                                <div>
                                    <v-flex
                                            xs6
                                    >
                                        Have you ever used Computer Science skills outside of a stricly Computer Science context?
                                    </v-flex>
                                    <v-flex
                                            xs6
                                    >
                                        <v-select
                                                v-model="extraKnowledge.otherFields"
                                                :items="[true, false]"
                                                solo
                                        ></v-select>
                                    </v-flex>
                                </div>
                                <v-flex
                                        xs6
                                >
                                    <v-btn primary
                                           v-on:click="showExtra(false); showEndOfTest()"
                                    >
                                        Next
                                    </v-btn>
                                </v-flex>
                            </v-layout>
                        </v-card-title>
                    </template>
                </v-card>

                <%--Last page of test--%>
                <v-card id="endCard" v-if="endBox.show" flat class="grey lighten-5">
                    <template>
                        <v-card-title>
                            <v-layout row wrap>
                                <v-flex xs12><strong>Final page</strong></v-flex>
                                <v-flex xs12>
                                    <span>Bravo! You have completed this difficult test.<br/>Pray that the gods are merciful.</span>
                                </v-flex>
                                <v-flex xs12>
                                    <v-btn primary
                                           v-on:click="submitTest()"
                                    >
                                        Submit
                                    </v-btn>
                                </v-flex>
                            </v-layout>
                        </v-card-title>
                    </template>
                </v-card>
            </v-flex>
        </v-layout>
    </v-container>
</template>
</v-app>

<script src="https://unpkg.com/axios/dist/axios.js"></script>
<script src = "https://cdn.rawgit.com/abdmob/x2js/master/xml2json.js"></script>
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<script src="https://cdn.jsdelivr.net/npm/vuetify/dist/vuetify.js"></script>
<script>
  apiUrl = "<%=request.getContextPath()%>";
  loggedInName = "${sessionScope.teacher.name}"
  loggedInEmail = "${sessionScope.teacher.mail}"
</script>
<script src="<%=request.getContextPath()%>/vue_components/sidebar.js"></script>
<script src="<%=request.getContextPath()%>/vue_components/titlebar.js"></script>
<script src="<%=request.getContextPath()%>/functions.js"></script>
<script src="<%=request.getContextPath()%>/tests/take.js"></script>
</body>
</html>
