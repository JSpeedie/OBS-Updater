import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.UIManager.*;
import javax.swing.event.*;
import javax.swing.text.*;

import static java.nio.file.StandardCopyOption.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Updater implements ActionListener {
	private final Map<String, ImageIcon> StockIconMap = new HashMap<>();
	/* Files for program */
	private File namesFile;
	private File leftNameFile;
	private File left2NameFile;
	private File rightNameFile;
	private File right2NameFile;
	private File leftCommentatorNameFile;
	private File rightCommentatorNameFile;
	private File leftScoreFile;
	private File rightScoreFile;
	private File bracketPositionFile;
	private File roundFormatFile;
	private File leftStockIconFile;
	private File left2StockIconFile;
	private File rightStockIconFile;
	private File right2StockIconFile;
	private File stockIconDir;
	private File portsDir;
	private File leftPortFile;
	private File left2PortFile;
	private File rightPortFile;
	private File right2PortFile;
	/* Local variables for score etc */
	private ArrayList<String> namesList = new ArrayList<>();
	private ArrayList<String> iconsList = new ArrayList<>();
	private ArrayList<String> portsList = new ArrayList<>();
	private String leftNameValue = "unnamed";
	private String leftNameValue2 = "unnamed";
	private String rightNameValue = "unnamed";
	private String rightNameValue2 = "unnamed";
	private String leftCommentatorNameValue = "unnamed";
	private String rightCommentatorNameValue = "unnamed";
	private int leftScoreValue = 0;
	private int rightScoreValue = 0;
	private int scoreY;
	/* GUI variables */
	final int gap_width = 15;
	final int small_gap_width = 5;
	final int element_height = 30;
	final int dir_text_element_height = 24;
	final int button_width = 80;
	final int combo_box_width = 190;
	final int icon_preview_width = 24;
	final int dir_text_field_width = 225;
	final int window_width = (2 * combo_box_width) + (button_width) + (10 * small_gap_width);
	final int window_height = (16 * element_height) + (6 * small_gap_width) + (10 * gap_width);
	final int name_font_size = 16;
	final int score_font_size = 36;
	final int text_field_font_size = 14;
	final int dir_text_field_font_size = 13;
	final int stock_visible_rows = 26;
	final int names_visible_rows = 30;
	private boolean show2 = true;

	/* Aliased GUI classes {{{ */
	public class JAliasedTextField extends JTextField {
		public JAliasedTextField(String s) {
			super(s);
		}

		@Override
		public void paintComponent(Graphics g) {
			Graphics2D graphics2d = (Graphics2D) g;
			graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
			super.paintComponent(g);
		}
	}

	public class JAliasedButton extends JButton {
		public JAliasedButton(String s) {
			super(s);
		}

		@Override
		public void paintComponent(Graphics g) {
			Graphics2D graphics2d = (Graphics2D) g;
			graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
			super.paintComponent(g);
		}
	}

	public class JAliasedComboBox extends JComboBox {
		public JAliasedComboBox() {
			super();
		}

		@Override
		public void paintComponent(Graphics g) {
			Graphics2D graphics2d = (Graphics2D) g;
			graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
			super.paintComponent(g);
		}
	}

	public class JAliasedLabel extends JLabel {
		public JAliasedLabel(String s) {
			super(s);
		}

		@Override
		public void paintComponent(Graphics g) {
			Graphics2D graphics2d = (Graphics2D) g;
			graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
			super.paintComponent(g);
		}
	}
	/* }}} */

	/* Editing elements */
	private JAliasedTextField leftScore;
	private JAliasedTextField rightScore;
	private JAliasedButton show2Button;
	private JAliasedButton leftScoreInc;
	private JAliasedButton leftScoreDec;
	private JAliasedButton rightScoreInc;
	private JAliasedButton rightScoreDec;
	private JAliasedComboBox leftName1;
	private JAliasedComboBox leftName2;
	private JAliasedComboBox rightName1;
	private JAliasedComboBox rightName2;
	private JAliasedComboBox leftStockIconCombo1;
	private JAliasedComboBox leftStockIconCombo2;
	private JAliasedComboBox rightStockIconCombo1;
	private JAliasedComboBox rightStockIconCombo2;
	private JAliasedComboBox leftPortCombo1;
	private JAliasedComboBox leftPortCombo2;
	private JAliasedComboBox rightPortCombo1;
	private JAliasedComboBox rightPortCombo2;
	private JAliasedComboBox leftCommentatorName;
	private JAliasedComboBox rightCommentatorName;
	private JList leftStockList1;
	private JList leftStockList2;
	private JList rightStockList1;
	private JList rightStockList2;
	private JAliasedTextField bracketPosition;
	private JAliasedTextField roundFormat;
	private JAliasedButton switchStockIcons1;
	private JAliasedButton switchStockIcons2;
	private JAliasedButton switchNames1;
	private JAliasedButton switchNames2;
	private JAliasedButton switchScore;
	private JAliasedButton switchCommentatorNames;
	private JAliasedButton reloadFilesButton;
	private JAliasedLabel leftLabel1;
	private JAliasedLabel leftLabel2;
	private JAliasedLabel rightLabel1;
	private JAliasedLabel rightLabel2;
	private JAliasedLabel leftScoreLabel;
	private JAliasedLabel rightScoreLabel;
	private JAliasedLabel bracketPositionLabel;
	private JAliasedLabel roundFormatLabel;
	private JAliasedLabel leftCommentatorLabel;
	private JAliasedLabel rightCommentatorLabel;
	private JLabel leftStockIconPreview1;
	private JLabel leftStockIconPreview2;
	private JLabel rightStockIconPreview1;
	private JLabel rightStockIconPreview2;
	/* Settings elements */
	private JAliasedTextField namesText;
	private JAliasedTextField leftNameText;
	private JAliasedTextField left2NameText;
	private JAliasedTextField rightNameText;
	private JAliasedTextField right2NameText;
	private JAliasedTextField leftCommentatorNameText;
	private JAliasedTextField rightCommentatorNameText;
	private JAliasedTextField leftScoreText;
	private JAliasedTextField rightScoreText;
	private JAliasedTextField bracketPositionText;
	private JAliasedTextField roundFormatText;
	private JAliasedTextField leftStockIconText;
	private JAliasedTextField left2StockIconText;
	private JAliasedTextField rightStockIconText;
	private JAliasedTextField right2StockIconText;
	private JAliasedTextField StockIconDirText;
	private JAliasedTextField PortsDirText;
	private JAliasedTextField leftPortText;
	private JAliasedTextField left2PortText;
	private JAliasedTextField rightPortText;
	private JAliasedTextField right2PortText;

	/* Custom Cell Renderer {{{ */
	public class StockIconListCellRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList list, Object obj, int index,
			boolean isSelected, boolean cellHasFocus) {

			JLabel item = (JLabel) super.getListCellRendererComponent(list, obj, index, isSelected,
				cellHasFocus);
			item.setIcon(StockIconMap.get((String) obj));
			item.setText("");
			return item;
		}
	}
	/* }}} */

	private void setFilePaths() {
		namesFile = new File(namesText.getText());
		leftNameFile = new File(leftNameText.getText());
		left2NameFile = new File(left2NameText.getText());
		rightNameFile = new File(rightNameText.getText());
		right2NameFile = new File(right2NameText.getText());
		leftCommentatorNameFile = new File(leftCommentatorNameText.getText());
		rightCommentatorNameFile = new File(rightCommentatorNameText.getText());
		leftScoreFile = new File(leftScoreText.getText());
		rightScoreFile = new File(rightScoreText.getText());
		bracketPositionFile = new File(bracketPositionText.getText());
		roundFormatFile = new File(roundFormatText.getText());
		leftStockIconFile = new File(leftStockIconText.getText());
		left2StockIconFile = new File(left2StockIconText.getText());
		rightStockIconFile = new File(rightStockIconText.getText());
		right2StockIconFile = new File(right2StockIconText.getText());
		stockIconDir = new File(StockIconDirText.getText());
		portsDir = new File(PortsDirText.getText());
		leftPortFile = new File(leftPortText.getText());
		left2PortFile = new File(left2PortText.getText());
		rightPortFile = new File(rightPortText.getText());
		right2PortFile = new File(right2PortText.getText());
	}

	private void readNames() {
		try {
			namesList.clear();
			BufferedReader reader = new BufferedReader(new FileReader(namesFile));
			String line;
			while ((line = reader.readLine()) != null) {
				namesList.add(line);
			}
		} catch (Exception e) {
			System.err.println("Error: Failed to read from File");
		}
	}

	private void readFromFile(File f, JTextComponent component) {
		String read;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(f));
			if ((read = reader.readLine()) != null) {
				component.setText("" + read);
			} else {
				System.err.println("Error: could not read from File");
			}
		} catch (Exception e) {
			System.err.println("Error: could not read from File");
		}
	}

	private String stringFromFile(File f) {
		String ret = "";
		String read;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(f));
			while ((read = reader.readLine()) != null) {
				ret += read;
			}

			return ret;
		} catch (Exception e) {
			System.err.println("Error: could not read from File");
			return "";
		}
	}

	private void writeToFile(String toWrite, File f) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			writer.write(toWrite);
			writer.close();
		} catch (Exception e) {
			System.err.println("Error: Failed to write to File");
		}
	}

	private void loadStockIcons() {
		try {
			File[] icons = stockIconDir.listFiles();
			iconsList.clear();

			/* Perform a null-check in case the path does not denote a directory */
			if (icons != null) {
				for (int i = 0; i < icons.length; i++) {
					iconsList.add(icons[i].getName());
				}
				Collections.sort(iconsList);
			} else {
				System.err.println("Warning: stockIconDir is not a directory");
			}
		} catch (Exception e) {
			System.err.println("Error: could not load stock icon directories");
		}
	}

	private void loadPorts() {
		try {
			File[] ports = portsDir.listFiles();
			portsList.clear();

			/* Perform a null-check in case the path does not denote a directory */
			if (ports != null) {
				for (int i = 0; i < ports.length; i++) {
					portsList.add(ports[i].getName());
				}
				Collections.sort(portsList);
			} else {
				System.err.println("Warning: portsDir is not a directory");
			}
		} catch (Exception e) {
			System.err.println("Error: could not load ports directories");
		}
	}

	private void updateElements() {
		readNames();
		loadStockIcons();
		loadPorts();
		leftName1.removeAllItems();
		leftName2.removeAllItems();
		rightName1.removeAllItems();
		rightName2.removeAllItems();
		leftCommentatorName.removeAllItems();
		rightCommentatorName.removeAllItems();
		leftStockIconCombo1.removeAllItems();
		leftStockIconCombo2.removeAllItems();
		rightStockIconCombo1.removeAllItems();
		rightStockIconCombo2.removeAllItems();
		leftPortCombo1.removeAllItems();
		leftPortCombo2.removeAllItems();
		rightPortCombo1.removeAllItems();
		rightPortCombo2.removeAllItems();
		for (int i = 0; i < namesList.size(); i++) {
			leftName1.addItem(namesList.get(i));
			leftName2.addItem(namesList.get(i));
			rightName1.addItem(namesList.get(i));
			rightName2.addItem(namesList.get(i));
			leftCommentatorName.addItem(namesList.get(i));
			rightCommentatorName.addItem(namesList.get(i));
		}
		for (int i = 0; i < iconsList.size(); i++) {
			leftStockIconCombo1.addItem(iconsList.get(i));
			leftStockIconCombo2.addItem(iconsList.get(i));
			rightStockIconCombo1.addItem(iconsList.get(i));
			rightStockIconCombo2.addItem(iconsList.get(i));
		}
		for (int i = 0; i < portsList.size(); i++) {
			leftPortCombo1.addItem(portsList.get(i));
			leftPortCombo2.addItem(portsList.get(i));
			rightPortCombo1.addItem(portsList.get(i));
			rightPortCombo2.addItem(portsList.get(i));
		}
		readFromFile(leftScoreFile, (JTextComponent) leftScore);
		// leftScoreValue = Integer.parseInt(leftScore.getText());
		readFromFile(rightScoreFile, (JTextComponent) rightScore);
		// rightScoreValue = Integer.parseInt(rightScore.getText());
		readFromFile(bracketPositionFile, (JTextComponent) bracketPosition);
		readFromFile(roundFormatFile, (JTextComponent) roundFormat);
		leftName1.setSelectedItem("" + stringFromFile(leftNameFile));
		leftName2.setSelectedItem("" + stringFromFile(left2NameFile));
		rightName1.setSelectedItem("" + stringFromFile(rightNameFile));
		rightName2.setSelectedItem("" + stringFromFile(right2NameFile));
		leftCommentatorName.setSelectedItem("" + stringFromFile(leftCommentatorNameFile));
		rightCommentatorName.setSelectedItem("" + stringFromFile(rightCommentatorNameFile));
	}

	private void setScorePositions() {
		leftScoreLabel.setBounds(
			leftName1.getX(),
			scoreY,
			combo_box_width,
			element_height);
		rightScoreLabel.setBounds(
			rightName1.getX(),
			leftScoreLabel.getY(),
			combo_box_width,
			element_height);
		leftScoreDec.setBounds(
			leftScoreLabel.getX(),
			leftScoreLabel.getY() + leftScoreLabel.getHeight() + small_gap_width,
			(combo_box_width - (2 * small_gap_width)) / 3,
			(int) (element_height * 1.5));
		leftScore.setBounds(
			leftScoreDec.getX() + leftScoreDec.getWidth() + 5,
			leftScoreDec.getY(),
			(combo_box_width - (2 * small_gap_width)) / 3,
			(int) (element_height * 1.5));
		leftScoreInc.setBounds(
			leftScore.getX() + leftScore.getWidth() + 5,
			leftScoreDec.getY(),
			(combo_box_width - (2 * small_gap_width)) / 3,
			(int) (element_height * 1.5));
		switchScore.setBounds(switchNames1.getX(), leftScoreInc.getY(), button_width, (int) (element_height * 1.5));
		rightScoreDec.setBounds(
			rightName1.getX(),
			leftScoreDec.getY(),
			(combo_box_width - (2 * small_gap_width)) / 3,
			(int) (element_height * 1.5));
		rightScore.setBounds(
			rightScoreDec.getX() + rightScoreDec.getWidth() + 5,
			leftScoreDec.getY(),
			(combo_box_width - (2 * small_gap_width)) / 3,
			(int) (element_height * 1.5));
		rightScoreInc.setBounds(
			rightScore.getX() + rightScore.getWidth() + 5,
			leftScoreDec.getY(),
			(combo_box_width - (2 * small_gap_width)) / 3,
			(int) (element_height * 1.5));
		bracketPositionLabel.setBounds(
			rightScoreDec.getX(),
			rightScoreDec.getY() + rightScoreDec.getHeight() + small_gap_width,
			combo_box_width,
			element_height);
		roundFormatLabel.setBounds(
			leftScoreDec.getX(),
			bracketPositionLabel.getY(),
			combo_box_width,
			element_height);
		bracketPosition.setBounds(
			leftName1.getX(),
			bracketPositionLabel.getY() + bracketPositionLabel.getHeight() + small_gap_width,
			combo_box_width,
			element_height);
		roundFormat.setBounds(
			rightScoreDec.getX(),
			bracketPosition.getY(),
			combo_box_width,
			element_height);
		leftCommentatorLabel.setBounds(
			bracketPosition.getX(),
			bracketPosition.getY() + bracketPosition.getHeight() + small_gap_width,
			combo_box_width,
			element_height);
		rightCommentatorLabel.setBounds(
			roundFormat.getX(),
			roundFormat.getY() + roundFormat.getHeight() + small_gap_width,
			combo_box_width,
			element_height);
		leftCommentatorName.setBounds(
			leftCommentatorLabel.getX(),
			leftCommentatorLabel.getY() + leftCommentatorLabel.getHeight() + small_gap_width,
			combo_box_width,
			element_height);
		switchCommentatorNames.setBounds(
			leftCommentatorName.getX() + leftCommentatorName.getWidth() + small_gap_width,
			leftCommentatorName.getY(),
			button_width,
			element_height);
		rightCommentatorName.setBounds(
			switchCommentatorNames.getX() + switchCommentatorNames.getWidth() + small_gap_width,
			leftCommentatorName.getY(),
			combo_box_width,
			element_height);
	}

	private void set2ElementsVisibility(boolean visibility) {
		leftLabel2.setVisible(visibility);
		leftStockIconPreview2.setVisible(visibility);
		leftStockIconCombo2.setVisible(visibility);
		leftStockList2.setVisible(visibility);
		leftPortCombo2.setVisible(visibility);
		leftName2.setVisible(visibility);
		switchStockIcons2.setVisible(visibility);
		switchNames2.setVisible(visibility);
		rightLabel2.setVisible(visibility);
		rightStockIconPreview2.setVisible(visibility);
		rightStockIconCombo2.setVisible(visibility);
		rightStockList2.setVisible(visibility);
		rightPortCombo2.setVisible(visibility);
		rightName2.setVisible(visibility);
	}

	private void setScoreY() {
		if (show2) {
			scoreY = leftName2.getY() + leftName2.getHeight() + small_gap_width;
		} else {
			scoreY = leftName1.getY() + leftName1.getHeight() + small_gap_width;
		}
		set2ElementsVisibility(show2);
	}

	private void toggle2() {
		setScoreY();
		setScorePositions();
		if (show2) {
			show2Button.setText("-");
		} else {
			show2Button.setText("+");
		}
	}

	public Updater() {
		/* TODO: replace this with more robust system */
		final String InitialBaseDirText = "/home/me/_obs";
		final String InitialNamesText = InitialBaseDirText + "/names.txt";
		final String InitialLeftNameText = InitialBaseDirText + "/left_name.txt";
		final String InitialLeft2NameText = InitialBaseDirText + "/left_name2.txt";
		final String InitialRightNameText = InitialBaseDirText + "/right_name.txt";
		final String InitialRight2NameText = InitialBaseDirText + "/right_name2.txt";
		final String InitialLeftCommentatorNameText = InitialBaseDirText + "/left_commentator_name.txt";
		final String InitialRightCommentatorNameText = InitialBaseDirText + "/right_commentator_name.txt";
		final String InitialLeftScoreText = InitialBaseDirText + "/left_score.txt";
		final String InitialRightScoreText = InitialBaseDirText + "/right_score.txt";
		final String InitialBracketPositionText = InitialBaseDirText + "/bracket_position.txt";
		final String InitialRoundFormatText = InitialBaseDirText + "/round_format.txt";
		final String InitialLeftStockIconText = InitialBaseDirText + "/left_stock_icon.png";
		final String InitialLeft2StockIconText = InitialBaseDirText + "/left_stock_icon2.png";
		final String InitialRightStockIconText = InitialBaseDirText + "/right_stock_icon.png";
		final String InitialRight2StockIconText = InitialBaseDirText + "/right_stock_icon2.png";
		final String InitialStockIconText = InitialBaseDirText + "/stock_icons";
		final String InitialPortsText = InitialBaseDirText + "/ports";
		final String InitialLeftPortText = InitialBaseDirText + "/left_port.png";
		final String InitialLeft2PortText = InitialBaseDirText + "/left_port2.png";
		final String InitialRightPortText = InitialBaseDirText + "/right_port.png";
		final String InitialRight2PortText = InitialBaseDirText + "/right_port2.png";
		/* Sets GUI style/theme */
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("GTK+".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e2) {
				System.err.println("Error: could not find system look and feel. Exiting...");
			}
		}
		/* Settings elements instantiation */
		namesText = new JAliasedTextField(InitialNamesText);
		leftNameText = new JAliasedTextField(InitialLeftNameText);
		left2NameText = new JAliasedTextField(InitialLeft2NameText);
		rightNameText = new JAliasedTextField(InitialRightNameText);
		right2NameText = new JAliasedTextField(InitialRight2NameText);
		leftCommentatorNameText = new JAliasedTextField(InitialLeftCommentatorNameText);
		rightCommentatorNameText = new JAliasedTextField(InitialRightCommentatorNameText);
		leftScoreText = new JAliasedTextField(InitialLeftScoreText);
		rightScoreText = new JAliasedTextField(InitialRightScoreText);
		bracketPositionText = new JAliasedTextField(InitialBracketPositionText);
		roundFormatText = new JAliasedTextField(InitialRoundFormatText);
		leftStockIconText = new JAliasedTextField(InitialLeftStockIconText);
		left2StockIconText = new JAliasedTextField(InitialLeft2StockIconText);
		rightStockIconText = new JAliasedTextField(InitialRightStockIconText);
		right2StockIconText = new JAliasedTextField(InitialRight2StockIconText);
		StockIconDirText = new JAliasedTextField(InitialStockIconText);
		PortsDirText = new JAliasedTextField(InitialPortsText);
		leftPortText = new JAliasedTextField(InitialLeftPortText);
		left2PortText = new JAliasedTextField(InitialLeft2PortText);
		rightPortText = new JAliasedTextField(InitialRightPortText);
		right2PortText = new JAliasedTextField(InitialRight2PortText);

		/* Reset the 2 score files to initial score */
		setFilePaths();
		JTabbedPane tabbedPane = new JTabbedPane() {
			@Override
			public void paintComponent(Graphics g) {
				Graphics2D graphics2d = (Graphics2D) g;
				graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
				super.paintComponent(g);
			}
		};
		leftScore = new JAliasedTextField("");
		rightScore = new JAliasedTextField("");
		show2Button = new JAliasedButton("t");
		leftScoreInc = new JAliasedButton("+");
		leftScoreDec = new JAliasedButton("-");
		rightScoreInc = new JAliasedButton("+");
		rightScoreDec = new JAliasedButton("-");
		leftName1 = new JAliasedComboBox();
		leftName2 = new JAliasedComboBox();
		rightName1 = new JAliasedComboBox();
		rightName2 = new JAliasedComboBox();
		leftStockIconCombo1 = new JAliasedComboBox();
		leftStockIconCombo2 = new JAliasedComboBox();
		rightStockIconCombo1 = new JAliasedComboBox();
		rightStockIconCombo2 = new JAliasedComboBox();
		leftPortCombo1 = new JAliasedComboBox();
		leftPortCombo2 = new JAliasedComboBox();
		rightPortCombo1 = new JAliasedComboBox();
		rightPortCombo2 = new JAliasedComboBox();
		leftCommentatorName = new JAliasedComboBox();
		rightCommentatorName = new JAliasedComboBox();
		leftStockList1 = new JList();
		leftStockList2 = new JList();
		rightStockList1 = new JList();
		rightStockList2 = new JList();
		bracketPosition = new JAliasedTextField("WR1");
		roundFormat = new JAliasedTextField("Best of 5");
		switchStockIcons1 = new JAliasedButton("switch");
		switchStockIcons2 = new JAliasedButton("switch");
		switchNames1 = new JAliasedButton("switch");
		switchNames2 = new JAliasedButton("switch");
		switchScore = new JAliasedButton("switch");
		switchCommentatorNames = new JAliasedButton("switch");
		reloadFilesButton = new JAliasedButton("reload");
		leftLabel1 = new JAliasedLabel("Left");
		leftLabel2 = new JAliasedLabel("Left 2");
		rightLabel1 = new JAliasedLabel("Right");
		rightLabel2 = new JAliasedLabel("Right 2");
		leftScoreLabel = new JAliasedLabel("Left Score");
		rightScoreLabel = new JAliasedLabel("Right Score");
		bracketPositionLabel = new JAliasedLabel("Bracket Position");
		roundFormatLabel = new JAliasedLabel("Round Format");
		leftCommentatorLabel = new JAliasedLabel("Left Commentator");
		rightCommentatorLabel = new JAliasedLabel("Right Commentator");
		leftStockIconPreview1 = new JLabel();
		leftStockIconPreview2 = new JLabel();
		rightStockIconPreview1 = new JLabel();
		rightStockIconPreview2 = new JLabel();

		JPanel paneEditing = new JPanel(null);
		JPanel paneSettings = new JPanel(null);
		paneEditing.setPreferredSize(paneEditing.getPreferredSize());
		paneSettings.setPreferredSize(paneSettings.getPreferredSize());
		paneEditing.validate();
		paneSettings.validate();
		JFrame frame = new JFrame("Updater");
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setSize(window_width, window_height);
		frame.setLocation(10, 40);
		frame.setVisible(true);
		// frame.setIconImage(new ImageIcon(imgURL).getImage());
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		/* Create a mouse adapter for the JLabels to allow for easy zero-ing of the scores */
		MouseAdapter zeroLabels = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				leftScoreValue = 0;
				rightScoreValue = 0;
				writeToFile("" + leftScoreValue, leftScoreFile);
				writeToFile("" + rightScoreValue, rightScoreFile);
				leftScore.setText("" + leftScoreValue);
				rightScore.setText("" + rightScoreValue);
			}
		};
		/* Set the location of the elements */
		leftLabel1.setBounds(5, 5, combo_box_width, element_height);
		leftLabel1.setFont(new Font("Arial", Font.BOLD, name_font_size));
		leftLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		rightLabel1.setBounds(
			leftLabel1.getX() + leftLabel1.getWidth() + small_gap_width + button_width + small_gap_width,
			leftLabel1.getY(),
			combo_box_width,
			element_height);
		rightLabel1.setFont(new Font("Arial", Font.BOLD, name_font_size));
		rightLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		leftStockIconPreview1.setBounds(leftLabel1.getX(),
			leftLabel1.getY() + leftLabel1.getHeight() + small_gap_width,
			icon_preview_width,
			element_height);
		leftStockIconPreview1.setIcon(new ImageIcon(leftStockIconFile.getPath()));
		leftStockIconCombo1.setBounds(
			leftStockIconPreview1.getX() + leftStockIconPreview1.getWidth() + small_gap_width,
			leftStockIconPreview1.getY(),
			combo_box_width - small_gap_width - leftStockIconPreview1.getWidth(),
			element_height);
		leftStockIconCombo1.setMaximumRowCount(stock_visible_rows);
		leftStockIconCombo1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					File charStockIconDir = new File(stockIconDir.getPath() + "/" + leftStockIconCombo1.getSelectedItem());
					File[] icons = charStockIconDir.listFiles();
					String[] iconNames = charStockIconDir.list();

					/* Perform a null-check in case the path does not denote a directory */
					if (icons != null) {
						/* Added the images within the character stock icon directory to the hashmap */
						for (int i = 0; i < icons.length; i++) {
							StockIconMap.put(icons[i].getName(), new ImageIcon(charStockIconDir.getPath() + "/" + icons[i].getName()));
						}
						/* Sets the data in the JList to have the names of the icons */
						leftStockList1.setListData(iconNames);
					} else {
						System.err.println("Warning: charStockIconDir is not a directory");
					}
				} catch (Exception error) {
					error.printStackTrace();
					System.err.println("Warning: could not load stock icon directories");
				}
			}
		});
		switchStockIcons1.setBounds(
			leftStockIconCombo1.getX() + leftStockIconCombo1.getWidth() + small_gap_width,
			leftStockIconCombo1.getY(),
			button_width,
			element_height);
		switchStockIcons1.setMargin(new Insets(0, 0, 0, 0));
		switchStockIcons1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String tempLeft = (String) leftStockIconCombo1.getSelectedItem();
				int tempLeftIndex = leftStockList1.getSelectedIndex();
				int tempRightIndex = rightStockList1.getSelectedIndex();
				leftStockIconCombo1.setSelectedItem("" + rightStockIconCombo1.getSelectedItem());
				rightStockIconCombo1.setSelectedItem("" + tempLeft);
				leftStockList1.setSelectedIndex(tempRightIndex);
				rightStockList1.setSelectedIndex(tempLeftIndex);
			}
		});
		rightStockIconCombo1.setBounds(
			switchStockIcons1.getX() + switchStockIcons1.getWidth() + small_gap_width,
			leftStockIconPreview1.getY(),
			combo_box_width - small_gap_width - icon_preview_width,
			element_height);
		rightStockIconCombo1.setMaximumRowCount(stock_visible_rows);
		rightStockIconCombo1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					File charStockIconDir = new File(stockIconDir.getPath() + "/" + rightStockIconCombo1.getSelectedItem());
					File[] icons = charStockIconDir.listFiles();
					String[] iconNames = charStockIconDir.list();

					/* Perform a null-check in case the path does not denote a directory */
					if (icons != null) {
						/* Added the images within the character stock icon directory to the hashmap */
						for (int i = 0; i < icons.length; i++) {
							StockIconMap.put(icons[i].getName(), new ImageIcon(charStockIconDir.getPath() + "/" + icons[i].getName()));
						}
						/* Sets the data in the JList to have the names of the icons */
						rightStockList1.setListData(iconNames);
					} else {
						System.err.println("Warning: charStockIconDir is not a directory");
					}
				} catch (Exception error) {
					error.printStackTrace();
					System.err.println("Warning: could not load stock icon directories");
				}
			}
		});
		rightStockIconPreview1.setBounds(
			rightStockIconCombo1.getX() + rightStockIconCombo1.getWidth() + small_gap_width,
			leftStockIconPreview1.getY(),
			icon_preview_width, element_height);
		rightStockIconPreview1.setIcon(new ImageIcon(rightStockIconFile.getPath()));
		leftStockList1.setCellRenderer(new StockIconListCellRenderer());
		leftStockList1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		leftStockList1.setLayoutOrientation(JList.VERTICAL_WRAP);
		leftStockList1.setVisibleRowCount(-1);
		leftStockList1.setBounds(small_gap_width,
			leftStockIconPreview1.getY() + leftStockIconPreview1.getHeight() + small_gap_width,
			combo_box_width, element_height);
		JScrollPane leftStockListScroller = new JScrollPane(leftStockList1);
		leftStockListScroller.setPreferredSize(new Dimension(leftStockList1.getWidth(), leftStockList1.getHeight()));
		leftStockList1.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				try {
					File charStockIconDir = new File(stockIconDir.getPath() + "/" + leftStockIconCombo1.getSelectedItem());
					File newIcon = new File(charStockIconDir.getPath() + "/" + leftStockList1.getSelectedValue());
					if (newIcon.exists()) {
						Files.copy(newIcon.toPath(), leftStockIconFile.toPath(), REPLACE_EXISTING);
						leftStockIconPreview1.setIcon(new ImageIcon(newIcon.getPath()));
						System.out.println("Info: new icon path = \"" + newIcon.getPath() + "\"");
					} else {
						System.err.println("Warning: new stock icon does not exist \""
							+ newIcon.getPath() + "\"");
					}
				} catch (Exception error) {
					error.printStackTrace();
					System.err.println("Warning: could not load stock icon");
				}
			}
		});
		leftPortCombo1.setBounds(
			leftStockList1.getX(),
			leftStockList1.getY() + leftStockList1.getHeight() + small_gap_width,
			combo_box_width,
			element_height);
		leftPortCombo1.setMaximumRowCount(stock_visible_rows);
		leftPortCombo1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					File newPort = new File(portsDir.getPath() + "/" + (String) leftPortCombo1.getSelectedItem());
					if (newPort.exists()) {
						Files.copy(newPort.toPath(), leftPortFile.toPath(), REPLACE_EXISTING);
						System.out.println("Info: new icon path = \"" + newPort.getPath() + "\"");
					} else {
						System.err.println("Warning: new port image does not exist \""
							+ newPort.getPath() + "\"");
					}
				} catch (Exception error) {
					error.printStackTrace();
					System.err.println("Warning: could not load port image");
				}
			}
		});
		rightStockList1.setCellRenderer(new StockIconListCellRenderer());
		rightStockList1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rightStockList1.setLayoutOrientation(JList.VERTICAL_WRAP);
		rightStockList1.setVisibleRowCount(-1);
		rightStockList1.setBounds(
			switchStockIcons1.getX() + switchStockIcons1.getWidth() + small_gap_width,
			rightStockIconPreview1.getY() + rightStockIconPreview1.getHeight() + small_gap_width,
			combo_box_width, element_height);
		JScrollPane rightStockListScroller = new JScrollPane(rightStockList1);
		rightStockListScroller.setPreferredSize(new Dimension(rightStockList1.getWidth(), rightStockList1.getHeight()));
		rightStockList1.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				try {
					File charStockIconDir = new File(stockIconDir.getPath() + "/" + rightStockIconCombo1.getSelectedItem());
					File newIcon = new File(charStockIconDir.getPath() + "/" + rightStockList1.getSelectedValue());
					if (newIcon.exists()) {
						Files.copy(newIcon.toPath(), rightStockIconFile.toPath(), REPLACE_EXISTING);
						rightStockIconPreview1.setIcon(new ImageIcon(newIcon.getPath()));
						System.out.println("Info: new icon path = \"" + newIcon.getPath() + "\"");
					} else {
						System.err.println("Warning: new stock icon does not exist \""
							+ newIcon.getPath() + "\"");
					}
				} catch (Exception error) {
					error.printStackTrace();
					System.err.println("Warning: could not load stock icon");
				}
			}
		});
		rightPortCombo1.setBounds(
			rightStockList1.getX(),
			rightStockList1.getY() + rightStockList1.getHeight() + small_gap_width,
			combo_box_width,
			element_height);
		rightPortCombo1.setMaximumRowCount(stock_visible_rows);
		rightPortCombo1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					File newPort = new File(portsDir.getPath() + "/" + (String) rightPortCombo1.getSelectedItem());
					if (newPort.exists()) {
						Files.copy(newPort.toPath(), right2PortFile.toPath(), REPLACE_EXISTING);
						System.out.println("Info: new icon path = \"" + newPort.getPath() + "\"");
					} else {
						System.err.println("Warning: new port image does not exist \""
							+ newPort.getPath() + "\"");
					}
				} catch (Exception error) {
					error.printStackTrace();
					System.err.println("Warning: could not load port image");
				}
			}
		});
		leftName1.setMaximumRowCount(names_visible_rows);
		leftName1.setBounds(
			leftPortCombo1.getX(),
			leftPortCombo1.getY() + leftPortCombo1.getHeight() + small_gap_width,
			combo_box_width,
			element_height);
		switchNames1.setBounds(leftName1.getX() + leftName1.getWidth() + small_gap_width, leftName1.getY(),
			button_width, element_height);
		switchNames1.setMargin(new Insets(0, 0, 0, 0));
		rightName1.setMaximumRowCount(names_visible_rows);
		rightName1.setBounds(switchNames1.getX() + switchNames1.getWidth() + small_gap_width, leftName1.getY(),
			combo_box_width, element_height);
		leftName1.setFont(new Font("Arial", Font.BOLD, name_font_size));
		rightName1.setFont(new Font("Arial", Font.BOLD, name_font_size));
		leftName1.setEditable(true);
		rightName1.setEditable(true);
		leftName1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				leftNameValue = (String) leftName1.getSelectedItem();
				writeToFile(leftNameValue, leftNameFile);
			}
		});
		rightName1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rightNameValue = (String) rightName1.getSelectedItem();
				writeToFile(rightNameValue, rightNameFile);
			}
		});
		switchNames1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String tempLeftName = leftNameValue;
				leftNameValue = rightNameValue;
				leftName1.setSelectedItem("" + rightNameValue);
				rightNameValue = tempLeftName;
				rightName1.setSelectedItem("" + tempLeftName);
			}
		});
		/* Second set of names */
		leftLabel2.setBounds(leftName1.getX(),
			leftName1.getY() + leftName1.getHeight() + small_gap_width,
			combo_box_width,
			element_height);
		leftLabel2.setFont(new Font("Arial", Font.BOLD, name_font_size));
		leftLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		show2Button.setBounds(leftName1.getX(),
			leftName1.getY() + leftName1.getHeight() + small_gap_width,
			element_height,
			element_height);
		show2Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				show2 = !show2;
				toggle2();
			}
		});
		rightLabel2.setBounds(
			leftLabel2.getX() + leftLabel2.getWidth() + small_gap_width + button_width + small_gap_width,
			leftLabel2.getY(),
			combo_box_width,
			element_height);
		rightLabel2.setFont(new Font("Arial", Font.BOLD, name_font_size));
		rightLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		leftStockIconPreview2.setBounds(leftName1.getX(),
			leftLabel2.getY() + leftLabel2.getHeight() + small_gap_width,
			icon_preview_width,
			element_height);
		leftStockIconPreview2.setIcon(new ImageIcon(left2StockIconFile.getPath()));
		leftStockIconCombo2.setBounds(
			leftStockIconPreview2.getX() + leftStockIconPreview2.getWidth() + small_gap_width,
			leftStockIconPreview2.getY(),
			combo_box_width - small_gap_width - leftStockIconPreview2.getWidth(),
			element_height);
		leftStockIconCombo2.setMaximumRowCount(stock_visible_rows);
		leftStockIconCombo2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					File charStockIconDir = new File(stockIconDir.getPath() + "/" + leftStockIconCombo2.getSelectedItem());
					File[] icons = charStockIconDir.listFiles();
					String[] iconNames = charStockIconDir.list();

					/* Perform a null-check in case the path does not denote a directory */
					if (icons != null) {
						/* Added the images within the character stock icon directory to the hashmap */
						for (int i = 0; i < icons.length; i++) {
							StockIconMap.put(icons[i].getName(), new ImageIcon(charStockIconDir.getPath() + "/" + icons[i].getName()));
						}
						/* Sets the data in the JList to have the names of the icons */
						leftStockList2.setListData(iconNames);
					} else {
						System.err.println("Warning: charStockIconDir is not a directory");
					}
				} catch (Exception error) {
					error.printStackTrace();
					System.err.println("Warning: could not load stock icon directories");
				}
			}
		});
		switchStockIcons2.setBounds(
			leftStockIconCombo2.getX() + leftStockIconCombo2.getWidth() + small_gap_width,
			leftStockIconCombo2.getY(),
			button_width,
			element_height);
		switchStockIcons2.setMargin(new Insets(0, 0, 0, 0));
		switchStockIcons2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String tempLeft = (String) leftStockIconCombo2.getSelectedItem();
				int tempLeftIndex = leftStockList2.getSelectedIndex();
				int tempRightIndex = rightStockList2.getSelectedIndex();
				leftStockIconCombo2.setSelectedItem("" + rightStockIconCombo2.getSelectedItem());
				rightStockIconCombo2.setSelectedItem("" + tempLeft);
				leftStockList2.setSelectedIndex(tempRightIndex);
				rightStockList2.setSelectedIndex(tempLeftIndex);
			}
		});
		rightStockIconCombo2.setBounds(
			switchStockIcons2.getX() + switchStockIcons2.getWidth() + small_gap_width,
			switchStockIcons2.getY(),
			combo_box_width - small_gap_width - icon_preview_width,
			element_height);
		rightStockIconCombo2.setMaximumRowCount(stock_visible_rows);
		rightStockIconCombo2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					File charStockIconDir = new File(stockIconDir.getPath() + "/" + rightStockIconCombo2.getSelectedItem());
					File[] icons = charStockIconDir.listFiles();
					String[] iconNames = charStockIconDir.list();

					/* Perform a null-check in case the path does not denote a directory */
					if (icons != null) {
						/* Added the images within the character stock icon directory to the hashmap */
						for (int i = 0; i < icons.length; i++) {
							StockIconMap.put(icons[i].getName(), new ImageIcon(charStockIconDir.getPath() + "/" + icons[i].getName()));
						}
						/* Sets the data in the JList to have the names of the icons */
						rightStockList2.setListData(iconNames);
					} else {
						System.err.println("Warning: charStockIconDir is not a directory");
					}
				} catch (Exception error) {
					error.printStackTrace();
					System.err.println("Warning: could not load stock icon directories");
				}
			}
		});
		rightStockIconPreview2.setBounds(
			rightStockIconCombo2.getX() + rightStockIconCombo2.getWidth() + small_gap_width,
			rightStockIconCombo2.getY(),
			icon_preview_width,
			element_height);
		rightStockIconPreview2.setIcon(new ImageIcon(right2StockIconFile.getPath()));
		leftStockList2.setCellRenderer(new StockIconListCellRenderer());
		leftStockList2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		leftStockList2.setLayoutOrientation(JList.VERTICAL_WRAP);
		leftStockList2.setVisibleRowCount(-1);
		leftStockList2.setBounds(small_gap_width,
			leftStockIconPreview2.getY() + leftStockIconPreview2.getHeight() + small_gap_width,
			combo_box_width, element_height);
		JScrollPane leftStockList2Scroller = new JScrollPane(leftStockList2);
		leftStockList2Scroller.setPreferredSize(new Dimension(leftStockList2.getWidth(), leftStockList2.getHeight()));
		leftStockList2.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				try {
					File charStockIconDir = new File(stockIconDir.getPath() + "/" + leftStockIconCombo2.getSelectedItem());
					File newIcon = new File(charStockIconDir.getPath() + "/" + leftStockList2.getSelectedValue());
					if (newIcon.exists()) {
						// TODO: change to leftStock2IconFile or something
						Files.copy(newIcon.toPath(), left2StockIconFile.toPath(), REPLACE_EXISTING);
						leftStockIconPreview2.setIcon(new ImageIcon(newIcon.getPath()));
						System.out.println("Info: new icon path = \"" + newIcon.getPath() + "\"");
					} else {
						System.err.println("Warning: new stock icon does not exist \""
							+ newIcon.getPath() + "\"");
					}
				} catch (Exception error) {
					error.printStackTrace();
					System.err.println("Warning: could not load stock icon");
				}
			}
		});
		leftPortCombo2.setBounds(
			leftStockList2.getX(),
			leftStockList2.getY() + leftStockList2.getHeight() + small_gap_width,
			combo_box_width,
			element_height);
		leftPortCombo2.setMaximumRowCount(stock_visible_rows);
		leftPortCombo2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					File newPort = new File(portsDir.getPath() + "/" + (String) leftPortCombo2.getSelectedItem());
					if (newPort.exists()) {
						Files.copy(newPort.toPath(), left2PortFile.toPath(), REPLACE_EXISTING);
						System.out.println("Info: new icon path = \"" + newPort.getPath() + "\"");
					} else {
						System.err.println("Warning: new port image does not exist \""
							+ newPort.getPath() + "\"");
					}
				} catch (Exception error) {
					error.printStackTrace();
					System.err.println("Warning: could not load port image");
				}
			}
		});
		rightStockList2.setCellRenderer(new StockIconListCellRenderer());
		rightStockList2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rightStockList2.setLayoutOrientation(JList.VERTICAL_WRAP);
		rightStockList2.setVisibleRowCount(-1);
		rightStockList2.setBounds(rightStockIconCombo2.getX(),
			rightStockIconCombo2.getY() + rightStockIconCombo2.getHeight() + small_gap_width,
			combo_box_width,
			element_height);
		JScrollPane rightStockList2Scroller = new JScrollPane(rightStockList2);
		rightStockList2Scroller.setPreferredSize(new Dimension(rightStockList2.getWidth(), rightStockList2.getHeight()));
		rightStockList2.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				try {
					File charStockIconDir = new File(stockIconDir.getPath() + "/" + rightStockIconCombo2.getSelectedItem());
					File newIcon = new File(charStockIconDir.getPath() + "/" + rightStockList2.getSelectedValue());
					if (newIcon.exists()) {
						// TODO: change to rightStock2IconFile or something
						Files.copy(newIcon.toPath(), right2StockIconFile.toPath(), REPLACE_EXISTING);
						rightStockIconPreview2.setIcon(new ImageIcon(newIcon.getPath()));
						System.out.println("Info: new icon path = \"" + newIcon.getPath() + "\"");
					} else {
						System.err.println("Warning: new stock icon does not exist \""
							+ newIcon.getPath() + "\"");
					}
				} catch (Exception error) {
					error.printStackTrace();
					System.err.println("Warning: could not load stock icon");
				}
			}
		});
		rightPortCombo2.setBounds(
			rightStockList2.getX(),
			rightStockList2.getY() + rightStockList2.getHeight() + small_gap_width,
			combo_box_width,
			element_height);
		rightPortCombo2.setMaximumRowCount(stock_visible_rows);
		rightPortCombo2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					File newPort = new File(portsDir.getPath() + "/" + (String) rightPortCombo2.getSelectedItem());
					if (newPort.exists()) {
						Files.copy(newPort.toPath(), rightPortFile.toPath(), REPLACE_EXISTING);
						System.out.println("Info: new icon path = \"" + newPort.getPath() + "\"");
					} else {
						System.err.println("Warning: new port image does not exist \""
							+ newPort.getPath() + "\"");
					}
				} catch (Exception error) {
					error.printStackTrace();
					System.err.println("Warning: could not load port image");
				}
			}
		});
		leftName2.setMaximumRowCount(names_visible_rows);
		leftName2.setBounds(
			small_gap_width,
			leftPortCombo2.getY() + leftPortCombo2.getHeight() + small_gap_width,
			combo_box_width,
			element_height);
		switchNames2.setBounds(
			leftName2.getX() + leftName2.getWidth() + small_gap_width,
			leftName2.getY(),
			button_width,
			element_height);
		switchNames2.setMargin(new Insets(0, 0, 0, 0));
		rightName2.setMaximumRowCount(names_visible_rows);
		rightName2.setBounds(
			switchNames2.getX() + switchNames2.getWidth() + small_gap_width,
			leftName2.getY(),
			combo_box_width,
			element_height);
		leftName2.setFont(new Font("Arial", Font.BOLD, name_font_size));
		rightName2.setFont(new Font("Arial", Font.BOLD, name_font_size));
		leftName2.setEditable(true);
		rightName2.setEditable(true);
		leftName2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				leftNameValue2 = (String) leftName2.getSelectedItem();
				writeToFile(leftNameValue2, left2NameFile);
			}
		});
		rightName2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rightNameValue2 = (String) rightName2.getSelectedItem();
				writeToFile(rightNameValue2, right2NameFile);
			}
		});
		switchNames2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String tempLeftName = leftNameValue2;
				leftNameValue2 = rightNameValue2;
				leftName2.setSelectedItem("" + rightNameValue2);
				rightNameValue2 = tempLeftName;
				rightName2.setSelectedItem("" + tempLeftName);
			}
		});
		/* Left Score Elements */
		leftScoreLabel.setFont(new Font("Arial", Font.BOLD, name_font_size));
		leftScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
		rightScoreLabel.setFont(new Font("Arial", Font.BOLD, name_font_size));
		rightScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
		leftScoreDec.setFont(new Font("Arial", Font.BOLD, score_font_size));
		leftScoreDec.setMargin(new Insets(0, 0, 0, 0));
		leftScore.setFont(new Font("Arial", Font.BOLD, score_font_size));
		leftScore.setHorizontalAlignment(SwingConstants.CENTER);
		leftScore.setEditable(false);
		leftScore.addMouseListener(zeroLabels);
		leftScoreInc.setFont(new Font("Arial", Font.BOLD, score_font_size));
		leftScoreInc.setMargin(new Insets(0, 0, 0, 0));
		/* Switch Score Element */
		switchScore.setMargin(new Insets(0, 0, 0, 0));
		/* Right Score Elements */
		rightScoreDec.setFont(new Font("Arial", Font.BOLD, score_font_size));
		rightScoreDec.setMargin(new Insets(0, 0, 0, 0));
		rightScore.setFont(new Font("Arial", Font.BOLD, score_font_size));
		rightScore.setHorizontalAlignment(SwingConstants.CENTER);
		rightScore.setEditable(false);
		rightScore.addMouseListener(zeroLabels);
		rightScoreInc.setFont(new Font("Arial", Font.BOLD, score_font_size));
		rightScoreInc.setMargin(new Insets(0, 0, 0, 0));
		/* Bracket Position Elements */
		bracketPositionLabel.setFont(new Font("Arial", Font.BOLD, name_font_size));
		bracketPositionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		roundFormatLabel.setFont(new Font("Arial", Font.BOLD, name_font_size));
		roundFormatLabel.setHorizontalAlignment(SwingConstants.CENTER);
		bracketPosition.setFont(new Font("Arial", Font.BOLD, text_field_font_size));
		bracketPosition.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				writeToFile("" + bracketPosition.getText(), bracketPositionFile);
			}
		});
		roundFormat.setFont(new Font("Arial", Font.BOLD, text_field_font_size));
		roundFormat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				writeToFile("" + roundFormat.getText(), roundFormatFile);
			}
		});
		leftCommentatorLabel.setFont(new Font("Arial", Font.BOLD, name_font_size));
		leftCommentatorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		rightCommentatorLabel.setFont(new Font("Arial", Font.BOLD, name_font_size));
		rightCommentatorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		leftCommentatorName.setMaximumRowCount(names_visible_rows);
		leftCommentatorName.setFont(new Font("Arial", Font.BOLD, name_font_size));
		leftCommentatorName.setEditable(true);
		switchCommentatorNames.setMargin(new Insets(0, 0, 0, 0));
		rightCommentatorName.setMaximumRowCount(names_visible_rows);
		rightCommentatorName.setFont(new Font("Arial", Font.BOLD, name_font_size));
		rightCommentatorName.setEditable(true);
		leftCommentatorName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				leftCommentatorNameValue = (String) leftCommentatorName.getSelectedItem();
				writeToFile(leftCommentatorNameValue, leftCommentatorNameFile);
			}
		});
		rightCommentatorName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rightCommentatorNameValue = (String) rightCommentatorName.getSelectedItem();
				writeToFile(rightCommentatorNameValue, rightCommentatorNameFile);
			}
		});
		switchCommentatorNames.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String tempLeftCommentatorName = leftCommentatorNameValue;
				leftCommentatorNameValue = rightCommentatorNameValue;
				leftCommentatorName.setSelectedItem("" + rightCommentatorNameValue);
				rightCommentatorNameValue = tempLeftCommentatorName;
				rightCommentatorName.setSelectedItem("" + tempLeftCommentatorName);
			}
		});
		/* Initialize the score elements and below to the right position */
		toggle2();
		/* Settings elements */
		namesText.setBounds(5, 5, dir_text_field_width, dir_text_element_height);
		namesText.setFont(new Font("Arial", Font.BOLD, dir_text_field_font_size));
		leftNameText.setBounds(
			namesText.getX(),
			namesText.getY() + namesText.getHeight() + small_gap_width,
			dir_text_field_width,
			dir_text_element_height);
		leftNameText.setFont(new Font("Arial", Font.BOLD, dir_text_field_font_size));
		left2NameText.setBounds(
			leftNameText.getX() + leftNameText.getWidth() + small_gap_width,
			leftNameText.getY(),
			dir_text_field_width,
			dir_text_element_height);
		left2NameText.setFont(new Font("Arial", Font.BOLD, dir_text_field_font_size));
		rightNameText.setBounds(
			leftNameText.getX(),
			leftNameText.getY() + leftNameText.getHeight() + small_gap_width,
			dir_text_field_width,
			dir_text_element_height);
		rightNameText.setFont(new Font("Arial", Font.BOLD, dir_text_field_font_size));
		right2NameText.setBounds(
			rightNameText.getX() + rightNameText.getWidth() + small_gap_width,
			rightNameText.getY(),
			dir_text_field_width,
			dir_text_element_height);
		right2NameText.setFont(new Font("Arial", Font.BOLD, dir_text_field_font_size));
		leftScoreText.setBounds(
			rightNameText.getX(),
			rightNameText.getY() + rightNameText.getHeight() + small_gap_width,
			dir_text_field_width,
			dir_text_element_height);
		leftScoreText.setFont(new Font("Arial", Font.BOLD, dir_text_field_font_size));
		rightScoreText.setBounds(
			leftScoreText.getX() + leftScoreText.getWidth() + small_gap_width,
			leftScoreText.getY(),
			dir_text_field_width,
			dir_text_element_height);
		rightScoreText.setFont(new Font("Arial", Font.BOLD, dir_text_field_font_size));
		bracketPositionText.setBounds(
			leftScoreText.getX(),
			leftScoreText.getY() + leftScoreText.getHeight() + small_gap_width,
			dir_text_field_width,
			dir_text_element_height);
		bracketPositionText.setFont(new Font("Arial", Font.BOLD, dir_text_field_font_size));
		roundFormatText.setBounds(
			bracketPositionText.getX() + bracketPositionText.getWidth() + small_gap_width,
			bracketPositionText.getY(),
			dir_text_field_width,
			dir_text_element_height);
		roundFormatText.setFont(new Font("Arial", Font.BOLD, dir_text_field_font_size));
		leftCommentatorNameText.setBounds(
			bracketPositionText.getX(),
			bracketPositionText.getY() + bracketPositionText.getHeight() + small_gap_width,
			dir_text_field_width,
			dir_text_element_height);
		leftCommentatorNameText.setFont(new Font("Arial", Font.BOLD, dir_text_field_font_size));
		rightCommentatorNameText.setBounds(
			leftCommentatorNameText.getX() + leftCommentatorNameText.getWidth() + small_gap_width,
			leftCommentatorNameText.getY(),
			dir_text_field_width,
			dir_text_element_height);
		rightCommentatorNameText.setFont(new Font("Arial", Font.BOLD, dir_text_field_font_size));
		leftStockIconText.setBounds(
			leftCommentatorNameText.getX(),
			leftCommentatorNameText.getY() + leftCommentatorNameText.getHeight() + small_gap_width,
			dir_text_field_width,
			dir_text_element_height);
		leftStockIconText.setFont(new Font("Arial", Font.BOLD, dir_text_field_font_size));
		left2StockIconText.setBounds(
			leftStockIconText.getX() + leftStockIconText.getWidth() + small_gap_width,
			leftStockIconText.getY(),
			dir_text_field_width,
			dir_text_element_height);
		left2StockIconText.setFont(new Font("Arial", Font.BOLD, dir_text_field_font_size));
		rightStockIconText.setBounds(
			leftStockIconText.getX(),
			leftStockIconText.getY() + leftStockIconText.getHeight() + small_gap_width,
			dir_text_field_width,
			dir_text_element_height);
		rightStockIconText.setFont(new Font("Arial", Font.BOLD, dir_text_field_font_size));
		right2StockIconText.setBounds(
			rightStockIconText.getX() + rightStockIconText.getWidth() + small_gap_width,
			rightStockIconText.getY(),
			dir_text_field_width,
			dir_text_element_height);
		right2StockIconText.setFont(new Font("Arial", Font.BOLD, dir_text_field_font_size));
		StockIconDirText.setBounds(
			leftStockIconText.getX(),
			leftStockIconText.getY() + leftStockIconText.getHeight() + small_gap_width,
			dir_text_field_width,
			dir_text_element_height);
		StockIconDirText.setFont(new Font("Arial", Font.BOLD, dir_text_field_font_size));
		PortsDirText.setBounds(
			StockIconDirText.getX() + StockIconDirText.getWidth() + small_gap_width,
			StockIconDirText.getY(),
			dir_text_field_width,
			dir_text_element_height);
		PortsDirText.setFont(new Font("Arial", Font.BOLD, dir_text_field_font_size));
		reloadFilesButton.setBounds(
			StockIconDirText.getX(),
			StockIconDirText.getY() + StockIconDirText.getHeight() + small_gap_width,
			button_width * 2,
			element_height);
		reloadFilesButton.setMargin(new Insets(0, 0, 0, 0));
		reloadFilesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				namesFile = new File(namesText.getText());
				leftNameFile = new File(leftNameText.getText());
				updateElements();
			}
		});
		/* Add button functionality */
		leftScoreDec.addActionListener(this);
		leftScoreInc.addActionListener(this);
		rightScoreDec.addActionListener(this);
		rightScoreInc.addActionListener(this);
		switchScore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int tempLeftScore = leftScoreValue;
				leftScoreValue = rightScoreValue;
				leftScore.setText("" + rightScoreValue);
				rightScoreValue = tempLeftScore;
				rightScore.setText("" + tempLeftScore);
				writeToFile("" + leftScoreValue, leftScoreFile);
				writeToFile("" + rightScoreValue, rightScoreFile);
			}
		});
		/* Read files to set text elements */
		updateElements();
		/* Add elements to the window */
		paneEditing.add(leftLabel1);
		paneEditing.add(rightLabel1);
		paneEditing.add(leftStockIconPreview1);
		paneEditing.add(leftStockIconCombo1);
		paneEditing.add(switchStockIcons1);
		paneEditing.add(rightStockIconCombo1);
		paneEditing.add(rightStockIconPreview1);
		paneEditing.add(leftStockList1);
		paneEditing.add(rightStockList1);
		paneEditing.add(leftPortCombo1);
		paneEditing.add(leftPortCombo2);
		paneEditing.add(rightPortCombo1);
		paneEditing.add(rightPortCombo2);
		paneEditing.add(leftName1);
		paneEditing.add(switchNames1);
		paneEditing.add(rightName1);

		paneEditing.add(show2Button);
		paneEditing.add(leftLabel2);
		paneEditing.add(rightLabel2);
		paneEditing.add(leftStockIconPreview2);
		paneEditing.add(rightStockIconPreview2);
		paneEditing.add(leftStockIconCombo2);
		paneEditing.add(switchStockIcons2);
		paneEditing.add(rightStockIconCombo2);
		paneEditing.add(leftStockList2);
		paneEditing.add(rightStockList2);
		paneEditing.add(leftName2);
		paneEditing.add(switchNames2);
		paneEditing.add(rightName2);

		paneEditing.add(leftScoreLabel);
		paneEditing.add(rightScoreLabel);
		paneEditing.add(leftScoreDec);
		paneEditing.add(leftScore);
		paneEditing.add(leftScoreInc);
		paneEditing.add(switchScore);
		paneEditing.add(rightScoreDec);
		paneEditing.add(rightScore);
		paneEditing.add(rightScoreInc);

		paneEditing.add(bracketPositionLabel);
		paneEditing.add(roundFormatLabel);
		paneEditing.add(bracketPosition);
		paneEditing.add(roundFormat);

		paneEditing.add(leftCommentatorLabel);
		paneEditing.add(rightCommentatorLabel);
		paneEditing.add(leftCommentatorName);
		paneEditing.add(switchCommentatorNames);
		paneEditing.add(rightCommentatorName);

		paneSettings.add(namesText);
		paneSettings.add(leftNameText);
		paneSettings.add(left2NameText);
		paneSettings.add(rightNameText);
		paneSettings.add(right2NameText);
		paneSettings.add(leftScoreText);
		paneSettings.add(rightScoreText);
		paneSettings.add(bracketPositionText);
		paneSettings.add(roundFormatText);
		paneSettings.add(leftCommentatorNameText);
		paneSettings.add(rightCommentatorNameText);
		paneSettings.add(leftStockIconText);
		paneSettings.add(rightStockIconText);
		paneSettings.add(StockIconDirText);
		paneSettings.add(PortsDirText);
		paneSettings.add(reloadFilesButton);
		tabbedPane.addTab("Editing", paneEditing);
		tabbedPane.addTab("Settings", paneSettings);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
	}

	public static void main(String arg[]) {
		new Updater();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (leftScoreDec == e.getSource()) {
			leftScoreDec.setEnabled(false);
			leftScoreValue--;
			writeToFile("" + leftScoreValue, leftScoreFile);
			leftScore.setText("" + leftScoreValue);
			leftScoreDec.setEnabled(true);
		} else if (leftScoreInc == e.getSource()) {
			leftScoreInc.setEnabled(false);
			leftScoreValue++;
			writeToFile("" + leftScoreValue, leftScoreFile);
			leftScore.setText("" + leftScoreValue);
			leftScoreInc.setEnabled(true);
		} else if (rightScoreDec == e.getSource()) {
			rightScoreDec.setEnabled(false);
			rightScoreValue--;
			writeToFile("" + rightScoreValue, rightScoreFile);
			rightScore.setText("" + rightScoreValue);
			rightScoreDec.setEnabled(true);
		} else if (rightScoreInc == e.getSource()) {
			rightScoreInc.setEnabled(false);
			rightScoreValue++;
			writeToFile("" + rightScoreValue, rightScoreFile);
			rightScore.setText("" + rightScoreValue);
			rightScoreInc.setEnabled(true);
		}
	}

}
