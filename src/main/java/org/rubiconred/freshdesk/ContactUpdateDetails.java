package org.rubiconred.freshdesk;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ContactUpdateDetails {
	boolean view_all_tickets = true;


	public boolean getView_all_tickets() {
		return view_all_tickets;
	}

	public void setView_all_tickets(boolean view_all_tickets) {
		this.view_all_tickets = view_all_tickets;
	}
	

}
