package com.smartcampus.resources;

import com.smartcampus.data.DataStore;
import com.smartcampus.models.Sensor;
import com.smartcampus.models.Room;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    // GET /api/v1/sensors: List all sensors
    @GET
    public Response getAllSensors(@QueryParam("type") String type) {
        List<Sensor> sensorsList = new ArrayList<>(DataStore.sensors.values());
        
        if (type != null) {
            sensorsList.removeIf(sensor -> !sensor.getType().equalsIgnoreCase(type));
        }
        
        return Response.ok(sensorsList).build();
                }

    // POST /api/v1/sensors: Register a new sensor
    @POST
    public Response createSensor(Sensor sensor) {
        if (sensor.getId() == null || sensor.getId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"error\":\"Sensor ID is required\"}")
                           .build();
        }
        
        // Ensure the room exists before assigning the sensor to it
        if (sensor.getRoomId() != null) {
            Room room = DataStore.rooms.get(sensor.getRoomId());
            if (room == null) {
                throw new com.smartcampus.exceptions.LinkedResourceNotFoundException("Room does not exist");
            }
            // Add this sensor's ID to the room's list of sensors
            if (!room.getSensorIds().contains(sensor.getId())) {
                room.getSensorIds().add(sensor.getId());
            }
        }

        DataStore.sensors.put(sensor.getId(), sensor);
        return Response.status(Response.Status.CREATED).entity(sensor).build();
    }

    // DELETE /api/v1/sensors/{sensorId}: Remove a sensor
    @DELETE
    @Path("/{sensorId}")
    public Response deleteSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        // Remove the sensor from the associated room
        if (sensor.getRoomId() != null) {
            Room room = DataStore.rooms.get(sensor.getRoomId());
            if (room != null) {
                room.getSensorIds().remove(sensorId);
            }
        }
        
        // Remove the sensor's readings from the database
        DataStore.sensorReadings.remove(sensorId);
        // Finally, remove the sensor itself
        DataStore.sensors.remove(sensorId);
        
        return Response.noContent().build();
    }
}