## API Documentation

This document provides detailed information about the API endpoints for the train ticket booking microservice system.

## User Service (`userms`)

Base Path: `/v1/user`

### 1. User Login

*   **Description:** Authenticates a user based on their email and password.
*   **Endpoint:** `/loginUser`
*   **Method:** `POST`
*   **Request Parameters:**
    *   `userEmail` (String): The user's email address.
    *   `password` (String): The user's password.
*   **Usage:**

	```bash
	curl -X POST "http://localhost:8084/v1/user/loginUser?userEmail=user@example.com&password=password123"
	```

### 2. User Signup

*   **Description:** Registers a new user.
*   **Endpoint:** `/signupUser`
*   **Method:** `POST`
*   **Request Parameters:**
    *   `userEmail` (String): The user's email address.
    *   `password` (String): The user's password.
*   **Usage:**

	```bash
	curl -X POST "http://localhost:8084/v1/user/signupUser?userEmail=newuser@example.com&password=newpassword"
	```

### 3. Book a Ticket

*   **Description:** Books a train ticket for the user.
*   **Endpoint:** `/bookTicket`
*   **Method:** `POST`
*   **Request Parameters:**
    *   `trainPrn` (String): The train's Passenger Reservation Number.
    *   `source` (String): The source station.
    *   `destination` (String): The destination station.
    *   `dateOfTravel` (LocalDate): The date of travel (format: `YYYY-MM-DD`).
    *   `numberOfSeatsToBeBooked` (int): The number of seats to book.
*   **Usage:**

	```bash
	curl -X POST "http://localhost:8084/v1/user/bookTicket?trainPrn=12345&source=CityA&destination=CityB&dateOfTravel=2025-12-25&numberOfSeatsToBeBooked=2"
	```

### 4. Fetch All Tickets

*   **Description:** Retrieves all tickets booked by the logged-in user.
*   **Endpoint:** `/fetchTickets`
*   **Method:** `GET`
*   **Usage:**

	```bash
	curl -X GET "http://localhost:8084/v1/user/fetchTickets"
	```

### 5. Cancel a Ticket

*   **Description:** Cancels a previously booked ticket.
*   **Endpoint:** `/cancelTicket`
*   **Method:** `POST`
*   **Request Parameters:**
    *   `ticketId` (String): The ID of the ticket to cancel.
*   **Usage:**

	```bash
	curl -X POST "http://localhost:8084/v1/user/cancelTicket?ticketId=TICKET123"
	```

### 6. Fetch Ticket by ID

*   **Description:** Retrieves the details of a specific ticket.
*   **Endpoint:** `/fetchTicketById`
*   **Method:** `GET`
*   **Request Parameters:**
    *   `ticketId` (String): The ID of the ticket to fetch.
*   **Usage:**

	```bash
	curl -X GET "http://localhost:8084/v1/user/fetchTicketById?ticketId=TICKET123"
	```

### 7. Reschedule a Ticket

*   **Description:** Reschedules a ticket to a new date.
*   **Endpoint:** `/rescheduleTicket`
*   **Method:** `POST`
*   **Request Parameters:**
    *   `ticketId` (String): The ID of the ticket to reschedule.
    *   `updatedDateOfTravel` (LocalDate): The new date of travel (format: `YYYY-MM-DD`).
*   **Usage:**

	```bash
	curl -X POST "http://localhost:8084/v1/user/rescheduleTicket?ticketId=TICKET123&updatedDateOfTravel=2025-12-26"
	```

## Train Service (`trainms`)

Base Path: `/v1/train`

### 1. Search for Trains

*   **Description:** Searches for available trains based on source, destination, and travel date.
*   **Endpoint:** `/searchTrains`
*   **Method:** `GET`
*   **Request Parameters:**
    *   `source` (String): The source station.
    *   `destination` (String): The destination station.
    *   `travelDate` (LocalDate): The date of travel (format: `YYYY-MM-DD`).
*   **Usage:**

	```bash
	curl -X GET "http://localhost:8084/v1/train/searchTrains?source=CityA&destination=CityB&travelDate=2025-12-25"
	```

### 2. Add a New Train

*   **Description:** Adds a new train to the system.
*   **Endpoint:** `/addTrain`
*   **Method:** `POST`
*   **Request Body:** A JSON object representing the `Train` entity.
*   **Usage:**

	```bash
	curl -X POST -H "Content-Type: application/json" -d '{"trainName":"Express", ...}' "http://localhost:8084/v1/train/addTrain"
	```

### 3. Add Multiple Trains

*   **Description:** Adds multiple trains to the system.
*   **Endpoint:** `/addMultipleTrains`
*   **Method:** `POST`
*   **Request Body:** A JSON array of `Train` objects.
*   **Usage:**

	```bash
	curl -X POST -H "Content-Type: application/json" -d '[{"trainName":"Express1", ...}, {"trainName":"Express2", ...}]' "http://localhost:8084/v1/train/addMultipleTrains"
	```

### 4. Update Train Details

*   **Description:** Updates the details of an existing train.
*   **Endpoint:** `/updateTrain`
*   **Method:** `POST`
*   **Request Body:** A JSON object representing the updated `Train` entity.
*   **Usage:**

	```bash
	curl -X POST -H "Content-Type: application/json" -d '{"trainId":"TRAIN123", "trainName":"New Express", ...}' "http://localhost:8084/v1/train/updateTrain"
	```

### 5. Check if Train Can Be Booked

*   **Description:** Checks if a train is available for booking.
*   **Endpoint:** `/canBeBooked`
*   **Method:** `GET`
*   **Request Parameters:**
    *   `trainPrn` (String): The train's PRN.
    *   `source` (String): The source station.
    *   `destination` (String): The destination station.
    *   `travelDate` (LocalDate): The date of travel (format: `YYYY-MM-DD`).
*   **Usage:**

	```bash
	curl -X GET "http://localhost:8084/v1/train/canBeBooked?trainPrn=12345&source=CityA&destination=CityB&travelDate=2025-12-25"
	```

## Seat Management Service (`trainms`)

Base Path: `/v1/seats`

### 1. Book Seats

*   **Description:** Books a specified number of seats on a train.
*   **Endpoint:** `/book`
*   **Method:** `POST`
*   **Request Body:** A `BookTrainRequestDTO` object.
*   **Usage:**

	```bash
	curl -X POST -H "Content-Type: application/json" -d '{"userId":"USER123", "trainPrn":"12345", ...}' "http://localhost:8084/v1/seats/book"
	```

### 2. Free Booked Seats

*   **Description:** Frees up previously booked seats.
*   **Endpoint:** `/freeBookedSeats`
*   **Method:** `PUT`
*   **Request Body:** A `FreeBookedSeatsRequestDTO` object.
*   **Usage:**

	```bash
	curl -X PUT -H "Content-Type: application/json" -d '{"bookedSeatsList":[...], "trainPrn":"12345", ...}' "http://localhost:8084/v1/seats/freeBookedSeats"
	```

### 3. Book Seats (by number)

*   **Description:** Books a specific number of seats.
*   **Endpoint:** `/bookSeats`
*   **Method:** `POST`
*   **Request Parameters:**
    *   `trainPrn` (String): The train's PRN.
    *   `travelDate` (LocalDate): The date of travel (format: `YYYY-MM-DD`).
    *   `numberOfSeatsToBeBooked` (int): The number of seats to book.
*   **Usage:**

	```bash
	curl -X POST "http://localhost:8084/v1/seats/bookSeats?trainPrn=12345&travelDate=2025-12-25&numberOfSeatsToBeBooked=2"
	```

## Ticket Service (`ticketms`)

Base Path: `/v1/tickets`

### 1. Find Ticket by ID

*   **Description:** Retrieves a ticket by its ID.
*   **Endpoint:** `/{ticketId}`
*   **Method:** `GET`
*   **Path Variable:**
    *   `ticketId` (String): The ID of the ticket.
*   **Usage:**

	```bash
	curl -X GET "http://localhost:8084/v1/tickets/TICKET123"
	```

### 2. Fetch All Tickets by IDs

*   **Description:** Retrieves multiple tickets by their IDs.
*   **Endpoint:** `/fetchAllTickets`
*   **Method:** `GET`
*   **Request Parameters:**
    *   `ticketIds` (List<String>): A list of ticket IDs.
*   **Usage:**

	```bash
	curl -X GET "http://localhost:8084/v1/tickets/fetchAllTickets?ticketIds=TICKET123,TICKET456"
	```

### 3. Cancel a Ticket

*   **Description:** Deletes a ticket by its ID.
*   **Endpoint:** `/{ticketId}`
*   **Method:** `DELETE`
*   **Path Variable:**
    *   `ticketId` (String): The ID of the ticket to delete.
*   **Usage:**

	```bash
	curl -X DELETE "http://localhost:8084/v1/tickets/TICKET123"
	```

### 4. Create a New Ticket

*   **Description:** Creates a new ticket.
*   **Endpoint:** `/createTicket`
*   **Method:** `POST`
*   **Request Body:** A `TicketRequestDTO` object.
*   **Usage:**

	```bash
	curl -X POST -H "Content-Type: application/json" -d '{"userId":"USER123", ...}' "http://localhost:8084/v1/tickets/createTicket"
	```

### 5. Reschedule a Ticket

*   **Description:** Reschedules an existing ticket.
*   **Endpoint:** `/rescheduleTicket/{ticketId}`
*   **Method:** `PUT`
*   **Path Variable:**
    *   `ticketId` (String): The ID of the ticket to reschedule.
*   **Request Parameters:**
    *   `updatedTravelDate` (LocalDate): The new travel date (format: `YYYY-MM-DD`).
*   **Usage:**

	```bash
	curl -X PUT "http://localhost:8084/v1/tickets/rescheduleTicket/TICKET123?updatedTravelDate=2025-12-26"
	```

## Mail Service (`mailms`)

Base Path: `/v1/email`

### 1. Send Verification Email

*   **Description:** Sends a verification email to the user.
*   **Endpoint:** `/sendEmail`
*   **Method:** `POST`
*   **Request Parameters:**
    *   `email` (String): The recipient's email address.
*   **Request Body:** A `TicketRequestDTO` object.
*   **Usage:**

	```bash
	curl -X POST -H "Content-Type: application/json" -d '{"ticketId":"TICKET123", ...}' "http://localhost:8084/v1/email/sendEmail?email=user@example.com"
	```