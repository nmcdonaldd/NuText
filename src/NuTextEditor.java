/*
 * Nicholas McDonald
 * nmmcdona@ucsd.edu
 *
 *
 * NuText.java
 */

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.rtf.RTFEditorKit;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;
import java.io.*;

public class NuTextEditor extends JFrame implements ActionListener {
    private JTextPane textArea = new JTextPane();
    private MenuBar menuBar = new MenuBar();

    private JScrollPane jScrollPane = new JScrollPane(textArea);

    private Menu file = new Menu();
    private MenuItem openFile = new MenuItem();
    private MenuItem saveFile = new MenuItem();
    private MenuItem close = new MenuItem();

    private Menu selection = new Menu();
    private MenuItem bold = new MenuItem();
    private MenuItem italics = new MenuItem();
    private MenuItem underline = new MenuItem();
    private MenuItem strikeThrough = new MenuItem();

    public NuTextEditor() {
        this.setSize(900, 500);
        this.setTitle("NuText - Untitled Document");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.textArea.setFont(new Font("Times New Roman", Font.PLAIN, 14));
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

        this.strikeThrough.setLabel("Strike-Through");
        this.strikeThrough.addActionListener(this);
        this.strikeThrough.setShortcut(new MenuShortcut(KeyEvent.VK_R, false));
        this.selection.add(this.strikeThrough);

        this.getContentPane().add(this.jScrollPane, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.close)
            closeDocument();

        else if(e.getSource() == this.openFile)
            openFile();

        else if(e.getSource() == this.saveFile)
            saveFile();

        else if(e.getSource() == this.bold)
            setStyle("bold");

        else if(e.getSource() == this.italics)
            setStyle("italics");

        else if(e.getSource() == this.underline)
            setStyle("underline");
    }

    private void closeDocument() { this.dispose(); }

    private void openFile() {
        JFileChooser open = new JFileChooser();
        int option = open.showOpenDialog(this);

        if(option == JFileChooser.APPROVE_OPTION) {
            this.textArea.setText("");
            this.setTitle("NuText - " + open.getName(open.getSelectedFile()));
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
            this.setTitle("NuText - " + save.getName(save.getSelectedFile()));
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

        Style styledDoc = textArea.addStyle("", null);
        switch (styleType) {
            case "bold": StyleConstants.setBold(styledDoc, true); break;
            case "italics": StyleConstants.setItalic(styledDoc, true); break;
            case "underline": StyleConstants.setUnderline(styledDoc, true); break;
            case "strike": StyleConstants.setStrikeThrough(styledDoc, true); break;
        }
        sDoc.setCharacterAttributes(selStart, selEnd - selStart, styledDoc, false);
    }
}