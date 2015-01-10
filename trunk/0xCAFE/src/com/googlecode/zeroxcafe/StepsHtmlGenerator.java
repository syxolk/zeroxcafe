package com.googlecode.zeroxcafe;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.math3.fraction.BigFraction;

import android.content.Context;
import android.util.Log;

public class StepsHtmlGenerator {

	private Context ctx;

	private StringBuilder content = new StringBuilder();

	private char localizedDecimalPoint;

	// input for generate
	private String number;
	private int baseFrom;
	private int baseTo;

	// output of convertToDecimalSystem, input for convertToTargetSystem
	private BigInteger sumInteger;
	private BigFraction sumDecPart;
	
	// output of convertToTargetSystem, input for showResult
	private StringBuilder resNumber;

	public StepsHtmlGenerator(Context ctx, String number, int baseFrom,
			int baseTo) {
		if (ctx == null)
			throw new NullPointerException("Context is null");
		if (number == null)
			throw new NullPointerException("Number is null");

		this.ctx = ctx;
		this.number = number;
		this.baseFrom = baseFrom;
		this.baseTo = baseTo;

		this.localizedDecimalPoint = ctx.getString(
				R.string.keyboard_decimalpoint).charAt(0);
	}

	public String generate() {
		content.append("<!doctype html><html><head><style type=\"text/css\">");

		try {
			content.append(RawUtils.loadStringRes(ctx, R.raw.style));
		} catch (Exception e) {
			// do nothing
			Log.w(StepsHtmlGenerator.class.getName(), e.toString());
		}

		content.append("</style></head><body>");

		boolean isCompat = MathUtils.isCompatible(number, baseFrom);
		boolean hasMax1DP = MathUtils.hasMaximumOneDecimalPoint(number);

		if (isCompat && hasMax1DP) {

			content.append("<h1>").append(ctx.getString(R.string.steps_title))
					.append("</h1>");

			content.append("<p>")
					.append(ctx.getString(R.string.steps_what,
							NumberFormatUtils.format(number,
									localizedDecimalPoint), baseFrom, baseTo))
					.append("</p>");

			// STEP 1

			if(baseFrom == 10) {
				BigFraction sum = BigFractionUtils.fromBigDecimal(new BigDecimal(number));
				
				sumInteger = BigFractionUtils.integerPart(sum);
				sumDecPart = sum.subtract(sumInteger);
			} else {
				convertToDecimalSystem();
			}

			// STEP 2
			convertToTargetSystem();
			
			showResult();

		} else if (!isCompat) {
			content.append(ctx.getString(R.string.output_error_incompatible));
		} else if (!hasMax1DP) {
			content.append(ctx
					.getString(R.string.output_error_too_much_decimal_points));
		}

		content.append("</body></html>");

		return content.toString();
	}

	private void convertToDecimalSystem() {
		// get position of decimal point
		int decimalPoint = number.indexOf(MathUtils.DECIMAL_POINT);

		content.append("<h2>").append(ctx.getString(R.string.steps_step1))
				.append("</h2>");

		content.append("(")
				.append(NumberFormatUtils.format(number, localizedDecimalPoint))
				.append(")<sub>").append(baseFrom).append("</sub> = ");

		String numberBeforeDec = "", numberAfterDec = "";

		if (decimalPoint == -1) {
			numberBeforeDec = number;
		} else if (decimalPoint == 0) {
			numberAfterDec = number.substring(1);
		} else if (decimalPoint == number.length() - 1) {
			numberBeforeDec = number.substring(0, number.length() - 1);
		} else {
			numberBeforeDec = number.substring(0, decimalPoint);
			numberAfterDec = number.substring(decimalPoint + 1);
		}

		BigFraction sum = BigFraction.ZERO;
		boolean mustWritePlus = false;

		if (numberBeforeDec.length() > 0) {
			for (int i = 0; i < numberBeforeDec.length(); i++) {

				int digit = Character
						.digit(numberBeforeDec.charAt(i), baseFrom);
				int exp = numberBeforeDec.length() - 1 - i;

				sum = sum.add(BigInteger.valueOf(baseFrom).pow(exp)
						.multiply(BigInteger.valueOf(digit)));

				if (mustWritePlus)
					content.append(" + ");

				content.append(digit).append("&middot;").append(baseFrom)
						.append("<sup>").append(exp).append("</sup>");

				mustWritePlus = true;
			}
		}

		if (numberAfterDec.length() > 0) {
			for (int i = 0; i < numberAfterDec.length(); i++) {

				int digit = Character.digit(numberAfterDec.charAt(i), baseFrom);
				int exp = i + 1;

				sum = sum.add(new BigFraction(BigInteger.valueOf(digit),
						BigInteger.valueOf(baseFrom).pow(exp)));

				if (mustWritePlus)
					content.append(" + ");

				content.append(digit).append(" &middot; ").append(baseFrom)
						.append("<sup>-").append(exp).append("</sup>");

				mustWritePlus = true;
			}
		}

		content.append(" = ").append(sum);

		sumInteger = BigFractionUtils.integerPart(sum);
		sumDecPart = sum.subtract(sumInteger);

		if (sumInteger.compareTo(BigInteger.ZERO) > 0
				&& sumDecPart.compareTo(BigFraction.ZERO) > 0) {

			content.append(" = ").append(sumInteger).append(" + ")
					.append(sumDecPart);
		}
	}

	private void convertToTargetSystem() {
		content.append("<h2>").append(ctx.getString(R.string.steps_step2))
				.append("</h2>");

		resNumber = new StringBuilder();

		if (sumInteger.compareTo(BigInteger.ZERO) > 0) {

			content.append("<h3>")
					.append(ctx.getString(R.string.steps_step2_method_mod))
					.append("</h3>");

			content.append("<table><tr><th>")
					.append(ctx.getString(R.string.steps_step2_div))
					.append("</th><th>")
					.append(ctx.getString(R.string.steps_step2_rem))
					.append("</th><th>")
					.append(ctx.getString(R.string.steps_step2_digit))
					.append("</th></tr>");

			// Ganzzahl-Teil

			while (sumInteger.compareTo(BigInteger.ZERO) > 0) {
				BigInteger[] res = sumInteger.divideAndRemainder(BigInteger
						.valueOf(baseTo));
				char rem = res[1].toString(baseTo).toUpperCase(Locale.US)
						.charAt(0);

				content.append("<tr>").append("<td>").append(sumInteger)
						.append(" &divide; ").append(baseTo).append(" = ")
						.append(res[0].toString()).append("</td>")
						.append("<td>R ").append(res[1].toString())
						.append("</th>").append("<td>").append(rem)
						.append("</td>").append("</tr>");

				sumInteger = res[0];
				resNumber.insert(0, rem);
			}

			content.append("</table>");
		}

		if (sumDecPart.compareTo(BigFraction.ZERO) > 0) {

			content.append("<h3>")
					.append(ctx.getString(R.string.steps_step2_method_mul))
					.append("</h3>");

			resNumber.append(MathUtils.DECIMAL_POINT);

			content.append("<table><tr><th>")
					.append(ctx.getString(R.string.steps_step2_mul))
					.append("</th><th>")
					.append(ctx.getString(R.string.steps_step2_int))
					.append("</th><th>")
					.append(ctx.getString(R.string.steps_step2_digit))
					.append("</th></tr>");

			int maximumSteps = 10, stepCounter = 0;
			List<BigFraction> decPartList = new ArrayList<BigFraction>();
			StringBuilder resNumberFracPart = new StringBuilder();

			while (sumDecPart.compareTo(BigFraction.ZERO) > 0
					&& stepCounter < maximumSteps) {

				int index = decPartList.indexOf(sumDecPart);

				if (index != -1) {
					resNumberFracPart.insert(index,
							MathUtils.PERIODICAL_FRACTION_INDICATOR);
					break;
				}

				decPartList.add(sumDecPart);

				BigFraction sumDecPartMultResult = sumDecPart
						.multiply(BigInteger.valueOf(baseTo));

				BigInteger intPart = BigFractionUtils
						.integerPart(sumDecPartMultResult);

				BigFraction newSumDecPart = sumDecPartMultResult
						.subtract(intPart);

				char digit = intPart.toString(baseTo).toUpperCase(Locale.US)
						.charAt(0);

				content.append("<tr><td> ").append(sumDecPart)
						.append(" &middot; ").append(baseTo).append(" = ")
						.append(sumDecPartMultResult).append(" = ")
						.append(intPart).append(" + ").append(newSumDecPart)
						.append("</td><td>").append(intPart)
						.append("</td><td>").append(digit).append("</td></tr>");

				sumDecPart = newSumDecPart;
				resNumberFracPart.append(digit);
				stepCounter++;
			}

			resNumber.append(resNumberFracPart);

			if (sumDecPart.compareTo(BigFraction.ZERO) > 0
					&& resNumberFracPart.indexOf(String
							.valueOf(MathUtils.PERIODICAL_FRACTION_INDICATOR)) == -1) {
				resNumber.append(MathUtils.UNFINISHED_DECIMAL_PLACES_INDICATOR);
			}

			content.append("</table>");
		}
	}

	private void showResult() {
		content.append("<h2>").append(ctx.getString(R.string.steps_result))
				.append("</h2>");

		content.append("(")
				.append(NumberFormatUtils.format(number, localizedDecimalPoint,
						false))
				.append(")<sub>")
				.append(baseFrom)
				.append("</sub> = (")
				.append(NumberFormatUtils.format(resNumber.toString(),
						localizedDecimalPoint, true)).append(")<sub>")
				.append(baseTo).append("</sub>");
	}
}