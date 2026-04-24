package com.smartcampus.resources;

import com.smartcampus.data.DataStore;
import com.smartcampus.models.Sensor;
import com.smartcampus.models.SensorReading;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/sensors/{sensorId}/readings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    // GET /api/v1/sensors/{sensorId}/readings: Get all readings for a specific sensor
    @GET
    public Response getSensorReadings(@PathParam("sensorId") String sensorId) {
        // First check if the sensor actually exists
        if (!DataStore.sensors.containsKey(sensorId)) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("{\"error\":\"Sensor not found\"}")
                           .build();
        }
        
        List<SensorReading> readings = DataStore.sensorReadings.getOrDefault(sensorId, new ArrayList<>());
        return Response.ok(readings).build();
    }

    // POST /api/v1/sensors/{sensorId}/readings: Record a new reading
    @POST
    public Response recordReading(@PathParam("sensorId") String sensorId, SensorReading reading) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("{\"error\":\"Sensor not found. Cannot record reading.\"}")
                           .build();
        }
        
        if (sensor.getStatus().equalsIgnoreCase("MAINTENANCE")) {
            throw new com.smartcampus.exceptions.SensorUnavailableException("Sensor is under maintenance");
        }

        // Auto-assign the sensor ID and a timestamp so the user doesn't have to
        reading.setId(java.util.UUID.randomUUID().toString());
        reading.setTimestamp(System.currentTimeMillis());

        // Save the reading into our database (HashMap)
        DataStore.sensorReadings.putIfAbsent(sensorId, new ArrayList<>());
        DataStore.sensorReadings.get(sensorId).add(reading);

        return Response.status(Response.Status.CREATED).entity(reading).build();
    }
}