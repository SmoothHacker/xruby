/** 
 * Copyright (c) 2005-2006 Xue Yong Zhi. All rights reserved.
 */

package com.xruby.runtime.lang;

import com.xruby.runtime.value.*;

class ModuleClassAndMethodCollection extends ClassAndMethodCollection {
	public RubyModule defineNewModule(String name) {
		RubyModule m = new RubyModule(name);
		constants_.put(name, new RubyValue(RubyRuntime.ModuleClass, m));//NOTE, do not use ObjectFactory.createClass, it will cause initialization issue
		return m;
	}

	public RubyValue defineModule(String name) throws RubyException {
		RubyValue v = constants_.get(name);
		if (null == v) {
			v = new RubyValue(RubyRuntime.ModuleClass, new RubyModule(name));
			constants_.put(name, v);
			return v;
		}
		
		if (v.getRubyClass() != RubyRuntime.ModuleClass) {
			throw new RubyException(RubyRuntime.TypeErrorClass, name + " is not a module");
		}
		
		return v;
	}

	/// e.g. A::B
	RubyValue getConstant(String name) throws RubyException {
		return constants_.get(name);
	}

	RubyValue setConstant(String name, RubyValue value) {
		constants_.put(name, value);
		return value;
	}

	public RubyValue getCurrentNamespaceConstant(String name) throws RubyException {
		RubyValue v = getConstant(name);
		if (null != v) {
			return v;
		}

		v = RubyRuntime.GlobalScope.getConstant(name);
		if (null == v) {
			throw new RubyException(RubyRuntime.NameErrorClass, "uninitialized constant " + name_ + "::" + name);
		}
		return v;
	}

	private static void throw_type_error_if_not_class_module(RubyValue receiver) throws RubyException {
		if (receiver.getRubyClass() != RubyRuntime.ClassClass &&
			receiver.getRubyClass() != RubyRuntime.ModuleClass) {
			RubyValue v = RubyRuntime.callPublicMethod(receiver, null, "to_s");
			String s = ((StringValue)v.getValue()).toString();
			throw new RubyException(RubyRuntime.TypeErrorClass, s + " is not a class/module");
		}
	}

	public static RubyValue getConstant(RubyValue receiver, String name) throws RubyException {
		throw_type_error_if_not_class_module(receiver);
		
		RubyValue v = ((ModuleClassAndMethodCollection)receiver.getValue()).getConstant(name);
		if (null == v) {
			String module_name = ((ModuleClassAndMethodCollection)receiver.getValue()).getName();
			throw new RubyException(RubyRuntime.NameErrorClass, "uninitialized constant " + module_name + "::" + name);
		}
		
		return v;
	}

	public static RubyValue setConstant(RubyValue value, RubyValue receiver, String name) throws RubyException {
		throw_type_error_if_not_class_module(receiver);

		return ((ModuleClassAndMethodCollection)receiver.getValue()).setConstant(name, value);
	}

	public static RubyValue getTopLevelConstant(String name) throws RubyException {
		RubyValue v = RubyRuntime.GlobalScope.getConstant(name);
		if (null == v) {
			throw new RubyException(RubyRuntime.NameErrorClass, "uninitialized constant " + name);
		}
		return v;
	}

	public static RubyValue setTopLevelConstant(RubyValue value, String name) throws RubyException {
		return RubyRuntime.GlobalScope.setConstant(name, value);
	}
}
