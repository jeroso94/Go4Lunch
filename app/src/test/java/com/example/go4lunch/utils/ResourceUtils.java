package com.example.go4lunch.utils;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * Created by JeroSo94 on 24/05/2022.
 */
public class ResourceUtils {

    @NonNull
    public static String getResourceContent(Object testClass, @NonNull String uri) throws IOException {
        @SuppressWarnings("ConstantConditions")
        InputStream inputStream = testClass.getClass().getClassLoader().getResourceAsStream(uri);

        if (inputStream != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                inputStream.close();
            }
            return writer.toString();
        } else {
            throw new IllegalStateException("No resource found for uri = " + uri);
        }
    }
}
