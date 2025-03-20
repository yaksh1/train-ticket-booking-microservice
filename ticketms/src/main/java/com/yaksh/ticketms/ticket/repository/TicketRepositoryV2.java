package com.yaksh.ticketms.ticket.repository;

import com.yaksh.ticketms.ticket.model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository interface for managing {@link Ticket} entities.
 * <p>
 * This interface extends the {@link MongoRepository} to provide CRUD operations
 * and additional query methods for the Ticket collection in the MongoDB database.
 * </p>
 *
 * @see Ticket
 * @see MongoRepository
 */
public interface TicketRepositoryV2 extends MongoRepository<Ticket, String> {
    // No additional methods are defined here; this interface inherits all CRUD operations
    // from the MongoRepository interface.
}