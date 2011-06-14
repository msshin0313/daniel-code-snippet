//$Id: User.java,v 1.1.2.2 2003/11/03 09:40:42 oneovthafew Exp $
package org.hibernate.auction;

import java.util.List;
import java.util.Set;

/**
 * @author Gavin King
 */
public class User extends Persistent {
	private String userName;
	private String password;
	private String email;
	private Name name;
	private List bids;
	private Set auctions;
	
	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getUserName() {
		return userName;
	}

	public void setEmail(String string) {
		email = string;
	}

	public void setPassword(String string) {
		password = string;
	}

	public void setUserName(String string) {
		userName = string;
	}

	public Set getAuctions() {
		return auctions;
	}

	public List getBids() {
		return bids;
	}

	public void setAuctions(Set list) {
		auctions = list;
	}

	public void setBids(List list) {
		bids = list;
	}
	
	public String toString() {
		return userName;
	}

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

}
