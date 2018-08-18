package test.defaultconstructor;

import se.motility.inheritables.annotations.RequireDefaultConstructor;

@RequireDefaultConstructor
public class MultipleConstructorsClass {
    
    private MultipleConstructorsClass() {}
    
    public MultipleConstructorsClass(Object arg) {}
    
}
