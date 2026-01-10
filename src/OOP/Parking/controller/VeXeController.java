package OOP.Parking.controller;

import OOP.Parking.model.VeXe;
import OOP.Parking.service.ParkingService;
import OOP.Parking.service.impl.ParkingServiceImpl;

import java.io.IOException;
import java.util.List;

public class VeXeController {
    private final ParkingService parkingService;

    public VeXeController() throws IOException {
        this.parkingService = new ParkingServiceImpl();
    }

    public List<VeXe> getDanhSachVeThang() {
        return parkingService.getDanhSachVeThang();
    }

    public boolean dangKyVeThang(VeXe ve) {
        return parkingService.dangKyVeThang(ve);
    }

    public boolean giaHanVeThang(String maVe, int soThang) {
        return parkingService.giaHanVeThang(maVe, soThang);
    }

    public VeXe timVeThang(String maVe) {
        return parkingService.timVeThang(maVe);
    }
}