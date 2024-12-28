
# Kuis UTBK (JavaSwing Quiz Application)

## Deskripsi
Kuis UTBK adalah aplikasi kuis berbasis JavaSwing yang dirancang untuk membantu pengguna dalam mempersiapkan ujian UTBK. Aplikasi ini menyediakan berbagai subtest dengan pertanyaan yang beragam dan penjelasan untuk setiap jawaban yang dipilih. Aplikasi ini dibuat untuk memenuhi tugas dari Ujian Akhir Praktikum (UAP) Pemrograman Lanjut, Informatika, Universitas Muhammadiyah Malang.

## Fitur Utama
- *Menu Utama*: Pengguna dapat memilih untuk memulai kuis sebagai pengguna baru.
- *Identitas Pengguna*: Pengguna diminta untuk memasukkan nama mereka sebelum memulai kuis.
- *Pemilihan Subtest*: Pengguna dapat memilih dari berbagai subtest, termasuk:
    - Penalaran Umum
    - Penalaran Matematika
    - Pengetahuan Kuantitatif
    - Literasi Inggris
    - Literasi Indonesia
    - Pemahaman Bacaan dan Menulis
    - Pengetahuan Umum
- *Kuis*: Pengguna menjawab pertanyaan dengan pilihan ganda.
- *Timer*: Pengguna memiliki waktu terbatas untuk menyelesaikan kuis (5 menit).
- *Hasil Akhir*: Setelah menyelesaikan kuis, pengguna akan melihat skor mereka dan dapat memilih untuk mengulang kuis atau keluar.
- *File Pembahasan*: Aplikasi dapat menghasilkan file DOCX yang berisi hasil kuis dan penjelasan untuk setiap pertanyaan.

## Persyaratan
- *Java Development Kit (JDK)*: Pastikan Anda memiliki JDK yang terinstal (versi 8 atau lebih baru).
- *Apache POI*: Untuk menghasilkan file DOCX, Anda perlu menambahkan pustaka Apache POI ke dalam proyek Anda.

## Instalasi
1. *Clone Repository*:
   bash
   git clone <repository-url>
   cd kuis4


2. *Tambahkan Dependensi*: Pastikan untuk menambahkan pustaka Apache POI ke dalam proyek Anda. Anda dapat mengunduhnya dari [Apache POI](https://poi.apache.org/download.html) dan menambahkannya ke classpath proyek Anda.

3. *Jalankan Aplikasi*: Buka terminal dan navigasikan ke direktori proyek.

## Penggunaan
1. *Mulai Aplikasi*: Jalankan aplikasi dan pilih "Pengguna Baru".
2. *Masukkan Nama*: Ketikkan nama Anda dan klik "Mulai Kuis".
3. *Pilih Subtest*: Pilih subtest yang ingin Anda kerjakan.
4. *Jawab Pertanyaan*: Bacalah pertanyaan dan pilih jawaban yang Anda anggap benar. Klik "Berikutnya" untuk melanjutkan.
5. *Lihat Hasil*: Setelah menyelesaikan semua pertanyaan, Anda akan melihat skor akhir Anda.
6. *File Pembahasan*: Anda dapat memilih untuk menghasilkan file pembahasan yang berisi hasil dan penjelasan untuk setiap pertanyaan.
