package in.exchange.rate.service;

import in.exchange.rate.enums.Currency;
import in.exchange.rate.enums.DiscountCode;
import in.exchange.rate.util.Utility;

public class VendorService {

	public String getPrice(String product, String vendorName) {
		double price = determinePrice(product, vendorName);
		DiscountCode discountCode = determineDiscountCode(product, vendorName);
		Currency currency = Currency.valueOf("USD");
		Utility.randomDelay();
		return String.format("%s:%.2f:%s:%s", vendorName, price, discountCode, currency);
	}

	// Deterministically generate a price based on shop name and product name
	private double determinePrice(String product, String vendorName) {
		if (product.equals("NA"))
			throw new IllegalArgumentException("Product not available");
		// Generate price between 0 and 199.99
		int hash = (vendorName + product).hashCode();
		int dollars = hash % 200;
		int cents = (hash / 7) % 100;
		return Math.abs(dollars + cents / 100.0);
	}

	// Deterministically pick a discount code based on shop name and product name
	private DiscountCode determineDiscountCode(String product, String vendorName) {
		DiscountCode[] codes = DiscountCode.values();
		return codes[Math.abs((vendorName + product).hashCode()) % codes.length];
	}
}
