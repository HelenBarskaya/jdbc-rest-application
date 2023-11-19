package org.example.repository.mapping;

import org.example.dto.ClientDto;
import org.example.model.Client;
import org.mapstruct.Mapper;

@Mapper
public interface ClientMapper {

    Client dtoToEntity(ClientDto clientDto);

    ClientDto entityToDto(Client client);
}
