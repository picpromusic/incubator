Title: Enable changes in visibility of non private fields in a binary compatible way
Author: SponsorRequired, Sebastian Sickelmann
Owner: SponsorRequired, Sebastian Sickelmann
Created: 2012/MM/DD
Type: Feature/Research
State: Draft
Exposure: Open
Component: --/--
Scope: SE
JSR: TBD
Discussion: jk8 dash dev at openjdk dot java dot net
Start: 2012/Q3
Effort: L
Duration: XL
Template: 1.0
Reviewed-by: TBD
Endorsed-by: TBD
Funded-by: Sebastian Sickelmann, Other contributors are welcome

Summary
-------

Actually it is not possible to remove non-private fields or limit the visibility of existing fields without breaking binary compatibility.
Where binary compatility is not a major issue in most application projects itis a somewhat bigger issue in library projects and a must have in APIs that are part of the JDK.
The suggested changes to the jvm allows it to remove non-private fields or change the visibility of non-private fields in a binary compatible way.

Non-Goals
---------

The suggested change does *not* want to change Java *(The Language)*.
It is *not* intended to allow field access through accessor(get/set) methods with simple field/property access syntax.

> instanceOrClass.fieldname

It would be possible to also support one special binary-compatible change to the static property of an field (from non-static to static) but this is out of focus.
However this would also be possible in an implementation-specific way. 

Changes in visibility of fields should be possible in a binary compatible way.
This does **not** ensure that the changes are fully runtime compatible.
And it definitivly breaks source compatibility in Java*(The Language)*, other languages that run ontop of the jvm can implement this differently. 

Motivation
----------

This will enable the OpenJDK-Contibutors to make some changes to the APIs and implementations that would otherwise be non binary compatible.
Futhermore all Java Library Projects can make more changes in a binary compatible way.
It is not a must have for Library Projects but a good investment in existing Java Software.
The only competitor i had seen are some suggestions to make changes to Java*(The Language)* to enable source-level use to accessor-methods via field/property access syntax.
This JEP does not want to change the language. Anyway a change to Java*(The Language)* that enables the use of accessor-methods via field/property access syntax should also observe binary compatibility in some way. 

Description
-----------

If you limit the visibility of an existing(older version of your implementation) field than callsites- (independent if it is byte-code or source-code) that accesses this field maybe (depending of the relationship of the callsite and the changed field) no longer valid.
You get compiler-errors for source-code and linkage-errors for byte-code.
While this maybe not a problem for API- and application-projects it is a real show-stopper, for limiting the visibility of existing fields, for APIs like the JDK provides.
While source-code compatibility is not a major case for the JDK-APIs binary-compatibility is. 
(todo: search and link the article from joe darcy about different compatibility-levels).

Field-Access in the jvm is done via the GETSTATIC*(0xB2)*,PUTSTATIC*(0xB3)*,
GETFIELD*(0xB4)*,PUTFIELD*(0xB5)* bytecodes.
This byte-codes are more or less simple (with some visibility checks) accesses to fields(static or instance-level).
If there is a field access through these byte-codes and the field to access in not visibile for the call-site you get linkage errors.
If you use the static variants to access an non-static field and vice-versa you UncompatibleClassChangeErrors.
Primary we want to get a solution to the linkage error case(change in visibity).
I can imaging three solution how this can be solved.
I think one of the solutions 1 and 2 should be choosen.
Solution 3 is compatible with solution 1 and solution 2 and can be combined with them.

### Solution 1: Field resolution is ambiguous and linkage is not callsite-class specific.

If there is an access to a field, regardless if it's static or non-static, and the callsite cannot resolve it, than the jvm searches for methods(now called accessor-methods), which have the same "static/non-static" property (as the bytecode enforces) that gives access to this field and is visible to the callsite.

This resoltion step, which is discussed in more detail later, is inserted between the steps 1 and 2 of JVMS7 5.4.3.2. 
The accessor-method needs to be marked by an annotation with RuntimeRetension (ex. java/lang/Accessor) in source-code.

The Accessor-Annoation has a property "value" that contains the name of the supported field. 

The matching method for **PUTSTATIC** and **PUTFIELD** has one single parameter which descriptor equals to the expected fieldtype.
The returntype for this method must be void. 

The matching method for **GETSTATIC** and **GETFIELD** has no parameters and a returnvaluetype which descriptor equals to the expected fieldtype.

There can only be two methods (one for GET and one for PUT) with the same fieldname. 
If there are multiple methods with the same fieldname an unambiguous accessor error is thrown while verifying the class that contains the unambiguous accessor-methods.
If the class contains accessor-methods and a field with the same name than an unambiguous field error is thrown. 
However this situation should be checked at compiletime of the class with the unambiguous field / field-access-methods.

If there is no method matching in the actually class the method lookup is recursively continued with the Field Resolution in superinterfaces (JVMS7 5.4.3.2 (2)).

This solution would lead to the following two workflows for changing visibility of fields in a binary compatible way.

1. for removing the original field --> just implement two methods (GET,PUT) that enables "virtual"-access to the "old-field".
2. for changing visibility of on original field --> refactor the original field to another fieldname and implement two methods (GET,PUT) that enables the "virtual"-access to the old field.

In both cases the new methods should have the visibility and static/non-static property of the original field.
 
This solution may introduces some unperfect fieldnames.
You maybe want to introduce some set/get-method style checks to your field access and don't want to loose your perfectly matching fieldname.
This is possible if we choose solution 2.


### Solution 2: Field resolution is not ambiguous anymore and linkage is callsite-class specific.

Additional to the reason, in solution 1, that an field cannot be resolved, this solution also applies if the field looked up is not visible to the call-site.
So there is an additional place compared to solution 1 where it is inserted into resolution of the field. 
Now it not just inserted between the steps 1 and 2 of JVMS7 5.4.3.2 (as in solution 1) but also before the second step after a failed visibility check for an resolved field that would otherwise lead directly to an IllegalAccessError. 

In Solution 1 the field is ambiguous (there is excatly one field with a name or a pair of accessor-methods for a virtual field).
In Solution 2 there can be a real field and multiple pairs of accessor-methods. (But only one for each visibility-level). 

If there are multiple method-pairs that matches the requirement the method with the least visiblity needed is choosen. 
If there are multiple methods-pairs with the same visibility an unambiguous accessor error is thrown while verifying the class that contains the unambiguous accessor-methods.

However this situation should be checked at compiletime of the class with the unambiguous field access-methods.

If there is no matching method in the actually class that method lookup is recursively continued with the Field Resolution in superinterfaces (JVMS7 5.4.3.2 (2)) as in solution 1.
Additionally in solution 2: if the process has already found the field (real-field or method-pair) but it was not accessible leads now to the IllegalAccessError.

I see one major downside in this approach.
It introduces an callsite-class dependent resolution.
A private field which is accessed inside the declaring class directly (which must be ensured) but is accessed from other classes via the accessor-method (which may or may not use the private field).
This has also some impact on reflection compatiblity which may be lead to confusing problems. 

### Solution 3: Field Access binding is delegated to an bootstrap method

Instead of creating special methods for field acceses we can create a method that describes the binding for us.
This method is marked with an annotation (ex. java/lang/AccessorBootstrapMethod) and accepts three parameters.
The first parameter is the name of the field to be bound, the second is the callsite who want to access the field and the third is an "unbounded/invalid" MethodHandle/MethodTyped to the virtual field.
The returnvalue of the method must be an MethodHandle to the accessormethod to use.
All posibilities that are valid for invokedynamic MethodHandles are available.
The resulting Methodhandle must meet the requirements (type and static/non-static property) of the corrosponding field access. 


### Changes to Reflection

For strict binary compatibility we need no changes to reflection functionality. But this
would break most reflective-code that works with the changed fields. Where the byte-code
way to access a field works, reflection doesn't work anymore. Sure reflective code can
find the accessor-methods on their on, but code that is written before the introduction 
of accessor-methods (as discussed here) would break.

The following methods in the java/lang/Class and java/lang/reflect/Field implementation can be changed/expanded to 
provide compatibility access to the "virtual fields". 

Changes to existing Methods:

1.  Field[]                Class.getFields()
2.  Field                  Class.getField(String)
3.  Field[]                Class.getDeclaredFields()
4.  Field                  Class.getDeclaredField(String)

5.  Annotation[]           Field.getDeclaredAnnotations()
6.  <T extends Annotation> Field.getAnnotation(Class<T>)
7.  boolean                Field.isSynthetic()

Details: 

1. The returned Array contains both fields and accesor-methods (as emulated fields) but only one of these for each field name. Because there can only a public field or a public accesor-method for an emulated field.
2. The returned Field is just the accesor-method as emulated field which matches the given field name. If there are(get/set) no such methods it returns the field with the given name if there is such a field.
3. The returned Array contains all fields and accessor-methods (as emulated fields). In Solution 2 there can be multiple values for the same field name. This is this way because there can be a field with the same name as the more accessible accessor-method. Ex. a field with private accesibility and an public accesor-method with the same fieldname.
4. The returned Field is just the accesor-method as emulated field which matches the given field name. If there are(get/set) no such methods it returns the field with the given name if the is such a field.
5. 

New Methods:

1.  Field[] Class.getFields(boolean)
2.  Field[] Class.getField(String,boolean)
3. Field[] Class.getDeclaredFields(boolean)
4. Field[] Class.getDeclaredField(String,boolean)

5. boolean                Field.isEmulated()
6. Method                 Field.getGetAccessor()
7. Method                 Field.getSetAccessor()

Details:


### Generics


### Signed-Code / Security-Related-Code
Maybe the suggested change to the bytecode semantics should not be applied to signed code that is compiled for a jvm version prior to the suggested change.
Lets say JVM(0) is the version which comes with the changed bytecode semantics.
And JVM(<0) are versions before that and JVM(>0) are versions after JVM(0).
Than maybe the suggested changes to the bytecode semantics should not be applied to siged code that is compiled for JVM(<0).
Some implementors of security related implementations or signed code what to turn of this compatibility function even if they compile against JVM(>0), so this feature should be turned of in some way.
It is possible to turn of this compatible-mode in varios levels from instruction to jar/(jigsaw-module). 
 - Annotation (instruction/method/class)
 - MANIFEST (jar/jigsaw-module)



// REQUIRED -- Describe the enhancement in detail: Both what it is and,
// to the extent understood, how you intend to implement it.  Summarize,
// at a high level, all of the interfaces you expect to modify or extend,
// including Java APIs, command-line switches, library/JVM interfaces,
// and file formats.  Explain how failures in applications using this
// enhancement will be diagnosed, both during development and in
// production.  Describe any open design issues.
//
// This section will evolve over time as the work progresses, ultimately
// becoming the authoritative high-level description of the end result.
// Include hyperlinks to additional documents as required.

Alternatives
------------

I had implemented a very small concept-prototype with bytecode-manipulation through the PreMain-Hook.
We can simply transform all loaded classes to use lasy linkage through invokedynamic for field accesses.
The implementation level impact would be much smaller, but i think the runtime costs would be to high for this approach.
We could limit lasy linkage(invokedynamic) for field access only for fields that are not visible/accessible at classload time(ex. the target class is not loaded yet), leave accessible field as they are and change field access that are not directly accesible but are
accessible through access-methods to calling the access methods. 

We could also handle every field access as an call to an accessor-method in first place and only if there is a field that is directly accessible from the callsite change this to direct field access.
This solution has an even bigger performance cost, because field access to private fields is naturally much more common.

One other problem i see with bytecode transformation is signed/sealed code. I don't think that this code should be changed.
But maybe it is prefareable for signed/seald not to change the bytecode semantics as well. 

Testing
-------

Performance
Security (for signed code)
// What kinds of test development and execution will be required in order
// to validate this enhancement, beyond the usual mandatory unit tests?
// Be sure to list any special platform or hardware requirements.

Risks and Assumptions
---------------------

The linkage process is slightly more complicated that it is today.
So the startup-time for cases that uses the compatiblity layer for field access will startup more slowly.
The semantic of an long existing bytecode would be changed with an somewhat more dificult/diffuse meaning. 
Simple (in some way statically linked) field accesses are replaced with method calls with implicit semantics and possible exception occurance that can surpress other (security related) exceptions.
Library Project that uses the new compatiblity field access can effect existing applications in new ways. 
The simple fast access to a simple field is replaced throught a method call which can be (depending of the code inside the method) somewhat slower.
If the field has some concurrency carateristics this can even result in new concurrency problems that are hard to find.
Applications Projects that are implicity using the new compatiblity field access can run into some realy late(production) runtime exceptions where the binary imcompatible change to the field would had resulted in an early (test-stage/startup) linkage error.

Dependences
-----------

// Describe all dependences that this JEP has on other JEPs, components,
// products, or anything else.  Dependences upon other JEPs should also
// be listed in the "Depends:" header at the top of the file.
//
// Describe any JEPs that depend upon this JEP, and likewise make sure
// they are listed in the "Blocks:" header at the top of this file.

Impact
------

// How will this work impact other parts of the platform, the product,
// and the contributors working on them?  Omit any irrelevant items.

  - Other JDK components: ...
  - Compatibility: The changes that are possible after this work is done are source incompatible.
  - Security: ...
  - Performance/scalability: ...
  - User experience: ...
  - I18n/L10n: ...
  - Accessibility: ...
  - Portability: ...
  - Packaging/installation: ...
  - Documentation: ...
  - TCK: ...
  - Other: ...