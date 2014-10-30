package com.foresee.gt3kf.sjtb.util;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Collection;
import java.util.HashSet;

/**
 * 文件操作通用类
 * 
 * @author ZMM
 */
public class FileUtil {

	/** 日志 **/
	private static Logger logger = Logger.getLogger(FileUtil.class);
	
	/**
	 * 判断指定路径文件或目录是否存在
	 * 
	 * @param strPath
	 *            文件全路径（含文件名）/目录全路径
	 * @return boolean
	 */
	public static boolean getState(String strPath) {
		boolean blnResult = false;
		File file = null;
		try {
			file = new File(strPath);
			if (file.exists() || file.isFile()) {
				blnResult = true;
			}
		} catch (Exception e) {
			logger.info("类:FileUtil,方法:getState,信息:路径有误，" + strPath);
		} finally {
			file = null;
		}
		// 释放对象
		strPath = null;
		return blnResult;
	}

	/**
	 * 读取文件到字符串中
	 * 
	 * 
	 * @param strFilePath
	 *            文件全路径(含文件名)
	 * @param strCoding
	 *            编码格式
	 * @return String
	 */
	public static String fileToString(String strFilePath, String strCoding) {
		StringBuffer strBuffResult = new StringBuffer();
		int i = 0;
		if (strCoding == null || strCoding.trim().length() <= 0) {
			strCoding = "UTF-8";
		}
		BufferedReader bufferedReader = null;
		try {
			if (strCoding == null || strCoding.trim().length() <= 0) {
				bufferedReader = new BufferedReader(new InputStreamReader(
						new FileInputStream(strFilePath)));
			} else {
				bufferedReader = new BufferedReader(new InputStreamReader(
						new FileInputStream(strFilePath), strCoding));
			}
			while ((i = bufferedReader.read()) != -1) {
				strBuffResult.append((char) i);
			}
			bufferedReader.close();
		} catch (Exception ex) {
			logger.info("类:FileUtil,方法:fileToString,信息:" + ex);
		} finally {
			bufferedReader = null;
		}
		// 释放对象
		strCoding = null;
		strFilePath = null;
		return strBuffResult.toString();
	}

	/**
	 * 将字符串写入到文件中
	 * 
	 * @param strContent
	 *            字符串内容
	 * 
	 * @param strFilePath
	 *            文件全路径(含文件名)
	 * @param strCoding
	 *            编码格式,默认：UTF-8
	 * @return boolean
	 */
	public static boolean stringToFile(String strContent, String strFilePath,
			String strCoding) {
		boolean blnResult = false;
		if (strCoding == null || strCoding.trim().length() <= 0) {
			strCoding = "UTF-8";
		}
		FileOutputStream fileOutputStream = null; // 文件输出对象
		Writer writer = null;
		try {
			fileOutputStream = new FileOutputStream(strFilePath);
			if (strCoding == null || strCoding.trim().length() <= 0) {
				writer = new OutputStreamWriter(fileOutputStream);
			} else {
				writer = new OutputStreamWriter(fileOutputStream, strCoding);
			}
			writer.write(strContent);
			writer.flush();
			
			blnResult = true;
		} catch (Exception ex) {
			logger.info("类:FileUtil；方法:stringToFile；信息:" + ex);
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fileOutputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			writer = null;
			fileOutputStream = null;
		}
		// 释放对象
		strCoding = null;
		strContent = null;
		strFilePath = null;
		return blnResult;
	}

	/**
	 * 将二进制文件写入磁盘
	 * 
	 * @param file
	 *            二进制文件内容
	 * 
	 * 
	 * 
	 * @param strFilePath
	 *            文件全路径(含文件名)
	 * 
	 * @return boolean
	 */
	public static boolean byteToFile(byte[] file, String strFilePath) {
		boolean blnResult = false;
		File tmp_file = null;
		FileOutputStream fileOutputStream = null; // 文件输出对象
		try {
			// 检查该文件是否存在
			tmp_file = new File(strFilePath);
			if (tmp_file.exists()) {
				tmp_file.delete();
			}

			fileOutputStream = new FileOutputStream(strFilePath);

			fileOutputStream.write(file);		
			blnResult = true;
		} catch (Exception ex) {
			logger.info("类:FileUtil；方法:byteToFile；信息:" + ex);
		} finally {
			try {
				fileOutputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fileOutputStream = null;
		}
		// 释放对象

		strFilePath = null;
		return blnResult;
	}

	/**
	 * 新建目录
	 * 
	 * @param strFolderPath
	 *            目录路径（含要创建的目录名称）
	 * 
	 * @return boolean
	 */
	public static boolean createFolder(String strFolderPath) {
		boolean blnResult = true;
		File file = null;
		if (strFolderPath != null && strFolderPath.trim().length() > 0) {
			try {
				file = new File(strFolderPath);
				if (!file.exists()) {
					blnResult = file.mkdirs();
				}
			} catch (Exception e) {
				logger.info("类:FileUtil,方法:createFolder,信息:创建目录操作出错,"
						+ strFolderPath);
				blnResult = false;
			} finally {
				file = null;
			}
		}
		// 释放对象
		strFolderPath = null;
		return blnResult;
	}

	/**
	 * 复制整个文件夹的内容
	 * 
	 * @param strOldFolderPath
	 *            准备拷贝的目录
	 * 
	 * @param strNewFolderPath
	 *            指定绝对路径的新目录
	 * @return void
	 */
	public static void copyFolder(String strOldFolderPath,
			String strNewFolderPath) {
		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;
		File file = null;
		String[] strArrayFile = null;
		File fileTemp = null;
		byte[] byteArray = null;
		int intIndex = 0;
		try {
			new File(strNewFolderPath).mkdirs(); // 如果文件夹不存在 则建立新文件夹

			file = new File(strOldFolderPath);
			strArrayFile = file.list();
			for (int i = 0; i < strArrayFile.length; i++) {
				if (strOldFolderPath.endsWith(File.separator)) {
					fileTemp = new File(strOldFolderPath + strArrayFile[i]);
				} else {
					fileTemp = new File(strOldFolderPath + File.separator
							+ strArrayFile[i]);
				}
				if (fileTemp.isFile() && (!fileTemp.isHidden())) {
					fileInputStream = new FileInputStream(fileTemp);
					fileOutputStream = new FileOutputStream(strNewFolderPath
							+ "/" + (fileTemp.getName()).toString());
					byteArray = new byte[1024 * 5];
					while ((intIndex = fileInputStream.read(byteArray)) != -1) {
						fileOutputStream.write(byteArray, 0, intIndex);
					}
					fileOutputStream.flush();
					fileOutputStream.close();
					fileInputStream.close();
					intIndex = 0;
				}
				if (fileTemp.isDirectory() && (!fileTemp.isHidden())) {// 如果是子文件夹

					copyFolder(strOldFolderPath + File.separator
							+ strArrayFile[i], strNewFolderPath
							+ File.separator + strArrayFile[i]);
				}
			}
		} catch (Exception e) {
			logger.info("类:FileUtil,方法:copyFolder,信息:复制整个文件夹内容操作出错," + e);
		} finally {
			fileInputStream = null;
			fileOutputStream = null;
			file = null;
			fileTemp = null;
			byteArray = null;
		}
		// 释放对象
		strArrayFile = null;
		strNewFolderPath = null;
		strOldFolderPath = null;
	}

	/**
	 * 复制单个文件
	 * 
	 * @param strOldFilePath
	 *            准备复制的文件源
	 * @param strNewFilePath
	 *            拷贝到新绝对路径带文件名
	 * @return boolean
	 */
	public static boolean copyFile(String strOldFilePath, String strNewFilePath) {
		boolean blnResult = false;
		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;
		File file = null;
		byte[] byteArray = null;
		int intIndex = 0;
		try {
			fileInputStream = new FileInputStream(strOldFilePath);
			file = new File(strNewFilePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			fileOutputStream = new FileOutputStream(file);
			byteArray = new byte[1024];
			while ((intIndex = fileInputStream.read(byteArray)) != -1) {
				for (int i = 0; i < intIndex; i++)
					fileOutputStream.write(byteArray[i]);
			}
			intIndex = 0;
			fileInputStream.close();
			fileOutputStream.close();
			blnResult = true;
		} catch (Exception e) {
			logger.info("类:FileUtil,方法:copyFile,信息:被拷贝文件不存在!" + e);
			e.printStackTrace();
		} finally {
			fileInputStream = null;
			fileOutputStream = null;
			file = null;
			byteArray = null;
		}
		// 释放对象
		strNewFilePath = null;
		strOldFilePath = null;
		return blnResult;
	}

	/**
	 * 删除文件
	 * 
	 * @param strFilePath
	 *            文件全路径（含文件名）
	 * 
	 * @return boolean
	 */
	public static boolean delFile(String strFilePath) {
		boolean blnResult = false;
		File file = null;
		if (strFilePath != null && strFilePath.trim().length() > 0) {
			try {
				file = new File(strFilePath);
				if (file.exists()) {
					file.delete();
					blnResult = true;
				} else {
					logger.info("类:FileUtil,方法:delFile,信息:被文件不存在,"
							+ strFilePath);
				}
			} catch (Exception e) {
				logger.info("类:FileUtil,方法:delFile,信息:删除文件有误," + e);
			} finally {
				file = null;
			}
		} else {
			logger.info("类:FileUtil,方法:delFile,"
					+ "信息:strFilePath = null.");
		}
		// 释放对象
		strFilePath = null;
		return blnResult;
	}

	/**
	 * 删除文件夹
	 * 
	 * 
	 * @param strFolderPath
	 *            文件夹完整绝对路径
	 * 
	 * @return void
	 */
	public static void delFolder(String strFolderPath) {
		File file = null;
		if (strFolderPath != null && strFolderPath.trim().length() > 0) {
			try {
				delAllFile(strFolderPath); // 删除完里面所有内容

				file = new File(strFolderPath);
				file.delete(); // 删除空文件夹
			} catch (Exception e) {
				logger.info("类:FileUtil,方法:delFolder,信息:删除目录有误," + e);
			} finally {
				file = null;
			}
		} else {
			logger.info("类:FileUtil,方法:delFolder,"
					+ "信息:strFolderPath=null");
		}
		// 释放对象
		strFolderPath = null;
	}

	/**
	 * 删除指定文件夹下所有文件及目录
	 * 
	 * @param strFolderPath
	 *            文件夹完整绝对路径
	 * 
	 * @return boolean
	 */
	public static boolean delAllFile(String strFolderPath) {
		boolean blnResult = false;
		int intFileCount = 0;
		String[] strArrayFile = null;
		File file = new File(strFolderPath);
		if (file.exists() && file.isDirectory()) {
			strArrayFile = file.list();
			if (strArrayFile == null || strArrayFile.length <= 0) {
				blnResult = true;
			} else {
				intFileCount = strArrayFile.length;
				if (intFileCount > 0) {
					for (int i = 0; i < intFileCount; i++) {
						if (strFolderPath.endsWith(File.separator)) {
							file = new File(strFolderPath + strArrayFile[i]);
						} else {
							file = new File(strFolderPath + File.separator
									+ strArrayFile[i]);
						}
						if (file.isFile()) {
							file.delete();
						}
						if (file.isDirectory()) {
							delAllFile(strFolderPath + File.separator
									+ strArrayFile[i]);// 先删除文件夹里面的文件

							delFolder(strFolderPath + File.separator
									+ strArrayFile[i]);// 再删除空文件夹

						}
						blnResult = true;
					}
				} else {
					blnResult = true;
				}
			}
		} else {
			logger.info("类:FileUtil,方法:delAllFile,信息:删除文件目录有误,"
					+ strFolderPath);
		}
		// 释放对象
		strFolderPath = null;
		strArrayFile = null;
		file = null;
		return blnResult;
	}

	/**
	 * 获得一个目录下面所有文件
	 * 
	 * 
	 * @param strFolderPath
	 *            文件夹完整绝对路径
	 * 
	 * @return String[]
	 */
	public static String[] getAllFile(String strFolderPath) {
		String[] strArrayResult = null;
		File file = null;
		if (strFolderPath != null && strFolderPath.trim().length() > 0) {
			file = new File(strFolderPath);
			if (getState(strFolderPath) && file.isDirectory()) {
				strArrayResult = file.list();
			}
			if (strArrayResult == null || strArrayResult.length <= 0
					|| strArrayResult[0].trim().length() <= 0) {
				strArrayResult = null;
			}
		} else {
			logger.info("类:FileUtil,方法:getAllFile,"
					+ "信息:strFolderPath=null");
		}
		// 释放对象
		file = null;
		strFolderPath = null;
		return strArrayResult;
	}

	/**
	 * 获得指定目录下所有的一级目录
	 * 
	 * 
	 * @param strFolderPath
	 *            指定路径名
	 * 
	 * @return File[]
	 */
	public static File[] getAllFolder(String strFolderPath) {
		File file = null;
		File[] fileArray = null;
		if (strFolderPath != null && strFolderPath.trim().length() > 0) {
			file = new File(strFolderPath);
			fileArray = file.listFiles();
			if (fileArray == null || fileArray.length <= 0) {
				fileArray = null;
			}
		} else {
			logger.info("类:FileUtil,方法:getAllFolder,"
					+ "信息:strFolderPath=null");
		}
		// 释放对象
		file = null;
		strFolderPath = null;
		return fileArray;
	}

	/**
	 * 获取目录下所有文件包括子文件夹
	 * 
	 * @param basePath
	 *            根目录
	 * @param filter
	 *            过滤条件，使用','分割
	 * @param container
	 *            符合条件的集合
	 * @return
	 */
	public static Collection<File> getAllFiles(final String basePath,
			final String filter, final Collection<File> container) {
		if (basePath != null && !"".equals(basePath) && container != null) {
			File file = new File(basePath);
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (File f : files) {
					getAllFiles(f.getAbsolutePath(), filter, container);
				}
			} else {
				String[] filters = filter.split(",");
				for (String strFilter : filters) {
					String fName = file.getAbsolutePath().toLowerCase();
					String strFilterLowCase = strFilter.trim().toLowerCase();
					boolean isSubFix = strFilterLowCase.startsWith(".");
					boolean isContains = fName.contains(strFilterLowCase);
					if (isSubFix) {
						isContains = false;
					}
					if (isSubFix && fName.endsWith(strFilterLowCase)
							|| isContains) {
						container.add(file);
						break;
					}
				}
			}
		}
		return container;
	}

	/**
	 * 获取目录下所有文件包括子文件夹
	 * 
	 * @param basePath
	 */
	public static Collection<File> getAllFiles(final String basePath,
			final String filter) {
		return getAllFiles(basePath, filter, new HashSet<File>());
	}

	/**
	 * 根据路径创建文件
	 * 
	 * @param filePath
	 *            文件路径+文件名
	 * @return boolean
	 */
	public static boolean createFileByPath(String filePath) {
		boolean result = false;
		try {
			File file = new File(filePath);
			result = file.createNewFile();
		} catch (IOException e) {
			logger.info("类:FileUtil,方法:createFileByPath," + "信息:"
					+ filePath + " 文件创建失败，原因" + e.getMessage());
		}
		return result;
	}
}
