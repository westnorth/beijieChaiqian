/* 
 * %W% %E% 
 */

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;
import java.util.Vector;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import jxl.JXLException;
import jxl.read.biff.BiffException;

public class mainGUI {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				myGUI frame = new myGUI();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				frame.setResizable(false);
				frame.setLayout(null);
				frame.setLocationRelativeTo(null);
			}
		});
	}
}

class myGUI extends JFrame {
	@SuppressWarnings("rawtypes")
	public myGUI() {
		setTitle("北街拆迁管理软件");
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		((JPanel) getContentPane()).setOpaque(false);
		setLayout(new BorderLayout());

		// setBounds(100, 100, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		panel = new JPanel();
		panel.setBounds(110, 110, DEFAULT_WIDTH - 20, DEFAULT_HEIGHT - 20);
		panel.setLayout(new BorderLayout());
		// JSlider interCellSpacingSlider;
		add(panel);

		controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));

		JPanel comboPanel = new JPanel(new ColumnLayout());
		JPanel printPanel = new JPanel(new ColumnLayout());

		panel.add(controlPanel, BorderLayout.NORTH);
		@SuppressWarnings("rawtypes")
		// Vector relatedComponents = new Vector();
		JPanel searchPanel = new JPanel(new ColumnLayout());
		// searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
		searchPanel.setBorder(new TitledBorder("搜索列表中姓名"));
		// label panel
		searchLabel = new JLabel("要查询的姓名:");
		searchNameTextField = new JTextField("", 6);

		btnSearch = new JButton("搜索");

		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				searchNameInTable(searchNameTextField.getText().toString());
			}
		});
		searchPanel.add(searchLabel);
		searchPanel.add(searchNameTextField);
		searchPanel.add(btnSearch);

		// Create the table.
		try {
			tableAggregate = createTable();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (JXLException e2) {
			e2.printStackTrace();
		}
		panel.add(tableAggregate, BorderLayout.CENTER);

		// ComboBox for selection modes.
		JPanel panelFilter = new JPanel();
		panelFilter.setLayout(new BoxLayout(panelFilter, BoxLayout.Y_AXIS));
		// panelFilter.setAlignmentY(1);
		panelFilter.setBorder(new TitledBorder("数据筛选"));

		selectionModeComboBox = new JComboBox() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Dimension getMaximumSize() {
				return getPreferredSize();
			}
		};
		for (int i = 0; i < strTableTitle.length; i++) {
			selectionModeComboBox.addItem(strTableTitle[i].toString());
		}
		selectionModeComboBox.setSelectedIndex(tableView.getSelectionModel()
				.getSelectionMode());
		selectionModeComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				// JComboBox source = (JComboBox) e.getSource();
				// tableView.setSelectionMode(source.getSelectedIndex());
			}
		});

		JPanel panelFilterName = new JPanel();
		// panelFilterName.setAlignmentX(RIGHT_ALIGNMENT);
		JLabel lblFilterName = new JLabel("筛选项目");
		panelFilterName.add(lblFilterName);
		panelFilterName.add(selectionModeComboBox);
		panelFilter.add(panelFilterName);

		condationComboBox = new JComboBox() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Dimension getMaximumSize() {
				return getPreferredSize();
			}
		};
		condationComboBox.addItem("大于(数字)");
		condationComboBox.addItem("小于(数字)");
		condationComboBox.addItem("等于(可为字符)");

		condationComboBox.setSelectedIndex(0);
		condationComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				// JComboBox source = (JComboBox) e.getSource();
				// tableView.setSelectionMode(source.getSelectedIndex());
			}
		});
		JLabel lblFilterCon = new JLabel("筛选条件");
		JPanel panelFilterCon = new JPanel();

		panelFilterCon.add(lblFilterCon);
		panelFilterCon.add(condationComboBox);
		panelFilter.add(panelFilterCon);

		final JTextField textCondation = new JTextField("", 10);
		JLabel lblFilterContent = new JLabel("筛选内容");
		JPanel panelFilterContent = new JPanel();
		panelFilterContent.add(lblFilterContent);
		panelFilterContent.add(textCondation);
		panelFilter.add(panelFilterContent);

		JPanel btnFilterPanel = new JPanel();

		btnSetDefaultTable = new JButton("显示所有数据");
		btnFilterPanel.add(btnSetDefaultTable);
		btnSetDefaultTable.setEnabled(false);

		btnCondation = new JButton("筛选");
		btnFilterPanel.add(btnCondation);

		panelFilter.add(btnFilterPanel);

		btnSetDefaultTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				vectTableData.removeAllElements();
				vectTableData.addAll(vectDefaultTableData);
				// tableView.invalidate();
				TableDataModel.fireTableDataChanged();
				btnSetDefaultTable.setEnabled(false);
			}
		});

		btnCondation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				int nName = selectionModeComboBox.getSelectedIndex();
				int nCondation = condationComboBox.getSelectedIndex();// 0:大于
																		// 1:小于
																		// 2:等于
				if (nName == 1 || nName == 2 || nName == 17) {
					if (nCondation == 0 || nCondation == 1) {
						JOptionPane.showMessageDialog(null,
								"筛选项目为姓名项时，筛选条件只能选择等于");
						return;
					}
				}
				String strContent = textCondation.getText();
				if (strContent.isEmpty()) {
					JOptionPane.showMessageDialog(null, "没有输入筛选内容，请输入后再试。");
					return;
				}
				switch (nCondation) {
				case 0:// 大于
					int nInit = 0,
					nEnd = vectTableData.size();
					for (int i = nInit; i < nEnd; i++) {
						String[] strObj = vectTableData.get(i).toString()
								.split(",");
						float n = Float.parseFloat(strContent);// 筛选条件
						float n2 = Float.parseFloat(strObj[nName]);// 表中数值
						if (n > n2) {
							TableDataModel.removeRow(i);
							i--;// 删除一个元素后，当前元素被后继的元素取代，所以将当前位置减1，
							// 下次从这个位置开始处理，便是实际上该位置已经是后继元素。
							// 如果不减1,则会越过当前位置的元素
							nEnd = vectTableData.size();
							// 将结束标志设置为新的Vector的大小
						}
					}
					break;
				case 1:// 小于
					nInit = 0;
					nEnd = vectTableData.size();
					for (int i = nInit; i < nEnd; i++) {
						String[] strObj = vectTableData.get(i).toString()
								.split(",");
						float n = Float.parseFloat(strContent);// 筛选条件
						float n2 = Float.parseFloat(strObj[nName]);// 表中数值
						if (n < n2) {
							TableDataModel.removeRow(i);
							i--;
							nEnd = vectTableData.size();
						}
					}
					break;
				case 2:// 等于
						// Enumeration enu = vectTableData.elements();
					if (nName == 1 || nName == 2) {// 如果是字符
						nInit = 0;
						nEnd = vectTableData.size();
						for (int i = nInit; i < nEnd; i++) {
							String[] strObj = vectTableData.get(i).toString()
									.split(",");
							if (strObj[nName].indexOf(strContent) == -1) {
								TableDataModel.removeRow(i);
								i--;
								nEnd = vectTableData.size();
							}
						}
					} else {
						nInit = 0;
						nEnd = vectTableData.size();
						for (int i = nInit; i < nEnd; i++) {
							String[] strObj = vectTableData.get(i).toString()
									.split(",");
							float n = Float.parseFloat(strContent);// 筛选条件
							float n2 = Float.parseFloat(strObj[nName]);// 表中数值
							if ((n - n2) > 0.01 || (n2 - n) > 0.01) {
								TableDataModel.removeRow(i);
								i--;
								nEnd = vectTableData.size();
							}
						}
					}
					break;
				}// end switch
				btnSetDefaultTable.setEnabled(true);
				if (!vectTableData.isEmpty())//如果表格数据不为空
					TableDataModel.fireTableDataChanged();
				// tableView.getSelectionModel().setSelectionInterval(0, 0);
				else
					tableView.revalidate();

			}
		});
		comboPanel.add(panelFilter);

		printPanel.setBorder(new TitledBorder("系统设置"));
		// headerLabel = new JLabel(getString("TableDemo.header"));
		// footerLabel = new JLabel(getString("TableDemo.footer"));
		checkboxForceCreate = new JCheckBox("强制重新生成文件");
		checkboxForceCreate.setSelected(false);
		// footerTextField = new JTextField(getString("TableDemo.footerText"),
		// 15);
		// fitWidth = new JCheckBox(getString("TableDemo.fitWidth"), true);
		btnCreateFolder = new JButton("创建目录");
		btnCreateCalcFile = new JButton("创建概算文件");
		JButton btnClear = new JButton("清空");
		btnClear.setBackground(Color.red);

		JButton btnExport = new JButton("导出当前表内容");
		btnCreateFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					CreateFolder();
				} catch (JXLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		btnCreateCalcFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					CreateCalcFile();
				} catch (JXLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Clean();
			}
		});
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				showExportDlg(strTableTitle, vectTableData);
			}
		});
		printPanel.add(checkboxForceCreate);

		JPanel buttons = new JPanel(new GridLayout(2, 3));
		// buttons.add(fitWidth);
		buttons.add(btnCreateFolder);
		buttons.add(btnCreateCalcFile);
		buttons.add(btnClear);
		buttons.add(btnExport);

		printPanel.add(buttons);

		// wrap up the panels and add them
		JPanel sliderWrapper = new JPanel();
		sliderWrapper.setLayout(new BoxLayout(sliderWrapper, BoxLayout.X_AXIS));
		// sliderWrapper.add(labelPanel);
		sliderWrapper.add(searchPanel);
		sliderWrapper.add(Box.createHorizontalGlue());
		sliderWrapper.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));

		JPanel leftWrapper = new JPanel();
		leftWrapper.setLayout(new BoxLayout(leftWrapper, BoxLayout.Y_AXIS));
		// leftWrapper.add(cbPanel);
		leftWrapper.add(sliderWrapper);

		// add everything
		controlPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));
		controlPanel.add(leftWrapper);
		controlPanel.add(comboPanel);
		controlPanel.add(printPanel);
		rowBegin = 0;
		tableView.getSelectionModel().setSelectionInterval(0, 0);

		vectDefaultTableData = (Vector) vectTableData.clone();

	}

	void Clean() {
		// TODO function clean

	}

	void CreateFolder() throws JXLException, IOException {
		ExcelUtils myExcel = new ExcelUtils();
		try {
			if (myExcel.ReadString(getCurrentDir() + strDataFile, 0, 0))// 取回数据成功
			{
				Object[][] mainstrTableData = myExcel.getStringData().get(0);
				for (int nRow = 1; nRow < mainstrTableData.length; nRow++) {
					String strName = mainstrTableData[nRow][1].toString();// 获得Excel中的姓名
					File dirFile = new File(getCurrentDir() + strName);
					if (!dirFile.exists())

					{
						dirFile.mkdir();// 创建以姓名为名称的文件夹
					}
					File oldFile = new File(getCurrentDir() + strCalcFile);
					// File newFile = new File(strName+".xls");
					File newFile = new File(getCurrentDir() + strName
							+ File.separatorChar + strCalcFile);
					if (!newFile.exists() || newFile.exists()
							&& checkboxForceCreate.isSelected()) {
						// 如果文件不存在或者虽然文件存在但强制生成选项被选中，都需要重新生成文件
						try {
							FileCopy(oldFile, newFile);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void CreateCalcFile() throws JXLException, IOException {
		// if(objListData.isEmpty())//如果之前没有取得objListData数据
		ExcelUtils myExcel = new ExcelUtils();
		try {
			if (myExcel.readObject(getCurrentDir() + strDataFile, 0, 0))// 取回数据成功
			{
				Object[][] mainstrTableData = myExcel.getObjectData().get(0);
				for (int nRow = 1; nRow < mainstrTableData.length; nRow++) {
					String strName = mainstrTableData[nRow][1].toString();// 获得Excel中的姓名
					Object[] objTemp = mainstrTableData[nRow];
					// writeExcel("1.xls",objTemp);
					myExcel.writeExcel(getCurrentDir() + strName
							+ File.separator + strCalcFile, objTemp);
				}
			}
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getCurrentDir() {
		String curdir = "";
		try {
			curdir = Thread.currentThread().getContextClassLoader()
					.getResource("").toURI().getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return curdir;

	}

	public static long FileCopy(File f1, File f2) throws Exception {
		FileInputStream in = new FileInputStream(f1);
		FileOutputStream out = new FileOutputStream(f2);
		byte[] buffer = new byte[1024];
		while (true) {
			int ins = in.read(buffer);
			if (ins == -1) {
				in.close();
				out.flush();
				out.close();
				return 1;
			} else
				out.write(buffer, 0, ins);
		}
	}

	public void searchNameInTable(String strName) {
		int nRow = tableView.getRowCount();
		boolean blFind = false;
		for (int i = rowBegin; i < nRow; i++) {
			if ((tableView.getValueAt(i, 1).toString()).indexOf(strName) > -1) {
				rowBegin = i + 1;// 把当前行的下一行设置为下次搜索的开始行
				blFind = true;
				// 滚动显示找到的行
				tableView.grabFocus();
				tableView.changeSelection(i, 2, false, false);
				break;
			}
		}
		if (!blFind) {
			JOptionPane.showMessageDialog(null, "当前行后没有姓名为" + strName
					+ "的用户，请修改搜索内容。如果确定输入正确，请直接再次单击搜索按钮从第一行开始搜索。");
			rowBegin = 0;
		}

	}

	public JScrollPane createTable() throws JXLException, IOException {

		ExcelUtils myExcel = new ExcelUtils();
		if (myExcel.ReadString(getCurrentDir() + strDataFile, 0, 0))//
		{
			String[][] mainstrTableData = new String[myExcel.getRow(0)][myExcel
					.getColumn(0)];
			mainstrTableData = myExcel.getStringData().get(0);
			strTableTitle = mainstrTableData[0].clone();
			int nTableRow = mainstrTableData.length - 1, nTableCol = mainstrTableData[0].length;
			for (int i = 1; i <= nTableRow; i++) {
				Vector vectsub = new Vector(mainstrTableData[0].length);
				for (int j = 0; j < mainstrTableData[0].length; j++)
					vectsub.add(j, mainstrTableData[i][j]);
				vectTableData.add(vectsub);
			}
		}

		// Create a model of the vectTableData.
		TableDataModel = new Table_Model(vectTableData, strTableTitle);
		// Create the table
		tableView = new JTable(TableDataModel);
		@SuppressWarnings("rawtypes")
		TableRowSorter sorter = new TableRowSorter(TableDataModel);
		tableView.setRowSorter(sorter);
		tableView.setAutoscrolls(true);

		tableView.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				final int nrow = tableView.rowAtPoint(me.getPoint());
				if (nrow < 0)
					return;// 如果当前选择的行小于1,说明处于未选中状态，则返回。
				Object objSelectRow = vectTableData.get(nrow);
				strSelectName = objSelectRow.toString().replace('[', ' ')
						.replace(']', ' ').split(",");// 转换为String后，字符会用一对方括号括起来，这个先用硬编码取掉。
				if (me.getButton() == MouseEvent.BUTTON1) {// 单击鼠标左键
					if (me.getClickCount() == 2) {// 如果是双击
						ShowDetailDlg(strTableTitle, strSelectName);
					}
				}

				if (me.getButton() == MouseEvent.BUTTON3) {
					tableView.setRowSelectionInterval(nrow, nrow);
					CreatePopMenu(tableView, me, nrow, strSelectName[1]);
				}
			}
		});
		tableView.setRowHeight(INITIAL_ROWHEIGHT);
		scrollpane = new JScrollPane(tableView);
		return scrollpane;
	}

	class ColumnLayout implements LayoutManager {
		int xInset = 5;
		int yInset = 5;
		int yGap = 2;

		public void addLayoutComponent(String s, Component c) {
		}

		public void layoutContainer(Container c) {
			Insets insets = c.getInsets();
			int height = yInset + insets.top;

			Component[] children = c.getComponents();
			Dimension compSize = null;
			for (int i = 0; i < children.length; i++) {
				compSize = children[i].getPreferredSize();
				children[i].setSize(compSize.width, compSize.height);
				children[i].setLocation(xInset + insets.left, height);
				height += compSize.height + yGap;
			}

		}

		public Dimension minimumLayoutSize(Container c) {
			Insets insets = c.getInsets();
			int height = yInset + insets.top;
			int width = 0 + insets.left + insets.right;

			Component[] children = c.getComponents();
			Dimension compSize = null;
			for (int i = 0; i < children.length; i++) {
				compSize = children[i].getPreferredSize();
				height += compSize.height + yGap;
				width = Math.max(width, compSize.width + insets.left
						+ insets.right + xInset * 2);
			}
			height += insets.bottom;
			return new Dimension(width, height);
		}

		public Dimension preferredLayoutSize(Container c) {
			return minimumLayoutSize(c);
		}

		public void removeLayoutComponent(Component c) {
		}
	}

	void ShowDetailDlg(String[] strTitle, String[] strDetail) {
		showDetail showDetailDlg = new showDetail(this, strTitle, strDetail);
		showDetailDlg.setVisible(true);
	}

	void showResultDlg(String[] strTitle, String[] strDetail) {
		showResult showDetailDlg = new showResult(this, strTitle, strDetail);
		showDetailDlg.setVisible(true);
	}

	void showExportDlg(String[] strTitle, Vector vectTableData2) {
		showExport dlgExport = new showExport(this, strTitle, vectTableData2);
		dlgExport.setVisible(true);
	}

	public void CreatePopMenu(JTable tableView, MouseEvent evt,
			int nSelectedLine, String strName) {
		tableView.changeSelection(nSelectedLine, 2, false, false);

		JPopupMenu popupMenu = new JPopupMenu();

		JMenuItem itemOpenFolder = new JMenuItem("打开" + strName.trim() + "主目录");
		JMenuItem itemDetail = new JMenuItem("查看" + strName.trim() + "明细");
		JMenuItem itemAddSource = new JMenuItem("添加" + strName.trim() + "图片资料");
		JMenuItem itemGetResult = new JMenuItem("概算" + strName.trim() + "安置情况");
		JMenuItem itemExportTable = new JMenuItem("导出当前表内容");
		JMenuItem itemAdd = new JMenuItem("添加新征收户");

		popupMenu.add(itemOpenFolder);
		popupMenu.add(itemDetail);
		popupMenu.add(itemAddSource);
		popupMenu.addSeparator();
		popupMenu.add(itemGetResult);
		popupMenu.add(itemExportTable);
		popupMenu.addSeparator();
		popupMenu.add(itemAdd);

		itemOpenFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				browsePath(getCurrentDir() + strSelectName[2].trim());
				System.out.println("点击了目录菜单");
			}
		});
		itemDetail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop desktop = Desktop.getDesktop();
					File fileOpen = new File(getCurrentDir()
							+ strSelectName[2].trim() + File.separator
							+ strCalcFile);
					if (!fileOpen.isFile()) {
						JOptionPane.showMessageDialog(null, "文件"
								+ getCurrentDir() + strSelectName[2].trim()
								+ File.separator + strCalcFile + "不存在，请先创建文件");
						return;
					}
					desktop.open(fileOpen);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		itemAddSource.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("点击了添加图片菜单");
				// TODO source item add
			}
		});
		itemAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("点击了添加菜单");
				// TODO add item
			}
		});
		itemExportTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showExportDlg(strTableTitle, vectTableData);
				System.out.println("点击了导出菜单");
			}
		});
		itemGetResult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("点击了概算菜单");
				showResultDlg(strTableTitle, strSelectName);
			}
		});

		popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
	}

	void updateDragEnabled(boolean dragEnabled) {
		tableView.setDragEnabled(dragEnabled);
		tipsTextField.setDragEnabled(dragEnabled);
	}

	public static void browsePath(String strPath) {
		String[] execString = new String[2];
		File filePath = new File(strPath);
		if (!filePath.isDirectory()) {
			JOptionPane.showMessageDialog(null, "目录" + strPath.trim()
					+ "不存在，请先创建目录");
			return;

		}
		String strfilePath = null;
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().startsWith("windows")) {
			// Window System;
			execString[0] = "explorer";
			try {
				strfilePath = strPath.replace("/", "\\");
			} catch (Exception ex) {
				strfilePath = strPath;
			}
		} else {
			// Unix or Linux;
			execString[0] = "nautilus";
			strfilePath = strPath;
		}

		execString[1] = strfilePath;
		try {
			Runtime.getRuntime().exec(execString);
		} catch (Exception ex) {
			System.out.println("异常啦...");
		}
	}

	private static final long serialVersionUID = 1L;
	JTable tableView;
	JScrollPane scrollpane;
	Dimension origin = new Dimension(0, 0);

	JLabel searchLabel;
	static JPanel panel;

	JComboBox selectionModeComboBox = null;
	JComboBox condationComboBox = null;

	JLabel headerLabel;
	JLabel footerLabel;

	JTextField tipsTextField;
	// JTextField footerTextField;
	JTextField searchNameTextField;

	JButton btnCreateCalcFile;
	JButton btnCreateFolder;
	JButton btnSearch;
	JButton btnCondation;
	JButton btnSetDefaultTable;

	JCheckBox checkboxForceCreate;

	JPanel controlPanel;
	JScrollPane tableAggregate;
	Table_Model TableDataModel;

	// String[][] strTableData;
	private Vector vectTableData = new Vector();
	private Vector vectDefaultTableData = new Vector();
	String[] strTableTitle;

	private static final int DEFAULT_WIDTH = 1024;
	private static final int DEFAULT_HEIGHT = 717;

	// 下面这两个参数是存放由Excel文件中取回结果的数据，一个为String，一个为Object
	List<String[][]> strListData;
	List<Object[][]> objListData;

	final int INITIAL_ROWHEIGHT = 33;

	private String strDataFile = "temple.xls";
	private String strCalcFile = "templeCalc.xls";

	private String[] strSelectName;
	// private ExcelUtils ex public String getCurrentDir() {
	int rowBegin;

}

/**
 * TableModel类，继承了AbstractTableModel
 * 
 * @author 五斗米
 * @author westnorth进行了部分修正
 */
class Table_Model extends AbstractTableModel {

	private static final long serialVersionUID = -7495940408592595397L;

	private Vector content = null;

	private String[] title_name = null;

	public Table_Model(Vector vectContent, String[] title) {
		content = vectContent;
		title_name = title;
	}

	public void addRow(Vector vectRow) {
		// Vector v = new Vector(4);
		// v.add(0, new Integer(content.size()));
		// v.add(1, name);
		// v.add(2, new Boolean(sex));
		// v.add(3, age);
		content.add(vectRow);
	}

	public void removeRow(int row) {
		content.remove(row);
	}

	public void removeRows(int row, int count) {
		for (int i = 0; i < count; i++) {
			if (content.size() > row) {
				content.remove(row);
			}
		}
	}

	/**
	 * 让表格中某些值可修改，但需要setValueAt(Object value, int row, int col)方法配合才能使修改生效
	 */
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// if (columnIndex == 0) {
		// return false;
		// }
		// return true;
		return false;
	}

	/**
	 * 使修改的内容生效
	 */
	public void setValueAt(Object value, int row, int col) {
		((Vector) content.get(row)).remove(col);
		((Vector) content.get(row)).add(col, value);
		this.fireTableCellUpdated(row, col);
	}

	public String getColumnName(int col) {
		return title_name[col];
	}

	public int getColumnCount() {
		return title_name.length;
	}

	public int getRowCount() {
		return content.size();
	}

	public Object getValueAt(int row, int col) {
		return ((Vector) content.get(row)).get(col);
	}

	/**
	 * 返回数据类型
	 */
	public Class getColumnClass(int col) {
		return getValueAt(0, col).getClass();
	}
}