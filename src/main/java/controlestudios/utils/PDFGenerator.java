package controlestudios.utils;

import controlestudios.models.Estudiante;
import controlestudios.models.Nota;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PDFGenerator {

    public static void generarBoletaNotas(Estudiante estudiante, List<Nota> notas, String rutaLogo) {
        String dest = "boleta_" + estudiante.getCedula() + ".pdf";

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            // Configurar contenido del PDF
            try (PDPageContentStream content = new PDPageContentStream(doc, page)) {
                // --- Agregar logo ---
                PDImageXObject logo = PDImageXObject.createFromFileByExtension(new File(rutaLogo), doc);
                float logoWidth = 100;
                float logoHeight = (logoWidth * logo.getHeight()) / logo.getWidth();
                content.drawImage(logo, 50, 750 - logoHeight, logoWidth, logoHeight);

                // --- Título del plantel ---
                content.beginText();
                content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 16);
                content.newLineAtOffset(50, 720);
                content.showText("COLEGIO NACIONAL CÉSAR AUGUSTO ÁGREDA");
                content.endText();

                // --- Datos del estudiante ---
                content.beginText();
                content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                content.newLineAtOffset(50, 680);
                content.showText("Nombre: " + estudiante.getNombreCompleto());
                content.newLineAtOffset(0, -20);
                content.showText("Cédula: " + estudiante.getCedula());
                content.newLineAtOffset(0, -20);
                content.showText("Sección: " + estudiante.getSeccion());
                content.endText();

                // --- Tabla de notas ---
                float margin = 50;
                float yPosition = 600;
                float rowHeight = 20;
                float tableWidth = page.getMediaBox().getWidth() - 2 * margin;

                // Encabezados de la tabla
                drawTableHeader(content, margin, yPosition, tableWidth, rowHeight);
                yPosition -= rowHeight;

                // Filas de notas
                double sumaTotal = 0;
                for (Nota nota : notas) {
                    drawTableRow(content, margin, yPosition, tableWidth, rowHeight, nota);
                    yPosition -= rowHeight;
                    sumaTotal += nota.getValor();
                }

                // --- Promedio general ---
                content.beginText();
                content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
                content.newLineAtOffset(margin, yPosition - 30);
                content.showText("Promedio General: " + String.format("%.2f", sumaTotal / notas.size()));
                content.endText();
            }

            // Guardar el documento
            doc.save(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void drawTableHeader(PDPageContentStream content, float x, float y, float width, float height) throws IOException {
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
        content.setLineWidth(1f);

        String[] headers = {"Materia", "Nota", "Promedio"};
        float colWidth = width / headers.length;

        for (int i = 0; i < headers.length; i++) {
            content.beginText();
            content.newLineAtOffset(x + (i * colWidth) + 5, y - 15);
            content.showText(headers[i]);
            content.endText();
        }

        content.moveTo(x, y);
        content.lineTo(x + width, y);
        content.stroke();
    }

    private static void drawTableRow(PDPageContentStream content, float x, float y, float width, float height, Nota nota) throws IOException {
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
        float colWidth = width / 3;

        String[] rowData = {
                nota.getNombreMateria(),
                String.valueOf(nota.getValor()),
                "N/A" // Ejemplo: Campo para promedio por materia
        };

        for (int i = 0; i < rowData.length; i++) {
            content.beginText();
            content.newLineAtOffset(x + (i * colWidth) + 5, y - 15);
            content.showText(rowData[i]);
            content.endText();
        }

        content.moveTo(x, y - height);
        content.lineTo(x + width, y - height);
        content.stroke();
    }
}