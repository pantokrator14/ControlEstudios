package controlestudios.utils;

import java.time.LocalDate;

public class PeriodoUtil {

    public static int obtenerMomentoActual(LocalDate fecha) {
        int mes = fecha.getMonthValue();

        if (mes >= 10 || mes <= 12) return 1;  // Oct-Dic
        if (mes >= 1 && mes <= 3) return 2;    // Ene-Mar
        if (mes >= 4 && mes <= 6) return 3;     // Abr-Jun

        return 0; // Periodo no escolar
    }

    public static boolean momentoHaIniciado(int momento) {
        LocalDate hoy = LocalDate.now();
        switch (momento) {
            case 1: return hoy.isAfter(LocalDate.of(hoy.getYear(), 9, 30));
            case 2: return hoy.isAfter(LocalDate.of(hoy.getYear(), 12, 31));
            case 3: return hoy.isAfter(LocalDate.of(hoy.getYear(), 3, 31));
            default: return false;
        }
    }

    public static int obtenerAnioEscolar() {
        LocalDate hoy = LocalDate.now();
        return (hoy.getMonthValue() >= 10) ? hoy.getYear() : hoy.getYear() - 1;
    }

    public static LocalDate[] obtenerFechasMomento(int momento, int anioEscolar) {
        LocalDate inicio = null;
        LocalDate fin = null;

        switch (momento) {
            case 1:
                inicio = LocalDate.of(anioEscolar, 10, 1);
                fin = LocalDate.of(anioEscolar, 12, 31);
                break;
            case 2:
                inicio = LocalDate.of(anioEscolar + 1, 1, 1);
                fin = LocalDate.of(anioEscolar + 1, 3, 31);
                break;
            case 3:
                inicio = LocalDate.of(anioEscolar + 1, 4, 1);
                fin = LocalDate.of(anioEscolar + 1, 6, 30);
                break;
        }

        return new LocalDate[]{inicio, fin};
    }

    public static boolean momentoExisteEnAnio(int momento, int anioEscolar) {
        // Solo validar existencia lógica (no requiere fechas reales)
        return momento >= 1 && momento <= 3;
    }

    public static boolean esMomentoFuturo(int momento, int anioEscolar) {
        int anioActual = obtenerAnioEscolar();
        int momentoActual = obtenerMomentoActual(LocalDate.now());

        if (anioEscolar > anioActual) return true;
        if (anioEscolar == anioActual && momento > momentoActual) return true;

        return false;
    }

    public static String obtenerNombrePeriodo(int momento, int anioEscolar) {
        String periodo = switch (momento) {
            case 1 -> "PRIMER MOMENTO";
            case 2 -> "SEGUNDO MOMENTO";
            case 3 -> "TERCER MOMENTO";
            default -> "";
        };

        return periodo + " (" + anioEscolar + "/" + (anioEscolar + 1) + ")";
    }

    public static int obtenerAnioEscolarActual() {
        LocalDate hoy = LocalDate.now();
        return (hoy.getMonthValue() >= 10) ? hoy.getYear() : hoy.getYear() - 1;
    }

    public static boolean esAnioEscolarActual(int anio) {
        return anio == obtenerAnioEscolarActual();
    }
}
