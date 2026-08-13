package dev.epse.app.s3

import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.DeleteObjectsRequest
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.amazonaws.services.s3.model.GetObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.util.IOUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import dev.epse.app.config.aws.AwsS3Properties
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.concurrent.TimeUnit

@Service
class S3Service(
    private val amazonS3: AmazonS3,
    private val awsS3Properties: AwsS3Properties
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun generateUploadUrl(fileName: String, expirationTimeInMinutes: Long = 60): String {
        val expiration = Date()
        expiration.time += TimeUnit.MINUTES.toMillis(expirationTimeInMinutes)

        val generatePresignedUrlRequest = GeneratePresignedUrlRequest(
            awsS3Properties.bucketName, fileName
        ).withMethod(com.amazonaws.HttpMethod.PUT)
            .withExpiration(expiration)

        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString()
    }

    fun generateDownloadUrl(fileName: String, expirationTimeInMinutes: Long = 60): String {
        val expiration = Date()
        expiration.time += TimeUnit.MINUTES.toMillis(expirationTimeInMinutes)

        val generatePresignedUrlRequest = GeneratePresignedUrlRequest(
            awsS3Properties.bucketName, fileName
        ).withExpiration(expiration).withMethod(HttpMethod.GET)

        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString()
    }

    fun download(objectKey: String): ByteArray {
        val objectRequest = GetObjectRequest(
            awsS3Properties.bucketName,
            objectKey
        )
        val s3Object = amazonS3.getObject(objectRequest)
        val byteArray = IOUtils.toByteArray(s3Object.objectContent)
        return byteArray
    }

    fun uploadDoc(prefix: String, entityId: Long, data: ByteArray): Pair<String, String> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH_mm_ss")
        val fileName = "${prefix}_${entityId}_${LocalDateTime.now().format(formatter)}.docx"
        val uploadName = "downloads/${fileName}"

        upload(
            objectKey = uploadName,
            inputStream = data.inputStream(),
            lengthInBytes = data.size.toLong()
        )

        return Pair(fileName, uploadName)
    }

    fun upload(
        objectKey: String,
        inputStream: InputStream,
        lengthInBytes: Long,
        contentType: String? = null
    ) {

        val request = PutObjectRequest(
            awsS3Properties.bucketName,
            objectKey,
            inputStream,
            ObjectMetadata()
        )

        request.metadata.contentType = contentType
        request.metadata.contentLength = lengthInBytes

        amazonS3.putObject(request)
    }

    fun delete(vararg objectKeys: String) {
        val request = DeleteObjectsRequest(awsS3Properties.bucketName)
            .withKeys(*objectKeys)

        amazonS3.deleteObjects(request)
    }

    fun deleteFolder(folderPath: String) {
        val folderPrefix = if (folderPath.endsWith("/")) folderPath else "$folderPath/"

        val allObjects = mutableListOf<String>()
        var objectListing = amazonS3.listObjects(awsS3Properties.bucketName, folderPrefix)

        do {
            allObjects.addAll(objectListing.objectSummaries.map { it.key })

            objectListing = if (objectListing.isTruncated) {
                amazonS3.listNextBatchOfObjects(objectListing)
            } else {
                null
            }
        } while (objectListing != null)

        if (allObjects.isNotEmpty()) {
            delete(*allObjects.toTypedArray())
        }

        log.info("Deleted {} objects from folder {}", allObjects.size, folderPath)
    }
}