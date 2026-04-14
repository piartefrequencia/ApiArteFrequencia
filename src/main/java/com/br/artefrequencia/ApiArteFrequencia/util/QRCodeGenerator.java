package com.br.artefrequencia.ApiArteFrequencia.util;


import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import java.util.Map;


public class QRCodeGenerator {

   public static BufferedImage generateQRCodeImage(String text, int width, int height) throws Exception {
    Map<EncodeHintType, Object> hints = new HashMap<>();
    hints.put(EncodeHintType.MARGIN, 0); // Define a margem interna do QR Code como ZERO

    BitMatrix matrix = new MultiFormatWriter().encode(
        text, 
        BarcodeFormat.QR_CODE, 
        width, 
        height, 
        hints // Passa a configuração de margem zero
    );
    return MatrixToImageWriter.toBufferedImage(matrix);
    }
}
