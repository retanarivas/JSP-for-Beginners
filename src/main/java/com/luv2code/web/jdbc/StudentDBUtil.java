package com.luv2code.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.sql.DataSource;

public class StudentDBUtil {
		
		private DataSource dataSource;
		
		public StudentDBUtil(DataSource theDataSource) {
			dataSource = theDataSource;
		}
		
		public List<Student> getStudents() throws SQLException {
			List<Student> students = new ArrayList<>();
			
			Connection myConn = null;
			Statement myStmt = null;
			ResultSet myRs = null;
			try { 
				myConn = dataSource.getConnection();
				
				String query  = "SELECT * FROM student ORDER BY last_name";
				myStmt = myConn.createStatement();
				
				myRs = myStmt.executeQuery(query);
				
				while(myRs.next()) {
					int id = myRs.getInt("id");
					String firstName = myRs.getString("first_name");
					String lastName = myRs.getString("last_name");
					String email = myRs.getString("email");
					
					Student tempStudent = new Student(id, firstName, lastName, email);
					
					students.add(tempStudent);
				}
				
				return students;
			} finally {
				close(myConn, myStmt, myRs);
			}
			
		}
		
		public void addStudent(Student student) throws SQLException {
			
			Connection myConn = null;
			PreparedStatement myStmt = null;
			
			try {
				myConn = dataSource.getConnection();
				
				String query  = "INSERT INTO student (first_name, last_name, email) VALUES (?, ?, ?)";
				
				myStmt = myConn.prepareStatement(query);
				myStmt.setString(1, student.getFirstName());
				myStmt.setString(2, student.getLastName());
				myStmt.setString(3, student.getEmail());
				
				myStmt.execute();
				
			} finally  {
				close(myConn, myStmt, null);
			}
		}
		
		public Student getStudentById(String theStudentId) throws SQLException, Exception {
			Student theStudent = null;
			
			Connection myConn = null;
			PreparedStatement myStmt = null;
			ResultSet myRs = null;
			int studentId;
			
			try { 
				studentId = Integer.parseInt(theStudentId);
				
				myConn = dataSource.getConnection();
				
				String query  = "SELECT * FROM student WHERE id=?";
				
				myStmt = myConn.prepareStatement(query);
				myStmt.setInt(1, studentId);
				myRs = myStmt.executeQuery();
				
				if(myRs.next()) {
					String firstName = myRs.getString("first_name");
					String lastName = myRs.getString("last_name");
					String email = myRs.getString("email");
					
					theStudent = new Student(studentId, firstName, lastName, email);
				}else {
					throw new Exception("Could not found the studnet ID: " + studentId);
				}
				
				return theStudent;
			} finally {
				close(myConn, myStmt, myRs);
			}
		}
		
		public void updateStudent(Student student) throws SQLException {
			Connection myConn = null;
			PreparedStatement myStmt = null;
			
			try {
				myConn = dataSource.getConnection();
				
				String query  = "UPDATE student SET first_name=?, last_name=?, email=? WHERE id=?";
				
				myStmt = myConn.prepareStatement(query);
				myStmt.setString(1, student.getFirstName());
				myStmt.setString(2, student.getLastName());
				myStmt.setString(3, student.getEmail());
				myStmt.setInt(4, student.getId());
				
				myStmt.execute();
				
			} finally {
				close(myConn, myStmt, null);
			}
			
		}


		public void deleteStudent(int id) throws SQLException {
			Connection myConn = null;
			PreparedStatement myStmt = null;
			
			try {
				myConn = dataSource.getConnection();
				
				String query  = "DELETE FROM student WHERE id=?";
				
				myStmt = myConn.prepareStatement(query);
				myStmt.setInt(1, id);
				
				myStmt.execute();
				
			} finally {
				close(myConn, myStmt, null);
			}
			
		}
		
		private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
			try {
				if(myConn != null) {
					myConn.close();
				}
				
				if(myStmt != null) {
					myStmt.close();
				}
				
				if(myRs != null) {
					myRs.close();
				}
				
			} catch(Exception exc) {
				exc.printStackTrace();
			}
			
		}

}
