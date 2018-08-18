package se.motility.inheritables.processor;

import java.net.MalformedURLException;

import javax.tools.JavaFileObject;

import org.junit.Test;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.JavaFileObjects;

/**
 * White Box Tests covering all states and transitions
 * 
 * @author M. Tegling
 *
 */
public class InheritanceCacheTest {

    @Test
    public void multipleClassesImplementingSameNonAnnotatedInterfaceCacheTest()
            throws MalformedURLException {
        JavaFileObject resource1 = JavaFileObjects
                .forResource("test/inheritance/UnaffectedClassImplementingInterface2.java");
        JavaFileObject resource2 = JavaFileObjects
                .forResource("test/inheritance/UnaffectedClassImplementingInterface1.java");
        Compilation compilation = TestUtils.compile(resource1, resource2);
        CompilationSubject.assertThat(compilation)
            .succeeded();
    }
    
    @Test
    public void multipleClassesImplementingSameAnnotatedInterfaceCacheTest()
            throws MalformedURLException {
        JavaFileObject resource1 = JavaFileObjects
                .forResource("test/inheritance/CorrectAnnotationInheritingClass2.java");
        JavaFileObject resource2 = JavaFileObjects
                .forResource("test/inheritance/CorrectAnnotationInheritingClass1.java");
        Compilation compilation = TestUtils.compile(resource1, resource2);
        CompilationSubject.assertThat(compilation)
            .succeeded();
    }
    
    @Test
    public void multipleNonAnnotatedSubclassesCacheTest()
            throws MalformedURLException {
        JavaFileObject resource1 = JavaFileObjects
                .forResource("test/inheritance/UnaffectedSubclass2.java");
        JavaFileObject resource2 = JavaFileObjects
                .forResource("test/inheritance/UnaffectedSubclass1.java");
        Compilation compilation = TestUtils.compile(resource1, resource2);
        CompilationSubject.assertThat(compilation)
            .succeeded();
    }
    
    @Test
    public void multipleAnnotatedSubclassesCacheTest()
            throws MalformedURLException {
        JavaFileObject resource1 = JavaFileObjects
                .forResource("test/inheritance/CorrectAnnotationInheritingClass3.java");
        JavaFileObject resource2 = JavaFileObjects
                .forResource("test/inheritance/IncorrectAnnotationInheritingClass3.java");
        Compilation compilation = TestUtils.compile(resource1, resource2);
        CompilationSubject.assertThat(compilation)
            .failed();
    }
    
    @Test
    public void directAnnotationSuccess()
            throws MalformedURLException {
        JavaFileObject resource = JavaFileObjects
                .forResource("test/defaultconstructor/NoConstructorClass.java");
        Compilation compilation = TestUtils.compile(resource);
        CompilationSubject.assertThat(compilation)
            .succeeded();
    }
    

    
}
