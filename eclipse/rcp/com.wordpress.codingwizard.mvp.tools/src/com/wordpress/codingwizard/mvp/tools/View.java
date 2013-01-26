package com.wordpress.codingwizard.mvp.tools;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

@javax.inject.Qualifier
@Documented
// Target hint not used by Eclipse 4 
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface View {

	Class display();

}
