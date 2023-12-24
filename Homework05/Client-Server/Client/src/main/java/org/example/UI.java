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
    private JPanel loginWindow;
    private JPanel chatListWindow;
    private JPanel chatsField;
    private JPanel mainWindow;
    private HashMap<Long, ChatContainer> activeChatsContainer;
    private HashMap<String, JButton> chats;

    public UI(App app) {
        super("The Messenger");
        this.app = app;
        activeChatsContainer = new HashMap<>();
        createMainFrame();
        createLoginWindow();
        baseContainer.add(loginContainer);
        setVisible(true);
    }

    public UI(App app, User user) {
        super("The Messenger");
        this.app = app;
        this.user = user;
        activeChatsContainer = new HashMap<>();
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
//TODO Создание группы
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
            mainWindow.remove(currentChatContainer);
            showChat(chat);
        });
        chatsField.add(chatButton);
        chats.put(chat.getName(), chatButton);
        createChatContainer(chat);
        chatsField.setVisible(true);
    }

    private void createChatContainer(Chat chat) {
        ChatContainer chatContainer = new ChatContainer(app, chat.getChatID(), WIDTH, HEIGHT);
        activeChatsContainer.put(chat.getChatID(), chatContainer);
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
        currentChatContainer = new JPanel();
        currentChatContainer.setVisible(false);
        mainWindow.add(currentChatContainer, BorderLayout.CENTER);
        mainChatContainer.add(mainWindow);
    }

    private void showChat(Chat chat) {
//        TODO загрузка истории чата
        mainWindow.setVisible(false);
        currentChatContainer = activeChatsContainer.get(chat.getChatID());
        mainWindow.add(currentChatContainer, BorderLayout.CENTER);
        mainWindow.setVisible(true);
    }

    public void addMessage(long chatId, String message) {
        ChatContainer chatContainer = activeChatsContainer.get(chatId);
        chatContainer.addMessage(message);
    }

    public void notFoundContact() {
        JOptionPane.showMessageDialog(null, "Пользователь не найден");
    }
}
