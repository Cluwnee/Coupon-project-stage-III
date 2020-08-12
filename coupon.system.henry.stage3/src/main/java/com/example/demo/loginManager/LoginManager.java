package com.example.demo.loginManager;

import java.util.HashMap;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import com.example.demo.exceptions.WrongUserDetailsException;
import com.example.demo.facades.AdminFacade;
import com.example.demo.facades.ClientFacade;
import com.example.demo.facades.CompanyFacade;
import com.example.demo.facades.CustomerFacade;
import com.example.demo.web.OurSession;

@Service
public class LoginManager {

	private ConfigurableApplicationContext ctx;
	private HashMap<String, OurSession> sessions;
	private AdminFacade adminFacade;
	private CompanyFacade companyFacade;
	private CustomerFacade customerFacade;

	public LoginManager(ConfigurableApplicationContext ctx, HashMap<String, OurSession> sessions,
			AdminFacade adminFacade, CompanyFacade companyFacade, CustomerFacade customerFacade) {
		super();
		this.ctx = ctx;
		this.sessions = sessions;
		this.adminFacade = adminFacade;
		this.companyFacade = companyFacade;
		this.customerFacade = customerFacade;
	}

	public ClientFacade login(String email, String password, ClientType clientType) throws WrongUserDetailsException {

		switch (clientType) {
		case Administrator:
			AdminFacade af = ctx.getBean(AdminFacade.class);
			if (af.login(email, password))
				return af;
			else
				throw new WrongUserDetailsException();

		case Company:
			CompanyFacade cof = ctx.getBean(CompanyFacade.class);
			if (cof.login(email, password))
				return cof;
			else
				throw new WrongUserDetailsException();

		case Customer:
			CustomerFacade cuf = ctx.getBean(CustomerFacade.class);
			if (cuf.login(email, password))
				return cuf;
			else
				throw new WrongUserDetailsException();
		}
		return null;
	}

	public HashMap<String, OurSession> getSessions() {
		return sessions;
	}

	public void setSessions(HashMap<String, OurSession> sessions) {
		this.sessions = sessions;
	}

	public ConfigurableApplicationContext getCtx() {
		return ctx;
	}

	public AdminFacade getAdminFacade() {
		return adminFacade;
	}

	public CompanyFacade getCompanyFacade() {
		return companyFacade;
	}

	public CustomerFacade getCustomerFacade() {
		return customerFacade;
	}

}
