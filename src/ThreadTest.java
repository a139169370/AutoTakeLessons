import CheckedAndAddStudentID.CheckedUserUsernamePasswordAndClimbStudentID;
import unti.PushChooseCoursePost;
import pojo.CourseInfo;
import pojo.User;

import javax.swing.*;

public class ThreadTest extends Thread{

	/**
	 * 接收pojo作为参数
	 */
	User user;
	CourseInfo courseInfo;
	String cookie;
	PushChooseCoursePost pushChooseCoursePost;
	JLabel statusJLabel;
	Boolean isCouldRun = true;

	public ThreadTest(User user, CourseInfo courseInfo, JLabel statusJLabel) {
		this.user = user;
		this.courseInfo = courseInfo;
		cookie = CheckedUserUsernamePasswordAndClimbStudentID.CheckedUserUsernamePasswordAndClimbStudentID(user, statusJLabel);
		pushChooseCoursePost = new PushChooseCoursePost();
		this.statusJLabel = statusJLabel;
	}

	/**
	 * 重写run方法，run方法的方法体就是线程执行体
	 */
	@Override
	public void run()
	{

		while (true){
			try {
				sleep(4 * 1000);
			}catch (Exception e){
				e.printStackTrace();
			}

			//如果为flase则退出
			if (!isCouldRun){
				break;
			}

			/**
			 * cookie为空则说明登录失败，可能是账号密码错误或非选课时间，失败则退出程序
			 */
			if (!cookie.trim().isEmpty() & !cookie.trim().equals("")) {
				/**
				 * 如果返回true则说明选课成功，退出循环
				 */
				if (pushChooseCoursePost.pushPost(courseInfo, cookie, statusJLabel)) {
					System.out.println("退出循环体");
					break;
				}
			}else {
				break;
			}
		}
	}

	public Boolean getISCouldRun() {
		return isCouldRun;
	}

	public void setIsCouldRun(Boolean isCouldRun) {
		this.isCouldRun = isCouldRun;
	}
}
