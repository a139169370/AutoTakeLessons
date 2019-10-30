import pojo.TxtInfo;
import unti.FileReader;

import javax.swing.*;

/**
 * @author Lucien
 * @date 2019/06/21 17:18
 * @description 主函数入口
 */

public class Main {
	public static void main(String[] ages){
		//读取文本信息
		TxtInfo.setInfo(FileReader.readTxt());
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		//将文本信息传送到MainUi构建视图
		MainUi mainUi = new MainUi();
	}
}
