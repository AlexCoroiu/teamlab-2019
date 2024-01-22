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
public class Question {
    private int id;
    private String text = "";
    private ArrayList<String> choices = new ArrayList<>(Arrays.asList("","","","","",""));
    private String answer = "";
    private Test test = new Test();
    private String component = "";
    private int score;
    private ArrayList<String> tags = new ArrayList<>();
    private String type = "";

    public Question() {}

    public Question(int id, String text, ArrayList<String> choices, String answer) {
        this.id = id;
        this.text = text;
        this.choices = choices;
        this.answer = answer;
    }

    public Question(int id, String text, ArrayList<String> choices, String a, String component, String type, int score) {
        this.id = id;
        this.text = text;
        this.choices = choices;
        this.answer = a;
        this.component = component;
        this.type = type;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public Question(int id, String text, ArrayList<String> choices, String answer, String component) {
        this.id = id;
        this.text = text;
        this.choices = choices;
        this.answer = answer;
        this.component = component;
    }

    public Question(String text, ArrayList<String> choices, String answer, String component) {
        this.text = text;
        this.choices = choices;
        this.answer = answer;
        this.component = component;
    }

    public String getComponent() {
        return component;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", choices=" + choices +
                ", answer='" + answer + '\'' +
                ", test=" + test +
                ", component='" + component + '\'' +
                ", score=" + score +
                ", tags=" + tags +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return id == question.id &&
                score == question.score &&
                Objects.equals(text, question.text) &&
                Objects.equals(choices, question.choices) &&
                Objects.equals(answer, question.answer) &&
                Objects.equals(test, question.test) &&
                Objects.equals(component, question.component) &&
                Objects.equals(tags, question.tags) &&
                Objects.equals(type, question.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, choices, answer, test, component, score, tags, type);
    }
}
