package com.morris.opensquare.services

import org.apache.commons.imaging.Imaging
import org.apache.commons.imaging.common.ImageMetadata
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata
import org.apache.commons.imaging.formats.tiff.TiffField
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.File

@Service
class NaiveImageTrackingService @Autowired constructor(private val fileService: FileService) {

    fun getExifImageMetaDate() {
        val image: File = fileService.getFile("image.jpeg")
        val metadata: ImageMetadata = Imaging.getMetadata(image)
        if (metadata is JpegImageMetadata) {
            val jpegImageMetadata: JpegImageMetadata = metadata
            val exifMetadata: TiffImageMetadata = jpegImageMetadata.exif
            val gpsInfo: TiffImageMetadata.GPSInfo = exifMetadata.gps
            println("latitude: " + gpsInfo.latitudeAsDegreesNorth)
            println("longitude: " + gpsInfo.longitudeAsDegreesEast)
            println("Original Date Time: " + printTagValue(jpegImageMetadata, ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL))

        }
    }

    private fun printTagValue(jpegMetadata: JpegImageMetadata, tagInfo: TagInfo) {
        val field: TiffField = jpegMetadata.findEXIFValueWithExactMatch(tagInfo)
        println(tagInfo.name + ": " + field.value);
    }
}