import java.awt.Desktop;
import java.awt.GridLayout;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import java.awt.event.*;
import java.io.IOException;

public class showExport extends JDialog {

	/**
	 * 读取表格中的姓名等信息，显示该户面积补偿款等详细情况
	 */
	private static final long serialVersionUID = 1L;
	final Vector<JCheckBox> vectCheck = new Vector<JCheckBox>();
	private final String strExportFilename = getCurrentDir() + "output.xls";

	public showExport(JFrame owner, final String[] strTitle,
			@SuppressWarnings("rawtypes") final Vector vectTableData) {

		super(owner, "征收户详情", true);

		setSize(380, 260);
		setResizable(false);
		setLocationRelativeTo(null);// 窗口居中
		JPanel panelMain = new JPanel();
		JPanel panelBasic = new JPanel(new GridLayout(6, 4));
		panelBasic.setBorder(new TitledBorder("选择导出项目"));
		for (int i = 0; i < strTitle.length; i++) {
			vectCheck.add(new JCheckBox(strTitle[i]));
			vectCheck.get(i).setSelected(true);
			panelBasic.add(vectCheck.get(i));
		}
		JButton btnExport = new JButton("导出");
		panelMain.add(panelBasic);
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				//XXX 这里需要根据复选框内容选择相应的内容进行输出
				ExcelUtils excelWrite = new ExcelUtils();
				if (excelWrite.WriteVect(strExportFilename, strTitle,
						vectTableData)) {
					try {
						Desktop desktop = Desktop.getDesktop();
						File fileOpen = new File(strExportFilename);
						desktop.open(fileOpen);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		JPanel panelBtn = new JPanel();
		panelBtn.add(btnExport);
		panelMain.add(panelBtn);
		add(panelMain);

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
}