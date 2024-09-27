package in.exchange.rate.domain;

import in.exchange.rate.enums.Currency;
import in.exchange.rate.enums.DiscountCode;

public class ExchangeRate {

	private final String vendorName;
	private final double price;
	private final DiscountCode discountCode;
	private final Currency currency;

	public ExchangeRate(String shopName, double price, DiscountCode code, Currency currency) {
		this.vendorName = shopName;
		this.price = price;
		this.discountCode = code;
		this.currency = currency;
	}

	public static ExchangeRate parse(String str) {
		String[] split = str.split(":");
		return new ExchangeRate(split[0], Double.parseDouble(split[1]), DiscountCode.valueOf(split[2]),
				Currency.valueOf(split[3]));
	}

	public String getVendorName() {
		return vendorName;
	}

	public double getPrice() {
		return price;
	}

	public DiscountCode getDiscountCode() {
		return discountCode;
	}

	public Currency getCurrency() {
		return currency;
	}
}
