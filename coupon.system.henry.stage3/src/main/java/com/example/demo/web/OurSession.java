package com.example.demo.web;

import org.springframework.stereotype.Component;

import com.example.demo.facades.ClientFacade;

@Component
public class OurSession {

	private long lastActivity;
	private ClientFacade facade;

	public OurSession() {
		super();
	}

	public OurSession(long lastActivity, ClientFacade facade) {
		super();
		this.lastActivity = lastActivity;
		this.facade = facade;
	}

	public long getLastActivity() {
		return lastActivity;
	}

	public void setLastActivity(long lastActivity) {
		this.lastActivity = lastActivity;
	}

	public ClientFacade getFacade() {
		return facade;
	}

	public void setFacade(ClientFacade facade) {
		this.facade = facade;
	}

	@Override
	public String toString() {
		return "OurSession [lastActivity=" + lastActivity + ", facade=" + facade + "]";
	}

}
