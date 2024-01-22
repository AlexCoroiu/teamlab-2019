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

let procedure = sessionStorage.getItem('procedure');
let id = sessionStorage.getItem('id');
let returnTo = sessionStorage.getItem('returnTo');
let converter = new X2JS();

let debug = false;

window.onbeforeunload = function(){
  sessionStorage.removeItem('returnTo');
};

var app = new Vue({
  el: "#app",

  data: () => ({
    procedure: procedure,
    id: parseInt(id),
    returnTo: JSON.parse(returnTo),
    editMode: procedure === 'add',

    assistant:{
      TestTextSuggestion: 'Make sure you structure your course contents in components for the SOLO assessment\n\n' +
          'Each component will be scored separately\n\n' +
          'Students will be graded according to how many components they know\n\n',
      TestText: 'This is your test! :)'
    },

    name: "Untitled Test",
    components: [],
    questions: [],
    selected: null,

    newComponentDialog: false,
    newComponentName: "",
    newQuestionDialog: false,
    newQuestionDialogOperation: 'add',
    newQuestionTypeIndex: 1,
    questionTypes: ["radio", "checkbox", "essay", "short", "file"],
    newQuestionText: "",
    newQuestionChoices: ["", ""],
    newQuestionAnswer: [1, 2, 3, 4],
    newQuestionCorrectAnswer: [],

    selectedQuestions: {},

    validation: {
      name: [
        name => (typeof name !== 'undefined' && !!name) || "Can't be empty.",
        name => (typeof name !== 'undefined' && !!name.replace(/\s/g, '').length) || "Can't be just spaces."
      ]
    },

    drawer: true,
    right: null
  }),

  computed: {
    newQuestionType: function () {
      return this.questionTypes[this.newQuestionTypeIndex]
    }
  },

  mounted() {
    this.$vuetify.theme.primary = '#3F51B5';

    if (procedure !== 'add') {
      axios
        .get(apiUrl + '/controller/tests/' + this.id, { headers: { 'Content-type' : 'application/xml' } })
        .then(response => {
          console.log(response);
          let test = converter.xml_str2json(response.data).test;
          console.log("Downloaded test:");
          console.log(test);
          app.name = test.name;
          console.log("Questions loaded:");
          if(Array.isArray(test.questions))               app.questions = test.questions;
          else if (typeof test.questions === "undefined") app.questions = [];
          else                                            app.questions = [test.questions];
          // build components list
          app.questions.forEach(function (question, i) {
            try {
              console.log(i + ". " + question.text);
            } catch (error) {
              console.log("Caught: ", error);
            }
            // deserialize answers properly
            if (typeof question.answer !== 'undefined')
              question.answer = JSON.parse(question.answer);
            // build components array
            let componentName = question.component; // which is a string with the name
            let component = app.components.find(component => component.name === componentName);
            if (!component) {
              component = {name: componentName, questions: []};
              app.components.push(component);
            }
            component.questions.push(question);
          });
          console.log("Components loaded: ", app.components);
        })
        .catch(error => console.log(error, error.response))
    } else if (procedure === 'add') {
      let newTest = {
        test: {
            name: this.name,
            questionsByComponents: {},
            questions: []
        }
      };
      newTest = converter.json2xml_str(newTest);
      console.log(newTest);
      axios
        .post(apiUrl + '/controller/tests/new', newTest, {headers: {'Content-Type': 'application/xml'}})
        .then(function (response) {
          console.log(response);
          console.log(converter.xml_str2json(response.data));
          app.id = converter.xml_str2json(response.data).test.id;
          app.procedure = 'edit'
        })
        .catch(function (error) {
          console.log(error, error.response);
        });
    }
  },

  watch: {
    selected: function (selectedComponent) {
      // get list of questions from server relevant to the selected component's name
      console.log("\nGetting questions for component \"" + selectedComponent.name + "\"\n");
      let newQuestions = []; // TODO get questions from server with tag selectedComponent
      axios
        .get(apiUrl + '/controller/tests/new/questions/' + selectedComponent.name)
        .then(function (response) {
          console.log(response);
          console.log(response.data);
          // app.xml = response.data;
          let downloadedQuestions = converter.xml_str2json(response.data).questions.question;
          console.log(downloadedQuestions);
          // app.course = course;
          app.downloadedQuestions = normalizeArrayDeserialization(downloadedQuestions);
          app.downloadedQuestions.forEach(function (question) {
            question.component = selectedComponent.name
          });
          newQuestions = app.downloadedQuestions;

          if (app.selected.questions) {
            // insert all questions added locally that the server doesn't know of yet
            app.selected.questions.forEach(function (question) {
              let exists = false;
              newQuestions.forEach(q => {
                if (q.id === question.id) { exists = true; q.selected = true }
              });
              if (!exists) {
                newQuestions.push(question)
              }
            });
            newQuestions.forEach(function (question) {
              if (app.selected.questions.includes(question))
                question.selected = true
            });
          } else {
            app.selected.questions = []
          }
          if (newQuestions) {
            app.questions = newQuestions
          }
        })
        .catch(function (error) {
          console.log(error, error.response);
        });


    }
  },

  methods: {
    showQuestions () {
      console.log(app.selected)
    },

    selectComponent (component) {
      app.selected = component
    },

    isValidName (name) {
      let valid = true;
      if (typeof app !== 'undefined' && typeof name !== 'undefined' && name !== null)
        app.validation.name.forEach(function (rule) {if (rule(name) !== true) valid = false});
      return valid;
    },

    setName () {
      if (!app.isValidName(app.name)) return;
      axios
        .put(apiUrl + '/controller/tests/' + app.id + '/name', app.name, {headers: {'Content-Type': 'text/plain'}})
        .then(response => console.log(response))
        .catch(error => console.log(error));
    },

    // send a request to the API
    updateQuestion (question, operation) {
      let safeChoices;
      if (question.type === 'radio' || question.type === 'checkbox') {
        safeChoices = question.choices;
      } else {
        safeChoices = ["fake_answer"];
      }
      for (i = 0; i < 6; ++i) safeChoices[i] = safeChoices[i] ? safeChoices[i] : "";
      let safeAnswer = "" + question.answer;
      if (Array.isArray(question.answer)) {
        safeAnswer = "";
        question.answer.forEach(choice => safeAnswer += (choice + "&"))
      }
      console.log("ANSWER IS ", safeAnswer);
      let modelQuestion = {  // what the server should be able to deserialize
        id: question.id,
        text: question.text,
        type: question.type,
        choices: safeChoices,
        answer: /*JSON.stringify*/safeAnswer,
        component: question.component,
      };
      console.log(question);
      console.log(modelQuestion);
      modelQuestion = converter.json2xml_str({question: modelQuestion});
      console.log(modelQuestion);

      if (operation === 'add') {
        axios
          .post(apiUrl + '/controller/tests/' + app.id + '/question', modelQuestion, {headers: {'Content-Type': 'application/xml'}})
          .then(function (response) {
            console.log(response);
            question.id = parseInt(response.data)
          })
          .catch(error => console.log(error, error.response))
      } else if (operation === 'delete') {
        axios
          .delete(apiUrl + '/controller/tests/' + app.id + '/question/' + question.id, {headers: {'Content-Type': 'application/xml'}})
          .then(response => console.log(response))
          .catch(error => console.log(error, error.response))
      }
    },

    toggleSelected (question) {
      console.log("KILLME");
      if (question.selected) {
        question.selected = false;
        app.selected.questions = app.selected.questions.filter(selectedQuestion => selectedQuestion !== question)
        app.updateQuestion(question, 'delete')
      } else {
        question.selected = true;
        app.selected.questions.push(question);
        app.updateQuestion(question, 'add')
      }
      this.$forceUpdate()
    },

    newComponent () {
      let newComponent = {
        name: JSON.parse(JSON.stringify(app.newComponentName))
      };
      app.components.push(newComponent);
      // reset values in new component modal and close it
      app.newComponentName = "";
      app.newComponentDialog = false;
      app.$refs.newComponentName.resetValidation()
      app.selectComponent(newComponent)
    },

    newQuestion (question) {
      let newQuestion = {
        id: 0, // this is what the server expects for new questions (i.e. ones that aren't in the database)
        type: JSON.parse(JSON.stringify(app.newQuestionType)),
        text: JSON.parse(JSON.stringify(app.newQuestionText)),
        ...(app.hasChoices(app.newQuestionType) && {choices: JSON.parse(JSON.stringify(app.newQuestionChoices))}),
        ...(app.hasChoices(app.newQuestionType) && {answer: JSON.stringify(app.newQuestionCorrectAnswer)}),
        component: JSON.parse(JSON.stringify(app.selected.name)),
      };
      if (app.newQuestionDialogOperation === 'add') {
        console.log("New question: ", newQuestion);
        app.questions.push(newQuestion);
        app.toggleSelected(newQuestion); // make it selected
      } else {
        let i = app.questions.indexOf(app.questionToEdit);
        app.questions[i] = newQuestion;
        let component = app.components.find(c => c.name === question.component);
        i = component.questions.indexOf(question);
        component.questions[i] = newQuestion;
        app.questionToEdit = null;
        app.deleteQuestion(question);
        app.toggleSelected(newQuestion)
        // app.toggleSelected(question)
      }

      app.closeAndResetQuestionDialog()
    },

    editQuestion (question) {
      // if (question.selected) app.toggleSelected(question);
      question.id = 0;  // need to create a new question in the backend
      app.questionToEdit = question;
      app.newQuestionText = question.text;
      app.newQuestionTypeIndex = app.questionTypes.indexOf(question.type);
      app.newQuestionChoices = question.choices ? question.choices : [];
      app.newQuestionCorrectAnswer = typeof question.answer !== 'undefined' ? question.answer : null;
      app.newQuestionDialogOperation = 'edit';
      app.newQuestionDialog = true;
    },

    deleteQuestion (question) {
      question.selected = false;
      app.selected.questions = app.selected.questions.filter(q => q !== question);
      let component = app.components.find(c => c.name === question.component);
      component.questions = component.questions.filter(q => q !== question);
      app.questions = app.questions.filter(q => q !== question);
      app.updateQuestion(question, 'delete')
      app.$forceUpdate();
    },

    // reset values in new question modal and close it
    closeAndResetQuestionDialog (question) {
      app.newQuestionDialog = false;
      app.$refs.newQuestionText.reset();
      app.newQuestionChoices = ["", ""];
      app.newQuestionCorrectAnswer = [];
      app.newQuestionDialogOperation = 'add'
    },

    checkQuestionValidation () {
      return app.isValidName(app.newQuestionText) && app.checkAnswersValidation() && !['short', 'essay', 'file'].includes(app.newQuestionType);
    },

    checkAnswersValidation () {
      return (
            (app.hasChoices(app.newQuestionType) && app.newQuestionChoices.filter(
                choice => typeof choice !== "undefined" && choice !== null && app.isValidName(choice)
              ).length >= 2
              && (typeof app.newQuestionCorrectAnswer != 'undefined' && app.newQuestionCorrectAnswer !== null
                && (!Array.isArray(app.newQuestionCorrectAnswer) || app.newQuestionCorrectAnswer.length > 0)
              )
            )
            || (!app.hasChoices(app.newQuestionType))
      )
    },

    newChoice () {
      app.newQuestionChoices.push("")
    },

    hasChoices (questionType) {
      return questionType === "radio" || questionType === "checkbox"
    },

    addQuestion (question) {
      question.inTest = true;
      this.$forceUpdate()
      // let component = this.selected.id;
      // if (!this.selectedQuestions[component])
      //   this.selectedQuestions[component] = [];
      // this.selectedQuestions[component].push(question); // TODO only do this if it doesn't exist already
      // console.log("selectedQuestions[" + component + "] += " + question.text)
    },

    returnToCourse () {
      let returnInfo = JSON.parse(sessionStorage.getItem("returnTo"));
      sessionStorage.setItem("newTestId", app.id);
      sessionStorage.setItem("procedure", "continue");
      sessionStorage.setItem("id", returnInfo.id);  // id of test to be loaded
      window.location.href = returnInfo.location;
    },

    nextPage () {
      let serializedCategories = JSON.stringify(this.categories);
      window.localStorage.setItem("categories", serializedCategories);
      console.log(serializedCategories);
      window.location.href = "./view.html"
    },

    gotoPage (url) {
      window.location.href = url
    },

    submitTest () {
      let questionsInTestMap = {};
      let questionsInTestList = [];  // server really only expects this one at this time
      this.components.forEach(function (component) {
        if (component) component.questions.forEach(function (question) {
          if (question.selected) {
            // extend the array length to 6 to keep the API call from crashing
            let safeChoices;
            if (question.type === 'radio' || question.type === 'checkbox') {
              safeChoices = question.choices;
            } else {
              safeChoices = ["fake_answer"];
            }
            console.log("safechoices (before): ", safeChoices);
            for (i = 0; i < 6; ++i) safeChoices[i] = safeChoices[i] ? safeChoices[i] : "";
            console.log("safechoices (after ): ", safeChoices);
            let modelQuestion = {  // what the server should be able to deserialize
              id: question.id,
              text: question.text,
              type: question.type,
              choices: safeChoices,
              answer: JSON.stringify(question.answer),
              component: component.name,
              tags: [component.name]
            };
            // if (questionsInTestMap[component.name]) questionsInTestMap[component.name].push(question);
            // else                                    questionsInTestMap[component.name] = [question];
            questionsInTestList.push(modelQuestion)
          }
        })
      });

      console.log("Questions in this test:");
      console.log(questionsInTestMap);
      console.log("\nSending to server...");

      //TODO make the title field editable and linked to a name variable
      let newTest = ({
        test: {
          name: this.name,
          questionsByComponents: questionsInTestMap,
          questions: questionsInTestList
        }
      });
      console.log(newTest);
      newTest = converter.json2xml_str(newTest);
      console.log(newTest);
      axios
        .post(apiUrl + '/controller/tests/new', newTest, {headers: {'Content-Type': 'application/xml'}})
        .then(function (response) {
          console.log(response);
          console.log(response.data);

          let returnData = JSON.parse(sessionStorage.getItem("returnTo"));
          console.log(returnData);
          sessionStorage.removeItem("returnTo");
          if (!returnData) {
            if (!debug) {
              let messages = [
                // TODO write something nice which can be shown when returning to test list
              ];
              sessionStorage.setItem('messages', messages);
              window.location.href = '../tests'
            }
          } else {
            if (returnData.location.endsWith("/courses/id")) {
              sessionStorage.setItem("newTestId", app.id);
              sessionStorage.setItem("procedure", "continue");
              sessionStorage.setItem("id", returnData.id);
              window.location.href = returnData.location
            }
          }
        })
        .catch(function (error) {
          console.log(error, error.response);
        });
    }

    /*loadCategories () {
      json = fetch('./components.json').json;
      return categories.children.push(...json)
    }*/
  }
});
