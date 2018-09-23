package com.merlin;

import java.util.List;


public class MainTest {
	

	 public static String getUnitLikeName(String address)
	  {
	    String addr = address;
	    if ((address != null) && (!"".equals(address))) {
	      if (address.indexOf("社区") > 0) {
	        addr = address.substring(address.indexOf("县") + 1, address.indexOf("社区") + 2);
	      } else if (address.indexOf("村") > 0) {
	        int i = address.indexOf("村");
	        int j = address.indexOf("县");
	        addr = address.substring(address.indexOf("县") + 1, address.indexOf("村") + 1);
	      }
	    }
	    return addr;
	  }

	  public static void main(String[] args)
	  {
		  System.out.println(getUnitLikeName("布拖县木尔乡草木村4组33号"));
	  }


}
