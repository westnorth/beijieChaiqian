import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

//import jxl.write.biff.RowsExceededException;

public class ExcelUtils {

	/**
	 * 可以读出Excel文件中的两种数据 其中,objListData表示读入的是Object类型 strListData表示读入的是String类型
	 */
	public static List<Object[][]> objListData;
	public static List<String[][]> strListData;

	public static int[] nRow;
	public static int[] nColumn;

	/**
	 * 构造一个Excel对象
	 * 
	 * @param fileMain
	 *            文件对象
	 * 
	 */

	public int getRow(int n) {
		return nRow[n];
	}

	public List<Object[][]> getObjectData() {
		return objListData;
	}

	public List<String[][]> getStringData() {
		return strListData;
	}

	public int getColumn(int n) {
		return nColumn[n];
	}

	public ExcelUtils() {

	}

	/**
	 * 将所有Excel单元格内容均读为String的函数
	 * 
	 * @param file
	 *            excel文件对象
	 * @param nColumnBegin
	 *            开始读取列
	 * @param nRowBegin
	 *            开始读取行
	 * @return 返回List<String[][]>
	 * @throws BiffException
	 * @throws IOException
	 */
	public boolean ReadString(String strFile, int nRowBegin, int nColumnBegin)
			throws BiffException, IOException {
		File FileExcel = new File(strFile);
		if (!FileExcel.canRead()) {
			JOptionPane.showMessageDialog(null, "打开数据文件出错，即将退出。请检查目录中是否存在名为"
					+ strFile + "的文件", "错误", JOptionPane.ERROR_MESSAGE);
			System.out.println("file can't read");
			System.exit(1);
			return false;
		}
		strListData = new ArrayList<String[][]>();
		// 创建excel文件的工作簿对象book
		Workbook book = Workbook.getWorkbook(FileExcel);
		// 获取excel文件工作簿的工作表数量sheets
		Sheet[] sheets = book.getSheets();
		// 声明每个工作表存储的二维数组对象
		String[][] row_contents = null;
		// 逐个工作表开始读取
		for (int sheet_index = 0; sheet_index < sheets.length; sheet_index++) {
			// 测试语句
			System.out.println("当前为" + sheet_index + "个工作簿！");
			// 创建工作表对象sheet
			Sheet sheet = sheets[sheet_index];
			// 获取excel当前工作表的总行数
			int rows = sheet.getRows();
			nRow = new int[sheets.length];
			nColumn = new int[sheets.length];
			// 获取excel当前工作表的总列数
			int columns = sheet.getColumns();

			if (nRowBegin > rows || nColumnBegin > columns) {
				System.out.println("设置的读取范围大于实际模板数据，请查证");
				System.exit(1);
			}
			nRow[sheet_index] = rows - nRowBegin;
			nColumn[sheet_index] = columns - nColumnBegin;
			// 测试语句
			System.out.println("当前工作簿一共有" + rows + "行、" + columns + "列");
			// 创建当前工作表的存储二维数组
			row_contents = new String[rows - nRowBegin][columns - nColumnBegin];
			// 循环将当前工作簿内容保存到对象中
			// 循环行
			int nRowTemp = 0, nColuTemp = 0;
			for (int row_index = nRowBegin; row_index < rows; row_index++) {
				// 循环列
				String[] column_contents = new String[columns - nColumnBegin];

				for (int column_index = nColumnBegin; column_index < columns; column_index++) {
					// 获取当前工作表.row_index,column_index单元格的cell对象
					Cell cell = sheet.getCell(column_index, row_index);
					if (cell.getType() != CellType.EMPTY) {
						column_contents[column_index] = cell.getContents();
					} else
						column_contents[column_index] = "0";
				}

				// 当前sheet,当前row的所有column,存放到row_contents二维数组的row_index位置
				row_contents[nRowTemp] = column_contents;
				nRowTemp++;
			}
			// 集合收集数据
			strListData.add(row_contents);
		}

		return true;
	}

	/**
	 * 将Excel单元格内容读为String与Float两种类型的函数
	 * 
	 * @param strFile
	 *            excel文件对象的名称
	 * @param nColumnBegin
	 *            开始读取列
	 * @param nRowBegin
	 *            开始读取行
	 * @return 返回逻辑值，成功为真
	 * @throws BiffException
	 * @throws IOException
	 */
	public boolean readObject(String strFile, int nRowBegin, int nColumnBegin)
			throws BiffException, IOException {
		File FileExcel = new File(strFile);
		if (!FileExcel.canRead()) {
			JOptionPane.showMessageDialog(null, "打开数据文件出错，即将退出。请检查目录中是否存在名为"
					+ strFile + "的文件", "错误", JOptionPane.ERROR_MESSAGE);
			System.out.println("file can't read");
			System.exit(1);
			return false;
		}
		// 创建方法返回List集合对象
		objListData = new ArrayList<Object[][]>();
		// 创建excel文件的工作簿对象book
		Workbook book = Workbook.getWorkbook(FileExcel);
		// 获取excel文件工作簿的工作表数量sheets
		Sheet[] sheets = book.getSheets();
		nRow = new int[sheets.length];
		nColumn = new int[sheets.length];
		// 声明每个工作表存储的二维数组对象
		Object[][] row_contents = null;
		// 逐个工作表开始读取
		for (int sheet_index = 0; sheet_index < sheets.length; sheet_index++) {
			// 测试语句
			System.out.println("当前为" + sheet_index + "个工作簿！");
			// 创建工作表对象sheet
			Sheet sheet = sheets[sheet_index];
			// 获取excel当前工作表的总行数
			int rows = sheet.getRows();
			// 获取excel当前工作表的总列数
			int columns = sheet.getColumns();
			if (nRowBegin > rows || nColumnBegin > columns) {
				System.out.println("设置的读取范围大于实际模板数据，请查证");
				System.exit(1);
			}
			nRow[sheet_index] = rows - nRowBegin;
			nColumn[sheet_index] = columns - nColumnBegin;
			// 测试语句
			System.out.println("当前工作簿一共有" + rows + "行、" + columns + "列");
			// 创建当前工作表的存储二维数组,nRowBegin遵循第一行为0的编码规则
			row_contents = new Object[rows - nRowBegin][columns - nColumnBegin];
			// 循环将当前工作簿内容保存到对象中
			// 循环行,下面几行用来设置循环的起始行
			int nRowTemp = 0, nColumnTemp = 0;

			for (int row_index = nRowBegin; row_index < rows; row_index++) {
				// 循环列
				Object[] column_contents = new Object[columns - nColumnBegin];
				for (int column_index = nColumnBegin; column_index < columns; column_index++) {
					// 获取当前工作表.row_index,column_index单元格的cell对象
					Cell cell = sheet.getCell(column_index, row_index);
					if (cell.getType() != CellType.EMPTY) {
						if (cell.getType() == CellType.NUMBER
								|| cell.getType() == CellType.NUMBER_FORMULA) {
							// 获取内容值
							column_contents[column_index] = Double
									.parseDouble(cell.getContents());
						} else {
							column_contents[column_index] = cell.getContents();
						}
					} else
						// 如果为空，则令其为0
						column_contents[column_index] = 0;
				}
				// 当前sheet,当前row的所有column,存放到row_contents二维数组的row_index位置
				row_contents[nRowTemp] = column_contents;
				nRowTemp++;
			}
			// 集合收集数据
			objListData.add(row_contents);
		}

		// 返回
		return true;
	}

	/**
	 * 搜索某一个文件中是否包含某个关键字
	 * 
	 * @param file
	 *            待搜索的文件
	 * @param keyWord
	 *            要搜索的关键字
	 * @return
	 */
	public boolean searchKeyWord(File file, String keyWord) {
		boolean res = false;

		Workbook wb = null;
		try {
			// 构造Workbook（工作薄）对象
			wb = Workbook.getWorkbook(file);
		} catch (BiffException e) {
			return res;
		} catch (IOException e) {
			return res;
		}

		if (wb == null)
			return res;

		// 获得了Workbook对象之后，就可以通过它得到Sheet（工作表）对象了
		Sheet[] sheet = wb.getSheets();

		boolean breakSheet = false;

		if (sheet != null && sheet.length > 0) {
			// 对每个工作表进行循环
			for (int i = 0; i < sheet.length; i++) {
				if (breakSheet)
					break;

				// 得到当前工作表的行数
				int rowNum = sheet[i].getRows();

				boolean breakRow = false;

				for (int j = 0; j < rowNum; j++) {
					if (breakRow)
						break;
					// 得到当前行的所有单元格
					Cell[] cells = sheet[i].getRow(j);
					if (cells != null && cells.length > 0) {
						boolean breakCell = false;
						// 对每个单元格进行循环
						for (int k = 0; k < cells.length; k++) {
							if (breakCell)
								break;
							// 读取当前单元格的值
							String cellValue = cells[k].getContents();
							if (cellValue == null)
								continue;
							if (cellValue.contains(keyWord)) {
								res = true;
								breakCell = true;
								breakRow = true;
								breakSheet = true;
							}
						}
					}
				}
			}
		}
		// 最后关闭资源，释放内存
		wb.close();

		return res;
	}

	/**
	 * 往Excel中插入图片
	 * 
	 * @param dataSheet
	 *            待插入的工作表
	 * @param col
	 *            图片从该列开始
	 * @param row
	 *            图片从该行开始
	 * @param width
	 *            图片所占的列数
	 * @param height
	 *            图片所占的行数
	 * @param imgFile
	 *            要插入的图片文件
	 */
	public void insertImg(WritableSheet dataSheet, int col, int row, int width,
			int height, File imgFile) {
		WritableImage img = new WritableImage(col, row, width, height, imgFile);
		dataSheet.addImage(img);
	}

	public void start() {

		try {
			// 创建一个工作薄
			WritableWorkbook workbook = Workbook.createWorkbook(new File(
					"Test1.xls"));
			// 待插入的工作表
			WritableSheet imgSheet = workbook.createSheet("Images", 0);
			// 要插入的图片文件
			File imgFile = new File("1.png");
			// 图片插入到第二行第一个单元格，长宽各占六个单元格
			this.insertImg(imgSheet, 0, 1, 6, 6, imgFile);
			workbook.write();
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据Excel单元格第二列（姓名）创建文件夹，将 objData中的数据填入模板，模板会根据公式自动计算出相应的数值 模板文件名为strFile
	 * 计算后会将模板保存到各自的文件夹中 一般来说，应该从第二列第二行开始计算
	 * 
	 * @param strFile
	 *            要操作伯文件名称
	 * @param objData
	 *            要填充的数据源，为Object数组
	 */
	public void writeExcel(String strFile, Object[] objData) throws IOException {

		try {
			File fileExcel = new File(strFile);
			if (!fileExcel.canWrite()) {
				JOptionPane.showMessageDialog(null, "要写入的文件不存在。");
				return;
			}
			jxl.Workbook wb = null; // 创建一个workbook对象
			InputStream is = new FileInputStream(fileExcel); // 创建一个文件流，读入Excel文件
			wb = Workbook.getWorkbook(is);
			// jxl.Workbook 对象是只读的，所以如果要修改Excel，需要创建一个可读的副本，副本指向原Excel文件（即下面的new
			// File(excelpath)）
			jxl.write.WritableWorkbook wbe = Workbook.createWorkbook(fileExcel, wb);// 创建workbook的副本
			WritableSheet ws = wbe.getSheet(0); // 获取第一个sheet

			WritableCell wc = ws.getWritableCell(1, 2);
			jxl.format.CellFormat cf = wc.getCellFormat();// 获取单元格的格式
			jxl.write.Label lbl = new jxl.write.Label(1, 2,
					objData[1].toString());// 将单元格的值改为“姓名”
			lbl.setCellFormat(cf);// 将修改后的单元格的格式设定成跟原来一样

			// /写入土地面积
			wc = ws.getWritableCell(3, 2);
			cf = wc.getCellFormat();// 获取单元格的格式
			double dbData = Double.parseDouble(objData[5].toString());
			jxl.write.Number nGround;
			if (dbData > 0) {
				nGround = new jxl.write.Number(3, 2, dbData);
			} else {
				nGround = new jxl.write.Number(3, 2, 0);
			}
			nGround.setCellFormat(cf);

			// /写入投影
			wc = ws.getWritableCell(5, 2);
			cf = wc.getCellFormat();// 获取单元格的格式
			dbData = Double.parseDouble(objData[3].toString());
			jxl.write.Number nTouying;
			if (dbData > 0) {
				nTouying = new jxl.write.Number(5, 2, dbData);
			} else {
				nTouying = new jxl.write.Number(5, 2, 0);
			}
			nTouying.setCellFormat(cf);

			// 写入空地面积
			wc = ws.getWritableCell(7, 2);
			cf = wc.getCellFormat();// 获取单元格的格式
			dbData = Double.parseDouble(objData[4].toString());
			jxl.write.Number nKongdi;
			if (dbData > 0) {
				nKongdi = new jxl.write.Number(7, 2, dbData);
			} else {
				nKongdi = new jxl.write.Number(7, 2, 0);
			}
			nKongdi.setCellFormat(cf);

			// 写入住宅面积
			wc = ws.getWritableCell(1, 3);
			cf = wc.getCellFormat();// 获取单元格的格式
			dbData = Double.parseDouble(objData[5].toString());
			jxl.write.Number nZhuzhai;
			if (dbData > 0) {
				nZhuzhai = new jxl.write.Number(1, 3, dbData);
			} else {
				nZhuzhai = new jxl.write.Number(1, 3, 0);
			}
			nZhuzhai.setCellFormat(cf);

			// 写入门面面积
			wc = ws.getWritableCell(3, 3);
			cf = wc.getCellFormat();// 获取单元格的格式
			dbData = Double.parseDouble(objData[6].toString());
			;
			jxl.write.Number nMenmian;
			if (dbData > 0) {
				nMenmian = new jxl.write.Number(3, 3, dbData);
			} else {
				nMenmian = new jxl.write.Number(3, 3, 0);
			}
			nMenmian.setCellFormat(cf);

			// 写入简易面积
			wc = ws.getWritableCell(5, 3);
			cf = wc.getCellFormat();// 获取单元格的格式
			dbData = Double.parseDouble(objData[7].toString());
			;
			jxl.write.Number nJianyi;
			if (dbData > 0) {
				nJianyi = new jxl.write.Number(5, 3, dbData);
			} else {
				nJianyi = new jxl.write.Number(5, 3, 0);
			}
			nJianyi.setCellFormat(cf);

			// 写入评估金额
			wc = ws.getWritableCell(1, 6);
			cf = wc.getCellFormat();// 获取单元格的格式
			dbData = Double.parseDouble(objData[10].toString());
			;
			jxl.write.Number nPinggu;
			if (dbData > 0) {
				nPinggu = new jxl.write.Number(1, 6, dbData);
			} else {
				nPinggu = new jxl.write.Number(1, 6, 0);
			}
			nPinggu.setCellFormat(cf);

			ws.addCell(lbl);// 将改过的单元格保存到sheet
			ws.addCell(nGround);
			ws.addCell(nTouying);
			ws.addCell(nKongdi);
			ws.addCell(nZhuzhai);
			ws.addCell(nMenmian);
			ws.addCell(nJianyi);
			ws.addCell(nPinggu);

			wbe.write();// 将修改保存到workbook --》一定要保存
			wbe.close();// 关闭workbook，释放内存 ---》一定要释放内存
		} catch (IOException e) {

			e.printStackTrace();
		} catch (WriteException e) {

			e.printStackTrace();
		} catch (BiffException e) {

			e.printStackTrace();
		} // 将文件流写入到workbook对象
	}

	public final boolean WriteVect(String strFilename, String[] strTitle,
			Vector vectData) {
		if (vectData.isEmpty()) {
			JOptionPane.showMessageDialog(null, "表格数据为空");
			return false;
		}
		try {
			int nVectLength = vectData.size();
			String[][] objData = new String[nVectLength][];
			jxl.Workbook wb = null; // 创建一个workbook对象
			File fileExcel = new File(strFilename);
			if (!fileExcel.canWrite()) {
				JOptionPane.showMessageDialog(null, "文件不存在");
				return false;
			}
			InputStream is = new FileInputStream(fileExcel);
			// 创建一个文件流，读入Excel文件
			wb = Workbook.getWorkbook(is);
			jxl.write.WritableWorkbook wbe = Workbook.createWorkbook(fileExcel,
					wb);
			// 创建workbook的副本
			WritableSheet ws = wbe.getSheet(0); // 获取第一个sheet

			WritableCell wc2 = ws.getWritableCell(ws.getRows() + 1,
					ws.getColumns() + 1);
			jxl.format.CellFormat cf2 = wc2.getCellFormat();
			int nOldRow = ws.getRows();
			int nOldColum = ws.getColumns();
			for (int i = 0; i < nOldRow; i++)
				for (int m = 0; m < nOldColum; m++) {
					jxl.write.Label lbl2 = new jxl.write.Label(m, i, "");
					lbl2.setCellFormat(cf2);
				}

			String[] strArrRow = vectData.get(0).toString().replace('[', ' ')
					.replace(']', ' ').split(",");
			for (int i = 0; i < strArrRow.length; i++) {
				jxl.write.Label lbl = new jxl.write.Label(i, 1,
						strTitle[i].toString());
				ws.addCell(lbl);// 将改过的单元格保存到sheet
			}
			for (int n = 0; n < nVectLength; n++) {
				strArrRow = vectData.get(n).toString().replace('[', ' ')
						.replace(']', ' ').split(",");
				for (int m = 0; m < strArrRow.length; m++) {
					WritableCell wc = ws.getWritableCell(m, n + 2);
					jxl.write.Label lbl = new jxl.write.Label(m, n + 2,
							strArrRow[m].toString());
					ws.addCell(lbl);// 将改过的单元格保存到sheet
				}
			}
			// jxl.Workbook 对象是只读的，所以如果要修改Excel，需要创建一个可读的副本，副本指向原Excel文件（即下面的new
			// File(excelpath)）
			// WritableCell wc =ws.getWritableCell(1, 2);
			// jxl.format.CellFormat cf = wc.getCellFormat();//获取单元格的格式
			// jxl.write.Label lbl = new jxl.write.Label(1, 2,
			// objData[1].toString());//将单元格的值改为“姓名”
			// lbl.setCellFormat(cf);//将修改后的单元格的格式设定成跟原来一样
			//
			// // /写入地面面积
			// wc = ws.getWritableCell(3, 2);
			// cf = wc.getCellFormat();// 获取单元格的格式
			// double dbData=Double.parseDouble(objData[2].toString());
			// jxl.write.Number nGround;
			// if (dbData > 0) {
			// nGround = new jxl.write.Number(3, 2, dbData);
			// } else {
			// nGround = new jxl.write.Number(3, 2, 0);
			// }
			// nGround.setCellFormat(cf);
			//
			// // /写入投影
			// wc = ws.getWritableCell(5, 2);
			// cf = wc.getCellFormat();// 获取单元格的格式
			// dbData=Double.parseDouble(objData[3].toString());
			// jxl.write.Number nTouying;
			// if (dbData > 0) {
			// nTouying = new jxl.write.Number(5, 2, dbData);
			// } else {
			// nTouying = new jxl.write.Number(5, 2, 0);
			// }
			// nTouying.setCellFormat(cf);
			//
			// //写入空地面积
			// wc = ws.getWritableCell(7, 2);
			// cf = wc.getCellFormat();// 获取单元格的格式
			// dbData=Double.parseDouble(objData[4].toString());
			// jxl.write.Number nKongdi;
			// if (dbData > 0) {
			// nKongdi = new jxl.write.Number(7, 2, dbData);
			// } else {
			// nKongdi = new jxl.write.Number(7, 2, 0);
			// }
			// nKongdi.setCellFormat(cf);
			//
			// //写入住宅面积
			// wc = ws.getWritableCell(1, 3);
			// cf = wc.getCellFormat();// 获取单元格的格式
			// dbData=Double.parseDouble(objData[5].toString());
			// jxl.write.Number nZhuzhai;
			// if (dbData > 0) {
			// nZhuzhai = new jxl.write.Number(1, 3,dbData);
			// } else {
			// nZhuzhai = new jxl.write.Number(1, 3, 0);
			// }
			// nZhuzhai.setCellFormat(cf);
			//
			//
			// //写入门面面积
			// wc = ws.getWritableCell(3, 3);
			// cf = wc.getCellFormat();// 获取单元格的格式
			// dbData=Double.parseDouble(objData[6].toString());;
			// jxl.write.Number nMenmian;
			// if (dbData > 0) {
			// nMenmian = new jxl.write.Number(3, 3,dbData);
			// } else {
			// nMenmian = new jxl.write.Number(3, 3, 0);
			// }
			// nMenmian.setCellFormat(cf);
			//
			// //写入简易面积
			// wc = ws.getWritableCell(5, 3);
			// cf = wc.getCellFormat();// 获取单元格的格式
			// dbData=Double.parseDouble(objData[7].toString());;
			// jxl.write.Number nJianyi;
			// if (dbData > 0) {
			// nJianyi = new jxl.write.Number(5, 3, dbData);
			// } else {
			// nJianyi = new jxl.write.Number(5, 3, 0);
			// }
			// nJianyi.setCellFormat(cf);
			//
			// //写入评估金额
			// wc = ws.getWritableCell(1, 6);
			// cf = wc.getCellFormat();// 获取单元格的格式
			// dbData=Double.parseDouble(objData[10].toString());;
			// jxl.write.Number nPinggu;
			// if (dbData > 0) {
			// nPinggu = new jxl.write.Number(1, 6, dbData);
			// } else {
			// nPinggu = new jxl.write.Number(1,6, 0);
			// }
			// nPinggu.setCellFormat(cf);
			//
			//
			//
			// ws.addCell(nGround);
			// ws.addCell(nTouying);
			// ws.addCell(nKongdi);
			// ws.addCell(nZhuzhai);
			// ws.addCell(nMenmian);
			// ws.addCell(nJianyi);
			// ws.addCell(nPinggu);

			wbe.write();// 将修改保存到workbook --》一定要保存
			wbe.close();// 关闭workbook，释放内存 ---》一定要释放内存
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		}
		return true;
	}

}
