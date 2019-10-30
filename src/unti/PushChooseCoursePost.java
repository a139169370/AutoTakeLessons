package unti;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;
import pojo.CourseInfo;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Lucien
 * @date 2019/06/18 20:34
 * @description
 */

public class PushChooseCoursePost {
	public Boolean pushPost(CourseInfo courseInfo, String cookie, JLabel statusJLabel){
		/**
		 * 获取参数
		 */
		String id = courseInfo.getCourseId();
		String code = courseInfo.getCourseCode();
		String classCode1 = courseInfo.getClassCode1();
		String classCode2 = courseInfo.getClassCode2();

		/**
		 * 当前时间
		 */
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		String date = df.format(new Date());
		date += " ";

		//构建该门课程URL
		String chooseClassURL = "http://class.sise.com.cn:7001/sise/module/select_class/studentselectteam_main.jsp?id=" + id + "&code=" + code;

		//post提交地址
		String postURL = "";
		//radiobutton参数，第一个大班参数
		String radiobutton = "";
		//第二个小班选项参数
		String smallClass = "";

		try {

			//new一个HTTPClient对象
			HttpClient httpClient = new HttpClient();

			/**
			 * 向服务器发送GET请求，接收服务器返回消息，解析消息以读取post请求提交地址与参数
			 */
			// 进行登陆后的操作，跳转到信息页面
			//获取home页面
			GetMethod getMethodForChooseClassUrl = new GetMethod(chooseClassURL);
			// 每次访问需授权的网址时需带上前面的 cookie 作为通行证
			// （httpClient客户端 会自动带上  如不是特殊要求一般不进行设置）
			getMethodForChooseClassUrl.setRequestHeader("cookie", cookie);
			// 你还可以通过 PostMethod/GetMethod 设置更多的请求后数据
			// 例如，referer 从哪里来的，UA 像搜索引擎都会表名自己是谁，无良搜索引擎除外
			getMethodForChooseClassUrl.setRequestHeader("Referer", "http://class.sise.com.cn:7001/sise/index.jsp");
			getMethodForChooseClassUrl.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.221 Safari/537.36 SE 2.X MetaSr 1.0");
			//发送执行
			httpClient.executeMethod(getMethodForChooseClassUrl);
			//将服务器返回的个人信息页面html文本保存在字符串中
			String chooseClassHtml = getMethodForChooseClassUrl.getResponseBodyAsString();

			/**
			 * 判断是否为“本课程所有教学班的人数已满”页面或“这个时间不允许你选课！”页面，若是则退出；
			 */
			if (chooseClassHtml.trim().contains("window.location.href")){
				System.out.println(date + "本课程所有教学班的人数已满");
				statusJLabel.setText(date + "教学班人数满");
				return false;
			}else if (chooseClassHtml.trim().contains("这个时间不允许你选课！")){
				System.out.println(date + "这个时间不允许你选课！");
				statusJLabel.setText(date + "非选课时间");
				return true;
			}else if (chooseClassHtml.trim().contains("本页面防刷新！3秒过后操作。")){
				System.out.println(date + "本页面防刷新！3秒过后操作。");
				statusJLabel.setText(date + "防刷新页面");
				return false;
			}


			/**
			 * 解析HTML获取post地址与数据
			 */
			//使用jsoup解析
			Document homeDocument = Jsoup.parse(chooseClassHtml);

			/**
			 * 解析post地址
			 */
			Elements scriptElements = homeDocument.getElementsByTag("script");
			for (Element element : scriptElements) {
				String[] data = element.data().trim().split("saveclass");
				String[] dataWithURL = data[1].split("form1.action=\"");
				postURL = dataWithURL[1].split("\"")[0];
				/**
				 * 将参数“LT”换为“T”
				 */
//				if (postURL.contains("LT")){
//					postURL.replace("LT", "T");
//				}
			}

			/**
			 * 解析post需要提交的数据
			 */
			Elements tdElements = homeDocument.getElementsByTag("td");
			for (Element td : tdElements) {
				if (td.text().contains(classCode1) & !td.text().contains(classCode1 + "0")) {
					String[] data = td.html().split("<br>");
					for (String dataString : data) {
						if (dataString.contains(classCode1)) {
							if (dataString.contains("input")) {
								String[] valueString = dataString.split("value=\"");
								radiobutton = valueString[1].split("\"")[0];
							}else {
								System.out.println(date + "选定大班无空余人数");
								statusJLabel.setText(date + "选定大班无空余人数");
								return false;
							}
						}
					}
					break;
				}
			}

			/**
			 * 如果radiobutton为空，则选班未开始
			 */
			if (radiobutton.trim().isEmpty() | radiobutton.equals("")) {
				System.out.println(date + "选课选班系统未开放，将继续循环！");
				statusJLabel.setText(date + "系统未开放，继续循环");

				/**
				 * 模拟点击“选课选班”按钮操作
				 */
				//选课选班链接的URL
				String chooseClassUrl = "http://class.sise.com.cn:7001/sise/module/select_class/studentselectteam_item.jsp";
				GetMethod getMethod = new GetMethod(chooseClassUrl);
				// 每次访问需授权的网址时需带上前面的 cookie 作为通行证
				// （httpClient客户端 会自动带上  如不是特殊要求一般不进行设置）
				getMethod.setRequestHeader("cookie", cookie);
				// 你还可以通过 PostMethod/GetMethod 设置更多的请求后数据
				// 例如，referer 从哪里来的，UA 像搜索引擎都会表名自己是谁，无良搜索引擎除外
				getMethod.setRequestHeader("Referer", "http://class.sise.com.cn:7001/sise/index.jsp");
				getMethod.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.221 Safari/537.36 SE 2.X MetaSr 1.0");
				//发送执行
				httpClient.executeMethod(getMethod);
				//将服务器返回的个人信息页面html文本保存在字符串中
				String Html = getMethod.getResponseBodyAsString();

				return false;
			}

			/**
			 * 如果该门课有小班要求，则获取小班数据
			 */
			if (!classCode2.trim().isEmpty() | !classCode2.equals("")) {
				Elements elementsByAttributeValue = homeDocument.getElementsByAttributeValue("name", radiobutton);
				for (Element element : elementsByAttributeValue){
					if (element.parent().text().trim().contains(classCode2)){
						smallClass = element.attr("value");
					}else {
						System.out.println(date + "无空余位置...继续循环");
						statusJLabel.setText(date + "无空余位置");
						return false;
					}
				}
			}


			/**
			 * 如果对应班级有位置，则将选课post请求提交至服务器
			 */
			if (!radiobutton.trim().isEmpty()) {
				//模拟登陆，按实际服务器端要求选用 Post 或 Get 请求方式
				PostMethod postMethod = new PostMethod("http://class.sise.com.cn:7001/sise/module/select_class/" + postURL);



				//判断该门课是否有大小班要求
				if (!classCode2.trim().isEmpty() | !classCode2.equals("")) {
					/**
					 * 填充有大小班要求的数据
					 */
					//构建NameValuePair数组，填充post请求的参数
					NameValuePair[] data = {
							new NameValuePair("radiobutton", radiobutton),
							new NameValuePair(radiobutton, smallClass)
					};
					postMethod.setRequestBody(data);
				} else {
					/**
					 * 填充只需选择大班的数据
					 */
					//构建NameValuePair数组，填充post请求的参数
					NameValuePair[] data = {
							new NameValuePair("radiobutton", radiobutton),
							new NameValuePair(radiobutton, smallClass)
					};
					postMethod.setRequestBody(data);
				}

				postMethod.setRequestHeader("cookie", cookie);
				// 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
				httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
				int statusCode = httpClient.executeMethod(postMethod);
				String chooseClassResultHtml = postMethod.getResponseBodyAsString();

				/**
				 * 解析提交请求后返回的数据
				 */
				if (chooseClassResultHtml.trim().contains("选班成功! 请手工点击刷新查看更新课表！！！")){
					System.out.println(date + "选班成功! 请手工点击刷新查看更新课表！！！");
					statusJLabel.setText("选班成功");
					statusJLabel.setForeground(Color.GREEN);
					return true;
				}else if (chooseClassResultHtml.trim().contains("选班出错!请重新选班提交")){
					System.out.println(date + "选班出错!请重新选班提交");
					statusJLabel.setText("选班出错");
					statusJLabel.setForeground(Color.RED);
					return true;
				}else if (chooseClassResultHtml.trim().contains("所选教学班的课时与现有教学班的课时有冲突！")){
					System.out.println(date + "所选教学班的课时与现有教学班的课时有冲突！");
					statusJLabel.setText("课时冲突");
					statusJLabel.setForeground(Color.RED);
					return true;
				}else if (chooseClassResultHtml.trim().contains("一个学期选择的课程不能超过28.5个学分！")){
					System.out.println("一个学期选择的课程不能超过28.5个学分！");
					statusJLabel.setText("超出学分");
					statusJLabel.setForeground(Color.RED);
					return true;
				}

			}else {
				System.out.println(date + "该班级无空余位置！");
				statusJLabel.setText("无空余位置");
				return false;
			}

		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
}
