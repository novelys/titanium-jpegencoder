/**
 * Native Android Jpeg encoder with few scaling and cropping features.
 * 
 * Copyright (c) 2011 by Novelys and other contributors
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

package com.novelys.jpegencoder;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiBlob;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

/**
 * Proxy over android native features
 * 
 * @author Novelys
 */
@Kroll.proxy(creatableInModule = JpegEncoderModule.class)
public class EncoderProxy extends KrollProxy {

	/** Blob image to process */
	private TiBlob imageBlob;
	
	/** New width for the image */
	private int newWidth;
	
	/** New height for the image */
	private int newHeight;
	
	/** Jpeg Compression quality, varies between 1 and 100 */
	private int compressionQuality;
	
	/** Wether or not proportions should be kept */
	private boolean keepProportions;
	
	/** Cropping rectangle */
	private CropRect cropRect;

	/**
	 * Construct and initialize the proxy.
	 */
	public EncoderProxy() {
		super();
	}

	/**
	 * Handle the dictionary of parameters given at creation.
	 */
	public void handleCreationDict(KrollDict dict) {
		
		if (dict.containsKeyAndNotNull("imageBlob")) {
			imageBlob = (TiBlob) dict.get("imageBlob");
		} else {
			imageBlob = null;
		}
		
		if (dict.containsKeyAndNotNull("newWidth")) {
			newWidth = dict.getInt("newWidth");
		} else {
			newWidth = 0;
		}
		
		if (dict.containsKeyAndNotNull("newHeight")) {
			newHeight = dict.getInt("newHeight");
		} else {
			newHeight = 0;
		}

		if (dict.containsKeyAndNotNull("keepProportions")) {
			keepProportions = dict.getBoolean("keepProportions");
		} else {
			keepProportions = false;
		}
		
		if (dict.containsKeyAndNotNull("compressionQuality")) {
			compressionQuality = dict.getInt("compressionQuality");
			if (compressionQuality > 100) { compressionQuality = 100;}
		} else {
			compressionQuality = 70; // Default value.
		}
		
		setCropRect(dict);
	}

	
	/*
	 * This method will calculate the inSampleSize property.  
	 */
	private int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
	
	
	/**
	 * Scale, encode or do both depending on the parameters given by the
	 * dictionary and then encode the image in Jpeg.
	 */
	@Kroll.method
	public void scaleCropAndEncode() {
	
		// TODO: Refactor this part into different methods.
		if ((keepProportions && (newWidth > 0 || newHeight > 0)) ||
			(newWidth > 0 && newHeight > 0) || cropRect != null) {

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			
			BitmapFactory.decodeByteArray(imageBlob.getBytes(), 0,
					imageBlob.getBytes().length, options);

		    // Calculate inSampleSize
		    options.inSampleSize = calculateInSampleSize(options, newWidth, newHeight);
		    System.out.println("in sample Size "+options.inSampleSize);
		    // Decode bitmap with inSampleSize set
		    options.inJustDecodeBounds = false;		
			
			Bitmap bm = BitmapFactory.decodeByteArray(imageBlob.getBytes(), 0,
					imageBlob.getBytes().length, options);
	
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			
			Bitmap resizedBitmap;
			if (newWidth > 0 && newHeight > 0) {
				resizedBitmap = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
			} else {
				resizedBitmap = bm;
			}
			
			if (cropRect != null) {
				if (cropRect.x != 0 && cropRect.y != 0 && cropRect.width != 0 && cropRect.height != 0) {
					if (cropRect.width == 0) {
						cropRect.width = (resizedBitmap.getWidth() - cropRect.x);
					}
					
					if (cropRect.height == 0) {
						cropRect.height = (resizedBitmap.getHeight() - cropRect.y);
					}
				}
				resizedBitmap = Bitmap.createBitmap(resizedBitmap, cropRect.x, cropRect.y, cropRect.width, cropRect.height);
			}
	
			resizedBitmap.compress(CompressFormat.JPEG, compressionQuality, bos);
	
			imageBlob = TiBlob.blobFromData(bos.toByteArray(),
					"image/jpeg");
		} else {
			encode();
		}
	}
	
	/**
	 * Convenience method, @see {@link EncoderProxy#scaleCropAndEncode()}
	 */
	@Kroll.method
	public void scaleAndEncode() {
		scaleCropAndEncode();
	}
	
	/**
	 * Convenience method, @see {@link EncoderProxy#scaleCropAndEncode()}
	 */
	@Kroll.method
	public void cropAndEncode() {
		scaleCropAndEncode();
	}
	
	/**
	 * Encode the image upon the given compression level.
	 */
	@Kroll.method
	public void encode() {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		byte[] byteArray = imageBlob.getBytes();

		Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0,
				byteArray.length);
			
		bm.compress(CompressFormat.JPEG, compressionQuality, bos);

		imageBlob = TiBlob.blobFromData(bos.toByteArray(),
				"image/jpeg");
	}

	@Kroll.getProperty
	@Kroll.method
	public int getCompressionQuality() {
		return compressionQuality;
	}

	@Kroll.setProperty
	@Kroll.method
	public void setCompressionQuality(int compressionQuality) {
		this.compressionQuality = compressionQuality;
	}

	@Kroll.getProperty
	@Kroll.method
	public TiBlob getImageBlob() {
		return imageBlob;
	}

	@Kroll.setProperty
	@Kroll.method
	public void setImageBlob(TiBlob imageBlob) {
		this.imageBlob = imageBlob;
	}

	@Kroll.getProperty
	@Kroll.method
	public int getNewWidth() {
		return newWidth;
	}

	@Kroll.setProperty
	@Kroll.method
	public void setNewWidth(int newWidth) {
		this.newWidth = newWidth;
	}

	@Kroll.getProperty
	@Kroll.method
	public int getNewHeight() {
		return newHeight;
	}

	@Kroll.setProperty
	@Kroll.method
	public void setNewHeight(int newHeight) {
		this.newHeight = newHeight;
	}

	@Kroll.getProperty
	@Kroll.method
	public boolean getKeepProportions() {
		return keepProportions;
	}

	@Kroll.setProperty
	@Kroll.method
	public void setKeepProportions(boolean keepProportions) {
		this.keepProportions = keepProportions;
	}

	public CropRect getCropRect() {
		return cropRect;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setCropRect(HashMap dictionary) {
		
		KrollDict dict = new KrollDict(dictionary);
		
		if (dict.containsKeyAndNotNull("cropRect")) {
			KrollDict krollDict = dict.getKrollDict("cropRect");
			this.cropRect = new CropRect();
			
			if (krollDict.containsKeyAndNotNull("x")) {
				cropRect.x = krollDict.getInt("x");
			} else {
				cropRect.x = 0;
			}
			
			if (krollDict.containsKeyAndNotNull("y")) {
				cropRect.y = krollDict.getInt("y");
			} else {
				cropRect.y = 0;
			}
			
			if (krollDict.containsKeyAndNotNull("width")) {
				cropRect.width = krollDict.getInt("width");
			} else {
				cropRect.width = 0;
			}
			
			if (krollDict.containsKeyAndNotNull("height")) {
				cropRect.height = krollDict.getInt("height");
			} else {
				cropRect.height = 0;
			}
			
		} else {
			cropRect = null;
		}
	}
}

/**
 * Structure holding a crop rectangle.
 */
class CropRect {
	public int x;
	public int y;
	public int width;
	public int height;
}
