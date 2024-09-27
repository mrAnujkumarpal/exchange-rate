package in.exchange.rate.service;

import java.util.concurrent.CompletableFuture;

import in.exchange.rate.domain.ExchangeRate;
import in.exchange.rate.domain.ShopPrice;
import in.exchange.rate.enums.DiscountCode;
import in.exchange.rate.util.Utility;

public class DiscountService {

	public static CompletableFuture<ShopPrice> applyDiscountAsync(ExchangeRate quote) {
		return CompletableFuture.supplyAsync(() -> applyDiscount(quote));
	}

	public static ShopPrice applyDiscount(ExchangeRate quote) {
		double discountedPrice = apply(quote.getPrice(), quote.getDiscountCode());
		Utility.randomDelay();
		return new ShopPrice(quote.getVendorName(), discountedPrice, quote.getCurrency());
	}

	private static double apply(double price, DiscountCode code) {
		return price * (100 - code.percentage) / 100;
	}
}
