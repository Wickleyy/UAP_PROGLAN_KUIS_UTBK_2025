package org.example.uap_proglan;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.*;
import java.util.*;

public class HelloApplication extends Application {

    private int currentQuestionIndex = 0;
    private int score = 0;
    private List<Question> questions;
    private Label timerLabel;
    private Timer timer;
    private int timeRemaining = 300; // 5 minutes
    private VBox questionPane;
    private List<String> answerHistory = new ArrayList<>(); // Track answers and explanations
    private String userName;
    private String selectedSubtest;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Kuis UTBK");

        // Input Identitas Pengguna
        VBox identityPane = new VBox(10);
        identityPane.setPadding(new Insets(10));
        TextField nameField = new TextField();
        nameField.setPromptText("Masukkan Nama Anda");
        Button startButton = new Button("Mulai Kuis");
        identityPane.getChildren().addAll(new Label("Identitas Pengguna"), nameField, startButton);

        // Action untuk tombol mulai
        startButton.setOnAction(e -> {
            userName = nameField.getText();
            if (userName.isEmpty()) {
                showAlert("Peringatan", "Nama tidak boleh kosong.");
            } else {
                showSubtestSelection(primaryStage);
            }
        });

        // Show Identity Scene
        Scene identityScene = new Scene(identityPane, 400, 200);
        primaryStage.setScene(identityScene);
        primaryStage.show();
    }

    private void showSubtestSelection(Stage primaryStage) {
        // Subtest selection layout
        VBox subtestPane = new VBox(10);
        subtestPane.setPadding(new Insets(10));
        Label subtestLabel = new Label("Pilih Subtest:");
        ToggleGroup subtestGroup = new ToggleGroup();

        // List of subtests
        String[] subtests = {
                "Penalaran Umum",
                "Penalaran Matematika",
                "Pengetahuan Kuantitatif",
                "Literasi Inggris",
                "Literasi Indonesia",
                "Pemahaman Bacaan dan Menulis",
                "Pengetahuan Umum"
        };

        for (String subtest : subtests) {
            RadioButton radioButton = new RadioButton(subtest);
            radioButton.setToggleGroup(subtestGroup);
            subtestPane.getChildren().add(radioButton);
        }

        Button confirmButton = new Button("Konfirmasi");
        subtestPane.getChildren().add(confirmButton);

        // Action for confirm button
        confirmButton.setOnAction(e -> {
            RadioButton selectedRadioButton = (RadioButton) subtestGroup.getSelectedToggle();
            if (selectedRadioButton != null) {
                selectedSubtest = selectedRadioButton.getText();
                questions = loadQuestions(selectedSubtest);
                primaryStage.setScene(createQuizScene(primaryStage));
            } else {
                showAlert("Peringatan", "Pilih subtest sebelum melanjutkan.");
            }
        });

        // Show Subtest Selection Scene
        Scene subtestScene = new Scene(subtestPane, 400, 300);
        primaryStage.setScene(subtestScene);
    }

    private Scene createQuizScene(Stage primaryStage) {
        // Root layout
        BorderPane root = new BorderPane();

        // Timer and Score
        timerLabel = new Label("Waktu Tersisa: 05:00");
        Label scoreLabel = new Label("Skor: 0");
        HBox topBar = new HBox(20, timerLabel, scoreLabel);
        topBar.setPadding(new Insets(10));
        root.setTop(topBar);

        // Center layout (for question and answers)
        questionPane = new VBox(10);
        questionPane.setPadding(new Insets(10));
        root.setCenter(questionPane);

        // Next Button Action
        Button nextButton = new Button("Berikutnya");
        nextButton.setOnAction(e -> handleNextButtonAction(scoreLabel, primaryStage));
        root.setBottom(nextButton);

        // Show First Question
        updateQuestion();

        // Timer
        startTimer(primaryStage);

        // Show Scene
        return new Scene(root, 600, 400);
    }

    private void startTimer(Stage primaryStage) {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Memperbarui waktu di thread utama JavaFX
                Platform.runLater(() -> {
                    timeRemaining--;
                    int minutes = timeRemaining / 60;
                    int seconds = timeRemaining % 60;
                    String time = String.format("%02d:%02d", minutes, seconds);
                    if (timerLabel != null) {
                        timerLabel.setText("Waktu Tersisa: " + time);
                    }

                    // Jika waktu habis
                    if (timeRemaining <= 0) {
                        timer.cancel();
                        showFinalScore(primaryStage);
                    }
                });
            }
        }, 1000, 1000);
    }

    private void handleNextButtonAction(Label scoreLabel, Stage primaryStage) {
        // Get the VBox containing the answers
        VBox answersBox = (VBox) questionPane.getChildren().get(2);

        // Get the selected RadioButton from the ToggleGroup
        RadioButton selectedAnswer = (RadioButton) answersBox.getChildren().stream()
                .filter(node -> node instanceof RadioButton)
                .map(node -> (RadioButton) node)
                .filter(RadioButton::isSelected)
                .findFirst()
                .orElse(null); // If no answer is selected, it will be null

        // Check if an answer was selected
        if (selectedAnswer != null) {
            String answer = selectedAnswer.getText();
            Question currentQuestion = questions.get(currentQuestionIndex);
            if (answer.equals(currentQuestion.getCorrectAnswer())) {
                score += 10;
                scoreLabel.setText("Skor: " + score);
            }

            // Track the answer and explanation
            String result = answer.equals(currentQuestion.getCorrectAnswer()) ? "Benar" : "Salah";
            String explanation = currentQuestion.getExplanation();
            trackAnswer(currentQuestion.getText(), currentQuestion.getImagePath(), answer, result, explanation);

            currentQuestionIndex++;
            if (currentQuestionIndex < questions.size()) {
                updateQuestion();
            } else {
                showFinalScore(primaryStage);
            }
        } else {
            showAlert("Peringatan", "Pilih jawaban sebelum melanjutkan.");
        }
    }

    private void trackAnswer(String questionText,String image, String selectedAnswer, String result, String explanation) {
        String answerRecord = String.format("Pertanyaan: %s\n%s\nJawaban Anda: %s\nStatus: %s\nPenjelasan: %s\n\n",
                questionText, image, selectedAnswer, result, explanation);
        answerHistory.add(answerRecord);
    }

    private void updateQuestion() {
        Question question = questions.get(currentQuestionIndex);

        // Clear previous question and answers
        questionPane.getChildren().clear();

        // Add question text
        Label questionLabel = new Label(question.getText());
        questionPane.getChildren().add(questionLabel);

        // Add image if available
        ImageView imageView = new ImageView();
        if (question.getImagePath() != null) {
            imageView.setImage(new Image(new File(question.getImagePath()).toURI().toString()));
            imageView.setFitHeight(200);
            imageView.setPreserveRatio(true);
        }
        questionPane.getChildren().add(imageView);

        // Add answer options
        ToggleGroup answersGroup = new ToggleGroup();
        VBox answersBox = new VBox(5);
        for (String option : question.getOptions()) {
            RadioButton answerButton = new RadioButton(option);
            answerButton.setToggleGroup(answersGroup);
            answersBox.getChildren().add(answerButton);
        }
        questionPane.getChildren().add(answersBox);
    }

    private void showFinalScore(Stage primaryStage) {
        timer.cancel();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hasil Akhir");
        alert.setHeaderText(null);
        alert.setContentText("Skor Anda: " + score);
        alert.showAndWait();

        // Tampilkan opsi untuk menyelesaikan atau memilih subtest lain
        showCompletionMenu(primaryStage);
    }

    private void showCompletionMenu(Stage primaryStage) {
        Alert completionAlert = new Alert(Alert.AlertType.CONFIRMATION);
        completionAlert.setTitle("Selesai");
        completionAlert.setHeaderText("Anda telah menyelesaikan subtest.");
        completionAlert.setContentText("Apakah Anda ingin memilih subtest lain?");

        ButtonType yesButton = new ButtonType("Ya");
        ButtonType noButton = new ButtonType("Tidak");
        completionAlert.getButtonTypes().setAll(yesButton, noButton);

        completionAlert.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                resetQuiz();
                showSubtestSelection(primaryStage);
            } else {
                generateExplanationFile(); // Generate the explanation file before exiting
                primaryStage.close(); // Exit the application
            }
        });
    }

    private void resetQuiz() {
        currentQuestionIndex = 0;
        score = 0;
        answerHistory.clear();
        timeRemaining = 300; // Reset timer to 5 minutes
    }

    private void generateExplanationFile() {
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFParagraph title = document.createParagraph();
            XWPFRun titleRun = title.createRun();
            titleRun.setBold(true);
            titleRun.setFontSize(16);
            titleRun.setText("Hasil Kuis dan Pembahasan");

            // Tambahkan nama pengguna
            XWPFParagraph userNameParagraph = document.createParagraph();
            XWPFRun userNameRun = userNameParagraph.createRun();
            userNameRun.setText("Nama: " + userName);
            userNameParagraph.createRun().addBreak(); // Tambahkan baris baru

            // Tambahkan subtest yang dikerjakan
            XWPFParagraph subtestParagraph = document.createParagraph();
            XWPFRun subtestRun = subtestParagraph.createRun();
            subtestRun.setText("Subtest: " + selectedSubtest);
            subtestParagraph.createRun().addBreak(); // Tambahkan baris baru

            // Tambahkan spasi
            document.createParagraph();

            for (String record : answerHistory) {
                XWPFParagraph paragraph = document.createParagraph();
                XWPFRun run = paragraph.createRun();
                run.setText(record);
            }

            // Tambahkan skor akhir
            XWPFParagraph finalScoreParagraph = document.createParagraph();
            XWPFRun finalScoreRun = finalScoreParagraph.createRun();
            finalScoreRun.setText("Skor Akhir Anda: " + score);

            // Simpan dokumen
            try (FileOutputStream out = new FileOutputStream("Pembahasan_Kuis.docx")) {
                document.write(out);
                showAlert("Pembahasan", "File pembahasan DOCX telah dibuat! Cek Pembahasan_Kuis.docx.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal membuat file pembahasan DOCX.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private List<Question> loadQuestions(String subtest) {
        List<Question> questions = new ArrayList<>();

        // Load questions based on selected subtest
        switch (subtest) {
            case "Penalaran Umum":
                questions.add(new Question("Apa hasil dari 2 + 2?",
                        Arrays.asList("3", "4", "5", "6"), "4", null, "Penjumlahan 2 + 2 menghasilkan 4."));
                questions.add(new Question("Siapa presiden pertama Indonesia?",
                        Arrays.asList("Soekarno", "Suharto", "Habibie", "Megawati"), "Soekarno", null,
                        "Presiden pertama Indonesia adalah Soekarno."));
                questions.add(new Question("Manakah gambar segitiga?",
                        Arrays.asList("A", "B", "C", "D"), "B", "src\\main\\resources\\images\\triangle.png",
                        "Gambar B adalah gambar segitiga."));
                questions.add(new Question("Apa hasil dari 10 - 4?",
                        Arrays.asList("5", "6", "7", "8"), "6", null, "10 - 4 = 6."));
                questions.add(new Question("Jika 5 + x = 10, berapa nilai x?",
                        Arrays.asList("2", "3", "4", "5"), "5", null, "x = 10 - 5 = 5."));
                break;

            case "Penalaran Matematika":
                questions.add(new Question("Jika x + 3 = 7, berapa nilai x?",
                        Arrays.asList("3", "4", "5", "6"), "4", null, "x = 7 - 3 = 4."));
                questions.add(new Question("Apa hasil dari 5 * 6?",
                        Arrays.asList("30", "25", "35", "40"), "30", null, "5 * 6 = 30."));
                questions.add(new Question("Berapa hasil dari 12 / 4?",
                        Arrays.asList("2", "3", "4", "5"), "3", null, "12 / 4 = 3."));
                questions.add(new Question("Jika 2x = 10, berapa nilai x?",
                        Arrays.asList("2", "3", "4", "5"), "5", null, "x = 10 / 2 = 5."));
                questions.add(new Question("Apa hasil dari 9 - 3?",
                        Arrays.asList("5", "6", "7", "8"), "6", null, "9 - 3 = 6."));
                break;

            case "Pengetahuan Kuantitatif":
                questions.add(new Question("Berapa banyak sudut pada segitiga?",
                        Arrays.asList("2", "3", "4", "5"), "3", null, "Segitiga memiliki 3 sudut."));
                questions.add(new Question("Apa hasil dari 15% dari 200?",
                        Arrays.asList("30", "25", "20", "15"), "30", null, "15% dari 200 adalah 30."));
                questions.add(new Question("Jika sebuah mobil menempuh jarak 60 km dalam 1 jam, berapa kecepatan mobil tersebut?",
                        Arrays.asList("60 km/jam", "50 km/jam", "70 km/jam", "80 km/jam"), "60 km/jam", null, "Kecepatan = Jarak / Waktu."));
                questions.add(new Question("Berapa hasil dari 8 * 7?",
                        Arrays.asList("54", "56", "58", "60"), "56", null, "8 * 7 = 56."));
                questions.add(new Question("Jika 3x = 12, berapa nilai x?",
                        Arrays.asList("2", "3", "4", "5"), "4", null, "x = 12 / 3 = 4."));
                break;

            case "Literasi Inggris":
                questions.add(new Question("What is the synonym of 'happy'?",
                        Arrays.asList("Sad", "Joyful", "Angry", "Bored"), "Joyful", null, "Joyful means happy."));
                questions.add(new Question("What does 'bilingual' mean?",
                        Arrays.asList("Speaking one language", "Speaking two languages", "Speaking three languages", "None of the above"), "Speaking two languages", null, "Bilingual means speaking two languages."));
                questions.add(new Question("Choose the correct sentence:",
                        Arrays.asList("He go to school", "He goes to school", "He going to school", "He gone to school"), "He goes to school", null, "The correct form is 'He goes to school'."));
                questions.add(new Question("What is the opposite of 'difficult'?",
                        Arrays.asList("Easy", "Hard", "Complicated", "Tough"), "Easy", null, "The opposite of difficult is easy."));
                questions.add(new Question("What is the past tense of 'go'?",
                        Arrays.asList("Goes", "Gone", "Went", "Going"), "Went", null, "The past tense of 'go' is 'went'."));
                break;

            case "Literasi Indonesia":
                questions.add(new Question("Apa sinonim dari 'cepat'?",
                        Arrays.asList("Lambat", "Kilat", "Pelan", "Tepat"), "Kilat", null, "Sinonim dari cepat adalah kilat."));
                questions.add(new Question("Apa arti dari kata 'berita'?",
                        Arrays.asList("Informasi", "Cerita", "Novel", "Puisi"), "Informasi", null, "Berita berarti informasi."));
                questions.add(new Question("Pilih kalimat yang benar:",
                        Arrays.asList("Dia pergi ke pasar", "Dia pergi pasar", "Dia pasar pergi", "Dia ke pasar pergi"), "Dia pergi ke pasar", null, "Kalimat yang benar adalah 'Dia pergi ke pasar'."));
                questions.add(new Question("Apa lawan kata dari 'besar'?",
                        Arrays.asList("Kecil", "Tinggi", "Lebar", "Rendah"), "Kecil", null, "Lawan kata dari besar adalah kecil."));
                questions.add(new Question("Apa bentuk lampau dari 'makan'?",
                        Arrays.asList("Makan", "Memakan", "Dimakan", "Makanlah"), "Dimakan", null, "Bentuk lampau dari 'makan' adalah 'dimakan'."));
                break;

            case "Pemahaman Bacaan dan Menulis":
                questions.add(new Question("Apa tema dari teks berikut: 'Hujan adalah berkah bagi petani'?",
                        Arrays.asList("Cuaca", "Pertanian", "Kesehatan", "Ekonomi"), "Pertanian", null, "Tema teks adalah pertanian."));
                questions.add(new Question("Apa tujuan dari membaca?",
                        Arrays.asList("Mendapatkan informasi", "Bersenang-senang", "Belajar", "Semua jawaban benar"), "Semua jawaban benar", null, "Tujuan membaca bisa beragam."));
                questions.add(new Question("Pilih kalimat yang tepat untuk mengakhiri paragraf:",
                        Arrays.asList("Oleh karena itu", "Namun", "Selanjutnya", "Di sisi lain"), "Oleh karena itu", null, "Kalimat penutup yang tepat adalah 'Oleh karena itu'."));
                questions.add(new Question("Apa yang dimaksud dengan 'sinopsis'?",
                        Arrays.asList("Ringkasan", "Ulasan", "Analisis", "Kritik"), "Ringkasan", null, "Sinopsis berarti ringkasan."));
                questions.add(new Question("Apa yang harus dilakukan sebelum menulis?",
                        Arrays.asList("Membaca", "Berpikir", "Merencanakan", "Semua jawaban benar"), "Semua jawaban benar", null, "Sebelum menulis, sebaiknya melakukan semua hal tersebut."));
                break;

            case "Pengetahuan Umum":
                questions.add(new Question("Siapa penemu lampu pijar?",
                        Arrays.asList("Nikola Tesla", "Thomas Edison", "Albert Einstein", "Isaac Newton"), "Thomas Edison", null, "Penemu lampu pijar adalah Thomas Edison."));
                questions.add(new Question("Apa ibu kota Indonesia?",
                        Arrays.asList("Jakarta", "Bandung", "Surabaya", "Medan"), "Jakarta", null, "Ibu kota Indonesia adalah Jakarta."));
                questions.add(new Question("Apa nama planet terdekat dengan matahari?",
                        Arrays.asList("Venus", "Mars", "Merkurius", "Jupiter"), "Merkurius", null, "Planet terdekat dengan matahari adalah Merkurius."));
                questions.add(new Question("Siapa yang menulis 'Laskar Pelangi'?",
                        Arrays.asList("Andrea Hirata", "Habiburrahman El Shirazy", "Tere Liye", "Pramoedya Ananta Toer"), "Andrea Hirata", null, "'Laskar Pelangi' ditulis oleh Andrea Hirata."));
                questions.add(new Question("Apa simbol kimia untuk air?",
                        Arrays.asList("H2O", "O2", "CO2", "NaCl"), "H2O", null, "Simbol kimia untuk air adalah H2O."));
                break;

            default:
                break;
        }

        return questions;
    }

    // Question class
    static class Question {
        private final String text;
        private final List<String> options;
        private final String correctAnswer;
        private final String imagePath;
        private final String explanation;

        public Question(String text, List<String> options, String correctAnswer, String imagePath, String explanation) {
            this.text = text;
            this.options = options;
            this.correctAnswer = correctAnswer;
            this.imagePath = imagePath;
            this.explanation = explanation;
        }

        public String getText() {
            return text;
        }

        public List<String> getOptions() {
            return options;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }

        public String getImagePath() {
            return imagePath;
        }

        public String getExplanation() {
            return explanation;
        }
    }
}