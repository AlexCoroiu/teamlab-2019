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

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class QuestionDAO {
    Connection conn;
    DB db;

    public QuestionDAO(Connection conn, DB db) {
        this.conn = conn;
        this.db = db;
    }

    public int createQuestion(Question q, int testID) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.CREATE_QUESTION.toString(), Statement.RETURN_GENERATED_KEYS);
        int qId;
        ps.setString(1, q.getText());
        ps.setString(2, q.getChoices().get(0));
        ps.setString(3, q.getChoices().get(1));
        ps.setString(4, q.getChoices().get(2));
        ps.setString(5, q.getChoices().get(3));
        ps.setString(6, q.getAnswer());
        ps.setInt(7,q.getScore());
        ps.setString(8,q.getType());
        ps.setString(9,q.getChoices().get(4));
        ps.setString(10,q.getChoices().get(5));
        ps.executeUpdate();
        qId = ps.getGeneratedKeys().getInt(1);
        q.setId(qId);
        ps.close();
        ArrayList<String> tags = q.getTags();
        for(String t: tags) {
            createTag(t,qId);
        }
        return qId;
    }

    //return list of questions that are tagged with the component
    public ArrayList<Question> getQuestionsByTags(String tag) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.GET_QUESTION_BY_TAG.toString());
        ps.setString(1,tag);
        ResultSet rs = ps.executeQuery();
        ArrayList<Question> questions = new ArrayList<Question>();
        while(rs.next()){
            int qid = rs.getInt("id");
            String text = rs.getString("text");
            String c1 = rs.getString("choice1");
            String c2 = rs.getString("choice2");
            String c3 = rs.getString("choice3");
            String c4 = rs.getString("choice4");
            String c5 = rs.getString("choice5");
            String c6 = rs.getString("choice6");
            String type = rs.getString("type");
            int score = rs.getInt("score");
            String a = rs.getString("answer");
            ArrayList<String> choices = new ArrayList<String>(Arrays.asList(c1,c2,c3,c4,c5,c6));
            Question q = new Question(qid,text,choices,a,"",type,score);
            questions.add(q);
        }
        ps.close();
        return questions;
    }

    private void createTag(String t, int qId) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.CREATE_TAG.toString());
        ps.setString(1,t);
        ps.executeUpdate();
        ps.close();
        addTagToQuestion(t,qId);
    }

    private void addTagToQuestion(String tId, int qId) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.ADD_TAG_TO_QUESTION.toString());
        ps.setString(1,tId);
        ps.setInt(2,qId);
        ps.executeUpdate();
        ps.close();
    }

    public HashMap<String,ArrayList<Question>> getQuestions(int id, boolean answer) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.GET_QUESTIONS.toString());
        ps.setInt(1,id);
        ResultSet rs = ps.executeQuery();
        HashMap<String,ArrayList<Question>> questions = new HashMap<String, ArrayList<Question>>();
        while(rs.next()){
            int qid = rs.getInt("id");
            String text = rs.getString("text");
            String c1 = rs.getString("choice1");
            String c2 = rs.getString("choice2");
            String c3 = rs.getString("choice3");
            String c4 = rs.getString("choice4");
            String c5 = rs.getString("choice5");
            String c6 = rs.getString("choice6");
            String component = rs.getString("component");
            String type = rs.getString("type");
            int score = rs.getInt("score");
            String a = "";
            if (answer){
                a = rs.getString("answer");
            }
            ArrayList<String> choices = new ArrayList<String>(Arrays.asList(c1,c2,c3,c4,c5,c6));
            Question q = new Question(qid,text,choices,a,component,type,score);
            if (questions.keySet().contains(component)){
                questions.get(component).add(q);
            }   else {
                ArrayList<Question> qs = new ArrayList<Question>(Arrays.asList(q));
                questions.put(component,qs);
            }
        }
        ps.close();
        return questions;
    }

    public void deleteQuestionFromTest(int q, int testId) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(Queries.DELETE_QUESTION.toString());
        ps.setInt(1,q);
        ps.setInt(2,testId);
        ps.executeUpdate();
        ps.close();
    }

    public int addQuestionToTest(int testID,Question question) throws SQLException {
        int qId = question.getId();
        if( qId == 0) {
            qId = createQuestion(question,testID);
        }
        PreparedStatement ps = conn.prepareStatement(Queries.ADD_QUESTION_TO_TEST.toString());
        ps.setInt(1,qId);
        ps.setInt(2,testID);
        ps.setString(3,question.getComponent());
        ps.executeUpdate();
        ps.close();
        return qId;
    }
}
