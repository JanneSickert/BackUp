package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFileChooser;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import enums.RecoverOrUpdate;
import enums.SettingType;
import interfaces.UI;
import test.Check_IO;

public class Gui implements UI {

	public static SettingType type;
	public static boolean update;
	public static boolean messageNotSent = true;
	private static MoveGui moveGui = null;
	private final int FIELD_LENGTH = 50;
	private static long copyedLength = 0L;
	private final String ERROR_MESSAGE = "The text field is empty or does not contain a valid file path.";
	private final String[] ERROR_MESSAGE_PASSWORD = { "The password field is empty!", "The passwords are different!" };
	private Timer timer;

	private MyFrame loadingFrame = null;

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
		messageNotSent = true;
		final int NR_ELEMENTS = 4;
		JPanel panel = new JPanel();
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

		private class ChoosePath {

			private String cD;

			/**
			 * Open a frame to choose a path.
			 */
			public ChoosePath() {
				JFileChooser chooser = new JFileChooser();
				chooser.showSaveDialog(null);
				cD = chooser.getSelectedFile().getAbsolutePath();
			}

			/**
			 * 
			 * @return The path which was selected in the constructor.
			 */
			public String getPath() {
				return this.cD;
			}
		}

		private boolean validPath(String path) {
			boolean[] b = { false, false };
			for (int i = 0; i < path.length(); i++) {
				if (path.charAt(i) == ':') {
					b[0] = true;
				}
			}
			for (int i = 0; i < path.length(); i++) {
				if (path.charAt(i) == '/' || path.charAt(i) == '\\') {
					b[1] = true;
				}
			}
			if (path.equals("")) {
				return false;
			} else {
				return (b[0] && b[1]);
			}
		}

		GetPath(String name) {
			super();
			messageNotSent = true;
			JLabel label = new JLabel(name);
			JTextField jtf = new JTextField(FIELD_LENGTH);
			JButton enter = new JButton("ENTER");
			ActionListener lal = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					path = jtf.getText();
					if (!(validPath(path))) {
						JOptionPane.showMessageDialog(null, ERROR_MESSAGE, "ERROR", JOptionPane.OK_CANCEL_OPTION);
					} else {
						messageNotSent = false;
					}
				}
			};
			enter.addActionListener(lal);
			jtf.addKeyListener(new KeyHandler(lal));
			JButton fileChooserButton = new JButton("Open file chooser");
			fileChooserButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ChoosePath cp = new ChoosePath();
					String local_path = cp.getPath();
					jtf.setText(local_path);
				}
			});
			this.add(label);
			this.add(jtf);
			this.add(enter);
			this.add(fileChooserButton);
		}

		public String getPath() {
			return path;
		}
	}

	class KeyHandler implements KeyListener {

		private ActionListener al;

		KeyHandler(ActionListener al) {
			this.al = al;
		}

		@Override
		public void keyPressed(KeyEvent arg0) {
			switch (arg0.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				al.actionPerformed(null);
				break;
			default:
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
		}
	}

	class GetPassword extends JPanel {

		private static final long serialVersionUID = -3384453354229902144L;
		private String password, confirm_password;

		GetPassword() {
			super();
			setLayout(null);
			messageNotSent = true;
			int SIZE_X = 150, SIZE_Y = 20;
			JLabel label = new JLabel("Password:");
			label.setLocation(2, 2);
			label.setSize(SIZE_X, SIZE_Y);
			JPasswordField jpf = new JPasswordField(FIELD_LENGTH);
			jpf.setLocation(200, 2);
			jpf.setSize(SIZE_X * 3, SIZE_Y);
			JLabel label2 = new JLabel("confirm password:");
			label2.setLocation(2, 50);
			label2.setSize(SIZE_X, SIZE_Y);
			JPasswordField jpf_confirm = new JPasswordField(FIELD_LENGTH);
			jpf_confirm.setLocation(200, 50);
			jpf_confirm.setSize(SIZE_X * 3, SIZE_Y);
			JButton enter = new JButton("ENTER");
			enter.setLocation(200, 90);
			enter.setSize(SIZE_X, (int) (SIZE_Y * 1.5));
			ActionListener lal = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					password = new String(jpf.getPassword());
					confirm_password = new String(jpf_confirm.getPassword());
					if (password.equals("") || confirm_password.equals("")) {
						JOptionPane.showMessageDialog(null, ERROR_MESSAGE_PASSWORD[0], "ERROR",
								JOptionPane.OK_CANCEL_OPTION);
					} else {
						if (password.equals(confirm_password)) {
							messageNotSent = false;
						} else {
							JOptionPane.showMessageDialog(null, ERROR_MESSAGE_PASSWORD[1], "ERROR",
									JOptionPane.OK_CANCEL_OPTION);
						}
					}
				}
			};
			enter.addActionListener(lal);
			KeyHandler kh = new KeyHandler(lal);
			jpf.addKeyListener(kh);
			jpf_confirm.addKeyListener(kh);
			this.add(label);
			this.add(jpf);
			this.add(label2);
			this.add(jpf_confirm);
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

	static class MovePanel extends JPanel {

		private static final long serialVersionUID = -5491547277707113467L;
		static final int X = 700, Y = 30;

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.BLACK);
			g2d.drawRect(3, 4 * Y, X - 50, Y);
			g2d.setColor(Color.GREEN);
			double dd = ((double) Gui.copyedLength / main.Main.lengthOfAllFiles) * (X - 50);
			int x_value = (int) Math.round(dd);
			g2d.fillRect(4, 4 * Y + 1, x_value - 1, Y - 1);
		}
	}

	public class MoveGui {

		private MovePanel movePanel = new MovePanel();
		private JLabel[] moveLabel = new JLabel[4];
		public MyFrame frame;

		public MoveGui() {
			movePanel.setLayout(null);
			for (int i = 0; i < moveLabel.length; i++) {
				moveLabel[i] = new JLabel();
				if (i % 2 == 0) {
					moveLabel[i].setSize(MovePanel.X, MovePanel.Y);
					moveLabel[i].setLocation(0, i * MovePanel.Y);
				} else {
					moveLabel[i].setSize(MovePanel.X, MovePanel.Y);
					moveLabel[i].setLocation(50, i * MovePanel.Y);
				}
				movePanel.add(moveLabel[i]);
			}
			frame = new MyFrame(movePanel);
		}

		@Check_IO(input = { "C:/from/testFile.txt", "C:/to/0" })
		public synchronized void setText(String from, String to) {
			String[] text = { "Move:", from, "to:", to };
			for (int i = 0; i < moveLabel.length; i++) {
				moveLabel[i].setText(text[i]);
			}
			movePanel.revalidate();
			movePanel.repaint();
		}
	}

	@Override
	public synchronized void move(String from, String to, long lengthFile) {
		if (moveGui == null) {
			moveGui = new MoveGui();
		}
		moveGui.setText(from, to);
		copyedLength = copyedLength + lengthFile;
	}

	@Override
	public void finishMessage() {
		System.exit(0);
	}

	static class LoadingPannel extends JPanel {

		private static final long serialVersionUID = -683315803839846527L;
		private String message = null;
		private int ballIndex = 0;

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.BLACK);
			final int X = 510, Y = 10, SIZE = 140;
			final int r = 60;
			g2d.drawOval(X, Y, SIZE, SIZE);
			g2d.drawOval(X + 20, Y + 20, SIZE - 40, SIZE - 40);
			Font font = new Font("Helvetica", Font.BOLD, 30);
			g2d.setFont(font);
			g2d.setColor(new Color(0, 155, 0));
			g2d.drawString(message, 10, 70);
			g2d.setColor(new Color(0, 0, 155));
			g2d.fillOval(((int) Math.round(r * Math.cos((double) (ballIndex / Math.PI)) + X + r)),
					((int) Math.round(r * Math.sin((double) (ballIndex / Math.PI)) + Y + r)), 20, 20);
		}

		LoadingPannel(String message) {
			this.message = message;
		}

		public void nextBallIndex() {
			ballIndex++;
			if (ballIndex == Integer.MAX_VALUE) {
				ballIndex = 0;
			}
		}
	}

	@Override
	public void showLoadingScreen(String message) {
		LoadingPannel pannel = new LoadingPannel(message);
		loadingFrame = new MyFrame(pannel);
		timer = new Timer(120, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pannel.nextBallIndex();
				pannel.revalidate();
				pannel.repaint();
			}
		});
		timer.start();
		loadingFrame.setVisible(true);
	}

	@Override
	public void closeLoadingScreen() {
		timer.stop();
		loadingFrame.setVisible(false);
	}

	@Override
	public void showNotFoundFiles(ArrayList<File> list) {
		ArrayList<String> string_list = new ArrayList<String>();
		for (File f : list) {
			string_list.add(f.getAbsolutePath());
		}
		new MyFrame(new MyMissingPannel(string_list));
	}

	static class MyMissingPannel extends JPanel {

		private static final long serialVersionUID = 3391336325888199776L;

		public MyMissingPannel(ArrayList<String> list) {
			super.setLayout(new GridLayout(2, 1));
			JTextArea output = new JTextArea();
			output.setLocation(20, 100);
			output.setSize(660, 600);
			String text = "";
			for (int i = 0; i < (list.size() - 1); i++) {
				text = text + list.get(i) + "\n";
			}
			text = text + list.get(list.size() - 1);
			output.setText(text);
			JScrollPane scroll = new JScrollPane();
			scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scroll.setViewportView(output);
			final String content = "<html><b><font size=\"30\">These files could not be processed.</font></b></html>";
			JLabel label = new JLabel(content, JLabel.CENTER);
			label.setSize(700, 100);
			add(label);
			add(scroll);
		}
	}
}