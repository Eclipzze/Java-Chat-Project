package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.CardLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;

import client.Client;
import objects.User;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JScrollPane;

public class Window {

	private JFrame frame;
	private JTextField txtUsername;
	private Client socketClient;
	private JTextField txtPassword;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Window() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 763, 429);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));
		
		JPanel panelLogin = new JPanel();
		frame.getContentPane().add(panelLogin, "name_283200026755400");
		panelLogin.setLayout(null);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				User user = new User();
				System.out.println(Thread.currentThread().getName());
				user.setUsername(txtUsername.getText());
				user.setPassword(txtPassword.getText());
				user.setLoggedIn(false);
				System.out.println("Cliecked");
				//socketClient = new Client();
				
				Thread a = new Thread(new Client(user));
				a.start();

				//socketClient.au(user);
				
			}
		});
		btnLogin.setBounds(384, 167, 97, 25);
		panelLogin.add(btnLogin);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(110, 167, 244, 25);
		panelLogin.add(txtUsername);
		txtUsername.setColumns(10);
		
		txtPassword = new JTextField();
		txtPassword.setBounds(110, 215, 244, 25);
		panelLogin.add(txtPassword);
		txtPassword.setColumns(10);
		
		JPanel panelMain = new JPanel();
		frame.getContentPane().add(panelMain, "name_283202674467900");
		panelMain.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(12, 13, 56, 16);
		panelMain.add(lblNewLabel);
		
		JList list = new JList();
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {""};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		list.setBounds(573, 12, 160, 357);
		panelMain.add(list);
	}
}
