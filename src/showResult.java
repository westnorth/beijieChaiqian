import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class showResult extends JDialog {

	/**
	 * 读取表格中的姓名等信息，显示该户面积补偿款等详细情况
	 */
	private static final long serialVersionUID = 1L;
	final Vector<JTextField> vectJTextField = new Vector<JTextField>();
	int[] nArrShow = { 0, 1, 5, 6, 7, 8, 11, 15 };
//	String[] strArrLabel = { "序号", "姓名", "空地面积", "评估空地面积", "宗地面积", "门面房", "住宅",
//			"简易结构", "附属物合计", "原始表附属物" };
	boolean blSameGround, blSameAppdix;
//<<<<<<< HEAD
//=======
	JLabel lblTipsGround=new JLabel("");
	JLabel lblTipsAppendix=new JLabel("");
//>>>>>>> 由于空地与附属物可能有两个不同的值。根据实际情况将两个值写入复选框，

	public showResult(JFrame owner, String[] strTitle, String[] strDetail) {
		super(owner, "征收户详情", true);
		setSize(600, 300);
		setResizable(false);
		int nVect = 0;
		blSameAppdix=true;
		blSameGround=true;
		setLocationRelativeTo(null);// 窗口居中
		JPanel panelMain = new JPanel();
		JComboBox comboxGround = new JComboBox();
		JComboBox comboxAppendix = new JComboBox();
		JPanel panelBasic = new JPanel(new GridLayout(4, 4));
		
		// 0序号 1姓名 2旧表姓名 3test 4old 5空地面积 6宗地面积 7门面房
		// 8住宅 9总计 10建筑基底 11简易结构 12附属物 13装修 14简易结构 15合计
		// 16原始表 17注
		
		panelBasic.setBorder(new TitledBorder(strDetail[1] + "详细资料"));
		Arrays.sort(nArrShow);
		// XXX 由于空地与附属物可能有两个不同的值。应根据实际情况将两个值写入复选框，并提醒用户选择一个正确的结果。
		for (int i = 0; i < strTitle.length; i++) {
			if (Arrays.binarySearch(nArrShow, i) >= 0) {
				JLabel lable = new JLabel(strTitle[i]);
				lable.setSize(150, 20);
				panelBasic.add(lable);
				if (i == 5) {//处理空地，如果两个一致，则只给下拉框中添加一个值，否则，将两个值均进行添加。
					comboxGround.addItem(strDetail[i]);
					if (!strDetail[i].trim().toString()
							.equals(strDetail[i - 1].trim().toString())) {
						blSameGround=false;
						comboxGround.addItem(strDetail[i - 1]);
					}
					panelBasic.add(comboxGround);
						lblTipsGround.setText("请选择正确的空地面积");
						lblTipsGround.setBackground(Color.red);
						comboxGround.addItem(strDetail[i - 1]);
					}
					panelBasic.add(comboxGround);
					comboxGround.addMouseListener(new MouseAdapter() {
						public void mouseReleased(MouseEvent evt) {//如果下拉框 
							blSameGround=true;
							if(blSameGround&&blSameAppdix)
								lblTipsGround.setText("已经选择了空地面积");
						}
					});
//>>>>>>> 由于空地与附属物可能有两个不同的值。根据实际情况将两个值写入复选框，
					i = 6;
					continue;
				}//end if i = 5
				if (i == 15) {
					comboxAppendix.addItem(strDetail[i]);
					if (!strDetail[i].trim().toString()
							.equals(strDetail[i + 1].trim().toString())) {
						blSameAppdix=false;
						lblTipsAppendix.setText("请选择正确的附属物价值");
						lblTipsAppendix.setBackground(Color.red);
//>>>>>>> 由于空地与附属物可能有两个不同的值。根据实际情况将两个值写入复选框，
						comboxAppendix.addItem(strDetail[i + 1]);
					}
					panelBasic.add(comboxAppendix);
					i = 17;
					comboxAppendix.addMouseListener(new MouseAdapter() {
						public void mouseReleased(MouseEvent evt) {
							blSameAppdix=true;
							if(blSameGround&&blSameAppdix)
								lblTipsAppendix.setText("已经选择了附属物价值");
						}
					});
					continue;
				}
				vectJTextField.add(new JTextField(strDetail[i]));
				panelBasic.add(vectJTextField.get(nVect));
				nVect++;
		}
//			}
//		}
		JPanel panelTips=new JPanel();
		JButton btnCreate=new JButton("概算");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO 概算程序
			}
		});
		panelTips.add(lblTipsAppendix);
		panelTips.add(lblTipsGround);
		panelMain.add(panelBasic);
		panelMain.add(panelTips);
		add(panelMain);
	}
}
