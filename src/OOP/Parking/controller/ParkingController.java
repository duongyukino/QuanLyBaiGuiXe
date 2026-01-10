package OOP.Parking.controller;

import OOP.Parking.model.PhuongTien;
import OOP.Parking.service.ParkingService;
import OOP.Parking.service.impl.ParkingServiceImpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class ParkingController {
    private final ParkingService parkingService;

    public ParkingController() throws IOException {
        this.parkingService = new ParkingServiceImpl();
    }

    public boolean xeVao(PhuongTien xe) {
        return parkingService.xeVao(xe);
    }

    public double xeRa(String bienSo) {
        return parkingService.xeRa(bienSo, LocalDateTime.now());
    }

    public List<PhuongTien> getXeTrongBai() {
        return parkingService.getXeTrongBai();
    }

    public int kiemTraChoTrong() {
        return parkingService.kiemTraChoTrong();
    }
}