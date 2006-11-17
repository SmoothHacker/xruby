package com.xruby.runtime.builtin;

import com.xruby.runtime.lang.NoBlockRubyMethod;
import com.xruby.runtime.lang.RubyMethod;
import com.xruby.runtime.lang.RubyNoArgMethod;
import com.xruby.runtime.lang.RubyOneArgMethod;
import com.xruby.runtime.lang.RubyAPI;
import com.xruby.runtime.lang.RubyValue;
import com.xruby.runtime.value.RubyFixnum;
import com.xruby.runtime.value.RubyArray;

class FixnumMethod {
	public static RubyMethod toS = new NoBlockRubyMethod() {
		public RubyValue run(RubyValue receiver, RubyArray args) {
			long base = (null == args) ? 10 : (RubyAPI.valueToLong(args.get(0)));
			RubyFixnum fixnum = (RubyFixnum)receiver;
			return fixnum.toS(base);
		}
	};
	
	public static RubyMethod equal = new RubyOneArgMethod() {
		protected RubyValue run(RubyValue receiver, RubyValue arg) {
			RubyFixnum num = (RubyFixnum)receiver;
			if (arg instanceof RubyFixnum) {
				return RubyAPI.test(num.equals(arg));
			}
			
			// FIXME: num equal
			return null;
		}
	};
	
	public static RubyMethod lshift = new RubyOneArgMethod() {
		protected RubyValue run(RubyValue receiver, RubyValue arg) {
			long value = RubyAPI.valueToLong(receiver);
			long width = RubyAPI.valueToLong(arg);
			if (width < 0) {
				// FIXME: rshift
			}
			
			// FIXME: bigshift
			
			return RubyFixnum.long2Fix(value << width);
		}
	};
		
	public static RubyMethod rshift = new RubyOneArgMethod() {
		protected RubyValue run(RubyValue receiver, RubyValue arg) {
			long value = RubyAPI.valueToLong(receiver);
			long width = RubyAPI.valueToLong(arg);
			if (width < 0) {
				// FIXME: lshift
			}
			
			// FIXME: bigshift
			
			return RubyFixnum.long2Fix(value >> width);
		}
	};
	
	public static RubyMethod plus = new RubyOneArgMethod() {
		protected RubyValue run(RubyValue receiver, RubyValue arg) {
			long left = RubyAPI.valueToLong(receiver);
			long right = RubyAPI.valueToLong(arg);
			return RubyFixnum.long2Fix(left + right);
		}
	};
	
	public static RubyMethod minus = new RubyOneArgMethod() {
		protected RubyValue run(RubyValue receiver, RubyValue arg) {
			long left = RubyAPI.valueToLong(receiver);
			long right = RubyAPI.valueToLong(arg);
			return RubyFixnum.long2Fix(left - right);
		}
	};
	
	public static RubyMethod mul = new RubyOneArgMethod() {
		protected RubyValue run(RubyValue receiver, RubyValue arg) {
			long left = RubyAPI.valueToLong(receiver);
			long right = RubyAPI.valueToLong(arg);
			return RubyFixnum.long2Fix(left * right);
		}
	};
	
	public static RubyMethod div = new RubyOneArgMethod() {
		protected RubyValue run(RubyValue receiver, RubyValue arg) {
			long left = RubyAPI.valueToLong(receiver);
			long right = RubyAPI.valueToLong(arg);
			return RubyFixnum.long2Fix(left / right);
		}
	};
	
	public static RubyMethod mod = new RubyOneArgMethod() {
		protected RubyValue run(RubyValue receiver, RubyValue arg) {
			long left = RubyAPI.valueToLong(receiver);
			long right = RubyAPI.valueToLong(arg);
			return RubyFixnum.long2Fix(left % right);
		}
	};
	
	public static RubyMethod abs = new RubyNoArgMethod() {
		protected RubyValue run(RubyValue receiver) {
			long value = RubyAPI.valueToLong(receiver);
			return RubyFixnum.long2Fix(value > 0 ? value : -value);
		}
	};
	
	public static RubyMethod cmp = new RubyOneArgMethod() {
		protected RubyValue run(RubyValue receiver, RubyValue arg) {
			long left = RubyAPI.valueToLong(receiver);
			long right = RubyAPI.valueToLong(arg);
			if (left == right) {
				return RubyFixnum.int2Fix(0);
			} else if (left > right) {
				return RubyFixnum.int2Fix(1);
			} else {
				return RubyFixnum.int2Fix(-1);
			}
			
			// FIXME : arg is not fixnum
		}
	};
	
	public static RubyMethod gt = new RubyOneArgMethod() {
		protected RubyValue run(RubyValue receiver, RubyValue arg) {
			long left = RubyAPI.valueToLong(receiver);
			long right = RubyAPI.valueToLong(arg);
			return RubyAPI.test(left > right);
		}
	};
	
	public static RubyMethod ge = new RubyOneArgMethod() {
		protected RubyValue run(RubyValue receiver, RubyValue arg) {
			long left = RubyAPI.valueToLong(receiver);
			long right = RubyAPI.valueToLong(arg);
			return RubyAPI.test(left >= right);
		}
	};
	
	public static RubyMethod lt = new RubyOneArgMethod() {
		protected RubyValue run(RubyValue receiver, RubyValue arg) {
			long left = RubyAPI.valueToLong(receiver);
			long right = RubyAPI.valueToLong(arg);
			return RubyAPI.test(left < right);
		}
	};
	
	public static RubyMethod le = new RubyOneArgMethod() {
		protected RubyValue run(RubyValue receiver, RubyValue arg) {
			long left = RubyAPI.valueToLong(receiver);
			long right = RubyAPI.valueToLong(arg);
			return RubyAPI.test(left <= right);
		}
	};
	
	public static RubyMethod rev = new RubyNoArgMethod() {
		protected RubyValue run(RubyValue receiver) {
			long value = RubyAPI.valueToLong(receiver);
			return RubyFixnum.long2Fix(~value);
		}
	};
    
    public static RubyMethod and = new RubyOneArgMethod() {
		protected RubyValue run(RubyValue receiver, RubyValue arg) {
			long left = RubyAPI.valueToLong(receiver);
			long right = RubyAPI.valueToLong(arg);
			return RubyFixnum.long2Fix(left & right);
		}
	};
	
	public static RubyMethod or = new RubyOneArgMethod() {
		protected RubyValue run(RubyValue receiver, RubyValue arg) {
			long left = RubyAPI.valueToLong(receiver);
			long right = RubyAPI.valueToLong(arg);
			return RubyFixnum.long2Fix(left | right);
		}
	};
	
	public static RubyMethod xor = new RubyOneArgMethod() {
		protected RubyValue run(RubyValue receiver, RubyValue arg) {
			long left = RubyAPI.valueToLong(receiver);
			long right = RubyAPI.valueToLong(arg);
			return RubyFixnum.long2Fix(left ^ right);
		}
	};
	
	public static RubyMethod zero = new RubyNoArgMethod() {
		protected RubyValue run(RubyValue receiver) {
			long value = RubyAPI.valueToLong(receiver);
			return RubyAPI.test(value == 0);
		}
	};
}
