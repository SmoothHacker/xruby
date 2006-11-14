package com.xruby.newruntime.builtin;

import com.xruby.newruntime.lang.RubyBlock;
import com.xruby.newruntime.lang.RubyMethod;
import com.xruby.newruntime.lang.RubyModule;
import com.xruby.newruntime.lang.RubyRuntime;
import com.xruby.newruntime.lang.RubySymbol;
import com.xruby.newruntime.lang.RubyValue;
import com.xruby.newruntime.value.RubyArray;

public class KernelBuilder implements ExtensionBuilder {
	private RubyModule kernelModule;

	public void initialize() {
		this.kernelModule = RubyRuntime.defineModule("Kernel");	
		this.kernelModule.defineMethod("send", KernelMethod.send, -1);
		this.kernelModule.defineMethod("__send__", KernelMethod.send, -1);
	}
}

class KernelMethod {
	public static RubyMethod send = new RubyMethod() {
		protected RubyValue run(RubyValue receiver, RubyArray args,
				RubyBlock block) {
			if (args.length() == 0) {
				RubyRuntime.raise(RubyRuntime.argumentError, "no method name given");
			}
			
			RubySymbol methodSymbol = (RubySymbol)args.remove(0);
			return receiver.callMethod(methodSymbol.toID(), args, block);
		}		
	};
}