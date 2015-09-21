package resourceserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * Created by Izzy on 06/09/15.
 */
@Configuration
@EnableResourceServer // [2]
public class ResourceServer extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "client_resource";

    @Value("${oauth.client.id}")
    private String clientId;

    @Value("${oauth.client.secret}")
    private String clientSecret;

    @Override // [3]
    public void configure(HttpSecurity http) throws Exception {
        http
                // Just for laughs, apply OAuth protection to only 2 resources
                .requestMatchers().antMatchers("/","/user/test").and()
                .authorizeRequests()
                .anyRequest().access("#oauth2.hasScope('read')"); //[4]
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(RESOURCE_ID);
    }


}

