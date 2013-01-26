package com.wordpress.codingwizard.mvp.tools.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Date;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.suppliers.ExtendedObjectSupplier;
import org.eclipse.e4.core.di.suppliers.IObjectDescriptor;
import org.eclipse.e4.core.di.suppliers.IRequestor;

import com.wordpress.codingwizard.mvp.tools.View;

public class PresenterObjectSupplier extends ExtendedObjectSupplier {

	public PresenterObjectSupplier() {
		System.out.println("Created");
	}
	
	@Inject
	IEclipseContext ctx;
	
	@Override
	public Object get(
			IObjectDescriptor descriptor, IRequestor requestor,
			boolean track, boolean group) {
		Class desiredType = (Class) descriptor.getDesiredType();
		View viewAnnotation = (View) desiredType.getAnnotation(View.class);
		Class viewInterface = viewAnnotation.display();
		
		IEclipseContext childCtx = ctx.createChild("presenter");
		childCtx.set(viewInterface, requestor.getRequestingObject());

		return ContextInjectionFactory.make(desiredType, ctx);
	}

}
