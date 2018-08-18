package se.motility.inheritables.processor;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import se.motility.inheritables.annotations.RequireDefaultConstructor;


/**
 * Annotation processor analyzing all concrete classes inheriting the annotation {@link 
 * RequireDefaultConstructor} via <i>extended inheritance</i>. The annotation processor
 * will fail compilation for any annotated class not having a default constructor (also 
 * known as an "empty constructor").
 * <p>
 * Having this check made at compile-time is convenient, for example, when using messaging
 * frameworks which work through reflection and thus rely on the existence of a default
 * constructor for calling {@link Class#newInstance()}, such as Jackson. Otherwise,
 * failing to implement a default constructor where necessary can cause very awkward
 * run-time exceptions.
 * <p>
 * NB. Classes with no explicit constructor defined will get a default constructor added
 * by the Java compiler and will hence pass the check.
 * 
 * @author M Tegling
 *
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("*")
public class DefaultConstructorProcessor extends AbstractInheritableAnnotationProcessor{

    private static final String ERROR_MESSAGE = "Missing default constructor. NB: This constructor can be set to 'private' access.";

    @Override
    protected boolean isCorrectlyAnnotated(TypeElement annotatedType) {
        return annotatedType.getEnclosedElements().stream()
                .filter(element -> element.getKind() == ElementKind.CONSTRUCTOR)
                .map(ExecutableElement.class::cast)
                .anyMatch(constructor -> constructor.getParameters().isEmpty());
    }

    @Override
    protected String getErrorMessage(TypeElement errorType) {
        return ERROR_MESSAGE;
    }

    @Override
    protected Class<RequireDefaultConstructor> getAnnotationType() {
        return RequireDefaultConstructor.class;
    }

}
