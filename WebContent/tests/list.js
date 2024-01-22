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

var converter = new X2JS();
var app = new Vue({
  el: "#app",
    data () {
      return {
          tests:null,
          loadingFailed: false
      }
  },
    mounted(){
      axios
          .get(apiUrl + '/controller/tests')
          .then(response => {
              let xml = (response.data);
              console.log(xml);
              let json = converter.xml_str2json(xml);
              console.log(json);
              //this.tests = JSON.parse(json)
              this.tests = normalizeArrayDeserialization(json.tests.test)
              }
          )
    },

    methods: {
        gotoId(url, id) {
            saveId(id);
            saveProcedure('view');
            gotoPage(url)
        },
        gotoNew(url) {
            saveProcedure('add');
            gotoPage(url)
        }
    }
}); //TODO error cases

// mounted() { //lamda
  //   loadingFailed: false,
  //   tests: () => {  // get them from server through AJAX    REZULATATUL E NUMELE FUNCTIEI
  //       let request = new XMLHttpRequest();
  //       request.open('GET', apiUrl + '/controller/tests', true);
  //
  //       request.onload = function() {
  //           if (this.status >= 200 && this.status < 400) {
  //               // Success!
  //               console.log("Received response:");
  //               console.log(this.response);
  //               this.tests = JSON.toObject(XMLibrary.toJSON(response)); //?
  //               console.log("Loaded tests list:");
  //               alert("tests loaded");
  //               console.log(this.tests);
  //               return this.tests // TODO probably doesn't work
  //           } else {
  //               // We reached our target server, but it returned an error
  //               console.log("Failed with status " + this.status);
  //               // console.log(this);
  //               var errWindow = window.open("", "Error", "width=900,height=800");
  //               errWindow.document.write(this.response);
  //               app.loadingFailed = true;
  //           }
  //       };
  //
  //       request.onerror = function() {
  //           alert("Could not reach server.");
  //           app.loadingFailed = true;
  //       };
  //
  //       request.send();
  //   }
  // })
