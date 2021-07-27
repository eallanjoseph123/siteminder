# Email Service

This service will provide an API to send to different API providers, and the same time is able to use other API when some of the API provider is not available.


#Features
1. Unified Email provider for SENDING EMAILS.
<br/>
   1.1 Use Strategy pattern to build payload request for each Email API provider.
2. Job scheduler (not implemented yet , need extra time to implement) - This is a scheduler that flags the Email API provider as available (healthy) or not (unhealthy).
3. API DOC - using  SWAGGER to easily play with this API and to properly document the API.
4. Added basic validation for email and you might want to check this EmailMessageRequest class.

#Tech

1. Springboot
2. Mysql
3. Swagger
4. RestAPI
5. Docker (for db only)

#Accomplishments:
 ```  
1. Created Unit test for both Controller and Service.
2. Used lombok to remove boilerplate codes.
3. Used Swagger to document API.
4. It can handle multiple email api providers and failover to the other api provider if there's an issue.
5. flexbility of codes especially when building the required payload for each API provider.
 ```  

## Prerequisites

* [Java 8 Runtime](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
* [Docker](https://docs.docker.com/get-docker/) & [Docker-Compose](https://docs.docker.com/compose/install/)

*Note: Docker is used for the local MySQL database instance, feel free to use your own instance or any other SQL database.*



### Run

0. Build Spring boot and run unit tests <br/>
   NOTE: should be run under siteminder package.
   ```
   mvn clean install 
    ```
1. Start database through docker-composes<br/>
   NOTE: should be run under siteminder package.

    ```
    docker-compose up -d
    ```

2. RUN   NOTE: should be run under siteminder package.
   ```
   java -jar target/siteminder-0.0.1-SNAPSHOT.jar
   ```
   Database (siteminder) will be automatically created by the docker-compose if you accidentally deleted it, <br/>
   Please run the below commands that will recreate the image.

   ```
   STOP the JAVA APP then:
   
   0. docker-compose stop
   1. docker-compose rm
   2. y
   3. docker-compose up -d
   
   RUN AGAIN the JAVA APP #2.
   ```

3. Add test data API providers - execute this sql scripts to populate the api provider data.


    2.1. INSERT INTO siteminder.email_provider (id, url, status, access_token , name) VALUES (1, "https://api.nylas.com/send", "HEALTHY", "4zgAjN30f1hTJ88i4PdkcQp7055cLE","nylas")
    
    2.2. INSERT INTO siteminder.email_provider (id, url, status, client_id, client_secret , name) VALUES (2,"https://api.mailjet.com/v3.1/send", "HEALTHY", "fc9e435040e3181a2d17009cf83fdd8d","aeff8249612df93b3f4dbb45336f5eb7","mailjet");

 
4. Access SWAGGER API Doc to test the REST API
   ```
   http://localhost:9191/swagger-ui.html
   ``` 


#Issues:
 ``` 
1. Unfortunately SendGrid and mailgun  are not implemented because:
   1.1. SendGrid -> I need their support from customer service team to enable my account which i think is not best time to spend since this is only an exam.
   1.2. MailGun ->  They are requiring a domain name and did not have a time to check it.
``` 
``` 
2. This 2 email api providers are the easist to signup and use unlike the Sendgrid and Mailgun so in this exam these 2 are implemented:
  2.1. Mailjet -> It can send to any email as long as it registered in my account.
  2.2. nylas  -> for some reason it keeps showing an error even properly passing or sending required API request and the error says please contact their support.
                 I think the only good thing for this issue is we can test if this api provider is not ok then we should use the other email api provider.
``` 