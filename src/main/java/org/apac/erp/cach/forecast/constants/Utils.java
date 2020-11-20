package org.apac.erp.cach.forecast.constants;

public class Utils {
	public static String convertAmountToString(Double amount) {
		String initialAmount = "" + amount;
		System.out.println("initialAmount ------------" + initialAmount);
		if (initialAmount.contains(".")) {
			String [] tab = initialAmount.split("\\.");
			if(tab[1].length() == 1) {
				tab[1] = "." + tab[1] + "00";
				initialAmount = tab[0] + tab[1];
			} else if(tab[1].length() == 2) {
				tab[1] = "." + tab[1] + "0";
				initialAmount = tab[0] + tab[1];
			}
			return initialAmount;
			//initialAmount = tab[0] +"." + tab[1];
		} else {
			initialAmount = initialAmount + ".000";
			return initialAmount;
		}
	}

}
