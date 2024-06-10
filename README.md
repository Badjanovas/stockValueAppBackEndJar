Welcome to StockValueApp, an application designed to help investors evaluate and manage stock valuations using multiple financial models.

Features:
  Stock Valuation Models
  Discounted Cash Flow (DCF):
    Calculate the intrinsic value of stocks based on future cash flow projections.
  Dividend Discount Model (DDM):
    Determine the intrinsic value of stocks using expected future dividends.
  Graham's Model:
    Evaluate stocks based on Benjamin Graham's valuation method, incorporating earnings and growth rates.

User Management:
  Registration and Authentication:
    Secure user registration and authentication system.

User Data Management:
  Update user details and view valuation history.

Caching:
  Enhanced Performance:
    Utilizes caching to store frequently accessed data, improving the overall performance and efficiency of the application.

Security:
  JWT Authentication: 
    Secure authentication and authorization using JSON Web Tokens. Custom Security Filters: Ensure that only authenticated users can access protected endpoints.

API Documentation:
  Swagger Integration:
    Comprehensive API documentation is provided through Swagger, making it easy to understand and interact with the endpoints.

Error Handling:
  Robust Exception Management:
    Handle exceptions gracefully and provide meaningful responses to the client.

Email Notifications:
  User Engagement:
    Send welcome emails to new users, enhancing communication and engagement.

Cross-Origin Resource Sharing (CORS):
  Front-End Integration:
    Configured to allow cross-origin requests, enabling seamless integration with front-end applications hosted on different domains.

Dependencies StockValueApp leverages several libraries and frameworks:
  Spring Boot Starter Data JPA: 
    For data persistence and ORM.
  Spring Boot Starter Web:
    To build web applications and RESTful services. 
  PostgreSQL:
    Database driver for PostgreSQL. 
  Lombok: 
    Reduces boilerplate code by generating getter, setter, and other methods at compile-time.
  Spring Boot Starter Test: 
    Provides testing infrastructure and utilities.
  Springdoc OpenAPI Starter WebMVC UI:
    For API documentation and interactive Swagger UI.
  Mockito:
    For mocking objects in tests. 
  JUnit:
    For unit testing. 
  Lettuce:
    For Redis support. 
  Spring Boot Starter Mail:
    For sending emails. 
  Spring Security:
    For authentication and authorization.
  JWT:
    For token-based authentication. 
  Commons Codec:
    For encoding and decoding utilities.
  Spring Boot Starter Cache: 
    For caching support.
