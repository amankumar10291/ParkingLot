package com.parkinglot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSpace {

    private int floor;
    private int amount;
    private int parkingSpots;
}
