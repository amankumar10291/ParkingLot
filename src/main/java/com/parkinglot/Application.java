package com.parkinglot;

import com.parkinglot.entity.VehicleType;
import com.parkinglot.resource.ParkingManager;

import java.util.Scanner;

public class Application {

    public static void main(String[] args) {
        ParkingManager parkingManager = new ParkingManager();
        Scanner scanner = new Scanner(System.in);
        VehicleType vehicleType = null;

        while (true) {
            System.out.println("******************************************");
            System.out.println("1.Available Space\n2.Park\n3.Unpark\n4.Exit");
            System.out.println("Enter your option: ");
            int input = scanner.nextInt();
            switch (input) {
                case 1:
                    System.out.println("Provide Vehicle type: ");
                    vehicleType = VehicleType.valueOf(scanner.next().toUpperCase());
                    System.out.println("Parking Available at:");
                    System.out.println(parkingManager.getAvailability(vehicleType, null));
                    break;
                case 2:
                    if (vehicleType == null) {
                        System.out.println("ERROR: First check for free space!!");
                        break;
                    }

                    System.out.println("Enter Vehicle License no: ");
                    String license = scanner.next();
                    System.out.println("Enter floor to park: ");
                    Integer floor = scanner.nextInt();
                    System.out.println("PARKING_INFO: " + parkingManager.parkVehicle(license, vehicleType, floor));
                    vehicleType = null;
                    break;
                case 3:
                    System.out.println("Enter Vehicle License no: ");
                    String licensePlate = scanner.next();
                    System.out.println("CHECK_OUT_INFO: " + parkingManager.unpark(licensePlate));
                    break;
                default:
                    System.exit(1);
            }
        }
    }
}
