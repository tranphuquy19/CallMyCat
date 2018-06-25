package shutdownServer;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class panel extends JPanel {
	private JTextField txtName;
	private String host = "Your PC Address";
	private String port = "Port Application";
	private String name = "Kitty Cat";
	
	public panel(String host, int port)
	{
		this.host = host;
		this.port = Integer.toString(port);
		
		setBorder(new EmptyBorder(2, 2, 2, 2));
		setForeground(new Color(148, 0, 211));
		setBackground(new Color(255, 255, 255));
		setLayout(null);
		
		JLabel lblHost = new JLabel("Host:");
		lblHost.setForeground(new Color(186, 85, 211));
		lblHost.setFont(new Font("Arial", Font.BOLD, 15));
		lblHost.setBounds(10, 77, 55, 18);
		add(lblHost);
		
		JLabel lblNewLabel = new JLabel("Config:");
		lblNewLabel.setForeground(new Color(186, 85, 211));
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 16));
		lblNewLabel.setBounds(10, 23, 117, 25);
		add(lblNewLabel);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(139, 0, 139));
		separator.setBounds(10, 59, 430, 2);
		add(separator);
		
		JLabel lblNewLabel_1 = new JLabel("Port:");
		lblNewLabel_1.setForeground(new Color(186, 85, 211));
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 15));
		lblNewLabel_1.setBounds(10, 106, 48, 18);
		add(lblNewLabel_1);
		
		JLabel txtAddress = new JLabel(this.host);
		txtAddress.setForeground(new Color(139, 0, 139));
		txtAddress.setFont(new Font("Arial", Font.ITALIC, 14));
		txtAddress.setBounds(60, 79, 361, 14);
		add(txtAddress);
		
		JLabel txtPort = new JLabel(this.port);
		txtPort.setForeground(new Color(139, 0, 139));
		txtPort.setFont(new Font("Arial", Font.ITALIC, 14));
		txtPort.setBounds(60, 108, 356, 14);
		add(txtPort);
		
		JLabel lblNewLabel_2 = new JLabel("Choose a cute name:");
		lblNewLabel_2.setForeground(new Color(186, 85, 211));
		lblNewLabel_2.setFont(new Font("Arial", Font.BOLD, 15));
		lblNewLabel_2.setBounds(10, 149, 161, 18);
		add(lblNewLabel_2);
		
		txtName = new JTextField();
		txtName.setForeground(new Color(138, 43, 226));
		txtName.setText(name);
		txtName.setFont(new Font("Arial", Font.PLAIN, 14));
		txtName.setBounds(166, 148, 143, 20);
		add(txtName);
		txtName.setColumns(10);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File js = new File("C:/Users/"+System.getProperty("user.name").toString()+"/Documents/Call My Cat/name.txt");
				name = txtName.getText().toString();
				try (PrintWriter writer = new PrintWriter(js);){
					writer.write(txtName.getText());
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnOk.setFont(new Font("Arial", Font.BOLD, 11));
		btnOk.setForeground(new Color(186, 85, 211));
		btnOk.setBackground(new Color(211, 211, 211));
		btnOk.setBounds(331, 148, 89, 23);
		add(btnOk);
		
		JButton btnNewButton = new JButton("Stop this server!");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}
	
	public String getName() {
		return name;
	}
	/**
	 * Create the panel.
	 */
	public panel() {
//		setBorder(new EmptyBorder(2, 2, 2, 2));
//		setForeground(new Color(148, 0, 211));
//		setBackground(new Color(255, 255, 255));
//		setLayout(null);
//		
//		JLabel lblHost = new JLabel("Host:");
//		lblHost.setForeground(new Color(186, 85, 211));
//		lblHost.setFont(new Font("Arial", Font.BOLD, 15));
//		lblHost.setBounds(10, 77, 55, 18);
//		add(lblHost);
//		
//		JLabel lblNewLabel = new JLabel("Config:");
//		lblNewLabel.setForeground(new Color(186, 85, 211));
//		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 16));
//		lblNewLabel.setBounds(10, 11, 117, 25);
//		add(lblNewLabel);
//		
//		JSeparator separator = new JSeparator();
//		separator.setForeground(new Color(139, 0, 139));
//		separator.setBounds(10, 59, 411, 2);
//		add(separator);
//		
//		JLabel lblNewLabel_1 = new JLabel("Port:");
//		lblNewLabel_1.setForeground(new Color(186, 85, 211));
//		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 15));
//		lblNewLabel_1.setBounds(10, 106, 48, 18);
//		add(lblNewLabel_1);
//		
//		JLabel txtAddress = new JLabel(this.host);
//		txtAddress.setForeground(new Color(139, 0, 139));
//		txtAddress.setFont(new Font("Arial", Font.ITALIC, 14));
//		txtAddress.setBounds(60, 79, 361, 14);
//		add(txtAddress);
//		
//		JLabel txtPort = new JLabel(this.port);
//		txtPort.setForeground(new Color(139, 0, 139));
//		txtPort.setFont(new Font("Arial", Font.ITALIC, 14));
//		txtPort.setBounds(60, 108, 356, 14);
//		add(txtPort);
//		
//		JLabel lblNewLabel_2 = new JLabel("Choose a cute name:");
//		lblNewLabel_2.setForeground(new Color(186, 85, 211));
//		lblNewLabel_2.setFont(new Font("Arial", Font.BOLD, 15));
//		lblNewLabel_2.setBounds(10, 149, 161, 18);
//		add(lblNewLabel_2);
//		
//		txtName = new JTextField();
//		txtName.setForeground(new Color(138, 43, 226));
//		txtName.setText(name);
//		txtName.setFont(new Font("Arial", Font.PLAIN, 14));
//		txtName.setBounds(166, 148, 143, 20);
//		add(txtName);
//		txtName.setColumns(10);
//		
//		JButton btnOk = new JButton("OK");
//		btnOk.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				File js = new File("name.txt");
//				try (PrintWriter writer = new PrintWriter(js);){
//					writer.write(txtName.getText());
//				} catch (FileNotFoundException e1) {
//					e1.printStackTrace();
//				}
//			}
//		});
//		btnOk.setFont(new Font("Arial", Font.BOLD, 11));
//		btnOk.setForeground(new Color(186, 85, 211));
//		btnOk.setBackground(new Color(211, 211, 211));
//		btnOk.setBounds(331, 148, 89, 23);
//		add(btnOk);
//		
//		JButton btnNewButton = new JButton("Stop this server!");
//		btnNewButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				System.exit(0);
//			}
//		});
//		btnNewButton.setForeground(new Color(186, 85, 211));
//		btnNewButton.setFont(new Font("Arial", Font.PLAIN, 11));
//		btnNewButton.setBounds(245, 14, 161, 23);
//		add(btnNewButton);
	}

}
