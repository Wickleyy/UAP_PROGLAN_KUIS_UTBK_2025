package org.example.uap_proglan;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.util.Units;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class FileManager {
    public void generateExplanationFile(User user, Map<String, List<String>> answerHistoryBySubtest, Map<String, Integer> scoresBySubtest) {
        String fileName = "pembahasan_kuis_" + user.getName() + ".docx"; // File name based on user
        try (XWPFDocument document = new XWPFDocument()) {
            XWPFParagraph title = document.createParagraph();
            XWPFRun titleRun = title.createRun();
            titleRun.setBold(true);
            titleRun.setFontSize(16);
            titleRun.setText("Hasil Kuis dan Pembahasan");
            titleRun.addBreak();

            // Add user name
            XWPFParagraph userNameParagraph = document.createParagraph();
            XWPFRun userNameRun = userNameParagraph.createRun();
            userNameRun.setText("Nama: " + user.getName());
            userNameParagraph.createRun().addBreak(); // Add new line

            // Add results for each subtest
            for (Map.Entry<String, List<String>> entry : answerHistoryBySubtest.entrySet()) {
                String subtestName = entry.getKey();
                List<String> records = entry.getValue();

                // Add completed subtest
                XWPFParagraph subtestParagraph = document.createParagraph();
                XWPFRun subtestRun = subtestParagraph.createRun();
                subtestRun.setBold(true);
                subtestRun.setText("Subtest: " + subtestName);
                subtestParagraph.createRun().addBreak(); // Add new line

                // Add answer history for this subtest
                for (String record : records) {
                    if (record.startsWith("Gambar: ")) {
                        String imagePath = record.substring(8); // Get image path
                        try (InputStream is = new FileInputStream(imagePath)) {
                            XWPFRun run = document.createParagraph().createRun();
                            run.addPicture(is, XWPFDocument.PICTURE_TYPE_PNG, imagePath, Units.toEMU(200), Units.toEMU(200)); // Adjust image size
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        XWPFParagraph paragraph = document.createParagraph();
                        XWPFRun run = paragraph.createRun();
                        run.setText(record);
                    }
                }

                // Add score for this subtest
                XWPFParagraph scoreParagraph = document.createParagraph();
                XWPFRun scoreRun = scoreParagraph.createRun();
                scoreRun.setText("Skor Akhir: " + scoresBySubtest.get(subtestName));
                scoreParagraph.createRun().addBreak(); // Add new line

                // Add space between subtests
                document.createParagraph();
            }

            // Save document
            try (FileOutputStream out = new FileOutputStream(fileName)) {
                document.write(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}