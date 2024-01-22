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

import java.io.*;
import java.util.ArrayList;

import static org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS;


//TOOD make diff files for diff coruses

//TODO make sure i always name the table columns names the same

//TODO hwo the fuck se creaza folderu ala pt rezultate ca ma fute
public class GroupMasterMind {
    //TODO paths in tomcat
    private final String PATH = "groupEng/";
    private final String RESULTS_PATH = IS_OS_WINDOWS ? "groupEng/groups/" :  "groupEng/groups./";
    private String id;
    //TODO rewrite the same files always all have one file per course?
    private final String RULES_FILE = ".groupeng";
    private final String STUDENTS_FILE = "students.csv";
    private final String RESULTS_FILE = "_groups.csv";
    private final String COMMAND = IS_OS_WINDOWS ? "py -2 GroupEng.py .\\" : "python2 GroupEng.py ./";

    public ArrayList<Group> createGroups(Course course) throws IOException, InterruptedException {
        id = String.valueOf(course.getId());
        createStudentsFile(course.getStudents());
        createRulesFile(course.getRules());
        Process p = Runtime.getRuntime().exec(COMMAND  + id + RULES_FILE, null,new File("groupEng"));
        p.waitFor();// 0 = terminated well but when  itry to do this manually outside fo java it doesnt work
        System.out.println("created");
        return readGroups();
    }

    private ArrayList<Group> readGroups() throws IOException {
        //TODO make path beutiful
        BufferedReader reader = new BufferedReader(new FileReader(RESULTS_PATH + id + "/" + id + RESULTS_FILE));
        //TODO what field to read according to how the database looks
        ArrayList<Group> groups = new ArrayList<Group>();
        String line;
        while((line = reader.readLine()) != null) {
            String [] sep = line.split(", ");
            int l = sep.length;
            Group g = new Group();

            //get group name(number)
            String[] group = sep[0].split(" ");
            System.out.println(sep[0]);
            g.setName(Integer.parseInt(group[1]));

            //create students
            ArrayList<Student> students = new ArrayList<Student>();
            for(int i =1; i < l; i++){
                Student s = new Student();
                System.out.println(Integer.parseInt(sep[i]));
                s.setId(Integer.parseInt(sep[i]));
                students.add(s);
            }

            g.setStudents(students);
            groups.add(g);
        }
        reader.close();
        System.out.println("created groups in db");
        return groups;
        //TODO add course to group?
    }

    //
    private void createStudentsFile(ArrayList<Student> studentsList) throws IOException {
        File studentsFile = new File(PATH + id + STUDENTS_FILE);
        BufferedWriter writer = new BufferedWriter(new FileWriter(PATH + id +  STUDENTS_FILE));
        //headers
        writer.write("id,email,name,gender,nationality,belbin,motivation,knowledge,study\n");
        for(Student s: studentsList){
            writer.write(s.getId() + "," +
                    s.getEmail() + "," +
                    s.getName() + "," +
                    s.getGender() + "," +
                    s.getNationality() + "," +
                    s.getBelbin() + "," +
                    s.getMotivation() + "," +
                    s.getKnowledge() + "," +
                    s.getStudy() +
                    "\n");
        }
        writer.close();
    }

    //TODO according to course/groups (list or string?)
    private void createRulesFile (String rules) throws IOException {
        File rulesFile = new File(PATH + id + RULES_FILE);
        BufferedWriter writer = new BufferedWriter(new FileWriter(PATH + id + RULES_FILE));
        //students file, idetifier and harcoded group size
        //TODO not hardcoded group size
        writer.write("classlist : " + id + STUDENTS_FILE + "\nstudent_identifier : id\n");
        writer.write(rules);
        writer.close();
    }

}
