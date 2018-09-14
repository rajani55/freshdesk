# freshdesk
Using the fresh desk rest api:

Contact.java: A POJO class, instance variables maps to the json attributes of client representation from Fresh desk

ContactUpdateDetails.java: A POJO class which should be having mapping attributes of client that need to be updated

ContactIntegration.java is the main class which triggers the rest api call.
 -> It needs to be run with two pairs of a flag and a value. one for API key and the other for the domain of freshdesk
 
 java ContactIntegration -apikey apikeyvalue -domain domainvalue


 e.g., java ContactIntegration -apikey hgdbsfldgsffdj -domain xyzcompany
