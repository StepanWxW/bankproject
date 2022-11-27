package ru.wxw.stepan.bankproject.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.wxw.stepan.bankproject.dto.ClientDTO;
import ru.wxw.stepan.bankproject.model.Client;
import ru.wxw.stepan.bankproject.repository.ClientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientService {
    private final ClientRepository clientRepository;
    @Autowired
    private final ModelMapper modelMapper;
    @Autowired
    public ClientService(ClientRepository clientRepository, ModelMapper modelMapper) {
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
    }
    @Transactional(readOnly = true)
    public List<ClientDTO> getAll() {
        return clientRepository.findAll()
                .stream()
                .map(p -> modelMapper.map(p, ClientDTO.class))
                .collect(Collectors.toCollection(ArrayList::new));
    }
    @Transactional(readOnly = true)
    public ClientDTO findById(Long id) {
        Client client = clientRepository.findById(id).orElse(null);
        return client != null
                ? modelMapper.map(client, ClientDTO.class)
                : null;
    }
}
