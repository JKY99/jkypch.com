package com.jkypch.web.dto;

public record LoginResponse(String accessToken, String refreshToken, String role) {}
