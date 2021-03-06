package resourceserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;


import java.security.KeyPair;

/**
 * Created by Izzy on 06/09/15.
 */
@Configuration
@EnableResourceServer // [2]
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServer extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "client_resource";

    @Value("${oauth.client.id}")
    private String clientId;

    @Value("${oauth.client.secret}")
    private String clientSecret;

    @Value("${security.oauth2.resource.jwt.keyValue}")
    private String pubKey;

    @Autowired
    private DefaultTokenServices tokenServices;

    @Bean
    public JwtTokenStore tokenStore() throws Exception{
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() throws Exception{
        JwtAccessTokenConverter jwtAccessTokenConverter =  new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setAccessTokenConverter(getDefaultAccessTokenConverter());
        jwtAccessTokenConverter.setVerifierKey(pubKey);
        jwtAccessTokenConverter.afterPropertiesSet();
        return jwtAccessTokenConverter;
    }

    @Bean
    AccessTokenConverter getDefaultAccessTokenConverter(){
        DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();
        defaultAccessTokenConverter.setUserTokenConverter(getCustomUserAuthenticationConvertor());
        return defaultAccessTokenConverter;
    }

    @Bean
    UserAuthenticationConverter getCustomUserAuthenticationConvertor(){
        return new CustomUserAuthenticationConvertor();
    }


    @Bean
    public DefaultTokenServices defaultTokenServices() throws Exception{
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenEnhancer(accessTokenConverter());
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }

    @Override // [3]
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().anyRequest().permitAll();
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

        resources
                .tokenServices(defaultTokenServices())
                .tokenStore(tokenStore())
                .resourceId(RESOURCE_ID);
    }


}

