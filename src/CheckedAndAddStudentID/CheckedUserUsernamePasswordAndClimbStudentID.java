package CheckedAndAddStudentID;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import pojo.User;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CheckedUserUsernamePasswordAndClimbStudentID {
	public static String CheckedUserUsernamePasswordAndClimbStudentID(User user, JLabel statusJLabel){

		String username = user.getUserName().trim().replace("\uFEFF", "");
		String password = user.getPassWord().trim();


		/**
		 * 当前时间
		 */
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = df.format(new Date());
		date += " " + username + ": ";

		try {
			//暂停5S，防止时间间隔过短被封IP
//			Thread.sleep(5 * 1000);

			//学生ID
			String studentID = null;
			// 登陆 Url
			String loginUrl = "http://class.sise.com.cn:7001/sise/login_check_login.jsp";
			// 需登陆后访问的 Url，爬取学生ID的URL
			String HomeUrl = "http://class.sise.com.cn:7001/sise/module/student_states/student_select_class/main.jsp";
			//选课选班链接的URL
			String chooseClassUrl = "http://class.sise.com.cn:7001/sise/module/select_class/studentselectteam_item.jsp";


			//获取请求头的随机参数
			String requestHeadRandomData[] = QueryDataToLogin.queryDataToLogin();

			//new一个HTTPClient对象
			HttpClient httpClient = new HttpClient();
			// 模拟登陆，按实际服务器端要求选用 Post 或 Get 请求方式
			PostMethod postMethod = new PostMethod(loginUrl);

			// 设置登陆时要求的信息，用户名和密码
			NameValuePair[] data = {
					new NameValuePair(requestHeadRandomData[0], requestHeadRandomData[1]),
					new NameValuePair("random", requestHeadRandomData[2]),
					new NameValuePair("token", requestHeadRandomData[3]),
					new NameValuePair("username", username),
					new NameValuePair("password", password)};
			postMethod.setRequestBody(data);

			// 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
			httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
			int statusCode = httpClient.executeMethod(postMethod);
			String loginResponseBody = postMethod.getResponseBodyAsString();

			/**
			 * 判断是否为选课时间，不是则返回空
			 */
			if (loginResponseBody.contains("还没到你的选课时间,不能进入系统哦！")){
				System.out.println(date + "还没到你的选课时间,不能进入系统哦！");
				statusJLabel.setText("未到你的选课时间");
				statusJLabel.setForeground(Color.RED);
				return "";
			}
			// 获得登陆后的 Cookie
			Cookie[] cookies = httpClient.getState().getCookies();
			StringBuffer tmpcookies = new StringBuffer();
			for (Cookie c : cookies) {
				tmpcookies.append(c.toString() + ";");
			}

			// 进行登陆后的操作，跳转到信息页面
			//获取home页面
			GetMethod getMethodForHome = new GetMethod(HomeUrl);
			// 每次访问需授权的网址时需带上前面的 cookie 作为通行证
			// （httpClient客户端 会自动带上  如不是特殊要求一般不进行设置）
			getMethodForHome.setRequestHeader("cookie", tmpcookies.toString());
			// 你还可以通过 PostMethod/GetMethod 设置更多的请求后数据
			// 例如，referer 从哪里来的，UA 像搜索引擎都会表名自己是谁，无良搜索引擎除外
			getMethodForHome.setRequestHeader("Referer", "http://class.sise.com.cn:7001/sise/index.jsp");
			getMethodForHome.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.221 Safari/537.36 SE 2.X MetaSr 1.0");
			//发送执行
			httpClient.executeMethod(getMethodForHome);
			//将服务器返回的个人信息页面html文本保存在字符串中
			String homeHtml = getMethodForHome.getResponseBodyAsString();

			/**
			 * 模拟点击“选课选班”按钮操作
			 */
			GetMethod getMethod = new GetMethod(chooseClassUrl);
			// 每次访问需授权的网址时需带上前面的 cookie 作为通行证
			// （httpClient客户端 会自动带上  如不是特殊要求一般不进行设置）
			getMethod.setRequestHeader("cookie", tmpcookies.toString());
			// 你还可以通过 PostMethod/GetMethod 设置更多的请求后数据
			// 例如，referer 从哪里来的，UA 像搜索引擎都会表名自己是谁，无良搜索引擎除外
			getMethod.setRequestHeader("Referer", "http://class.sise.com.cn:7001/sise/index.jsp");
			getMethod.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.221 Safari/537.36 SE 2.X MetaSr 1.0");
			//发送执行
			httpClient.executeMethod(getMethod);
			//将服务器返回的个人信息页面html文本保存在字符串中
			String Html = getMethod.getResponseBodyAsString();


			//判断是否登录成功
			//!-1则找不到，既是登陆失败
			if (homeHtml.indexOf("选课选班") == -1) {
				System.out.println(date + "账号或密码错误！");
				statusJLabel.setText("账号密码错误");
				statusJLabel.setForeground(Color.RED);
				return "";
			}else {
				return tmpcookies.toString();
			}
		}catch (Throwable t){
			statusJLabel.setText("教务系统重启中，如若程序出错请重启");
			statusJLabel.setForeground(Color.RED);
			t.printStackTrace();
		}
		statusJLabel.setText("程序错误！！");
		statusJLabel.setForeground(Color.RED);
		return "";
	}
}
