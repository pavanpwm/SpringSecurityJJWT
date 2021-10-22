package sec.eci.poc.user;

import javax.persistence.Table;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Component
@Entity
@Table(name = "poc_user")
public class POCUser {
	
	@Id
	@Column(name = "id" )
	private String id;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "roles")
	private String roles;

	public POCUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	public POCUser(String id, String username, String password, String roles) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.roles = roles;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ArrayList<String> getRoles() {
		System.out.println(roles);
		return new ArrayList<String>(Arrays.asList(this.roles.split(",")));
	}

	public void addRole(String csvRoles) {
		if (roles.isEmpty()) {
			this.roles.trim();
			this.roles += csvRoles;
		}else {
			this.roles += "," + csvRoles;
		}
	}

	public boolean isPresent(String username) {
		if (this.username.equals(username)) {
			return true;
		}
		return false;
	}
	
	
	
	
	
	
	

	

}
