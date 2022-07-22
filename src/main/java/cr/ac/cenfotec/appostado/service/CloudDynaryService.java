package cr.ac.cenfotec.appostado.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CloudDynaryService {

    Cloudinary cloudinary = new Cloudinary(
        ObjectUtils.asMap("cloud_name", "dxg5n5trm", "api_key", "438641788789733", "api_secret", "5gUwSO-M96saGsVGnWwtm9gZITc")
    );

    public Map upload(String path) throws IOException {
        File file = new File(path);
        Map result = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        file.delete();
        return result;
    }
}
