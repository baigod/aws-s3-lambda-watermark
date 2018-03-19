package me.douboo.aws;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class WaterMark {
	private static final String FONT_NAME = "微软雅黑";
	private static final int FONT_STYLE = Font.BOLD;
	private static final int FONT_SIZE = 60;
	private static final Color FONT_COLOR = Color.black;
	private static final float ALPHA = 0.2F;

	/**
	 * 添加单条文字水印
	 * 
	 * @param is
	 * @param markText
	 * @return
	 */
	public static byte[] textWaterMark(InputStream is, String markText) {
		ByteArrayOutputStream os = null;
		int X = 636;
		int Y = 700;

		try {
			Image image = ImageIO.read(is);
			// 计算原始图片宽度长度
			int width = image.getWidth(null);
			int height = image.getHeight(null);
			// 创建图片缓存对象
			BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			// 创建java绘图工具对象
			Graphics2D graphics2d = bufferedImage.createGraphics();
			// 参数主要是，原图，坐标，宽高
			graphics2d.drawImage(image, 0, 0, width, height, null);
			graphics2d.setFont(new Font(FONT_NAME, FONT_STYLE, FONT_SIZE));
			graphics2d.setColor(FONT_COLOR);

			// 使用绘图工具将水印绘制到图片上
			// 计算文字水印宽高值
			int waterWidth = FONT_SIZE * getTextLength(markText);
			int waterHeight = FONT_SIZE;
			// 计算水印与原图高宽差
			int widthDiff = width - waterWidth;
			int heightDiff = height - waterHeight;
			// 水印坐标设置
			if (X > widthDiff) {
				X = widthDiff;
			}
			if (Y > heightDiff) {
				Y = heightDiff;
			}
			// 水印透明设置
			graphics2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, ALPHA));
			graphics2d.drawString(markText, X, Y + FONT_SIZE);
			graphics2d.dispose();

			os = new ByteArrayOutputStream();

			// 使用图像编码工具类，输出缓存图像到目标文件
			ImageIO.write(bufferedImage, "jpg", os);
			return os.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 添加单图片水印
	 * 
	 * @param is
	 * @param imgRealPath
	 * @return
	 */
	public static byte[] imageWaterMark(InputStream is, String imgRealPath) {
		ByteArrayOutputStream os = null;
		int X = 636;
		int Y = 763;

		try {
			Image image = ImageIO.read(is);
			// 计算原始图片宽度长度
			int width = image.getWidth(null);
			int height = image.getHeight(null);
			// 创建图片缓存对象
			BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			// 创建java绘图工具对象
			Graphics2D graphics2d = bufferedImage.createGraphics();
			// 参数主要是，原图，坐标，宽高
			graphics2d.drawImage(image, 0, 0, width, height, null);
			graphics2d.setFont(new Font(FONT_NAME, FONT_STYLE, FONT_SIZE));
			graphics2d.setColor(FONT_COLOR);

			// 水印图片路径
			File logo = new File(imgRealPath);
			Image imageLogo = ImageIO.read(logo);
			int widthLogo = imageLogo.getWidth(null);
			int heightLogo = imageLogo.getHeight(null);
			int widthDiff = width - widthLogo;
			int heightDiff = height - heightLogo;
			// 水印坐标设置
			if (X > widthDiff) {
				X = widthDiff;
			}
			if (Y > heightDiff) {
				Y = heightDiff;
			}
			// 水印透明设置
			graphics2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, ALPHA));
			graphics2d.drawImage(imageLogo, X, Y, null);

			graphics2d.dispose();
			os = new ByteArrayOutputStream();

			// 使用图像编码工具类，输出缓存图像到目标文件
			ImageIO.write(bufferedImage, "jpg", os);

			return os.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 添加多条文字水印
	 * 
	 * @param is
	 * @param markText
	 * @return
	 */
	public static byte[] multipleTextWaterMark(InputStream is, String markText) {
		ByteArrayOutputStream os = null;
		try {
			Image image = ImageIO.read(is);
			// 计算原始图片宽度长度
			int width = image.getWidth(null);
			int height = image.getHeight(null);
			// 创建图片缓存对象
			BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			// 创建java绘图工具对象
			Graphics2D graphics2d = bufferedImage.createGraphics();
			// 参数主要是，原图，坐标，宽高
			graphics2d.drawImage(image, 0, 0, width, height, null);
			graphics2d.setFont(new Font(FONT_NAME, FONT_STYLE, FONT_SIZE));
			graphics2d.setColor(FONT_COLOR);

			// 使用绘图工具将水印绘制到图片上
			// 计算文字水印宽高值
			int waterWidth = FONT_SIZE * getTextLength(markText);
			int waterHeight = FONT_SIZE;

			// 水印透明设置
			graphics2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, ALPHA));
			graphics2d.rotate(Math.toRadians(30), bufferedImage.getWidth() / 2, bufferedImage.getHeight() / 2);

			int x = -width / 2;
			int y = -height / 2;

			while (x < width * 1.5) {
				y = -height / 2;
				while (y < height * 1.5) {
					graphics2d.drawString(markText, x, y);
					y += waterHeight + 100;
				}
				x += waterWidth + 100;
			}
			graphics2d.dispose();

			os = new ByteArrayOutputStream();

			// 使用图像编码工具类，输出缓存图像到目标文件
			ImageIO.write(bufferedImage, "jpg", os);

			return os.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 多图片水印
	 * 
	 * @param is
	 * @param imgRealPath
	 * @return
	 */
	public static byte[] multipleImageWaterMark(InputStream is, String imgRealPath) {
		ByteArrayOutputStream os = null;

		try {
			Image image = ImageIO.read(is);
			// 计算原始图片宽度长度
			int width = image.getWidth(null);
			int height = image.getHeight(null);
			// 创建图片缓存对象
			BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			// 创建java绘图工具对象
			Graphics2D graphics2d = bufferedImage.createGraphics();
			// 参数主要是，原图，坐标，宽高
			graphics2d.drawImage(image, 0, 0, width, height, null);
			graphics2d.setFont(new Font(FONT_NAME, FONT_STYLE, FONT_SIZE));
			graphics2d.setColor(FONT_COLOR);

			// 水印图片路径
			File logo = new File(imgRealPath);
			Image imageLogo = ImageIO.read(logo);
			int widthLogo = imageLogo.getWidth(null);
			int heightLogo = imageLogo.getHeight(null);

			// 水印透明设置
			graphics2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, ALPHA));

			graphics2d.rotate(Math.toRadians(30), bufferedImage.getWidth() / 2, bufferedImage.getHeight() / 2);

			int x = -width / 2;
			int y = -height / 2;

			while (x < width * 1.5) {
				y = -height / 2;
				while (y < height * 1.5) {
					graphics2d.drawImage(imageLogo, x, y, null);
					y += heightLogo + 100;
				}
				x += widthLogo + 100;
			}
			graphics2d.dispose();
			os = new ByteArrayOutputStream();

			// 使用图像编码工具类，输出缓存图像到目标文件
			ImageIO.write(bufferedImage, "jpg", os);

			return os.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	// 计算水印文本长度
	// 1、中文长度即文本长度 2、英文长度为文本长度二分之一
	public static int getTextLength(String text) {
		// 水印文字长度
		int length = text.length();

		for (int i = 0; i < text.length(); i++) {
			String s = String.valueOf(text.charAt(i));
			if (s.getBytes().length > 1) {
				length++;
			}
		}
		length = length % 2 == 0 ? length / 2 : length / 2 + 1;
		return length;
	}
}
