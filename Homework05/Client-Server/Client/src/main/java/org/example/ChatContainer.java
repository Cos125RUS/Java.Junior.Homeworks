package org.example;

import javax.swing.*;
import java.awt.*;

public class ChatContainer extends Container {
    private final App app;
    private final long id;
    private final int width;
    private final int height;
    private final JPanel chatWindow;
    private final JPanel chatPanel;
    private final SpringLayout chatLayout;
    private final Container chatWindowContainer;
    private Container lastMessage;


    public ChatContainer(App app, long id, int width, int height) {
        this.app = app;
        this.id = id;
        this.width = width / 3 * 2;
        this.height = height;
        lastMessage = new Container();
        chatWindowContainer = new Container();
        chatPanel = new JPanel(new BorderLayout());
        chatLayout = new SpringLayout();
        chatWindow = new JPanel(chatLayout);
        create();
    }

    private void create() {
        setPreferredSize(new Dimension(width, height - 60));
        chatPanel.setSize(width, height - 60);
        chatPanel.setBackground(Color.getHSBColor(100, 100, 255));
        chatWindowContainer.setPreferredSize(new Dimension(width, height - 90));
        chatWindow.setSize(width, height - 90);
        chatWindow.setBackground(Color.YELLOW);
        chatPanel.add(chatWindowContainer, BorderLayout.CENTER);
        chatWindowContainer.add(chatWindow);
        lastMessage = getMessageContainer("");
        chatLayout.putConstraint(SpringLayout.NORTH, lastMessage,
                -((int) lastMessage.getPreferredSize().getHeight()), SpringLayout.NORTH, chatWindow);
        chatLayout.putConstraint(SpringLayout.WEST, lastMessage, 0, SpringLayout.WEST, chatWindow);
        JMenuBar entryBar = getEntryBar();
        chatPanel.add(entryBar, BorderLayout.SOUTH);
        add(chatPanel);
    }

    private JMenuBar getEntryBar() {
        JMenuBar entryBar = new JMenuBar();
        entryBar.setPreferredSize(new Dimension(width - 11, 30));
        JTextField entryField = new JTextField();
        entryBar.add(entryField);
        JButton sendButton = new JButton(">>");
        sendButton.addActionListener(action -> {
            String message = entryField.getText();
            app.sendMessage(id, message);
            chatWindow.setVisible(false);
            Container messageContainer = getMessageContainer(message);
            chatWindow.add(messageContainer);
            chatLayout.putConstraint(SpringLayout.NORTH, messageContainer,
                    (int) messageContainer.getPreferredSize().getHeight() + 15,
                    SpringLayout.NORTH, lastMessage);
            chatLayout.putConstraint(SpringLayout.EAST, messageContainer, -15,
                    SpringLayout.EAST, chatWindow);
            lastMessage = messageContainer;
            chatWindow.setVisible(true);
        });
        entryBar.add(sendButton);
        return entryBar;
    }

    private Container getMessageContainer(String message) {
        Container messageContainer = new Container();
        messageContainer.setPreferredSize(new Dimension(200, 50));
        JPanel messageBox = new JPanel(new BorderLayout());
        messageBox.setSize(200,50);
        messageBox.setBackground(Color.CYAN);
        messageContainer.add(messageBox);
        JLabel newMessage = new JLabel(message);
        messageBox.add(newMessage);
        return messageContainer;
    }

    public void addMessage(String message) {
        chatWindow.setVisible(false);
        Container messageContainer = getMessageContainer(message);
        chatWindow.add(messageContainer);
        chatLayout.putConstraint(SpringLayout.NORTH, messageContainer,
                (int) messageContainer.getPreferredSize().getHeight() + 15,
                SpringLayout.NORTH, lastMessage);
        chatLayout.putConstraint(SpringLayout.WEST, messageContainer, 15,
                SpringLayout.WEST, chatWindow);
        lastMessage = messageContainer;
        chatWindow.setVisible(true);
    }
}
