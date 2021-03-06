/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.jsendnsca.encryption;

import static javax.crypto.Cipher.*;

import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Triple DES encryption
 *
 * @author krisajenkins
 */
public class TripleDESEncryptor implements Encryptor {

    private static final String DES_ALGORITHM = "DESede";
    private static final String DES_TRANSFORMATION = "DESede/CFB8/PKCS5Padding";

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.jsendnsca.encryption.Encryptor#encrypt(byte[],
     * byte[], java.lang.String)
     */
    public void encrypt(byte[] passiveCheckBytes, byte[] initVector, String password) {
        try {
            final byte[] keyBytes = toFixedSizeByteArray(password.getBytes("US-ASCII"), 24);
            final byte[] initVectorBytes = toFixedSizeByteArray(initVector, 8);

            final SecretKey key = new SecretKeySpec(keyBytes, DES_ALGORITHM);
            final IvParameterSpec iv = new IvParameterSpec(initVectorBytes);

            final Cipher cipher = Cipher.getInstance(DES_TRANSFORMATION);
            cipher.init(ENCRYPT_MODE, key, iv);
            final byte[] cipherText = cipher.doFinal(passiveCheckBytes);

            System.arraycopy(cipherText, 0, passiveCheckBytes, 0, passiveCheckBytes.length);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] toFixedSizeByteArray(byte[] source, int fixedLength) {
        byte[] result = new byte[fixedLength];

        for (int i = 0; i < fixedLength && i < source.length; i++) {
            if (i < source.length) {
                result[i] = source[i];
            } else {
                result[i] = 0;
            }
        }

        return result;
    }
}
