package com.smartcampus.data;

import com.smartcampus.models.Room;
import com.smartcampus.models.Sensor;
import com.smartcampus.models.SensorReading;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {
    public static Map<String, Room> rooms = new HashMap<>();
    public static Map<String, Sensor> sensors = new HashMap<>();
    public static Map<String, List<SensorReading>> sensorReadings = new HashMap<>();

    static {
        rooms.put("LIB-301", new Room("LIB-301", "Library Quiet Study", 50));
    }
}