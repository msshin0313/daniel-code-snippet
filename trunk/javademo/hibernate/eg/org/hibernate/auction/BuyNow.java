//$Id: BuyNow.java,v 1.1.2.1 2003/11/03 09:55:18 oneovthafew Exp $
package org.hibernate.auction;

/**
 * @author Gavin King
 */
public class BuyNow extends Bid {
	public boolean isBuyNow() {
		return true;
	}
	public String toString() {
		return super.toString() + " (buy now)";
	}
}
