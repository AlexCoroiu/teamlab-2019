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
import org.junit.BeforeClass;
import org.junit.Test;
import nl.utwente.teamlab.model.Student;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class KnowledgeMasterMindTest {
    private KnowledgeMasterMind master = new KnowledgeMasterMind();
    static Student s = new Student();
    static nl.utwente.teamlab.model.Test test = new nl.utwente.teamlab.model.Test();
    static nl.utwente.teamlab.model.Test result = new nl.utwente.teamlab.model.Test();
    static HashMap<String, ArrayList<Question>> questions = new HashMap<>();
    static HashMap<String, ArrayList<Question>> qrm = new HashMap<>();
    static Question q1 = new Question();
    static Question q2 = new Question();
    static Question q3 = new Question();
    static Question qr1 = new Question();
    static Question qr2 = new Question();
    static Question qr3 = new Question();
    static String c1 = "c1";
    static String c2 = "c2";

    @BeforeClass
    public static void prepare(){
        q1.setAnswer("1");
        q2.setAnswer("1");
        q3.setAnswer("1");

        ArrayList<Question> qa = new ArrayList<>();
        qa.add(q1);
        questions.put(c1,qa);

        ArrayList<Question> qa2 = new ArrayList<>();
        qa2.add(q2);
        qa2.add(q3);
        questions.put(c2,qa2);

        test.setQuestionsByComponents(questions);

        qr1.setAnswer("2");
        qr2.setAnswer("1");
        qr3.setAnswer("2");

        ArrayList<Question> qr = new ArrayList<>();
        qr.add(qr1);
        qrm.put(c1,qr);

        ArrayList<Question> qrl2 = new ArrayList<>();
        qrl2.add(qr2);
        qrl2.add(qr3);
        qrm.put(c2,qrl2);

        result.setQuestionsByComponents(qrm);
        s.setResults(result);

    }
    @Test
    public void scoreStudent() {
        assertEquals(2, master.scoreStudent(s,test));
        s.setKnowledge(2);
        assertEquals(4, master.scoreStudent(s,test));
        s.setKnowledge(1);
        assertEquals(3, master.scoreStudent(s,test));
    }
}
