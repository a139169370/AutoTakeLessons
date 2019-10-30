package pojo;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

/**
 * @author Lucien
 * @date 2019/06/20 15:55
 * @description 构建每行的JPanel
 */

@Getter
@Setter
public class JPanelRow extends JPanel {
	/**
	 * 声明文本框
	 */
//	JTextField studentIdJTextField = new JTextField(8);
//	JPasswordField passWordJTextField = new JPasswordField(8);
//	JTextField courseNameJTextField = new JTextField(8);
//	JTextField courseIdJTextField = new JTextField(5);
//	JTextField courseCodeJTextField = new JTextField(6);
//	JTextField bigClassCodeJTextField = new JTextField(5);
//	JTextField smallClassCodeJTextField = new JTextField(5);
//	JTextField statusJTextField = new JTextField(5);


	JLabel studentIdJLabel = new JLabel();
	JLabel passWordJLabel = new JLabel("******");
	JLabel courseNameJLabel = new JLabel();
	JLabel courseIdJLabel = new JLabel();
	JLabel courseCodeJLabel = new JLabel();
	JLabel bigClassCodeJLabel = new JLabel();
	JLabel smallClassCodeJLabel = new JLabel("******");
	JLabel statusJLabel = new JLabel("未运行");
	public JPanelRow(LayoutManager layout) {
		super(layout, true);
		addComponent();
	}

	public JPanelRow(LayoutManager layout, String studentId, String passWord, String courseName, String courseId, String courseCode, String bigClassCode) {
		super(layout, true);

		/**
		 *	如果有传入参数则设置JLabel内容
		 */
		studentIdJLabel.setText(studentId);
		courseNameJLabel.setText(courseName);
		courseIdJLabel.setText(courseId);
		courseCodeJLabel.setText(courseCode);
		bigClassCodeJLabel.setText(bigClassCode);

		addComponent();
	}

	/**
	 * 方法实现说明
	 * @author	lucien
	 * new JLabel，并且将JLabel和JLabel添加到JPanel中
	 * @return	void
	 * @date	2019/6/20 16:10
	 */
	private void addComponent(){


		add(new JLabel("学号:"));
		add(studentIdJLabel);

		add(new JLabel("密码:"));
		add(passWordJLabel);

		add(new JLabel("课程名称:"));
		add(courseNameJLabel);

		add(new JLabel("课程ID:"));
		add(courseIdJLabel);

		add(new JLabel("课程代码:"));
		add(courseCodeJLabel);

		add(new JLabel("大班编号:"));
		add(bigClassCodeJLabel);

		add(new JLabel("小班编号:"));
		add(smallClassCodeJLabel);

		add(new JLabel("状态:"));
		add(statusJLabel);

		setVisible(true);
		setSize(1200, 100);
	}
}
