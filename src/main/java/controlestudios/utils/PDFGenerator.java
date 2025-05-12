package controlestudios.utils;

import controlestudios.models.Estudiante;
import controlestudios.models.Nota;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.IOException;
import java.util.List;

public class PDFGenerator {

    public static void generarBoletaNotas(Estudiante estudiante, List<Nota> notas, String rutaLogo) {
        String dest = SystemPaths.getDesktopPath() + "\\Boletin_" + estudiante.getCedula() + ".pdf";

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            try (PDPageContentStream content = new PDPageContentStream(doc, page)) {

                // --- Encabezado institucional ---
                content.setFont(PDType1Font.HELVETICA_BOLD, 10);
                content.beginText();
                content.newLineAtOffset(50, 750);
                content.showText("REPÚBLICA BOLIVARIANA DE VENEZUELA");
                content.newLineAtOffset(0, -15);
                content.showText("MINISTERIO DEL PODER POPULAR PARA LA EDUCACIÓN");
                content.newLineAtOffset(0, -15);
                content.showText("UNIDAD EDUCATIVA NACIONAL CÉSAR AUGUSTO AGREDA");
                content.endText();

                // --- Logo (si es necesario) ---
                PDImageXObject logo = PDImageXObject.createFromFile(rutaLogo, doc);
                content.drawImage(logo, 400, 700, 100, 100); // Ajusta coordenadas según el modelo

                // --- Título del boletín ---
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.beginText();
                content.newLineAtOffset(220, 650); // Centrado aproximado
                content.showText("BOLETÍN DE CALIFICACIONES");
                content.endText();

                // --- Datos del estudiante ---
                content.setFont(PDType1Font.HELVETICA, 12);
                content.beginText();
                content.newLineAtOffset(50, 600);
                content.showText("Nombre: " + estudiante.getNombreCompleto());
                content.newLineAtOffset(0, -25);
                content.showText("Cédula: " + estudiante.getCedula());
                content.newLineAtOffset(0, -25);
                content.showText("Sección: " + estudiante.getSeccion());
                content.endText();

                // --- Tabla de materias ---
                float marginX = 50;
                float yPosition = 500;
                float rowHeight = 20;

                // Encabezados de la tabla
                content.setFont(PDType1Font.HELVETICA_BOLD, 12);
                content.beginText();
                content.newLineAtOffset(marginX, yPosition);
                content.showText("MATERIA");
                content.newLineAtOffset(350, 0); // Ajuste para columna "PROMEDIO"
                content.showText("PROMEDIO MATERIA");
                content.endText();

                // Línea divisoria debajo del encabezado
                content.moveTo(marginX, yPosition - 5);
                content.lineTo(marginX + 400, yPosition - 5);
                content.stroke();

                // Filas de notas
                content.setFont(PDType1Font.HELVETICA, 12);
                yPosition -= 30; // Espacio después del encabezado
                for (Nota nota : notas) {
                    content.beginText();
                    content.newLineAtOffset(marginX, yPosition);
                    content.showText(nota.getNombreMateria());
                    content.newLineAtOffset(350, 0);
                    content.showText(String.valueOf(nota.getValor()));
                    content.endText();
                    yPosition -= rowHeight;
                }

                // --- Promedio general ---
                double promedio = notas.stream().mapToDouble(Nota::getValor).average().orElse(0);
                content.setFont(PDType1Font.HELVETICA_BOLD, 12);
                content.beginText();
                content.newLineAtOffset(marginX, yPosition - 30);
                content.showText("Promedio General: " + String.format("%.2f", promedio));
                content.endText();

                // --- Firma del director ---
                content.setFont(PDType1Font.HELVETICA_OBLIQUE, 12);
                content.beginText();
                content.newLineAtOffset(400, 100); // Alineado a la derecha
                content.showText("Lcdo. Gustavo Curiel");
                content.newLineAtOffset(0, -20);
                content.showText("Director del Plantel");
                content.endText();

            }

            doc.save(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}