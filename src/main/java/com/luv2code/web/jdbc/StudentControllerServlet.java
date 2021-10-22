package com.luv2code.web.jdbc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private StudentDBUtil studentDBUtil;
	
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		
		try {
			
			studentDBUtil = new StudentDBUtil(dataSource);
			
		} catch(Exception exc) {
			throw new ServletException(exc);
		}
	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String command = (String) request.getParameter("command");
			if(command == null) {
				command = "LIST";
			}
			
			switch(command) {
				case "LIST":
					listStudents(response, request);
					break;
				case "ADD":
					addStudent(response, request);
					break;
				case "LOAD":
					loadStudent(response, request);
					break;
				case "UPDATE":
					updateStudent(response, request);
					break;
				case "DELETE":
					deleteStudent(response, request);
					break;
				default:
					listStudents(response, request);
			}
			
		} catch(Exception exc) {
			throw new ServletException(exc);
		}
		
	}


	private void deleteStudent(HttpServletResponse response, HttpServletRequest request) throws Exception {
		int id = Integer.parseInt(request.getParameter("studentId"));
		
		studentDBUtil.deleteStudent(id);
		
		listStudents(response, request);
	}


	private void updateStudent(HttpServletResponse response, HttpServletRequest request) throws Exception {
		int id = Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		Student student = new Student(id, firstName, lastName, email);
		
		studentDBUtil.updateStudent(student);
	
		listStudents(response, request);
	}


	private void loadStudent(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String theStudentId = request.getParameter("studentId");
		
		Student student = studentDBUtil.getStudentById(theStudentId);
		
		request.setAttribute("THE_STUDENT", student);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
	}

	private void addStudent(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String firstName = (String) request.getParameter("firstName");
		String lastName = (String) request.getParameter("lastName");
		String email = (String) request.getParameter("email");
		
		Student student = new Student(firstName, lastName, email);		
		studentDBUtil.addStudent(student);
		
		listStudents(response, request);
	}


	private void listStudents(HttpServletResponse response, HttpServletRequest request) throws Exception {
		
		List<Student> students = studentDBUtil.getStudents();
		
		request.setAttribute("STUDENT_LIST", students);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
	}

}
