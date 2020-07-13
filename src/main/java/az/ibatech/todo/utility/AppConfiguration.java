package az.ibatech.todo.utility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppConfiguration {
    private String host;
    private String port;
    private String mail;
}
