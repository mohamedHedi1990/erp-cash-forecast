package org.apac.erp.cach.forecast.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apac.erp.cach.forecast.persistence.entities.Company;
import org.apac.erp.cach.forecast.service.CompanyService;
import org.apac.erp.cach.forecast.service.DBFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequestMapping("/api/file")
@RestController
public class FileController {
	@Autowired
	private DBFileStorageService DBFileStorageService;

	@Autowired
	private CompanyService companyService;

	/*@CrossOrigin
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	// @PostMapping("/{restaurantId}/{menuId}")
	@PostMapping("{context}/{userId}")
	public User uploadIdentityFile(@RequestParam("file") MultipartFile file, @PathVariable("context") String context, @PathVariable("userId") Long userId) {
		String fileName;
		try {
			fileName = DBFileStorageService.storeFile(file);
			String fileDownloadUri = "";

			fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/file/"+context + "/" + userId +"/downloadFile/")
					.path(fileName).toUriString();

			return userService.updateUserUrls(context,fileDownloadUri, userId);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}*/
	
	@CrossOrigin
	@PostMapping("/logo/{companyId}")
	public Company uploadLogoFile(@RequestParam("file") MultipartFile file, @PathVariable("companyId") Long companyId) {
		String fileName;
		try {
			fileName = DBFileStorageService.storeFile(file);
			String fileDownloadUri = "";

			fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/file/downloadFile/")
					.path(fileName).toUriString();

			return companyService.updateCompanyLogoUrl(fileDownloadUri, companyId);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@CrossOrigin
	@PostMapping("/signature/{companyId}")
	public Company uploadSignatureFile(@RequestParam("file") MultipartFile file, @PathVariable("companyId") Long companyId) {
		String fileName;
		try {
			fileName = DBFileStorageService.storeFile(file);
			String fileDownloadUri = "";

			fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/file/downloadFile/")
					.path(fileName).toUriString();

			return companyService.updateCompanySignatureUrl(fileDownloadUri, companyId);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	

//	@CrossOrigin
//	// @PreAuthorize("hasRole('ROLE_ADMIN')")
//	// @PostMapping("/{restaurantId}/{menuId}")
//	@PostMapping("/menu/{menuId}")
//	public Menu uploadFileForMenu(@RequestParam("file") MultipartFile file, @PathVariable("menuId") Long menuId) {
//		String fileName;
//		try {
//			fileName = DBFileStorageService.storeFile(file);
//			String fileDownloadUri = "";
//			fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/file/downloadFile/")
//					.path(fileName).toUriString();
//
//			// return restoSevcie.updateRestarantOrMenuPicture(fileDownloadUri,restaurantId,
//			// menuId);
//			return restoSevcie.updateMenuPicture(fileDownloadUri, menuId);
//
//			// return new UploadFileResponse(fileName, fileDownloadUri,
//			// file.getContentType(), file.getSize());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//
//	}

	@CrossOrigin
	@GetMapping("/downloadFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request)
			throws Exception {
		// Load file as Resource
		Resource resource = DBFileStorageService.loadFileAsResource(fileName);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
		
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
	
	@CrossOrigin
	@GetMapping("{context}/{userId}/downloadFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request,
			@PathVariable("context") String context, @PathVariable("userId") Long userId)
			throws Exception {
		// Load file as Resource
		Resource resource = DBFileStorageService.loadFileAsResource(fileName);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
}
