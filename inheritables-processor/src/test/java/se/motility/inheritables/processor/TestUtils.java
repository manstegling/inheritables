package se.motility.inheritables.processor;

import javax.tools.JavaFileObject;

import com.google.testing.compile.Compilation;

public class TestUtils {

    public static Compilation compile(JavaFileObject... resources) {
        return com.google.testing.compile.Compiler.javac()
                .withProcessors(new DefaultConstructorProcessor())
                .compile(resources);
    }
    
    private TestUtils() {
        throw new UnsupportedOperationException("Utility class: Do not instantiate");
    }
    
}
