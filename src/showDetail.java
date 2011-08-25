import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class showDetail extends JDialog {

	/**
	 * 读取表格中的姓名等信息，显示该户面积补偿款等详细情况
	 */
	private static final long serialVersionUID = 1L;
	final Vector<JTextField> vectJTextField = new Vector<JTextField>();
	public showDetail(JFrame owner, String[] strTitle, String[] strDetail) {
		super(owner, "征收户详情", true);
		setSize(600, 300);
		setResizable(false);
		setLocationRelativeTo(null);// 窗口居中
		JPanel panelMain = new JPanel();
		JPanel panelBasic = new JPanel(new GridLayout(6, 4));

		panelBasic.setBorder(new TitledBorder(strDetail[1] + "详细资料"));
		for (int i = 0; i < strTitle.length; i++) {
			JLabel lable = new JLabel(strTitle[i]);
			lable.setSize(150, 20);
			panelBasic.add(lable);
			vectJTextField.add(new JTextField(strDetail[i]));
			panelBasic.add(vectJTextField.get(i));
		}
		JButton btnPrint = new JButton("打印");
		JButton btnEdit = new JButton("修改");
		panelMain.add(panelBasic);
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				//TODO 添加详情表单中打印功能
			}
		});
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				//TODO 添加详情表单中修改功能
			}
		});


		JPanel panelBtn = new JPanel();
		panelBtn.add(btnEdit);
		panelBtn.add(btnPrint);
		panelMain.add(panelBtn);

		JPanel paneltips = new JPanel();
		JTextArea textTips = new JTextArea("在这里，可以对用户的数据进行修改，修改将计入数据库。"
				+ "方法是：对数据进行了修改后,单击修改按钮即可。");
		textTips.setWrapStyleWord(false);
		paneltips.add(textTips);
		panelMain.add(paneltips);

		add(panelMain);
	}
}

