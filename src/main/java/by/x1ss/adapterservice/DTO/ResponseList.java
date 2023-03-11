package by.x1ss.adapterservice.DTO;

import by.x1ss.adapterservice.model.Response;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "responseList")
public class ResponseList {
    @XmlElement(name = "response")
    private List<Response> responses;
}
