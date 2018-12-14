package hm.binkley.labs.overrides;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@Builder
@Data
public final class Example {
    @Id
    private Long id;
    private String name;
}
