package by.x1ss.adapterservice.service;

import by.x1ss.adapterservice.DTO.ResponseList;

public interface AdapterService {
    ResponseList getAnswer(String clientIdentifier, Boolean isJuridical);
}
