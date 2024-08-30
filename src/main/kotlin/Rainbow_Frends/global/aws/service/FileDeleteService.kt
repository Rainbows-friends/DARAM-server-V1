package Rainbow_Frends.global.aws.service

import com.amazonaws.services.s3.AmazonS3
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Service
class FileDeleteService(
    private val amazonS3: AmazonS3, @Value("\${AWS_S3_BUCKET}") private val bucket: String
) {
    fun deleteFile(fileUrl: String) {
        val decodedUrl = URLDecoder.decode(fileUrl, StandardCharsets.UTF_8.toString())
        val fileName = decodedUrl.substring(decodedUrl.lastIndexOf("/") + 1)
        amazonS3.deleteObject(bucket, fileName)
    }
}