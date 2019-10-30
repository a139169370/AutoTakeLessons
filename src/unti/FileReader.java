package unti;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucien
 * @date 2019/06/20 17:24
 * @description
 */

public class FileReader {

	/**
	 * 读取文本信息，并返回一个二层List，如果读取出错则返回null
	 * @return List<List<String>> & null
	 */
	public static List<List<String>> readTxt() {
		List<List<String>> info = new ArrayList<List<String>>();
		try {
			File file = new File("../info.txt");
			if(file.isFile() && file.exists()) {
				InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
				BufferedReader br = new BufferedReader(inputStreamReader);
				String lineTxt = null;
				while ((lineTxt = br.readLine()) != null) {
					List<String> infoRow = new ArrayList<String>();
					for (String text : lineTxt.split(" ")){
						infoRow.add(text);
					}
					info.add(infoRow);
				}
				br.close();
				inputStreamReader.close();
				return info;
			} else {
				FileOutputStream fos = new FileOutputStream("../info.txt");
				fos.write(new byte[]{(byte)0xEF, (byte)0xBB, (byte)0xBF});
				fos.close();
				System.out.println("创建文件成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("文件读取错误！");
		}
		return null;
	}

	public static void createFile(){
		try {
			FileOutputStream fos = new FileOutputStream("../info.txt");
			fos.write(new byte[]{(byte)0xEF, (byte)0xBB, (byte)0xBF});
			fos.close();
			System.out.println("创建文件成功！");
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public File TXTHandler(File file) {

		//原本这里的是gb2312，为了兼容更多，选择了gbk
		String code = "gbk";
		byte[] head = new byte[3];
		try {
			InputStream inputStream = new FileInputStream(file);
			inputStream.read(head);
			if (head[0] == -1 && head[1] == -2) {
				code = "UTF-16";
			} else if (head[0] == -2 && head[1] == -1) {
				code = "Unicode";
			} else if (head[0] == -17 && head[1] == -69 && head[2] == -65) {
				code = "UTF-8";
			}
			inputStream.close();

			System.out.println(code);
			if (code.equals("UTF-8")) {
				return file;
			}
			String str = FileUtils.readFileToString(file, code);
			FileUtils.writeStringToFile(file, str, "UTF-8");
			System.out.println("转码结束");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return file;
	}
}
