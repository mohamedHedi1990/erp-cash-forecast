package org.apac.erp.cach.forecast.constants;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

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

	public static String convertAmountToStringWithSeperator(double amount1) {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
		symbols.setGroupingSeparator('\'');
		DecimalFormat df=new DecimalFormat("#,###.###", symbols);
		String result=df.format(amount1);
		System.out.println(result);
		String initialAmount=result;

		if (initialAmount.contains(".")) {
			String [] tab = initialAmount.split("\\.");

			if(tab[1].length() == 1) {
				tab[1] = "." + tab[1] + "00";
			} else if(tab[1].length() == 2) {
				tab[1] = "." + tab[1] + "0";

			}
			initialAmount = tab[0] + tab[1];
			System.out.println("init amount :"+initialAmount);

			return initialAmount;
		} else {
			initialAmount = initialAmount + ".000";
			System.out.println("init amount :"+initialAmount);

			return initialAmount;
		}
	}
}
