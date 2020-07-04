package az.ibatech.todo.db.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.stream.IntStream;


@EnableWebMvc
@Configuration
public class AlexConfig implements WebMvcConfigurer {
//    private static final String PREFIX = "classpath:/static";
//    //for web mapping
//    private static final String[] WEB_MAPPINGS = {"/css/**","/img/**","/js/**"};
//    //for file system mappings
//    private static final String[] FILE_LOCATIONS = {"/css/","/img/","/js/"};
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        if(WEB_MAPPINGS.length != FILE_LOCATIONS.length)  throw new IllegalArgumentException(
//                "Config: Mapping size must be equal to the Location size");
//        IntStream.range(0,WEB_MAPPINGS.length)
//                .forEach(idx-> registry.addResourceHandler(WEB_MAPPINGS[idx])
//                        .addResourceLocations(PREFIX + FILE_LOCATIONS[idx]));
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/**")
                .addResourceLocations(
                        "classpath:/webapp/WEB-INF/jsp/",
                        "classpath:/templates",
                        "classpath:/static/img/",
                        "classpath:/static/css/",
                        "classpath:/static/js/",
                        "classpath:/static/"
                );
    }
}
