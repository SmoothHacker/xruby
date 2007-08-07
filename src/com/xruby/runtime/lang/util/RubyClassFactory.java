package com.xruby.runtime.lang.util;

import java.lang.annotation.Annotation;

import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;

import com.xruby.compiler.codegen.CgUtil;
import com.xruby.compiler.codegen.Types;
import com.xruby.runtime.lang.RubyClass;
import com.xruby.runtime.lang.RubyModule;
import com.xruby.runtime.lang.RubyValue;
import com.xruby.runtime.lang.annotation.RubyLevelClass;
import com.xruby.runtime.lang.annotation.UndefMethod;

class RubyClassFactory extends RubyTypeFactory {
	RubyClassFactory(Class klass) {
		super(klass);
	}
	
	protected boolean isModule() {
		return false;
	}
	
	protected Class getTypeAnnotationClass() {
		return RubyLevelClass.class;
	}

	protected String getBuilderName(Class klass) {
		return CgUtil.getInternalName(klass.getName() + "$ClassBuilder");
	}

	protected Class getInterface() {
		return RubyClassBuilder.class;
	}

	protected Method getBuilderMethod() {
		return Method.getMethod(CgUtil.getMethodName("createRubyClass", RubyClass.class));
	}
	
	protected String createMethodFactoryName() {
		return "createMethodFactory";
	}

	protected int createRubyType(GeneratorAdapter mg, Annotation annotation) {
		RubyLevelClass klassAnnotation = (RubyLevelClass)annotation;
		mg.push(klassAnnotation.name());
		loadSuperClass(mg, klassAnnotation.superclass());
		mg.invokeStatic(Types.RUBY_API_TYPE, 
				Method.getMethod(CgUtil.getMethodName("defineClass", RubyClass.class, 
						String.class, RubyValue.class)));

		int rubyTypeIdx = mg.newLocal(Types.RUBY_CLASS_TYPE);
		mg.storeLocal(rubyTypeIdx);

		for (String moduleName : klassAnnotation.modules()) {
			includeModule(mg, rubyTypeIdx, moduleName);
		}
		
		for (UndefMethod method : klassAnnotation.undef()) {
			undefMethod(mg, rubyTypeIdx, method);
		}

		return rubyTypeIdx;
	}

	private void includeModule(GeneratorAdapter mg, int rubyTypeIdx, String moduleName) {
		mg.loadLocal(rubyTypeIdx);
		loadModule(mg, moduleName, rubyTypeIdx);
		mg.invokeVirtual(Types.RUBY_MODULE_TYPE, Method.getMethod(
				CgUtil.getMethodName("includeModule", Void.TYPE, RubyModule.class)));
	}
	
	private void undefMethod(GeneratorAdapter mg, int rubyTypeIdx, UndefMethod method) {
		mg.loadLocal(rubyTypeIdx);
		if (method.classMethod()) {
			mg.invokeVirtual(Types.RUBY_VALUE_TYPE, 
					Method.getMethod(CgUtil.getMethodName("getRubyClass", RubyClass.class)));
		}
		
		mg.push(method.name());
		mg.invokeVirtual(Types.RUBY_MODULE_TYPE, Method.getMethod(
				CgUtil.getMethodName("undefMethod", Void.TYPE, String.class)));
	}

	private static void loadSuperClass(GeneratorAdapter mg, String superclass) {
		if (superclass == null || superclass.length() ==  0) {
			mg.push((String)null);
		}
		
		if (Types.isBuiltinClass(superclass)) {
			mg.getStatic(Types.RUBY_RUNTIME_TYPE, 
					superclass + "Class", Types.RUBY_CLASS_TYPE);
		} else {
			// FIXME: 
		}
	}
	
	private void loadModule(GeneratorAdapter mg, String module, int rubyTypeIdx) {
		if (Types.isBuiltinModule(module)) {
			mg.getStatic(Types.RUBY_RUNTIME_TYPE, 
					module + "Module", Types.RUBY_MODULE_TYPE);
		} else {
			mg.loadLocal(rubyTypeIdx);
			mg.push(module);
			mg.invokeStatic(Types.RUBY_API_TYPE, Method.getMethod(
					CgUtil.getMethodName("getCurrentNamespaceConstant", RubyValue.class, RubyModule.class, String.class)));
			mg.checkCast(Types.RUBY_MODULE_TYPE);
		}
	}
}