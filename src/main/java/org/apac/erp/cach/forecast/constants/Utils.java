package org.apac.erp.cach.forecast.constants;

import java.text.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
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
		DecimalFormat df=new DecimalFormat("# ###.###", symbols);
		NumberFormat numberFormat = NumberFormat.getInstance(java.util.Locale.FRENCH);
		String initialAmount=numberFormat.format(amount1);
		//String result=df.format(amount1);
		
		//String initialAmount=result;
		

		if (initialAmount.contains(".") || initialAmount.contains(",")) {
			String [] tab = null;
			if(initialAmount.contains(".") ) {
				tab = initialAmount.split("\\.");
			} else {
				tab = initialAmount.split(",");
			}

			if(tab[1].length() == 1) {
				tab[1] = "." + tab[1] + "00";
				initialAmount = tab[0] + tab[1];
			} else if(tab[1].length() == 2) {
				tab[1] = "." + tab[1] + "0";
				initialAmount = tab[0] + tab[1];
			} else if(tab[1].length() == 3) {
				tab[1] = "." + tab[1];
				initialAmount = tab[0] + tab[1];
			}
			
			System.out.println("init amount :"+initialAmount);

			return initialAmount;
		} else {
			initialAmount = initialAmount + ".000";
			System.out.println("init amount :"+initialAmount);

			return initialAmount;
		}
	}

	public static Date getFirstDayMonthPrevious(Date date)
	{
		System.out.println("-----first date in month -----"+date);
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate localDateMM=localDate.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
		Date dateMM = Date.from(localDateMM.atStartOfDay(ZoneId.systemDefault()).toInstant());
		System.out.println("-----last date in month MM-----"+dateMM);
		return dateMM;
	}
	public static Date getLastDayMonthPrevious(Date date) {
		System.out.println("-----last date in month -----"+date);
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate localDateMM=localDate.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
		Date dateMM = Date.from(localDateMM.atStartOfDay(ZoneId.systemDefault()).toInstant());
        System.out.println("-----last date in month MM-----"+dateMM);
		return dateMM;
	}
	public static Integer getMonthFromDate(Date date)
	{
		LocalDate localDate = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
		Integer month = localDate.getMonthValue();
		return month;
	}
	public static Integer getYearFromDate(Date date)
	{
		LocalDate localDate = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
		Integer year = localDate.getYear();
		return year;
	}
	public static Integer getMonth(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Integer month = cal.get(Calendar.MONTH);
		return month+1;
	}
	public static String getMonthName(int month)
	{

		String monthName = null;
		switch (month) {
			case 1:
				monthName = "Janv";
				break;
			case 2:
				monthName = "Fevr";
				break;
			case 3:
				monthName = "Mars";
				break;
			case 4:
				monthName = "Avr";
				break;
			case 5:
				monthName = "Mai";
				break;
			case 6:
				monthName = "Juin";
				break;
			case 7:
				monthName = "Juil";
				break;
			case 8:
				monthName = "Aout";
				break;
			case 9:
				monthName = "Sept";
				break;
			case 10:
				monthName = "Oct";
				break;
			case 11:
				monthName = "Nov";
				break;
			case 12:
				monthName = "Dec";
				break;
		}

		return monthName;
	}
}
