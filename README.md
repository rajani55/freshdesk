# freshdesk
Using the fresh desk rest api:

Contact.java is a POJO class, instance variables mapping to the json attributes of client representation from Fresh desk

ContactUpdateDetails.java is a POJO class, which should be having mapping attributes of client that need to be ContactUpdateDetails

ContactIntegration.java is the main class which triggers the rest api call.
 -> It needs to be run with two pairs of a flag and a value. one for API key and the other for the domain of freshdesk
    java ContactIntegration -apikey <apikey> -domain <domain>


    e.g., java ContactIntegration -apikey hgdbsfldgsffdj -domain xyzcompany
