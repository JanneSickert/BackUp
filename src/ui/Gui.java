package ui;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import enums.RecoverOrUpdate;
import enums.SettingType;
import interfaces.UI;

public class Gui implements UI {

	public static SettingType type;
	public static boolean update;
	public static boolean messageNotSent = true;
	private static MoveGui moveGui = null;
	private final int FIELD_LENGTH = 50;

	class MyFrame extends JFrame {

		private static final long serialVersionUID = -7117249426309167751L;

		MyFrame(JPanel panel) {
			super();
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setTitle("BackUp program");
			setSize(700, 200);
			setLocation(900, 550);
			add(panel);
			setVisible(true);
		}
		
		public void end() {
			this.setVisible(false);
		}
	}

	@Override
	public void showHead() {
	}

	@Override
	public SettingType getSettings() {
		JPanel panel = new JPanel();
		final int NR_ELEMENTS = 4;
		panel.setLayout(new GridLayout(1, 4));
		JButton[] button = { new JButton(SettingType.COPY_ONLY.getName()), new JButton(SettingType.PASSWORD.getName()),
				new JButton(SettingType.KEY_FILE.getName()), new JButton(SettingType.PASSWORD_AND_KEY_FILE.getName()) };
		ActionListener[] al = { new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				type = SettingType.COPY_ONLY;
				messageNotSent = false;
			}
		}, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				type = SettingType.PASSWORD;
				messageNotSent = false;
			}
		}, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				type = SettingType.KEY_FILE;
				messageNotSent = false;
			}
		}, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				type = SettingType.PASSWORD_AND_KEY_FILE;
				messageNotSent = false;
			}
		} };
		for (int i = 0; i < NR_ELEMENTS; i++) {
			button[i].addActionListener(al[i]);
			panel.add(button[i]);
		}
		MyFrame frame = new MyFrame(panel);
		while (messageNotSent) {
			try {
				Thread.sleep(2);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		frame.end();
		return Gui.type;
	}

	class GetPath extends JPanel {

		private static final long serialVersionUID = -3384453354229902144L;
		private String path;

		GetPath(String name) {
			super();
			final int X = 200, Y = 50;
			messageNotSent = true;
			JLabel label = new JLabel(name);
			label.setLocation(0, 200);
			label.setSize(X, Y);
			JTextField jtf = new JTextField(FIELD_LENGTH);
			jtf.setLocation(X, 200);
			jtf.setSize(X, Y);
			JButton enter = new JButton("ENTER");
			enter.setLocation(X * 2, 200);
			enter.setSize(X, Y);
			enter.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					path = jtf.getText();
					messageNotSent = false;
				}
			});
			this.add(label);
			this.add(jtf);
			this.add(enter);
		}

		public String getPath() {
			return path;
		}
	}
	
	class GetPassword extends JPanel {

		private static final long serialVersionUID = -3384453354229902144L;
		private String password;

		GetPassword() {
			super();
			final int X = 200, Y = 50;
			messageNotSent = true;
			JLabel label = new JLabel("Password");
			label.setLocation(0, 200);
			label.setSize(X, Y);
			JPasswordField jpf = new JPasswordField(FIELD_LENGTH);
			jpf.setLocation(X, 200);
			jpf.setSize(X, Y);
			JButton enter = new JButton("ENTER");
			enter.setLocation(X * 2, 200);
			enter.setSize(X, Y);
			enter.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					password = new String(jpf.getPassword());
					messageNotSent = false;
				}
			});
			this.add(label);
			this.add(jpf);
			this.add(enter);
		}

		public String getPassword() {
			return password;
		}
	}
	
	private String askPath(String message) {
		GetPath gp = new GetPath(message);
		MyFrame frame = new MyFrame(gp);
		while (messageNotSent) {
			try {
				Thread.sleep(2);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		frame.end();
		return (gp.getPath());
	}

	@Override
	public String getSourceRootPath() {
		return (askPath("Path of folders to copy"));
	}

	@Override
	public String getDestinationRootPath() {
		return (askPath("Path where the backup should be saved"));
	}

	@Override
	public boolean updateOrRecover() {
		messageNotSent = true;
		JPanel panel = new JPanel();
		final int NR_ELEMENTS = 2;
		panel.setLayout(new GridLayout(1, 2));
		JButton[] button = { new JButton(RecoverOrUpdate.RECOVER.getName()),
				new JButton(RecoverOrUpdate.UPDATE.getName()) };
		ActionListener[] al = { new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Gui.update = false;
				messageNotSent = false;
			}
		}, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Gui.update = true;
				messageNotSent = false;
			}
		} };
		for (int i = 0; i < NR_ELEMENTS; i++) {
			button[i].addActionListener(al[i]);
			panel.add(button[i]);
		}
		MyFrame frame = new MyFrame(panel);
		while (messageNotSent) {
			try {
				Thread.sleep(2);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		frame.end();
		return Gui.update;
	}

	@Override
	public String getPathForKeyFile() {
		return (askPath("Path where the key file should be stored"));
	}

	@Override
	public String getPassword() {
		GetPassword gp = new GetPassword();
		MyFrame frame = new MyFrame(gp);
		while (messageNotSent) {
			try {
				Thread.sleep(2);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		String password = gp.getPassword();
		frame.end();
		return password;
	}

	@Override
	public String getRecoveryOutputPath() {
		return (askPath("Target directory of the data to be restored"));
	}
	
	class MoveGui {
		
		private JPanel movePanel = new JPanel();
		private JLabel moveLabel = new JLabel();
		private Font f = new Font("SansSerif", Font.LAYOUT_LEFT_TO_RIGHT, 14 );
		public MyFrame frame;
		
		MoveGui() {
			moveLabel.setFont(f);
			movePanel.add(moveLabel);
			frame = new MyFrame(movePanel);
		}
		
		public void setText(String from, String to) {
			moveLabel.setText("Move " + from + " to " + to);
		}
	}

	@Override
	public void move(String from, String to) {
		if (moveGui == null) {
			moveGui = new MoveGui();
		}
		moveGui.setText(from, to);
	}

	@Override
	public void finishMessage() {
		moveGui.frame.setVisible(false);
	}
}