package se.motility.inheritables.processor;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

/**
 * Abstract annotation processor traversing the type hierarchy to identify classes inheriting
 * a specific annotation. What annotation to look for is determined by the concrete processor.
 * <p>
 * In this project the concept of 'annotation inheritance' is extended to also allow passing 
 * down annotations via interfaces.
 * <p>
 * At this time only annotations <i>without attributes</i> ("markers") have been considered.
 * Therefore, this API does not give the possibility to retrieve attributes of a detected
 * annotation. (For more on multiple inheritance and associated problems, see 
 * <a href="https://en.wikipedia.org/wiki/Multiple_inheritance#The_diamond_problem">
 * The Diamond Problem</a>)
 * 
 * @author M Tegling
 *
 */
public abstract class AbstractInheritableAnnotationProcessor extends AbstractProcessor{

    /**
     * Method to analyze each type annotated with the annotation of interest (either directly
     * or via extended inheritance).
     * @param annotatedType the type annotated with the annotation of interest
     * @return <code>false</code> if the annotated type does not satisfy the requirements
     * imposed by the annotation and should fail to compile; <code>true</code> otherwise.
     */
    protected abstract boolean isCorrectlyAnnotated(TypeElement annotatedType);
    
    /**
     * Method for producing the error message to be associated with the compilation
     * failure of the annotated type.
     * @param errorType the annotated type failing the annotation check
     * @return message regarding why the type failed to compile
     */
    protected abstract String getErrorMessage(TypeElement errorType);
    
    
    /**
     * Implement this method to tell the detection algorithm which annotation
     * to look for.
     * @return the class of the annotation of interest
     */
    protected abstract Class<? extends Annotation> getAnnotationType();
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        Set<TypeElement> annotatedInterfaces = new HashSet<>();
        Set<TypeElement> annotatedTypes = new HashSet<>();
        Set<TypeElement> nonAnnotatedElements = new HashSet<>();

        for (TypeElement type : ElementFilter.typesIn(roundEnv.getRootElements())) {
            if (type.getKind().isClass()) {
                // Perform DFS with caching in type hierarchy
                isAnnotatedByInheritance(type, annotatedInterfaces, annotatedTypes, nonAnnotatedElements);
            }
        }

        for (TypeElement annotatedType : annotatedTypes) {
            if (!isCorrectlyAnnotated(annotatedType)) {
                processingEnv.getMessager()
                    .printMessage(Kind.ERROR, getErrorMessage(annotatedType), annotatedType);
            }
        }
        return false;
    }

    private boolean isAnnotatedByInheritance(TypeElement type, Set<TypeElement> annotatedInterfaces,
            Set<TypeElement> annotatedTypes, Set<TypeElement> nonAnnotatedTypes) {
        if (nonAnnotatedTypes.contains(type)) {
            return false;
        } else if (annotatedTypes.contains(type)) {
            return true;
        } else {
            if (isDirectlyAnnotated(type)) {
                // Annotation detected by the compiler
                annotatedTypes.add(type);
                return true;
            }

            if (isAnnotatedByInterface(type.getInterfaces(), annotatedInterfaces, nonAnnotatedTypes)) {
                annotatedTypes.add(type);
                return true;
            }

            // Check superclass hierarchy
            TypeMirror superMirror = type.getSuperclass();
            if (superMirror.getKind() == TypeKind.NONE) {
                nonAnnotatedTypes.add(type);
                return false;
            }

            TypeElement superElement = (TypeElement) ((DeclaredType) superMirror).asElement();
            if (isAnnotatedByInheritance(superElement, annotatedInterfaces, annotatedTypes, nonAnnotatedTypes)) {
                annotatedTypes.add(type);
                return true;
            } else {
                nonAnnotatedTypes.add(type);
                return false;
            }
        }
    }

    // Recursive method to find annotations in interface hierarchy
    private boolean isAnnotatedByInterface(List<? extends TypeMirror> interfaceMirrors,
            Set<TypeElement> annotatedInterfaces, Set<TypeElement> nonAnnotatedInterfaces) {
        for (TypeMirror interfaceMirror : interfaceMirrors) {
            DeclaredType declared = (DeclaredType) interfaceMirror;
            TypeElement ifType = (TypeElement) declared.asElement();
            if (nonAnnotatedInterfaces.contains(ifType)) {
                return false;
            } else if (annotatedInterfaces.contains(ifType)) {
                return true;
            } else if (isDirectlyAnnotated(ifType) || 
                    isAnnotatedByInterface(ifType.getInterfaces(), annotatedInterfaces, nonAnnotatedInterfaces)) {
                annotatedInterfaces.add(ifType);
                return true;
            } else {
                nonAnnotatedInterfaces.add(ifType);
            }
        }
        return false;
    }

    private boolean isDirectlyAnnotated(TypeElement type){
        return type.getAnnotation(getAnnotationType()) != null;
    }

}
