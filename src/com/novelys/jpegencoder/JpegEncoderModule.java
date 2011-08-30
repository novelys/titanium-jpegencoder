/**
 * Native Android Jpeg encoder with few scaling and cropping features.
 * 
 * Copyright (c) 2011 by Novelys and other contributors
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

package com.novelys.jpegencoder;

import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;

import org.appcelerator.titanium.TiContext;

@Kroll.module(name="JpegEncoder", id="com.novelys.jpegencoder")
public class JpegEncoderModule extends KrollModule {
	public JpegEncoderModule(TiContext tiContext) {
		super(tiContext);
	}
}
