package co.jordan.boldauthentic.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
public class UpdateDto {

    private String codeBar;
    private boolean state;

}
