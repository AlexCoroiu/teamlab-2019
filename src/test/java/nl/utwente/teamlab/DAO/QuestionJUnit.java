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

package nl.utwente.teamlab.DAO;

import nl.utwente.teamlab.model.Question;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class QuestionJUnit {
    static DB db = DB.getInstance();
    static nl.utwente.teamlab.model.Test test = new nl.utwente.teamlab.model.Test();
    static Question question = new Question( "text",new ArrayList<String>(Arrays.asList("a","b","c","d","", "")), "1","c1");
    static int id;
    static int qid;

    @BeforeClass
    public static void prepare() throws SQLException {
        test.setName("test questions");
        id = db.createTest(test);
        qid = db.addQuestionToTest(id,question);
        question.setId(qid);
    }

    @Test
    public void addQuestion() throws SQLException {
        HashMap<String,ArrayList<Question>> questions = db.getQuestions(id,true);
        test.setQuestionsByComponents(questions);
        test.createQuestionsList();
        Question result = test.getQuestions().get(0);
        assertEquals(question,result);
    }

    @Test
    public void deleteQuestion() throws SQLException {
        db.deleteQuestion(question.getId(),id);
        HashMap<String,ArrayList<Question>> result = db.getQuestions(id,true);
        assertEquals(new HashMap<String,ArrayList<Question>>(), result);
    }

}
