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

package nl.utwente.teamlab.API;


import nl.utwente.teamlab.logic.Service;
import nl.utwente.teamlab.model.*;
import nl.utwente.teamlab.security.SecurityManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Path("/")
@Consumes({"application/xml"})
@Produces({"application/xml"})
public class API {
    private Service service = new Service();

    //CREATE
    @POST
    @Path("/courses/new")
    public Course createCourse(Course course,
                                 @Context HttpServletResponse response,
                                 @Context HttpServletRequest request){
        course.setOwner((Teacher) SecurityManager.getUser(request));
        try {
            return service.createCourse(course);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Course();
        }
    }

    @POST
    @Path("/tests/new")
    public Test createTest(Test test,
                           @Context HttpServletResponse response,
                           @Context HttpServletRequest request) {
        System.out.println(test);
        test.setTeacher((Teacher) SecurityManager.getUser(request));
        try {
            return service.createTest(test);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Test();
        }
    }

    @PUT //idempotent - known ID
    @Path ("/courses/{id}/student/result")//TODO idk
    public void addResult(@PathParam("id") int id, Student student,
                              @Context HttpServletResponse response,
                              @Context HttpServletRequest request) {
        try {
            service.addResult(id,student);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @POST // not idempotent - randomness generated by GroupEng
    @Path("courses/{id}/groups")
    public void createGroups(@PathParam("id") int id,
                             Course course,
                             @Context HttpServletResponse response,
                             @Context HttpServletRequest request) {
        try {
            service.createGroups(course,(Teacher) SecurityManager.getUser(request));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @POST
    @Produces({"text/plain"})
    @Path("tests/{id}/question")
    public String createQuestion(@PathParam("id") int id,
                              Question question,
                              @Context HttpServletResponse response,
                              @Context HttpServletRequest request)  {
        try {
            Integer qId = service.addQuestionToTest(id, question, (Teacher) SecurityManager.getUser(request));
            return qId.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return "-1";
        }
    }

    //READ
    //all
    @GET
    @Path("/courses")
    public List<Course> getCourses(@Context HttpServletResponse response,
                                   @Context HttpServletRequest request)  {
        try {
            return service.getCourses((Teacher) SecurityManager.getUser(request));
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @GET
    @Path("/tests")
    public List<Test> getTests(@Context HttpServletResponse response,
                               @Context HttpServletRequest request)  {
        try {
            return service.getTests((Teacher) SecurityManager.getUser(request));
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    //for courses
    @GET
    @Path("courses/{id}/groups")
    public List<Group> getGroups(@PathParam("id") int id,
                                 @Context HttpServletResponse response,
                                 @Context HttpServletRequest request)  {
        try {
            return service.getGroups(id,(Teacher) SecurityManager.getUser(request));
        } catch (SQLException e) {
            e.printStackTrace();
            return  new ArrayList<>();
        }
    }

    //csv
    @GET
    @Path("courses/{id}/groups/csv")
    public Course getGroupsCSV(@PathParam("id") int id,
                               @Context HttpServletResponse response,
                               @Context HttpServletRequest request) {
        try {
            return service.getGroupsCSV(id, (Teacher) SecurityManager.getUser(request));
        } catch (SQLException e) {
            e.printStackTrace();
            return new Course();
        }
    }

    @GET
    @Path("courses/{id}/results")
    public List<Student> getStudents(@PathParam("id") int id,
                                     @Context HttpServletResponse response,
                                     @Context HttpServletRequest request){
        try {
            return service.getStudents(id,(Teacher) SecurityManager.getUser(request));
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @GET
    @Path("courses/{id}/charts")
    public List<Chart> getCharts(@PathParam("id") int course,
                                 @Context HttpServletResponse response,
                                 @Context HttpServletRequest request) {
        try {
            return service.getCharts(course,(Teacher) SecurityManager.getUser(request));
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    //for tests
    @GET
    @Path("/tests/new/questions/{tag}")
    public List<Question> getQuestions(@PathParam("tag") String tag,
                                       @Context HttpServletResponse response,
                                       @Context HttpServletRequest request) {
        try {
            return service.getQuestionByTags(tag); //does not need owner
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    //one
    @GET
    @Path("/courses/{id}")
    public Course getCourse(@PathParam("id") int id,
                            @Context HttpServletResponse response,
                            @Context HttpServletRequest request){
        try {
            return service.getCourseForTeacher(id,(Teacher) SecurityManager.getUser(request));
        } catch (SQLException e) {
            e.printStackTrace();
            return new Course();
        }
    }

    @GET
    @Path("/courses/{id}/student")
    public Course getCourseForStudent(@PathParam("id") int id,
                                      @Context HttpServletResponse response,
                                      @Context HttpServletRequest request)  {
        try {
            return service.getCourseForStudent(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Course();
        }
    }

    @GET
    @Path("/tests/{id}")
    public Test getTest(@PathParam("id") int id,
                         @Context HttpServletResponse response,
                         @Context HttpServletRequest request)  {
        try {
            return service.getTest(id,(Teacher) SecurityManager.getUser(request));
        } catch (SQLException e) {
            e.printStackTrace();
            return new Test();
        }
    }




    //UPDATE
    //course
    @PUT
    @Path("courses/{id}/name")
    @Consumes({"text/plain"})
    public void addNameToCourse(@PathParam("id") int id, String name,
                                @Context HttpServletResponse response,
                                @Context HttpServletRequest request){
        try {
            service.addNameToCourse(id,name,(Teacher) SecurityManager.getUser(request));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @PUT
    @Path("/courses/{id}/test")
    public void addTestToCourse(@PathParam("id") int id, Test test,
                                @Context HttpServletResponse response,
                                @Context HttpServletRequest request) {
        try {
            service.addTestToCourse(id,test,(Teacher) SecurityManager.getUser(request));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @PUT
    @Path("courses/{id}/rules")
    public  void addRulesToCourse(@PathParam("id") int id,
                                  String rules,
                                  @Context HttpServletResponse response,
                                  @Context HttpServletRequest request)  {
        try {
            service.addRulesToCourse(id,rules,(Teacher) SecurityManager.getUser(request));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @PUT
    @Path("courses/{id}/studies")
    public  void addStudiesToCourse(@PathParam("id") int id,
                                  String studies,
                                  @Context HttpServletResponse response,
                                  @Context HttpServletRequest request) {
        try {
            service.addStudiesToCourse(id,studies,(Teacher) SecurityManager.getUser(request));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @PUT
    @Path("courses/{id}/status")
    @Consumes({"text/plain"})
    public void addStatus(@PathParam("id") int id,
                          boolean status,
                          @Context HttpServletResponse response,
                          @Context HttpServletRequest request){
        try {
            service.addStatus(id,status,(Teacher) SecurityManager.getUser(request));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //groups
    @PUT
    @Path("courses/{cid}/groups/{id}/grade")
    @Consumes({"text/plain"})
    public void addGrade (@PathParam("id") int id,
                          @PathParam("cid") int cid,
                          int grade,
                          @Context HttpServletResponse response,
                          @Context HttpServletRequest request)  {
        try {
            service.addGradeToGroup(grade,id,cid,(Teacher) SecurityManager.getUser(request));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @PUT
    @Path("courses/{cid}/groups/{id}/performance")
    @Consumes({"text/plain"})
    public void addPerformance (@PathParam("id") int id,
                                @PathParam("cid") int cid,
                          int value,
                          @Context HttpServletResponse response,
                          @Context HttpServletRequest request) {
        try {
            service.addPerformanceToGroup(value,id,cid,(Teacher) SecurityManager.getUser(request));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @PUT
    @Path("courses/{cid}/groups/{gid}/students/{sid}")
    public void addStudentToGroup (@PathParam("cid") int course,
                                   @PathParam("gid") int group,
                                   @PathParam("sid") int student,
                                   @Context HttpServletResponse response,
                                   @Context HttpServletRequest request){
        try {
            service.addStudentToGroup(course,group,student,(Teacher) SecurityManager.getUser(request));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @DELETE
    @Path("courses/{cid}/groups/{gid}/students/{sid}")
    public void deleteStudentFromGroup (@PathParam("cid") int course,
                                        @PathParam("gid") int group,
                                        @PathParam("sid") int student,
                                @Context HttpServletResponse response,
                                @Context HttpServletRequest request) {
        try {
            service.deleteStudentFromGroup(course,group,student,(Teacher) SecurityManager.getUser(request));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //tests
    @PUT
    @Path("tests/{id}/name")
    @Consumes({"text/plain"}) //should be like in rules, or rules like here
    public void addNameToTests(@PathParam("id") int id, String name,
                                @Context HttpServletResponse response,
                                @Context HttpServletRequest request){
        System.out.println(name);
        try {
            service.addNameToTest(id,name,(Teacher) SecurityManager.getUser(request));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @DELETE
    @Path("tests/{id}/question/{qid}")
    public void deleteQuestionFromTest (@PathParam("id") int id,
                                       @PathParam("qid") int q,
                                       @Context HttpServletResponse response,
                                       @Context HttpServletRequest request)  {
        try {
            service.deleteQuestionFromTest(id,q,(Teacher) SecurityManager.getUser(request));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
