package com.yaksh.ticketms.ticket.repository;

import com.yaksh.ticketms.ticket.model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TicketRepositoryV2 extends MongoRepository<Ticket,String> {
}
