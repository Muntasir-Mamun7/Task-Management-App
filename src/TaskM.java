/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
import javax.swing.*;
import javax.swing.event.DocumentListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.Color;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;


import javax.swing.event.DocumentEvent;

/**
 *
 * @author 
 */
public class TaskM extends javax.swing.JFrame {
    
    private Map<LocalDate, String[]> toDoTasksMap = new HashMap<>();
    private Map<LocalDate, String[]> completedTasksMap = new HashMap<>();
    private Map<JButton, String> taskDetailsMap = new HashMap<>();
    private LocalDate currentDate;
    private JButton currentSelectedButton;
    private JPopupMenu suggestionPopup;
    private List<String> taskSuggestions;

    public TaskM() {
    setTitle("Tasukuu~izādo 1.0.0.5"); // Set the title of the application
    initComponents();
    jComboBox1.setVisible(false);  // Make jComboBox1 invisible
    displayCurrentDate();
    addActionListeners();
    initTimeComboBox();
    addTimeComboBoxListener();
    addComponentsToScrollPane();
    initTaskSuggestions();
    addTaskSuggestionListener();
}

   private void showTutorial() {
    String tutorialText = """
                          Welcome to Tasukuu~iz\u0101do 1.0.0.5!
                          
                          1. To add a task, type the task in the text field and press Enter or click the Add button.
                          2. To mark a task as completed, click the 'Complete' button.
                          3. Use the date buttons to view tasks for different days.
                          4. Right-click on a task to see more options, such as viewing task details.
                          5. The system will automatically save your tasks and completed tasks.
                          
                          Enjoy using Tasukuu~iz\u0101do!""";
    
    JOptionPane.showMessageDialog(this, tutorialText, "Tutorial", JOptionPane.INFORMATION_MESSAGE);
}
    
    private void initTaskSuggestions() {
        taskSuggestions = new ArrayList<>();
        // Add some default task suggestions
        taskSuggestions.add("Meeting with team");
        taskSuggestions.add("Submit report");
        taskSuggestions.add("Call client");
        taskSuggestions.add("Review code");
        taskSuggestions.add("Plan project");
        taskSuggestions.add("Update documentation");
        taskSuggestions.add("Fix bugs");
        taskSuggestions.add("Prepare presentation");
        taskSuggestions.add("Attend workshop");
        taskSuggestions.add("Conduct interview");
        taskSuggestions.add("Analyze data");
        taskSuggestions.add("Design new feature");
        taskSuggestions.add("Write unit tests");
        taskSuggestions.add("Deploy application");
        taskSuggestions.add("Monitor system");
        taskSuggestions.add("Optimize performance");
        taskSuggestions.add("Backup database");
        taskSuggestions.add("Research new technology");
        taskSuggestions.add("Create user stories");
        taskSuggestions.add("Review pull requests");
        taskSuggestions.add("Schedule meeting");
    }
    
    private void addTaskSuggestionListener() {
        suggestionPopup = new JPopupMenu();
        suggestionPopup.setFocusable(true); // Make the popup focusable
        jTextField1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                showSuggestions();
            }
    
            @Override
            public void removeUpdate(DocumentEvent e) {
                showSuggestions();
            }
    
            @Override
            public void changedUpdate(DocumentEvent e) {
                showSuggestions();
            }
        });
    
        jTextField1.addKeyListener(new KeyAdapter() {
            private int selectedIndex = -1;
    
            @Override
            public void keyPressed(KeyEvent e) {
                if (suggestionPopup.isVisible()) {
                    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        selectedIndex = (selectedIndex + 1) % suggestionPopup.getComponentCount();
                        updateSelection();
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        selectedIndex = (selectedIndex - 1 + suggestionPopup.getComponentCount()) % suggestionPopup.getComponentCount();
                        updateSelection();
                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (selectedIndex >= 0 && selectedIndex < suggestionPopup.getComponentCount()) {
                            JMenuItem item = (JMenuItem) suggestionPopup.getComponent(selectedIndex);
                            item.doClick();
                        }
                    }
                }
            }
    
            private void updateSelection() {
                for (int i = 0; i < suggestionPopup.getComponentCount(); i++) {
                    JMenuItem item = (JMenuItem) suggestionPopup.getComponent(i);
                    item.setArmed(i == selectedIndex);
                }
            }
        });
    }
    
    private void showSuggestions() {
        String text = jTextField1.getText().trim();
        suggestionPopup.removeAll();
    
        if (!text.isEmpty()) {
            for (String suggestion : taskSuggestions) {
                if (suggestion.toLowerCase().contains(text.toLowerCase())) {
                    JMenuItem item = new JMenuItem(suggestion);
                    item.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            jTextField1.setText(suggestion);
                            suggestionPopup.setVisible(false);
                        }
                    });
                    suggestionPopup.add(item);
                }
            }
            if (suggestionPopup.getComponentCount() > 0) {
                suggestionPopup.show(jTextField1, 0, jTextField1.getHeight());
                jTextField1.requestFocusInWindow(); // Ensure the text field retains focus
            } else {
                suggestionPopup.setVisible(false);
            }
        } else {
            suggestionPopup.setVisible(false);
        }
    }

    private void initTimeComboBox() {
        String[] defaultTimes = {"08:00 AM", "09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM", "05:00 PM"};

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        String currentTime = now.format(formatter);

        String[] updatedTimes = new String[defaultTimes.length + 1];
        updatedTimes[0] = currentTime;
        System.arraycopy(defaultTimes, 0, updatedTimes, 1, defaultTimes.length);

        jComboBox1 = new javax.swing.JComboBox<>(updatedTimes);
        jComboBox1.setSelectedItem(currentTime);
    }

    private void addTimeComboBoxListener() {
        jComboBox1.addActionListener(e -> {
            String selectedTime = (String) jComboBox1.getSelectedItem();
            String newTime = JOptionPane.showInputDialog("Customize Time", selectedTime);
            if (newTime != null && !newTime.trim().isEmpty()) {
                jComboBox1.addItem(newTime);
                jComboBox1.setSelectedItem(newTime);
            }
        });
    }

    private void addComponentsToScrollPane() {
        scrollPane1.add(jComboBox1);
    }

   private void addActionListeners() {
    addTaskButtonListeners();
    addDateButtonListeners();
    addClearAndEditListeners(toDoBtn1, clearBtn1, editBtn1);
    addClearAndEditListeners(toDoBtn2, clearBtn2, editBtn2);
    addClearAndEditListeners(toDoBtn3, clearBtn3, editBtn3);
    addClearAndEditListeners(toDoBtn4, clearBtn4, editBtn4);
    addClearAndEditListeners(toDoBtn5, clearBtn5, editBtn5);

    JButton[] buttons = {toDoBtn1, toDoBtn2, toDoBtn3, toDoBtn4, toDoBtn5};
    for (JButton button : buttons) {
        if (button != null) {
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        showContextMenu(e, button);
                    }
                }
            });
        }
    }

    // Add the tutorial action listener for jButton10
    if (jButton10 != null) {
        jButton10.addActionListener(e -> showTutorial());
    }
}

    private void addTaskButtonListeners() {
        if (jButton8 != null) {
            jButton8.addActionListener(e -> addTask());
        }

        if (jTextField1 != null) {
            jTextField1.addActionListener(e -> addTask());
        }

        if (jButton9 != null) {
            jButton9.addActionListener(e -> moveTaskToCompleted());
        }
    }

    private void addDateButtonListeners() {
        addDateButtonListener(jButton1, LocalDate.now());
        addDateButtonListener(jButton2, LocalDate.now().plusDays(1));
        addDateButtonListener(jButton3, LocalDate.now().plusDays(2));
        addDateButtonListener(jButton4, LocalDate.now().plusDays(3));
        addDateButtonListener(jButton5, LocalDate.now().plusDays(4));
        addDateButtonListener(jButton6, LocalDate.now().plusDays(5));
        addDateButtonListener(jButton7, LocalDate.now().plusDays(6));
    }

    private void addDateButtonListener(JButton button, LocalDate date) {
        if (button != null) {
            button.addActionListener(e -> {
                loadTasksForDate(date);
                if (currentSelectedButton != null) {
                    currentSelectedButton.setBackground(null); // Reset previous button color
                }
                button.setBackground(Color.GREEN); // Set current button color
                currentSelectedButton = button; // Update current selected button
            });
        }
    }

    private void addTask() {
        String text = jTextField1.getText().trim();
        if (!text.isEmpty()) {
            String selectedTime = (String) jComboBox1.getSelectedItem();
    
            LocalDate currentDate = this.currentDate;
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = currentDate.format(dateFormatter);
    
            String dateTime = formattedDate + " " + selectedTime;
            String taskDetails = "Task: " + text + "\nDate: " + formattedDate + "\nTime: " + selectedTime;
    
            String taskWithTime = "<html><div style='display: flex; justify-content: space-between; width: 100%;'><span style='text-align: left;'>" + text + "</span><span style='text-align: right; visibility: hidden;'>" + dateTime + "</span></div></html>";
    
            JButton[] buttons = {toDoBtn1, toDoBtn2, toDoBtn3, toDoBtn4, toDoBtn5};
            for (JButton button : buttons) {
                if (button.getText().isEmpty()) {
                    button.setText(taskWithTime);
                    taskDetailsMap.put(button, taskDetails);
    
                    saveTasksForDate(currentDate);
                    jTextField1.setText(""); // Clear the text field
    
                    // Call the method to play the sound
                    playTaskAddedSound();
                    break;
                }
            }
        }
    }

    private void moveTaskToCompleted() {
        JButton[] toDoButtons = {toDoBtn1, toDoBtn2, toDoBtn3, toDoBtn4, toDoBtn5};
    JButton[] completedButtons = {completedBtn1, completedBtn2, completedBtn3, completedBtn4, completedBtn5};

    for (JButton toDoButton : toDoButtons) {
        if (!toDoButton.getText().isEmpty()) {
            String text = toDoButton.getText();
            toDoButton.setText("");

            for (JButton completedButton : completedButtons) {
                if (completedButton.getText().isEmpty()) {
                    completedButton.setText(text);
                    saveTasksForDate(currentDate);

                    // Play task completed sound effect
                    playTaskCompletedSound();
                    // Show notification
                    JOptionPane.showMessageDialog(null, "You have completed the following task:\n" + text);

                    System.out.println("Notification shown for task: " + text); // Debug statement

                    return;
                    }
                }
            }
        }
    }

    private void playTaskAddedSound() {
        try {
            java.net.URL url = this.getClass().getClassLoader().getResource("soundEffect/taskAddedSound.wav");
            if (url == null) {
                System.err.println("Could not find the sound file.");
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void playTaskCompletedSound() {
        try {
            java.net.URL url = this.getClass().getClassLoader().getResource("soundEffect/taskCompleteSound.wav");
            if (url == null) {
                System.err.println("Could not find the sound file.");
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    private void addClearAndEditListeners(JButton toDoBtn, JButton clearBtn, JButton editBtn) {
        clearBtn.setVisible(false);
        editBtn.setVisible(false);

        toDoBtn.addPropertyChangeListener("text", evt -> {
            boolean hasText = !toDoBtn.getText().isEmpty();
            clearBtn.setVisible(hasText);
            editBtn.setVisible(hasText);
        });

        clearBtn.addActionListener(e -> {
            if (!toDoBtn.getText().isEmpty()) {
                toDoBtn.setText("");
                saveTasksForDate(currentDate);
                JOptionPane.showMessageDialog(null, "Task removed successfully");
            }
        });

        editBtn.addActionListener(e -> {
            if (!toDoBtn.getText().isEmpty()) {
                int response = JOptionPane.showConfirmDialog(null, "Do you want to edit the following task?", "Edit Task", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    String newText = JOptionPane.showInputDialog("Edit Task", toDoBtn.getText());
                    if (newText != null && !newText.trim().isEmpty()) {
                        toDoBtn.setText(newText);
                        saveTasksForDate(currentDate);
                    }
                }
            }
        });
    }

    private void showContextMenu(MouseEvent e, JButton button) {
        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem seeDetailsItem = new JMenuItem("See Task Details");
        seeDetailsItem.addActionListener(event -> showTaskDetails(button));
        contextMenu.add(seeDetailsItem);
        contextMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    private void showTaskDetails(JButton button) {
        String taskDetails = taskDetailsMap.get(button);
        if (taskDetails != null) {
            JOptionPane.showMessageDialog(null, taskDetails, "Task Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void displayCurrentDate() {
        currentDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM dd");

        if (jButton1 != null) jButton1.setText(currentDate.format(dateFormatter));
        if (jButton2 != null) jButton2.setText(currentDate.plusDays(1).format(dateFormatter));
        if (jButton3 != null) jButton3.setText(currentDate.plusDays(2).format(dateFormatter));
        if (jButton4 != null) jButton4.setText(currentDate.plusDays(3).format(dateFormatter));
        if (jButton5 != null) jButton5.setText(currentDate.plusDays(4).format(dateFormatter));
        if (jButton6 != null) jButton6.setText(currentDate.plusDays(5).format(dateFormatter));
        if (jButton7 != null) jButton7.setText(currentDate.plusDays(6).format(dateFormatter));

        // Initially set jButton1 to green
        jButton1.setBackground(Color.CYAN);
        currentSelectedButton = jButton1;
    }

    private void loadTasksForDate(LocalDate date) {
        currentDate = date;
        String[] toDoTasks = toDoTasksMap.getOrDefault(date, new String[5]);
        String[] completedTasks = completedTasksMap.getOrDefault(date, new String[5]);

        JButton[] toDoButtons = {toDoBtn1, toDoBtn2, toDoBtn3, toDoBtn4, toDoBtn5};
        JButton[] completedButtons = {completedBtn1, completedBtn2, completedBtn3, completedBtn4, completedBtn5};

        for (int i = 0; i < toDoButtons.length; i++) {
            if (toDoTasks[i] != null) {
                String taskWithTime = "<html><div style='display: flex; justify-content: space-between; width: 100%;'><span style='text-align: left;'>" + toDoTasks[i] + "</span><span style='text-align: right; visibility: hidden;'></span></div></html>";
                toDoButtons[i].setText(taskWithTime);
            } else {
                toDoButtons[i].setText("");
            }
        }

        for (int i = 0; i < completedButtons.length; i++) {
            if (completedTasks[i] != null) {
                completedButtons[i].setText(completedTasks[i]);
            } else {
                completedButtons[i].setText("");
            }
        }
    }

    private void saveTasksForDate(LocalDate date) {
        JButton[] toDoButtons = {toDoBtn1, toDoBtn2, toDoBtn3, toDoBtn4, toDoBtn5};
        JButton[] completedButtons = {completedBtn1, completedBtn2, completedBtn3, completedBtn4, completedBtn5};

        String[] toDoTasks = new String[5];
        String[] completedTasks = new String[5];

        for (int i = 0; i < toDoButtons.length; i++) {
            toDoTasks[i] = toDoButtons[i].getText();
        }

        for (int i = 0; i < completedButtons.length; i++) {
            completedTasks[i] = completedButtons[i].getText();
        }

        toDoTasksMap.put(date, toDoTasks);
        completedTasksMap.put(date, completedTasks);
    }
    


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jScrollPane1 = new javax.swing.JScrollPane();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        completedBtn1 = new javax.swing.JButton();
        editBtn1 = new javax.swing.JButton();
        clearBtn1 = new javax.swing.JButton();
        clearBtn2 = new javax.swing.JButton();
        editBtn2 = new javax.swing.JButton();
        clearBtn3 = new javax.swing.JButton();
        editBtn3 = new javax.swing.JButton();
        editBtn4 = new javax.swing.JButton();
        clearBtn4 = new javax.swing.JButton();
        editBtn5 = new javax.swing.JButton();
        clearBtn5 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        scrollPane1 = new java.awt.ScrollPane();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        toDoBtn1 = new javax.swing.JButton();
        toDoBtn3 = new javax.swing.JButton();
        toDoBtn4 = new javax.swing.JButton();
        toDoBtn5 = new javax.swing.JButton();
        completedBtn2 = new javax.swing.JButton();
        completedBtn3 = new javax.swing.JButton();
        completedBtn4 = new javax.swing.JButton();
        completedBtn5 = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0));
        jButton10 = new javax.swing.JButton();
        toDoBtn2 = new javax.swing.JButton();

        jMenu1.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setPreferredSize(new java.awt.Dimension(880, 632));
        setResizable(false);

        jButton1.setBackground(new java.awt.Color(153, 255, 204));
        jButton1.setText("jButton1");

        jButton2.setBackground(new java.awt.Color(153, 255, 204));
        jButton2.setText("jButton2");

        jButton3.setBackground(new java.awt.Color(153, 255, 204));
        jButton3.setText("jButton3");

        jButton4.setBackground(new java.awt.Color(153, 255, 204));
        jButton4.setText("jButton4");

        jButton5.setBackground(new java.awt.Color(153, 255, 204));
        jButton5.setText("jButton5");

        jButton6.setBackground(new java.awt.Color(153, 255, 204));
        jButton6.setText("jButton6");

        jButton7.setBackground(new java.awt.Color(153, 255, 204));
        jButton7.setText("jButton7");

        jLabel1.setFont(new java.awt.Font("Gujarati MT", 3, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setText("Task:");

        jTextField1.setFont(new java.awt.Font("Kristen ITC", 0, 12)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(102, 102, 102));
        jTextField1.setText("Type your task here..."); // NOI18N
        jTextField1.setToolTipText("");

        jLabel2.setFont(new java.awt.Font("Gujarati MT", 3, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setText("Time:");

        jButton8.setBackground(new java.awt.Color(51, 102, 255));
        jButton8.setFont(new java.awt.Font("Kristen ITC", 0, 14)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("Add Task");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(102, 102, 102));
        jButton9.setFont(new java.awt.Font("Kristen ITC", 0, 14)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("Mark Completed");

        jLabel3.setFont(new java.awt.Font("Mshtakan", 3, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 102));
        jLabel3.setText("To-Do List");

        jLabel4.setFont(new java.awt.Font("Mshtakan", 3, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 102, 153));
        jLabel4.setText("Completed");

        completedBtn1.setBackground(java.awt.SystemColor.activeCaption);

        editBtn1.setBackground(new java.awt.Color(0, 153, 255));
        editBtn1.setForeground(new java.awt.Color(255, 255, 255));
        editBtn1.setText("Edit");
        editBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBtn1ActionPerformed(evt);
            }
        });

        clearBtn1.setBackground(new java.awt.Color(255, 0, 0));
        clearBtn1.setForeground(new java.awt.Color(255, 255, 255));
        clearBtn1.setText("✕");
        clearBtn1.setToolTipText("");
        clearBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBtn1ActionPerformed(evt);
            }
        });

        clearBtn2.setBackground(new java.awt.Color(255, 0, 0));
        clearBtn2.setForeground(new java.awt.Color(255, 255, 255));
        clearBtn2.setText("✕");
        clearBtn2.setToolTipText("");
        clearBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBtn2ActionPerformed(evt);
            }
        });

        editBtn2.setBackground(new java.awt.Color(0, 153, 255));
        editBtn2.setForeground(new java.awt.Color(255, 255, 255));
        editBtn2.setText("Edit");

        clearBtn3.setBackground(new java.awt.Color(255, 0, 0));
        clearBtn3.setForeground(new java.awt.Color(255, 255, 255));
        clearBtn3.setText("✕");
        clearBtn3.setToolTipText("");
        clearBtn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBtn3ActionPerformed(evt);
            }
        });

        editBtn3.setBackground(new java.awt.Color(0, 153, 255));
        editBtn3.setForeground(new java.awt.Color(255, 255, 255));
        editBtn3.setText("Edit");
        editBtn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBtn3ActionPerformed(evt);
            }
        });

        editBtn4.setBackground(new java.awt.Color(0, 153, 255));
        editBtn4.setForeground(new java.awt.Color(255, 255, 255));
        editBtn4.setText("Edit");

        clearBtn4.setBackground(new java.awt.Color(255, 0, 0));
        clearBtn4.setForeground(new java.awt.Color(255, 255, 255));
        clearBtn4.setText("✕");
        clearBtn4.setToolTipText("");
        clearBtn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBtn4ActionPerformed(evt);
            }
        });

        editBtn5.setBackground(new java.awt.Color(0, 153, 255));
        editBtn5.setForeground(new java.awt.Color(255, 255, 255));
        editBtn5.setText("Edit");

        clearBtn5.setBackground(new java.awt.Color(255, 0, 0));
        clearBtn5.setForeground(new java.awt.Color(255, 255, 255));
        clearBtn5.setText("✕");
        clearBtn5.setToolTipText("");
        clearBtn5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBtn5ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        scrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        scrollPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        scrollPane1.setFont(new java.awt.Font("Kristen ITC", 0, 12)); // NOI18N
        scrollPane1.setForeground(new java.awt.Color(255, 255, 102));

        jLabel5.setText("SunRise:");

        jLabel6.setText("SunSet :");

        jLabel7.setText("See all completed Task");

        toDoBtn1.setBackground(new java.awt.Color(0, 153, 153));
        toDoBtn1.setForeground(new java.awt.Color(255, 255, 255));
        toDoBtn1.setAutoscrolls(true);
        toDoBtn1.setBorder(null);
        toDoBtn1.setBorderPainted(false);
        toDoBtn1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        toDoBtn1.setMaximumSize(new java.awt.Dimension(200, 25));
        toDoBtn1.setMinimumSize(new java.awt.Dimension(200, 25));
        toDoBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toDoBtn1ActionPerformed(evt);
            }
        });

        toDoBtn3.setBackground(new java.awt.Color(0, 153, 153));
        toDoBtn3.setForeground(new java.awt.Color(255, 255, 255));
        toDoBtn3.setBorder(null);
        toDoBtn3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        toDoBtn3.setMaximumSize(new java.awt.Dimension(200, 25));
        toDoBtn3.setMinimumSize(new java.awt.Dimension(200, 25));
        toDoBtn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toDoBtn3ActionPerformed(evt);
            }
        });

        toDoBtn4.setBackground(new java.awt.Color(0, 153, 153));
        toDoBtn4.setForeground(new java.awt.Color(255, 255, 255));
        toDoBtn4.setBorder(null);
        toDoBtn4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        toDoBtn4.setMaximumSize(new java.awt.Dimension(200, 25));
        toDoBtn4.setMinimumSize(new java.awt.Dimension(200, 25));
        toDoBtn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toDoBtn4ActionPerformed(evt);
            }
        });

        toDoBtn5.setBackground(new java.awt.Color(0, 153, 153));
        toDoBtn5.setForeground(new java.awt.Color(255, 255, 255));
        toDoBtn5.setBorder(null);
        toDoBtn5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        toDoBtn5.setMaximumSize(new java.awt.Dimension(200, 25));
        toDoBtn5.setMinimumSize(new java.awt.Dimension(200, 25));
        toDoBtn5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toDoBtn5ActionPerformed(evt);
            }
        });

        completedBtn2.setBackground(java.awt.SystemColor.activeCaption);

        completedBtn3.setBackground(java.awt.SystemColor.activeCaption);
        completedBtn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                completedBtn3ActionPerformed(evt);
            }
        });

        completedBtn4.setBackground(java.awt.SystemColor.activeCaption);

        completedBtn5.setBackground(java.awt.SystemColor.activeCaption);

        jButton10.setText("Need Help?");

        toDoBtn2.setBackground(new java.awt.Color(0, 153, 153));
        toDoBtn2.setForeground(new java.awt.Color(255, 255, 255));
        toDoBtn2.setBorder(null);
        toDoBtn2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        toDoBtn2.setMaximumSize(new java.awt.Dimension(200, 25));
        toDoBtn2.setMinimumSize(new java.awt.Dimension(200, 25));
        toDoBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toDoBtn2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6)
                        .addGap(18, 18, 18)
                        .addComponent(jButton7)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel7)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel5)
                                        .addGap(64, 64, 64)
                                        .addComponent(jLabel6))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(toDoBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(toDoBtn4, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(toDoBtn3, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(toDoBtn5, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(toDoBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(clearBtn3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(editBtn3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(clearBtn2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(editBtn2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(editBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(clearBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(editBtn4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(clearBtn5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(clearBtn4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                    .addComponent(editBtn5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(30, 30, 30))))
                                        .addGap(30, 30, 30)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(completedBtn5, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(completedBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(completedBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(completedBtn3, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(completedBtn4, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(8, 8, 8)))
                                .addGap(96, 96, 96))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(231, 231, 231)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))))
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(scrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 191, Short.MAX_VALUE)
                        .addComponent(jButton8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(127, 127, 127))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jButton6)
                    .addComponent(jButton7))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton8)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 179, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(clearBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(editBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(completedBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(clearBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(editBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(clearBtn3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(editBtn3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(clearBtn4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(editBtn4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(clearBtn5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(editBtn5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(5, 5, 5)
                                        .addComponent(completedBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(completedBtn3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(completedBtn4, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(completedBtn5, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(toDoBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(toDoBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(toDoBtn3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(toDoBtn4, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(toDoBtn5, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 91, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6)
                    .addComponent(jButton10)
                    .addComponent(jLabel5))
                .addGap(20, 20, 20))
        );

        jLabel3.getAccessibleContext().setAccessibleName("To - Do List");
        jLabel6.getAccessibleContext().setAccessibleName("SunSet:");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void clearBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBtn1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clearBtn1ActionPerformed

    private void clearBtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBtn2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clearBtn2ActionPerformed

    private void clearBtn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBtn3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clearBtn3ActionPerformed

    private void clearBtn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBtn4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clearBtn4ActionPerformed

    private void clearBtn5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBtn5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clearBtn5ActionPerformed

    private void editBtn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtn3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editBtn3ActionPerformed

    private void editBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtn1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editBtn1ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void toDoBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toDoBtn1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_toDoBtn1ActionPerformed

    private void toDoBtn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toDoBtn3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_toDoBtn3ActionPerformed

    private void toDoBtn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toDoBtn4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_toDoBtn4ActionPerformed

    private void toDoBtn5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toDoBtn5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_toDoBtn5ActionPerformed

    private void completedBtn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_completedBtn3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_completedBtn3ActionPerformed

    private void toDoBtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toDoBtn2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_toDoBtn2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TaskM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TaskM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TaskM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TaskM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new TaskM().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clearBtn1;
    private javax.swing.JButton clearBtn2;
    private javax.swing.JButton clearBtn3;
    private javax.swing.JButton clearBtn4;
    private javax.swing.JButton clearBtn5;
    private javax.swing.JButton completedBtn1;
    private javax.swing.JButton completedBtn2;
    private javax.swing.JButton completedBtn3;
    private javax.swing.JButton completedBtn4;
    private javax.swing.JButton completedBtn5;
    private javax.swing.JButton editBtn1;
    private javax.swing.JButton editBtn2;
    private javax.swing.JButton editBtn3;
    private javax.swing.JButton editBtn4;
    private javax.swing.JButton editBtn5;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private java.awt.ScrollPane scrollPane1;
    private javax.swing.JButton toDoBtn1;
    private javax.swing.JButton toDoBtn2;
    private javax.swing.JButton toDoBtn3;
    private javax.swing.JButton toDoBtn4;
    private javax.swing.JButton toDoBtn5;
    // End of variables declaration//GEN-END:variables

    private void clearAllTasks() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
