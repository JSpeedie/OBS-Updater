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

public class GUIPlayerSection {
	private final Map<String, ImageIcon> StockIconMap = new HashMap<>();
	private final Map<String, ImageIcon> PortIconMap = new HashMap<>();
	private String nameValue = "unnamed";
	/* Files for program */
	private File namesFile;
	private File nameFile;
	private File stockIconFile;
	private File stockIconDir;
	private File portsDir;
	private File portFile;
	/* GUI variables */
	final int gap_width = 15;
	final int small_gap_width = 5;
	final int element_height = 30;
	final int dir_text_element_height = 24;
	final int button_width = 80;
	final int combo_box_width = 190;
	final int icon_preview_width = 24 * 2;
	final int dir_text_field_width = 225;
	final int window_width = (2 * combo_box_width) + (button_width) + (10 * small_gap_width);
	final int window_height = (16 * element_height) + (6 * small_gap_width) + (10 * gap_width);
	final int name_font_size = 16;
	final int score_font_size = 36;
	final int text_field_font_size = 14;
	final int dir_text_field_font_size = 13;
	final int stock_visible_rows = 26;
	final int names_visible_rows = 30;

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
	private JAliasedLabel nameLabel;
	private JLabel stockIconPreview;
	private JAliasedComboBox stockIconComboBox;
	private JList stockList;
	private JList portsList;
	private JScrollPane stockListScroller;
	private JScrollPane portsListScroller;
	private JAliasedComboBox nameComboBox;

	/* Custom Cell Renderers {{{ */
	public class StockIconListCellRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList list, Object obj, int index,
			boolean isSelected, boolean cellHasFocus) {

			JLabel item = (JLabel) super.getListCellRendererComponent(list, obj, index, isSelected,
				cellHasFocus);
			Image image = (StockIconMap.get((String) obj)).getImage();
			double WToHRatio = ((double) image.getWidth(null)) / ((double) image.getHeight(null));
			Image ScaledIC = image.getScaledInstance(
				(int) (element_height * WToHRatio),
				element_height,
				java.awt.Image.SCALE_FAST);
			item.setIcon(new ImageIcon(ScaledIC));
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
			Image image = (PortIconMap.get((String) obj)).getImage();
			double WToHRatio = ((double) image.getWidth(null)) / ((double) image.getHeight(null));
			Image ScaledIC = image.getScaledInstance(
				(int) (element_height * WToHRatio),
				element_height,
				java.awt.Image.SCALE_FAST);
			item.setIcon(new ImageIcon(ScaledIC));
			item.setText("");
			return item;
		}
	}
	/* }}} */

	public void writeToFile(String toWrite, File f) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			writer.write(toWrite);
			writer.close();
		} catch (Exception e) {
			System.err.println("Error: Failed to write to File");
		}
	}

	private String readFromFile(File f) {
		String read;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(f));
			if ((read = reader.readLine()) != null) {
				return read;
			} else {
				System.err.println("Error: could not read from File");
				return "";
			}
		} catch (Exception e) {
			System.err.println("Error: could not read from File");
			return "";
		}
	}

	public GUIPlayerSection(int x, int y, String labelText, String namesFilePath,
		String nameFilePath, String stockIconFilePath, String stockIconsFilePath,
		String portsFilePath, String portFilePath) {

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

		namesFile = new File(namesFilePath);
		nameFile = new File(nameFilePath);
		stockIconFile = new File(stockIconFilePath);
		stockIconDir = new File(stockIconsFilePath);
		portsDir = new File(portsFilePath);
		portFile = new File(portFilePath);

		nameLabel = new JAliasedLabel(labelText);
		stockIconPreview = new JLabel();
		stockIconComboBox = new JAliasedComboBox();
		nameComboBox = new JAliasedComboBox();
		portsList = new JList();
		portsListScroller = new JScrollPane(portsList);
		stockList = new JList();

		nameLabel.setFont(new Font("Arial", Font.BOLD, name_font_size));
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

		setLocation(x, y);

		ImageIcon IC = new ImageIcon(stockIconFile.getPath());
		Image image = IC.getImage();
		double WToHRatio = ((double) image.getWidth(null)) / ((double) image.getHeight(null));
		Image ScaledIC = image.getScaledInstance(
			(int) (stockIconPreview.getHeight() * WToHRatio),
			stockIconPreview.getHeight(),
			java.awt.Image.SCALE_FAST);
		stockIconPreview.setIcon(new ImageIcon(ScaledIC));



		stockIconComboBox.setMaximumRowCount(stock_visible_rows);
		stockIconComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					File charStockIconDir =
						new File(stockIconDir.getPath() + "/" + stockIconComboBox.getSelectedItem() + "/");
					File[] icons = charStockIconDir.listFiles();
					String[] iconNames = charStockIconDir.list();

					/* Perform a null-check in case the path does not denote a directory */
					if (icons != null) {
						/* Added the images within the character stock icon directory to the hashmap */
						for (int i = 0; i < icons.length; i++) {
							StockIconMap.put(icons[i].getName(),
								new ImageIcon(charStockIconDir.getPath() + "/" + icons[i].getName()));
						}
						/* Sets the data in the JList to have the names of the icons */
						stockList.setListData(iconNames);
					} else {
						System.err.println("Warning: charStockIconDir is not a directory: " +
							charStockIconDir.getPath());
					}
				} catch (Exception error) {
					error.printStackTrace();
					System.err.println("Warning: could not load stock icon directories");
				}
			}
		});
		stockList.setCellRenderer(new StockIconListCellRenderer());
		stockList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		stockList.setLayoutOrientation(JList.VERTICAL_WRAP);
		stockList.setVisibleRowCount(-1);
		stockListScroller.setPreferredSize(new Dimension(stockList.getWidth(), stockList.getHeight()));
		stockList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				try {
					File charStockIconDir = new File(stockIconDir.getPath() + "/" + stockIconComboBox.getSelectedItem());
					File newIcon = new File(charStockIconDir.getPath() + "/" + stockList.getSelectedValue());
					if (newIcon.exists()) {
						Files.copy(newIcon.toPath(), stockIconFile.toPath(), REPLACE_EXISTING);
						ImageIcon IC = new ImageIcon(newIcon.getPath());
						Image image = IC.getImage();
						Image ScaledIC = image.getScaledInstance(stockIconPreview.getWidth(),
							stockIconPreview.getHeight(), java.awt.Image.SCALE_FAST); // scale it the smooth way
						stockIconPreview.setIcon(new ImageIcon(ScaledIC));
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
		portsList.setCellRenderer(new PortIconListCellRenderer());
		portsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		portsList.setLayoutOrientation(JList.VERTICAL_WRAP);
		portsList.setVisibleRowCount(-1);
		portsList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				try {
					File newIcon = new File(portsDir.getPath() + "/" + portsList.getSelectedValue());
					if (newIcon.exists()) {
						Files.copy(newIcon.toPath(), portFile.toPath(), REPLACE_EXISTING);
						System.out.println("Info: new port icon path = \"" + newIcon.getPath() + "\"");
					} else {
						System.err.println("Warning: new port icon does not exist \""
							+ newIcon.getPath() + "\"");
					}
				} catch (Exception error) {
					error.printStackTrace();
					System.err.println("Warning: could not load port icon");
				}
			}
		});
		nameComboBox.setEditable(true);
		// The combobox must be set to editable before this method call will work
		nameComboBox.setSelectedItem(readFromFile(nameFile));
		nameComboBox.setMaximumRowCount(names_visible_rows);
		nameComboBox.setFont(new Font("Arial", Font.BOLD, name_font_size));
		nameComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nameValue = (String) nameComboBox.getSelectedItem();
				writeToFile(nameValue, nameFile);
			}
		});
	}

	public int getX() {
		int min = nameLabel.getX();
		if (stockIconPreview.getX() < min) min = stockIconPreview.getX();
		if (stockIconComboBox.getX() < min) min = stockIconComboBox.getX();
		if (stockListScroller.getX() < min) min = stockListScroller.getX();
		if (portsListScroller.getX() < min) min = portsListScroller.getX();
		if (nameComboBox.getX() < min) min = nameComboBox.getX();

		return min;
	}

	public void setLocation(int x, int y) {
		nameLabel.setBounds(x, y, combo_box_width, element_height);
		stockIconPreview.setBounds(nameLabel.getX(),
			nameLabel.getY() + nameLabel.getHeight() + small_gap_width,
			icon_preview_width,
			element_height);
		stockIconComboBox.setBounds(
			stockIconPreview.getX() + stockIconPreview.getWidth() + small_gap_width,
			stockIconPreview.getY(),
			combo_box_width - small_gap_width - stockIconPreview.getWidth(),
			element_height);
		stockList.setBounds(
			stockIconPreview.getX(),
			stockIconPreview.getY() + stockIconPreview.getHeight() + small_gap_width,
			combo_box_width,
			element_height);
		stockListScroller = new JScrollPane(stockList);
		stockListScroller.setBounds(
			stockIconPreview.getX(),
			stockIconPreview.getY() + stockIconPreview.getHeight() + small_gap_width,
			combo_box_width,
			(int) (element_height * 1.5));
		portsList.setBounds(
			stockListScroller.getX(),
			stockListScroller.getY() + stockListScroller.getHeight() + small_gap_width,
			combo_box_width,
			element_height);
		portsListScroller.setBounds(
			stockListScroller.getX(),
			stockListScroller.getY() + stockListScroller.getHeight() + small_gap_width,
			combo_box_width,
			(int) (element_height * 1.5));
		nameComboBox.setBounds(
			portsListScroller.getX(),
			portsListScroller.getY() + portsListScroller.getHeight() + small_gap_width,
			combo_box_width,
			element_height);
	}

	public int getY() {
		int min = nameLabel.getY();
		if (stockIconPreview.getY() < min) min = stockIconPreview.getY();
		if (stockIconComboBox.getY() < min) min = stockIconComboBox.getY();
		if (stockListScroller.getY() < min) min = stockListScroller.getY();
		if (portsListScroller.getY() < min) min = portsListScroller.getY();
		if (nameComboBox.getY() < min) min = nameComboBox.getY();

		return min;
	}

	public int getWidth() {
		int lx = this.getX();
		int rx = lx;
		if (stockIconPreview.getX() + stockIconPreview.getWidth() > rx) {
			rx = stockIconPreview.getX() + stockIconPreview.getWidth();
		}
		if (stockIconComboBox.getX() + stockIconComboBox.getWidth() > rx) {
			rx = stockIconComboBox.getX() + stockIconComboBox.getWidth();
		}
		if (stockListScroller.getX() + stockListScroller.getWidth() > rx) {
			rx = stockListScroller.getX() + stockListScroller.getWidth();
		}
		if (portsListScroller.getX() + portsListScroller.getWidth() > rx) {
			rx = portsListScroller.getX() + portsListScroller.getWidth();
		}
		if (nameComboBox.getX() + nameComboBox.getWidth() > rx) {
			rx = nameComboBox.getX() + nameComboBox.getWidth();
		}

		return rx - lx;
	}

	public int getHeight() {
		int ty = this.getY();
		int by = ty;
		if (stockIconPreview.getY() + stockIconPreview.getHeight() > by) {
			by = stockIconPreview.getY() + stockIconPreview.getHeight();
		}
		if (stockIconComboBox.getY() + stockIconComboBox.getHeight() > by) {
			by = stockIconComboBox.getY() + stockIconComboBox.getHeight();
		}
		if (stockListScroller.getY() + stockListScroller.getHeight() > by) {
			by = stockListScroller.getY() + stockListScroller.getHeight();
		}
		if (portsListScroller.getY() + portsListScroller.getHeight() > by) {
			by = portsListScroller.getY() + portsListScroller.getHeight();
		}
		if (nameComboBox.getY() + nameComboBox.getHeight() > by) {
			by = nameComboBox.getY() + nameComboBox.getHeight();
		}

		return by - ty;
	}

	public int getCharacterBoxY() {
		return stockIconComboBox.getY();
	}

	public String getCharacter() {
		return (String) stockIconComboBox.getSelectedItem();
	}

	public void setCharacter(String s) {
		stockIconComboBox.setSelectedItem(s);
	}

	public Object getChosenImageFileName() {
		return stockList.getSelectedValue();
	}

	public void setChosenImageFileName(Object o) {
		stockList.setSelectedValue(o, true);
	}

	public int getChosenImageIndex() {
		return stockList.getSelectedIndex();
	}

	public void setChosenImageIndex(int i) {
		stockList.setSelectedIndex(i);
	}

	public int getPortsListY() {
		return portsListScroller.getY();
	}

	public Object getPortsFileName() {
		return portsList.getSelectedValue();
	}

	public void setPortsFileName(Object o) {
		portsList.setSelectedValue(o, true);
	}

	public int getNameBoxY() {
		return nameComboBox.getY();
	}

	public String getName() {
		return (String) nameComboBox.getSelectedItem();
	}

	public void setName(String s) {
		nameComboBox.setSelectedItem(s);
	}

	public int setVisible(boolean vis) {
		nameLabel.setVisible(vis);
		stockIconPreview.setVisible(vis);
		stockIconComboBox.setVisible(vis);
		stockListScroller.setVisible(vis);
		stockList.setVisible(vis);
		portsListScroller.setVisible(vis);
		portsList.setVisible(vis);
		nameComboBox.setVisible(vis);
		return 0;
	}

	public int addToPanel(JPanel pane) {
		pane.add(nameLabel);
		pane.add(stockIconPreview);
		pane.add(stockIconComboBox);
		pane.add(stockListScroller);
		pane.add(portsListScroller);
		pane.add(nameComboBox);

		return 0;
	}

	public void addCharActionListener(ActionListener a) {
		stockIconComboBox.addActionListener(a);
	}

	public void setNamesPath(String s) {
		namesFile = new File(s);
	}

	public void setStockDirPath(String s) {
		stockIconDir = new File(s);
	}

	public void setPortsDirPath(String s) {
		portsDir = new File(s);
	}

	public void updateElements(ArrayList<String> namesList, ArrayList<String> iconsList,
		ArrayList<String> portsArrayList) {

		String oldName = (String) nameComboBox.getSelectedItem();
		String oldChar = (String) stockIconComboBox.getSelectedItem();
		nameComboBox.removeAllItems();
		Collections.sort(namesList);
		stockIconComboBox.removeAllItems();
		for (int i = 0; i < namesList.size(); i++) {
			nameComboBox.addItem(namesList.get(i));
		}
		nameComboBox.setSelectedItem(oldName);
		for (int i = 0; i < iconsList.size(); i++) {
			stockIconComboBox.addItem(iconsList.get(i));
		}
		// Empty the port list
		//((DefaultListModel) (portsList.getModel())).clear();
		//for (int i = 0; i < portsArrayList.size(); i++) {
		//	((DefaultListModel) (portsList.getModel())).addElement(portsArrayList.get(i));
		//}
		nameComboBox.setSelectedItem("" + readFromFile(nameFile));
		try {
			File portIconDir = new File(portsDir.getPath());
			File[] icons = portIconDir.listFiles();
			String[] iconNames = portIconDir.list();

			/* Perform a null-check in case the path does not denote a directory */
			if (icons != null) {
				/* Added the images within the character stock icon directory to the hashmap */
				for (int i = 0; i < icons.length; i++) {
					PortIconMap.put(icons[i].getName(), new ImageIcon(portIconDir.getPath()
						+ "/" + icons[i].getName()));
				}
				Arrays.sort(iconNames);
				/* Sets the data in the JList to have the names of the icons */
				System.out.println("Setting portsList!");
				portsList.setListData(iconNames);
			} else {
				System.err.println("Warning: portIconDir is not a directory");
			}
		} catch (Exception error) {
			error.printStackTrace();
			System.err.println("Warning: could not load port icons");
		}
	}
}
