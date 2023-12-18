package org.example;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class View extends JFrame {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int POS_X = 100;
    private static final int POS_Y = 100;
    private JPanel mainWindow;
    private JPanel courseWindow;
    private DB db;
    private HashMap<JCheckBox, Course> coursesMap;

    public View(DB db) {
        super("SchoolDB");
        this.db = db;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocation(POS_X, POS_Y);
        JMenuBar menuBar = createMenuBar();
        mainWindow = new JPanel();
        createCourseWindow();
        add(menuBar, BorderLayout.NORTH);
        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu fileMenu = new JMenu();
        fileMenu.setSize(20, 40);
        fileMenu.setText("File");
        JMenuItem showItem = new JMenuItem("Show list");
        showItem.addActionListener(actionEvent -> {
            showList(db.selectAll());
        });
        JMenuItem newItem = new JMenuItem("Add new course");
        newItem.addActionListener(actionEvent -> {
            createCourseWindow();
            writeCourse();
        });
        JMenuItem delete = new JMenuItem("Delete selected");
        delete.addActionListener(actionEvent -> {
            //TODO: Добавить множественное удаление
        });
        fileMenu.add(showItem);
        fileMenu.add(newItem);
        fileMenu.add(delete);
        bar.add(fileMenu);
        return bar;
    }

    private void createCourseWindow() {
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
            db.create(new Course(title, duration));
            showList(db.selectAll());
        });
        addLayout(layout, titleLabel, titleField, durationLabel, durationSpinner, okButton);
    }

    private void createCourseWindow(int id){
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
            db.update(id, title, duration);
            showList(db.selectAll());
        });
        addLayout(layout, titleLabel, titleField, durationLabel, durationSpinner, okButton);
    }

    private void addLayout(SpringLayout layout, JLabel titleLabel, JTextField titleField,
                           JLabel durationLabel, JSpinner durationSpinner, JButton okButton){
        courseWindow.add(titleLabel);
        courseWindow.add(titleField);
        courseWindow.add(durationLabel);
        courseWindow.add(durationSpinner);
        courseWindow.add(okButton);
        putConstraintElement(layout, titleLabel, courseWindow, courseWindow,
                getWidth() / 10, getHeight() / 5);
        layout.putConstraint(SpringLayout.NORTH, titleField, getWidth() / 10,
                SpringLayout.NORTH, courseWindow);
        layout.putConstraint(SpringLayout.WEST, titleField, 100,
                SpringLayout.EAST, titleLabel);
        layout.putConstraint(SpringLayout.NORTH, durationLabel, 50,
                SpringLayout.NORTH, titleLabel);
        layout.putConstraint(SpringLayout.WEST, durationLabel, getHeight() / 5,
                SpringLayout.WEST, courseWindow);
        layout.putConstraint(SpringLayout.NORTH, durationSpinner, 50,
                SpringLayout.NORTH, titleField);
        layout.putConstraint(SpringLayout.WEST, durationSpinner, 78,
                SpringLayout.EAST, durationLabel);
        layout.putConstraint(SpringLayout.NORTH, okButton, 70,
                SpringLayout.NORTH, durationLabel);
        layout.putConstraint(SpringLayout.WEST, okButton,
                getWidth() / 2 - okButton.getWidth() / 2,
                SpringLayout.WEST, courseWindow);
    }

    private void putConstraintElement(SpringLayout layout, JComponent component,
                                      JComponent up, JComponent left, int stepVert, int stepHor) {
        layout.putConstraint(SpringLayout.NORTH, component, stepVert, SpringLayout.NORTH, up);
        layout.putConstraint(SpringLayout.WEST, component, stepHor, SpringLayout.WEST, left);
    }

    public void showList(List<Course> courses) {
        remove(courseWindow);
        SpringLayout layout = new SpringLayout();
        mainWindow = new JPanel(layout);
        JLabel num = new JLabel(" ");
        mainWindow.add(num);
        JLabel title = new JLabel("Title");
        mainWindow.add(title);
        JLabel duration = new JLabel("Duration");
        mainWindow.add(duration);
        putConstraintElement(layout, num, mainWindow, mainWindow, 0, 5);
        putConstraintElement(layout, title, mainWindow, num, 0, getHeight() / 5);
        putConstraintElement(layout, duration, mainWindow, title, 0,  getHeight() / 3 *2);
        coursesMap = new HashMap<>();
        for (int i = 0; i < courses.size(); i++){
            Course course = courses.get(i);
            JCheckBox checkBox = new JCheckBox();
            JLabel courseTitle = new JLabel(course.getTitle());
            JLabel courseDuration = new JLabel(String.valueOf(course.getDuration()));
            JButton update = new JButton("update");
            update.setPreferredSize(new Dimension(40, 15));
            update.addActionListener(eventAction -> {
                int id = course.getId();
                createCourseWindow(id);
                writeCourse();
            });
            JButton del = new JButton("delete");
            del.setPreferredSize(new Dimension(40, 15));
            del.addActionListener(eventAction -> {
                db.delete(course.getId());
                remove(mainWindow);
                showList(db.selectAll());
            });
            coursesMap.put(checkBox, course);
            mainWindow.add(checkBox);
            mainWindow.add(courseTitle);
            mainWindow.add(courseDuration);
            mainWindow.add(update);
            mainWindow.add(del);
            putConstraintElement(layout, checkBox, mainWindow, mainWindow, ((i+1)*20), 5);
            putConstraintElement(layout, courseTitle, mainWindow, checkBox,
                    ((i+1)*20), getHeight() / 5);
            putConstraintElement(layout, courseDuration, mainWindow, courseTitle,
                    ((i+1)*20), getHeight() / 3 *2);
            putConstraintElement(layout, update, mainWindow, courseDuration, ((i+1)*20), 100);
            putConstraintElement(layout, del, mainWindow, update, ((i+1)*20), 50);
        }
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
}
