package by.x1ss.adapterservice.web.dto;

import by.x1ss.adapterservice.domain.object.ResponseList;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class ResponseListDTO {
    List<ResponseDTO> responseList;

    public ResponseListDTO(ResponseList responseList) {
        this.responseList = responseList.getResponses().stream().map(ResponseDTO::new).collect(Collectors.toList());
    }
}
