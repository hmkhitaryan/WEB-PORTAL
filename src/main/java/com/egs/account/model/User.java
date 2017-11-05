package com.egs.account.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.builder.EqualsBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "user")
public class User implements Serializable {

	private static final long serialVersionUID = -6424480067919514779L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String username;

	@JsonIgnore
	private String password;

	@Transient
	@JsonIgnore
	private String passwordConfirm;

	private String firstName;

	private String lastName;

	private String email;

	private Date dateRegistered;

	private String skypeID;

	@Column(name = "enabled")
	private boolean enabled;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	@JsonIgnore
	private Set<Role> roles;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<Catalog> catalogs;

	public User() {
		super();
		this.enabled = false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Date getDateRegistered() {
		return dateRegistered;
	}

	public void setDateRegistered(Date dateRegistered) {
		this.dateRegistered = dateRegistered;
	}

	public String getSkypeID() {
		return skypeID;
	}

	public void setSkypeID(String skypeID) {
		this.skypeID = skypeID;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<Catalog> getCatalogs() {
		return catalogs;
	}

	public void setCatalogs(Set<Catalog> catalogs) {
		this.catalogs = catalogs;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof User)) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		final User other = (User) obj;
		final EqualsBuilder builder = new EqualsBuilder();
		builder.append(this.firstName, other.firstName);
		builder.append(this.lastName, other.lastName);
		builder.append(this.username, other.username);
		builder.append(this.skypeID, other.skypeID);
		return builder.isEquals();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + firstName + ", lastName="
				+ lastName + ", username=" + username + "]";
	}
}
