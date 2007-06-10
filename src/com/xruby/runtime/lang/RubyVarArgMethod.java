/** 
 * Copyright 2007 Ye Zheng
 * Distributed under the GNU General Public License 2.0
 */

package com.xruby.runtime.lang;

import com.xruby.runtime.value.RubyArray;

public abstract class RubyVarArgMethod extends RubyMethod {
	public RubyVarArgMethod() {
		super(-1, false, 0);
	}
	
	public RubyVarArgMethod(int argc, boolean has_asterisk_parameter, int default_argc) {
		super(argc, has_asterisk_parameter, default_argc);
	}
	
	protected abstract RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block);
	
	protected RubyValue run(RubyValue receiver, RubyValue arg, RubyArray args, RubyBlock block) {
		if (null != arg) {
			return this.run(receiver, new RubyArray(arg), block);
		} else {
			return this.run(receiver, args, block);
		}
	}

	protected RubyValue run(RubyValue receiver, RubyBlock block) {
		return this.run(receiver, (RubyArray)null, block);
	}

	protected RubyValue run(RubyValue receiver, RubyValue arg, RubyBlock block) {
		return this.run(receiver, new RubyArray(arg), block);
	}
}
