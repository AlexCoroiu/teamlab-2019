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

import java.util.ArrayList;

public class Converter {

    public String groupsToCSV(ArrayList<Group> groups) {
        System.out.println(groups);
        String csv = "group,grade,student name,student number\n";
        ArrayList<Student> students;
        for(Group g: groups){
            students = g.getStudents();
            for (Student s: students){
                csv = csv + g.getName() + "," + g.getGrade() + "," +
                        s.getName() + "," + s.getId() + "\n";
            }
        }
        System.out.println("converted");
        return csv;
    }
}
