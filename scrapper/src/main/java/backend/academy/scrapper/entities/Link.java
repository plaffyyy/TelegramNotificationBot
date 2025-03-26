package backend.academy.scrapper.entities;

import backend.academy.scrapper.utils.converters.JsonConverter;
import backend.academy.scrapper.utils.converters.StringListConverter;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "links")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotNull
    private String url;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "jsonb")
    @NotNull
    private List<String> tags;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "jsonb")
    @NotNull
    private List<String> filters;

    @Convert(converter = JsonConverter.class)
    @Column(columnDefinition = "jsonb")
    private JsonNode update;
}
