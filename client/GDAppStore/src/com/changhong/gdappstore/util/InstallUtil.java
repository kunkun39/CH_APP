package com.changhong.gdappstore.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import android.util.Log;

public class InstallUtil {
	/**
	 * 静默安装（system/app下OK）
	 * 
	 * @param path
	 *            应用路径
	 * @return success为0,fail 为2
	 */
	public static int installApp(String filePath) {
		String[] args = { "pm", "install", "-r", filePath };
		return exeCommond(args);
	}

	/**
	 * 静默卸载应用
	 * 
	 * @param packageName
	 *            包名
	 * @return success为0,fail 为2
	 */
	public static int uninstallApp(String packageName) {
		String[] args = { "pm", "uninstall", packageName };
		return exeCommond(args);
	}

	private static int exeCommond(String[] args) {
		ProcessBuilder processBuilder = new ProcessBuilder(args);

		Process process = null;
		BufferedReader successResult = null;
		BufferedReader errorResult = null;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder errorMsg = new StringBuilder();
		int result;
		try {
			process = processBuilder.start();
			successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
			errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String s;

			while ((s = successResult.readLine()) != null) {
				successMsg.append(s);
			}

			while ((s = errorResult.readLine()) != null) {
				errorMsg.append(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
			result = 2;
		} catch (Exception e) {
			e.printStackTrace();
			result = 2;
		} finally {
			try {
				if (successResult != null) {
					successResult.close();
				}
				if (errorResult != null) {
					errorResult.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (process != null) {
				process.destroy();
			}
		}

		// TODO should add memory is not enough here
		if (successMsg.toString().contains("Success") || successMsg.toString().contains("success")) {
			result = 0;
		} else {
			result = 2;
		}
		Log.d("installSlient", "successMsg:" + successMsg + " result==" + result + ", ErrorMsg:" + errorMsg);
		return result;
	}
}
