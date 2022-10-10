package com;

import java.io.File;
import java.io.IOException;

public class SimpleExecutor {
    public Process exec(File path, String... args) {
        try {
            return new ProcessBuilder()
                    .directory(path)
                    .command(args)
                    .start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Process exec(File path,
                        ProcessBuilder.Redirect in,
                        ProcessBuilder.Redirect err,
                        String... args) {
        try {
            return new ProcessBuilder()
                    .directory(path)
                    .redirectInput(in)
                    .redirectError(err)
                    .command(args)
                    .start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Process exec(File path,
                        ProcessBuilder.Redirect out,
                        ProcessBuilder.Redirect in,
                        ProcessBuilder.Redirect err,
                        String... args) {
        try {
            return new ProcessBuilder()
                    .directory(path)
                    .command(args)
                    .redirectOutput(out)
                    .redirectInput(in)
                    .redirectError(err)
                    .command(args)
                    .start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
