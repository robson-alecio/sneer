package com.mybrick.impl;

import com.mybrick.MyBrick;
import org.apache.commons.lang.*;

public class MyBrickImpl implements MyBrick {
   public void doSomething() {
      System.out.println(ObjectUtils.identityToString(this));
   }
  
}
