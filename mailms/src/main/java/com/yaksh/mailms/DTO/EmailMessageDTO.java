package com.yaksh.mailms.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessageDTO implements Serializable {
    private TicketRequestDTO ticket;
    private String email;
}

