package com.functional.interfaces;

public interface InterfaceA {

	String interfaceA="sfds";
	
	void testAbstract();
	
	default void testDefaultIntefaceA()
	{
		System.out.println("testDefaultIntefaceA");
		
	}
	
	static void testStaticInterfaceA()
	{
		System.out.println("testStaticInterfaceA");
	}
	
	
}
