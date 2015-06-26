package com.afollestad.silk.utilities;

import java.io.*;

/**
 * @author Aidan Follestad (afollestad)
 */
public class IOUtils {

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    public static void closeQuietly(InputStream input) {
        closeQuietly((Closeable) input);
    }

    private static void closeQuietly(OutputStream output) {
        closeQuietly((Closeable) output);
    }

    private static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

    private static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    private static long copyLarge(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static byte[] inputStreamToBytes(InputStream stream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            IOUtils.copy(stream, byteArrayOutputStream);
        } catch (IOException e) {
            IOUtils.closeQuietly(byteArrayOutputStream);
            return null;
        }
        return byteArrayOutputStream.toByteArray();
    }
}
