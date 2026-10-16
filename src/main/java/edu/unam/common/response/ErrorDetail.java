package edu.unam.common.response;

import java.util.Map;

public record ErrorDetail(
        int status,
        String code,
        String title,
        String detail,
        Map<String, Object> source
) {}

