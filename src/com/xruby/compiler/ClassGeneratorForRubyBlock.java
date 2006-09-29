package com.xruby.compiler;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.Method;

import com.xruby.core.lang.*;
import java.util.*;

class ClassGeneratorForRubyBlock extends ClassGenerator {

	private final SymbolTable symbol_table_of_the_current_scope_;
	private final int argc_;
	private final boolean has_asterisk_parameter_;
	Set<String> fields_ = new HashSet<String>();
	
	public void loadVariable(String name) {
		if ((symbol_table_of_the_current_scope_.getLocalVariable(name) != null ||
			symbol_table_of_the_current_scope_.getMethodParameter(name) >= 0) &&
			getSymbolTable().getMethodParameter(name) < 0) {
			fields_.add(name);
			mg_for_run_method_.loadThis();
			mg_for_run_method_.getField(Type.getType("L" + name_ + ";"), name, Type.getType(RubyValue.class));
		} else {
			super.loadVariable(name);
		}
	}
	
	public ClassGeneratorForRubyBlock(String name,
			int argc,
			boolean has_asterisk_parameter,
			SymbolTable symbol_table_of_the_current_scope) {
		super(name);
		mg_for_run_method_ = visitRubyBlock(argc, has_asterisk_parameter);
		symbol_table_of_the_current_scope_ = symbol_table_of_the_current_scope;
		argc_ = argc;
		has_asterisk_parameter_ = has_asterisk_parameter;
	}
	
	protected Class getType() {
		return RubyBlock.class;
	}

	private MethodGenerator visitRubyBlock(int argc, boolean has_asterisk_parameter) {
		cw_.visit(Opcodes.V1_5,
				0,		//No modifier
				name_,	
				null,								// signature
				"com/xruby/core/lang/RubyBlock",	// superName
				null								// interface
				);
	
		return new MethodGenerator(Opcodes.ACC_PROTECTED,
				Method.getMethod("com.xruby.core.lang.RubyValue run(com.xruby.core.lang.RubyValue, com.xruby.core.value.ArrayValue)"),
				null,// signature
				new Type[] {Type.getType(RubyException.class)},// Type[] exceptions
				cw_);
	}

	static String buildContructorSignature(int size) {
		StringBuilder method_name = new StringBuilder("void <init> (");
		for (int i = 0; i < size; ++i) {
			if (0 != i) {
				method_name.append(", ");
			}
			method_name.append("com.xruby.core.lang.RubyValue");
		}
		method_name.append(")");
		return method_name.toString();
	}
	
	public String[] createFieldsAndConstructorOfRubyBlock() {
		String[] commons = fields_.toArray(new String[fields_.size()]);
		createConstructorOfRubyBlock(commons);
		createFields(commons);
		return commons;
	}
	
	private void createFields(final String[] commons) {
		for (String name : commons) {
			FieldVisitor fv = cw_.visitField(Opcodes.ACC_PRIVATE,
					name,
					Type.getDescriptor(RubyValue.class),
					null,
					null
					);
			fv.visitEnd();
		}
	}
	
	private void createConstructorOfRubyBlock(final String[] commons) {
		
		MethodGenerator mg = new MethodGenerator(Opcodes.ACC_PUBLIC,
				Method.getMethod(buildContructorSignature(commons.length)),
				null,
				null,
				cw_);
		
		mg.loadThis();
		mg.push(argc_);
		mg.push(has_asterisk_parameter_);
		mg.invokeConstructor(Type.getType(RubyBlock.class),
						Method.getMethod("void <init> (int, boolean)"));
		
		for (int i = 0; i < commons.length; ++i) {
			mg.loadThis();
			mg.loadArg(i);
			mg.putField(Type.getType("L" + name_ + ";"), commons[i], Type.getType(RubyValue.class));
		}
		
		mg.returnValue();
		mg.endMethod();
	}
}
