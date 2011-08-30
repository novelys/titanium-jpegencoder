var window = Ti.UI.createWindow({
	backgroundColor:'white'
});

var label = Ti.UI.createLabel({
	text: "Check /sdcard/com.novelys.jpegencoder/ !"
});

var Novelys = {}

Novelys.JpegEncoder = require('com.novelys.jpegencoder');

var im = Ti.UI.createImageView({
	image: 'http://th06.deviantart.net/fs71/PRE/i/2010/205/3/a/Anticipation_Taokaka_by_Vampamanian.jpg'
});

window.add(label);
window.open();

var first = Novelys.JpegEncoder.createEncoder({
	imageBlob: im.toBlob(),
	compressionQuality: 70,
});

var second = Novelys.JpegEncoder.createEncoder({
	imageBlob: im.toBlob(),
	compressionQuality: 70,
	keepProportions: true,
	newHeight: 500,
});

var third = Novelys.JpegEncoder.createEncoder({
	imageBlob: im.toBlob(),
	compressionQuality: 70,
	newWidth: 500,
	newHeight: 300,
});

var fourth = Novelys.JpegEncoder.createEncoder({
	imageBlob: im.toBlob(),
	compressionQuality: 70,
	newWidth: 500,
	newHeight: 300,
	cropRect: {x: 30, y: 30, width: 100, height: 100}
});

var fifth = Novelys.JpegEncoder.createEncoder({
	imageBlob: im.toBlob(),
	cropRect: {x: 30, y: 30, width: 100, height: 100}
});

var original = Titanium.Filesystem.getFile(Titanium.Filesystem.externalStorageDirectory, 'original.jpg');
original.write(im.toBlob());

first.encode();

var compressed = Titanium.Filesystem.getFile(Titanium.Filesystem.externalStorageDirectory, 'compressed.jpg');
compressed.write(first.imageBlob);

second.scaleAndEncode();

var scaled = Titanium.Filesystem.getFile(Titanium.Filesystem.externalStorageDirectory, 'scaled_and_compressed.jpg');
scaled.write(second.imageBlob);

third.scaleAndEncode();

var scaled2 = Titanium.Filesystem.getFile(Titanium.Filesystem.externalStorageDirectory, 'scaled_and_compressed2.jpg');
scaled2.write(third.imageBlob);

fourth.scaleAndEncode();

var scaledCropped = Titanium.Filesystem.getFile(Titanium.Filesystem.externalStorageDirectory, 'scaled_cropped.jpg');
scaledCropped.write(fourth.imageBlob);

fifth.cropAndEncode();

var cropped = Titanium.Filesystem.getFile(Titanium.Filesystem.externalStorageDirectory, 'cropped.jpg');
cropped.write(fifth.imageBlob);