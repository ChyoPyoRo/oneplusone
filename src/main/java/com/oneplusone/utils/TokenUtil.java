package com.oneplusone.utils;

import com.oneplusone.enums.errors.CustomException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenUtil {
  //jwt를 사용하지 않고 간단하게 UUID값을 가지고 암호화한 토큰을 사용
  @Value("${token.key}")
  private String secretToken;
  private final long EXPIRATION_TIME_MS = 1000 * 60 * 30; // 30분

  public  String encrypt(String uuid) {
    long timestamp = System.currentTimeMillis();
    String value = uuid + ":" + timestamp;
    try {
      SecretKeySpec key = new SecretKeySpec(secretToken.getBytes(), "AES");
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, key);
      byte[] encrypted = cipher.doFinal(value.getBytes());
      return Base64.getEncoder().encodeToString(encrypted);
    } catch (Exception e) {
      throw new RuntimeException("Encrypt error", e);
    }
  }

  public  String decrypt(String token) throws Exception {
      SecretKeySpec key = new SecretKeySpec(secretToken.getBytes(), "AES");
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(Cipher.DECRYPT_MODE, key);
      byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(token));
      String[] parts = new String(decrypted).split(":");
      String uuid = parts[0];
      long timestamp = Long.parseLong(parts[1]);
      if (System.currentTimeMillis() - timestamp > EXPIRATION_TIME_MS) {
        throw new CustomException("Token expired");
      }
      return uuid;

  }

}
