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

Vue.component ('groupmaking', {
  props: ['courseId', 'expanded', 'choices'],

  data: function () {
    return {
      groupSize: '3',
      sizeAdjustment: 'less',
      operations: ['distribute', 'aggregate', 'cluster', 'balance'],
      attributes: [
        'belbin',
        'study',
        'nationality',
        'gender',
        'motivation',
        'knowledge'
      ],
      searchChoice: null,

      rules: [{operation: 'cluster', attribute: 'gender', values: ['F']}]  // example
    }
  },

  computed: {
    /*values: function () {
      alert("\n\n\nKILLLL MEEE\n\n\n");
      if (typeof this.choices !== 'undefined') {
        let result = [
          this.choices['Belbin role'],
          this.choices['Study'],
          this.choices['Nationality'],
          this.choices['Gender'],
          this.choices['Motivation'],
          this.choices['Knowledge'],
        ];
        console.log('\n\n\n\ngroupmaking.values is', result, '\n\n\n');
        return result;
      } else {
        console.log('\n\n\n\ngroupmaking.values is', [null, null, null, null, null, null], '\n\n\n');
        return [null, null, null, null, null, null]
      }
    }*/
    normalizedChoices: function () {  // FIXME should really be using this instead of normalizedChoices2, but it doesn't update for some reason
      let normalized = {};
      Object.entries(this.choices).forEach(([val, key]) => {
        normalized[key.toString().toLowerCase().split(' ')[0]] = val
      });
      return normalized;
    }
  },

  methods: {
    normalizedChoices2 () {
      let normalized = {};
      Object.entries(this.choices).forEach(([key, val]) => {
        normalized[key.toString().toLowerCase().split(' ')[0]] = val
      });
      return normalized;
    },

    newRule () {
      this.rules.push({operation: '', attribute: '', values: ''});
      console.log(this.choices);
    },

    makeGrouping () {
      console.log(this.rules);
      let rules = '';
      rules += 'group_size : ' + this.groupSize
            + (this.sizeAdjustment === 'more' ? '+' : (this.sizeAdjustment === 'less' ? '-' : ''))
            + '\n';
      this.rules.forEach(function (rule) {
        rules += (''
          + '\n - '
          + rule.operation
          + ' : '
          + rule.attribute);
        if (rule.operation === 'distribute') {
          rules += (''
            + '\n'
            + '   values : '
          );
          rule.values.forEach(function (value) {
            rules += value + ', '
          });
          rules = rules.slice(0, -2);
        } else if (rule.operation === 'cluster') {
          rules += (''
            + '\n'
            + '   values : '
          );
          rule.values.forEach(function (value) {
            rules += value + ' = '
          });
          rules = rules.slice(0, -2);
        }
        rules += '\n'
      });
      console.log(rules);
      console.log();
      console.log();
      let course = {
        course: {
          id: courseId,
          rules: rules
        }
      };
      let xml = x2js.json2xml_str(course).replace(new RegExp('\n', 'g'), '&#xA;');
      console.log(xml);
      axios
        .post(apiUrl + '/controller/courses/' + courseId + '/groups', xml, {headers: {'Content-Type': 'application/xml'}})
        .then(response => { if (debug) console.log(response); else window.location.reload() })
        .catch(error => console.log(error))
    }
  },

  template: `
<v-container fluid>
    <v-layout row wrap v-show="expanded">
      <v-flex xs12><span class="title">Rules</span></v-flex>
      <v-layout row wrap v-for="(rule, index) in rules" :key="index"> <!-- FIXME layout is borked when values checkbox doesn't exist -->
        <v-flex xs12 sm12 mx-2>
          <v-layout row wrap>
            <v-flex xs6 sm4>
              <v-select
                :items="operations"
                label="operation"
                v-model="rule.operation"
              ></v-select>
            </v-flex>
            <v-flex xs6 sm4>
              <v-select
                :items="attributes"
                label="attribute"
                v-model="rule.attribute"
              ></v-select>
            </v-flex>
            <v-flex xs12 sm4>
              <v-spacer></v-spacer>
              <v-autocomplete v-show="rule.operation !== 'balance' && rule.operation !== 'aggregate'"
                  v-model="rule.values"
                  :items="normalizedChoices2()[rule.attribute]"
                  :search-input.sync="searchChoice"
                  hide-selected
                  :hint="'people to ' + rule.operation + (rule.operation === 'cluster' ?  ' together' : ' among groups')"
                  label="values"
                  multiple
                  small-chips
                  clearable
                  deletable-chips
                  persistent-hint
                  >
                  </v-autocomplete>
              </v-autocomplete>
              <!--<v-checkbox v-for="choice in choices['Gender']" :key="choice"
                v-model="rule.values"
                :label="choice"
                :value="choice"
                multiple
              >
              
              </v-checkbox>-->
              <!--<v-text-field
                label="values"
                flat
                v-model="rule.values"
              ></v-text-field>-->
            </v-flex>
          </v-layout>
        </v-flex>
      </v-layout>
      <v-flex xs12>
        <v-btn color="secondary" flat v-on:click="newRule()">
          <v-icon left>add</v-icon>
          <span>New rule</span>
        </v-btn>
      </v-flex>
      <v-flex xs12 style="max-width: 4cm" ml-2>
        <v-text-field 
          label="Group size"
          suffix="people"
          v-model="groupSize"
          ></v-text-field>
      </v-flex>
      <v-flex xs12 sm12 ml-2>
        <v-layout row wrap align-center justify-start>
          <v-flex shrink><span class="body-1">If necessary, I want some groups to have one person </span></v-flex>
          <v-flex shrink>
            <v-select v-model="sizeAdjustment"
                      :items="['more', 'less']"
                      class="body-1"
                      style='max-width: 6ch'
            />
          </v-flex>
          <v-flex shrink><span class="body-1"> than the rest.</span></v-flex>
        </v-layout>
      </v-flex>
      <!--<v-flex xs12>
        {{ choices }}
        <br/><br/>
        {{ normalizedChoices }}
        <br/><br/>
        {{ normalizedChoices2() }}
        <br/><br/>
        {{ rules }}
      </v-flex>-->
      <v-flex xs12>
        <v-btn color="primary" v-on:click="makeGrouping()" style="border-radius: 3pt">
          <v-icon left>send</v-icon>
          <span>Make grouping</span>
        </v-btn>
      </v-flex>
    </v-layout>
    </v-container>
  `,
});