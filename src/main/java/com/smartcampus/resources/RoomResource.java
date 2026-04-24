package com.smartcampus.resources;

import com.smartcampus.data.DataStore;
import com.smartcampus.models.Room;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    // GET all rooms
    @GET
    public Response getAllRooms() {
        List<Room> rooms = new ArrayList<>(DataStore.rooms.values());
        return Response.ok(rooms).build();
    }

    // POST create room
    @POST
    public Response createRoom(Room room) {
        DataStore.rooms.put(room.getId(), room);
        return Response.status(Response.Status.CREATED).entity(room).build();
    }

    // GET one room
    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Room not found\"}")
                    .build();
        }

        return Response.ok(room).build();
    }

    // DELETE room (IMPORTANT PART)
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // 🔥 THIS IS WHERE YOUR EXCEPTION WORKS
        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            throw new com.smartcampus.exceptions.RoomNotEmptyException("Room has active sensors");
        }

        DataStore.rooms.remove(roomId);
        return Response.noContent().build();
    }
}