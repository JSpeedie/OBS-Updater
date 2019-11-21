import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;
import javax.swing.*;
import javax.swing.UIManager.*;
import javax.swing.event.*;
import javax.swing.text.*;

import static java.nio.file.StandardCopyOption.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Updater implements ActionListener {
	private final Map<String, ImageIcon> StockIconMap = new HashMap<>();
	private final Map<String, ImageIcon> PortIconMap = new HashMap<>();
	/* Preference Keys */
	private static final String LEFT1_CHAR="L1_CHAR";
	private static final String LEFT2_CHAR="L2_CHAR";
	private static final String RIGHT1_CHAR="R1_CHAR";
	private static final String RIGHT2_CHAR="R2_CHAR";
	private static final String LEFT1_CHAR_IMAGE="L1_CHAR_IMAGE";
	private static final String LEFT2_CHAR_IMAGE="L2_CHAR_IMAGE";
	private static final String RIGHT1_CHAR_IMAGE="R1_CHAR_IMAGE";
	private static final String RIGHT2_CHAR_IMAGE="R2_CHAR_IMAGE";
	private static final String LEFT1_PORT_FILE_PATH="L1_PORT_FILE_PATH";
	private static final String LEFT2_PORT_FILE_PATH="L2_PORT_FILE_PATH";
	private static final String RIGHT1_PORT_FILE_PATH="R1_PORT_FILE_PATH";
	private static final String RIGHT2_PORT_FILE_PATH="R2_PORT_FILE_PATH";
	private static final String FILE_NAMES="F_NAMES";
	private static final String FILE_STOCK_ICONS="F_STOCK_ICONS";
	private static final String FILE_PORTS="F_PORTS";
	/* Preference Defaults */
	private static String DEFAULT_LEFT1_CHAR="null";
	private static String DEFAULT_LEFT2_CHAR="null";
	private static String DEFAULT_LEFT1_CHAR_IMAGE="null";
	private static String DEFAULT_LEFT2_CHAR_IMAGE="null";
	private static String DEFAULT_RIGHT1_CHAR="null";
	private static String DEFAULT_RIGHT2_CHAR="null";
	private static String DEFAULT_RIGHT1_CHAR_IMAGE="null";
	private static String DEFAULT_RIGHT2_CHAR_IMAGE="null";
	private static String DEFAULT_LEFT1_PORT_FILE_PATH="null";
	private static String DEFAULT_LEFT2_PORT_FILE_PATH="null";
	private static String DEFAULT_RIGHT1_PORT_FILE_PATH="null";
	private static String DEFAULT_RIGHT2_PORT_FILE_PATH="null";
	private static String DEFAULT_FILE_NAMES="~/_obs/names.txt";
	private static String DEFAULT_FILE_STOCK_ICONS="~/_obs/stock_icons/";
	private static String DEFAULT_FILE_PORTS="~/_obs/ports/";
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
	private String leftCommentatorNameValue = "unnamed";
	private String rightCommentatorNameValue = "unnamed";
	private int leftScoreValue = 0;
	private int rightScoreValue = 0;
	private int scoreY;
	/* GUI variables */
	final int gap_width = 15;
	final int small_gap_width = 5;
	final int element_height = 30;
	final int dir_text_element_height = 30;
	final int dir_label_element_height = 20;
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
	private boolean show2 = false;
	private boolean showCommentators = false;

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

	/* Updating elements */
	private GUIPlayerSection left1PS;
	private GUIPlayerSection left2PS;
	private GUIPlayerSection right1PS;
	private GUIPlayerSection right2PS;
	private JAliasedTextField leftScore;
	private JAliasedTextField rightScore;
	private JAliasedButton show2Button;
	private JAliasedButton showCommentatorsButton;
	private JAliasedButton leftScoreInc;
	private JAliasedButton leftScoreDec;
	private JAliasedButton rightScoreInc;
	private JAliasedButton rightScoreDec;
	private JAliasedComboBox leftCommentatorName;
	private JAliasedComboBox rightCommentatorName;
	private JAliasedTextField bracketPosition;
	private JAliasedTextField roundFormat;
	private JAliasedButton switchStockIcons1;
	private JAliasedButton switchStockIcons2;
	private JAliasedButton switchNames1;
	private JAliasedButton switchNames2;
	private JAliasedButton switchPorts1;
	private JAliasedButton switchPorts2;
	private JAliasedButton switchScore;
	private JAliasedButton switchCommentatorNames;
	private JAliasedButton reloadFilesButton;
	private JAliasedLabel leftScoreLabel;
	private JAliasedLabel rightScoreLabel;
	private JAliasedLabel bracketPositionLabel;
	private JAliasedLabel roundFormatLabel;
	private JAliasedLabel leftCommentatorLabel;
	private JAliasedLabel rightCommentatorLabel;
	/* Settings elements */
	private JAliasedLabel namesLabel;
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
	private JAliasedTextField leftPortText;
	private JAliasedTextField left2PortText;
	private JAliasedTextField rightPortText;
	private JAliasedTextField right2PortText;
	private JAliasedLabel StockIconDirLabel;
	private JAliasedLabel PortsDirLabel;
	private JAliasedTextField StockIconDirText;
	private JAliasedTextField PortsDirText;
	private JAliasedButton StockIconDirBrowseButton;
	private JAliasedButton PortsDirBrowseButton;

	/* Custom Cell Renderers {{{ */
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

	public class PortIconListCellRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList list, Object obj, int index,
			boolean isSelected, boolean cellHasFocus) {

			JLabel item = (JLabel) super.getListCellRendererComponent(list, obj, index, isSelected,
				cellHasFocus);
			item.setIcon(PortIconMap.get((String) obj));
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

	public void writeToFile(String toWrite, File f) {
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
		Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
		/* Update stock icon path for each player section */
		left1PS.setNamesPath(namesText.getText());
		left2PS.setNamesPath(namesText.getText());
		right1PS.setNamesPath(namesText.getText());
		right2PS.setNamesPath(namesText.getText());
		left1PS.setStockDirPath(StockIconDirText.getText());
		left2PS.setStockDirPath(StockIconDirText.getText());
		right1PS.setStockDirPath(StockIconDirText.getText());
		right2PS.setStockDirPath(StockIconDirText.getText());
		left1PS.setPortsDirPath(PortsDirText.getText());
		left2PS.setPortsDirPath(PortsDirText.getText());
		right1PS.setPortsDirPath(PortsDirText.getText());
		right2PS.setPortsDirPath(PortsDirText.getText());

		readNames();
		loadStockIcons();
		loadPorts();

		left1PS.updateElements(namesList, iconsList, portsList);
		right1PS.updateElements(namesList, iconsList, portsList);
		left2PS.updateElements(namesList, iconsList, portsList);
		right2PS.updateElements(namesList, iconsList, portsList);

		/* Set characters to remembered characters */
		left1PS.setCharacter(prefs.get(LEFT1_CHAR, DEFAULT_LEFT1_CHAR));
		right1PS.setCharacter(prefs.get(RIGHT1_CHAR, DEFAULT_RIGHT1_CHAR));
		left2PS.setCharacter(prefs.get(LEFT2_CHAR, DEFAULT_LEFT2_CHAR));
		right2PS.setCharacter(prefs.get(RIGHT2_CHAR, DEFAULT_RIGHT2_CHAR));
		/* Set image used for character to remembered image */
		left1PS.setChosenImageFileName(prefs.get(LEFT1_CHAR_IMAGE, DEFAULT_LEFT1_CHAR_IMAGE));
		right1PS.setChosenImageFileName(prefs.get(RIGHT1_CHAR_IMAGE, DEFAULT_RIGHT1_CHAR_IMAGE));
		left2PS.setChosenImageFileName(prefs.get(LEFT2_CHAR_IMAGE, DEFAULT_LEFT2_CHAR_IMAGE));
		right2PS.setChosenImageFileName(prefs.get(RIGHT2_CHAR_IMAGE, DEFAULT_RIGHT2_CHAR_IMAGE));

		/* Set port image to remembered image */
		left1PS.setPortsFileName(prefs.get(LEFT1_PORT_FILE_PATH, DEFAULT_LEFT1_PORT_FILE_PATH));
		right1PS.setPortsFileName(prefs.get(RIGHT1_PORT_FILE_PATH, DEFAULT_RIGHT1_PORT_FILE_PATH));
		left2PS.setPortsFileName(prefs.get(LEFT2_PORT_FILE_PATH, DEFAULT_LEFT2_PORT_FILE_PATH));
		right2PS.setPortsFileName(prefs.get(RIGHT2_PORT_FILE_PATH, DEFAULT_RIGHT2_PORT_FILE_PATH));

		String leftComOldName = (String) leftCommentatorName.getSelectedItem();
		String rightComOldName = (String) rightCommentatorName.getSelectedItem();
		leftCommentatorName.removeAllItems();
		rightCommentatorName.removeAllItems();
		for (int i = 0; i < namesList.size(); i++) {
			leftCommentatorName.addItem(namesList.get(i));
			rightCommentatorName.addItem(namesList.get(i));
		}
		leftCommentatorName.setSelectedItem(leftComOldName);
		rightCommentatorName.setSelectedItem(rightComOldName);
		readFromFile(leftScoreFile, (JTextComponent) leftScore);
		// leftScoreValue = Integer.parseInt(leftScore.getText());
		readFromFile(rightScoreFile, (JTextComponent) rightScore);
		// rightScoreValue = Integer.parseInt(rightScore.getText());
		readFromFile(bracketPositionFile, (JTextComponent) bracketPosition);
		readFromFile(roundFormatFile, (JTextComponent) roundFormat);
		leftCommentatorName.setSelectedItem("" + stringFromFile(leftCommentatorNameFile));
		rightCommentatorName.setSelectedItem("" + stringFromFile(rightCommentatorNameFile));
	}

	private void setScorePositions() {
		leftScoreLabel.setBounds(
			left1PS.getX(),
			scoreY,
			combo_box_width,
			element_height);
		rightScoreLabel.setBounds(
			right1PS.getX(),
			scoreY,
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
		switchScore.setBounds(left1PS.getX() + left1PS.getWidth() + small_gap_width,
			leftScoreInc.getY(), button_width, (int) (element_height * 1.5));
		rightScoreDec.setBounds(
			right1PS.getX(),
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
			leftScoreDec.getX(),
			leftScoreDec.getY() + leftScoreDec.getHeight() + small_gap_width,
			combo_box_width,
			element_height);
		roundFormatLabel.setBounds(
			rightScoreDec.getX(),
			bracketPositionLabel.getY(),
			combo_box_width,
			element_height);
		bracketPosition.setBounds(
			bracketPositionLabel.getX(),
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
		showCommentatorsButton.setBounds(left1PS.getX(),
			bracketPosition.getY() + bracketPosition.getHeight() + small_gap_width,
			element_height,
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
		left2PS.setVisible(visibility);
		switchStockIcons2.setVisible(visibility);
		switchNames2.setVisible(visibility);
		switchPorts2.setVisible(visibility);
		right2PS.setVisible(visibility);
	}

	private void setCommentatorsElementsVisibility(boolean visibility) {
		leftCommentatorLabel.setVisible(visibility);
		rightCommentatorLabel.setVisible(visibility);
		leftCommentatorName.setVisible(visibility);
		switchCommentatorNames.setVisible(visibility);
		rightCommentatorName.setVisible(visibility);
	}

	private void setScoreY() {
		if (show2) {
			scoreY = left2PS.getY() + left2PS.getHeight() + small_gap_width;
		} else {
			scoreY = left1PS.getY() + left1PS.getHeight() + small_gap_width;
		}
	}

	private void toggle2() {
		setScoreY();
		setScorePositions();
		if (show2) {
			show2Button.setText("-");
		} else {
			show2Button.setText("+");
		}
		set2ElementsVisibility(show2);
	}

	private void toggleCommentators() {
		if (showCommentators) {
			showCommentatorsButton.setText("-");
		} else {
			showCommentatorsButton.setText("+");
		}
		setCommentatorsElementsVisibility(showCommentators);
	}

	public Updater() {
		Preferences prefs = Preferences.userRoot().node(this.getClass().getName());

		/* TODO: IN-PROGRESS: replace this with more robust system */
		final String InitialBaseDirText = System.getProperty("user.home") + "/_obs";
		final String InitialNamesText =
			prefs.get(FILE_NAMES, DEFAULT_FILE_NAMES);
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
		final String InitialStockIconText =
			prefs.get(FILE_STOCK_ICONS, DEFAULT_FILE_STOCK_ICONS);
		final String InitialPortsText =
			prefs.get(FILE_PORTS, DEFAULT_FILE_PORTS);
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
		namesLabel = new JAliasedLabel("List of Names");
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
		leftPortText = new JAliasedTextField(InitialLeftPortText);
		left2PortText = new JAliasedTextField(InitialLeft2PortText);
		rightPortText = new JAliasedTextField(InitialRightPortText);
		right2PortText = new JAliasedTextField(InitialRight2PortText);
		StockIconDirLabel = new JAliasedLabel("Stock Icon Path:");
		PortsDirLabel = new JAliasedLabel("Ports Path:");
		StockIconDirText = new JAliasedTextField(InitialStockIconText);
		PortsDirText = new JAliasedTextField(InitialPortsText);

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
		// TODO change init
		left1PS = new GUIPlayerSection(5, 5, "Left",
			InitialBaseDirText + "/names.txt",
			InitialBaseDirText + "/left_name.txt",
			InitialBaseDirText + "/left_stock_icon.png",
			InitialBaseDirText + "/stock_icons",
			InitialBaseDirText + "/ports",
			InitialBaseDirText + "/left_port.png");
		right1PS = new GUIPlayerSection(
			left1PS.getX() + left1PS.getWidth() + (2 * small_gap_width) + button_width, 5, "Right",
			InitialBaseDirText + "/names.txt",
			InitialBaseDirText + "/right_name.txt",
			InitialBaseDirText + "/right_stock_icon.png",
			InitialBaseDirText + "/stock_icons",
			InitialBaseDirText + "/ports",
			InitialBaseDirText + "/right_port.png");
		left2PS = new GUIPlayerSection(left1PS.getX(), left1PS.getY() + left1PS.getHeight() + small_gap_width, "Left 2",
			InitialBaseDirText + "/names.txt",
			InitialBaseDirText + "/left_name2.txt",
			InitialBaseDirText + "/left_stock_icon2.png",
			InitialBaseDirText + "/stock_icons",
			InitialBaseDirText + "/ports",
			InitialBaseDirText + "/left_port2.png");
		right2PS = new GUIPlayerSection(
			right1PS.getX(), right1PS.getY() + right1PS.getHeight() + small_gap_width,
			"Right 2",
			InitialBaseDirText + "/names.txt",
			InitialBaseDirText + "/right_name2.txt",
			InitialBaseDirText + "/right_stock_icon2.png",
			InitialBaseDirText + "/stock_icons",
			InitialBaseDirText + "/ports",
			InitialBaseDirText + "/right_port2.png");
		leftScore = new JAliasedTextField("");
		rightScore = new JAliasedTextField("");
		show2Button = new JAliasedButton("t");
		showCommentatorsButton = new JAliasedButton("t");
		leftScoreInc = new JAliasedButton("+");
		leftScoreDec = new JAliasedButton("-");
		rightScoreInc = new JAliasedButton("+");
		rightScoreDec = new JAliasedButton("-");
		leftCommentatorName = new JAliasedComboBox();
		rightCommentatorName = new JAliasedComboBox();
		bracketPosition = new JAliasedTextField("WR1");
		roundFormat = new JAliasedTextField("Best of 5");
		switchStockIcons1 = new JAliasedButton("switch");
		switchStockIcons2 = new JAliasedButton("switch");
		switchNames1 = new JAliasedButton("switch");
		switchNames2 = new JAliasedButton("switch");
		switchPorts1 = new JAliasedButton("switch");
		switchPorts2 = new JAliasedButton("switch");
		switchScore = new JAliasedButton("switch");
		switchCommentatorNames = new JAliasedButton("switch");
		reloadFilesButton = new JAliasedButton("reload");
		leftScoreLabel = new JAliasedLabel("Left Score");
		rightScoreLabel = new JAliasedLabel("Right Score");
		bracketPositionLabel = new JAliasedLabel("Bracket Position");
		roundFormatLabel = new JAliasedLabel("Round Format");
		leftCommentatorLabel = new JAliasedLabel("Left Commentator");
		rightCommentatorLabel = new JAliasedLabel("Right Commentator");

		StockIconDirBrowseButton = new JAliasedButton("Browse...");
		PortsDirBrowseButton = new JAliasedButton("Browse...");

		JPanel paneUpdating = new JPanel(null);
		JPanel paneSettings = new JPanel(null);
		paneUpdating.setPreferredSize(paneUpdating.getPreferredSize());
		paneSettings.setPreferredSize(paneSettings.getPreferredSize());
		paneUpdating.validate();
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
		// Second set of names
		show2Button.setBounds(left1PS.getX(),
			left1PS.getY() + left1PS.getHeight() + small_gap_width,
			element_height,
			element_height);
		show2Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				show2 = !show2;
				toggle2();
			}
		});
		showCommentatorsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showCommentators = !showCommentators;
				toggleCommentators();
			}
		});

		/* Set the location of the elements */
		switchStockIcons1.setBounds(
			left1PS.getX() + left1PS.getWidth() + small_gap_width,
			left1PS.getCharacterBoxY(),
			button_width,
			element_height);
		switchStockIcons1.setMargin(new Insets(0, 0, 0, 0));
		switchStockIcons1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (right1PS.getChosenImageFileName() != null) {
					prefs.put(LEFT1_CHAR_IMAGE, (String) right1PS.getChosenImageFileName());
				}
				if (left1PS.getChosenImageFileName() != null) {
					prefs.put(RIGHT1_CHAR_IMAGE, (String) left1PS.getChosenImageFileName());
				}

				String tempLeft = left1PS.getCharacter();
				int tempLeftIndex = left1PS.getChosenImageIndex();
				int tempRightIndex = right1PS.getChosenImageIndex();

				left1PS.setCharacter(right1PS.getCharacter());
				right1PS.setCharacter(tempLeft);
				left1PS.setChosenImageIndex(tempRightIndex);
				right1PS.setChosenImageIndex(tempLeftIndex);

				prefs.put(LEFT1_CHAR, left1PS.getCharacter());
				prefs.put(RIGHT1_CHAR, right1PS.getCharacter());
			}
		});
		switchStockIcons2.setBounds(
			left2PS.getX() + left2PS.getWidth() + small_gap_width,
			left2PS.getCharacterBoxY(),
			button_width,
			element_height);
		switchStockIcons2.setMargin(new Insets(0, 0, 0, 0));
		switchStockIcons2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (right2PS.getChosenImageFileName() != null) {
					prefs.put(LEFT2_CHAR_IMAGE, (String) right2PS.getChosenImageFileName());
				}
				if (left2PS.getChosenImageFileName() != null) {
					prefs.put(RIGHT2_CHAR_IMAGE, (String) left2PS.getChosenImageFileName());
				}

				String tempLeft = left2PS.getCharacter();
				int tempLeftIndex = left2PS.getChosenImageIndex();
				int tempRightIndex = right2PS.getChosenImageIndex();

				left2PS.setCharacter(right2PS.getCharacter());
				right2PS.setCharacter(tempLeft);
				left2PS.setChosenImageIndex(tempRightIndex);
				right2PS.setChosenImageIndex(tempLeftIndex);

				prefs.put(LEFT2_CHAR, left2PS.getCharacter());
				prefs.put(RIGHT2_CHAR, right2PS.getCharacter());
			}
		});

		switchNames1.setBounds(
			left1PS.getX() + left1PS.getWidth() + small_gap_width,
			left1PS.getNameBoxY(),
			button_width,
			element_height);
		switchNames1.setMargin(new Insets(0, 0, 0, 0));
		switchNames1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String tempLeftName = left1PS.getName();
				left1PS.setName(right1PS.getName());
				right1PS.setName(tempLeftName);
			}
		});
		switchNames2.setBounds(
			left2PS.getX() + left2PS.getWidth() + small_gap_width,
			left2PS.getNameBoxY(),
			button_width,
			element_height);
		switchNames2.setMargin(new Insets(0, 0, 0, 0));
		switchNames2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String tempLeftName = left2PS.getName();
				left2PS.setName(right2PS.getName());
				right2PS.setName(tempLeftName);
			}
		});

		switchPorts1.setBounds(
			left1PS.getX() + left1PS.getWidth() + small_gap_width,
			left1PS.getPortsListY(),
			button_width,
			element_height);
		switchPorts1.setMargin(new Insets(0, 0, 0, 0));
		switchPorts1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String tempLeft = (String) left1PS.getPortsFileName();
				left1PS.setPortsFileName(right1PS.getPortsFileName());
				right1PS.setPortsFileName(tempLeft);

				prefs.put(LEFT1_PORT_FILE_PATH, (String) left1PS.getPortsFileName());
				prefs.put(RIGHT1_PORT_FILE_PATH, (String) right1PS.getPortsFileName());
			}
		});
		switchPorts2.setBounds(
			left2PS.getX() + left2PS.getWidth() + small_gap_width,
			left2PS.getPortsListY(),
			button_width,
			element_height);
		switchPorts2.setMargin(new Insets(0, 0, 0, 0));
		switchPorts2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String tempLeft = (String) left2PS.getPortsFileName();
				left2PS.setPortsFileName(right2PS.getPortsFileName());
				right2PS.setPortsFileName(tempLeft);

				prefs.put(LEFT2_PORT_FILE_PATH, (String) left2PS.getPortsFileName());
				prefs.put(RIGHT2_PORT_FILE_PATH, (String) right2PS.getPortsFileName());
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
		show2 = false;
		showCommentators = false;
		toggle2();
		toggleCommentators();
		/* Settings elements */
		namesLabel.setBounds(5, 5, dir_text_field_width * 2 + small_gap_width, dir_label_element_height);
		namesLabel.setFont(new Font("Arial", Font.BOLD, dir_text_field_font_size));
		namesText.setBounds(
			namesLabel.getX(),
			namesLabel.getY() + namesLabel.getHeight() + small_gap_width,
			dir_text_field_width * 2 + small_gap_width,
			dir_text_element_height);
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
		StockIconDirLabel.setBounds(
			rightStockIconText.getX(),
			rightStockIconText.getY() + rightStockIconText.getHeight() + small_gap_width,
			dir_text_field_width * 2 + small_gap_width,
			dir_label_element_height);
		StockIconDirLabel.setFont(new Font("Arial", Font.BOLD, dir_text_field_font_size));
		StockIconDirText.setBounds(
			StockIconDirLabel.getX(),
			StockIconDirLabel.getY() + StockIconDirLabel.getHeight() + small_gap_width,
			(dir_text_field_width * 2) - button_width,
			dir_text_element_height);
		StockIconDirText.setFont(new Font("Arial", Font.BOLD, dir_text_field_font_size));
		StockIconDirBrowseButton.setBounds(
			StockIconDirText.getX() + dir_text_field_width * 2 + small_gap_width - button_width,
			StockIconDirText.getY(),
			button_width,
			dir_text_element_height);
		StockIconDirBrowseButton.setFont(new Font("Arial", Font.BOLD, dir_text_field_font_size));
		StockIconDirBrowseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setCurrentDirectory(new File(prefs.get(FILE_STOCK_ICONS, DEFAULT_FILE_STOCK_ICONS)));
				jfc.setDialogTitle("Select Stock Icon Directory...");
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (jfc.showOpenDialog(paneSettings) == JFileChooser.APPROVE_OPTION) {
					try {
						StockIconDirText.setText(jfc.getSelectedFile().getAbsolutePath());
						prefs.put(FILE_STOCK_ICONS, jfc.getSelectedFile().getAbsolutePath());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		PortsDirLabel.setBounds(
			StockIconDirText.getX(),
			StockIconDirText.getY() + StockIconDirText.getHeight() + small_gap_width,
			dir_text_field_width * 2 + small_gap_width,
			dir_label_element_height);
		PortsDirLabel.setFont(new Font("Arial", Font.BOLD, dir_text_field_font_size));
		PortsDirText.setBounds(
			PortsDirLabel.getX(),
			PortsDirLabel.getY() + PortsDirLabel.getHeight() + small_gap_width,
			(dir_text_field_width * 2) - button_width,
			dir_text_element_height);
		PortsDirText.setFont(new Font("Arial", Font.BOLD, dir_text_field_font_size));
		PortsDirBrowseButton.setBounds(
			PortsDirText.getX() + dir_text_field_width * 2 + small_gap_width - button_width,
			PortsDirText.getY(),
			button_width,
			dir_text_element_height);
		PortsDirBrowseButton.setFont(new Font("Arial", Font.BOLD, dir_text_field_font_size));
		PortsDirBrowseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setCurrentDirectory(new File(prefs.get(FILE_PORTS, DEFAULT_FILE_PORTS)));
				jfc.setDialogTitle("Select Ports Directory...");
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (jfc.showOpenDialog(paneSettings) == JFileChooser.APPROVE_OPTION) {
					try {
						PortsDirText.setText(jfc.getSelectedFile().getAbsolutePath());
						prefs.put(FILE_PORTS, jfc.getSelectedFile().getAbsolutePath());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});


		/* TODO: check these bound-settings for the non-settings elements */
		bracketPositionLabel.setBounds(
			leftScoreDec.getX(),
			leftScoreDec.getY() + leftScoreDec.getHeight() + small_gap_width,
			combo_box_width,
			element_height);
		bracketPosition.setBounds(
			bracketPositionLabel.getX(),
			bracketPositionLabel.getY() + bracketPositionLabel.getHeight() + small_gap_width,
			combo_box_width,
			element_height);
		bracketPosition.setFont(new Font("Arial", Font.BOLD, text_field_font_size));
		showCommentatorsButton.setBounds(left1PS.getX(),
			bracketPosition.getY() + bracketPosition.getHeight() + small_gap_width,
			element_height,
			element_height);
		reloadFilesButton.setBounds(
			PortsDirText.getX(),
			PortsDirText.getY() + PortsDirText.getHeight() + small_gap_width,
			button_width * 2,
			element_height);
		reloadFilesButton.setMargin(new Insets(0, 0, 0, 0));
		reloadFilesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				namesFile = new File(namesText.getText());
				stockIconDir = new File(StockIconDirText.getText());
				portsDir = new File(PortsDirText.getText());

				/* Update file paths of elements to use saved settings */
				leftNameFile = new File(leftNameText.getText());
				rightNameFile = new File(rightNameText.getText());
				left2NameFile = new File(left2NameText.getText());
				right2NameFile = new File(right2NameText.getText());
				leftScoreFile = new File(leftScoreText.getText());
				rightScoreFile = new File(rightScoreText.getText());
				bracketPositionFile = new File(bracketPositionText.getText());
				roundFormatFile = new File(roundFormatText.getText());
				leftCommentatorNameFile = new File(leftCommentatorNameText.getText());
				rightCommentatorNameFile = new File(rightCommentatorNameText.getText());
				leftStockIconFile = new File(leftStockIconText.getText());
				rightStockIconFile = new File(rightStockIconText.getText());
				left2StockIconFile = new File(left2StockIconText.getText());
				right2StockIconFile = new File(right2StockIconText.getText());

				/* Save file paths program remembers */
				prefs.put(FILE_NAMES, namesText.getText());
				prefs.put(FILE_STOCK_ICONS, StockIconDirText.getText());
				prefs.put(FILE_PORTS, PortsDirText.getText());
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
		/* These action listeners must be added after 'updateElements()'
		   so as not to be triggered by method calls in 'updateElements()' */
		left1PS.addCharActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (left1PS.getCharacter() != null) {
					prefs.put(LEFT1_CHAR, left1PS.getCharacter());
				}
			}
		});
		right1PS.addCharActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (right1PS.getCharacter() != null) {
					prefs.put(RIGHT1_CHAR, right1PS.getCharacter());
				}
			}
		});
		left2PS.addCharActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (left2PS.getCharacter() != null) {
					prefs.put(LEFT2_CHAR, left2PS.getCharacter());
				}
			}
		});
		right2PS.addCharActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (right2PS.getCharacter() != null) {
					prefs.put(RIGHT2_CHAR, right2PS.getCharacter());
				}
			}
		});

		left1PS.addPortsListListSelectionListener(new ListSelectionListener () {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (left1PS.getPortsFileName() != null) {
					prefs.put(LEFT1_PORT_FILE_PATH, (String) left1PS.getPortsFileName());
				}
			}
		});
		right1PS.addPortsListListSelectionListener(new ListSelectionListener () {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (right1PS.getPortsFileName() != null) {
					prefs.put(RIGHT1_PORT_FILE_PATH, (String) right1PS.getPortsFileName());
				}
			}
		});
		left2PS.addPortsListListSelectionListener(new ListSelectionListener () {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (left2PS.getPortsFileName() != null) {
					prefs.put(LEFT2_PORT_FILE_PATH, (String) left2PS.getPortsFileName());
				}
			}
		});
		right2PS.addPortsListListSelectionListener(new ListSelectionListener () {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (right2PS.getPortsFileName() != null) {
					prefs.put(RIGHT2_PORT_FILE_PATH, (String) right2PS.getPortsFileName());
				}
			}
		});
		/* Add elements to the window */
		left1PS.addToPanel(paneUpdating);
		right1PS.addToPanel(paneUpdating);
		left2PS.addToPanel(paneUpdating);
		right2PS.addToPanel(paneUpdating);
		paneUpdating.add(switchStockIcons1);
		paneUpdating.add(switchStockIcons2);
		paneUpdating.add(switchNames1);
		paneUpdating.add(switchNames2);
		paneUpdating.add(switchPorts1);
		paneUpdating.add(switchPorts2);

		paneUpdating.add(show2Button);
		paneUpdating.add(showCommentatorsButton);

		paneUpdating.add(leftScoreLabel);
		paneUpdating.add(rightScoreLabel);
		paneUpdating.add(leftScoreDec);
		paneUpdating.add(leftScore);
		paneUpdating.add(leftScoreInc);
		paneUpdating.add(switchScore);
		paneUpdating.add(rightScoreDec);
		paneUpdating.add(rightScore);
		paneUpdating.add(rightScoreInc);

		paneUpdating.add(bracketPositionLabel);
		paneUpdating.add(roundFormatLabel);
		paneUpdating.add(bracketPosition);
		paneUpdating.add(roundFormat);

		paneUpdating.add(leftCommentatorLabel);
		paneUpdating.add(rightCommentatorLabel);
		paneUpdating.add(leftCommentatorName);
		paneUpdating.add(switchCommentatorNames);
		paneUpdating.add(rightCommentatorName);

		paneSettings.add(namesLabel);
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
		paneSettings.add(left2StockIconText);
		paneSettings.add(rightStockIconText);
		paneSettings.add(right2StockIconText);
		paneSettings.add(StockIconDirLabel);
		paneSettings.add(StockIconDirText);
		paneSettings.add(StockIconDirBrowseButton);
		paneSettings.add(PortsDirLabel);
		paneSettings.add(PortsDirText);
		paneSettings.add(PortsDirBrowseButton);
		paneSettings.add(reloadFilesButton);
		tabbedPane.addTab("Updating", paneUpdating);
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
