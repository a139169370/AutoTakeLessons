package pojo;

/**
 * @author Lucien
 * @date 2019/06/19 10:55
 * @description
 */

public class CourseInfo {
	private String courseName;
	private String courseId;
	private String courseCode;
	private String classCode1;
	private String classCode2;

	public CourseInfo(String courseName, String courseId, String courseCode, String classCode1) {
		this.courseName = courseName;
		this.courseId = courseId;
		this.courseCode = courseCode;
		this.classCode1 = classCode1;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getClassCode1() {
		return classCode1;
	}

	public void setClassCode1(String classCode1) {
		this.classCode1 = classCode1;
	}

	public String getClassCode2() {
		return classCode2;
	}

	public void setClassCode2(String classCode2) {
		this.classCode2 = classCode2;
	}
}
