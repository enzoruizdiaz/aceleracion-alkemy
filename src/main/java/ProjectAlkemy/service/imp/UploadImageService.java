package ProjectAlkemy.service.imp;

import org.springframework.web.multipart.MultipartFile;

public interface UploadImageService {

    public String uploadFile(MultipartFile multipartFile);
    public String deleteFile(String url);

}
