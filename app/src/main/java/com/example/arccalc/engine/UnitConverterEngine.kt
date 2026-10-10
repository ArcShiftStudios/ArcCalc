package com.example.arccalc.engine

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.abs

data class ConversionUnit(
    val id: String,
    val name: String,
    val symbol: String,
    val toBase: (Double) -> Double,
    val fromBase: (Double) -> Double
)

data class UnitCategory(
    val id: String,
    val name: String,
    val units: List<ConversionUnit>,
    val defaultFrom: String,
    val defaultTo: String
)

object UnitConverterEngine {

    val categories: List<UnitCategory> = listOf(
        // 1. Length (Base: Meter)
        UnitCategory(
            id = "length",
            name = "Length",
            units = listOf(
                ConversionUnit("km", "Kilometer", "km", { it * 1000.0 }, { it / 1000.0 }),
                ConversionUnit("m", "Meter", "m", { it }, { it }),
                ConversionUnit("cm", "Centimeter", "cm", { it * 0.01 }, { it / 0.01 }),
                ConversionUnit("mm", "Millimeter", "mm", { it * 0.001 }, { it / 0.001 }),
                ConversionUnit("um", "Micrometer", "µm", { it * 1e-6 }, { it / 1e-6 }),
                ConversionUnit("nm", "Nanometer", "nm", { it * 1e-9 }, { it / 1e-9 }),
                ConversionUnit("mi", "Mile", "mi", { it * 1609.344 }, { it / 1609.344 }),
                ConversionUnit("yd", "Yard", "yd", { it * 0.9144 }, { it / 0.9144 }),
                ConversionUnit("ft", "Foot", "ft", { it * 0.3048 }, { it / 0.3048 }),
                ConversionUnit("in", "Inch", "in", { it * 0.0254 }, { it / 0.0254 }),
                ConversionUnit("nmi", "Nautical Mile", "nmi", { it * 1852.0 }, { it / 1852.0 })
            ),
            defaultFrom = "m",
            defaultTo = "ft"
        ),
        // 2. Weight / Mass (Base: Kilogram)
        UnitCategory(
            id = "weight",
            name = "Weight / Mass",
            units = listOf(
                ConversionUnit("t", "Metric Ton", "t", { it * 1000.0 }, { it / 1000.0 }),
                ConversionUnit("kg", "Kilogram", "kg", { it }, { it }),
                ConversionUnit("g", "Gram", "g", { it * 0.001 }, { it / 0.001 }),
                ConversionUnit("mg", "Milligram", "mg", { it * 1e-6 }, { it / 1e-6 }),
                ConversionUnit("ug", "Microgram", "µg", { it * 1e-9 }, { it / 1e-9 }),
                ConversionUnit("lb", "Pound", "lb", { it * 0.45359237 }, { it / 0.45359237 }),
                ConversionUnit("oz", "Ounce", "oz", { it * 0.028349523125 }, { it / 0.028349523125 }),
                ConversionUnit("st", "Stone", "st", { it * 6.35029318 }, { it / 6.35029318 }),
                ConversionUnit("ton_us", "US Ton", "US t", { it * 907.18474 }, { it / 907.18474 }),
                ConversionUnit("ton_uk", "Imperial Ton", "UK t", { it * 1016.0469088 }, { it / 1016.0469088 })
            ),
            defaultFrom = "kg",
            defaultTo = "lb"
        ),
        // 3. Temperature
        UnitCategory(
            id = "temperature",
            name = "Temperature",
            units = listOf(
                ConversionUnit("c", "Celsius", "°C", { it }, { it }),
                ConversionUnit("f", "Fahrenheit", "°F", { (it - 32.0) * 5.0 / 9.0 }, { it * 9.0 / 5.0 + 32.0 }),
                ConversionUnit("k", "Kelvin", "K", { it - 273.15 }, { it + 273.15 }),
                ConversionUnit("r", "Rankine", "°R", { (it - 491.67) * 5.0 / 9.0 }, { (it + 273.15) * 9.0 / 5.0 })
            ),
            defaultFrom = "c",
            defaultTo = "f"
        ),
        // 4. Volume (Base: Liter)
        UnitCategory(
            id = "volume",
            name = "Volume",
            units = listOf(
                ConversionUnit("l", "Liter", "L", { it }, { it }),
                ConversionUnit("ml", "Milliliter", "mL", { it * 0.001 }, { it / 0.001 }),
                ConversionUnit("m3", "Cubic Meter", "m³", { it * 1000.0 }, { it / 1000.0 }),
                ConversionUnit("gal_us", "Gallon (US)", "gal", { it * 3.785411784 }, { it / 3.785411784 }),
                ConversionUnit("qt_us", "Quart (US)", "qt", { it * 0.946352946 }, { it / 0.946352946 }),
                ConversionUnit("pt_us", "Pint (US)", "pt", { it * 0.473176473 }, { it / 0.473176473 }),
                ConversionUnit("cup_us", "Cup (US)", "cup", { it * 0.2365882365 }, { it / 0.2365882365 }),
                ConversionUnit("floz_us", "Fluid Ounce (US)", "fl oz", { it * 0.0295735295625 }, { it / 0.0295735295625 }),
                ConversionUnit("tbsp", "Tablespoon", "tbsp", { it * 0.01478676478125 }, { it / 0.01478676478125 }),
                ConversionUnit("tsp", "Teaspoon", "tsp", { it * 0.00492892159375 }, { it / 0.00492892159375 })
            ),
            defaultFrom = "l",
            defaultTo = "gal_us"
        ),
        // 5. Area (Base: Square Meter)
        UnitCategory(
            id = "area",
            name = "Area",
            units = listOf(
                ConversionUnit("km2", "Square Kilometer", "km²", { it * 1e6 }, { it / 1e6 }),
                ConversionUnit("m2", "Square Meter", "m²", { it }, { it }),
                ConversionUnit("cm2", "Square Centimeter", "cm²", { it * 0.0001 }, { it / 0.0001 }),
                ConversionUnit("ha", "Hectare", "ha", { it * 10000.0 }, { it / 10000.0 }),
                ConversionUnit("ac", "Acre", "ac", { it * 4046.8564224 }, { it / 4046.8564224 }),
                ConversionUnit("mi2", "Square Mile", "mi²", { it * 2589988.110336 }, { it / 2589988.110336 }),
                ConversionUnit("ft2", "Square Foot", "ft²", { it * 0.09290304 }, { it / 0.09290304 }),
                ConversionUnit("in2", "Square Inch", "in²", { it * 0.00064516 }, { it / 0.00064516 })
            ),
            defaultFrom = "m2",
            defaultTo = "ft2"
        ),
        // 6. Speed (Base: Meter/Second)
        UnitCategory(
            id = "speed",
            name = "Speed",
            units = listOf(
                ConversionUnit("ms", "Meter/second", "m/s", { it }, { it }),
                ConversionUnit("kmh", "Kilometer/hour", "km/h", { it / 3.6 }, { it * 3.6 }),
                ConversionUnit("mph", "Mile/hour", "mph", { it * 0.44704 }, { it / 0.44704 }),
                ConversionUnit("kn", "Knot", "kn", { it * 0.514444444 }, { it / 0.514444444 }),
                ConversionUnit("fts", "Foot/second", "ft/s", { it * 0.3048 }, { it / 0.3048 }),
                ConversionUnit("mach", "Mach (at sea lvl)", "Ma", { it * 340.29 }, { it / 340.29 })
            ),
            defaultFrom = "kmh",
            defaultTo = "mph"
        ),
        // 7. Time (Base: Second)
        UnitCategory(
            id = "time",
            name = "Time",
            units = listOf(
                ConversionUnit("yr", "Year (365d)", "yr", { it * 31536000.0 }, { it / 31536000.0 }),
                ConversionUnit("mo", "Month (30d)", "mo", { it * 2592000.0 }, { it / 2592000.0 }),
                ConversionUnit("wk", "Week", "wk", { it * 604800.0 }, { it / 604800.0 }),
                ConversionUnit("d", "Day", "d", { it * 86400.0 }, { it / 86400.0 }),
                ConversionUnit("h", "Hour", "h", { it * 3600.0 }, { it / 3600.0 }),
                ConversionUnit("min", "Minute", "min", { it * 60.0 }, { it / 60.0 }),
                ConversionUnit("s", "Second", "s", { it }, { it }),
                ConversionUnit("ms", "Millisecond", "ms", { it * 0.001 }, { it / 0.001 }),
                ConversionUnit("us", "Microsecond", "µs", { it * 1e-6 }, { it / 1e-6 })
            ),
            defaultFrom = "h",
            defaultTo = "min"
        ),
        // 8. Digital Data (Base: Byte)
        UnitCategory(
            id = "data",
            name = "Digital Data",
            units = listOf(
                ConversionUnit("b", "Bit", "b", { it / 8.0 }, { it * 8.0 }),
                ConversionUnit("B", "Byte", "B", { it }, { it }),
                ConversionUnit("KB", "Kilobyte (1000)", "KB", { it * 1000.0 }, { it / 1000.0 }),
                ConversionUnit("MB", "Megabyte", "MB", { it * 1e6 }, { it / 1e6 }),
                ConversionUnit("GB", "Gigabyte", "GB", { it * 1e9 }, { it / 1e9 }),
                ConversionUnit("TB", "Terabyte", "TB", { it * 1e12 }, { it / 1e12 }),
                ConversionUnit("PB", "Petabyte", "PB", { it * 1e15 }, { it / 1e15 }),
                ConversionUnit("KiB", "Kibibyte (1024)", "KiB", { it * 1024.0 }, { it / 1024.0 }),
                ConversionUnit("MiB", "Mebibyte", "MiB", { it * 1048576.0 }, { it / 1048576.0 }),
                ConversionUnit("GiB", "Gibibyte", "GiB", { it * 1073741824.0 }, { it / 1073741824.0 }),
                ConversionUnit("TiB", "Tebibyte", "TiB", { it * 1099511627776.0 }, { it / 1099511627776.0 })
            ),
            defaultFrom = "GB",
            defaultTo = "MB"
        ),
        // 9. Pressure (Base: Pascal)
        UnitCategory(
            id = "pressure",
            name = "Pressure",
            units = listOf(
                ConversionUnit("pa", "Pascal", "Pa", { it }, { it }),
                ConversionUnit("kpa", "Kilopascal", "kPa", { it * 1000.0 }, { it / 1000.0 }),
                ConversionUnit("bar", "Bar", "bar", { it * 100000.0 }, { it / 100000.0 }),
                ConversionUnit("psi", "Pound/sq inch", "psi", { it * 6894.757293168 }, { it / 6894.757293168 }),
                ConversionUnit("atm", "Atmosphere", "atm", { it * 101325.0 }, { it / 101325.0 }),
                ConversionUnit("mmhg", "Torr / mmHg", "mmHg", { it * 133.322387415 }, { it / 133.322387415 })
            ),
            defaultFrom = "bar",
            defaultTo = "psi"
        ),
        // 10. Energy (Base: Joule)
        UnitCategory(
            id = "energy",
            name = "Energy",
            units = listOf(
                ConversionUnit("j", "Joule", "J", { it }, { it }),
                ConversionUnit("kj", "Kilojoule", "kJ", { it * 1000.0 }, { it / 1000.0 }),
                ConversionUnit("cal", "Calorie", "cal", { it * 4.184 }, { it / 4.184 }),
                ConversionUnit("kcal", "Kilocalorie", "kcal", { it * 4184.0 }, { it / 4184.0 }),
                ConversionUnit("wh", "Watt-hour", "Wh", { it * 3600.0 }, { it / 3600.0 }),
                ConversionUnit("kwh", "Kilowatt-hour", "kWh", { it * 3.6e6 }, { it / 3.6e6 }),
                ConversionUnit("ev", "Electronvolt", "eV", { it * 1.602176634e-19 }, { it / 1.602176634e-19 }),
                ConversionUnit("btu", "BTU", "BTU", { it * 1055.05585262 }, { it / 1055.05585262 })
            ),
            defaultFrom = "kwh",
            defaultTo = "j"
        ),
        // 11. Power (Base: Watt)
        UnitCategory(
            id = "power",
            name = "Power",
            units = listOf(
                ConversionUnit("w", "Watt", "W", { it }, { it }),
                ConversionUnit("kw", "Kilowatt", "kW", { it * 1000.0 }, { it / 1000.0 }),
                ConversionUnit("mw", "Megawatt", "MW", { it * 1e6 }, { it / 1e6 }),
                ConversionUnit("hp", "Horsepower (hp)", "hp", { it * 745.699872 }, { it / 745.699872 }),
                ConversionUnit("ftlbpm", "Foot-pound/min", "ft·lb/min", { it * 0.0225969658 }, { it / 0.0225969658 })
            ),
            defaultFrom = "kw",
            defaultTo = "hp"
        ),
        // 12. Frequency (Base: Hertz)
        UnitCategory(
            id = "frequency",
            name = "Frequency",
            units = listOf(
                ConversionUnit("hz", "Hertz", "Hz", { it }, { it }),
                ConversionUnit("khz", "Kilohertz", "kHz", { it * 1000.0 }, { it / 1000.0 }),
                ConversionUnit("mhz", "Megahertz", "MHz", { it * 1e6 }, { it / 1e6 }),
                ConversionUnit("ghz", "Gigahertz", "GHz", { it * 1e9 }, { it / 1e9 }),
                ConversionUnit("rpm", "Revolutions/min", "RPM", { it / 60.0 }, { it * 60.0 })
            ),
            defaultFrom = "hz",
            defaultTo = "rpm"
        ),
        // 13. Angle (Base: Degree)
        UnitCategory(
            id = "angle",
            name = "Angle",
            units = listOf(
                ConversionUnit("deg", "Degree", "°", { it }, { it }),
                ConversionUnit("rad", "Radian", "rad", { Math.toDegrees(it) }, { Math.toRadians(it) }),
                ConversionUnit("grad", "Gradian", "grad", { it * 0.9 }, { it / 0.9 }),
                ConversionUnit("arcmin", "Arcminute", "arcmin", { it / 60.0 }, { it * 60.0 }),
                ConversionUnit("arcsec", "Arcsecond", "arcsec", { it / 3600.0 }, { it * 3600.0 })
            ),
            defaultFrom = "deg",
            defaultTo = "rad"
        )
    )

    fun convert(
        categoryId: String,
        fromUnitId: String,
        toUnitId: String,
        inputValue: Double
    ): Double {
        val category = categories.firstOrNull { it.id == categoryId } ?: return inputValue
        val fromUnit = category.units.firstOrNull { it.id == fromUnitId } ?: return inputValue
        val toUnit = category.units.firstOrNull { it.id == toUnitId } ?: return inputValue

        val baseValue = fromUnit.toBase(inputValue)
        return toUnit.fromBase(baseValue)
    }

    fun formatConvertedResult(value: Double): String {
        if (value.isNaN() || value.isInfinite()) return "0"
        val absVal = abs(value)
        val symbols = DecimalFormatSymbols(Locale.US).apply {
            decimalSeparator = '.'
            groupingSeparator = ','
        }
        return if (absVal != 0.0 && (absVal >= 1e12 || absVal < 1e-6)) {
            val df = DecimalFormat("0.######E0", symbols)
            df.format(value).replace("E", "e")
        } else if (value == Math.floor(value) && absVal < 1e12) {
            val df = DecimalFormat("#,##0", symbols)
            df.format(value)
        } else {
            val df = DecimalFormat("#,##0.########", symbols)
            df.format(value)
        }
    }
}
