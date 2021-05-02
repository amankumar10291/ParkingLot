package com.parkinglot.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ParkingLot {

    private Vehicle vehicle;
    private Date enteredAt;
    private Date exitAt;
    private ParkingSpace parkingSpace;

}
