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

let converter = new X2JS();

/*Vue.component ('question', {
  props: ['text', 'choices', 'answer'],

  data: function () {
    return {
      answer: answer
    }
  },

  methods: {

  },

  template: `
    <v-card>
      <v-card-title>
        <strong>{{ text }}</strong><br/>
        <v-radio-group v-model="answer">
          <v-radio
            v-for="(choice, index) in choices"
            :key="choice"
            :label="choice"
            :value="index"
          ></v-radio>
        </v-radio-group>
      </v-card-title>
    </v-card>
  `
});*/

var app = new Vue ({
  el: "#app",
  data: {
    questionsByComponents: {
      /*algebra: [
        {
          text: "Do you like bananas?",
          choices: ["Yes", "No", "Strawberries"],
          answer: null
        },
        {
          text: "Which number is a perfect square?",
          choices: ["2", "4", "6", "8"],
          answer: null
        }
      ],
      dinosaurs: [
        {
          text: "Are dinosaurs cool?",
          choices: ["Yes", "No", "Somewhat", "Who cares?"],
          answer: null
        },
        {
          text: "Are birds cool?",
          choices: ["Birds are the coolest", "BIRDS!!!!", "OMG BIRDS", "Yessss! YEEEESS!!"],
          answer: null
        }
      ]*/
    },
    c: 0,
    q: -1,
    // motivationQuestions: null,
    generalInfo: {
      name: typeof loggedInName !== 'undefined' ? loggedInName : null,
      studentNumber: null,
      study: null,
      belbin: null,
      email: typeof loggedInEmail !== 'undefined' ? loggedInEmail : null,
      nationality: null,
      gender: null
    },
    extraKnowledge: {
      multipleComponents: null,
      otherFields: null
    },
    participatingStudies: [/*"TCS", "AM", "PSY", "EE", "BIT", "TW"*/],
    generalBox: {
      show: true
    },
    motivationBox: {
      show: false,
      questions: [
        { text: "I am excited about the material of this module.", answer: 0, negative: false },
        { text: "I intend to dedicate plenty of time to working for this module.", answer: 0, negative: false },
        { text: "I am confident I will pass the module.", answer: 0, negative: false },
        { text: "I have certain circumstances that may prevent me from properly working on the module.", answer: 0, negative: true },
        { text: "I expect my experience with this module to be a positive one.", answer: 0, negative: false }
      ]
    },
    componentBox: {
      show: false,
      component: null
    },
    questionBox: {
      show: false,
      component: null,
      question: null
    },
    extraBox: {
      show: false
    },
    endBox: {
      show: false
    },
    id: null,

    belbinOptions: [
      { name: "Thinking", value: "T" },
      { name: "People",   value: "P" },
      { name: "Action",   value: "A" }
    ],

    validation: { // got this from stackoverflow, here: https://stackoverflow.com/a/52447947
      email: [
        value => {
          if(value && value.length > 0) {
            const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
            return pattern.test(value) || 'Invalid e-mail.';
          } else {
            return false
          }
        }
      ],

      name: [
        name => (name != null && !!name) || "Can't be empty.",
        name => name != null && !!name.replace(/\s/g, '').length || "Can't be just spaces."
      ],

      notNull: [ fieldValue => (fieldValue != false && fieldValue != null) || "Needs value." ]

    },

    countryCodes: JSON.parse("[\"AF\",\"AL\",\"DZ\",\"AS\",\"AD\",\"AO\",\"AQ\",\"AG\",\"AR\",\"AM\",\"AW\",\"AU\",\"AT\",\"AZ\",\"BS\",\"BH\",\"BD\",\"BB\",\"BY\",\"BE\",\"BZ\",\"BJ\",\"BM\",\"BT\",\"BO\",\"BA\",\"BW\",\"BV\",\"BR\",\"IO\",\"BN\",\"BG\",\"BF\",\"BI\",\"KH\",\"CM\",\"CA\",\"CV\",\"KY\",\"CF\",\"TD\",\"CL\",\"CN\",\"CX\",\"CC\",\"CO\",\"KM\",\"CG\",\"OF\",\"CD\",\"CK\",\"CR\",\"CI\",\"HR\",\"CU\",\"CY\",\"CZ\",\"DK\",\"DJ\",\"DM\",\"DO\",\"EC\",\"EG\",\"EL\",\"SV\",\"GQ\",\"ER\",\"EE\",\"ET\",\"FK\",\"FO\",\"FJ\",\"FI\",\"FR\",\"GF\",\"PF\",\"TF\",\"GA\",\"GM\",\"GE\",\"DE\",\"GH\",\"GI\",\"GR\",\"GL\",\"GD\",\"GP\",\"GU\",\"GT\",\"GN\",\"GW\",\"GY\",\"HT\",\"HM\",\"HN\",\"HK\",\"HU\",\"IS\",\"IN\",\"ID\",\"OF\",\"IR\",\"IQ\",\"IE\",\"IL\",\"IT\",\"JM\",\"JP\",\"JO\",\"KZ\",\"KE\",\"KI\",\"OF\",\"KP\",\"OF\",\"KR\",\"KW\",\"KG\",\"LA\",\"LV\",\"LB\",\"LS\",\"LR\",\"LY\",\"LI\",\"LT\",\"LU\",\"MO\",\"OF\",\"MK\",\"MG\",\"MW\",\"MY\",\"MV\",\"ML\",\"MT\",\"MH\",\"MQ\",\"MR\",\"MU\",\"YT\",\"MX\",\"OF\",\"FM\",\"OF\",\"MD\",\"MC\",\"MN\",\"ME\",\"MS\",\"MA\",\"MZ\",\"MM\",\"NA\",\"NR\",\"NP\",\"NL\",\"AN\",\"NC\",\"NZ\",\"NI\",\"NE\",\"NG\",\"NU\",\"NF\",\"MP\",\"NO\",\"OM\",\"PK\",\"PW\",\"PS\",\"PA\",\"PG\",\"PY\",\"PE\",\"PH\",\"PN\",\"PL\",\"PT\",\"PR\",\"QA\",\"RE\",\"RO\",\"RU\",\"RW\",\"SH\",\"KN\",\"LC\",\"PM\",\"VC\",\"WS\",\"SM\",\"ST\",\"SA\",\"SN\",\"RS\",\"SC\",\"SL\",\"SG\",\"SK\",\"SI\",\"SB\",\"SO\",\"ZA\",\"GS\",\"ES\",\"LK\",\"SD\",\"SR\",\"SJ\",\"SZ\",\"SE\",\"CH\",\"SY\",\"TW\",\"TJ\",\"OF\",\"TZ\",\"TH\",\"TL\",\"TG\",\"TK\",\"TO\",\"TT\",\"TN\",\"TR\",\"TM\",\"TC\",\"TV\",\"UG\",\"UA\",\"AE\",\"GB\",\"US\",\"UM\",\"UY\",\"UZ\",\"VU\",\"VE\",\"VN\",\"VG\",\"VI\",\"WF\",\"EH\",\"YE\",\"ZM\",\"ZW\"]"),

  },

  computed: {
    components: function () {
      return Object.keys(this.questionsByComponents)
    },
    questions: function () {
      return this.questionsByComponents[this.components[this.c]]
    }
  },

  mounted: function () {
    let urlFragments = window.location.href.split('/');
    this.id = urlFragments[urlFragments.length - 1];
    console.log('this.id: ' + this.id);
    axios
      .get(apiUrl + '/controller/courses/' + this.id + '/student')
      .then(function (response) {
        console.log(response.data);
        // app.xml = response.data;
        let course = converter.xml_str2json(response.data).course;
        console.log(course);
        app.participatingStudies = normalizeArrayDeserialization(course.studies);
        // app.course = course;
        app.downloadedCourse = course;
        // app.questionsByComponents = course.test.questionsByComponents;
        let questions = normalizeArrayDeserialization(course.test.questions);
        questions.forEach(function (question, i) {
          console.log(i + ". " + question.text);
          let componentName = question.component; // which is a string with the name
          if (!app.questionsByComponents[componentName]) {
            app.questionsByComponents[componentName] = [];
          }
          app.questionsByComponents[componentName].push(question);
        });
      })
      .catch(function (error) {
        console.log(error);
      });
  },

  methods: {
    checkValidationGeneral () {
      let valid = true;
      if (typeof app !== 'undefined') {
        app.validation.name.forEach(function (rule) {
          if (rule(app.generalInfo.name) !== true || rule(app.generalInfo.studentNumber) !== true) valid = false
        });
        app.validation.email.forEach(function (rule) {
          if (rule(app.generalInfo.email) !== true) valid = false
        });
        if (!(app.generalInfo.gender && app.generalInfo.nationality && app.generalInfo.study)) valid = false;
      }
      return valid;
    },

    showGeneral (boolean) {
      if (boolean === true || boolean === undefined)
        this.generalBox.show = true;
      else
        this.generalBox.show = false
    },
    showMotivation (boolean) {
      if (!this.checkValidationGeneral()) { alert("Please fill in all values correctly"); this.generalBox.show = true; return; } //fIXME wrong condition, some of this code shouldn't be here
      if (boolean === true || boolean === undefined)
        this.motivationBox.show = true;
      else
        this.motivationBox.show = false
    },
    nextQuestion () {
      this.q += 1;
      this.showQuestion(this.questions[this.q]);
      if (this.q >= this.questions.length) {
        // go to next component
        this.showQuestion(false);
        this.c += 1;
        this.q = -1;
        if (this.c >= this.components.length) {
          this.showExtra();
        } else {
          // this.questions = this.questionsByComponents[this.components[this.c]];
          this.askAboutComponent(this.components[this.c]);
        }
      } else {
        this.showQuestion(this.questions[this.q], this.components[this.c])
      }
    },
    showQuestion (question, component) {
      if (question) {
        this.questionBox.question = question;
        this.questionBox.component = component;
        this.questionBox.show = true
      } else {
        this.questionBox.show = false;
        this.questionBox.question = null;
        this.questionBox.component = null
      }
    },
    askAboutComponent (component) {
      if (component) {
        this.componentBox.component = component;
        this.componentBox.show = true
      } else {
        this.componentBox.show = false;
        this.componentBox.component = null
      }
    },
    skipComponent () {
      this.c += 1;
      if (this.c >= this.components.length) {
        this.askAboutComponent(false);
        this.showEndOfTest(true);
      }
      this.askAboutComponent(this.components[this.c])
    },
    showExtra (boolean) {
      if (boolean === true || boolean === undefined)
        this.extraBox.show = true;
      else
        this.extraBox.show = false
    },
    showEndOfTest (boolean) { // work both as showEndOfTest() and showEndOfTest(true)
      if (boolean === true || boolean === undefined)
        this.endBox.show = true;
      else
        this.endBox.show = false
    },
    submitTest () {
      let motivation = 0;
      this.motivationBox.questions.forEach(question => {
        if (!question.negative) {
          motivation += question.answer
        } else {
          motivation += (5 - question.answer)
        }
      });
      motivation = Math.round(motivation / this.motivationBox.questions.length);
      let results = this.downloadedCourse.test;
      results.questionsByComponents = [];
      results.questions = [];
      Object.keys(this.questionsByComponents).forEach(function(component) {
        results.questions = results.questions.concat(app.questionsByComponents[component]);
        results.questionsByComponents.push({ item: {
          key: component,
          value: app.questionsByComponents[component]
        }});
      });
      results.questions.forEach(function (question) {
          if (question.type === 'radio' || question.type === 'checkbox') {
            question.answer = JSON.stringify(question.answer)
          }
      });
      console.log(results.questions);
      let student = {
        student: {
          id: this.generalInfo.studentNumber,
          name: this.generalInfo.name,
          study: this.generalInfo.study,
          nationality: this.generalInfo.nationality,
          gender: this.generalInfo.gender,
          belbin: this.generalInfo.belbin,
          motivation: motivation,
          knowledge: (this.extraKnowledge.multipleComponents ? 1 : 0) + (this.extraKnowledge.otherFields ? 1 : 0),
          email: this.generalInfo.email,
          results: results
        }
      };
      student.student.results.questionsByComponents = /*this.questionsByComponents;*/ undefined;
      student = converter.json2xml_str(student);
      console.log(student);
      axios  // TODO Check how to submit this to the backend.
        .put(apiUrl + '/controller/courses/' + this.id + '/student/result/', student, {headers: {'Content-Type': 'application/xml'}})
        .then(function (response) {
          console.log(response);
        })
        .catch(function (error) {
          console.log(error);
        });
    }
  }
});