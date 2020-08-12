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

import com.example.demo.beans.Company;
import com.example.demo.beans.Customer;
import com.example.demo.facades.AdminFacade;
import com.example.demo.loginManager.LoginManager;

@RestController
@RequestMapping("admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

	private LoginManager manager;

	public AdminController(LoginManager manager) {
		super();
		this.manager = manager;
	}

	// Token Checker:
	// First check if the token exists and if it matches the client facade.
	// Second check the last activity of the logged in user.
	public AdminFacade tokenCheck(String token) {
		OurSession session = manager.getSessions().get(token);
		if (manager.getSessions().containsKey(token) && session.getFacade() instanceof AdminFacade) {
			long idleCheck = System.currentTimeMillis();
			if (idleCheck - session.getLastActivity() > 1800000) {
				manager.getSessions().remove(token);
				return null;
			}
			session.setLastActivity(System.currentTimeMillis());
			return (AdminFacade) session.getFacade();
		} else {
			return null;
		}
	}

	// Create company.
	// If it fails, throw an exception from AdminFacade addCompany().
	@PostMapping("create/company/{token}")
	public ResponseEntity<?> addCompany(@PathVariable String token, @RequestBody Company company) {
		try {
			AdminFacade admin = tokenCheck(token);
			if (admin != null) {
				admin.addCompany(company);
				return ResponseEntity.ok(company);
			} else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	// Update company.
	// If it fails, throw an exception from the AdminFacade updateCompany().
	@PutMapping("update/company/{token}")
	public ResponseEntity<String> updateCompany(@PathVariable String token, @RequestBody Company company) {
		try {
			AdminFacade admin = tokenCheck(token);
			if (admin != null) {
				admin.updateCompany(company);
				return ResponseEntity.ok("Company updated successfully");
			} else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	// Delete company.
	// If it fails, throw an exception from AdminFacade deleteCompany().
	@DeleteMapping("delete/company/{id}/{token}")
	public ResponseEntity<String> deleteCompany(@PathVariable String token, @PathVariable int id) {
		try {
			AdminFacade admin = tokenCheck(token);
			if (admin != null) {
				admin.deleteCompany(id);
				return ResponseEntity.ok("Company deleted successfully");
			} else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	// Return all the companies through AdminFacade getAllCompanies().
	@GetMapping("find/all/companies/{token}")
	public ResponseEntity<?> getAllCompanies(@PathVariable String token) {
		AdminFacade admin = tokenCheck(token);
		if (admin != null)
			return ResponseEntity.ok(admin.getAllCompanies());
		else
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user!");
	}

	// Return one company.
	// If it fails, throw an exception from AdminFacade getOneCompany().
	@GetMapping("find/one/company/{id}/{token}")
	public ResponseEntity<?> getOneCompany(@PathVariable String token, @PathVariable int id) {
		try {
			AdminFacade admin = tokenCheck(token);
			if (admin != null) {
				return ResponseEntity.ok(admin.getOneCompany(id));
			} else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	// Create customer.
	// If it fails, throw an exception from AdminFacade addCustomer().
	@PostMapping("create/customer/{token}")
	public ResponseEntity<?> addCustomer(@PathVariable String token, @RequestBody Customer customer) {
		try {
			AdminFacade admin = tokenCheck(token);
			if (admin != null) {
				admin.addCustomer(customer);
				return ResponseEntity.ok(customer);
			} else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	// Update customer.
	// If it fails, throw an exception from the AdminFacade updateCustomer().
	@PutMapping("update/customer/{token}")
	public ResponseEntity<String> updateCustomer(@PathVariable String token, @RequestBody Customer customer) {
		try {
			AdminFacade admin = tokenCheck(token);
			if (admin != null) {
				admin.updateCustomer(customer);
				return ResponseEntity.ok("Customer updated successfully");
			} else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	// Delete customer.
	// If it fails, throw an exception from AdminFacade deleteCustomer().
	@DeleteMapping("delete/customer/{id}/{token}")
	public ResponseEntity<String> deleteCustomer(@PathVariable String token, @PathVariable int id) {
		try {
			AdminFacade admin = tokenCheck(token);
			if (admin != null) {
				admin.deleteCustomer(id);
				return ResponseEntity.ok("Customer deleted successfully");
			} else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	// Return all the customers through AdminFacade getAllCustomers().
	@GetMapping("find/all/customers/{token}")
	public ResponseEntity<?> getAllCustomers(@PathVariable String token) {
		AdminFacade admin = tokenCheck(token);
		if (admin != null)
			return ResponseEntity.ok(admin.getAllCustomers());
		else
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user!");
	}

	// Return one customer.
	// If it fails, throw an exception from AdminFacade getOneCustomer().
	@GetMapping("find/one/customer/{id}/{token}")
	public ResponseEntity<?> getOneCustomer(@PathVariable String token, @PathVariable int id) {
		try {
			AdminFacade admin = tokenCheck(token);
			if (admin != null) {
				return ResponseEntity.ok(admin.getOneCustomer(id));
			} else
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized user!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

}
