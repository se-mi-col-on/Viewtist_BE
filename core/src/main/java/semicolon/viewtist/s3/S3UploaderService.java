package semicolon.viewtist.s3;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import semicolon.viewtist.image.entity.Image;
import semicolon.viewtist.image.repository.ImageRepository;

@Service
@RequiredArgsConstructor
public class S3UploaderService {

  private final ImageRepository imageRepository;
  private final AmazonS3 amazonS3;

  @Value("${s3.bucket}")
  private String bucket;

  private String changedImageName(String originName) { //이미지 이름 중복 방지를 위해 랜덤으로 생성
    String random = UUID.randomUUID().toString();
    return random + originName;
  }

  private String uploadImageToS3(MultipartFile image)
      throws IOException { //이미지를 S3에 업로드하고 이미지의 url을 반환
    String originName = image.getOriginalFilename(); //원본 이미지 이름
    String ext = Objects.requireNonNull(originName).substring(originName.lastIndexOf(".")); //확장자
    String changedName = changedImageName(originName); //새로 생성된 이미지 이름
    ObjectMetadata metadata = new ObjectMetadata(); //메타데이터
    metadata.setContentType("image/" + ext);

    PutObjectResult putObjectResult = amazonS3.putObject(new PutObjectRequest(
        bucket, changedName, image.getInputStream(), metadata
    ).withCannedAcl(CannedAccessControlList.PublicRead));

    return amazonS3.getUrl(bucket, changedName).toString(); //데이터베이스에 저장할 이미지가 저장된 주소

  }


  public String uploadImage(MultipartFile image) throws IOException {
    String originName = image.getOriginalFilename();
    String storedImageUrl = uploadImageToS3(image);

    Image photoUrl = Image.builder() //이미지에 대한 정보를 담아서 반환
        .imageUrl(storedImageUrl)
        .build();

    imageRepository.save(photoUrl);
    return storedImageUrl;
  }


  public void deleteFileFromS3(String fileName) {
    amazonS3.deleteObject(bucket, fileName);
  }

}

