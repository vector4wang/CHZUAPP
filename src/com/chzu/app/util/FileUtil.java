package com.chzu.app.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;

public class FileUtil
{
	public static String filePath = android.os.Environment.getExternalStorageDirectory() + "/CHZUDownLoad";

	public static String getFileName(String str)
	{
		// 去除url中的符号作为文件名返回
		str = str.replaceAll("(?i)[^a-zA-Z0-9\u4E00-\u9FA5]", "");
		System.out.println("filename = " + str);
		return str + ".png";
	}

	public static void writeSDcard(String fileName, InputStream inputStream)
	{
		try
		{
			File file = new File(filePath);
			if (!file.exists())
			{
				file.mkdirs();
			}

			FileOutputStream fileOutputStream = new FileOutputStream(filePath + "/" + fileName);
			byte[] buffer = new byte[512];
			int count = 0;
			while ((count = inputStream.read(buffer)) > 0)
			{
				fileOutputStream.write(buffer, 0, count);
			}
			fileOutputStream.flush();
			fileOutputStream.close();
			inputStream.close();
			System.out.println("writeToSD success");
		} catch (IOException e)
		{
			e.printStackTrace();
			System.out.println("writeToSD fail");
		}
	}

	public static boolean writeSDcard(String fileName, Bitmap bmp)
	{
		try
		{
			File file = new File(filePath);

			if (!file.exists())
			{
				file.mkdirs();
			}
			File imgFile = new File(filePath + "/" + getFileName(fileName));
			if (imgFile.exists())
			{
				return true;
			}
			InputStream is = bitmap2InputStream(bmp);
			FileOutputStream fileOutputStream = new FileOutputStream(imgFile);
			byte[] buffer = new byte[512];
			int count = 0;
			while ((count = is.read(buffer)) > 0)
			{
				fileOutputStream.write(buffer, 0, count);
			}
			fileOutputStream.flush();
			fileOutputStream.close();
			is.close();
			System.out.println("writeToSD success");
		} catch (IOException e)
		{
			e.printStackTrace();
			System.out.println("writeToSD fail");
			return false;
		}
		return true;
	}

	// Bitmap转换成byte[]
	public static byte[] bitmap2Bytes(Bitmap bm)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	// 将Bitmap转换成InputStream
	public static InputStream bitmap2InputStream(Bitmap bm)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// Write a compressed version of the bitmap to the specified
		// outputstream.
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}
}
