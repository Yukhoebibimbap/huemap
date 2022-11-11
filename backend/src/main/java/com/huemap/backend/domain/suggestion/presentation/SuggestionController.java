package com.huemap.backend.domain.suggestion.presentation;

import java.time.LocalDateTime;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.huemap.backend.common.response.success.RestResponse;
import com.huemap.backend.domain.bin.domain.BinType;
import com.huemap.backend.domain.suggestion.application.SuggestionService;
import com.huemap.backend.domain.suggestion.dto.request.SuggestionCreateRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/suggestions")
@RequiredArgsConstructor
public class SuggestionController {

	private final SuggestionService suggestionService;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("bin-location")
	public RestResponse save(@RequestBody @Valid SuggestionCreateRequest suggestionCreateRequest) {
		/**
		 * TODO: user 도메인 구현 후 수정 예정
		 */
		final Long userId = 1L;
		return RestResponse.of(suggestionService.save(suggestionCreateRequest, userId));
	}

	@GetMapping("bin-location")
	public RestResponse findAllByGuAndTypeAndDate(@PathParam("gu") String gu, @PathParam("type") BinType type,
		@PathParam("startDate") LocalDateTime startDate, @PathParam("endDate") LocalDateTime endDate) {
		return RestResponse.of(suggestionService.findAllByGuAndTypeAndDate(gu, type, startDate, endDate));
	}
}
