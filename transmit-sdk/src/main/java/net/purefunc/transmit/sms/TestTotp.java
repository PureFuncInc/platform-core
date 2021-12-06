package net.purefunc.transmit.sms;

import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class TestTotp {

    public static void main(String[] args) throws QrGenerationException, IOException {
        SecretGenerator secretGenerator = new DefaultSecretGenerator();
        String secret = secretGenerator.generate();
        // secret = "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV"

        QrData data = new QrData.Builder()
                .label("example@example.com")
                .secret(secret)
                .issuer("AppName")
                .algorithm(HashingAlgorithm.SHA1) // More on this below
                .digits(6)
                .period(30)
                .build();

        QrGenerator generator = new ZxingPngQrGenerator();
        byte[] imageData = generator.generate(data);

        final BufferedImage read = ImageIO.read(new ByteArrayInputStream(imageData));
        ImageIO.write(read, "png", new File("./qrcode.png"));
    }
}
