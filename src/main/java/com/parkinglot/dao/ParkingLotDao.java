package com.parkinglot.dao;

import com.parkinglot.WorkflowException;
import com.parkinglot.entity.ParkingLot;
import com.parkinglot.entity.ParkingSpace;
import com.parkinglot.entity.Vehicle;
import com.parkinglot.entity.VehicleType;
import lombok.Data;

import java.util.*;

@Data
public class ParkingLotDao extends ParkingSpaceDao {

    private HashMap<String, ParkingLot> parkedMap = new HashMap<>();
    private HashMap<VehicleType, List<ParkingSpace>> parkedSpace = new HashMap<>();

    public ParkingLotDao() {
        super();
    }


    public List<ParkingSpace> getAvailableParking(VehicleType type) {
        if (parkedSpace == null || parkedSpace.size() == 0) {
            return getTotalParkingSpace(type);
        }
        List<ParkingSpace> usedParking = parkedSpace.get(type);
        List<ParkingSpace> totalParking = getTotalParkingSpace(type);
        List<ParkingSpace> freeSpace = new ArrayList<>();
        for (final ParkingSpace parkingSpace : totalParking) {
            ParkingSpace free = new ParkingSpace();
            Optional<ParkingSpace> spaceOptional = usedParking.stream()
                    .filter(f -> f.getFloor() == parkingSpace.getFloor())
                    .findAny();
            if (spaceOptional.isPresent()) {
                free.setAmount(parkingSpace.getAmount());
                free.setFloor(parkingSpace.getFloor());
                free.setParkingSpots(parkingSpace.getParkingSpots() - spaceOptional.get().getParkingSpots());
            }
            freeSpace.add(free);
        }

        return freeSpace;
    }

    public HashMap<VehicleType, List<ParkingSpace>> getAllAvailableParking() {
        if (parkedSpace == null || parkedSpace.size() == 0) {
            return getTotalParkingSpaceForAll();
        }
        HashMap<VehicleType, List<ParkingSpace>> freeSpace = new HashMap<>();
        for (VehicleType type : VehicleType.values()) {
            freeSpace.put(type, getAvailableParking(type));
        }
        return freeSpace;
    }


    public ParkingLot getParkedVehicleInfo(Vehicle vehicle) {
        return parkedMap.get(vehicle.getLicensePlate());
    }

    public boolean parkVehicle(ParkingLot parkingLot) {
        parkedMap.put(parkingLot.getVehicle().getLicensePlate(), parkingLot);
        List<ParkingSpace> parked = parkedSpace.get(parkingLot.getVehicle().getVehicleType());
        if (parked == null || parked.size() == 0) {
            parkedSpace.put(parkingLot.getVehicle().getVehicleType(), Collections.singletonList(parkingLot.getParkingSpace()));
            return true;
        }
        for (ParkingSpace parkingSpace : parked) {
            if (parkingSpace.getFloor() == parkingLot.getParkingSpace().getFloor()) {
                parkingSpace.setParkingSpots(parkingSpace.getParkingSpots() + 1);
                break;
            }
        }
        parkedSpace.replace(parkingLot.getVehicle().getVehicleType(), parked);
        return true;
    }

    public ParkingLot unpark(String license) {
        if(!parkedMap.containsKey(license)){
            throw new WorkflowException(400, "INVALID LICENSE NUMBER, VEHICLE NOT AVAILABLE!!");
        }
        ParkingLot parkingLot = parkedMap.get(license);
        parkedMap.remove(license);
        List<ParkingSpace> parked = parkedSpace.get(parkingLot.getVehicle().getVehicleType());
        if (parked.size() == 0) {
            throw new WorkflowException(400, "INVALID LICENSE NUMBER, VEHICLE NOT PARKED!!");
        }
        for (ParkingSpace parkingSpace : parked) {
            if (parkingSpace.getFloor() == parkingLot.getParkingSpace().getFloor()) {
                parkingSpace.setParkingSpots(parkingSpace.getParkingSpots() - 1);
                break;
            }
        }
        parkedSpace.replace(parkingLot.getVehicle().getVehicleType(), parked);
        return parkingLot;
    }
}
