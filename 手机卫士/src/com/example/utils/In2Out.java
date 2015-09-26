package com.example.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class In2Out {

	/**
	 * 获取输入流中的字符串
	 * 
	 * @param in
	 *            输入流
	 * @return 输入流中的字符串内容
	 * @throws IOException
	 */
	public static String getInputstreamInfo(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte b[] = new byte[1024];
		int len = 0;
		while ((len = in.read(b)) != -1) {
			out.write(b, 0, len);
		}
		String result = out.toString();
		out.close();
		return result;
	}

	/**
	 * 文件复制
	 * 
	 * @param sourcePath
	 *            文件源路径
	 * @param destPath
	 *            要复制的目标路径
	 */
	public static void fileCopy(String sourcePath, String destPath) {
		FileInputStream in;
		FileOutputStream out;
		try {
			in = new FileInputStream(sourcePath);
			out = new FileOutputStream(destPath);

			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 文件复制
	 * 
	 * @param sourcePath
	 *            文件源路径
	 * @param destPath
	 *            要复制的目标路径
	 */
	public static void fileCopy(InputStream in, String destPath) {
		FileOutputStream out;
		try {
			out = new FileOutputStream(destPath);

			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
