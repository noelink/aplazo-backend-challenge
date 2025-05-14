package com.aplazo.mx.creditlines.util;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.Period;

@Slf4j
public class DateCalculator {

    public static int calcularEdad(String fechaNacimientoStr) {

        LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoStr);

        LocalDate hoy = LocalDate.now();

        return Period.between(fechaNacimiento, hoy).getYears();
    }

}
