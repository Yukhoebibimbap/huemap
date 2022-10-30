package com.huemap.backend.bin.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huemap.backend.common.response.success.RestResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bins")
@RequiredArgsConstructor
public class BinController {

	@GetMapping
	public ResponseEntity<RestResponse> findAll() {

		RestResponse response = RestResponse.of(null);

		return ResponseEntity.ok(response);
	}
}
