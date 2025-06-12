package nl.robor.koord.units

public enum class DistanceUnit(internal val factor: Double, internal val symbol: String) {
    NANOMETERS(1e-9, "nm"),
    MICROMETERS(1e-6, "μm"),
    MILLIMETERS(1e-3, "mm"),
    CENTIMETERS(1e-2, "cm"),
    METERS(1.0, "m"),
    KILOMETERS(1e3, "km");
}