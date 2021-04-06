package ServicePrint.Utilities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.TimeZone;

public class AuthDigest {
    private String nonce;
    private String createdDate;
    private String passwordDigest;

    public AuthDigest(String password) {
        init(password);
    }

    public String getNonce() {
        return this.nonce;
    }

    public String getCreatedDate() {
        return this.createdDate;
    }

    public String getPasswordDigest() {
        return this.passwordDigest;
    }


    private void init(String password) {
        try {
            // Init nonce
            SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
            rand.setSeed(System.currentTimeMillis());
            byte[] nonceBytes = new byte[16];
            rand.nextBytes(nonceBytes);
            this.nonce = (Base64.getEncoder()).encodeToString(nonceBytes);

            // Init created date
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            this.createdDate = df.format(Calendar.getInstance().getTime());

            // From the spec: Password_Digest = Base64 ( SHA-1 ( nonce + created + password ) )
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(nonceBytes);
            baos.write(this.createdDate.getBytes(StandardCharsets.UTF_8));
            baos.write(password.getBytes(StandardCharsets.UTF_8));
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digestedPassword =  md.digest(baos.toByteArray());
            this.passwordDigest = (Base64.getEncoder()).encodeToString(digestedPassword);

        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
