package com.zjedu.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.mysql.jdbc.StringUtils;
import com.zjedu.dao.ExcelObject;

public class ExcelReader {

	/**
	 * excel中的列数
	 */
	private final static int N = 10;

	public static void main(String[] args) throws Exception {
		File file = new File("ExcelDemo.xls");
		String[][] result = getData(file, 2);
		List<ExcelObject> excelObjects = geneExcelObject(result);
		for (ExcelObject excelObject : excelObjects) {
			System.out.println(excelObject);
		}
		// Map<String, Map<String, List<ExcelObject>>> ans =
		// geneExcelObjectMap(result);
		// for (Entry<String, Map<String, List<ExcelObject>>> out :
		// ans.entrySet()) {
		// System.out.println(out.getKey() + "==>");
		// for (Entry<String, List<ExcelObject>> in : out.getValue()
		// .entrySet()) {
		// System.out.println(in.getKey());
		// }
		// System.out.println("=====\n");
		// }
	}

	/**
	 * 根据从excel读取的内容构建ExcelObject
	 * 
	 * @param result
	 * @return
	 */
	public static List<ExcelObject> geneExcelObject(String[][] result) {
		List<ExcelObject> excelObjects = new ArrayList<ExcelObject>();
		String[] container = new String[N];
		for (String[] strings : result) {
			for (int i = 0; i < strings.length; i++) {
				if (!StringUtils.isEmptyOrWhitespaceOnly(strings[i]) && i < N) {
					container[i] = strings[i];
				}
			}
			ExcelObject curr = new ExcelObject(container[1], container[2],
					container[3], container[4], container[5], container[6],
					container[7], container[8], container[9]);
			excelObjects.add(curr);
		}
		return excelObjects;
	}

	/**
	 * 根据从excel读取的内容构建一个二级目录结构的map
	 */
	public static Map<String, Map<String, List<ExcelObject>>> geneExcelObjectMap(
			String[][] result) {
		Map<String, Map<String, List<ExcelObject>>> ans = new LinkedHashMap<String, Map<String, List<ExcelObject>>>();
		Map<String, List<ExcelObject>> currMap = null;
		List<ExcelObject> currList = null;
		String[] container = new String[N];
		for (String[] strings : result) {
			// 功能组
			if (!StringUtils.isEmptyOrWhitespaceOnly(strings[2])) {
				currMap = new LinkedHashMap<String, List<ExcelObject>>();
				ans.put(strings[2], currMap);
			}

			// 菜单分组
			if (!StringUtils.isEmptyOrWhitespaceOnly(strings[4])) {
				currList = new ArrayList<ExcelObject>();
				currMap.put(strings[4], currList);
			}
			for (int i = 0; i < strings.length; i++) {
				if (!StringUtils.isEmptyOrWhitespaceOnly(strings[i]) && i < N) {
					container[i] = strings[i];
				}
			}
			ExcelObject curr = new ExcelObject(container[1], container[2],
					container[3], container[4], container[5], container[6],
					container[7], container[8], container[9]);
			currList.add(curr);
		}
		return ans;
	}

	/**
	 * 
	 * 读取Excel的内容，第一维数组存储的是一行中格列的值，二维数组存储的是多少个行
	 * 
	 * @param file
	 *            读取数据的源Excel
	 * @param ignoreRows
	 *            读取数据忽略的行数，比喻行头不需要读入 忽略的行数为1
	 * @return 读出的Excel中数据的内容
	 * @throws FileNotFoundException
	 * @throws IOException
	 */

	public static String[][] getData(File file, int ignoreRows)
			throws FileNotFoundException, IOException {
		List<String[]> result = new ArrayList<String[]>();
		int rowSize = 0;
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				file));
		// 打开HSSFWorkbook
		POIFSFileSystem fs = new POIFSFileSystem(in);
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFCell cell = null;
		for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
			HSSFSheet st = wb.getSheetAt(sheetIndex);
			// 第一行为标题，不取
			for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {
				HSSFRow row = st.getRow(rowIndex);
				if (row == null) {
					continue;
				}
				int tempRowSize = row.getLastCellNum() + 1;
				if (tempRowSize > rowSize) {
					rowSize = tempRowSize;
				}
				String[] values = new String[rowSize];
				Arrays.fill(values, "");
				boolean hasValue = false;
				for (short columnIndex = 0; columnIndex <= row.getLastCellNum(); columnIndex++) {
					String value = "";
					cell = row.getCell(columnIndex);
					if (cell != null) {
						// 注意：一定要设成这个，否则可能会出现乱码
						cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						switch (cell.getCellType()) {
						case HSSFCell.CELL_TYPE_STRING:
							value = cell.getStringCellValue();
							break;
						case HSSFCell.CELL_TYPE_NUMERIC:
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								Date date = cell.getDateCellValue();
								if (date != null) {
									value = new SimpleDateFormat("yyyy-MM-dd")
											.format(date);
								} else {
									value = "";
								}
							} else {
								value = new DecimalFormat("0").format(cell
										.getNumericCellValue());
							}
							break;
						case HSSFCell.CELL_TYPE_FORMULA:
							// 导入时如果为公式生成的数据则无值
							if (!cell.getStringCellValue().equals("")) {
								value = cell.getStringCellValue();
							} else {
								value = cell.getNumericCellValue() + "";
							}
							break;
						case HSSFCell.CELL_TYPE_BLANK:
							break;
						case HSSFCell.CELL_TYPE_ERROR:
							value = "";
							break;
						case HSSFCell.CELL_TYPE_BOOLEAN:
							value = (cell.getBooleanCellValue() == true ? "Y"
									: "N");
							break;
						default:
							value = "";
						}
					}

					// 这里可能导致：如果第一列没有数据，此行中后面的数据会被忽略
					// if (columnIndex == 0 && value.trim().equals("")) {
					// break;
					// }
					value = format(value);
					values[columnIndex] = rightTrim(value);
					hasValue = true;
				}
				if (hasValue) {
					result.add(values);
				}
			}
		}
		in.close();
		String[][] returnArray = new String[result.size()][rowSize];
		for (int i = 0; i < returnArray.length; i++) {
			returnArray[i] = (String[]) result.get(i);
		}
		return returnArray;
	}

	/**
	 * 
	 * 去掉字符串右边的空格
	 * 
	 * @param str
	 *            要处理的字符串
	 * 
	 * @return 处理后的字符串
	 */

	public static String rightTrim(String str) {
		if (str == null) {
			return "";
		}
		int length = str.length();
		for (int i = length - 1; i >= 0; i--) {
			if (str.charAt(i) != 0x20) {
				break;
			}
			length--;
		}
		return str.substring(0, length);
	}

	public static String format(String orig) {
		orig = orig.replaceAll("\r", "");
		orig = orig.replaceAll("\n", "");
		orig = orig.replaceAll("\t", "");
		return orig;
	}
}
