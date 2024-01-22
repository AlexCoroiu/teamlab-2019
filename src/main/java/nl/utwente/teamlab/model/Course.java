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

package nl.utwente.teamlab.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

@XmlRootElement
public class Course {
    private int id;
    private String name = "";
    private Teacher owner = new Teacher();
    private Test test = new Test();
    private String studiesString = "";
    private String rules = "";
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<String> studies = new ArrayList<>();
    private ArrayList<Chart> charts = new ArrayList<>();
    private String groupsCSV = "";
    private boolean status = false;

    public String getGroupsCSV() {
        return groupsCSV;
    }

    public void setGroupsCSV(String groupsCSV) {
        this.groupsCSV = groupsCSV;
    }

    public Course() {

    }

    public Course(int id, String name, Teacher teacher, Test test, String studies) {
        this.id = id;
        this.name = name;
        this.owner = teacher;
        this.test = test;
        this.studiesString = studies;
    }

    public Course(int id, String name, Teacher owner, Test test) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.test = test;
    }

    public Course(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Course(int id, String name, Teacher teacher, Test test, String studies, String rules, boolean status) {
        this.id = id;
        this.name = name;
        this.owner = teacher;
        this.test = test;
        this.studiesString = studies;
        this.rules = rules;
        this.status = status;
    }

    public ArrayList<Chart> getCharts() {
        return charts;
    }

    public void setCharts(ArrayList<Chart> charts) {
        this.charts = charts;
    }

    public String getStudiesString() {
        return studiesString;
    }

    public void setStudiesString(String studiesString) {
        this.studiesString = studiesString;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public ArrayList<String> getStudies() {
        return studies;
    }

    public void setStudies(ArrayList<String> studies) {
        this.studies = studies;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teacher getOwner() {
        return owner;
    }

    public void setOwner(Teacher owner) {
        this.owner = owner;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public String getRules() {
        return rules;
    }

    public void studiesToString(){
        if(studiesString.equals("")) {
            for (String s : studies) {
                studiesString = studiesString + s + ",";
            }
            if (!studiesString.equals("")) {
                studiesString = studiesString.substring(0, studiesString.length() - 1);
            }
        }
    }

    public void studiesToArray(){
        if(!studiesString.equals("")) {
            studies = new ArrayList<String>(Arrays.asList(studiesString.split(",")));
        }
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", owner=" + owner +
                ", test=" + test +
                ", studiesString='" + studiesString + '\'' +
                ", rules='" + rules + '\'' +
                ", students=" + students +
                ", studies=" + studies +
                ", charts=" + charts +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return id == course.id &&
                Objects.equals(name, course.name) &&
                Objects.equals(owner, course.owner) &&
                Objects.equals(test, course.test) &&
                Objects.equals(studiesString, course.studiesString) &&
                Objects.equals(rules, course.rules) &&
                Objects.equals(students, course.students) &&
                Objects.equals(studies, course.studies) &&
                Objects.equals(charts, course.charts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, owner, test, studiesString, rules, students, studies, charts);
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
