package com.example.demo.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.beans.CategoryType;
import com.example.demo.beans.Coupon;
import com.example.demo.facades.CompanyFacade;
import com.example.demo.loginManager.LoginManager;

@RestController
@RequestMapping("company")
@CrossOrigin(origins = "http://localhost:4200")
public class CompanyController {

	private LoginManager manager;

	public CompanyController(LoginManager manager) {
		super();
		this.manager = manager;
	}

	// Token Checker:
	// First check if the token exists and if it matches the client facade.
	// Second check the last activity of the logged in user.
	public CompanyFacade tokenCheck(String token) {
		OurSession session = manager.getSessions().get(token);
		if (manager.getSessions().containsKey(token) && session.getFacade() instanceof CompanyFacade) {
			long idleCheck = System.currentTimeMillis();
			if (idleCheck - session.getLastActivity() > 1800000) {
				manager.getSessions().remove(token);
				return null;
			}
			session.setLastActivity(System.currentTimeMillis());
			return (CompanyFacade) session.getFacade();
		} else {
			return null;
		}
	}

	// Create coupon.
	// If it fails, throw an exception from CompanyFacade addCoupon().
	@PostMapping("create/coupon/{token}")
	public ResponseEntity<?> addCoupon(@PathVariable String token, @RequestBody Coupon coupon) {
		try {
			CompanyFacade cof = tokenCheck(token);
			if (cof != null) {
				cof.addCoupon(coupon);
				return ResponseEntity.ok(coupon);
			} else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	// Update coupon.
	// If it fails, throw an exception from CompanyFacade updateCoupon().
	@PutMapping("update/coupon/{token}")
	public ResponseEntity<String> updateCoupon(@PathVariable String token, @RequestBody Coupon coupon) {
		try {
			CompanyFacade cof = tokenCheck(token);
			if (cof != null) {
				cof.updateCoupon(coupon);
				return ResponseEntity.ok("Coupon updated successfully");
			} else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	// Delete coupon.
	// If it fails, throw an exception from CompanyFacade deleteCoupon().
	@DeleteMapping("delete/coupon/{id}/{token}")
	public ResponseEntity<String> deleteCoupon(@PathVariable String token, @PathVariable int id) {
		try {
			CompanyFacade cof = tokenCheck(token);
			if (cof != null) {
				cof.deleteCoupon(id);
				return ResponseEntity.ok("Coupon deleted successfully");
			} else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	// Return all the company's coupons through CompanyFacade getCompanyCoupons().
	// Added throw because we work with an object that may throw null exception.
	@GetMapping("find/all/coupons/{token}")
	public ResponseEntity<?> getAllCoupons(@PathVariable String token) {
		try {
			CompanyFacade cof = tokenCheck(token);
			if (cof != null)
				return ResponseEntity.ok(cof.getCompanyCoupons());
			else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	// Get one coupon by ID.
	// if it fails, throw an exception from CompanyFacade getOneCompanyCoupon().
	@GetMapping("find/one/coupon/{id}/{token}")
	public ResponseEntity<?> getOneCoupon(@PathVariable String token, @PathVariable int id) {
		try {
			CompanyFacade cof = tokenCheck(token);
			if (cof != null) {
				return ResponseEntity.ok(cof.getOneCompanyCoupon(id));
			} else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	// Get company coupons by category.
	// No need to enter company because it is taken care of by the facade and login.
	@GetMapping("find/all/coupons/category/{category}/{token}")
	public ResponseEntity<?> getAllCouponsbyCategory(@PathVariable String token, @PathVariable CategoryType category) {
		CompanyFacade cof = tokenCheck(token);
		if (cof != null)
			return ResponseEntity.ok(cof.getCompanyCouponsByCategory(category, cof.getCompanyDetails()));
		else
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user!");
	}

	// Get company coupons by max price.
	// No need to enter company because it is taken care of by the facade and login.
	@GetMapping("find/all/coupons/price/{maxPrice}/{token}")
	public ResponseEntity<?> getAllCouponsByPrice(@PathVariable String token, @PathVariable double maxPrice) {
		CompanyFacade cof = tokenCheck(token);
		if (cof != null)
			return ResponseEntity.ok(cof.getCompanyCouponsByPrice(maxPrice, cof.getCompanyDetails()));
		else
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user!");
	}

	@GetMapping("info/{token}")
	public ResponseEntity<?> getCompanyDetails(@PathVariable String token) {
		CompanyFacade cof = tokenCheck(token);
		if (cof != null)
			return ResponseEntity.ok(cof.getCompanyDetails());
		else
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user!");
	}
}
