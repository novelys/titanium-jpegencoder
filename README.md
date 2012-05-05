
# Android Jpeg Encoder for Titanium

## Description

By default, Titanium encodes jpeg image with the highest quality possible, sometimes resulting in heavy files sent through the network. JpegEncoder provides a small set of functions to scale, crop and compress an image.

Jpeg Encoder only works on Android platforms. If you need another module which offers similar features on iOS device, take a look at [gudmundurh](https://github.com/gudmundurh)'s [Titanium Imaging](https://github.com/gudmundurh/titanium-imaging).

This module is still in beta, feel free to report any issues. Or fork it, patch the issue and send a pull request.

The library is now compatible with Titanium 1.8+. If you need to support older version, check the `under-1-8` branch (v0.1.1).

## Install the module

You can either download a precompiled build from Github (downloads button, if so skip steps one and two) or get the source code and build it yourself.

Note: If you plan on building the module, make sure the following classpath entries are set according to your environment:

* android.jar
* maps.jar
* titanium.jar
* js.jar
* kroll-apt.jar
* kroll-common.jar

Next, rename `build.properties.example` into `build.properties` and, again, set the following paths according to your environment:

* `titanium.platform` (Titanium version MUST be at least 1.8.0.1 for Jpeg Encoder 0.2)
* `android.platform`
* `google.apis`
* `android.ndk`

You can now build the module!

1. At root, run `ant build.xml`
2. Get the generated zip file from the `dist/` directory
3. Copy the downloaded (or generated) archive to the root of the Titanium app, or to the root of the system Titanium installation.
4. Edit `tiapp.xml` to add the module

```xml
<modules>
    <module version="{VERSION_NUMBER}" platform="android">com.novelys.jpegencoder</module>
</modules>
```

## Accessing the Jpeg Encoder Module

To access this module from JavaScript, you would do the following:

```javascript
var JpegEncoder = require("com.novelys.jpegencoder");
```

The `JpegEncoder` variable is a reference to the Module object.	


## Usage

You need to pass a blob image to the encoder, set the `imageBlob` property with the given blob.

### Compressing

You can choose the compression level by setting the `compressionQuality` property, then call the `encode` method. Supported values range from 1 (lowest quality) to 100 (highest quality). If `compressionQuality` is left blank, 70 will be used as default. The minimal example would be:

```javascript
var first = JpegEncoder.createEncoder({
	imageBlob: imageBlob,
	compressionQuality: 70
});

first.encode();

var newImageBlob = first.imageBlob;
```

### Scaling

Scaling an image is just as easy. Set the `newHeight` and `newWidth` properties and call the `scaleAndEncode` method.

```javascript
var second = JpegEncoder.createEncoder({
	imageBlob: imageBlob,
	newWidth: 500,
	newHeight: 300,
});

second.scaleAndEncode();
```

Alternatively, if you want to keep proportions during scaling you can set only one property (either `newHeight` or `newWidth`) and set the `keepProportions` property to `true`.

```javascript
var third = JpegEncoder.createEncoder({
	imageBlob: imageBlob,
	keepProportions: true,
	newHeight: 500,
});

third.scaleAndEncode();
```

### Cropping

To crop a picture, you need to pass a dictionary to the `cropRect` property, which contains the coordinates of the top left corner of the crop rectangle, its width and its height. And then call `cropAndEncode`.

```javascript
var fourth = JpegEncoder.createEncoder({
	imageBlob: imageBlob,
	cropRect: {x: 30, y: 30, width: 100, height: 100}
});

fourth.cropAndEncode();
```

You can as well scale and crop at the same time. The module will first scale the image and then crop the result upon the given rectangle.

```javascript
var fifth = JpegEncoder.createEncoder({
	imageBlob: imageBlob,
	compressionQuality: 70,
	newWidth: 500,
	newHeight: 300,
	cropRect: {x: 30, y: 30, width: 100, height: 100}
});

fifth.scaleCropAndEncode();
```

### Retrieving encoded image

To retrieve encoded image, simply calls the `imageBlob` property from the encoder. It will still be a blob image you can work with. For instance: 

```javascript
var scaled = Titanium.Filesystem.getFile('scaled_and_compressed2.jpg');
scaled.write(second.imageBlob);
```

Check `example/app.js` for more examples.

## Authors

Android Jpeg Encoder was made by [Frédéric Maquin](http://www.fredericmaquin.com) from [Novelys](http://www.novelys.com) (http://www.novelys.com). A small and agile software development team.

Feel free to contact us through our [website](http://www.novelys.com/contact) (http://www.novelys.com/contact)

## License

Licensed under the terms of the Apache Public License.

Please see the LICENSE included with this distribution for details.
