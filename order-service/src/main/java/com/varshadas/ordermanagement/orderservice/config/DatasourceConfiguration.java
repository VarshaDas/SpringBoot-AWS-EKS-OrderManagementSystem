package com.varshadas.ordermanagement.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;



//    public class DatabaseCredentialsProvider {
//
//        public static DatabaseCredentials getDatabaseCredentials() {
//            AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard().build();
//            GetSecretValueRequest request = new GetSecretValueRequest()
//                    .withSecretId("arn:aws:secretsmanager:us-west-2:123456789012:secret:mydatabase-credentials-AbCdEf");
//            GetSecretValueResult result = client.getSecretValue(request);
//
//            String secretString = result.getSecretString();
//            // Parse secretString to extract username and password
//            // Example: {"username": "admin", "password": "secretpassword"}
//            // You'll need to implement the parsing logic
//
//            // Return database credentials
//            return new DatabaseCredentials(username, password);
//        }
//    }


// AWS Secrets Manager Logic (commented out for MySQL direct connection)
//import com.amazonaws.services.secretsmanager.*;
//import com.amazonaws.services.secretsmanager.model.*;

// Configuration class for setting up the MySQL DataSource
@Configuration
public class DatasourceConfiguration {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/inventory");
        //should encrypt
        dataSource.setUsername("");
        dataSource.setPassword("");
        return dataSource;
    }


}



