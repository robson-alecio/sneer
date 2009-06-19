package sneer.foundation.brickness;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Brick {

	Class<? extends Nature>[] value() default {};

}
