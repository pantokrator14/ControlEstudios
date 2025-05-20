package controlestudios.utils;

import controlestudios.models.Estudiante;
import javafx.scene.control.Alert;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PDFConstanciaGenerator {

    public static void generarConstancia(Estudiante estudiante, String rutaLogo) {
        String dest = SystemPaths.getDesktopPath() + "\\Constancia_" + estudiante.getCedula() + ".pdf";
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'días del mes' MMMM 'de' yyyy");

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            try (PDPageContentStream content = new PDPageContentStream(doc, page)) {
                // ========== LOGO Y MEMBRETE ALINEADOS ==========
                // Cargar logo
                InputStream logoStream = PDFConstanciaGenerator.class.getResourceAsStream(rutaLogo);
                PDImageXObject logo = PDImageXObject.createFromByteArray(doc, logoStream.readAllBytes(), "logo");

                // Posicionar logo y texto en misma línea
                float logoWidth = 80;
                float logoHeight = 90;
                float yPosition = 750;
                // Dibujar logo
                content.drawImage(logo, 50, yPosition - 60, logoWidth, logoHeight); // Ajuste vertical -10 para alinear con texto

                // Texto institucional al lado del logo
                content.setFont(PDType1Font.HELVETICA_BOLD, 10);
                content.beginText();
                content.newLineAtOffset(50 + logoWidth + 20, yPosition); // 15px de separación
                content.showText("REPÚBLICA BOLIVARIANA DE VENEZUELA");
                content.newLineAtOffset(0, -15);
                content.showText("MINISTERIO DEL PODER POPULAR PARA LA EDUCACIÓN");
                content.newLineAtOffset(0, -15);
                content.showText("UNIDAD EDUCATIVA NACIONAL CÉSAR AUGUSTO AGREDA");
                content.newLineAtOffset(0, -15);
                content.showText("CÓDIGO DEL PLANTEL: S1512D1114");
                content.newLineAtOffset(0, -15);
                content.showText("RIF J-40963802");
                content.endText();

                // ========== TÍTULO CENTRADO ==========
                String titulo = "CONSTANCIA DE ESTUDIO";
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                float titleWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(titulo)/1000 * 16;
                float titleX = (PDRectangle.A4.getWidth() - titleWidth)/2;

                content.beginText();
                content.newLineAtOffset(titleX, 620);
                content.showText(titulo);
                content.endText();

                // ========== TEXTO JUSTIFICADO ==========
                String textoConstancia = "Quien suscribe, Gustavo Curiel, titular de la cédula de identidad V.-9.506.178, " +
                        "en mi condición de Director de la Unidad Educativa Nacional \"Cesar Augusto Agreda\", que funciona en " +
                        "la Av. Buchivacoa entre Pinto Salinas y Callejón Ignacio Sarmiento, sector Bobare, Municipio Miranda " +
                        "del Estado Falcón, hace constar por medio de la presente que el(la) Alumno(a) " +
                        estudiante.getNombreCompleto() + ", titular de la cédula de identidad V-" + estudiante.getCedula() + ", " +
                        "cursa en la sección: " + estudiante.getSeccion() + ", de este plantel a la fecha actual.";

                content.setFont(PDType1Font.HELVETICA, 12);
                writeJustifiedText(content, textoConstancia, 50, 580, 500); // 515 = Ancho disponible (595px - margenes)

                // ========== FECHA Y FIRMA ==========
                // Fecha
                String fechaTexto = "Constancia que se expide en Santa Ana de Coro, a los " + fechaActual.format(formatter) + ".";
                content.beginText();
                content.newLineAtOffset(50, 400);
                content.showText(fechaTexto);
                content.endText();

                // Firma centrada
                content.setFont(PDType1Font.HELVETICA_BOLD, 12);
                float firmaX = (PDRectangle.A4.getWidth() - 150)/2; // Centrar línea de firma

                content.beginText();
                content.newLineAtOffset(firmaX, 300);
                content.showText("_________________________");
                content.newLineAtOffset(40, -30); // Ajuste para centrar texto bajo línea
                content.showText("Lcdo. Gustavo Curiel");
                content.newLineAtOffset(0, -20);
                content.showText("C.I.: V.-9.506.178");
                content.newLineAtOffset(0, -20);
                content.showText("Director del Plantel");
                content.endText();

            }

            doc.save(dest);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error al generar la constancia: " + e.getMessage()).show();
        }
    }

    // ========== MÉTODO PARA TEXTO JUSTIFICADO ==========
    private static void writeJustifiedText(PDPageContentStream content, String text,
                                           float startX, float startY, float width) throws IOException {
        String[] words = text.split(" ");
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (currentLine.length() == 0) {
                currentLine.append(word);
            } else {
                String testLine = currentLine + " " + word;
                float testWidth = PDType1Font.HELVETICA.getStringWidth(testLine)/1000 * 12;

                if (testWidth <= width) {
                    currentLine.append(" ").append(word);
                } else {
                    lines.add(currentLine.toString());
                    currentLine = new StringBuilder(word);
                }
            }
        }
        lines.add(currentLine.toString());

        float yPosition = startY;
        for (String line : lines) {
            content.beginText();
            content.newLineAtOffset(startX, yPosition);

            // Calcular espacios para justificación
            float lineWidth = PDType1Font.HELVETICA.getStringWidth(line)/1000 * 12;
            if (lineWidth < width) {
                int numSpaces = line.split(" ").length - 1;
                if (numSpaces > 0) {
                    float spaceWidth = (width - lineWidth)/numSpaces;
                    content.setCharacterSpacing(spaceWidth/1000 * 12); // Convertir a unidades de texto
                }
            }

            content.showText(line);
            content.setCharacterSpacing(0); // Resetear espaciado
            content.endText();
            yPosition -= 20; // Espacio entre líneas
        }
    }
}