package org.example.uap_proglan;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;

public class UIManager {
    private final JFrame frame;
    private User currentUser;
    private QuizManager quizManager;
    private JLabel timerLabel;
    private Timer timer;
    private int timeRemaining = 300; // 5 minutes
    private JTextArea questionArea;
    private JPanel answerPanel;
    private JLabel questionImageLabel; // Component for question image

    public UIManager() {
        frame = new JFrame("Kuis UTBK");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null); // Center the frame
        quizManager = new QuizManager();
        frame.getContentPane().setBackground(Color.WHITE); // White background
        frame.setVisible(true);
    }

    public void showMainMenu() {
        JPanel mainMenu = new JPanel();
        mainMenu.setLayout(new BoxLayout(mainMenu, BoxLayout.Y_AXIS));
        mainMenu.setBackground(new Color(224, 247, 251)); // Light blue background

        JLabel titleLabel = new JLabel("Selamat Datang di Kuis UTBK");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(0, 102, 204)); // Dark blue color

        JButton newUserButton = createStyledButton("Pengguna Baru");
        newUserButton.addActionListener(e -> showIdentityInput());

        mainMenu.add(Box.createVerticalStrut(50));
        mainMenu.add(titleLabel);
        mainMenu.add(Box.createVerticalStrut(20));
        mainMenu.add(newUserButton);
        frame.setContentPane(mainMenu);
        frame.revalidate();
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(new Color(0, 153, 51)); // Green background
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Hand cursor on hover
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(200, 40));
        return button;
    }

    private void showIdentityInput() {
        JPanel identityPane = new JPanel();
        identityPane.setLayout(new BoxLayout(identityPane, BoxLayout.Y_AXIS));
        identityPane.setBackground(new Color(224, 247, 251)); // Light blue background

        JLabel identityLabel = new JLabel("Identitas Pengguna");
        identityLabel.setFont(new Font("Arial", Font.BOLD, 20));
        identityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        identityLabel.setForeground(new Color(0, 102, 204)); // Dark blue color

        JTextField nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(200, 30));
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));
        nameField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));

        JButton startButton = createStyledButton("Mulai Kuis");
        startButton.addActionListener(e -> {
            String userName = nameField.getText();
            if (userName.isEmpty()) {
                showAlert("Nama tidak boleh kosong.");
            } else {
                currentUser = new User(userName);
                showSubtestSelection();
            }
        });

        identityPane.add(identityLabel);
        identityPane.add(Box.createVerticalStrut(20));
        identityPane.add(nameField);
        identityPane.add(Box.createVerticalStrut(20));
        identityPane.add(startButton);
        frame.setContentPane(identityPane);
        frame.revalidate();
    }

    private void showSubtestSelection() {
        JPanel subtestPane = new JPanel();
        subtestPane.setLayout(new BoxLayout(subtestPane, BoxLayout.Y_AXIS));
        subtestPane.setBackground(new Color(224, 247, 251)); // Light blue background

        JLabel subtestLabel = new JLabel("Pilih Subtest:");
        subtestLabel.setFont(new Font("Arial", Font.BOLD, 20));
        subtestLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtestLabel.setForeground(new Color(0, 102, 204)); // Dark blue color

        String[] subtests = {
                "Penalaran Umum",
                "Penalaran Matematika",
                "Pengetahuan Kuantitatif",
                "Literasi Inggris",
                "Literasi Indonesia",
                "Pemahaman Bacaan dan Menulis",
                "Pengetahuan Umum"
        };

        ButtonGroup subtestGroup = new ButtonGroup();
        for (String subtest : subtests) {
            JRadioButton radioButton = new JRadioButton(subtest);
            radioButton.setFont(new Font("Arial", Font.PLAIN, 16));
            radioButton.setBackground(new Color(224, 247, 251)); // Light blue background
            radioButton.setForeground(Color.BLACK);
            subtestGroup.add(radioButton);
            subtestPane.add(radioButton);
        }

        JButton confirmButton = createStyledButton("Konfirmasi");
        confirmButton.addActionListener(e -> {
            Enumeration<AbstractButton> buttons = subtestGroup.getElements();
            while (buttons.hasMoreElements()) {
                JRadioButton button = (JRadioButton) buttons.nextElement();
                if (button.isSelected()) {
                    quizManager.loadQuestions(button.getText());
                    frame.setContentPane(createQuizPanel());
                    frame.revalidate();
                    break;
                }
            }
        });

        subtestPane.add(subtestLabel);
        subtestPane.add(Box.createVerticalStrut(20));
        subtestPane.add(confirmButton);
        frame.setContentPane(subtestPane);
        frame.revalidate();
    }

    private JPanel createQuizPanel() {
        JPanel quizPanel = new JPanel();
        quizPanel.setLayout(new BorderLayout());
        quizPanel.setBackground(new Color(224, 247, 251)); // Light blue background

        timerLabel = new JLabel("Waktu Tersisa: 05:00");
        timerLabel.setForeground(new Color(211, 47, 47)); // Red color for timer
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        quizPanel.add(timerLabel, BorderLayout.NORTH);

        questionArea = new JTextArea();
        questionArea.setEditable(false);
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        questionArea.setFont(new Font("Arial", Font.PLAIN, 16));
        questionArea.setBackground(Color.WHITE);
        quizPanel.add(new JScrollPane(questionArea), BorderLayout.CENTER);

        answerPanel = new JPanel();
        answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.Y_AXIS));
        quizPanel.add(answerPanel, BorderLayout.SOUTH);

        JButton nextButton = createStyledButton("Berikutnya");
        nextButton.addActionListener(e -> handleNextButtonAction());
        quizPanel.add(nextButton, BorderLayout.EAST);

        questionImageLabel = new JLabel();
        questionImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        quizPanel.add(questionImageLabel, BorderLayout.WEST); // Add image label to the left

        updateQuestion();
        startTimer();

        return quizPanel;
    }

    private void startTimer() {
        timer = new Timer(1000, e -> {
            timeRemaining--;
            int minutes = timeRemaining / 60;
            int seconds = timeRemaining % 60;
            timerLabel.setText(String.format("Waktu Tersisa: %02d:%02d", minutes, seconds));

            if (timeRemaining <= 0) {
                timer.stop();
                showFinalScore();
            }
        });
        timer.start();
    }

    private void handleNextButtonAction() {
        // Get the selected answer
        for (Component component : answerPanel.getComponents()) {
            if (component instanceof JRadioButton radioButton) {
                if (radioButton.isSelected()) {
                    String answer = radioButton.getText();
                    Question currentQuestion = quizManager.getCurrentQuestion();
                    boolean isCorrect = answer.equals(currentQuestion.getCorrectAnswer());

                    // Update score and feedback
                    if (isCorrect) {
                        quizManager.incrementScore();
                    }

                    // Track the answer and explanation
                    String result = isCorrect ? "Benar" : "Salah";
                    trackAnswer(currentQuestion.getText(), currentQuestion.getImagePath(), answer, result, currentQuestion.getExplanation());

                    quizManager.nextQuestion();
                    if (!quizManager.isQuizComplete()) {
                        updateQuestion();
                    } else {
                        currentUser.getScoresBySubtest().put("Subtest", quizManager.getScore());
                        currentUser.addResult("Skor: " + quizManager.getScore());
                        showFinalScore();
                    }
                    return;
                }
            }
        }
        showAlert("Pilih jawaban sebelum melanjutkan.");
    }

    private void updateQuestion() {
        Question question = quizManager.getCurrentQuestion();

        // Update question text
        questionArea.setText(question.getText());

        // Update question image
        if (question.getImagePath() != null && !question.getImagePath().isEmpty()) {
            ImageIcon questionImage = new ImageIcon(question.getImagePath());
            questionImageLabel.setIcon(questionImage);
        } else {
            questionImageLabel.setIcon(null); // Clear image if none
        }

        // Update answer options
        answerPanel.removeAll();
        ButtonGroup answerGroup = new ButtonGroup();
        for (String option : question.getOptions()) {
            JRadioButton answerButton = new JRadioButton(option);
            answerButton.setFont(new Font("Arial", Font.PLAIN, 16));
            answerButton.setBackground(new Color(224, 247, 251)); // Light blue background
            answerButton.setForeground(Color.BLACK);
            answerGroup.add(answerButton);
            answerPanel.add(answerButton);
        }

        // Refresh the display
        answerPanel.revalidate();
        answerPanel.repaint();
    }

    private void showFinalScore() {
        timer.stop();
        JOptionPane.showMessageDialog(frame, "Skor Anda: " + quizManager.getScore(), "Hasil Akhir", JOptionPane.INFORMATION_MESSAGE);
        showCompletionMenu();
    }

    private void showCompletionMenu() {
        int response = JOptionPane.showConfirmDialog(frame, "Anda telah menyelesaikan subtest. Apakah Anda ingin memilih subtest lain?", "Selesai", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            resetQuiz();
            showSubtestSelection();
        } else {
            FileManager fileManager = new FileManager();
            fileManager.generateExplanationFile(currentUser, currentUser.getAnswerHistoryBySubtest(), currentUser.getScoresBySubtest());
            frame.dispose(); // Exit the application
        }
    }

    private void resetQuiz() {
        quizManager = new QuizManager(); // Reset quiz manager
        timeRemaining = 300; // Reset timer to 5 minutes
    }

    private void showAlert(String message) {
        JOptionPane.showMessageDialog(frame, message, "Peringatan", JOptionPane.WARNING_MESSAGE);
    }

    private void trackAnswer(String questionText, String image, String selectedAnswer, String result, String explanation) {
        String answerRecord = String.format("Pertanyaan: %s\nJawaban Anda: %s\nStatus: %s\nPenjelasan: %s\n\n",
                questionText, selectedAnswer, result, explanation);

        // Save results by subtest
        currentUser.getAnswerHistoryBySubtest().computeIfAbsent("Subtest", k -> new ArrayList<>()).add(answerRecord);

        // If there is an image, add the image path
        if (image != null && !image.isEmpty()) {
            currentUser.getAnswerHistoryBySubtest().get("Subtest").add("Gambar: " + image);
        }
    }
}