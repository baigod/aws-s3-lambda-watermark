package me.douboo.aws.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;

import me.douboo.aws.WaterMark;

public class WaterMarkTests {
	@Test
	public void testTextWaterMark() throws IOException {
		File file = new File("C:\\Users\\Administrator\\Pictures\\test.jpg");
		byte[] textWaterMark = WaterMark.textWaterMark(new FileInputStream(file), "FOR KYC ONLY");
		OutputStream os = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\新建文件夹\\test1.jpg");
		os.write(textWaterMark);
		os.close();
	}

	@Test
	public void testMultipleTextWaterMark() throws IOException {
		File file = new File("C:\\Users\\Administrator\\Pictures\\test.jpg");
		byte[] textWaterMark = WaterMark.multipleTextWaterMark(new FileInputStream(file), "KYC IMG");
		OutputStream os = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\新建文件夹\\test1.jpg");
		os.write(textWaterMark);
		os.close();
	}
}
