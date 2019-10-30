package pojo;

import java.util.List;

/**
 * @author Lucien
 * @date 2019/06/21 17:16
 * @description
 */

public class TxtInfo {
	private static List<List<String>> info;

	public TxtInfo() {

	}

	public static List<List<String>> getInfo() {
		return info;
	}

	public static void setInfo(List<List<String>> info) {
		TxtInfo.info = info;
	}
}
