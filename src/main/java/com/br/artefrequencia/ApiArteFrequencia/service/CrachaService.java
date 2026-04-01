package com.br.artefrequencia.ApiArteFrequencia.service;

import java.awt.*;
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
        Graphics2D g = cracha.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // FUNDO + MARCA D'ÁGUA
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        try {
            ClassPathResource resource = new ClassPathResource("static/ProCidadania.png");
            InputStream input = resource.getInputStream();

            BufferedImage fundo = ImageIO.read(input);
            // TRANSPARÊNCIA DA IMAGEM
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));

            int imgW = 550;
            int imgH = 550;

            int imgX = (width - imgW) / 2;
            int imgY = (height - imgH) / 2;

            g.drawImage(fundo, imgX, imgY, imgW, imgH, null);
            // VOLTAR PARA OPACIDADE NORMAL
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        } catch (Exception e) {
            // fallback caso não encontre a imagem
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);

        }
        // BORDA
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(4f));
        g.drawRoundRect(10, 10, width - 20, height - 20, 20, 20);

        // TITULO
        g.setFont(new Font("Arial", Font.BOLD, 34));
        g.setColor(Color.BLACK);
        drawCentered(g, "Instituição Pró-Cidadania", width / 2, 60);

        // FOTO
        int fotoW = 300;
        int fotoH = 400;
        int fotoX = (width - fotoW) / 2;
        int fotoY = 90;
        try {
            String fotoStr = aluno.getFoto();
            if (fotoStr != null && !fotoStr.isEmpty()) {
                String base64 = fotoStr.contains(",") ? fotoStr.split(",", 2)[1] : fotoStr;
                byte[] fotoBytes = Base64.getDecoder().decode(base64);
                BufferedImage foto = ImageIO.read(new java.io.ByteArrayInputStream(fotoBytes));
                Image fotoScaled = foto.getScaledInstance(fotoW, fotoH, Image.SCALE_SMOOTH);
                g.drawImage(fotoScaled, fotoX, fotoY, null);

            }
        } catch (Exception ex) {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(fotoX, fotoY, fotoW, fotoH);
        }
        // NOME DO ALUNO
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 34));

        int nomeY = fotoY + fotoH + 70;

        int linhasNome = drawMultiLineCentered(
                g,
                aluno.getNome(),
                width / 2,
                nomeY,
                width - 80);

        // QR CODE
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

            int qrSize = 300;

            int qrX = (width - qrSize) / 2;

            int qrY = nomeY + (linhasNome * g.getFontMetrics().getHeight()) - 30;

            Image qrScaled = qrImage.getScaledInstance(qrSize, qrSize, Image.SCALE_SMOOTH);

            g.drawImage(qrScaled, qrX, qrY, null);
        } catch (Exception ex) {
            g.setColor(Color.RED);
            drawCentered(g, "Erro no QR", width / 2, height - 200);
        }
        g.dispose();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(cracha, "png", baos);

        return baos.toByteArray();
    }

    // TEXTO CENTRALIZADO
    private void drawCentered(Graphics2D g, String text, int centerX, int baselineY) {
        if (text == null)
            text = "";
        FontMetrics fm = g.getFontMetrics();

        int textWidth = fm.stringWidth(text);

        int x = centerX - textWidth / 2;

        g.drawString(text, x, baselineY);
    }

    // TEXTO COM QUEBRA AUTOMÁTICA
    private int drawMultiLineCentered(Graphics2D g, String text, int centerX, int startY, int maxWidth) {
        if (text == null)
            text = "";

        FontMetrics fm = g.getFontMetrics();

        String[] words = text.split(" ");

        String line = "";

        int y = startY;

        int lines = 0;

        for (String word : words) {

            String testLine = line + word + " ";
            int width = fm.stringWidth(testLine);
            if (width > maxWidth) {
                drawCentered(g, line.trim(), centerX, y);
                line = word + " ";
                y += fm.getHeight();
                lines++;
            } else {
                line = testLine;
            }
        }
        if (!line.isEmpty()) {
            drawCentered(g, line.trim(), centerX, y);
            lines++;
        }
        return lines;
    }
}