package com.arogya.stage.sms;

import java.awt.Color;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class SendSms {

    public JFrame frmSendYourSms1;
    private JTextField textField;
    private JButton btnSendNow;
    private JPanel panel;
    private JTextArea textArea;
    private JScrollPane scrollPane;

    /**
     * Launch the application.
     */
    

    /**
     * Create the application.
     */
    public SendSms() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frmSendYourSms1 = new JFrame();
        frmSendYourSms1.setFont(new Font("Sitka Text", Font.BOLD | Font.ITALIC, 18));
        frmSendYourSms1.setIconImage(Toolkit.getDefaultToolkit().getImage(SendSms.class.getResource("/way2sms/DSCN1497.JPG" )));
        frmSendYourSms1.getContentPane().setBackground(new Color(248, 248, 255));
        frmSendYourSms1.setBackground(new Color(255, 245, 238));
        frmSendYourSms1.setResizable(false);
        frmSendYourSms1.setForeground(new Color(139, 0, 0));
        frmSendYourSms1.setTitle("Send your Sms" );
        frmSendYourSms1.setBounds(100, 100, 450, 300);
        frmSendYourSms1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JLabel lblResci = new JLabel("Recipient" );
        lblResci.setForeground(new Color(139, 0, 0));
        lblResci.setFont(new Font("Sitka Text", Font.BOLD | Font.ITALIC, 14));
        frmSendYourSms1.getContentPane().add(lblResci, "1, 3, center, center" );
        
        textField = new JTextField();
        textField.setBackground(new Color(255, 239, 213));
        textField.setToolTipText("Enter recipient number" );
        textField.setFont(new Font("Tahoma", Font.BOLD, 18));
        frmSendYourSms1.getContentPane().add(textField, "3, 3, center, center" );
        textField.setColumns(12);
        
        JLabel lblNewLabel = new JLabel("Message" );
        lblNewLabel.setForeground(new Color(139, 0, 0));
        lblNewLabel.setFont(new Font("Sitka Text", Font.BOLD | Font.ITALIC, 14));
        frmSendYourSms1.getContentPane().add(lblNewLabel, "1, 4, center, center" );
        
        panel = new JPanel();
        panel.setBackground(new Color(248, 248, 255));
        frmSendYourSms1.getContentPane().add(panel, "3, 4, fill, center" );
        
        scrollPane = new JScrollPane();
        panel.add(scrollPane);
        
        textArea = new JTextArea();
        textArea.setBackground(new Color(255, 239, 213));
        scrollPane.setViewportView(textArea);
        textArea.setToolTipText("Enter your message" );
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 14));
        textArea.setRows(5);
        textArea.setColumns(17);
        
        btnSendNow = new JButton("Send Now" );
        
        btnSendNow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
            String rec=textField.getText().trim();
            String msg=textArea.getText().trim();
            try
            {
                String status="message is sent to "+rec;
                WaySms ws=new WaySms();
                ws.sendSMS(rec, msg);
                JOptionPane.showMessageDialog(null,status,"Information message",JOptionPane.INFORMATION_MESSAGE);        
                textArea.setText("" );
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, 
                        "Retry Again..error to connect!!","Error message", 
                        JOptionPane.ERROR_MESSAGE);
            }
            
            }
        });
        
        btnSendNow.setBackground(new Color(255, 228, 225));
        btnSendNow.setForeground(new Color(220, 20, 60));
        btnSendNow.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
        frmSendYourSms1.getContentPane().add(btnSendNow, "3, 5, center, center" );
    }
}