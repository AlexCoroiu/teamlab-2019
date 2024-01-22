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
<html>
<head>
  <meta charset="UTF-8">
  <link href="https://fonts.googleapis.com/css?family=Roboto:100,300,400,500,700,900|Material+Icons" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/vuetify/dist/vuetify.min.css" rel="stylesheet">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui">
  <title>Create new test</title>
</head>
<body>
  <v-app id="app"><template>
    <sidebar
      username="${sessionScope.teacher.name}"
      email="${sessionScope.teacher.mail}"
    >
      <%--<p class="body-2">Tips</p>--%>

      <p class="body-1" style="white-space: pre-wrap;"
      >{{ assistant.TestText }}</p>

      <p class="body-1" style="white-space: pre-wrap;"
         v-if="editMode"
      >{{ assistant.TestTextSuggestion }}</p>
    </sidebar>

    <v-content>
      <titlebar
          username="${sessionScope.teacher.name}"
          email="${sessionScope.teacher.mail}"
      >
        <span v-if="!editMode">{{ name }}</span>
        <v-text-field
                v-else
                v-model="name"
                solo
                label="Test name"
                :rules="validation.name"
                v-on:blur="setName()"
                v-on:keyup.enter="setName()"
        ></v-text-field>
      </titlebar>

      <v-container fluid style="max-height: 1vh">
        <v-layout
            justify-space-between
            pa-3
            wrap
        >
          <v-flex
              v-if="!editMode"
              xs12
              class="grey lighten-5"
              <%--px-3--%>
              >
            <v-btn
              outline color="primary" style="border-radius: 3pt"
              v-on:click="editMode = !editMode"
            >
              Edit
            </v-btn>
          </v-flex>
          <v-flex
              v-else
              xs5
              class="grey lighten-5"
              px-3
          >
            <v-subheader>
              Components
            </v-subheader>
            <v-list
                    v-show="components.length > 0"
            >
              <v-list-tile
                v-for="component in components"
                :key="component.name"
                v-on:click="selectComponent(component)"
                >
                <v-list-tile-content>
                  <v-list-tile-title v-text="component.name"></v-list-tile-title> <!-- also maybe how many questions in this component -->
                </v-list-tile-content>
              </v-list-tile>
              <%--<v-list-tile>
                <v-list-tile-content>
                  <v-list-tile-action>
                  </v-list-tile-action>
                </v-list-tile-content>
              </v-list-tile>--%>
            </v-list>
            <v-dialog v-model="newComponentDialog" max-width="290">
              <template v-slot:activator="{}">
                <v-btn flat v-on:click="newComponentDialog = true" style="border-radius: 3pt">
                  <v-icon>add</v-icon>
                  <span>New component</span>
                </v-btn>
              </template>
              <v-layout>
                <v-flex class="white" py-1 px-2>
                  <v-card flat>
                    <v-text-field
                            label="Name"
                            v-model="newComponentName"
                            ref="newComponentName"
                            :rules="validation.name"
                    >
                    </v-text-field>
                    <v-card-actions>
                      <v-spacer></v-spacer>
                      <v-btn color="secondary" flat v-on:click="newComponentDialog = false; $refs.newComponentName.reset()">Cancel</v-btn>
                      <v-btn color="primary" :disabled="!isValidName(newComponentName)" v-on:click="newComponent()" outline>Add</v-btn>
                    </v-card-actions>
                  </v-card>
                </v-flex>
              </v-layout>
            </v-dialog>
          </v-flex>
          <v-flex
              v-if="editMode"
              d-flex
              xs7
              <%--text-xs-center--%>
              px-3
          >
            <v-scroll-y-transition mode="out-in">
              <div
                  v-if="!selected"
                  class="title grey--text text--lighten-1 font-weight-light"
                  style="align-self: center;"
              >
                Select a Component
              </div>
              <v-card
                v-else
                flat
                class="grey lighten-5 mx-auto"
              >
                <v-subheader>
                  Questions in component {{ selected.name }}
                </v-subheader>
                <v-list
                  v-show="questions.length > 0"
                >
                  <v-list-tile
                    v-for="(question, i) in questions"
                    :key="i"
                    v-on:click=""
                  >
                    <v-list-tile-action>
                      <v-icon v-show="question.selected"
                              color="teal darken-2" style="font-size: 14pt">check</v-icon>
                    </v-list-tile-action>
                    <v-list-tile-content
                      v-on:click="toggleSelected(question)"
                    >
                      <v-tooltip bottom color="white">
                        <template v-slot:activator="{ on }">
                          <v-list-tile-title v-text="question.text" v-on="on"></v-list-tile-title> <!-- also maybe how many questions in this component -->
                        </template>
                        <v-card flat clas="ma-2 pa-2">
                          <div class="ma-2">
                            <span class="title">{{ question.text }}</span>
                            <ul style="list-style: none">
                              <li
                                      v-for="(choice, i) in question.choices"
                                      :key="i"
                              >
                            <span <%-- TODO make this clean --%>
                                    :class="{'body-1': !(i === question.answer || Array.isArray(question.answer) && question.answer.includes(i)), 'body-2': (i === question.answer || Array.isArray(question.answer) && question.answer.includes(i))}"
                                    :style="{'color': (i === question.answer || Array.isArray(question.answer) && question.answer.includes(i)) ? 'teal' : 'black'}">
                              {{ choice }}
                            </span>
                              </li>
                            </ul>
                          </div>
                        </v-card>
                      </v-tooltip>
                    </v-list-tile-content>
                    <!--<v-list-tile-action>
                      <v-speed-dial
                              :direction="'left'"
                              :name="'hell'"
                              :open-on-hover='true'
                              :transition="'slide-x-reverse-transition'"
                      >
                        <template v-slot:activator>
                          <v-btn flat icon
                                 v-on:click="editQuestion(question)"
                          >
                            <v-icon>edit</v-icon>
                          </v-btn>
                        </template>
                        <v-btn
                          fab
                          dark
                          small
                          color="pink darken-2"
                          class="mr-3"
                          v-on:click="deleteQuestion(question)"
                        >
                          <v-icon>delete</v-icon>
                        </v-btn>
                      </v-speed-dial>
                    </v-list-tile-action>-->
                  </v-list-tile>
                  <%--<v-list-tile
                  >
                  </v-list-tile>--%>
                </v-list>
                <v-dialog v-model="newQuestionDialog" persistent max-width="60%">
                  <template v-slot:activator="{}">
                    <v-btn flat v-on:click="newQuestionDialog = true" style="border-radius: 3pt">
                      <v-icon>add</v-icon>
                      <span>New question</span>
                    </v-btn>
                  </template>
                  <v-card>
                    <v-layout row wrap>
                      <%--<v-flex>--%>
                      <%--<v-layout column>--%>
                      <%--</v-layout>--%>
                      <%--</v-flex>--%>
                      <v-flex>
                        <v-layout row wrap>
                          <%--<v-flex my-2>
                          </v-flex>--%>
                          <v-flex xs12 mx-4 px-4>
                            <v-layout column>
                              <v-flex xs12>
                                <v-textarea
                                        label="Text"
                                        v-model="newQuestionText"
                                        rows="2"
                                        auto-grow
                                        ref="newQuestionText"
                                        :rules="validation.name"
                                ></v-textarea>
                              </v-flex>
                              <v-flex xs12 mx-0>
                                <v-subheader>Type</v-subheader>
                                <v-btn-toggle v-model="newQuestionTypeIndex" mandatory>
                                  <v-layout row>
                                    <v-btn flat>
                                      <v-icon left>radio_button_checked</v-icon>
                                      <v-spacer></v-spacer>
                                      <span>Multiple choice</span>
                                    </v-btn>
                                    <v-btn flat>
                                      <v-icon left>check_box</v-icon>
                                      <v-spacer></v-spacer>
                                      <span>Checklist</span>
                                    </v-btn>
                                    <v-btn flat>
                                      <v-icon left>notes</v-icon>
                                      <v-spacer></v-spacer>
                                      <span>Essay</span>
                                    </v-btn>
                                    <v-btn flat>
                                      <v-icon left>short_text</v-icon>
                                      <v-spacer></v-spacer>
                                      <span>Short</span>
                                    </v-btn>
                                    <v-btn flat>
                                      <v-icon left>attach_file</v-icon>
                                      <v-spacer></v-spacer>
                                      <span>File upload</span>
                                    </v-btn>
                                  </v-layout>
                                </v-btn-toggle>
                              </v-flex>
                              <v-flex xs12>
                                <%-- Multiple choice (radio) --%>
                                <v-radio-group
                                        v-if="newQuestionType === 'radio'"
                                        v-model="newQuestionCorrectAnswer"
                                >
                                  <v-layout
                                  <%--align-center--%>
                                  <%--justify-start--%>
                                          v-for="(option, i) in newQuestionChoices"
                                          :key="i"
                                  >
                                    <v-flex shrink>
                                      <v-radio
                                              :value="i"
                                      ></v-radio>
                                    </v-flex>
                                    <v-flex grow>
                                      <v-text-field
                                              :label="'Option ' + (i + 1)"
                                              v-model="newQuestionChoices[i]"
                                      ></v-text-field>
                                    </v-flex>
                                  </v-layout>
                                </v-radio-group>

                                <%-- Checkbox --%>
                                <v-layout
                                <%--align-center--%>
                                <%--justify-start--%>
                                        v-if="newQuestionType === 'checkbox'"
                                <%--v-show="hasChoices(newQuestionType)"--%>
                                        v-for="(option, i) in newQuestionChoices"
                                        :key="i"
                                >
                                  <v-flex shrink>
                                    <v-checkbox
                                            multiple
                                            v-model="newQuestionCorrectAnswer"
                                            :value="i"
                                            :key="i"
                                    ></v-checkbox>
                                  </v-flex>
                                  <v-flex grow>
                                    <v-text-field
                                            :label="'Option ' + (i + 1)"
                                            v-model="newQuestionChoices[i]"
                                    ></v-text-field>
                                  </v-flex>
                                </v-layout>

                                <v-btn v-if="hasChoices(newQuestionType) && (newQuestionChoices.length < 6)"
                                       v-on:click="newChoice()"
                                       style="border-radius: 3pt"
                                       flat>
                                  <v-icon>add</v-icon>
                                  <span>New option</span>
                                </v-btn>

                                <%--&lt;%&ndash; File upload &ndash;%&gt;--%>
                                <%-- Why did I think this needs to go here? I'll just leave it so that I can easily copy it later. --%>
                                <%--<v-btn--%>
                                <%--v-if="newQuestionType == 'file'"--%>
                                <%--color="secondary"--%>
                                <%--v-on:click="prompt('Filename: just kidding')"--%>
                                <%-->--%>
                                <%--<v-icon left>cloud_upload</v-icon>--%>
                                <%--<span>Attach file</span>--%>
                                <%--</v-btn>--%>
                              </v-flex>
                            </v-layout>
                            <%--<ul>
                              <li>

                              </li>
                              <li>

                              </li>
                              <li>
                                <v-text-field
                                        label="Option B"
                                        v-model="newQuestionChoices[1]"
                                ></v-text-field>
                              </li>
                              <li>
                                <v-text-field
                                        label="Option C"
                                        v-model="newQuestionChoices[2]"
                                ></v-text-field>
                              </li>
                              <li>
                                <v-text-field
                                        label="Option D"
                                        v-model="newQuestionChoices[3]"
                                ></v-text-field>
                              </li>
                              <li>
                                <v-select
                                        :items="newQuestionAnswer"
                                        v-model="newQuestionCorrectAnswer"
                                        label="Answer"
                                ></v-select>
                              </li>
                            </ul>--%>
                          </v-flex>
                        </v-layout>
                      </v-flex>
                    </v-layout>
                    <v-card-actions>
                      <v-spacer></v-spacer>
                      <span v-if="newQuestionDialogOperation === 'add' && ['short', 'essay', 'file'].includes(newQuestionType)">
                        <i class="body-1" style="color: grey">Coming in a future version.</i>
                      </span>
                      <span v-else>
                        <v-icon size="1.48em" color="primary">info_outline</v-icon>
                        <span class="body-1">This question will be visible to other users.</span>
                      </span>
                      <v-btn color="secondary" flat v-on:click="closeAndResetQuestionDialog()">Cancel</v-btn>
                      <v-btn  v-if="newQuestionDialogOperation === 'add'" outline :disabled="!checkQuestionValidation()"
                              color="primary" flat v-on:click="newQuestion()">Add</v-btn>
                      <v-btn  v-if="newQuestionDialogOperation === 'edit'" outline :disabled="!checkQuestionValidation()"
                              color="primary" flat v-on:click="newQuestion(questionToEdit)">Save</v-btn>
                    </v-card-actions>
                  </v-card>
                </v-dialog>
              </v-card>
              <%--<v-card
                  v-else
                  :key="selected.id"
                  class="grey lighten-5 mx-auto"
                  flat
                  max-width="400"
                  min-height="260"
              >
                <v-list
                    three-line
                    :items:="selected.questions"
                    class="grey lighten-5"
                > <!-- TODO Replace with cards
                       or whatever just do something about the spacing.
                   -->
                  <v-subheader>
                    Questions in {{ selected.name }}
                  </v-subheader>
                  <template v-for="(question, index) in selected.questions">
                    <v-list-tile
                      :key="question.id"
                    >
                      <v-list-tile-content>
                        <v-list-tile-title
                            v-html="question.text"
                        >
                        </v-list-tile-title>
                        <v-list-tile-content>
                          <v-btn
                              small
                              round
                              color="blue-grey"
                              class="white--text"
                              v-on:click="addQuestion(question)"
                            >
                            <v-icon left>add</v-icon>
                            Add
                          </v-btn>
                        </v-list-tile-content>
                      </v-list-tile-content>
                    </v-list-tile>
                  </template>
                </v-list>
              </v-card>--%>
            </v-scroll-y-transition>
          </v-flex>
        <v-flex shrink mt-3 ml-2>
            <v-btn v-on:click="editMode = !editMode"
                   v-if="editMode"
                   color="primary"
                   outline
                   style="border-radius: 3pt"
            >
                <span v-show="editMode">Done</span>
                <span v-show="!editMode">Edit</span>
            </v-btn>
        </v-flex>
          <v-flex xs12 mt-4>
            <div v-if="editMode">
              <v-divider v-if="editMode"></v-divider>
              <br/><br/>
            </div>
            <v-card flat style="overflow-y: scroll">
              <v-card flat class="mt-3" v-for="(component, i) in components" :key="i">
                <span class="ml-3 title">{{ component.name }}</span>
                <v-expansion-panel expand style="box-shadow: none"
                  >
                    <v-expansion-panel-content v-for="(question, i) in component.questions"
                         :key="i" class="mb-1"
                    >
                      <template v-slot:header>
                        <%--<div>--%>
                          <span class="subheading">{{ question.text }}</span>
                        <%--</div>--%>
                      </template>
                      <ul style="list-style: none">
                        <li
                                v-for="(choice, i) in question.choices"
                                :key="i"
                        >
                            <span <%-- TODO make this clean --%>
                                    :class="{'body-1': !(i === question.answer || Array.isArray(question.answer) && question.answer.includes(i)), 'body-2': (i === question.answer || Array.isArray(question.answer) && question.answer.includes(i))}"
                                    :style="{'color': (i === question.answer || Array.isArray(question.answer) && question.answer.includes(i)) ? 'teal' : 'black'}">
                              {{ choice }}
                            </span>
                        </li>
                      </ul>

                    </v-expansion-panel-content>
                  </v-expansion-panel>
              </v-card>
            </v-card>
          </v-flex>
          <v-flex shrink>
            <v-btn v-on:click="returnToCourse()"
                   v-if="returnTo && !editMode"
                   color="primary"
                   outline
                   style="border-radius: 3pt"
            >
              return to course
            </v-btn>
          </v-flex>
          <v-flex grow><v-spacer></v-spacer></v-flex>
        </v-layout>
      </v-container>
    </v-content>
</template></v-app>

<script src="https://unpkg.com/axios/dist/axios.js"></script>
<script src = "https://cdn.rawgit.com/abdmob/x2js/master/xml2json.js"></script>
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<script src="https://cdn.jsdelivr.net/npm/vuetify/dist/vuetify.js"></script>
<script>apiUrl = "<%=request.getContextPath()%>"</script>
<script src="<%=request.getContextPath()%>/vue_components/sidebar.js"></script>
<script src="<%=request.getContextPath()%>/vue_components/titlebar.js"></script>
<script src="<%=request.getContextPath()%>/functions.js"></script>
<script src="<%=request.getContextPath()%>/tests/test.js"></script>
</body>
</html>