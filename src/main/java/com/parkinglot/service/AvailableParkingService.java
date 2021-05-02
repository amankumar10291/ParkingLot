package com.parkinglot.service;

import com.parkinglot.WorkflowException;
import com.parkinglot.dao.ParkingLotDao;
import com.parkinglot.entity.ParkingLot;
import com.parkinglot.entity.ParkingSpace;
import com.parkinglot.entity.VehicleType;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class AvailableParkingService {

    private ParkingLotDao parkingLotDao = new ParkingLotDao();
    private HashMap<VehicleType, List<ParkingSpace>> freeSpaceMap = new HashMap<>();

    public AvailableParkingService(){
        if (freeSpaceMap.size() == 0) {
            freeSpaceMap = parkingLotDao.getAllAvailableParking();
        }
    }

    public List<ParkingSpace> getAvailableSpace(VehicleType type, Integer floor) {
        if (floor == null)
            return freeSpaceMap.get(type);
        else
            return freeSpaceMap.get(type).stream().filter(f -> f.getFloor() == floor).collect(Collectors.toList());
    }

    //Vehicle unparked increase free space
    public boolean increaseAvailableSpace(ParkingLot parkingLot) {
        if (parkingLot == null) {
            return false;
        }
        List<ParkingSpace> spaces = freeSpaceMap.get(parkingLot.getVehicle().getVehicleType());
        for (ParkingSpace parkingSpace : spaces) {
            if (parkingLot.getParkingSpace() != null
                    && parkingLot.getParkingSpace().getFloor() == parkingSpace.getFloor()) {
                parkingSpace.setParkingSpots(parkingSpace.getParkingSpots() + 1);
                break;
            }
        }
        freeSpaceMap.replace(parkingLot.getVehicle().getVehicleType(), spaces);
        return true;
    }

    //Vehicle parked decrease free space
    public boolean decreaseAvailableSpace(ParkingLot parkingLot) {
        if (freeSpaceMap.size() == 0) {
            throw new WorkflowException(400, "PARKING ALREADY FULL!!");
        }
        List<ParkingSpace> spaces = freeSpaceMap.get(parkingLot.getVehicle().getVehicleType());
        for (ParkingSpace parkingSpace : spaces) {
            if (parkingLot.getParkingSpace() != null
                    && parkingLot.getParkingSpace().getFloor() == parkingSpace.getFloor()) {
                parkingSpace.setParkingSpots(parkingSpace.getParkingSpots() - 1);
                break;
            }
        }
        freeSpaceMap.replace(parkingLot.getVehicle().getVehicleType(), spaces);
        return true;
    }
}
