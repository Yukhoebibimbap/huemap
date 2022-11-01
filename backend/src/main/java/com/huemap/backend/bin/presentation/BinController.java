package com.huemap.backend.bin.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.huemap.backend.bin.application.BinService;
import com.huemap.backend.bin.domain.BinType;
import com.huemap.backend.common.response.success.RestResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/bins")
@RequiredArgsConstructor
public class BinController {

	private final BinService binService;

	@GetMapping
	public ResponseEntity<RestResponse> findAll(@RequestParam("type") BinType type) {

		RestResponse response = RestResponse.of(binService.findAll(type));
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<RestResponse> findById(@PathVariable("id") Long binId) {

		RestResponse response = RestResponse.of(binService.findById(binId));
		return ResponseEntity.ok(response);
	}

}
