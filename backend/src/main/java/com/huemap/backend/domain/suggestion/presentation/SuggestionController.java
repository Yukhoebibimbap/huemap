package com.huemap.backend.domain.suggestion.presentation;

import java.time.LocalDateTime;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.huemap.backend.common.response.success.RestResponse;
import com.huemap.backend.common.security.CurrentUser;
import com.huemap.backend.domain.bin.domain.BinType;
import com.huemap.backend.domain.suggestion.application.SuggestionService;
import com.huemap.backend.domain.suggestion.dto.request.SuggestionCreateRequest;
import com.huemap.backend.domain.user.domain.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class SuggestionController {

	private final SuggestionService suggestionService;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("v1/suggestions/bin-location")
	public RestResponse save(
			@CurrentUser User user,
		  @RequestBody @Valid SuggestionCreateRequest suggestionCreateRequest
	) {
		return RestResponse.of(suggestionService.save(suggestionCreateRequest, user.getId()));
	}

	@GetMapping("v1/suggestions/bin-location")
	public RestResponse findAllByGuAndTypeAndDate(@PathParam("gu") String gu, @PathParam("type") BinType type,
		@PathParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startDate,
		@PathParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endDate) {
		return RestResponse.of(suggestionService.findAllByGuAndTypeAndDate(gu, type, startDate, endDate));
	}
}
