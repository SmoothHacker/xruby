/** 
 * Copyright 2007 Ye Zheng
 * Distributed under the GNU General Public License 2.0
 */

package com.xruby.compiler.codegen;

public class ClassGeneratorForVarArgRubyMethod extends ClassGeneratorForRubyMethod {
	public ClassGeneratorForVarArgRubyMethod(String method_name, String fileName, String name, int argc, boolean has_asterisk_parameter, int default_argc, boolean is_singleton_method) {
		super(method_name, fileName, name, argc, has_asterisk_parameter, default_argc,
				is_singleton_method);
	}
	
	public String getSuperName() {
		return "com/xruby/runtime/lang/RubyVarArgMethod";
	}
	
	public String getRunMethodName() {
		return "com.xruby.runtime.lang.RubyValue run(com.xruby.runtime.lang.RubyValue, com.xruby.runtime.value.RubyArray, com.xruby.runtime.lang.RubyBlock)";
	}
	
	public Class getSuperClass() {
		return Types.RubyVarArgMethodClass;
	}

	public String getSuperCtorName() {
		return "void <init> (int, boolean, int)";
	}
	
	public void pushBasciArgForSuperArg(MethodGenerator mg, int argc, boolean has_asterisk_parameter, int default_argc) {
		mg.push(argc);
		mg.push(has_asterisk_parameter);
		mg.push(default_argc);			
	}
	
	public void callSuperMethod() {
		this.getMethodGenerator().RubyAPI_callSuperMethod(this.getMethodName());
	}
}
