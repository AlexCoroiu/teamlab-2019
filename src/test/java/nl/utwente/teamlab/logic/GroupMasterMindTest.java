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

import nl.utwente.teamlab.model.Course;
import nl.utwente.teamlab.model.Group;
import nl.utwente.teamlab.model.Student;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertNotEquals;

public class GroupMasterMindTest {
    private static GroupMasterMind master = new GroupMasterMind();
    private static Course course = new Course();

    @BeforeClass
    public static void prepare(){
        String rules = "group_size : 2-\n" +
                "- balance: knowledge";
        ArrayList<Student> students = new ArrayList<Student>();

        Student s = new Student();
        s.setId(1);
        s.setKnowledge(3);
        students.add(s);

        Student s2 = new Student();
        s2.setId(2);
        s2.setKnowledge(3);
        students.add(s2);

        Student s3 = new Student();
        s3.setId(3);
        s3.setKnowledge(1);
        students.add(s3);

        course.setStudents(students);
        course.setRules(rules);
    }

    @Test
    public void createGroups() throws IOException, InterruptedException {
        ArrayList<Group> groups = master.createGroups(course);
        assertNotEquals(0,groups.size());
        course.setId(1);
        //second course
        groups = master.createGroups(course);
        assertNotEquals(0,groups.size());
    }

}
