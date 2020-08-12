package com.example.demo.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.beans.CategoryType;
import com.example.demo.beans.Coupon;
import com.example.demo.facades.CustomerFacade;
import com.example.demo.loginManager.LoginManager;

@RestController
@RequestMapping("customer")
@CrossOrigin(origins = "http://localhost:4200")
public class CustomerController {

	private LoginManager manager;

	public CustomerController(LoginManager manager) {
		super();
		this.manager = manager;
	}

	// Token Checker:
	// First check if the token exists and if it matches the client facade.
	// Second check the last activity of the logged in user.
	public CustomerFacade tokenCheck(String token) {
		OurSession session = manager.getSessions().get(token);
		if (manager.getSessions().containsKey(token) && session.getFacade() instanceof CustomerFacade) {
			long idleCheck = System.currentTimeMillis();
			if (idleCheck - session.getLastActivity() > 1800000) {
				manager.getSessions().remove(token);
				return null;
			}
			session.setLastActivity(System.currentTimeMillis());
			return (CustomerFacade) session.getFacade();
		} else {
			return null;
		}
	}

	@PostMapping("purchase/{token}")
	public ResponseEntity<?> purchaseCoupon(@PathVariable String token, @RequestBody Coupon coupon) {
		try {
			CustomerFacade cuf = tokenCheck(token);
			if (cuf != null) {
				cuf.purchaseCoupon(coupon);
				return ResponseEntity.ok("Coupon purchased successfully");
			} else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@GetMapping("find/all/coupons/{token}")
	public ResponseEntity<?> getCustomerCoupons(@PathVariable String token) {
		try {
			CustomerFacade cuf = tokenCheck(token);
			if (cuf != null)
				return ResponseEntity.ok(cuf.getCustomerCoupons());
			else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@GetMapping("find/all/coupons/category/{category}/{token}")
	public ResponseEntity<?> getCustomerCouponsByCategory(@PathVariable String token,
			@PathVariable CategoryType category) {
		try {
			CustomerFacade cuf = tokenCheck(token);
			if (cuf != null)
				return ResponseEntity.ok(cuf.getCustomerCouponsByCategory(category));
			else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@GetMapping("find/all/coupons/price/{maxPrice}/{token}")
	public ResponseEntity<?> getCustomerCouponsByPrice(@PathVariable String token, @PathVariable double maxPrice) {
		try {
			CustomerFacade cuf = tokenCheck(token);
			if (cuf != null)
				return ResponseEntity.ok(cuf.getCustomerCouponsByPrice(maxPrice));
			else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@GetMapping("info/{token}")
	public ResponseEntity<?> getCustomerDetails(@PathVariable String token) {
		CustomerFacade cuf = tokenCheck(token);
		if (cuf != null)
			return ResponseEntity.ok(cuf.getCustomerDetails());
		else
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user!");
	}

}
