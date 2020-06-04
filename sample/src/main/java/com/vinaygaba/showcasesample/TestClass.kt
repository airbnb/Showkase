package com.vinaygaba.showcasesample

object TestClass {
    fun testMethod() {
        val a = "Hello"
    }
}

//fun topLevelMethod() {
//    val a = "Hello"
//}


// Wrappec inside class

//public final class TestClass {
//    public final void testMethod() {
//        String a = "Hello";
//    }
//}


// Top level

//public final class TestClassKt {
//    public static final void topLevelMethod() {
//        String a = "Hello";
//    }
//}

// Object

//public final class TestClass {
//    public static final TestClass INSTANCE;
//
//    public final void testMethod() {
//        String a = "Hello";
//    }
//
//    private TestClass() {
//    }
//
//    static {
//        TestClass var0 = new TestClass();
//        INSTANCE = var0;
//    }
//}
