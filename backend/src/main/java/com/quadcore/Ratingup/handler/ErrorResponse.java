package com.quadcore.Ratingup.handler;

import java.util.List;

public record ErrorResponse(String code, List<String> messages) {}
