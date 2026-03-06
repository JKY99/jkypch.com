package com.jkypch.web.dto;

public record ChangePasswordRequest(String currentPassword, String newPassword) {}
