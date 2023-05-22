package com.ppe.TennisTieBreak.media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/media")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MediaController {

	@Autowired
	IMediaService mediaService;

	
	@GetMapping("/uploads/{dir}/{filename}")
	public ResponseEntity<?> getFile(@PathVariable(name = "dir") String dir,
			@PathVariable(name = "filename") String filename) {
		Resource file = mediaService.load(dir, filename);
		if (file != null) {
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
					.body(file);
		} else {
			return new ResponseEntity<>("Could not read the file1!", HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/uploads/joueur/{dir}/{filename}")
	public ResponseEntity<?> getFilee(@PathVariable(name = "dir") String dir,
			@PathVariable(name = "filename") String filename) {
		Resource file = mediaService.load("joueur/"+dir, filename);
		if (file != null) {
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
					.body(file);
		} else {
			return new ResponseEntity<>("Could not read the file!", HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/uploads/entraineur/{dir}/{filename}")
	public ResponseEntity<?> getFileentraineur(@PathVariable(name = "dir") String dir,
			@PathVariable(name = "filename") String filename) {
		Resource file = mediaService.load("entraineur/"+dir, filename);
		if (file != null) {
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
					.body(file);
		} else {
			return new ResponseEntity<>("Could not read the file!", HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/uploads/terrain/{dir}/{filename}")
	public ResponseEntity<?> getFileterrain(@PathVariable(name = "dir") String dir,
			@PathVariable(name = "filename") String filename) {
		Resource file = mediaService.load("terrain/"+dir, filename);
		if (file != null) {
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
					.body(file);
		} else {
			return new ResponseEntity<>("Could not read the file2!", HttpStatus.NOT_FOUND);
		}
	}
}
