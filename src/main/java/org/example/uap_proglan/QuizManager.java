package org.example.uap_proglan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizManager {
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;

    // Konstanta jalur untuk gambar
    private static final String IMAGE_PATH = "src\\main\\resources\\images\\";

    public void loadQuestions(String subtest) {
        questions = new ArrayList<>();

        // Load questions berdasarkan subtest yang dipilih
        switch (subtest) {
            case "Penalaran Umum":
                addQuestion("Apa hasil dari 2 + 2?",
                        List.of("3", "4", "5", "6"), "4", null, "Penjumlahan 2 + 2 menghasilkan 4.");
                addQuestion("Siapa presiden pertama Indonesia?",
                        List.of("Soekarno", "Suharto", "Habibie", "Megawati"), "Soekarno", null,
                        "Presiden pertama Indonesia adalah Soekarno.");
                addQuestion("Manakah gambar segitiga?",
                        List.of("A", "B", "C", "D"), "B", IMAGE_PATH + "triangle.png",
                        "Gambar B adalah gambar segitiga.");
                addQuestion("Apa hasil dari 10 - 4?",
                        List.of("5", "6", "7", "8"), "6", null, "10 - 4 = 6.");
                addQuestion("Jika 5 + x = 10, berapa nilai x?",
                        List.of("2", "3", "4", "5"), "5", null, "x = 10 - 5 = 5.");
                break;

            case "Penalaran Matematika":
                addQuestion("Jika x + 3 = 7, berapa nilai x?",
                        List.of("3", "4", "5", "6"), "4", null, "x = 7 - 3 = 4.");
                addQuestion("Apa hasil dari 5 * 6?",
                        List.of("30", "25", "35", "40"), "30", null, "5 * 6 = 30.");
                addQuestion("Berapa hasil dari 12 / 4?",
                        List.of("2", "3", "4", "5"), "3", null, "12 / 4 = 3.");
                addQuestion("Jika 2x = 10, berapa nilai x?",
                        List.of("2", "3", "4", "5"), "5", null, "x = 10 / 2 = 5.");
                addQuestion("Apa hasil dari 9 - 3?",
                        List.of("5", "6", "7", "8"), "6", null, "9 - 3 = 6.");
                break;

            case "Pengetahuan Kuantitatif":
                questions.add(new Question("Berapa banyak sudut pada segitiga?",
                        List.of("2", "3", "4", "5"), "3", null, "Segitiga memiliki 3 sudut."));
                questions.add(new Question("Apa hasil dari 15% dari 200?",
                        List.of("30", "25", "20", "15"), "30", null, "15% dari 200 adalah 30."));
                questions.add(new Question("Jika sebuah mobil menempuh jarak 60 km dalam 1 jam, berapa kecepatan mobil tersebut?",
                        List.of("60 km/jam", "50 km/jam", "70 km/jam", "80 km/jam"), "60 km/jam", null, "Kecepatan = Jarak / Waktu."));
                questions.add(new Question("Berapa hasil dari 8 * 7?",
                        List.of("54", "56", "58", "60"), "56", null, "8 * 7 = 56."));
                questions.add(new Question("Jika 3x = 12, berapa nilai x?",
                        List.of("2", "3", "4", "5"), "4", null, "x = 12 / 3 = 4."));
                break;

            case "Literasi Inggris":
                questions.add(new Question("What is the synonym of 'happy'?",
                        List.of("Sad", "Joyful", "Angry", "Bored"), "Joyful", null, "Joyful means happy."));
                questions.add(new Question("What does 'bilingual' mean?",
                        List.of("Speaking one language", "Speaking two languages", "Speaking three languages", "None of the above"), "Speaking two languages", null, "Bilingual means speaking two languages."));
                questions.add(new Question("Choose the correct sentence:",
                        List.of("He go to school", "He goes to school", "He going to school", "He gone to school"), "He goes to school", null, "The correct form is 'He goes to school'."));
                questions.add(new Question("What is the opposite of 'difficult'?",
                        List.of("Easy", "Hard", "Complicated", "Tough"), "Easy", null, "The opposite of difficult is easy."));
                questions.add(new Question("What is the past tense of 'go'?",
                        List.of("Goes", "Gone", "Went", "Going"), "Went", null, "The past tense of 'go' is 'went'."));
                break;

            case "Literasi Indonesia":
                questions.add(new Question("Apa sinonim dari 'cepat'?",
                        List.of("Lambat", "Kilat", "Pelan", "Tepat"), "Kilat", null, "Sinonim dari cepat adalah kilat."));
                questions.add(new Question("Apa arti dari kata 'berita'?",
                        List.of("Informasi", "Cerita", "Novel", "Puisi"), "Informasi", null, "Berita berarti informasi."));
                questions.add(new Question("Pilih kalimat yang benar:",
                        List.of("Dia pergi ke pasar", "Dia pergi pasar", "Dia pasar pergi", "Dia ke pasar pergi"), "Dia pergi ke pasar", null, "Kalimat yang benar adalah 'Dia pergi ke pasar'."));
                questions.add(new Question("Apa lawan kata dari 'besar'?",
                        List.of("Kecil", "Tinggi", "Lebar", "Rendah"), "Kecil", null, "Lawan kata dari besar adalah kecil."));
                questions.add(new Question("Apa bentuk lampau dari 'makan'?",
                        List.of("Makan", "Memakan", "Dimakan", "Makanlah"), "Dimakan", null, "Bentuk lampau dari 'makan' adalah 'dimakan'."));
                break;

            case "Pemahaman Bacaan dan Menulis":
                questions.add(new Question("Apa tema dari teks berikut: 'Hujan adalah berkah bagi petani'?",
                        List.of("Cuaca", "Pertanian", "Kesehatan", "Ekonomi"), "Pertanian", null, "Tema teks adalah pertanian."));
                questions.add(new Question("Apa tujuan dari membaca?",
                        List.of("Mendapatkan informasi", "Bersenang-senang", "Belajar", "Semua jawaban benar"), "Semua jawaban benar", null, "Tujuan membaca bisa beragam."));
                questions.add(new Question("Pilih kalimat yang tepat untuk mengakhiri paragraf:",
                        List.of("Oleh karena itu", "Namun", "Selanjutnya", "Di sisi lain"), "Oleh karena itu", null, "Kalimat penutup yang tepat adalah 'Oleh karena itu'."));
                questions.add(new Question("Apa yang dimaksud dengan 'sinopsis'?",
                        List.of("Ringkasan", "Ulasan", "Analisis", "Kritik"), "Ringkasan", null, "Sinopsis berarti ringkasan."));
                questions.add(new Question("Apa yang harus dilakukan sebelum menulis?",
                        List.of("Membaca", "Berpikir", "Merencanakan", "Semua jawaban benar"), "Semua jawaban benar", null, "Sebelum menulis, sebaiknya melakukan semua hal tersebut."));
                break;

            case "Pengetahuan Umum":
                questions.add(new Question("Siapa penemu lampu pijar?",
                        List.of("Nikola Tesla", "Thomas Edison", "Albert Einstein", "Isaac Newton"), "Thomas Edison", null, "Penemu lampu pijar adalah Thomas Edison."));
                questions.add(new Question("Apa ibu kota Indonesia?",
                        List.of("Jakarta", "Bandung", "Surabaya", "Medan"), "Jakarta", null, "Ibu kota Indonesia adalah Jakarta."));
                questions.add(new Question("Apa nama planet terdekat dengan matahari?",
                        List.of("Venus", "Mars", "Merkurius", "Jupiter"), "Merkurius", null, "Planet terdekat dengan matahari adalah Merkurius."));
                questions.add(new Question("Siapa yang menulis 'Laskar Pelangi'?",
                        List.of("Andrea Hirata", "Habiburrahman El Shirazy", "Tere Liye", "Pramoedya Ananta Toer"), "Andrea Hirata", null, "'Laskar Pelangi' ditulis oleh Andrea Hirata."));
                questions.add(new Question("Apa simbol kimia untuk air?",
                        List.of("H2O", "O2", "CO2", "NaCl"), "H2O", null, "Simbol kimia untuk air adalah H2O."));
                break;

            default:
                break;
        }

        Collections.shuffle(questions); // Acak daftar pertanyaan
        currentQuestionIndex = 0; // Reset indeks pertanyaan
        score = 0; // Reset skor
    }

    // Metode untuk menambahkan pertanyaan ke daftar
    private void addQuestion(String text, List<String> options, String correctAnswer, String imagePath, String explanation) {
        questions.add(new Question(text, options, correctAnswer, imagePath, explanation));
    }

    // Mendapatkan pertanyaan saat ini
    public Question getCurrentQuestion() {
        return questions.get(currentQuestionIndex);
    }

    // Berpindah ke pertanyaan berikutnya
    public void nextQuestion() {
        currentQuestionIndex++;
    }

    // Mengecek apakah kuis selesai
    public boolean isQuizComplete() {
        return currentQuestionIndex >= questions.size();
    }

    // Mendapatkan skor
    public int getScore() {
        return score;
    }

    // Menambah skor
    public void incrementScore() {
        score += 20; // Tambahkan poin untuk jawaban benar
    }
}