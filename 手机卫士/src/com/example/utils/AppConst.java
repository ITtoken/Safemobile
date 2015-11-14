package com.example.utils;

import android.content.Context;
import android.os.Environment;

import com.example.mobilesafe.R;
import com.example.mobilesafe.R.id;

/**
 * ����APP������ȫ�ֳ���
 * 
 * @author Administrator
 *
 */
public class AppConst {
	/**
	 * ���¶Ի���
	 */
	public static final int UPDATE_DIALOG = 0;
	/**
	 * URL�쳣
	 */
	public static final int URL_EXCEPTION = 1;
	/**
	 * �����쳣
	 */
	public static final int INTERNET_EXCEPTION = 2;
	/**
	 * JSON���ݽ����쳣
	 */
	public static final int JSON_EXCEPTION = 3;
	/**
	 * �޿ɸ��°汾
	 */
	public static final int NO_UPDATE = 4;
	/**
	 * appinfo�ļ���
	 */
	public static final String APPINFO = "appinfo";/* appinfo�ļ��� */

	/**
	 * �ֻ���Ļ���
	 */
	public static final String SCREEN_WIDTH = "screen_width";
	/**
	 * �ֻ���Ļ�߶�
	 */
	public static final String SCREEN_HEIGHT = "screen_height";

	/**
	 * ����λ��:SD��
	 */
	public static final String BACKUP_SDCARD = Environment
			.getExternalStorageDirectory() + "/MobilePhone";
	/**
	 * ����λ��:�ֻ��ڴ�
	 */
	public static final String BACKUP_INNER = " /data/data/com.example.mobilesafe"
			+ "/files/MobilePhone";

	/**
	 * ������ʾToast����ԴID
	 */
	private static int[] id = new int[] { R.drawable.toast_bc,
			R.drawable.toast_blue_bc, R.drawable.toast_green_bc,
			R.drawable.toast_orange_bc, R.drawable.toast_pink_bc,
			R.drawable.toast_purple, R.drawable.toast_red_bc };

	/**
	 * �������ĵ�������ʽ����
	 */
	private static String[] items = new String[] { "Ĭ��(��͸��)", "������", "��Ȼ��",
			"��ů��", "������", "������", "�޴���" };

	/**
	 * ��ȡ����λ�õĵ�ͼƬ��Դid
	 * 
	 * @param whitch
	 *            ������λ��
	 * @return ����λ�õĵ�ͼƬ��Դid
	 */
	public static int getId(int whitch) {
		return id[whitch];
	}

	public static String[] getItem() {
		return items;

	}
}
