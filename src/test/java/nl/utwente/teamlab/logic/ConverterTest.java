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

import nl.utwente.teamlab.model.Group;
import nl.utwente.teamlab.model.Student;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ConverterTest {

    private Converter converter = new Converter();

    @Test
    public void groupsToCSV(){
        Student s1 = new Student();
        s1.setId(1);
        s1.setName("s1");
        Student s2 = new Student();
        s2.setId(2);
        s2.setName("s2");
        ArrayList<Student> sts = new ArrayList<>(Arrays.asList(s1,s2));
        Group g1 = new Group();
        g1.setStudents(sts);
        g1.setName(1);
        Group g2 = new Group();
        g2.setStudents(sts);
        g2.setName(2);
        ArrayList<Group> groups = new ArrayList<>(Arrays.asList(g1,g2));
        String csv = "group,grade,student name,student number\n" +
                "1,0,s1,1\n" +
                "1,0,s2,2\n" +
                "2,0,s1,1\n" +
                "2,0,s2,2\n";
        assertEquals(csv, converter.groupsToCSV(groups));
    }

}