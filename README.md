## REST API Service for Advertising Categories and Text Banners
This is a back-end service for a web application that allows users to manage advertising categories and text banners. Provides open access to obtaining an advertising banner unique for a certain date by matching categories from the database with the maximum price. This service provides REST API endpoints for creating, editing, and deleting objects related to advertising categories and text banners.

# Getting Started
To use this service, you will need to have a working understanding of REST APIs and be familiar with HTTP requests and responses. You will also need to have access to the server hosting the service.

# Installing
To install this service, simply clone the repository and run it on your server. You will also need to set up a database for storing advertising categories and text banners and configuration files for connecting to a local and test database.

# Endpoints
The following endpoints are available in this service:

The text of the advertising banner
* GET /bid?cat=cat1&cat=cat2&cat=cat3... : Returns the text of the advertising banner. Which corresponds to a set of categories, is unique to the user on the current day and has a maximum price.

Advertising Categories
* GET /category: Returns a list of all active advertising categories.
* GET /category/filter/{filter}: Returns a list of all active advertising categories that match the filter.
* GET /category/{id}: Returns the advertising category with the specified ID.
* POST /category: Creates a new advertising category. JSON in body.
* PUT /category/{id}: Updates the advertising category with the specified ID. JSON with updated data in body.
* DELETE /category/{id}: Deletes the advertising category with the specified ID. The category is not deleted from the database, but is marked as deleted.\
Advertising Categories JSON format:
```json
    {
        "name":"Name of category",
        "nameId":"Category Id"
    }
```
\
Text Banners
* GET /banner: Returns a list of all active text banners.
* GET /banner/{id}: Returns the text banner with the specified ID.
* GET /banner/filter/{filter}: Returns a list of all active text banners that match the filter.
* POST /banner: Creates a new text banner. JSON in body. The request parameters specify the id of the category to which the banner belongs
* PUT /banner/{id}: Updates the text banner with the specified ID. JSON with updated data in body.
* DELETE /banner/{id}: Deletes the text banner with the specified ID. The banner is not deleted from the database, but is marked as deleted.\
  Text Banners JSON format:
```json
    {
        "name":"Banner name",
        "text":"Banner text (will be shown to the user)",
        "price": 99.99
    }
```

# Authentication
This service requires authentication to access the all endpoints excluding the /bid endpoint it is available to all users. To authenticate, you will need to include a valid JWT token in the Authorization header of your requests.

# Error Handling
This service returns appropriate HTTP status codes and error messages for invalid requests. If you encounter an error while using the service, please refer to the error message for more information.
