package com.pimp.domain;

import com.pimp.PimpRestApplication;
import com.pimp.config.MongoConfig;
import com.pimp.repositories.ClientDetailsRepository;
import com.pimp.services.CustomClientDetailsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PimpRestApplication.class, MongoConfig.class, CustomClientDetailsService.class, ClientDetailsRepository.class},
        properties = "db.port=37017")
public class InsertClientDocumentIT {

    @Autowired
    CustomClientDetailsService clientDetailsService;

   // @Ignore
    @Test
    public void testInsert() throws Exception {
        PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();

        ClientDocument clientDocument = new ClientDocument()
                .setAuthorities(Arrays.asList("USER"))
                .setClientId("angularClient")
                .setClientSecret(encoder.encode("secret123"))
                .setGrantTypes(Arrays.asList("password", "refresh_token"))
                .setScopes(Arrays.asList("user_actions"))
                .setResourceIds(Arrays.asList("PIMP_RESOURCE"));
        clientDetailsService.saveClientDetails(clientDocument);
    }
}