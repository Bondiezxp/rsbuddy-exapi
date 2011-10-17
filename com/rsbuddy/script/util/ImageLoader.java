package com.rsbuddy.script.util;

import com.rsbuddy.script.methods.Environment;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.net.URL;
import java.util.LinkedHashMap;

import javax.imageio.ImageIO;

public class ImageLoader {

	private static final LinkedHashMap<String, Image> CACHE = new LinkedHashMap<String, Image>();

	/**
	 * Gets an image from a file if it exists, otherwise it gets it from a url
	 * and saves it to a file.
	 * 
	 * @param fileName
	 *            The file name to save it as.
	 * @param imageURL
	 *            The url of the image to get.
	 * @return An image from the file if the file exists, otherwise an image
	 *         from the specified url.
	 */
	public static Image getImage(final String fileName, final String imageURL) {
		if (CACHE.containsKey(fileName)) {
			return CACHE.get(fileName);
		}
		try {
			final File f = new File(Environment.getStorageDirectory(), fileName);
			if (!f.exists()) {
				final URL url = new URL(imageURL);
				final Image img = ImageIO.read(url);
				if (img == null) {
					return null;
				}
				ImageIO.write((RenderedImage) img, "PNG", f);
				CACHE.put(fileName, img);
				return img;
			}
			final Image img = ImageIO.read(f);
			if (img == null) {
				return null;
			}
			CACHE.put(fileName, img);
			return img;
		} catch (final Exception e) {
			return null;
		}
	}
}