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
import java.util.HashMap;
import java.util.Objects;

@XmlRootElement
public class  Test {

    private int id;
    private String name = "";
    private ArrayList<Course> courses = new ArrayList<>();
    private Teacher teacher = new Teacher();
    private HashMap<String,ArrayList<Question>> questionsByComponents = new HashMap<>();
    private ArrayList<Question> questions = new ArrayList<>();

    public Test () {}

    public Test (int id, String name){
        this.id = id;
        this.name = name;
    }
    public Test(String name, Teacher teacher, HashMap<String, ArrayList<Question>> questions) {
        this.name = name;
        this.teacher = teacher;
        this.questionsByComponents = questions;
    }

    public Test(int id, String name, Teacher teacher){
        this.id =id;
        this.name = name;
        this.teacher = teacher;
    }

    public Test(int id, String name, Teacher teacher, HashMap<String, ArrayList<Question>> questions) {
        this.id = id;
        this.name = name;
        this.teacher = teacher;
        this.questionsByComponents = questions;
    }

    public Test(int id){
        this.id = id;
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

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public HashMap<String, ArrayList<Question>> getQuestionsByComponents() {
        return questionsByComponents;
    }

    public void setQuestionsByComponents(HashMap<String, ArrayList<Question>> questionsByComponents) {
        this.questionsByComponents = questionsByComponents;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public void createQuestionsList(){
        if(questions.size() == 0) {
            for (ArrayList<Question> i : questionsByComponents.values()) {
                questions.addAll(i);
            }
        }
    }

    public void createQuestionsMap(){
        if(questionsByComponents.size() == 0){
            for(Question q: questions){
                String component = q.getComponent();
                if (questionsByComponents.keySet().contains(component)){
                    questionsByComponents.get(component).add(q);
                }   else {
                    ArrayList<Question> qs = new ArrayList<Question>(Arrays.asList(q));
                    questionsByComponents.put(component,qs);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", courses=" + courses +
                ", teacher=" + teacher +
                ", questionsByComponents=" + questionsByComponents +
                ", questions=" + questions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Test test = (Test) o;
        return id == test.id &&
                Objects.equals(name, test.name) &&
                Objects.equals(courses, test.courses) &&
                Objects.equals(teacher, test.teacher) &&
                Objects.equals(questionsByComponents, test.questionsByComponents) &&
                Objects.equals(questions, test.questions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, courses, teacher, questionsByComponents, questions);
    }
}
