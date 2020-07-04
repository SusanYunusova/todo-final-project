package az.ibatech.todo.db.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.context.request.RequestContextListener;

@EnableOAuth2Sso
@Configuration
public class OAuth2Configuration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .antMatcher("/**")
//                .authorizeRequests()
//                .antMatchers("/", "/login**", "/webjars/**","/index","/addTask", "/error**")
//                .permitAll()
//                .anyRequest()
//                .authenticated();
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/login**",
                        "/webjars/**",
                        "/error**",
                        "/index**",
                        "/static/css/**",
                        "/static/js/**",
                        "/static/img/**"
                )

                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/css/*","/js/","/img/*").permitAll()
                .anyRequest()
                .authenticated();
    }

    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }
}
