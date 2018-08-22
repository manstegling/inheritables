# inheritables



## Why inheritables is a definite must-have

I am fed up with untimely run-time exceptions being thrown by Jackson and other frameworks (e.g. anything using `Class::newInstance` when deserializing objects). If you are too, `inheritables` will make that a thing of the past!

In a life of pressing deadlines and last-minute scope changes it's just expected to sometimes forget adding an empty constructor next to those constructors you yourself use and care about in your everyday life. Such a mistake can normally not only be costly time-wise but is oftentimes also quite embarassing. To make the world a better place I therefore decided to create this project.

The key to success was to extend the concept of 'annotation inheritance' to also allow **passing down annotations via interfaces**. 

This project comes with an annotation called `@RequireDefaultConstructor`. If a concrete class implements an interface (or extends a superclass) somewhere in the type hierarchy having this annotation the class will be checked for having a default constructor (a.k.a. "empty constructor"). If such a constructor is missing the class will fail to compile. This check is done using the special annotation processor `DefaultConstructorProcessor`.

I figured this notion of _extended inheritance_ could be useful for other purposes as well, so I've made it easy for you to implement your own checks as well.



## Using the @RequireDefaultConstructor annotation

If you want to use the annotation `@RequireDefaultConstructor` with the associated annotation processor `DefaultConstructorProcessor`, you will need the following two dependencies:

```xml
  <dependency>
      <groupId>se.motility.inheritables</groupId>
      <artifactId>inheritables-annotations</artifactId>
      <version>1.0.0</version>
  </dependency>
  <dependency>
      <groupId>se.motility.inheritables</groupId>
      <artifactId>inheritables-processor</artifactId>
      <version>1.0.0</version>
      <optional>true</optional>
  </dependency>
```

To get the annotation processing to run as part of your compilation you will then need to add the processor to the `maven-compiler-plugin`:

```xml
  <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.8.0</version>
    <configuration>
      <source>1.8</source>
      <target>1.8</target>
      <annotationProcessors>
        <!-- Annotation processor -->
        <annotationProcessor>se.motility.inheritables.processor.DefaultConstructorProcessor</annotationProcessor>
      </annotationProcessors>
    </configuration>
  </plugin>
```

Voilà, simply annotate your **Message** super-interface with `@RequireDefaultConstructor` and all your message types will automatically be checked at compile-time -- saving you from embarrassing run-time exceptions!

#### Extra config needed when building incrementally [Eclipse]

To enjoy the functionality of annotation processing at compile-time when using Eclipse the following configuration is required:

1. Right-click on your project in 'Package Explorer'
2. Select 'Properties'
3. Navigate to Java Compiler -> Annotation Processing
4. Check 'Enable project specific settings'
5. Navigate to Java Compiler -> Annotation Processing -> Factory Path
6. Check 'Enable project specific settings'
7. Use 'Add Variable...' to add the following two variables:

 * M2_REPO/se/motility/inheritables/inheritables-annotation/1.0.0/inheritables-annotation-1.0.0.jar
 * M2_REPO/se/motility/inheritables/inheritables-processor/1.0.0/inheritables-processor-1.0.0.jar


## Creating your own inheritable annotations

The concept of inheritable annotations can be used in many areas beyond checking for the presence of a default constructor. 

### Create and configure a new module/project

If you want to create your own annotation and associated annotation processor, simply _create a new module in your project_ having `inheritables-processor` as a dependency. The reason for needing a separate module is that special build configuration is required for annotation processors, as per below:

#### 1. 

Use the argument `-proc:none` when compiling the annotation processor to prevent the processor from trying to compile itself.

Example of build configuration to do this:

```xml
  <build>
    <plugins>
      <plugin>
      <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.8.0</version>
        <configuration>
          <source>1.8</source>
          <target>1.8</target>
          <!-- Disable annotation processing -->
          <compilerArgument>-proc:none</compilerArgument>
        </configuration>
      </plugin>
    </plugins>
  </build>
```

#### 2.

Register your annotation processor by adding a new file named `javax.annotation.processing.Processor` to `src/main/resources/META-INF/services`. In this file, add a line containing the fully qualified name for your annotation processor.

For the module containing the `DefaultConstructorProcessor` the file looks like below:


> se.motility.inheritables.processor.DefaultConstructorProcessor



### Create an annotation processor

To create your own annotation processor create a new class extending `AbstractInheritableAnnotationProcessor` and implement the abstract methods. 

Please note that your processor class must be annotated with the following two annotations to function correctly:

```
  @SupportedSourceVersion(SourceVersion.RELEASE_8)
  @SupportedAnnotationTypes("*")
```

Simply return the class of the annotation you want to use in the method `getAnnotationClass` in your annotation processor. You can use an already existing annotation or create a new annotation for this purpose. Please note that if you are creating a new annotation class, this class must live in a different module/project than annotation processor!

See the `AbstractInheritableAnnotationProcessor` javadoc for more information on implementation. 

To enable processing with your newly created annotation processor in your project follow the steps described in [Using the @RequireDefaultConstructor annotation](#using-the-requiredefaultconstructor-annotation).



## Comments

At this time only annotations _without attributes_ ("markers") have been considered. Therefore, this API does not give the possibility to retrieve attributes of a detected annotation. For more on multiple inheritance and associated problems, see [The Diamond Problem](https://en.wikipedia.org/wiki/Multiple_inheritance#The_diamond_problem).

 ----
 
Måns Tegling, 2018

 