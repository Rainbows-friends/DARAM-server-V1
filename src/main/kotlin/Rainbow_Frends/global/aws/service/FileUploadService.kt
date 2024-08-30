package Rainbow_Frends.global.aws.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class FileUploadService(private val s3Client: AmazonS3) {
    fun uploadFile(file: MultipartFile, bucketName: String): Pair<String, String> {
        val fileName = "${UUID.randomUUID()}-${file.originalFilename}"
        val fileObj = ObjectMetadata().apply {
            contentLength = file.size
        }
        s3Client.putObject(bucketName, fileName, file.inputStream, fileObj)
        val fileUrl = s3Client.getUrl(bucketName, fileName).toString()
        return Pair(fileUrl, fileName)
    }
}