package in.exchange.rate.enums;

public enum DiscountCode {
	
	NONE(0), 
	SILVER(5), 
	GOLD(10), 
	PLATINUM(15), 
	DIAMOND(20);

	public int percentage;

	DiscountCode(int percentage) {
		this.percentage = percentage;
	}

	

	
}
