package se.motility.inheritables.processor;

import java.net.MalformedURLException;

import javax.tools.JavaFileObject;

import org.junit.Test;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.JavaFileObjects;

import se.motility.inheritables.processor.DefaultConstructorProcessor;

/**
 * Tests covering the functionality of the {@link DefaultConstructorProcessor}
 * annotation processor.
 * 
 * @author M. Tegling
 *
 */
public class DefaultConstructorProcessorTest {

    @Test
    public void succeedWhenNoConstructorPresent() throws MalformedURLException {
        JavaFileObject resource = JavaFileObjects
                .forResource("test/defaultconstructor/NoConstructorClass.java");
        Compilation compilation = TestUtils.compile(resource);
        CompilationSubject.assertThat(compilation)
            .succeeded();
    }

    @Test
    public void failWhenMissingEmptyConstructor() throws MalformedURLException {
        JavaFileObject resource = JavaFileObjects
            .forResource("test/defaultconstructor/MissingDefaultConstructorClass.java");
        Compilation compilation = TestUtils.compile(resource);
        CompilationSubject.assertThat(compilation)
            .failed();
    }

    @Test
    public void succeedWhenEmptyConstructorIsPresentAmongMultiple() throws MalformedURLException {
        JavaFileObject resource = JavaFileObjects
                .forResource("test/defaultconstructor/MultipleConstructorsClass.java");
        Compilation compilation = TestUtils.compile(resource);
        CompilationSubject.assertThat(compilation)
            .succeeded();
    }


}

