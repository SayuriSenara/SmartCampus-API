# Smart Campus API

## Project Overview

This project is a RESTful API built using Java (JAX-RS). It manages rooms, sensors, and sensor readings.

## Technologies Used

* Java
* JAX-RS (Jersey)
* Maven
* GlassFish Server
* Postman

## How to Run

1. Open project in NetBeans
2. Run the project
3. Open browser or Postman
4. Use:
   http://localhost:8080/SmartCampusAPI/api/v1

## API Endpoints

### Rooms

* GET /rooms
* POST /rooms
* GET /rooms/{id}
* DELETE /rooms/{id}

### Sensors

* GET /sensors
* GET /sensors?type=CO2
* POST /sensors

### Sensor Readings

* GET /sensors/{id}/readings
* POST /sensors/{id}/readings

## Error Handling

* 409 → Room has sensors
* 422 → Room not found
* 403 → Sensor in maintenance
* 500 → Server error

## Sample Commands

Create Room:
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms -H "Content-Type: application/json" -d "{"id":"R1","name":"Room","capacity":30}"

Create Sensor:
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors -H "Content-Type: application/json" -d "{"id":"S1","roomId":"R1","type":"CO2","status":"ACTIVE"}"

