package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class View extends JFrame {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int POS_X = 100;
    private static final int POS_Y = 100;
    private JPanel mainWindow;
    private JPanel bodyWindow;
    private JPanel courseWindow;
    private DB db;

    public View(DB db) {
        super("SchoolDB");
        this.db = db;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocation(POS_X, POS_Y);
        JMenuBar menuBar = createMenuBar();
        createMainWindow();
        createCourseWindow();
        add(menuBar, BorderLayout.NORTH);
        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu fileMenu = new JMenu();
        fileMenu.setSize(20, 40);
        fileMenu.setText("File");
        JMenuItem showItem = new JMenuItem("Show");
        showItem.addActionListener(actionEvent -> {
            List<Course> courses = db.selectAll();
            showList(courses);
        });
        JMenuItem newItem = new JMenuItem("Add");
        newItem.addActionListener(actionEvent -> {
            writeCourse();
        });
        JMenuItem change = new JMenuItem("Change");
        change.addActionListener(actionEvent -> {
            int id = getId();
        });
        JMenuItem delete = new JMenuItem("Delete");
        delete.addActionListener(actionEvent -> {

        });
        fileMenu.add(showItem);
        fileMenu.add(newItem);
        fileMenu.add(change);
        fileMenu.add(delete);
        bar.add(fileMenu);
        return bar;
    }

    private void createMainWindow() {
        mainWindow = new JPanel(new BorderLayout());
        JPanel head = addConstraints(false, "id", "Title", "Duration");
        bodyWindow = new JPanel();
        mainWindow.add(head, BorderLayout.NORTH);
        mainWindow.add(bodyWindow, BorderLayout.CENTER);
    }

    private JPanel addConstraints(Boolean checkBox, String id, String title, String duration) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.1;
        constraints.gridy = 0;
        constraints.gridx = 0;
        if (checkBox)
            panel.add(new JCheckBox(), constraints);
        else
            panel.add(new JLabel(" ", SwingConstants.CENTER), constraints);
        constraints.weightx = 0.2;
        constraints.gridx = 1;
        panel.add(new JLabel(id, SwingConstants.CENTER), constraints);
        constraints.weightx = 0.5;
        constraints.gridx = 2;
        panel.add(new JLabel(title, SwingConstants.CENTER), constraints);
        constraints.weightx = 0.2;
        constraints.gridx = 3;
        panel.add(new JLabel(duration, SwingConstants.CENTER), constraints);
        return panel;
    }

    private void createCourseWindow(){
        courseWindow = new JPanel();
        SpringLayout layout = new SpringLayout();
        courseWindow.setLayout(layout);
        JLabel titleLabel = new JLabel("Title ");
        JTextField titleField = new JTextField(25);
        JLabel durationLabel = new JLabel("Duration ");
        JSpinner durationSpinner = new JSpinner(new SpinnerNumberModel(
                1, 1, 50, 1));
        JButton okButton = new JButton("Ok");
        okButton.addActionListener(actionEvent -> {
            String title = titleField.getText();
            int duration = (int) durationSpinner.getValue();
            //TODO: Добавить валидацию
        });
        courseWindow.add(titleLabel);
        courseWindow.add(titleField);
        courseWindow.add(durationLabel);
        courseWindow.add(durationSpinner);
        courseWindow.add(okButton);
        layout.putConstraint(SpringLayout.NORTH, titleLabel, getWidth()/10,
                SpringLayout.NORTH, courseWindow);
        layout.putConstraint(SpringLayout.WEST, titleLabel, getHeight()/5,
                SpringLayout.WEST, courseWindow);
        layout.putConstraint(SpringLayout.NORTH, titleField, getWidth()/10,
                SpringLayout.NORTH, courseWindow);
        layout.putConstraint(SpringLayout.WEST, titleField, 100,
                SpringLayout.EAST , titleLabel);
        layout.putConstraint(SpringLayout.NORTH, durationLabel, 50,
                SpringLayout.NORTH, titleLabel);
        layout.putConstraint(SpringLayout.WEST, durationLabel, getHeight()/5,
                SpringLayout.WEST, courseWindow);
        layout.putConstraint(SpringLayout.NORTH, durationSpinner, 50,
                SpringLayout.NORTH, titleField);
        layout.putConstraint(SpringLayout.WEST, durationSpinner, 78,
                SpringLayout.EAST, durationLabel);
        layout.putConstraint(SpringLayout.NORTH, okButton, 70,
                SpringLayout.NORTH, durationLabel);
        layout.putConstraint(SpringLayout.WEST, okButton,
                getWidth()/2 - okButton.getWidth()/2,
                SpringLayout.WEST, courseWindow);
    }

    public void showList(List<Course> courses){
        remove(courseWindow);
        createMainWindow();
        int lineCount = 10;
        if (courses.size() > lineCount)
            lineCount = courses.size();
        bodyWindow.setLayout(new GridLayout(lineCount, 1));
        courses.forEach(it -> {
            JPanel line = addConstraints(true,
                    String.valueOf(it.getId()),
                    it.getTitle(),
                    String.valueOf(it.getDuration()));
            bodyWindow.add(line);
        });
        add(mainWindow);
        repaint();
        setVisible(true);
    }

    private void writeCourse() {
        remove(mainWindow);
        add(courseWindow);
        repaint();
        setVisible(true);
    }

    private int getId() {


        return 0;
    }
}
