package in.exchange.rate;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import in.exchange.rate.domain.ShopPrice;
import in.exchange.rate.service.BestExchangeRate;
import in.exchange.rate.util.Utility;

@SpringBootApplication
public class ExchangeRateApplication {

	public static void main(String[] args) {

		String mProduct = "BestProduct"; // "NA"
		String vendorName = "";
		runFindAllPricesAsync(mProduct, vendorName);

	}

	public static void runFindAllPricesAsync(String mProduct, String vendorName) {
		Utility.print("> Calling findAllPricesAsync");
		long startTime = Utility.currentTime();
		Stream<CompletableFuture<ShopPrice>> fut = new BestExchangeRate().findAllPricesAsync(mProduct, vendorName);
		Utility.print("< findAllPricesAsync returns after " + (Utility.currentTime() - startTime) + " milliseconds");

		CompletableFuture[] futArr = fut
				.map(f -> f.thenAccept(price -> System.out
						.println(String.format("%s (%d milliseconds)", price, (Utility.currentTime() - startTime)))))
				.toArray(size -> new CompletableFuture[size]);
		CompletableFuture.allOf(futArr).join();
		Utility.print("All shops returned results after " + (Utility.currentTime() - startTime) + " milliseconds");
	}

}