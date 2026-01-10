package OOP.Parking.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class XeDapPricingStrategy implements PricingStrategy {
    private static final double PHI_GIO = 2000;
    private static final double PHI_NGAY = 10000;

    @Override
    public double calculateFee(LocalDateTime entryTime, LocalDateTime exitTime) {
        long gio = ChronoUnit.HOURS.between(entryTime, exitTime);
        if (gio <= 1) return PHI_GIO;
        if (gio <= 24) return PHI_NGAY;
        return PHI_NGAY * (gio / 24 + 1);
    }
}