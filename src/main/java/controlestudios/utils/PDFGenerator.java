package controlestudios.utils;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import controlestudios.models.Estudiante;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class PDFGenerator {
    private static final float[] COLUMN_WIDTHS = {3, 1};
    private static final PdfFont TITLE_FONT;
    private static final PdfFont HEADER_FONT;
    private static final PdfFont BODY_FONT;

    static {
        try {
            TITLE_FONT = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD);
            HEADER_FONT = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA);
            BODY_FONT = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA);
        } catch (IOException e) {
            throw new RuntimeException("Error inicializando fuentes", e);
        }
    }

    public static void generarBoletin(Estudiante estudiante, Map<String, Double> promediosMaterias, double promedioGeneral) {
        String fileName = "boletines/Boletin_" + estudiante.getCedula() + ".pdf";
        new File("boletines").mkdirs();

        try (PdfWriter writer = new PdfWriter(new FileOutputStream(fileName));
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document document = new Document(pdfDoc, PageSize.A4)) {

            document.setMargins(40, 40, 50, 50);

            // Agregar logo
            URL imageUrl = PDFGenerator.class.getResource("/images/logo.png");
            assert imageUrl != null;
            ImageData imageData = ImageDataFactory.create(imageUrl);
            Image logo = new Image(imageData);
            logo.scaleToFit(120, 120);
            logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
            document.add(logo);

            // Encabezado institucional
            addInstitutionalHeader(document);

            // Título del documento
            Paragraph titulo = new Paragraph("BOLETÍN DE CALIFICACIONES\n\n")
                    .setFont(TITLE_FONT)
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(titulo);

            // Datos del estudiante
            addStudentInfo(document, estudiante, promedioGeneral);

            // Tabla de calificaciones
            addGradesTable(document, promediosMaterias);

            // Firma del director
            addDirectorSignature(document);

        } catch (FileNotFoundException e) {  // Manejo específico para archivo no encontrado
            System.err.println("Error: Archivo no encontrado");
        } catch (IOException e) {  // Manejo general para otros errores de E/S
            System.err.println("Error general de E/S");
        }
    }

    private static void addInstitutionalHeader(Document document) {
        Paragraph header = new Paragraph()
                .setFont(HEADER_FONT)
                .setFontSize(10)
                .add("REPÚBLICA BOLIVARIANA DE VENEZUELA\n")
                .add("MINISTERIO DEL PODER POPULAR PARA LA EDUCACIÓN\n")
                .add("UNIDAD EDUCATIVA NACIONAL CÉSAR AUGUSTO AGREDA\n\n")
                .setTextAlignment(TextAlignment.CENTER);
        document.add(header);
    }

    private static void addStudentInfo(Document document, Estudiante estudiante, double promedioGeneral) {
        Paragraph studentInfo = new Paragraph()
                .setFont(BODY_FONT)
                .setFontSize(12)
                .add("Nombre: " + estudiante.getNombreCompleto() + "\n")
                .add("Cédula: " + estudiante.getCedula() + "\n")
                .add("Sección: " + estudiante.getSeccion() + "\n")
                .add(String.format("Promedio General: %.2f\n\n", promedioGeneral));
        document.add(studentInfo);
    }

    private static void addGradesTable(Document document, Map<String, Double> promediosMaterias) {
        Table table = new Table(COLUMN_WIDTHS);
        table.setWidth(100);

        // Encabezados
        addTableHeaderCell(table, "MATERIA");
        addTableHeaderCell(table, "PROMEDIO");

        // Datos
        promediosMaterias.forEach((materia, promedio) -> {
            table.addCell(new Cell().add(new Paragraph(materia).setFont(BODY_FONT)));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f", promedio)).setFont(BODY_FONT)));
        });

        document.add(table);
    }

    private static void addTableHeaderCell(Table table, String text) {
        Cell cell = new Cell()
                .add(new Paragraph(text).setFont(BODY_FONT))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setTextAlignment(TextAlignment.CENTER);
        table.addCell(cell);
    }

    private static void addDirectorSignature(Document document) {
        Paragraph firma = new Paragraph()
                .add("\n\n\n_____________________________\n")
                .add("Lcdo. Gustavo Curiel\n")
                .add("Director del Plantel\n")
                .setTextAlignment(TextAlignment.RIGHT)
                .setFont(BODY_FONT);
        document.add(firma);
    }
}