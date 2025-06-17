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
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PDFGenerator {

    public static void generarBoletaNotas(
            Estudiante estudiante,
            List<Nota> notas,
            String rutaLogo,
            int momento,
            int anioEscolar,
            double promedioGeneral
    ) {
        String periodo = PeriodoUtil.obtenerNombrePeriodo(momento, anioEscolar);
        String dest = SystemPaths.getDesktopPath() + "\\Boletin_" + estudiante.getCedula() + ".pdf";

        try(PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            try (PDPageContentStream content = new PDPageContentStream(doc, page)) {

                // ========== LOGO Y MEMBRETE ALINEADOS ==========
                // Cargar logo
                InputStream logoStream = PDFGenerator.class.getResourceAsStream(rutaLogo); // Clase actual
                if (logoStream == null) {
                    throw new IOException("Logo no encontrado: " + rutaLogo);
                }
                PDImageXObject logo = PDImageXObject.createFromByteArray(doc, logoStream.readAllBytes(), "logo");

                // Posicionar logo y texto en misma línea
                float logoWidth = 80;
                float logoHeight = 90;
                float yyPosition = 750;
                // Dibujar logo
                content.drawImage(logo, 50, yyPosition - 60, logoWidth, logoHeight); // Ajuste vertical -10 para alinear con texto

                // Texto institucional al lado del logo
                content.setFont(PDType1Font.HELVETICA_BOLD, 10);
                content.beginText();
                content.newLineAtOffset(50 + logoWidth + 20, yyPosition); // 15px de separación
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

                // --- Título del boletín ---
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.beginText();
                content.newLineAtOffset(220, 650); // Centrado aproximado
                content.showText("BOLETÍN DE CALIFICACIONES - " + periodo);
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

                // --- Tabla de materias (con promedios) ---
                float marginX = 50;
                float yPosition = 500;
                float rowHeight = 20;

// Agrupar notas por materia
                Map<String, List<Double>> notasPorMateria = notas.stream()
                        .collect(Collectors.groupingBy(
                                Nota::getNombreMateria,
                                Collectors.mapping(Nota::getValor, Collectors.toList())
                        ));

// Encabezados de la tabla
                content.setFont(PDType1Font.HELVETICA_BOLD, 12);
                content.beginText();
                content.newLineAtOffset(marginX, yPosition);
                content.showText("MATERIA");
                content.newLineAtOffset(350, 0);
                content.showText("PROMEDIO");
                content.endText();

// Línea divisoria debajo del encabezado
                content.moveTo(marginX, yPosition - 5);
                content.lineTo(marginX + 400, yPosition - 5);
                content.stroke();

// Filas de promedios por materia
                content.setFont(PDType1Font.HELVETICA, 12);
                yPosition -= 30;
                for (Map.Entry<String, List<Double>> entry : notasPorMateria.entrySet()) {
                    String materia = entry.getKey();
                    double promedioMateria = entry.getValue().stream()
                            .mapToDouble(Double::doubleValue)
                            .average()
                            .orElse(0);

                    content.beginText();
                    content.newLineAtOffset(marginX, yPosition);
                    content.showText(materia);
                    content.newLineAtOffset(350, 0);
                    content.showText(String.format("%.2f", promedioMateria));
                    content.endText();

                    yPosition -= rowHeight;
                }

                // --- Promedio general ---
                content.setFont(PDType1Font.HELVETICA_BOLD, 12);
                content.beginText();
                content.newLineAtOffset(marginX, yPosition - 30);
                content.showText("Promedio General: " + String.format("%.2f", promedioGeneral));
                content.endText();

                // --- Firma del director ---
                content.setFont(PDType1Font.HELVETICA_BOLD, 12);
                float firmaY = yPosition - 100; // espacio después de la última fila
                float firmaX = (PDRectangle.A4.getWidth() - 150) / 2;

                content.beginText();
                content.newLineAtOffset(firmaX, firmaY);
                content.showText("_________________________");
                content.newLineAtOffset(40, -30);
                content.showText("Lcdo. Gustavo Curiel");
                content.newLineAtOffset(0, -20);
                content.showText("C.I.: V.-9.506.178");
                content.newLineAtOffset(0, -20);
                content.showText("Director del Plantel");
                content.endText();

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        doc.save(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}