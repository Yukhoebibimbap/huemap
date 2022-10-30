package com.huemap.backend.bin.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huemap.backend.bin.application.BinService;
import com.huemap.backend.common.response.success.RestResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/bins")
@RequiredArgsConstructor
public class BinController {

	private final BinService binService;

	@GetMapping
	public ResponseEntity<RestResponse> findAll() {

		RestResponse response = RestResponse.of(binService.findAll());

		return ResponseEntity.ok(response);
	}
}
