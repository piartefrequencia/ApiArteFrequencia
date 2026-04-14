package com.br.artefrequencia.ApiArteFrequencia.service;


import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;
import javax.imageio.ImageIO;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.br.artefrequencia.ApiArteFrequencia.model.Db1.Aluno;
import com.br.artefrequencia.ApiArteFrequencia.util.QRCodeGenerator;



@Service
public class CrachaService {

    public byte[] gerarCrachaPNG(Aluno aluno) throws Exception {

        int width = 600;
        int height = 900;

        BufferedImage cracha = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D grafica = cracha.createGraphics();

        grafica.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        grafica.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // FUNDO BRANCO
        grafica.setColor(Color.BLACK);
        grafica.fillRect(0, 0, width, height);

        // MARCA D'ÁGUA (LOGO CENTRAL)
        try {
            ClassPathResource resource = new ClassPathResource("static/ProCidadania.png");
            InputStream input = resource.getInputStream();
            BufferedImage logo = ImageIO.read(input);

            grafica.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.40f));

            int size = 600;
            int x = (width - size) / 2;
            int y = (height - size) / 2;

            grafica.drawImage(logo, x, y, size, size, null);
            grafica.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        } catch (Exception e) {
        }
        // BORDA
        grafica.setColor(new Color(30, 30, 30));
        grafica.setStroke(new BasicStroke(0f));
        grafica.drawRoundRect(10, 10, width - 20, height - 20, 25, 25);

        // TITULO
        grafica.setFont(new Font("Arial", Font.BOLD, 34));
        grafica.setColor(Color.WHITE);
        drawCentered(grafica, "Associação Pró-Cidadania", width / 2, 70);

        // FOTO COM BORDA ARREDONDADA
        int fotoW = 280;
        int fotoH = 300;
        int fotoX = (width - fotoW) / 2;
        int fotoY = 120;

        try {
            String fotoStr = aluno.getFoto();
            if (fotoStr != null && !fotoStr.isEmpty()) {

                String base64 = fotoStr.contains(",") ? fotoStr.split(",", 2)[1] : fotoStr;
                byte[] fotoBytes = Base64.getDecoder().decode(base64);
                BufferedImage foto = ImageIO.read(new java.io.ByteArrayInputStream(fotoBytes));

                Shape clip = new RoundRectangle2D.Float(fotoX, fotoY, fotoW, fotoH, 30, 30);
                grafica.setClip(clip);

                grafica.drawImage(foto, fotoX, fotoY, fotoW, fotoH, null);

                grafica.setClip(null);

                // borda
                grafica.setColor(Color.GRAY);
                grafica.setStroke(new BasicStroke(2f));
                grafica.drawRoundRect(fotoX, fotoY, fotoW, fotoH, 30, 30);
            }

        } catch (Exception e) {
            grafica.setColor(Color.LIGHT_GRAY);
            grafica.fillRoundRect(fotoX, fotoY, fotoW, fotoH, 30, 30);
        }

        // NOME
        grafica.setFont(new Font("Arial", Font.BOLD, 32));
        grafica.setColor(Color.WHITE);

        int nomeY = fotoY + fotoH + 60;
        int linhasNome = drawMultiLineCentered(
                grafica, aluno.getNome(),
                width / 2,
                nomeY,
                width - 100);

        // QR CODE COM MOLDURA
        String qrBase64 = aluno.getQrcode();
        if (qrBase64 == null || qrBase64.isEmpty()) {
            qrBase64 = QRCodeGenerator.generateQRCodeBase64(
                    "ALUNO:" + aluno.getMatricula(),
                    400,
                    400);
        }
        try {
            String qrOnly = qrBase64.contains(",") ? qrBase64.split(",", 2)[1] : qrBase64;
            byte[] qrBytes = Base64.getDecoder().decode(qrOnly);
            BufferedImage qrImage = ImageIO.read(new java.io.ByteArrayInputStream(qrBytes));

            int qrSize = 320;

            int qrX = (width - qrSize) / 2;
            int qrY = nomeY + (linhasNome * grafica.getFontMetrics().getHeight()) + 10;

            // fundo branco do QR (moldura)
            int padding = -50;

            grafica.setColor(Color.WHITE);
            grafica.fillRoundRect(qrX - padding, qrY - padding, qrSize + (padding * 2), qrSize + (padding * 2), 15, 15);

            grafica.setColor(Color.GRAY);
            grafica.drawRoundRect(qrX - padding, qrY - padding, qrSize + (padding * 2), qrSize + (padding * 2), 15, 15);

            // QR
            grafica.drawImage(qrImage, qrX, qrY, qrSize, qrSize, null);

        } catch (Exception e) {
            grafica.setColor(Color.RED);
            drawCentered(grafica, "Erro no QR", width / 2, height - 200);
        }
        grafica.dispose();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(cracha, "png", baos);

        return baos.toByteArray();
    }

    // TEXTO CENTRALIZADO
    private void drawCentered(Graphics2D grafica, String text, int centerX, int y) {
        if (text == null)
            text = "";
        FontMetrics fm = grafica.getFontMetrics();
        int x = centerX - fm.stringWidth(text) / 2;
        grafica.drawString(text, x, y);
    }

    // TEXTO MULTILINHA
    private int drawMultiLineCentered(Graphics2D grafica, String text, int centerX, int startY, int maxWidth) {

        if (text == null)
            text = "";
        FontMetrics fm = grafica.getFontMetrics();
        String[] words = text.split(" ");
        String line = "";
        int y = startY;
        int lines = 0;

        for (String word : words) {
            String testLine = line + word + " ";
            if (fm.stringWidth(testLine) > maxWidth) {
                drawCentered(grafica, line.trim(), centerX, y);
                line = word + " ";
                y += fm.getHeight();
                lines++;
            } else {
                line = testLine;
            }
        }
        if (!line.isEmpty()) {
            drawCentered(grafica, line.trim(), centerX, y);
            lines++;
        }
        return lines;
    }
}
