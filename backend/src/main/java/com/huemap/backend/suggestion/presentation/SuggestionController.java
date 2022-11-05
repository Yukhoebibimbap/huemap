package com.huemap.backend.suggestion.presentation;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.huemap.backend.common.response.success.RestResponse;
import com.huemap.backend.suggestion.application.SuggestionService;
import com.huemap.backend.suggestion.dto.request.SuggestionCreateRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/suggestions")
@RequiredArgsConstructor
public class SuggestionController {

	private final SuggestionService suggestionService;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("bin-location")
	public RestResponse save(@RequestBody @Valid SuggestionCreateRequest suggestionCreateRequest) {

		return RestResponse.of(suggestionService.save(suggestionCreateRequest));
	}
}
