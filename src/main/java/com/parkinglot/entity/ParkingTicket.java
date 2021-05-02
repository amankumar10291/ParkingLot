package com.parkinglot.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ParkingTicket {

    private Vehicle vehicle;
    private Date enteredAt;
    private Date exitAt;

}
