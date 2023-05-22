package com.ppe.TennisTieBreak.media;

import java.util.Set;

import org.springframework.core.io.Resource;



public interface IMediaService {
	
	public Set<MediaDTO> setMediaNames(Set<MediaDTO> medias);

	public String setFileName(String name);

	public long uploadList(Set<MediaDTO> medias, String path);

	long upload(byte[] file, String filename, String path);

	Media getById(Long id);

	void deleteById(long id);

	public boolean deleteFileByUrl(String url);

	public Resource load(String dir, String filename);

}
