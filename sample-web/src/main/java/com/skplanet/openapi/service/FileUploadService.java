package com.skplanet.openapi.service;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("fileUploadService")
public class FileUploadService {
	
	@Value("${bulkjobservice.localsavefolder}")
	public static String localSaveFolder;

	public boolean fileUpload(MultipartFile mRequest) {
		
		String uploadPath = localSaveFolder+"upload/output.txt";
		File file = new File(uploadPath);
		
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		
		if (mRequest == null) {
			System.out.println("Req is null");
			return false;
		}
		
		try {
			mRequest.transferTo(file);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	} // fileUpload end

}
