package nl.vu.cs.softwaredesign;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.IOException;

public class PdfExporter {

    public void exportPlan(TrainingPlan plan, String filePath) throws IOException {
        PDType1Font font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        float fontSize = 11f;
        float leading = 14f;

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(font, fontSize);
            contentStream.newLineAtOffset(50, 750);

            float y = 750;
            String[] lines = plan.toDisplayString().split("\n");

            for (String line : lines) {
                if (y <= 60) {
                    contentStream.endText();
                    contentStream.close();

                    page = new PDPage();
                    document.addPage(page);

                    contentStream = new PDPageContentStream(document, page);
                    contentStream.beginText();
                    contentStream.setFont(font, fontSize);
                    contentStream.newLineAtOffset(50, 750);
                    y = 750;
                }

                contentStream.showText(line);
                contentStream.newLineAtOffset(0, -leading);
                y -= leading;
            }

            contentStream.endText();
            contentStream.close();

            document.save(filePath);
        }
    }
}