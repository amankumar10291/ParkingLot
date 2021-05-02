package com.parkinglot.dao;

import com.parkinglot.WorkflowException;
import com.parkinglot.entity.ParkingSpace;
import com.parkinglot.entity.VehicleType;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public abstract class ParkingSpaceDao {

    private HashMap<VehicleType, List<ParkingSpace>> parkingInfo = new HashMap<VehicleType, List<ParkingSpace>>();

    public ParkingSpaceDao() {
        List<ParkingSpace> spaces = new ArrayList<ParkingSpace>();
        spaces.add(new ParkingSpace(1, 100, 10));
        spaces.add(new ParkingSpace(2, 100, 10));
        parkingInfo.put(VehicleType.CAR, spaces);

        spaces = new ArrayList<ParkingSpace>();
        spaces.add(new ParkingSpace(1, 30, 10));
        spaces.add(new ParkingSpace(2, 30, 10));
        parkingInfo.put(VehicleType.MOTORBIKE, spaces);
    }

    public boolean addParkingSpace() {

        return true;
    }

    public List<ParkingSpace> getTotalParkingSpace(VehicleType type) {
        if (parkingInfo.containsKey(type)) {
            return parkingInfo.get(type);
        }
        throw new WorkflowException(404, "PARKING NOT AVAILABLE FOR TYPE: " + type);
    }

    public HashMap<VehicleType, List<ParkingSpace>> getTotalParkingSpaceForAll() {
        if (parkingInfo != null) {
            return parkingInfo;
        }
        throw new WorkflowException(404, "PARKING NOT AVAILABLE.");
    }

    public int getParkingFare(VehicleType type, Integer floor) {
        if (parkingInfo.size() == 0) {
            throw new WorkflowException(404, "PARKING FARE NOT CONFIGURED.");
        }
        return parkingInfo.get(type).stream().filter(f -> floor != null && f.getFloor() == floor).findFirst().get().getAmount();
    }
}
