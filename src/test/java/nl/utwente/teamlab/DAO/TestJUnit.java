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

import nl.utwente.teamlab.model.Course;
import nl.utwente.teamlab.model.Question;
import nl.utwente.teamlab.model.Teacher;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class TestJUnit {
    static DB db = DB.getInstance();
    static nl.utwente.teamlab.model.Test test = new nl.utwente.teamlab.model.Test();
    static int id;
    static Teacher teacher = new Teacher("test@course");
    Course course = new Course();
    static Question qNew = new Question( "text",new ArrayList<String>(Arrays.asList("a","b","c","d","", "")), "1","c1");

    @BeforeClass
    public static void prepare() throws SQLException {
        test.setTeacher(teacher);
        id = db.createTest(test);
        test.setId(id);
    }

    @Test
    public void setName() throws SQLException {
        String name = "Test name";
        db.addNameToTest(id,name);
        nl.utwente.teamlab.model.Test result = db.getTest(id,true);
        assertEquals(name,result.getName());
        db.addNameToTest(id,"");
    }

    @Test
    public void getTest () throws SQLException {
        nl.utwente.teamlab.model.Test result = db.getTest(id,true);
        assertEquals(test,result);
    }

    @Test
    public void getTestForStudents () throws SQLException {
        db.addQuestionToTest(id,qNew);
        nl.utwente.teamlab.model.Test result = db.getTest(id,false);
        qNew.setAnswer("");
        test.setQuestions(new ArrayList<>(Arrays.asList(qNew)));
        test.createQuestionsMap();
        test.setQuestions(new ArrayList<>());
        assertEquals(test, result);

    }

    @Test
    public void getTests() throws SQLException {
        ArrayList<nl.utwente.teamlab.model.Test> result = new ArrayList<>(Arrays.asList(db.getTests(teacher).get(0)));
        test.setTeacher(new Teacher());
        ArrayList<nl.utwente.teamlab.model.Test> tests = new ArrayList<>(Arrays.asList(test));
        assertEquals(tests,result);
        test.setTeacher(teacher);
    }
}
