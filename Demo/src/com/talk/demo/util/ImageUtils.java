package com.talk.demo.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {
	
	private static int DISPLAY_WIDTH = 480;
	private static int DISPLAY_HEIGHT = 480;
	
	public static void saveMyBitmap(String oring,String bitName) throws IOException {
		File originalFile = new File(oring);
		Bitmap bmp = decodeFile(originalFile);
		File f = new File(oring.substring(0, oring.length()-4)+"_thumb.jpg");
		f.createNewFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		bmp.compress(Bitmap.CompressFormat.JPEG, 30, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// decodes image and scales it to reduce memory consumption
	private static Bitmap decodeFile(File f) {
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);
			// The new size we want to scale to
			final int REQUIRED_HEIGHT = 800;
			final int REQUIRED_WIDTH = 480;
			// Find the correct scale value. It should be the power of 2.
			int width_tmp = o.outWidth, height_tmp = o.outHeight;

			System.out.println(width_tmp + "  " + height_tmp);
			Log.w("===", (width_tmp + "  " + height_tmp));

			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_WIDTH
						&& height_tmp / 2 < REQUIRED_HEIGHT)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale++;

				Log.w("===", scale + "''" + width_tmp + "  " + height_tmp);
			}
			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 显示缩略图的代码
	public static Bitmap decodeBitmap(String path) {
		BitmapFactory.Options op = new BitmapFactory.Options();
		op.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(path, op); // 获取尺寸信息
		// 获取比例大小
		int wRatio = (int) Math.ceil(op.outWidth / DISPLAY_WIDTH);
		int hRatio = (int) Math.ceil(op.outHeight / DISPLAY_HEIGHT);
		// 如果超出指定大小，则缩小相应的比例
		if (wRatio > 1 && hRatio > 1) {
			if (wRatio > hRatio) {
				op.inSampleSize = wRatio;
			} else {
				op.inSampleSize = hRatio;
			}
		}
		op.inJustDecodeBounds = false;
		bmp = BitmapFactory.decodeFile(path, op);
		return bmp;
	}
	
	public static Drawable decodeDrawable(String path) {
		BitmapFactory.Options op = new BitmapFactory.Options();
		Bitmap rawBitmap = BitmapFactory.decodeFile(path, op); // 获取尺寸信息
		//————>以下为将图片高宽和的大小kB压缩
		// 得到图片原始的高宽
		int rawHeight = op.outHeight;
		int rawWidth = op.outWidth;
		// 设定图片新的高宽
		int newHeight = 400;
		int newWidth = 400;
		// 计算缩放因子
		float heightScale = ((float) newHeight) / rawHeight;
		heightScale = heightScale>1?heightScale:1;
		float widthScale = ((float) newWidth) / rawWidth;
		widthScale = widthScale>1?widthScale:1;
		// 新建立矩阵
		Matrix matrix = new Matrix();
		matrix.postScale(heightScale, widthScale);
		// 设置图片的旋转角度
		//matrix.postRotate(-30);
		// 设置图片的倾斜
		//matrix.postSkew(0.1f, 0.1f);
        //将图片大小压缩
		//压缩后图片的宽和高以及kB大小均会变化
		Bitmap newBitmap = Bitmap.createBitmap(rawBitmap, 0, 0, rawWidth,rawWidth, matrix, true);
		// 将Bitmap转换为Drawable
		Drawable newBitmapDrawable = new BitmapDrawable(newBitmap);	
		//imageView.setImageDrawable(newBitmapDrawable);
		return newBitmapDrawable;
	}
}
