/*
 * Nicholas McDonald
 * nmmcdona@ucsd.edu
 *
 *
 * NuText.java
 */

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.rtf.RTFEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class NuTextEditor extends JFrame implements ActionListener {
    private JTextPane textArea = new JTextPane();

    private MenuBar menuBar = new MenuBar();
    private JMenuBar jMenuBar = new JMenuBar();

    private JScrollPane jScrollPane = new JScrollPane(textArea,ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    private JComboBox fontsMenu;
    private JComboBox colorMenu;

    private String [] colors = {"black", "blue", "cyan", "darkGray", "gray", "green", "lightGray", "magenta", "orange", "pink", "red", "white", "yellow"};

    private Menu file = new Menu();
    private MenuItem openFile = new MenuItem();
    private MenuItem saveFile = new MenuItem();
    private MenuItem close = new MenuItem();

    private Menu selection = new Menu();
    private MenuItem bold = new MenuItem();
    private MenuItem italics = new MenuItem();
    private MenuItem underline = new MenuItem();

    private JButton boldButton = new JButton("B");
    private JButton italicsButton = new JButton("I");
    private JButton underlineButton = new JButton("U");

    //defaults
    private String defaultFont = "Times New Roman";
    private String defaultColor = "black";

    public NuTextEditor() {
        this.setSize(700, 500);
        this.setTitle("Untitled Document");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.textArea.setFont(new Font(this.defaultFont, Font.PLAIN, 14));
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(textArea);
        this.setMenuBar(this.menuBar);
        this.menuBar.add(this.file);
        this.menuBar.add(this.selection);

        this.file.setLabel("File");

        this.openFile.setLabel("Open");
        this.openFile.addActionListener(this);
        this.openFile.setShortcut(new MenuShortcut(KeyEvent.VK_O, false));
        this.file.add(this.openFile);

        this.saveFile.setLabel("Save");
        this.saveFile.addActionListener(this);
        this.saveFile.setShortcut(new MenuShortcut(KeyEvent.VK_S, false));
        this.file.add(this.saveFile);

        this.close.setLabel("Close");
        this.close.setShortcut(new MenuShortcut(KeyEvent.VK_F4, false));
        this.close.addActionListener(this);
        this.file.add(this.close);

        this.selection.setLabel("Selection");

        this.bold.setLabel("Bold");
        this.bold.addActionListener(this);
        this.bold.setShortcut(new MenuShortcut(KeyEvent.VK_B, false));
        this.selection.add(this.bold);

        this.italics.setLabel("Italicize");
        this.italics.addActionListener(this);
        this.italics.setShortcut(new MenuShortcut(KeyEvent.VK_I, false));
        this.selection.add(this.italics);

        this.underline.setLabel("Underline");
        this.underline.addActionListener(this);
        this.underline.setShortcut(new MenuShortcut(KeyEvent.VK_U, false));
        this.selection.add(this.underline);

        this.jScrollPane.setViewportView(this.textArea);
        this.getContentPane().add(this.jScrollPane);
        this.setJMenuBar(jMenuBar);

        this.fontsMenu = new JComboBox<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        this.jMenuBar.add(this.fontsMenu);
        this.fontsMenu.setMaximumSize(this.fontsMenu.getPreferredSize());
        this.fontsMenu.setSelectedItem(this.defaultFont);
        this.fontsMenu.addActionListener(this);

        this.colorMenu = new JComboBox<>(this.colors);
        this.jMenuBar.add(this.colorMenu);
        this.colorMenu.setMaximumSize(this.colorMenu.getPreferredSize());
        this.colorMenu.setSelectedItem(this.defaultColor);
        this.colorMenu.addActionListener(this);

        this.jMenuBar.add(this.boldButton);
        this.boldButton.setMaximumSize(this.boldButton.getPreferredSize());
        this.boldButton.addActionListener(this);

        this.jMenuBar.add(this.italicsButton);
        this.italicsButton.setMaximumSize(this.boldButton.getPreferredSize());
        this.italicsButton.addActionListener(this);

        this.jMenuBar.add(this.underlineButton);
        this.underlineButton.setMaximumSize(this.underlineButton.getPreferredSize());
        this.underlineButton.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.close)
            closeDocument();

        else if(e.getSource() == this.openFile)
            openFile();

        else if(e.getSource() == this.saveFile)
            saveFile();

        else if(e.getSource() == this.bold || e.getSource() == this.boldButton)
            setStyle("bold");

        else if(e.getSource() == this.italics || e.getSource() == this.italicsButton)
            setStyle("italics");

        else if(e.getSource() == this.underline || e.getSource() == this.underlineButton)
            setStyle("underline");

        else if(e.getSource() == this.fontsMenu)
            setStyle("font");

        else if(e.getSource() == this.colorMenu)
            setStyle("color");
    }

    private void closeDocument() { this.dispose(); }

    private void openFile() {
        JFileChooser open = new JFileChooser();
        int option = open.showOpenDialog(this);

        if(option == JFileChooser.APPROVE_OPTION) {
            this.textArea.setText("");
            this.setTitle(open.getName(open.getSelectedFile()) + " - [" + open.getSelectedFile().getPath() + "]");
            RTFEditorKit openKit = new RTFEditorKit();

            try {
                openKit.read(new FileInputStream(open.getSelectedFile()), textArea.getStyledDocument(), 0);
            } catch(Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private void saveFile() {
        JFileChooser save = new JFileChooser();
        int option = save.showSaveDialog(this);

        if(option == JFileChooser.APPROVE_OPTION) {
            this.setTitle(save.getName(save.getSelectedFile()) + " - [" + save.getSelectedFile().getPath() + "]");
            RTFEditorKit closeKit = new RTFEditorKit();

            try {
                closeKit.write(new FileOutputStream(save.getSelectedFile()), textArea.getStyledDocument(), 0, textArea.getStyledDocument().getEndPosition().getOffset());
            } catch(Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private void setStyle(String styleType) {
        StyledDocument sDoc = textArea.getStyledDocument();
        int selStart = textArea.getSelectionStart();
        int selEnd = textArea.getSelectionEnd();

        if(selStart == selEnd)
            return;

        if(selEnd < selStart) {
            int temp = selEnd;
            selEnd = selStart;
            selStart = temp;
        }

        Style style = textArea.addStyle("", null);
        switch (styleType) {
            case "bold": StyleConstants.setBold(style, true); break;
            case "italics": StyleConstants.setItalic(style, true); break;
            case "underline": StyleConstants.setUnderline(style, true); break;
            case "font": StyleConstants.setFontFamily(style, this.fontsMenu.getSelectedItem().toString()); break;
            case "color":
                switch(this.colorMenu.getSelectedItem().toString()) {
                    case "black": StyleConstants.setForeground(style, Color.BLACK); break;
                    case "blue": StyleConstants.setForeground(style, Color.BLUE); break;
                    case "cyan": StyleConstants.setForeground(style, Color.CYAN); break;
                    case "darkGray": StyleConstants.setForeground(style, Color.DARK_GRAY); break;
                    case "gray": StyleConstants.setForeground(style, Color.GRAY); break;
                    case "green": StyleConstants.setForeground(style, Color.GREEN); break;
                    case "lightGray": StyleConstants.setForeground(style, Color.LIGHT_GRAY); break;
                    case "magenta": StyleConstants.setForeground(style, Color.MAGENTA); break;
                    case "orange": StyleConstants.setForeground(style, Color.ORANGE); break;
                    case "pink": StyleConstants.setForeground(style, Color.PINK); break;
                    case "red": StyleConstants.setForeground(style, Color.RED); break;
                    case "white": StyleConstants.setForeground(style, Color.WHITE); break;
                    case "yellow": StyleConstants.setForeground(style, Color.YELLOW); break;
                }
        }
        sDoc.setCharacterAttributes(selStart, selEnd - selStart, style, false);
    }
}