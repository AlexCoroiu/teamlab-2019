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
import java.util.Objects;

//TODO create useful constructors for all model classes
@XmlRootElement
public class Student {
    private int id;
    private String email = "";
    private String name = "";
    private String gender = ""; //M,F,NB
    private String nationality = ""; //...
    private String belbin = ""; //T,A,P
    private int motivation;
    private int knowledge;
    private String study = "";
    private Test results = new Test();
    private ArrayList<Group> groups = new ArrayList<>();
    private ArrayList<Course> courses = new ArrayList<>();

    public Student() {
    }

    public Student(int id, String email, String name, String gender, String nationality, String belbin, int motivation, int knowledge, String study) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.nationality = nationality;
        this.belbin = belbin;
        this.motivation = motivation;
        this.knowledge = knowledge;
        this.study = study;
    }

    //workaround
    public Student(String email) {
        this.email = email;
    }

    public Test getResults() {
        return results;
    }

    public void setResults(Test results) {
        this.results = results;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudy() {
        return study;
    }

    public void setStudy(String study) {
        this.study = study;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getMotivation() {
        return motivation;
    }

    public void setMotivation(int motivation) {
        this.motivation = motivation;
    }

    public int getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(int knowledge) {
        this.knowledge = knowledge;
    }

    public String getBelbin() {
        return belbin;
    }

    public void setBelbin(String belbin) {
        this.belbin = belbin;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", nationality='" + nationality + '\'' +
                ", belbin='" + belbin + '\'' +
                ", motivation=" + motivation +
                ", knowledge=" + knowledge +
                ", study='" + study + '\'' +
                ", results=" + results +
                ", groups=" + groups +
                ", courses=" + courses +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id &&
                motivation == student.motivation &&
                knowledge == student.knowledge &&
                Objects.equals(email, student.email) &&
                Objects.equals(name, student.name) &&
                Objects.equals(gender, student.gender) &&
                Objects.equals(nationality, student.nationality) &&
                Objects.equals(belbin, student.belbin) &&
                Objects.equals(study, student.study) &&
                Objects.equals(results, student.results) &&
                Objects.equals(groups, student.groups) &&
                Objects.equals(courses, student.courses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, name, gender, nationality, belbin, motivation, knowledge, study, results, groups, courses);
    }
}
