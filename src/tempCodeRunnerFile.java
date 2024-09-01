public TaskM() {
        initComponents();
        displayCurrentDate();
        addActionListeners();
    }
    
    private void addActionListeners() {
        jButton8.addActionListener(e -> addTask());
    
        jTextField1.addActionListener(e -> addTask());
    
        jButton9.addActionListener(e -> {
            JButton[] toDoButtons = {toDoBtn1, toDoBtn2, toDoBtn3, toDoBtn4, toDoBtn5};
            JButton[] completedButtons = {completedBtn1, completedBtn2, completedBtn3, completedBtn4, completedBtn5};
    
            for (JButton toDoButton : toDoButtons) {
                if (!toDoButton.getText().isEmpty()) {
                    String text = toDoButton.getText();
                    toDoButton.setText("");
    
                    for (JButton completedButton : completedButtons) {
                        if (completedButton.getText().isEmpty()) {
                            completedButton.setText(text);
                            return;
                        }
                    }
                }
            }
        });
    
        addClearAndEditListeners(toDoBtn1, clearBtn1, editBtn1);
        addClearAndEditListeners(toDoBtn2, clearBtn2, editBtn2);
        addClearAndEditListeners(toDoBtn3, clearBtn3, editBtn3);
        addClearAndEditListeners(toDoBtn4, clearBtn4, editBtn4);
        addClearAndEditListeners(toDoBtn5, clearBtn5, editBtn5);
    }
    
    private void addTask() {
        String text = jTextField1.getText().trim();
        if (!text.isEmpty()) {
            JButton[] buttons = {toDoBtn1, toDoBtn2, toDoBtn3, toDoBtn4, toDoBtn5};
            for (JButton button : buttons) {
                if (button.getText().isEmpty()) {
                    button.setText(text);
                    jTextField1.setText(""); // Clear the text field
                    break;
                }
            }
        }
    }
    
    private void addClearAndEditListeners(JButton toDoBtn, JButton clearBtn, JButton editBtn) {
        clearBtn.setVisible(false); // Initially set clear button to invisible
        editBtn.setVisible(false);  // Initially set edit button to invisible
    
        toDoBtn.addPropertyChangeListener("text", evt -> {
            boolean hasText = !toDoBtn.getText().isEmpty();
            clearBtn.setVisible(hasText);
            editBtn.setVisible(hasText);
        });
    
        clearBtn.addActionListener(e -> {
            if (!toDoBtn.getText().isEmpty()) {
                toDoBtn.setText("");
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
                    }
                }
            }
        });
    }