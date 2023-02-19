package by.x1ss.adapterservice.service;

import by.x1ss.adapterservice.model.Response;

public interface AdapterService {
    Response getAnswer(String clientIdentifier, Boolean isJuridical);
}
