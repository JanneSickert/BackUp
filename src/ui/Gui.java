package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFileChooser;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
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
				@SuppressWarnings("unused")
				int c = chooser.showSaveDialog(null);
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
					messageNotSent = false;
				}
			};
			enter.addActionListener(lal);
			jtf.addKeyListener(new KeyHandler(lal));
			JButton fileChooserButton = new JButton("Open file chooser");
			fileChooserButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ChoosePath cp = new ChoosePath();
					jtf.setText(cp.getPath());
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
	
	class KeyHandler implements KeyListener{

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
		private String password;

		GetPassword() {
			super();
			messageNotSent = true;
			JLabel label = new JLabel("Password");
			JPasswordField jpf = new JPasswordField(FIELD_LENGTH);
			JButton enter = new JButton("ENTER");
			ActionListener lal = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					password = new String(jpf.getPassword());
					messageNotSent = false;
				}
			};
			enter.addActionListener(lal);
			jpf.addKeyListener(new KeyHandler(lal));
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
		moveGui.frame.setVisible(false);
	}
}