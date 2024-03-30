package com.builderbackend.app.dtos;

import lombok.Data;

@Data
public class InternalNotesAttachmentDTO {

    String internalNotesAttachmentId;

    /*
     * MIME type is a way of identifying files so the browser/client knows the
     * format. For example:
     * 
     * text/plain for plain text files
     * image/jpeg for JPEG images
     * application/pdf for PDFs
     * audio/mpeg for MP3 audio files
     * etc...
     * 
     * We need this because interal notes isnt only gonna hold images, it can hold
     * any file type (Ideally),
     * so we want to know what type of file were dealing with
     */
    private String mimeType;

    // attachment inside of note
    private String base64Data;
}
