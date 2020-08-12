package com.example.demo.web;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.beans.Coupon;
import com.example.demo.db.CouponRepository;
import com.example.demo.exceptions.WrongUserDetailsException;
import com.example.demo.facades.AdminFacade;
import com.example.demo.facades.ClientFacade;
import com.example.demo.facades.CompanyFacade;
import com.example.demo.facades.CustomerFacade;
import com.example.demo.loginManager.ClientType;
import com.example.demo.loginManager.LoginManager;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {

	// We use our LoginManager to control logins and checks.
	// We use sessions HashMap to set up the tokens and logged in facade.
	private LoginManager manager;
	private Map<String, OurSession> sessions;
	private CouponRepository coupRepo;

	// Constructor instead of "@Autowire" to avoid errors, safer.
	public LoginController(LoginManager manager, Map<String, OurSession> sessions, CouponRepository coupRepo) {
		super();
		this.manager = manager;
		this.sessions = sessions;
		this.coupRepo = coupRepo;
	}

	// Login method tries to log in with email, password and the client type and
	// checks if it's actually someone in our data base.
	@PostMapping("login/{email}/{password}/{clientType}")
	public ResponseEntity<?> login(@PathVariable String email, @PathVariable String password, @PathVariable ClientType clientType) {
		ClientFacade facade = null;
		try {
			facade = manager.login(email, password, clientType);
			if (facade != null) {
				OurSession session = new OurSession();
				String token = UUID.randomUUID().toString();
				if (facade instanceof AdminFacade) {
					session.setFacade((AdminFacade) facade);
					session.setLastActivity(System.currentTimeMillis());
				} else if (facade instanceof CompanyFacade) {
					session.setFacade((CompanyFacade) facade);
					session.setLastActivity(System.currentTimeMillis());
				} else if (facade instanceof CustomerFacade) {
					session.setFacade((CustomerFacade) facade);
					session.setLastActivity(System.currentTimeMillis());
				} else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad details");
				}
				manager.getSessions().put(token, session);
				return ResponseEntity.ok(token);
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad details");
		} catch (WrongUserDetailsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage()); // If fail throw exception.The
																						// exception is the wrong login
																						// details from the LoginManager
		}
	}

	// Logout method.
	// Remove the token and send a message of logging out.
	@PostMapping("logout/{token}")
	public ResponseEntity<String> logout(@PathVariable String token) {
		sessions.remove(token);
		return ResponseEntity.ok("logged out");
	}

	// Method to return and show all coupons on the web
	@GetMapping("show")
	public List<Coupon> getAllCoupons() {
		return coupRepo.findAll();
	}

}
