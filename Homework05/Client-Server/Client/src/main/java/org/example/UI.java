package org.example;

import org.example.ChatModels.Chat;
import org.example.ChatModels.User;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class UI extends JFrame {
    private static final int WIDTH = 780;
    private static final int HEIGHT = 640;
    private static final int POS_X = 100;
    private static final int POS_Y = 100;

    private final App app;
    private User user;
    private Container baseContainer;
    private Container loginContainer;
    private Container chatsListContainer;
    private Container mainChatContainer;
    private Container currentChatContainer;
    private Container chatWindowContainer;
    private Container lastMessage;
    private JPanel loginWindow;
    private JPanel chatListWindow;
    private JPanel chatsField;
    private JPanel mainWindow;
    private JPanel currentChatPanel;
    private JPanel currentChatWindow;
    private HashMap<Long, JPanel> activeChatsWindows;
    private SpringLayout currentChatLayout;
    private HashMap<String, JButton> chats;

    public UI(App app) {
        super("The Messenger");
        this.app = app;
        activeChatsWindows = new HashMap<>();
        createMainFrame();
        createLoginWindow();
        baseContainer.add(loginContainer);
        setVisible(true);
    }

    public UI(App app, User user) {
        super("The Messenger");
        this.app = app;
        this.user = user;
        activeChatsWindows = new HashMap<>();
        createMainFrame();
        if (app.authorization(user.getName())) {
            app.saveUserData(user);
            authorization();
        }
        setVisible(true);
    }

    private void createMainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(WIDTH, HEIGHT);
        setLocation(POS_X, POS_Y);
        createBaseContainer();
    }

    private void createBaseContainer() {
        baseContainer = getContentPane();
        baseContainer.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        baseContainer.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    }

    private void authorization() {
        createChatListWindow();
        createMainWindow();
        setBaseContainer();
    }

    private void setBaseContainer() {
        baseContainer.setLayout(new BoxLayout(baseContainer, BoxLayout.X_AXIS));
        baseContainer.add(chatsListContainer);
        baseContainer.add(mainChatContainer);
    }

    private void createLoginWindow() {
        loginContainer = new Container();
        loginContainer.setSize(WIDTH, HEIGHT);
        SpringLayout layout = new SpringLayout();
        loginWindow = new JPanel(layout);
        loginWindow.setSize(WIDTH, HEIGHT);
        loginWindow.setBackground(Color.LIGHT_GRAY);
        JLabel loginLabel = new JLabel("Имя");
        JTextField loginField = new JTextField();
        loginField.setPreferredSize(new Dimension(200, 20));
        JButton loginButton = new JButton("Войти");
        loginButton.addActionListener(action -> {
            String login = loginField.getText();
            if (app.authorization(login)) {
                user = app.getUser();
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
        loginContainer.add(loginWindow);
    }

    private void createChatListWindow() {
        chats = new HashMap<>();
        chatsListContainer = new Container();
        chatsListContainer.setPreferredSize(new Dimension(WIDTH / 3, HEIGHT));
        chatListWindow = new JPanel(new BorderLayout());
        chatListWindow.setSize(WIDTH / 3, HEIGHT);
        chatListWindow.setBackground(Color.GRAY);
        JMenuBar chatListBar = new JMenuBar();
        JMenu chatSetting = new JMenu("...");
        JMenuItem newContact = new JMenuItem("Новый контакт");
        newContact.addActionListener(action -> {
            String contact = JOptionPane.showInputDialog(this, "Введите имя контакта",
                    "Новый контакт");
            app.newContact(contact);
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
        chatListBar.add(new JLabel("   "));
        chatListWindow.add(chatListBar, BorderLayout.NORTH);
        addChats();
        chatsListContainer.add(chatListWindow);
    }

    private void addChats() {
        chatsField = new JPanel();
        chatListWindow.add(chatsField, BorderLayout.CENTER);
        List<Chat> chats = app.getChats();
        chats.forEach(this::createChatInChatList);
    }

    public void createChatInChatList(Chat chat) {
        chatsField.setVisible(false);
        JButton chatButton = new JButton(chat.getName());
        chatButton.setPreferredSize(new Dimension(WIDTH / 3, 50));
        chatButton.addActionListener(action -> {
            mainWindow.remove(currentChatPanel);
            showChat(chat);
        });
        chatsField.add(chatButton);
        chats.put(chat.getName(), chatButton);
        chatsField.setVisible(true);
    }

    private void createMainWindow() {
        mainChatContainer = new Container();
        mainChatContainer.setPreferredSize(new Dimension(WIDTH / 3 * 2, HEIGHT));
        mainWindow = new JPanel(new BorderLayout());
        mainWindow.setSize(WIDTH / 3 * 2 - 10, HEIGHT);
        mainWindow.setBackground(Color.LIGHT_GRAY);
        JMenuBar mainBar = new JMenuBar();
        mainBar.setSize(30, (int) mainWindow.getSize().getHeight());
        mainBar.setLayout(new BorderLayout());
        JMenu windowSetting = new JMenu("...");
        JLabel userName = new JLabel(user.getName());
        mainBar.add(windowSetting, BorderLayout.EAST);
        mainBar.add(userName, BorderLayout.WEST);
        mainWindow.add(mainBar, BorderLayout.NORTH);
        currentChatPanel = new JPanel();
        currentChatPanel.setVisible(false);
        mainWindow.add(currentChatPanel, BorderLayout.CENTER);
        mainChatContainer.add(mainWindow);
    }

    private void showChat(Chat chat) {
//        TODO загрузка истории чата
        mainWindow.setVisible(false);
        currentChatContainer = new Container();
        currentChatContainer.setPreferredSize(new Dimension(WIDTH / 3 * 2, HEIGHT - 60));
        currentChatLayout = new SpringLayout();
        currentChatPanel = new JPanel(new BorderLayout());
        currentChatPanel.setSize(WIDTH / 3 * 2, HEIGHT - 60);
        currentChatPanel.setBackground(Color.getHSBColor(100, 100, 255));
        chatWindowContainer = new Container();
        chatWindowContainer.setPreferredSize(new Dimension(WIDTH / 3 * 2, HEIGHT - 90));
        currentChatWindow = new JPanel(currentChatLayout);
        currentChatWindow.setSize(WIDTH / 3 * 2, HEIGHT - 90);
        currentChatWindow.setBackground(Color.YELLOW);
        currentChatPanel.add(chatWindowContainer, BorderLayout.CENTER);
        chatWindowContainer.add(currentChatWindow);
        lastMessage = getMessageContainer("");
        currentChatLayout.putConstraint(SpringLayout.NORTH, lastMessage,
                -((int) lastMessage.getPreferredSize().getHeight()), SpringLayout.NORTH, currentChatWindow);
        currentChatLayout.putConstraint(SpringLayout.WEST, lastMessage, 0, SpringLayout.WEST, currentChatWindow);
        JMenuBar entryBar = getEntryBar(chat);
        currentChatPanel.add(entryBar, BorderLayout.SOUTH);
        currentChatContainer.add(currentChatPanel);
        mainWindow.add(currentChatContainer, BorderLayout.CENTER);
        mainWindow.setVisible(true);
    }

    private JMenuBar getEntryBar(Chat chat) {
        JMenuBar entryBar = new JMenuBar();
        entryBar.setPreferredSize(new Dimension(WIDTH / 3 * 2 - 11, 30));
        JTextField entryField = new JTextField();
        entryBar.add(entryField);
        JButton sendButton = new JButton(">>");
        sendButton.addActionListener(action -> {
            String message = entryField.getText();
            app.sendMessage(chat.getChatID(), message);
            currentChatWindow.setVisible(false);
            Container messageContainer = getMessageContainer(message);
            currentChatWindow.add(messageContainer);
            currentChatLayout.putConstraint(SpringLayout.NORTH, messageContainer,
                    (int) messageContainer.getPreferredSize().getHeight() + 15,
                    SpringLayout.NORTH, lastMessage);
            currentChatLayout.putConstraint(SpringLayout.EAST, messageContainer, -15,
                    SpringLayout.EAST, currentChatWindow);
            lastMessage = messageContainer;
            currentChatWindow.setVisible(true);
        });
        entryBar.add(sendButton);
        return entryBar;
    }

    public void print(String message) {
        currentChatWindow.setVisible(false);
        Container messageContainer = getMessageContainer(message);
        currentChatWindow.add(messageContainer);
        currentChatLayout.putConstraint(SpringLayout.NORTH, messageContainer,
                (int) messageContainer.getPreferredSize().getHeight() + 15,
                SpringLayout.NORTH, lastMessage);
        currentChatLayout.putConstraint(SpringLayout.WEST, messageContainer, 15,
                SpringLayout.WEST, currentChatWindow);
        lastMessage = messageContainer;
        currentChatWindow.setVisible(true);
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
}
