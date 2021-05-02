package com.parkinglot.resource;

import com.parkinglot.WorkflowException;
import com.parkinglot.entity.ParkingLot;
import com.parkinglot.entity.ParkingSpace;
import com.parkinglot.entity.Vehicle;
import com.parkinglot.entity.VehicleType;
import com.parkinglot.service.AvailableParkingService;
import com.parkinglot.service.ParkingLotService;

import java.util.List;

public class ParkingManager {

    private AvailableParkingService availableParkingService = new AvailableParkingService();
    private ParkingLotService parkingLotService = new ParkingLotService();

    public List<ParkingSpace> getAvailability(VehicleType type,
                                              Integer floor) {
        return availableParkingService.getAvailableSpace(type, floor);
    }

    public ParkingLot parkVehicle(String license, VehicleType vehType, Integer floor) {
        ParkingLot parkingLot = new ParkingLot();
        try {
            Vehicle vehicle = new Vehicle();
            vehicle.setLicensePlate(license);
            vehicle.setVehicleType(vehType);

            parkingLot.setVehicle(vehicle);
            parkingLot.setParkingSpace(new ParkingSpace(floor, 0, 1));
            parkingLot = parkingLotService.park(parkingLot);

        } catch (WorkflowException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        return parkingLot;
    }


    public ParkingLot unpark(String license) {
        ParkingLot parkingLot = parkingLotService.unpark(license);
        return parkingLot;
    }
}
