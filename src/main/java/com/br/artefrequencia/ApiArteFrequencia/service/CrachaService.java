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

import jakarta.annotation.PostConstruct;

@Service
public class CrachaService {

    private BufferedImage logoCache;

    @PostConstruct
    public void init() {
        try {
            ClassPathResource resource = new ClassPathResource("static/ProCidadania.png");
            try (InputStream is = resource.getInputStream()) {
                logoCache = ImageIO.read(is);
            }
        } catch (Exception e) {
            System.err.println("Aviso: Logo ProCidadania não encontrada no caminho static/");
        }
    }

    public byte[] gerarCrachaPNG(Aluno aluno) throws Exception {
        int width = 350;
        int height = 550;

        BufferedImage cracha = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D grafica = cracha.createGraphics();

        grafica.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        grafica.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // FUNDO PRETO
        grafica.setColor(Color.BLACK);
        grafica.fillRect(0, 0, width, height);

        // MARCA D'ÁGUA (Usando o Cache)
        if (logoCache != null) {
            grafica.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.30f));
            int size = 350;
            grafica.drawImage(logoCache, (width - size) / 2, (height - size) / 2, size, size, null);
            grafica.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }

        // TÍTULO
        grafica.setFont(new Font("Arial", Font.BOLD, 22));
        grafica.setColor(Color.WHITE);
        drawCentered(grafica, "Associação Pró-Cidadania", width / 2, 45);

        // FOTO DO ALUNO
        int fotoW = 160;
        int fotoH = 180;
        int fotoX = (width - fotoW) / 2;
        int fotoY = 80;

        try {
            String fotoStr = aluno.getFoto();
            if (fotoStr != null && !fotoStr.isEmpty()) {
                String base64 = fotoStr.contains(",") ? fotoStr.split(",", 2)[1] : fotoStr;
                byte[] fotoBytes = Base64.getDecoder().decode(base64);

                try (java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(fotoBytes)) {
                    BufferedImage foto = ImageIO.read(bais);
                    Shape clip = new RoundRectangle2D.Float(fotoX, fotoY, fotoW, fotoH, 20, 20);
                    grafica.setClip(clip);
                    grafica.drawImage(foto, fotoX, fotoY, fotoW, fotoH, null);
                    grafica.setClip(null);
                }
            }
        } catch (Exception e) {
            grafica.setColor(Color.DARK_GRAY);
            grafica.fillRoundRect(fotoX, fotoY, fotoW, fotoH, 20, 20);
        }

        // NOME DO ALUNO
        grafica.setFont(new Font("Arial", Font.BOLD, 16));
        grafica.setColor(Color.WHITE);
        int nomeY = fotoY + fotoH + 40;
        int linhas = drawMultiLineCentered(grafica, aluno.getNome().toUpperCase(), width / 2, nomeY, width - 60);

        // QR CODE
        try {
            int qrSize = 130;

            String conteudoQR = "ID:" + aluno.getId();

            BufferedImage qrImage = QRCodeGenerator.generateQRCodeImage(conteudoQR, qrSize, qrSize);
            int qrX = (width - qrSize) / 2;
            int qrY = nomeY + (linhas * 25) + 10;

            int padding = 3;
            grafica.setColor(Color.WHITE);
            grafica.fillRoundRect(
                    qrX - padding,
                    qrY - padding,
                    qrSize + (padding * 2),
                    qrSize + (padding * 2),
                    10, 10);

            grafica.drawImage(qrImage, qrX, qrY, null);
        } catch (Exception e) {
            grafica.setColor(Color.RED);
            drawCentered(grafica, "ERRO QR", width / 2, height - 50);
        }

        grafica.dispose();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(cracha, "png", baos);
            return baos.toByteArray();
        }
    }

    private void drawCentered(Graphics2D grafica, String text, int centerX, int y) {
        if (text == null)
            text = "";
        FontMetrics fm = grafica.getFontMetrics();
        grafica.drawString(text, centerX - fm.stringWidth(text) / 2, y);
    }

    private int drawMultiLineCentered(Graphics2D grafica, String text, int centerX, int startY, int maxWidth) {
        if (text == null)
            text = "";
        FontMetrics fm = grafica.getFontMetrics();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        int y = startY;
        int count = 0;

        for (String word : words) {
            if (fm.stringWidth(line + word) > maxWidth) {
                drawCentered(grafica, line.toString().trim(), centerX, y);
                line = new StringBuilder(word + " ");
                y += fm.getHeight();
                count++;
            } else {
                line.append(word).append(" ");
            }
        }
        drawCentered(grafica, line.toString().trim(), centerX, y);
        return count + 1;
    }
}
