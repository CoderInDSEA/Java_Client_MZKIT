package com.company;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class GUI_UDPClient extends Frame implements ActionListener {
    private Label LabelGUI;    // Declare a Label component
    public TextField TextFieldGui; // Declare a TextField component
    public Button ButtonGui;   // Declare a Button component
    public String command ;
    public final static int SERVICE_PORT = 50001;

    // Constructor to setup GUI components and event handlers
    public GUI_UDPClient() {
        setLayout(new FlowLayout());

        LabelGUI = new Label("Command Enter");  // construct the Label component
        add(LabelGUI);                    // "super" Frame container adds Label component

        TextFieldGui = new TextField( 20); // construct the TextField component with initial text
        TextFieldGui.setEditable(true);       // set to read-only
        add(TextFieldGui);                     // "super" Frame container adds TextField component

        ButtonGui = new Button("Send");   // construct the Button component
        add(ButtonGui);                    // "super" Frame container adds Button component

        ButtonGui.addActionListener(this);


        setTitle("AWT Counter");  // "super" Frame sets its title
        setSize(250, 100);        // "super" Frame sets its initial window size
        setVisible(true);         // "super" Frame shows
    }

    // ActionEvent handler - Called back upon button-click.
    @Override
    public void actionPerformed(ActionEvent evt)   {
        try {
            command = TextFieldGui.getText();
            ClientFunc(command);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String ClientFunc(String command) throws IOException{
        try {
            DatagramSocket clientSocket = new DatagramSocket();

            // Получите IP-адрес сервера
            InetAddress IPAddress = InetAddress.getByName("localhost");

            Scanner input = new Scanner(System.in);
            byte[] sendingDataBuffer = new byte[command.length()];
            byte[] sendingBufferCommandInit = new byte[1024];
            sendingDataBuffer = command.getBytes();
            int indexPar = command.indexOf ("%") ;


            DatagramPacket sendingPacket = new DatagramPacket(sendingDataBuffer,sendingDataBuffer.length,IPAddress, SERVICE_PORT);
            clientSocket.send(sendingPacket);
            byte[] receivingDataBuffer = new byte[sendingDataBuffer.length];
            DatagramPacket receivingPacket = new DatagramPacket(receivingDataBuffer,receivingDataBuffer.length);
            clientSocket.receive(receivingPacket);
            String receivedData = new String(receivingPacket.getData());
            System.out.println("Sent from the server: "+receivedData);

            // Закройте соединение с сервером через сокет
            clientSocket.close();
        }
        catch(SocketException e) {
            e.printStackTrace();
        }
        return "Fail";
    }
}
