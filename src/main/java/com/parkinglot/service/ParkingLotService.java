package com.parkinglot.service;

import com.parkinglot.WorkflowException;
import com.parkinglot.dao.ParkingLotDao;
import com.parkinglot.entity.ParkingLot;

import java.util.Date;

public class ParkingLotService implements iParkingService {

    private AvailableParkingService availableParkingService = new AvailableParkingService();
    private ParkingLotDao parkingLotDao = new ParkingLotDao();

    public ParkingLotService() {

    }

    public ParkingLot park(ParkingLot parkingLot) {
        ParkingLot parkedVehicleInfo = parkingLotDao.getParkedVehicleInfo(parkingLot.getVehicle());
        if (parkedVehicleInfo != null) {
            throw new WorkflowException(400, "Vehicle already parked with license:" + parkingLot.getVehicle().getLicensePlate());
        }
        parkingLot.setEnteredAt(new Date());
        parkingLot.getParkingSpace().setAmount(
                parkingLotDao.getParkingFare(parkingLot.getVehicle().getVehicleType(), parkingLot.getParkingSpace().getFloor()));

        availableParkingService.decreaseAvailableSpace(parkingLot);
        boolean isParked = parkingLotDao.parkVehicle(parkingLot);
        if (!isParked) {
            availableParkingService.increaseAvailableSpace(parkingLot);
        }
        return parkingLot;
    }

    public ParkingLot unpark(String license) {
        ParkingLot parkingLot = parkingLotDao.unpark(license);
        if (parkingLot != null) {
            availableParkingService.increaseAvailableSpace(parkingLot);
        }
        parkingLot.setExitAt(new Date());
        int amountPerHr = parkingLotDao.getParkingFare(parkingLot.getVehicle().getVehicleType(), parkingLot.getParkingSpace().getFloor());
        int hrParked = getParkedHr(parkingLot.getEnteredAt().getTime(), parkingLot.getExitAt().getTime());
        parkingLot.getParkingSpace().setAmount(hrParked);
        return parkingLot;
    }

    private int getParkedHr(long time, long time1) {
        long diff = (time1 - time) / (1000 * 60 * 60);
        return (int) Math.ceil(diff);
    }
}
