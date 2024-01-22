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

package nl.utwente.teamlab.logic;

import nl.utwente.teamlab.model.Question;
import nl.utwente.teamlab.model.Student;
import nl.utwente.teamlab.model.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class KnowledgeMasterMind {

    //SOLO taxonomy calculation
    public static int scoreStudent(Student student, Test test) {
        Test result = student.getResults();
        HashMap<String, ArrayList<Question>> answers = test.getQuestionsByComponents();
        result.createQuestionsMap();
        HashMap<String, ArrayList<Question>> results = result.getQuestionsByComponents();
        int knowledge = student.getKnowledge(); //CAN ALREADY BE SET TO 1 or 2 for the high levels of SOLO
        int nrcomp = answers.size();
        int components = 0;
        for(String c: answers.keySet()){
            ArrayList<Question> rs = results.get(c);
            ArrayList<Question> qs = answers.get(c);
            int s = qs.size();
            int componentScore = 0;
            //checkString all questions for this component
            for(int i =0; i<s;i++){
                //compare student answer with actual answer
                if ((qs.get(i).getAnswer()).equals(rs.get(i).getAnswer())) {
                    componentScore++;
                }
            }
            if (componentScore >= s/2){ //if more than half of the questions for this component where answered correctly
                components ++;
            }
        }

        if(components !=0) {
            //if the students knows more than half of the components
            if (components >= nrcomp / 2) {
                knowledge = knowledge + 2;
            } else { //if the students knows just few components
                knowledge = 1;
            }
        } else { //the student doesnt know any component
            if (knowledge > 0){ //the student says the have worked with the component??
                knowledge = knowledge - 1;
            } //TODO not sure what to do when student says theyve doen stuff but dont actually answer the questions properly
            else {
                knowledge = components; //=0
            }
        }
        return knowledge;
    }
}
