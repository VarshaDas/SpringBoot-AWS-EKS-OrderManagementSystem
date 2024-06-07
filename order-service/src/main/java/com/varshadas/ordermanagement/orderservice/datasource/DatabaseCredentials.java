//package com.varshadas.ordermanagement.orderservice.datasource;
//
//import com.varshadas.ordermanagement.orderservice.DatabaseCredentials;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//
//import javax.sql.DataSource;
//
//import com.amazonaws.services.secretsmanager.*;
//import com.amazonaws.services.secretsmanager.model.*;
//
//
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
//
//    @Configuration
//    public class DatasourceConfiguration {
//
//        @Bean
//        public DataSource dataSource() {
//            // Fetch database credentials from AWS Secrets Manager
//            DatabaseCredentials credentials = DatabaseCredentialsProvider.getDatabaseCredentials();
//
//            // Configure datasource with fetched credentials
//            DriverManagerDataSource dataSource = new DriverManagerDataSource();
//            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//            dataSource.setUrl("jdbc:mysql://hostname:3306/database");
//            dataSource.setUsername(credentials.getUsername());
//            dataSource.setPassword(credentials.getPassword());
//
//            return dataSource;
//        }
//    }
//
//}
