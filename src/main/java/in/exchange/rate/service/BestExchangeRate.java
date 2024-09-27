package in.exchange.rate.service;

import in.exchange.rate.domain.ExchangeRate;
import in.exchange.rate.domain.ShopPrice;
import in.exchange.rate.domain.Vendor;
import in.exchange.rate.enums.Currency;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class BestExchangeRate {

	private final List<Vendor> mShops = Arrays.asList(new Vendor("BestShop"), new Vendor("WeRipYouOff"),
			new Vendor("Abriss GmbH"), new Vendor("IKEA"));

	// One thread for each shop (results in total exec time = exec time of 1 shop)
	private final Executor mExec = Executors.newFixedThreadPool(Math.min(mShops.size(), 100), runnable -> {
		Thread t = new Thread(runnable);
		t.setDaemon(true);
		return t;
	});

	public ShopPrice findPrice(String vendorName, String product) {
		String priceInfo = new VendorService().getPrice(product, vendorName);
		ShopPrice price = DiscountService.applyDiscount(ExchangeRate.parse(priceInfo));
		price.convertCurrency(ExchangeService.getUsdEur(), Currency.EUR);
		return price;
	}

	public Future<ShopPrice> findPriceAsync(String vendorName, String product) {
		return supplyAsync(() -> new VendorService().getPrice(product, vendorName)).thenApply(ExchangeRate::parse)
				.thenCompose(q -> supplyAsync(() -> DiscountService.applyDiscount(q)))
				.thenCombine(supplyAsync(() -> ExchangeService.getUsdEur()), (price, rate) -> {
					price.convertCurrency(rate, Currency.EUR);
					return price;
				});
	}

	public Stream<CompletableFuture<ShopPrice>> findAllPricesAsync(String product, String vendorName) {
		return mShops.stream()
				.map(vendor -> supplyAsync(() -> new VendorService().getPrice(product, vendorName), mExec))
				.map(f -> f.thenApply(ExchangeRate::parse))
				.map(f -> f.thenCompose(quote -> supplyAsync(() -> DiscountService.applyDiscount(quote), mExec)))
				.map(f -> f.thenCombine(supplyAsync(ExchangeService::getUsdEur, mExec), (price, rate) -> {
					price.convertCurrency(rate, Currency.EUR);
					return price;
				}));
	}
}
