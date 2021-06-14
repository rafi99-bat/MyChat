package com.project.mychat;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtAddress;
	private JLabel lblAddress;
	private JTextField txtPort;
	
	private LoginButton handle = new LoginButton();

	public Login() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		setResizable(false);
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 380);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtName = new JTextField();
		txtName.setBounds(64, 55, 165, 20);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(127, 39, 39, 14);
		contentPane.add(lblName);
		
		txtAddress = new JTextField();
		txtAddress.setBounds(64, 111, 165, 20);
		contentPane.add(txtAddress);
		txtAddress.setColumns(10);
		
		lblAddress = new JLabel("IP Address:");
		lblAddress.setBounds(114, 95, 65, 14);
		contentPane.add(lblAddress);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(133, 164, 27, 14);
		contentPane.add(lblPort);
		
		txtPort = new JTextField();
		txtPort.setColumns(10);
		txtPort.setBounds(64, 180, 165, 20);
		contentPane.add(txtPort);
		
		JLabel lbAddressDesc = new JLabel("(eg. 192.168.0.2)");
		lbAddressDesc.setBounds(100, 133, 93, 14);
		contentPane.add(lbAddressDesc);
		
		JLabel lbPortDesc = new JLabel("(eg. 4420)");
		lbPortDesc.setBounds(119, 203, 56, 14);
		contentPane.add(lbPortDesc);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(handle);
		btnLogin.setBounds(98, 286, 98, 26);
		contentPane.add(btnLogin);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
