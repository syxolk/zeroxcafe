package com.googlecode.zeroxcafe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.Resources.NotFoundException;

/**
 * Utility class for raw resource loading.
 * 
 * @author Hans
 */
public final class RawUtils {

	private RawUtils() {
	}

	/**
	 * default encoding for all resources is utf-8
	 */
	public static final String RESOURCE_ENCODING = "utf-8";

	/**
	 * Loads a raw resource and returns the content as a string.
	 * 
	 * @param ctx
	 *            Context to load the resource from
	 * @param id
	 *            resource id
	 * @return content as string
	 * @throws NotFoundException
	 *             if resource was not found
	 * @throws IOException
	 *             any IO exception
	 */
	public static String loadStringRes(Context ctx, int id)
			throws NotFoundException, IOException {
		StringBuilder text = new StringBuilder();

		BufferedReader buffreader = new BufferedReader(new InputStreamReader(
				ctx.getResources().openRawResource(id), RESOURCE_ENCODING));

		String line;

		while ((line = buffreader.readLine()) != null) {
			text.append(line);
			text.append('\n');
		}

		return text.toString();
	}
}