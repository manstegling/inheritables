package se.motility.inheritables.processor;

import java.net.MalformedURLException;

import javax.tools.JavaFileObject;

import org.junit.Test;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.JavaFileObjects;

/**
 * Black Box Unit Tests for all inheritance scenarios. For each inheritance scenario
 * there is one test with a class having a default constructor (= success) and one
 * test with a class missing the default constructor (= failure). Also includes tests
 * to prove that the processor is not affecting compilation of classes without annotations.
 * 
 * @author M. Tegling
 *
 */
public class InheritanceTest {

    @Test
    public void nonAnnotatedSubClassSuccess() throws MalformedURLException{
        JavaFileObject resource = JavaFileObjects
                .forResource("test/inheritance/UnaffectedSubclass1.java");
        Compilation compilation = TestUtils.compile(resource);
        CompilationSubject.assertThat(compilation)
            .succeeded();
    }
    
    @Test
    public void nonAnnotatedClassImplementingInterfaceSuccess() throws MalformedURLException{
        JavaFileObject resource = JavaFileObjects
                .forResource("test/inheritance/UnaffectedClassImplementingInterface1.java");
        Compilation compilation = TestUtils.compile(resource);
        CompilationSubject.assertThat(compilation)
            .succeeded();
    }
    
    @Test
    public void implementingInterfaceDirectlyNoConstructorSuccess() throws MalformedURLException{
        JavaFileObject resource = JavaFileObjects
                .forResource("test/inheritance/CorrectAnnotationInheritingClass0.java");
        Compilation compilation = TestUtils.compile(resource);
        CompilationSubject.assertThat(compilation)
            .succeeded();
    }
    
    @Test
    public void implementingInterfaceDirectlySuccess() throws MalformedURLException{
        JavaFileObject resource = JavaFileObjects
                .forResource("test/inheritance/CorrectAnnotationInheritingClass1.java");
        Compilation compilation = TestUtils.compile(resource);
        CompilationSubject.assertThat(compilation)
            .succeeded();
    }
    
    @Test
    public void implementingInterfaceDirectlyFail() throws MalformedURLException{
        JavaFileObject resource = JavaFileObjects
                .forResource("test/inheritance/IncorrectAnnotationInheritingClass1.java");
        Compilation compilation = TestUtils.compile(resource);
        CompilationSubject.assertThat(compilation)
            .failed();
    }

    @Test
    public void implementingInterfaceExtendingAnnotatedSuccess() throws MalformedURLException{
        JavaFileObject resource = JavaFileObjects
                .forResource("test/inheritance/CorrectAnnotationInheritingClass2.java");
        Compilation compilation = TestUtils.compile(resource);
        CompilationSubject.assertThat(compilation)
            .succeeded();
    }
    
    @Test
    public void implementingInterfaceExtendingAnnotatedFail() throws MalformedURLException{
        JavaFileObject resource = JavaFileObjects
                .forResource("test/inheritance/IncorrectAnnotationInheritingClass2.java");
        Compilation compilation = TestUtils.compile(resource);
        CompilationSubject.assertThat(compilation)
            .failed();
    }

    @Test
    public void extendingClassImplementingAnnotatedInterfaceSuccess() throws MalformedURLException {
        JavaFileObject resource = JavaFileObjects
                .forResource("test/inheritance/CorrectAnnotationInheritingClass3.java");
        Compilation compilation = TestUtils.compile(resource);
        CompilationSubject.assertThat(compilation)
            .succeeded();
    }
    
    @Test
    public void extendingClassImplementingAnnotatedInterfaceFail() throws MalformedURLException {
        JavaFileObject resource = JavaFileObjects
                .forResource("test/inheritance/IncorrectAnnotationInheritingClass3.java");
        Compilation compilation = TestUtils.compile(resource);
        CompilationSubject.assertThat(compilation)
            .failed();
    }

}
