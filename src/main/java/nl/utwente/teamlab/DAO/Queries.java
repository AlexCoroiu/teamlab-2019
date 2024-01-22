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

import java.io.*;

public enum Queries {


    CREATE_QUESTIONS(schema()[0]),

    CREATE_STUDENTS(schema()[1]),

    CREATE_TAGS(schema()[2]),

    CREATE_TAGS_QUESTION(schema()[3]),

    CREATE_TEACHERS(schema()[4]),

    CREATE_TESTS(schema()[5]),

    CREATE_COURSES(schema()[6]),

    CREATE_GROUPS(schema()[7]),

    CREATE_PROGRESS(schema()[8]),

    CREATE_QUESTIONS_TEST(schema()[9]),

    CREATE_STUDENTS_COURSE(schema()[10]),

    //TODO maybe populate db with initial data

    CREATE_TEACHER("INSERT OR IGNORE INTO teachers (email, name) " +
            "VALUES (?,?);" ),

    GET_TEACHER("SELECT name FROM teachers " +
            "WHERE email = ?;"),

    GET_COURSES("SELECT id, name FROM courses " +
            "WHERE owner = ? " +
            "ORDER BY id DESC;"),

    CREATE_COURSE ("INSERT INTO courses (name, owner, studies, test, rules, status) " +
            "VALUES (?,?,?,?,?,0);"),

    ADD_NAME_TO_COURSE ("UPDATE courses " +
            "SET name = ? " +
            "WHERE id = ?;"),

    GET_COURSE ("SELECT * FROM courses " +
            "WHERE id = ?;"),

    GET_TESTS("SELECT id, name FROM tests " +
            "WHERE owner = ? " +
            "ORDER BY id DESC;"),

    CREATE_TEST("INSERT INTO tests (name, owner) " +
            "VALUES (?,?);"),

    ADD_NAME_TO_TEST ("UPDATE tests " +
            "SET name = ? " +
            "WHERE id = ?;"),

    GET_TEST("SELECT * FROM tests " +
            "WHERE id = ?;"),

    GET_QUESTIONS("SELECT * FROM questions, questions_test " +
            "WHERE questions_test.test = ? " +
            "AND questions_test.question = questions.id;"),

    CREATE_QUESTION("INSERT OR IGNORE INTO questions (text, choice1, choice2, choice3, choice4, " +
            "answer, score, type, choice5, choice6) " +
            "VALUES (?,?,?,?,?,?,?,?,?,?);"),

    DELETE_QUESTION("DELETE FROM questions_test " +
            "WHERE question = ? " +
            "AND test = ?;"),

    ADD_QUESTION_TO_TEST("INSERT OR IGNORE INTO questions_test (question, test, component) " +
            "VALUES (?,?,?);"),

    CREATE_TAG("INSERT OR IGNORE INTO tags (name) " +
            "VALUES (?);" ),

    ADD_TAG_TO_QUESTION("INSERT OR IGNORE INTO tags_question (tag, question) " +
            "VALUES (?,?);"),

    GET_QUESTION_BY_TAG("SELECT * FROM questions, tags_question " +
            "WHERE tags_question.tag = ? " +
            "AND tags_question.question = questions.id " +
            "ORDER BY questions.score " +
            "LIMIT 10;"), //TODO make final static variable somewhere

    CREATE_STUDENT("INSERT OR REPLACE INTO students (id,email,name,gender,nationality,belbin) " +
            "VALUES (?,?,?,?,?,?);"), //replace in case the student was registered before (in current or another course)

    ADD_STUDENT_TO_COURSE("INSERT OR REPLACE INTO students_course (student, course, motivation, knowledge, study, 'group') " +
            "VALUES (?,?,?,?,?,?);"), //replace in case the student does the test more than 1 time

    ADD_PROGRESS("INSERT INTO progress (student, course, motivation, knowledge, 'date') " +
            "VALUES (?,?,?,?, datetime('now','localtime'));"),

    GET_STUDENTS_BY_COURSE("SELECT * FROM students, students_course " +
            "WHERE students_course.course = ? " +
            "AND students_course.student = students.id;"),

    ADD_STUDIES_TO_COURSE("UPDATE courses " +
            "SET studies = ? " +
            "WHERE id = ?;"),

    ADD_TEST_TO_COURSE("UPDATE courses " +
            "SET test = ? " +
            "WHERE id = ?;"),

    ADD_RULES_TO_COURSE("UPDATE courses " +
            "SET rules = ? " +
            "WHERE id = ?;"),

    ADD_STATUS_TO_COURSE("UPDATE courses " +
            "SET status = ? " +
            "WHERE id = ?;"),

    CREATE_GROUP("INSERT INTO groups (name, course, grade, performance) " +
            "VALUES (?,?,?,?);"),

    ADD_GROUP_TO_STUDENT("UPDATE students_course " +
            "SET 'group' = ? " + //group is a predefined thing in sql
            "WHERE student = ? " +
            "AND course = ?; "),

    GET_GROUPS("SELECT motivation, knowledge, study, groups.id AS gid, groups.name AS gname, " +
            "groups.grade, groups.performance, students.* FROM students_course, groups, students " +
            "WHERE students_course.course = ? " +
            "AND students_course.\"group\" = groups.id " +
            "AND students_course.student = students.id " +
            "ORDER BY groups.id;"),

    ADD_GRADE_TO_GROUP("UPDATE groups " +
            "SET grade = ? " +
            "WHERE id = ?;"),

    ADD_PERFORMANCE_TO_GROUP("UPDATE groups " +
            "SET performance = ? " +
            "WHERE id = ?;"),

    DELETE_STUDENT_FROM_GROUP("UPDATE students_course " +
            "SET 'group' = 0 " +
            "WHERE student = ?" +
            "AND course = ?;"),

    GET_STUDENTS_GENDER("SELECT students.gender, count(gender) FROM students, students_course\n" +
            "WHERE students_course.course = ?\n" +
            "AND students_course.student = students.id\n" +
            "GROUP BY students.gender;"),

    GET_STUDENTS_NATIONALITY("SELECT students.nationality, count(nationality) FROM students, students_course\n" +
            "WHERE students_course.course = ?\n" +
            "AND students_course.student = students.id\n" +
            "GROUP BY students.nationality;"),

    GET_STUDENTS_BELBIN("SELECT students.belbin, count(belbin) FROM students, students_course\n" +
            "WHERE students_course.course = ?\n" +
            "AND students_course.student = students.id\n" +
            "GROUP BY students.belbin;"),

    GET_STUDENTS_MOTIVATION("SELECT motivation, count(motivation) FROM students_course\n" +
            "WHERE students_course.course = ?\n" +
            "GROUP BY motivation;"),

    GET_STUDENTS_KNOWLEDGE("SELECT knowledge, count(knowledge) FROM students_course\n" +
            "WHERE students_course.course = ?\n" +
            "GROUP BY knowledge;"),

    GET_STUDENTS_STUDY("SELECT study, count(study) FROM students_course\n" +
            "WHERE students_course.course = ?\n" +
            "GROUP BY study;");

    private final String query;

    Queries(String query) {
        this.query = query;
    }

    private static String[] schema() {
        String schemaFile = "schema";
        String schema= "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(schemaFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = "";
        while(true){
            try {
                if (!((line = reader.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            schema = schema + line + "\n";
        }
        String[] schemas = schema.split(";");
        System.out.println(schemas);
        return schemas;
}

    @Override
    public String toString() {
        return query;
    }
}
