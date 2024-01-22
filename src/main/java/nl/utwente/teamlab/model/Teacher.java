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

@XmlRootElement
public class Teacher {
    private String mail = "";
    private String name = "";
    private ArrayList<Course> courses = new ArrayList<>();
    private ArrayList<Test> tests = new ArrayList<>();

    public Teacher() {}

    public Teacher(String mail, String name, ArrayList<Course> courses, ArrayList<Test> tests) {
        this.mail = mail;
        this.name = name;
        this.courses = courses;
        this.tests = tests;
    }

    public Teacher(String mail){
        this.mail = mail;
    }

    public Teacher(String mail, String name) {
        this.mail = mail;
        this.name = name;
    }

    public ArrayList<Test> getTests() {
        return tests;
    }

    public void setTests(ArrayList<Test> tests) {
        this.tests = tests;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "mail='" + mail + '\'' +
                ", name='" + name + '\'' +
                ", courses=" + courses +
                ", tests=" + tests +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(mail, teacher.mail) &&
                Objects.equals(name, teacher.name) &&
                Objects.equals(courses, teacher.courses) &&
                Objects.equals(tests, teacher.tests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mail, name, courses, tests);
    }
}
