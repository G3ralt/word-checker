package com.alexosenov.projects;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;


public class Benchmark {

    public static void main(String[] args) throws RunnerException {

        Options options = new OptionsBuilder()
                .forks(2)
                .threads(50)
                .build();

        new Runner(options).run();
    }
}
