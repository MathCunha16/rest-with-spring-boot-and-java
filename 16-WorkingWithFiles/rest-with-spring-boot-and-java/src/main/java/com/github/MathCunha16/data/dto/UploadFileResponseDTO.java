package com.github.MathCunha16.data.dto;

import java.io.Serializable;

public record UploadFileResponseDTO(
        String fileName,
        String fileDownloadUri,
        String fileType,
        long size
) implements Serializable {
    private static final long serialVersionUID = 1L;
}
