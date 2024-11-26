Document Control Sheet
Current Version	0.4
Project Code	NS_SBI_04_08_2024
Project Name	SBI ePay Implementation
Document Type	 ePay SDK
Author	 Binoy Medhi / Faizan Pinjari
First Reviewed By	 Ranu Jain
Final Reviewed By	
 Approved By	
Contact Person	Binoy Medhi / Faizan Pinjari
Frequency of Document Review	Next Revision of Standard Documents
Document Creation Date	26-08-2024
Last Updated Date	5-10-2024

Revision History
Version	Date	Name	Comments
0.1	26-08-2024	Binoy Medhi	Draft Version
0.2	29-08-2024	Binoy Medhi	Incorporated feedback received from Neeraj
0.4	5-10-2024	Ranu Jain	Synch up with FSD version 0.4
0.5	15-10-2024	Ranu Jain	Add the configuration with Payment Service








Document Purpose
The purpose of this document is to outline the design for Transaction. This will include a view of the high-level architecture and the breakdown of the internal subsystems. UML class and sequence diagrams will be provided to show how the system will be put together and how data will flow through the system.

Functional Specification Mapping
TD	FS	 BRD Details
TD_TX 1	FS_TX 1	Token Generation
TD_TX 2	FS_TX 2	Merchant Customer
TD_TX 3	FS_TX 3	Merchant Order 
TD_TX 4	FS_TX 4	Merchant Transaction Initiation and Payment Flow
TD_TX 4	FS_TX 4	Merchant Transaction Initiation and Payment Flow



Technical Design 
⦁	Technology Stack
Dependencies	Version
Java	21.x.x
Gradle	8.9
Spring Boot	3.3.x
Spring Schedular	3.3.x
Spring Security	6.3.x
Swagger (API Documentation)	3.1.x
JUnit	5
Redis	7.x
Liquibase	4.29
Slf4j	1.7.x
Oracle Driver	19.x
Apache Kafka	2.7.X


⦁	Generic Class Flow 
 
⦁	Generic Header
{
   [HTTP METHOD TYPE] [API_END_POINT] 
   HTTP/1.1 Host: [HOST NAME]
   Authorization: Bearer <your-access-token>
   API-Key: <your-api-key> 
   Content-Type: application/json 
   Accept: application/json 
   User-Agent: [CLIENT_DETAILS]
   Request-ID: [Unique Identifier for Request to track]
   Origin: [Request Originator]
}
⦁	
⦁	Generic Request body
{
 "fieldName":"value",
}
⦁	
⦁	Generic Response body
{
 "data": [GENERIC_RESPONSE_OBJECT],
 “count”: Total Record Count for data Object,
 “size”: Total Record Count in current data Object,
 "errors": [
      {
         "errorCode": "ERROR_CODE",
         "errorMessage":"ERROR_MSG",
         "reason":"ERROR_REASON"
      },
      {
         "errorCode": "ERROR_CODE",
         "errorMessage": "ERROR_MSG",
         "reason":"ERROR_REASON"
      }
     ],
 "status": "STATUS_CODE"
}
⦁	
⦁	Generic Error Codes
Error Code	 Error Description
 1001	{field Name} is mandatory.
 1002	{field Name} is invalid. {Reason}
 1003	{field Name} is not found.
 1004	{field Name} is already present.
 1005	{field Name} is not active.
 1006	{field Name} state not accurate.

Http Error code	 Description
 401	Unauthorized
 403	Role Access denied (Forbidden)
 400	Bad Request

Error Reason	 Description
 Data	For Data Validation Errors
 Business	For Business Validation Errors
 Customer	For Customer Related Errors
 Technical	For System Related Errors

⦁	API Summary 
API Type	API Definition	API details
Transaction	Transaction Booking API 	Transaction booking API
 	Transaction Payment API	Initiate the payment for booked Transaction and generate SBI ePay Reference number (ATRN)
 	Charges API	Fetch Payment mode pricing on transaction page as per selection of merchant's customer
 	Customer GSTN API	Customer GSTN validation and submission.
 	Transaction Status API	Provide the Transaction Status by passing the Transaction Reference Number
Merchant	Create Customer	Merchant can create the customer.
 	Customer Deletion API	Merchant can delete the customer details, it will be soft delete
 	Customer Retrieval API with search(filter) or without	Merchant can retrieve customer details based on any search criteria like (customer Id, name, creation Date, active, address etc.), If search criteria is not passed then all customer information will come for that merchant.

It's a paginated API.
 	Create Order	Merchant can create the order for making payment.
 	Order Retrieval API with search(filter) or without	Merchant can retrieve order details based on any search criteria like (customer Id, Order Number, Order Ref Number, Status, creation Date, address etc.), If search criteria is not passed then all Order information will come for that merchant.

It's a paginated API.
 	Transaction Retrieval API with search(filter) or without	Merchant can retrieve Transaction details based on any search criteria like (customer Id, Order Number, Order Ref Number, ATRN Number, Status, creation Date, address etc.), If search criteria is not passed then all Transaction information will come for that merchant.

It's a paginated API.
 	Transaction Refund API	Merchant can initiate refund based on transaction reference number (ATRN)
 	Transaction Cancelled API	Merchant can initiate Transaction cancelation based on transaction reference number (ATRN)
Rule 	Rules APIs	This API will check the rule as per module (Customer Creation, Order Creation, Transaction Booking, Payment) and merchant configuration data wise 



⦁	 Generic Class Definition	
Class Name	  Class Definition
Controller	The controller acts as a mediator between the client and the backend application logic, ensuring that HTTP requests are properly handled, business logic is appropriately delegated, and responses are returned in a structured and meaningful way. Core responsibility of controller class will be: 
⦁	Handling http Request 
⦁	Data Binding and Request Validation
⦁	Delegate the request to Service layer
⦁	Sending back Http Response
⦁	API Documentation
⦁	Api Versioning
⦁	Role Based Access 
⦁	Exception Handlers
Service	A Service class encapsulating the business logic of the application. The service layer is where the core business rules and processes are implemented. This layer acts as an intermediary between the controller layer and the data access layer (repository).
⦁	Centralizing Business Rules
⦁	Business-Oriented Methods
⦁	Data Aggregation
⦁	Data Transformation
⦁	Implementing Business Workflows
⦁	Handling Business Exceptions
⦁	Validating Business Rules
⦁	Integration with Third-Party Services
⦁	DAO Layer Connectivity
⦁	Caching Implementation
Validator	Validator classes are used to validate data input to ensure that it meets certain criteria before it is processed further. This validation could be applied to request object, data transfer objects (DTOs), or any other form of data that requires verification.
⦁	Field-Level Validation
⦁	Complex Rules
⦁	Custom Rules
⦁	Decoupling Validation for reusability 
⦁	Meaningful Error Messages
DAO	This layer promotes loose coupling and a clear separation of concerns between the business logic and data persistence logic
⦁	Database Interaction
⦁	Encapsulating SQL Queries
⦁	Transaction Management
⦁	Database Exceptions
⦁	Centralizing Data Logic
Repository	The repository pattern, particularly when implemented using Spring Data JPA, is a powerful way to manage data persistence in a Spring application. Repositories provide a clean and efficient interface for performing CRUD operations and executing queries, all while abstracting the complexities of the underlying database interactions
⦁	Reduction of Boilerplate Code
⦁	CRUD Operations
⦁	Query Methods
⦁	Pagination and Sorting
⦁	JPA Repository
Request Object	It Hold the Http Request Object and in Controller will binding the http Request in Java Request Object
Response Object	It is having the http response object which will be return by all the APIs
DTO Object	It is a Data Transfer Object which builds up from Request Object and business validation run on it and then builds the Entity object for DB Operation.
Entity Object	An Entity object represents a table in a relational database, and each instance of the entity corresponds to a row in that table. It is a bridge between the object-oriented world of the application and the relational database
⦁	Table Mapping
⦁	Relationship Management like One to One, One to Many, Many to One, Many to Many etc...
Logging	Configuration: The logging configuration is managed through a configuration file (e.g., log4j2.xml).
Log Rotation Mechanism: Rotating log files daily and keeping a history of the last 7 days (configurable) in server folders.
Log Message Format: [Timestamp] [Log Level] [Correlation Id] [Service Name] [Class Name] [Line Number] [Message]
Log Storage: Logs are aggregated and sent to a centralized logging system (e.g., ELK Stack) for easier management and analysis.
Log Retention Policy: Logs are retained for 30 days(configurable). Logs older than the retention period is automatically deleted to free up space.
Log Storage DB: All Transaction Request and Response logs will be stored in DB by some utility.
Log Filtering: A log Filter will be used to mask sensitive data like password and card numbers
Exception Handler	Application will leverage the powerful exception handling features provided by the Spring Framework, including @ExceptionHandler, @ControllerAdvice, and ResponseStatusException. These features will be used to implement a centralized error management system, ensuring that all exceptions are handled consistently and efficiently.
Exceptions in the application will be categorized into three main types:
⦁	Business Exceptions: Custom exceptions related to business logic, 
⦁	Validation Exceptions: Exceptions triggered by input validation and business validation failures
⦁	System Exceptions: Unforeseen exceptions like NullPointerException or database connectivity issues
Sensitive Data: Exception messages and error responses will be carefully crafted to avoid exposing stack traces or any internal system details to the end-user.
application.yaml	It's a configuration file that defines various settings and properties for the application. This file uses a hierarchical structure with keys and values defined in a tree-like format.
 
The application.yaml file will be created to manage different environment profiles (e.g., dev, test, prod). This approach ensures that each environment has its own configuration, reducing the risk of using incorrect settings in production. By separating configurations for different environments, we can easily customize behavior based on the environment, such as database connections, logging levels, and external service URLs.
 
This file has the section like application settings, server configuration, data source configuration, logging configuration, caching configuration, security configuration, application-level configuration






⦁	1. TD_TS 1 - Token Generation
1.1 Data Flow Diagram

 

1.2 Sequence Diagram
 
1.3 Class Diagram 
  
1.4 Generate Token API 
1.4.1 API Definition / Implementation
⦁	Endpoint: /v1/token/{requestType}
⦁	Method: POST
⦁	Description: Generates a JWT token for the specified requestType (Order and Transaction). This token will be used for further accessing the Customer, Order and Transaction functionality.
⦁	Request Headers
⦁	Content-Type: application/json
⦁	Merchant-API-Key: <Merchant API Key>
⦁	Merchant-Secret-Key: <Merchant Secret Key>
⦁	Request Parameter
⦁	order: Hash value of OrderID + MID                       
Properties
	Mandatory	 Description	DataType
(Length)
Merchant-API-Key	Yes (For request Type Order)	Merchant API Key which was generated during onboarding and shared with Merchant	Varchar2 (200)
Merchant-Secret-Key	Yes (For request Type Customer and Order)	Merchant Secret Key which was generated during onboarding and shared with Merchant	Varchar2 (200)


Path Parameter	 Yes (For request Type Transaction)	Hash value of OrderID + MID {This field is mandatory for Transaction Type}	Varchar2 (200)



⦁	Response: Success: 200 – Ok
{
   "data": [
      {
         "token":"<JWT Token>"
      }
   ],
   "status": 1
}


Properties	 Description
 token	JWT Token
⦁	Response: Error: 200 – Ok
               {
   "errors": [
      {
         "errorCode":"ERROR_CODE ",
         "errorMessage":"ERROR_MSG",
          "reason":"ERROR_REASON"
      },
    ],
   "status":0
}


Error Code	 Error Description
 Respective generic error code and description with field Name
                                           
1.4.2 Validation
1.4.2.1 Request Data Validation
⦁	Valid Request Type value will be customer, order and transaction.
⦁	Request Type is customer or Order then API key and secret key is mandatory in header.
⦁	Request Type is transaction then request parameter value order is mandatory.

1.4.2.1 Business Data Validation 
⦁	New Token Generation will be called only if 
⦁	No active token in system for request type.
⦁	Merchant is present and in active state.
⦁	Transaction token request - Order is present and not in failed or expired state.

1.4.3 Business Logic
⦁	Step1: Spring Security Filter – It will filter the request as whitelist and pass request to token controller.
⦁	Step2: Token controller – Request reach to controller.
⦁	Step3: Spring binding and Validation – It will bind the http request object into Token Request and Basic data validation will be run on Token Request as per given Request Type.
⦁	Step4: Token Service – Token controller calls the Token Service to generate token.
⦁	Step5: Token Dao
⦁	Request Type Customer or Order - Token Service call the Token Dao to fetch the Merchant Data 
⦁	Request Type Transaction - Token Service call the Token Dao to fetch the Order data for given hash value
⦁	Step6: Token Service
⦁	If respective value is found value, then call the validator class to validate data Request otherwise throw the Custom Exception.
⦁	Step7: Token Validator – Validate the Merchant / Order data as per Business Validation
⦁	Step8: Token DAO – Check if there is already any active Token is present and cannot expired in next 10 min (Configurable at Merchant or System Level)
⦁	Step9: Authentication Service – If there is no Token found then Generate the JWT token with Spring Security Framework with having MID, expiry time, request Type value in it.
⦁	Step10:  Token DAO – Save token information in DB
⦁	Step 11: Token Service – Push the message to Notification Queue as per configuration has been set up at Merchant level. 
⦁	Step 12: Token Service - Share the response to caller.

1.4.4 Information
⦁	Spring Security: 
⦁	Spring Security: Provides authentication and authorization features, including JWT token handling.
⦁	Set up JWT filter to intercept requests.
⦁	Validate JWT token using JwtTokenUtil.
⦁	If valid, extract user details and set them in the security context.
⦁	If invalid, reject the request with an unauthorized response.

⦁	Auditing:
⦁	Log Request: Log all incoming requests, including headers and body parameters (excluding sensitive information) securely to the database / ELK stack as per the need.
⦁	Log Response: Log all responses, including generated token details and any error messages except the token.
⦁	Database: Ensure each transaction token request logs include a mapping with the orderID and merchant Id.
1.5 Invalidate Token API 
1.5.1 API Definition / Implementation
⦁	Endpoint: /v1/token/invalidate
⦁	Method: POST
⦁	Description: Invalidates an existing JWT token for a merchant, typically used when a new token is requested.
⦁	Request Headers
⦁	Content-Type: application/json
⦁	Authorization: Bearer <JWT Token>
⦁	Response:  Success: 200 – Ok

{
   "data": [
      {
         "message":" Token invalidated successfully."
      }
   ],
   "status": "1"
}

Properties	 Description
 message	Message for successful invalidation of token

⦁	Response: Error: 200 – Ok
               {
   "errors": [
      {
         "errorCode":"ERROR_CODE ",
         "errorMessage":"ERROR_MSG",
          "reason":"ERROR_REASON"
      },
    ],
   "status":0
}

Error Code	 Error Description
 1501	Token is already Expired.


1.5.2 Validation  
1.5.2.1 Request Data Validation
⦁	Valid Token should be present in header
1.5.2.2 Business Validation
⦁	Extract the Token and get the MID and check active token is present or not
1.5.3 Business Logic
⦁	Step1: Spring Security Filter – It will filter the request and check if the token present passes the request to the token controller otherwise 401 error will throw.
⦁	Step2: Token controller – Request reach to controller and call the Token Service.
⦁	Step3: Token service – call the validator to check if an active token is present or not.
⦁	Step4: Token Dao – Mark Token as expired.
⦁	Step 5: Token Service – Push the message to Notification Queue as per configuration has been set up at Merchant level.
⦁	Step6: Token Service – Response back with message “Token Expired”. 
1.5.4 Information
⦁	Auditing:
⦁	Log Request: Log all incoming requests, including headers and body parameters (excluding sensitive information) securely to the database / ELK stack as per the need.
⦁	Log Response: Log all responses, including generated token details and any error messages except the token.
⦁	Database Mapping: Update audit logs to mark tokens as revoked with a reference to the orderID and merchant Id if applicable.

⦁	


⦁	2. TD_TS 2 - Merchant Customer
2.1 Data Flow Diagram

 


2.2. Sequence Diagram

 

2.3. Class Diagram

 

2.4. Merchant Customer Creation API 
2.4.1. API Definition / Implementation
⦁	Endpoint: /v1/customer
⦁	Method: POST
⦁	Description: Creates a new merchant customer 
⦁	Request Headers
⦁	Content-Type: application/json
⦁	 Authorization (String): Bearer <JWT Token> 
 Note:  Token generated for Customer Request Type
⦁	Request Body:
      { 
       “customerRequest”: String
       }

Properties	Data	 Description
 customerRequest	{
“mId”: string,
“name”: string,
“email”: string,
“phoneNumber”: string,
“gstin”: string,
“addressLine1”: string,
“addressLine2”: string,
“city”: string,
“state”: string,
“country”: string,
“pincode”: string,
}	Encrypted Message of Customer Request.
Encryption logic would be AES256-GCM-NOPADDING


Customer Request
Properties	Mandatory	Description	Data Type
MID	Yes	Merchant Id	Varchar2 (200)
Name	Yes	Customer Name	Varchar2 (200)
Email	Yes, If Phone Number Parameter is not provided	Customer Email	Varchar2 (200)

Phone Number	Yes, If Email parameter is not provided	Customer Phone Number	Varchar2 (50)

GST In	No	Customer GST Number	Varchar2 (200)
Address Line 1	No	Customer Full Address	Varchar2 (200)
Address Line 2	No	 	Varchar2 (200)
City	No	 	Varchar2 (50)
State	No	 	Varchar2 (50)
Country	No	 	Varchar2 (50)
Pin Code	No	 	Varchar2 (50)

⦁	Response: Success: 200 – Ok
{
   "data": [
      {
        “id”: UUID
        "customerId":"string",
       }
   ],
"status": 1,
"count": 1,
"size": 1
}
⦁	Response: Error: 200 – Ok
               {
   "errors": [
      {
         "errorCode":"ERROR_CODE ",
         "errorMessage":"ERROR_MSG",
          "reason":"ERROR_REASON"
      },
    ],
   "status":0
}

Error Code	 Error Description
1501	Unique Customer Id Generation Fail
 Respective generic error code and description with field Name

 2.4.2 Validation
2.4.2.1 Request Data Validation
⦁	Request should be able to decrypt.
⦁	Mandatory Data check. 
⦁	Data Format check for Email and PhoneNumber with regex.
⦁	Pincode should map with given City and State
2.4.2.2 Business Validation
⦁	Validate that the combination of Name, Email, and Phone Number is unique for the given Merchant ID.
⦁	Customer Id is uniquely created for Merchant.
2.4.3 Business Logic
⦁	Step1: Spring Security Filter – It will filter the request and check if the valid token is present, then passes the request to the controller otherwise 401 error will throw.
⦁	Step2: Customer controller – Request reach to controller and call the Service.
⦁	Step3: Customer Service – Call the Dao Layer and fetch Merchant Data from view and then call the Decryption Service by passing the request Object and Merchant Secret Key.
⦁	Step4: Decryption Service – Decrypt the request and create the Customer Request Object.
⦁	Step5: Customer Service – Call the Customer Dao to duplicate check for customer.
⦁	Step6: Customer Dao – Fetch the Customer Data from Customer Repository by Name, Email and Phone Number if present then pass the object.
⦁	Step7: Customer Service – If Customer present then pass the customer id to controller otherwise call the Dao layer to save the Customer Object.
⦁	Step8: Customer Dao – Generate the customer Id inside sync method with logic 
⦁	Prefix: Cust_ 
⦁	Value
⦁	Use a combination of random alphanumeric characters and follow with the prefix.
⦁	Use a secure random number generator to create a 16-character alphanumeric string.
⦁	Ensure the generated string does not follow a predictable pattern.
⦁	Fallback Logic: As we will have the unique constraint check for customer Id, so if data save failed due to this error System will retry to generate the customer id 3 times (system level configuration) time. If the system cannot generate, an error message is sent back to the caller.
⦁	Step9: Customer Repository – Save the Customer Object in DB
⦁	Step 10: Customer Service – Push the message to Notification Queue as per configuration has been set up at Merchant level.
⦁	Step11: Customer service – Received the Customer Object then pass the customer id to controller
2.5.4 Information
⦁	 Auditing:
⦁	Log Request: Log all incoming requests, including headers and body parameters (excluding sensitive information) securely to the database / ELK stack as per the need.
⦁	Log Response: Log all responses, including generated token details and any error messages except the token.


