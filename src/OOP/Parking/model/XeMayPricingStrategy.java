package OOP.Parking.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class XeMayPricingStrategy implements PricingStrategy {
    private static final double PHI_NGAY = 30000;

    @Override
    public double calculateFee(LocalDateTime entryTime, LocalDateTime exitTime) {
        long gio = ChronoUnit.HOURS.between(entryTime, exitTime);
        
        // Nếu gửi <= 24h -> Tính 1 ngày
        if (gio <= 24) {
            return PHI_NGAY;
        }
        
        // Nếu gửi > 24h -> Tính theo số ngày (làm tròn lên)
        // Ví dụ: 25h -> 2 ngày
        long soNgay = (gio / 24) + 1;
        return PHI_NGAY * soNgay;
    }
}