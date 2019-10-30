import lombok.Getter;
import lombok.Setter;
import pojo.CourseInfo;
import pojo.JPanelRow;
import pojo.TxtInfo;
import pojo.User;
import unti.FileReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucien
 * @date 2019/06/20 15:06
 * @description
 */

@Setter
@Getter
public class MainUi extends JFrame {

	/**
	 * 主界面JPanel
	 */
	private JPanel mainJPanel;

	//用于判断当前状态是否为运行
	Boolean isRunThread = false;

	/**
	 * 	线程类队列，用于中断线程
	 */
	List<ThreadTest> threadTestList = new ArrayList<ThreadTest>();

	/**
	 * 开始/结束运行按钮
	 */
	JButton startRunJButton = new JButton("点击开始");

	/**
	 * “请先停止”提示框，在未停止时点击读取info.txt按钮显示
	 */
	JDialog pleaseStopJDialog = new JDialog(this, "请先停止！", false);
	public MainUi(){
		super("刷刷刷");
		//流式布局，左对齐，水平竖直间隔5个像素
		 mainJPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

		 //填充view
		addView(TxtInfo.getInfo());

		//将mainJPanel添加到JFrame
		add(mainJPanel);

		/**
		 * info.txt按钮
		 */
		JButton createTxtFileButton = new JButton("创建info.txt");
		createTxtFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileReader.createFile();
			}
		});

		/**
		 * “请先停止”提示框，在未停止时点击读取info.txt按钮显示
		 */
		pleaseStopJDialog.setLayout(new GridLayout(2, 1));
		JPanel pleaseStopJLbelJPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		pleaseStopJLbelJPanel.add(new JLabel("请先停止！"));
		pleaseStopJLbelJPanel.setSize(200,75);
		pleaseStopJDialog.add(pleaseStopJLbelJPanel);
		JButton confirmJButton = new JButton("确定");
		confirmJButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pleaseStopJDialog.setVisible(false);
			}
		});
		JPanel pleaseStopJButtonJPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		pleaseStopJButtonJPanel.add(confirmJButton);
		pleaseStopJButtonJPanel.setSize(200, 75);
		pleaseStopJDialog.add(pleaseStopJButtonJPanel);

		pleaseStopJDialog.setVisible(false);
		pleaseStopJDialog.setSize(200, 150);
		pleaseStopJDialog.setLocationRelativeTo(this);
		/**
		 * 刷新界面按钮，读取文本并刷新界面
		 */
		JButton refreshJButton = new JButton("读取info.txt");
		refreshJButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isRunThread) {
					/**
					 * 判断当前是否为运行状态，若不是则读取txt；
					 */
					//读取txt
					TxtInfo.setInfo(FileReader.readTxt());
					//删除目前视图
					mainJPanel.removeAll();
					//填充视图
					addView(TxtInfo.getInfo());
					//重绘视图
					mainJPanel.validate();
					mainJPanel.repaint();
				}else {
					pleaseStopJDialog.setVisible(true);
				}
			}
		});

		/**
		 * 开始运行按钮，启动线程开始循环爬取教务系统
		 */
		startRunJButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isRunThread){
					/**
					 * 判断当前是否为运行状态，若不是则启动线程；
					 */
					//更改状态
					isRunThread = true;
					//调用pojo获取数据
					List<List<String>> info = TxtInfo.getInfo();
					for (int i = 0;i < mainJPanel.getComponentCount(); i++){
						//获取statusJLabel
						JPanel jPanel = (JPanel) mainJPanel.getComponent(i);
						JLabel statusJLabel = (JLabel) jPanel.getComponent(jPanel.getComponentCount() -1);
						//设置statusJLabel前景色与文本
						statusJLabel.setForeground(new Color(75,129,191));
						statusJLabel.setText("运行中");
						//填充pojo
						List<String> txtRow = info.get(i);
						User user = new User(txtRow.get(0), txtRow.get(1));
						CourseInfo courseInfo = new CourseInfo(txtRow.get(2), txtRow.get(3), txtRow.get(4), txtRow.get(5));
						//如果有小班编码则写入
						if (txtRow.size() == 7){
							courseInfo.setClassCode2(txtRow.get(6));
						}else {
							courseInfo.setClassCode2("");
						}
						ThreadTest threadTest = new ThreadTest(user, courseInfo, statusJLabel);
						threadTest.start();
						threadTestList.add(threadTest);
					}

					//更改按钮文本
					startRunJButton.setText("点击停止");
				}else {
					/**
					 * else:判断当前是否为运行状态，若不是则中断线程；
					 * for:循环线程序列，判断是否已经中断，若不是则中断线程
					 */
					for (ThreadTest threadTest : threadTestList){
						//设置为不可运行
						threadTest.setIsCouldRun(false);
					}
					threadTestList.clear();
					//更改状态
					isRunThread = false;

					for (int i = 0;i < mainJPanel.getComponentCount(); i++) {
						//获取statusJLabel
						JPanel jPanel = (JPanel) mainJPanel.getComponent(i);
						JLabel statusJLabel = (JLabel) jPanel.getComponent(jPanel.getComponentCount() - 1);
						statusJLabel.setText("未运行");
						statusJLabel.setForeground(Color.BLACK);
					}
					//更改按钮文本
					startRunJButton.setText("点击开始");
				}
			}
		});

		/**
		 * 填充按钮控件JPanel，并填充入JFrame
		 */
		JPanel buttonJPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		buttonJPanel.add(createTxtFileButton);
		buttonJPanel.add(refreshJButton);
		buttonJPanel.add(startRunJButton);
		add(buttonJPanel, BorderLayout.SOUTH);

		//关闭窗口事件
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//此行开始下数三行需按顺序，防止二次加载和做到窗体居中
		setVisible(true);
		setSize(900, 500);
		//窗体居中
		setLocationRelativeTo(getOwner());
	}

	/**
	 * 传入参数填充mainJPanel
	 */
	public void addView(List<List<String>> info){
		/**
		 * info为null或含有信息体；
		 * 根据文本信息info内容循环添加布局；
		 */
		if (info != null) {
			for (List<String> infoRow : info) {
				JPanelRow jPanelRow = new JPanelRow(new FlowLayout(FlowLayout.LEFT, 5, 5), infoRow.get(0), infoRow.get(1), infoRow.get(2), infoRow.get(3), infoRow.get(4), infoRow.get(5));
				if (infoRow.size() == 7) {
					jPanelRow.getSmallClassCodeJLabel().setText(infoRow.get(6));
				}
				mainJPanel.add(jPanelRow);
			}
		}
	}
}
