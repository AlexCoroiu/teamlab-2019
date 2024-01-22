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

courseId = sessionStorage.getItem('id');
procedure = sessionStorage.getItem('procedure');
let x2js = new X2JS();

let debug = false;

var app = new Vue ({
  el: '#app',
  data: {
      assistant:{
          courseText: 'This is your course! :)\n',
          courseTextFields: 'Do not forget to prepare the fields of this course\n',
          courseTextNoStudents: 'You can share the test of this course with students\n',
          overviewText: 'Check out the demographics of your course!\n',
          studentsText: 'Check out your students!\n',
          groupsText: 'Check out your groups!\n',
          groupsTextNoGrouping: 'You can create grouping rules to group up your students based on four operations:\n\n' +
              'DISTRIBUTE - makes sure a variable is equally spread across groups (e.g. distribute belbin P)\n\n' +
              'CLUSTER - useful to make sure minorities are not isolated (e.g. cluster gender F)\n\n' +
              'AGGREGATE - good when you want to have a homogeneous group (e.g. aggregate study retake)\n\n' +
              'BALANCE - makes sure groups are balanced on a variable (e.g. balance knowledge)\n\n' +
              'Check out the distribution of your students to create appropriate grouping rules!'
      },
    doneLoading: false,
    courseId: parseInt(courseId),
    courseName: 'Untitled course',
    status: false,
    tests: [],
    selectedTest: '',
    selectedStudies: [],
    searchStudy: '',
    students: [],
    groups: [],
    editMode: procedure === 'add' || procedure === 'continue',
    procedure: procedure,
    selectedTab: null,
    dC: null,
    charts:[],
    shareLinkBox: false,
    shareLinkAlert: false,
    expandRules: false,
    validation: { // lists of functions for Vuetify's input validation
      valid: false,
      name: [
        name => !!name || "Can't be empty.",
        name => !!name.replace(/\s/g, '').length || "Can't be just spaces."
      ]
    },
    studentsFilter: null,
    loadingFailed: null,
    groupingValues: {},
    groupPerformanceDialog: false
  },

  mounted () {
    this.$vuetify.theme.primary = '#3F51B5';

    if (procedure !== 'add') {
      console.log("Getting course from " + apiUrl + '/controller/courses/' + courseId);
      axios
        .get(apiUrl + '/controller/courses/' + courseId)
        .then(response => {
          console.log("response: ", response);
          let course = x2js.xml_str2json(response.data).course;
          console.log(course);
          app.dC = course;
          app.courseName = course.name;
          app.status = course.status === 'true';
          if (procedure !== 'continue')
            app.selectedTest = course.test;
          app.selectedStudies = course.studies;
          if (app.selectedStudies && !Array.isArray(app.selectedStudies)) app.selectedStudies = [app.selectedStudies]
          // app.students = response.data.students;
          // this.groups = response.course.groups;
          // console.log(response);
        })
        .catch(error => console.log(error));

      if (procedure === 'continue') {
        this.selectedTest = sessionStorage.getItem('newTestId');
        console.log("continuing, test id is", this.selectedTest);
      }

      sessionStorage.removeItem('newTestId');

      console.log("Getting results from " + apiUrl + '/controller/courses/' + courseId + '/results');
      axios
        .get(apiUrl + '/controller/courses/' + courseId + '/results')
        .then(response => {
          console.log(response.data);
          let students = x2js.xml_str2json(response.data).students.student;
          console.log(students);
          if(Array.isArray(students))
            app.students = students;
          else if (students)
            app.students= [students]
        })
        .catch(error => { console.log(error); app.loadingFailed = true });

      console.log('Getting groups from ' + apiUrl + '/controller/courses/' + courseId + '/groups');
      axios
        .get(apiUrl + '/controller/courses/' + courseId + '/groups')
        .then(function(response) {
          console.log(response.data);
          let groups = x2js.xml_str2json(response.data).groups.group;
          groups = normalizeArrayDeserialization(groups);
          console.log(groups);
          if (groups) groups.forEach(group => {
            group.addMenu = false;
            // if (typeof group.performance === 'undefined') group.performance = '';
            // group.editGrade = false;
            if (!Array.isArray(group.students))
              group.students = [group.students]
          });
          app.groups = groups;
        })
        .catch(error => console.log(error));

      console.log('Getting charts from ' + apiUrl + '/controller/courses/' + courseId + '/charts');
      axios
        .get(apiUrl + '/controller/courses/' + courseId + '/charts')
        .then(function(response) {
            //list of chart setting
            chartSettings = [
                {//gender (3)
                    name:'Gender',
                    type:'doughnut',
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.2)',
                        'rgba(54, 162, 235, 0.2)',
                        'rgba(255, 206, 86, 0.2)'
                    ]
                },
                { //nat: yah so we dotn knwo the number of nationalities that will eb in there :/
                  name:'Nationality',
                  type:'bar',
                    backgroundColor: [
                    ]
                },
                { // belbin (3)
                  name:'Belbin role',
                  type: 'pie',
                    backgroundColor: [
                        'rgba(255, 99, 132, 1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(255, 206, 86, 1)'
                    ]
                },
                { //motivation (3) ?
                  name:'Motivation',
                  type: 'bar',
                    backgroundColor: [
                        'rgba(75, 192, 192, 0.2)',
                        'rgba(153, 102, 255, 0.2)',
                        'rgba(255, 159, 64, 0.2)'
                    ]
                },
                {//knowledge (5)
                    name:'Knowledge',
                    type: 'polarArea',
                    backgroundColor: [
                        'rgba(54, 162, 235, 1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(75, 192, 192, 1)',
                        'rgba(153, 102, 255, 1)',
                        'rgba(255, 159, 64, 1)'
                    ]
                },
                {// study - unknwon size
                    name:'Study',
                    type: 'bar',
                    backgroundColor: [
                    ]
                }
            ];
          console.log(response.data);
          let charts = x2js.xml_str2json(response.data).charts.chart;
          // fix them up a bit from that terrible deserialization
          charts.forEach(chart => {
            if (!Array.isArray(chart.labels)) {
              chart.labels = [chart.labels];
              chart.values = [chart.valies]
            }
          });
          console.log(charts);
          app.charts = charts;

          window.setTimeout(function() {
            if (app.students.length > 0) {
              //draw charts
              app.$forceUpdate();
              app.charts.forEach(function (chart, i) {
                console.log("Drawing chart-" + i, chart);
                console.log("labels: ", chart.labels);
                console.log("values: ", chart.values);
                let target = document.getElementById('chart-' + i).getContext('2d');
                chart.name = chartSettings[i].name;
                chart.chart = new Chart(target, {
                  type: chartSettings[i].type,
                  data: {
                    labels: chart.labels,
                    datasets: [{
                      label: 'number',
                      data: chart.values,
                      backgroundColor: chartSettings[i].backgroundColor
                    }]
                  }
                })
              });
              app.charts.forEach(chart => {
                app.groupingValues[chart.name] = chart.labels
              })
            }
          },240); // TODO should just wait for document to be completely loaded
        })
        .catch(error => console.log(error));
    } else {
      let config = {
        headers: {'Content-Type': 'application/xml'}
      };
      let course = x2js.json2xml_str({
        course: {
          id: 0,
          name: this.courseName,
          test: null,
          students: null,
          studies: null
        }
      });
        console.log(course);
        console.log("POSTing initial to " + apiUrl + '/controller/courses/new');
        axios
          .post(apiUrl + '/controller/courses/new', course, config)
          .then(function (response) {
            console.log(response);
            // TODO load course ID from response
            let newCourseId = x2js.xml_str2json(response.data).course.id;
            console.log("\ncourse id is: ", newCourseId);
            app.courseId = parseInt(newCourseId);
            courseId = app.courseId;
          })
          .catch(function (error) {
            console.log(error, error.response);
          });
    }

    console.log("Getting tests from " + apiUrl + '/controller/tests');
    axios
      .get(apiUrl + '/controller/tests')
      .then(response => {
        console.log(response.data);
        let tests = x2js.xml_str2json(response.data).tests.test;
        console.log(tests);
        if(Array.isArray(tests))
          this.tests = tests;
        else
          this.tests = [tests];
      })
      .catch(error => console.log(error));
  },

  computed: {
    anyStudents: function () {
      return Array.isArray(this.students) && this.students.length > 0
    },

    anyGroups: function () {
      return Array.isArray(this.groups) && this.groups.length > 0
    },

    groupedStudentIds: function () {
      let ids = [];
      app.groups.forEach(
        group => group.students.forEach(
          student => ids.push(student.id)
        )
      );
      return ids;
    },

    ungroupedStudents: function () {
      let uS = [];
      app.students.forEach(function (student) {
        if (!app.groupedStudentIds.includes(student.id))
          uS.push(student);
        /*let ungrouped = true;
        app.groups.forEach(function (group) {
          let inGroup = group.students.filter(groupedStudent => groupedStudent.id === student.id);
          if (inGroup) {
            ungrouped = false;
            console.log(student.name + " (" + student.id + ") is in group " + group.name);
            // return ungrouped;
            console.log(ungrouped);
          }
        });
        console.log(ungrouped);
        if (ungrouped) uS.push(student);*/
      });
      return uS;
    },

    shareLink: function () {  // FIXME this may be incorrect? certainly sharing a link to "localhost" wouldn't work
      if (window.location.href .endsWith("/new")) {
        return window.location.href.replace("/courses/new", "/courses/student/" + this.courseId)
      } else {
        return window.location.href.replace("/courses/id", "/courses/student/" + this.courseId)
      }
    },

    filteredStudents: function () {
      if (this.studentsFilter !== null && Array.isArray(this.students) && this.students.length > 0) {
        console.log(this.students);
        return this.students.filter(student => { return student.name.toLowerCase().includes(this.studentsFilter.toLowerCase()) || student.id.toLowerCase().includes(this.studentsFilter.toLowerCase())});
      }
      else
        return this.students
    },

    /*groupingValues: function () {
      let result = {};
      if (this.charts) {
        this.charts.forEach(chart => {
          result[chart.name] = chart.labels
        })
      }
      return result
    }*/
  },

  watch: {
    selectedTest: function (newTestId) {
      if (/*!app.editMode && */app.tests.some(test => test.id === newTestId)) {
        console.log("Setting new test to ", newTestId);
        app.setTest(newTestId);
      }
    },

    shareLinkBox: function (open) {
      if (open === false) {
        this.shareLinkAlert = false;
      }
    },

    status: function (newStatus) {
      let requestBody = this.status.toString();
      let config = { headers: {'Content-Type': 'text/plain'} };
      axios
        .put(apiUrl + '/controller/courses/' + this.courseId + '/status', requestBody, config)
        .then(reply => console.log(reply))
        .catch(error => console.log(error, error.response))
    }
  },

  methods: {
    setName () {
      axios
        .put(apiUrl + '/controller/courses/' + app.courseId + '/name', app.courseName, {headers: {'Content-Type': 'text/plain'}})
        .then(response => { console.log(response); app.editMode = false})
        .catch(error => console.log(error));
    },

    goCreateNewTest() {
      sessionStorage.setItem("returnTo", JSON.stringify({location: window.location.href.replace("/new", "/id"), id: app.courseId}));
      sessionStorage.setItem("procedure", "add");
      if (window.location.href.endsWith("/courses/id"))
        window.location.href = window.location.href.replace("/courses/id", "/tests/new");
      else if (window.location.href.endsWith("/courses/new"))
        window.location.href = window.location.href.replace("/courses/new", "/tests/new");
    },

    addToGroup (student, groupNo) {
      if(!groupNo)
        groupNo = prompt("Specify group number:");
      if (!groupNo || groupNo === "") { alert('okay, be like that'); return; }
      let receivingGroup = app.groups.find(group => group.id === groupNo);
      receivingGroup.students.push(student);
      axios
        .put(apiUrl + '/controller/courses/' + app.courseId + '/groups/' + receivingGroup.id + '/students/' + student.id)
        .then(response => console.log(response))
        .catch(error => console.log(error));
    },

    removeFromGroup (group, student) {
      group.students = group.students.filter(gStudent => gStudent !== student);
      axios
        .delete(apiUrl + '/controller/courses/' + app.courseId + '/groups/' + group.id + '/students/' + student.id)
        .then(response => console.log(response))
        .catch(error => console.log(error));
    },

    setTest (testId) {
      // let course = x2js.json2xml_str({course: {id: app.courseId, test: {id: testId}}});
      let test = x2js.json2xml_str({test: {id: testId}});
      console.log("Setting test for this course (id=" + app.courseId + ") to test with id=" + testId + " which is", test);
      axios
        .put(apiUrl + '/controller/courses/' + app.courseId + '/test', test, {headers: {'Content-Type': 'application/xml'}})
        .then(response => console.log(response))
        .catch(error => console.log(error.response));
    },

    setStudies () {
      if (!app.editMode || true) {
        console.log("studies updating to: ", this.selectedStudies);
        let xml = '';
        if (app.selectedStudies) app.selectedStudies.forEach(study => xml += study + ', ');
        xml = xml.slice(0, xml.length - 2);
        console.log("XML Studies: ", xml);
        axios
          .put(
            apiUrl + '/controller/courses/' + app.courseId + '/studies',
            xml,
            {headers: {'Content-Type': 'application/xml'}}
          )
          .then(response => console.log(response))
          .catch(error => console.log(error.response))
      }
    },

    checkValidation () {
      let valid = true;
      if (typeof app !== 'undefined')
        app.validation.name.forEach(function (rule) {if (rule(app.courseName) !== true) valid = false});
      return valid;
    },

    editGrade (group) {
      let oldGrade = group.grade;
      let newGrade = prompt('new grade: ');
      if (parseInt(newGrade)) {
        group.grade = parseInt(newGrade);
        let requestBody = group.grade.toString();
        let config = { headers: {'Content-Type': 'text/plain'} };
        axios
          .put(apiUrl + '/controller/courses/' + this.courseId + '/groups/' + group.id + '/grade', requestBody, config)
          .then(reply => console.log(reply))
          .catch(error => console.log(error, error.response))
      }
    },

    editPerformance (group) {
      let oldPerformance = group.performance;
      let newPerformance = prompt('new performance value: ');
      if (parseInt(newPerformance)) {
        group.performance = parseInt(newPerformance);
        let requestBody = group.performance.toString();
        let config = { headers: {'Content-Type': 'text/plain'} };
        axios
          .put(apiUrl + '/controller/courses/' + this.courseId + '/groups/' + group.id + '/performance', requestBody, config)
          .then(reply => console.log(reply))
          .catch(error => console.log(error, error.response))
      }
    },

    /*showGroupPerformanceDialog (boolean) {
      if (boolean === true || boolean === undefined) {
        app.groupPerformanceDialog = true;
      } else {
        app.groupPerformanceDialog = false;
        app.$refs.groupPerformanceTextarea.reset()
      }
    },

    setGroupPerformance (group) {
      let requestBody = group.performance;
      let config = { headers: {'Content-Type': 'text/plain'} };
      axios
        .put(apiUrl + '/controller/courses/' + this.courseId + '/groups/' + group.id + '/performance', requestBody, config)
        .then(reply => console.log(reply))
        .catch(error => console.log(error, error.response))
    },*/

    downloadGroupsCsv () {
      axios
        .get(apiUrl + '/controller/courses/' + app.courseId + '/groups/csv')
        .then(function (response) {
          console.log(x2js.xml_str2json(response.data).course.groupsCSV);
          let csvText = x2js.xml_str2json(response.data).course.groupsCSV;

          // got this from https://stackoverflow.com/a/18197341/
          let element = document.createElement('a');
          element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(csvText));
          element.setAttribute('download', app.courseName + ' students.csv');
          element.style.display = 'none';
          document.body.appendChild(element);
          element.click();
          document.body.removeChild(element);
        })
        .catch(error => console.log(error))
    },

    copyLinkToClipboard () {
      let textfield = document.getElementById("shareLinkField");
      textfield.select();
      document.execCommand("copy");
      this.shareLinkAlert = true;
    },

    sendCourse () {
      // check if inputs are all valid
      // here just if the name isn't empty
      let valid = this.checkValidation();
      if (!valid) {
        alert("Validation failed! Won't send.");
        return
      }

      // send to API
      console.log("Sending course:");
      let config = {
        headers: {'Content-Type': 'application/xml'}
      };
      if (procedure === 'add') {
        let course = x2js.json2xml_str({
          course: {
            id: procedure === 'add' ? 0 : app.courseId,
            name: app.courseName,
            test: app.tests.find(test => test.id === app.selectedTest),
            students: app.students,
            studies: (app.selectedStudies.length > 1) ? app.selectedStudies : {study: app.selectedStudies[0]}  //FIXME this still doesn't work
          }
        });
        console.log(course);
        console.log("POSTing it to " + apiUrl + '/controller/courses/new');
        axios
          .post(apiUrl + '/controller/courses/new', course, config)
          .then(function (response) {
            console.log(response);
            window.location.href = '../courses'
          })
          .catch(function (error) {
            console.log(error);
          });
      } else {
        let course = x2js.json2xml_str({
          course: {id: app.courseId, test: {id: app.selectedTest}}
        });
        console.log(course);
        console.log("PUTing it at " + apiUrl + '/controller/courses/' + app.courseId + "/test");
        axios
          .put(apiUrl + '/controller/courses/' + app.courseId + '/test', course, config)
          .then(function (response) {
            console.log(response);
            window.location.href = '../courses'
          })
          .catch(function (error) {
            console.log(error);
          });
      }
    }
  }
});
//
// console.log("Hayoooooooooooo");