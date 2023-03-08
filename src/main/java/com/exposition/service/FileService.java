package com.exposition.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exposition.entity.File;
import com.exposition.repository.FileRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

	private final FileRepository fileRepository;
	public File saveFile(File file) {
		return fileRepository.save(file);
	}
}
