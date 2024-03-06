package semicolon.viewtist.s3.s3Uploader;


import static semicolon.viewtist.global.exception.ErrorCode.S3_FILE_CONVERT_ERROR;
import static semicolon.viewtist.global.exception.ErrorCode.S3_FILE_DELETE_ERROR;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import semicolon.viewtist.image.exception.S3Exception;

@Service
public class S3Uploader {

  private final AmazonS3 amazonS3;
  private final String bucket;

  public S3Uploader(AmazonS3 amazonS3) {
    this.amazonS3 = amazonS3;
    this.bucket = "viewtistbucket";
  }

  public String upload(MultipartFile multipartFile, String dirName) throws IOException {
    File uploadFile = convert(multipartFile);
    return upload(uploadFile, dirName);
  }

  private File convert(MultipartFile file) throws IOException {
    String originalFileName = Objects.requireNonNull(file.getOriginalFilename());
    String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName;
    File convertFile = new File(uniqueFileName);
    if (convertFile.createNewFile()) {
      try (FileOutputStream fos = new FileOutputStream(convertFile)) {
        fos.write(file.getBytes());
      }
      return convertFile;
    }
    throw new S3Exception(S3_FILE_CONVERT_ERROR, file.getOriginalFilename());
  }

  private String upload(File uploadFile, String dirName) {
    String fileName = dirName + "/" + uploadFile.getName();
    String uploadImageUrl = putS3(uploadFile, fileName);
    removeNewFile(uploadFile);
    return uploadImageUrl;
  }

  private String putS3(File uploadFile, String fileName) {
    amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(
        CannedAccessControlList.PublicRead));
    return amazonS3.getUrl(bucket, fileName).toString();
  }

  private void removeNewFile(File targetFile) {
    if (targetFile.delete()) {
      return;
    }
    throw new S3Exception(S3_FILE_DELETE_ERROR, targetFile.getName());
  }

  public void deleteFileFromS3(String fileName) {
    amazonS3.deleteObject(bucket, fileName);
  }

}

