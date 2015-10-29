package com.example.utils;

import com.example.mobilesafe.R;
import com.example.mobilesafe.R.id;

/**
 * 定义APP中所有全局常量
 * 
 * @author Administrator
 *
 */
public class AppConst {
	/**
	 * 更新对话框
	 */
	public static final int UPDATE_DIALOG = 0;
	/**
	 * URL异常
	 */
	public static final int URL_EXCEPTION = 1;
	/**
	 * 网络异常
	 */
	public static final int INTERNET_EXCEPTION = 2;
	/**
	 * JSON数据解析异常
	 */
	public static final int JSON_EXCEPTION = 3;
	/**
	 * 无可更新版本
	 */
	public static final int NO_UPDATE = 4;
	/**
	 * appinfo文件名
	 */
	public static final String APPINFO = "appinfo";/* appinfo文件名 */

	/**
	 * 来电显示Toast的资源ID
	 */
	private static int[] id = new int[] { R.drawable.toast_bc,
			R.drawable.toast_blue_bc, R.drawable.toast_green_bc,
			R.drawable.toast_orange_bc, R.drawable.toast_pink_bc,
			R.drawable.toast_purple, R.drawable.toast_red_bc };

	/**
	 * 设置中心的设置样式名称
	 */
	private static String[] items = new String[] { "默认(半透明)", "海洋蓝", "自然绿",
			"温暖橙", "魅力粉", "贵族紫", "艳纯红" };

	/**
	 * 获取给定位置的的图片资源id
	 * 
	 * @param whitch
	 *            给定的位置
	 * @return 给定位置的的图片资源id
	 */
	public static int getId(int whitch) {
		return id[whitch];
	}

	public static String[] getItem() {
		return items;

	}
}
