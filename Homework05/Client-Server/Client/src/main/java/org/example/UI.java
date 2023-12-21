package org.example;

import org.example.ChatModels.Chat;
import org.example.ChatModels.Contact;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class UI extends JFrame {
    private static final int WIDTH = 780;
    private static final int HEIGHT = 640;
    private static final int POS_X = 100;
    private static final int POS_Y = 100;

    private App app;
    private Container baseContainer;
    private Container chatsListContainer;
    private Container mainChatContainer;
    private JPanel loginWindow;
    private JPanel chatListWindow;
    private JPanel chatsField;
    private JPanel mainWindow;
    private JPanel currentChat;
    private SpringLayout currentChatLayout;
    private JLabel lastMessage;
    private HashMap<String, JButton> chats;

    public UI(App app) {
        super("The Messenger");
        this.app = app;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(WIDTH, HEIGHT);
        setLocation(POS_X, POS_Y);
        baseContainer = getContentPane();
        createLoginWindow();
        baseContainer.add(loginWindow);
        setVisible(true);
    }

    private void authorization(){
        createChatListWindow();
        createMainWindow();
        baseContainer.add(chatListWindow);
        baseContainer.add(mainWindow);
    }

    private void createLoginWindow() {
        SpringLayout layout = new SpringLayout();
        loginWindow = new JPanel(layout);
        loginWindow.setSize(WIDTH , HEIGHT);
        loginWindow.setBackground(Color.LIGHT_GRAY);
        JLabel loginLabel = new JLabel("Имя");
        JTextField loginField = new JTextField();
        loginField.setPreferredSize(new Dimension(200,20));
        JButton loginButton = new JButton("Войти");
        loginButton.addActionListener(action -> {
            String login = loginField.getText();
            if (app.authorization(login)) {
                loginWindow.setVisible(false);
                baseContainer.remove(loginWindow);
                authorization();
            }
        });
        loginWindow.add(loginLabel);
        loginWindow.add(loginField);
        loginWindow.add(loginButton);
        layout.putConstraint(SpringLayout.NORTH, loginLabel, HEIGHT / 3, SpringLayout.NORTH, loginWindow);
        layout.putConstraint(SpringLayout.WEST, loginLabel, WIDTH / 2 - 1, SpringLayout.WEST, loginWindow);
        layout.putConstraint(SpringLayout.NORTH, loginField, 25, SpringLayout.NORTH, loginLabel);
        layout.putConstraint(SpringLayout.WEST, loginField, WIDTH / 2 - 85, SpringLayout.WEST, loginWindow);
        layout.putConstraint(SpringLayout.NORTH, loginButton, 35, SpringLayout.NORTH, loginField);
        layout.putConstraint(SpringLayout.WEST, loginButton, WIDTH / 2 - 22, SpringLayout.WEST, loginWindow);
    }

    private void createChatListWindow() {
        chats = new HashMap<>();
        chatListWindow = new JPanel(new BorderLayout());
        chatListWindow.setSize(WIDTH / 3, HEIGHT);
        chatListWindow.setBackground(Color.GRAY);
        JMenuBar chatListBar = new JMenuBar();
        JMenu chatSetting = new JMenu("...");
        JMenuItem newContact = new JMenuItem("Новый контакт");
        newContact.addActionListener(action -> {
            String contact = JOptionPane.showInputDialog(this, "Введите имя контакта",
                    "Новый контакт");
            createChatInChatList(new Contact(12L, new User(contact)));
        });
        JMenuItem newGroup = new JMenuItem("Новая группа");
        newGroup.addActionListener(action -> {

        });
        JMenuItem setting = new JMenuItem("Настройки");
        setting.addActionListener(action -> {

        });
        chatSetting.add(newContact);
        chatSetting.add(newGroup);
        chatSetting.add(setting);
        JTextField search = new JTextField();
        chatListBar.add(chatSetting);
        chatListBar.add(search);
        chatListWindow.add(chatListBar, BorderLayout.NORTH);
        addChats();
    }

    private void addChats() {
        chatsField = new JPanel();
        chatListWindow.add(chatsField, BorderLayout.CENTER);
        List<Chat> chats = app.getChats();
        chats.forEach(this::createChatInChatList);
        createChatInChatList(new Contact(12L, new User("Contact01")));
        createChatInChatList(new Contact(12L, new User("Contact02")));
        createChatInChatList(new Contact(12L, new User("Contact03")));
    }

    private void createChatInChatList(Chat chat){
        chatsField.setVisible(false);
        JButton chatButton = new JButton(chat.getName());
        chatButton.setPreferredSize(new Dimension(WIDTH / 3 - 2, 50));
        chatButton.addActionListener(action -> {
            mainWindow.remove(currentChat);
            showChat(chat);
        });
        chatsField.add(chatButton);
        chats.put(chat.getName(), chatButton);
        chatsField.setVisible(true);
    }

    private void createMainWindow() {
        mainWindow = new JPanel(new BorderLayout());
        mainWindow.setSize(WIDTH / 3 * 2, HEIGHT);
        mainWindow.setBackground(Color.LIGHT_GRAY);
        JMenuBar mainBar = new JMenuBar();
        mainBar.setLayout(new BorderLayout());
        JMenu windowSetting = new JMenu("...");
        mainBar.add(windowSetting, BorderLayout.EAST);
        mainWindow.add(mainBar, BorderLayout.NORTH);
        currentChat = new JPanel();
        currentChat.setVisible(false);
        mainWindow.add(currentChat, BorderLayout.CENTER);
    }

    private void showChat(Chat chat) {
//        TODO загрузка истории чата
        mainWindow.setVisible(false);
        currentChatLayout = new SpringLayout();
        currentChat = new JPanel(currentChatLayout);
        currentChat.setBackground(Color.getHSBColor(100,100,255));
        mainWindow.add(currentChat, BorderLayout.CENTER);
        lastMessage = new JLabel();
        currentChatLayout.putConstraint(SpringLayout.NORTH, lastMessage, 0, SpringLayout.NORTH, currentChat);
        currentChatLayout.putConstraint(SpringLayout.WEST, lastMessage, WIDTH/3, SpringLayout.WEST, currentChat);
        JMenuBar entryBar = getEntryBar();
        currentChat.add(entryBar);
        currentChatLayout.putConstraint(SpringLayout.SOUTH, entryBar, 0, SpringLayout.SOUTH, currentChat);
        currentChatLayout.putConstraint(SpringLayout.WEST, entryBar, WIDTH/3, SpringLayout.WEST, currentChat);
        mainWindow.setVisible(true);
    }

    private JMenuBar getEntryBar() {
        JMenuBar entryBar = new JMenuBar();
        entryBar.setPreferredSize(new Dimension(WIDTH/3*2 - 14, 30));
        JTextField entryField = new JTextField();
        entryBar.add(entryField);
        JButton sendButton = new JButton(">>");
        sendButton.addActionListener(action -> {
            String message = entryField.getText();
            app.sendMessage(message);
            mainWindow.setVisible(false);
            JLabel newMessage = new JLabel(message);
            currentChat.add(newMessage);
            currentChatLayout.putConstraint(SpringLayout.NORTH, newMessage, 20, SpringLayout.NORTH, lastMessage);
            currentChatLayout.putConstraint(SpringLayout.EAST, newMessage, - (message.length() + 10),
                    SpringLayout.EAST, currentChat);
            lastMessage = newMessage;
            mainWindow.setVisible(true);
        });
        entryBar.add(sendButton);
        return entryBar;
    }

    public void print(String message) {
        mainWindow.setVisible(false);
        JLabel newMessage = new JLabel(message);
        currentChat.add(newMessage);
        currentChatLayout.putConstraint(SpringLayout.NORTH, newMessage, 20, SpringLayout.NORTH, lastMessage);
        currentChatLayout.putConstraint(SpringLayout.WEST, newMessage, WIDTH/3+10, SpringLayout.WEST, currentChat);
        lastMessage = newMessage;
        mainWindow.setVisible(true);
    }
}
