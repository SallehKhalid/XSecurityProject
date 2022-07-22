package cs432project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import javax.swing.JFileChooser;

public class RunLoginPage {
    public static void main(String[] args) {
        LoginFrame frame = new LoginFrame();
        frame.setTitle("Security Login");
        frame.setVisible(true);
        frame.setBounds(10, 10, 370, 600);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setResizable(false);

    }
}

class LoginFrame extends JFrame implements ActionListener {

    Container container = getContentPane();
    JLabel userLabel = new JLabel("USERNAME");
    JLabel passwordLabel = new JLabel("PASSWORD");
    JTextField userTextField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("LOGIN");
    JButton registerButton = new JButton("REGISTER");
    JButton browseButton = new JButton("Browse");
    JCheckBox showPassword = new JCheckBox("Show Password");
    String Keys_Folder_Path = "";
    JLabel Path_Label = new JLabel();
    JLabel Title = new JLabel("XSecurity");

    LoginFrame() {
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();

    }

    public void setLayoutManager() {
        container.setLayout(null);
    }

    public void setLocationAndSize() {
        userLabel.setBounds(50, 150, 100, 30);
        passwordLabel.setBounds(50, 220, 100, 30);
        userTextField.setBounds(150, 150, 150, 30);
        passwordField.setBounds(150, 220, 150, 30);
        showPassword.setBounds(150, 250, 150, 30);
        showPassword.setBackground(Color.cyan);
        loginButton.setBounds(50, 300, 100, 30);
        registerButton.setBounds(200, 300, 100, 30);
        browseButton.setBounds(125, 350, 100, 30);
        container.setBackground(Color.cyan);
        Title.setBounds(150, 50, 100, 30);
        Font font = new Font("Times new roman", 50, 23);
        Title.setFont(font);

    }

    public void addComponentsToContainer() {
        container.add(userLabel);
        container.add(passwordLabel);
        container.add(userTextField);
        container.add(passwordField);
        container.add(showPassword);
        container.add(loginButton);
        container.add(browseButton);
        container.add(registerButton);
        container.add(Title);
    }

    public void addActionEvent() {
        loginButton.addActionListener(this);
        browseButton.addActionListener(this);
        showPassword.addActionListener(this);
        registerButton.addActionListener(this);
    }

    public void doGenkey()
            throws java.security.NoSuchAlgorithmException,
            java.io.IOException {
        String fileBase = Keys_Folder_Path;
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        try (FileOutputStream out = new FileOutputStream(fileBase + "\\" + userTextField.getText() + "public.key")) {
            out.write(kp.getPrivate().getEncoded());
        }

        try (FileOutputStream out = new FileOutputStream(fileBase + "\\" + userTextField.getText() + "private.key")) {
            out.write(kp.getPublic().getEncoded());
        }
        System.out.println("--KEYS GENERATED--");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String userText;
            String pwdText;
            userText = userTextField.getText();
            pwdText = passwordField.getText();
            if (userText != null && pwdText != null && Keys_Folder_Path != "") {
                JOptionPane.showMessageDialog(this, "Login Successful");
                new Encryptwindow(Keys_Folder_Path).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Enter Name or password or choose folder");
            }

        }

        if (e.getSource() == registerButton) {
            try {
                doGenkey();
                JOptionPane.showMessageDialog(this, "Registeration Successful");
                new Encryptwindow(Keys_Folder_Path).setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please Choose a file");
            }

        }

        if (e.getSource() == browseButton) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int option = fileChooser.showOpenDialog(this);

            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                Keys_Folder_Path = file.getAbsolutePath();
                Path_Label.setText("Folder Selected: " + Keys_Folder_Path);
            } else {
                Path_Label.setText("Open command canceled");
            }
        }

        if (e.getSource() == showPassword) {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
            }
        }

    }
}
