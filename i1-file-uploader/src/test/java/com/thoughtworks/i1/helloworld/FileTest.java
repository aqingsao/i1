package com.thoughtworks.i1.helloworld;

import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileTest {
    @Test
    public void test() throws IOException {
        File file = new File("/tmp/a.txt");
        FileWriter fileWriter = new FileWriter(file);
        try {
            fileWriter.append("Just a test");
        } finally {
            fileWriter.close();
        }
    }
}
