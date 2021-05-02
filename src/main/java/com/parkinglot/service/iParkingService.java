package com.parkinglot.service;

import com.parkinglot.entity.ParkingLot;
import com.parkinglot.entity.Vehicle;

public interface iParkingService {

    ParkingLot park(ParkingLot parkingLot);

    ParkingLot unpark(String license);
}
