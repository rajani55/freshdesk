package org.rubiconred.freshdesk;

import java.util.Base64;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;


public class ContactIntegration {

	public static String contactsRestEndpoint = "https://%s.freshdesk.com/api/v2/contacts"; 
	public static String specificContactRestEndpoint = "https://%s.freshdesk.com/api/v2/contacts/%s";
	public static String contactUpdateRestEndpoint = "https://%s.freshdesk.com/api/v2/contacts/%d";
    private static String apiKey ;
    private static String encodedApiKey ;
    private static String domain;
    
	static void setEncodedApiKey() {
		encodedApiKey=  Base64.getEncoder().withoutPadding().encodeToString(apiKey.getBytes());
	}
		
	static Contact fetchContact(Client client, String id) {
		WebTarget target = client.target(String.format(specificContactRestEndpoint, domain, id));
		Response response = target.request().header(HttpHeaders.AUTHORIZATION, encodedApiKey).get();
		return response.readEntity(Contact.class);
	}

	static Contact[] fetchContacts(Client client) {
		Response response =null;
		Contact[] contacts = null;
		contactsRestEndpoint = String.format(contactsRestEndpoint, domain);
		do {
			Contact[] contactsFromRes = null;
			WebTarget target = client.target(contactsRestEndpoint).queryParam("per_page", 100);
			response = target.request().header(HttpHeaders.AUTHORIZATION, encodedApiKey).get();
			if(response.getStatus() != 200) {
				System.out.println("Unsuccessful in Fetching contacts"+ response);
			}
			if(contacts == null) {
				contacts = response.readEntity(Contact[].class);
			}else {
				contactsFromRes = response.readEntity(Contact[].class);
				Contact[] contacts2 = new Contact[contacts.length + contactsFromRes.length];
				System.arraycopy(contacts, 0, contacts2, 0, contacts.length);
				System.arraycopy(contactsFromRes, 0, contacts2, contacts.length, contactsFromRes.length); 
				contacts = contacts2;
			}
			try {
				if(!response.getStringHeaders().containsKey("link")) {
					break;
				}
			}catch(Exception e) {
				break;
			}
			int commaIndex = response.getHeaderString("link").indexOf(">");
			contactsRestEndpoint = response.getHeaderString("link").substring(1, commaIndex);

		}while(true);

		return contacts;
	}
	static void updateContacts(Client client, Contact[] contacts) {
		for(int i =0;i<contacts.length;i++) {
			updateContact(client, contacts[i].getId());
		}
	}

	static void updateContact(Client client, Long id) {
		ContactUpdateDetails contactUpdateDetails = new ContactUpdateDetails();
		WebTarget target = client.target(String.format(contactUpdateRestEndpoint,domain, id));
		Entity<ContactUpdateDetails> entity = Entity.json(contactUpdateDetails);
		Response response = target.request().header(HttpHeaders.AUTHORIZATION, encodedApiKey).put(entity);
		if(response.getStatus() != 200) {
			System.out.println("Unsuccessful Update for Contact ID: "+id);
			System.out.println(response.readEntity(String.class));
		}
	}

	public static void main(String[] args) throws Exception {
		if(args!=null && args.length > 0 ) {
			for(int argsIndex=0;argsIndex<args.length;argsIndex++) {
				if(args[argsIndex].equalsIgnoreCase("-apiKey")){
					apiKey = args[argsIndex+1];
					setEncodedApiKey();
				}
				if(args[argsIndex].equalsIgnoreCase("-domain")){
					domain = args[argsIndex+1];
				}
			}
		}
		if(apiKey == null) {
			
			throw new Exception("apiKey is not supplied. Please provide in the format -apiKey <apiKeyValue>");
		}
		if(domain == null) {

			throw new Exception("domain is not supplied. Please provide in the format -domain <domainValue of Freshdesk>");
		}
		Client client = ClientBuilder.newClient();
		 /*** To enable Jersey server logging data ***/
		/*Client client = ClientBuilder.newBuilder()
				.property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_ANY)
	            .property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_CLIENT, "WARNING").build(); */
		Contact[] contacts = fetchContacts(client);
		System.out.println("Fetched Contacts:" + (contacts!=null?contacts.length:0));
		if(contacts!=null && contacts.length > 0) {
		 updateContacts(client, contacts);
		}
	}
}
