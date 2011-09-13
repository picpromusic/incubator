Place asm-debug-all-4.0_RC1.jar in libs dir and run
ant clean
ant test

you can download the jar from here:
http://forge.ow2.org/project/showfiles.php?group_id=23

The output should end with:


test:
    [mkdir] Created dir: /home/sebastian/repos/github-incubator/jdk/compatibleFieldAccess/tmp
     [java] <<<OLD>>>
     [java] java.lang.RuntimeException: INIT_OLD
     [java] java.lang.RuntimeException: NEW
     [java] <<<NEW>>>
     [java] Exception in thread "main" java.lang.IllegalAccessError: tried to access field NEW.cause from class TestNew
     [java] 	at TestNew.testIt(TestNew.txt:3)
     [java] 	at Main.main(Main.java:7)
     [java] Java Result: 1
     [java] MOCKINJECT BCI
     [java] <<<OLD>>>
     [java] java.lang.RuntimeException: INIT_OLD
     [java] java.lang.RuntimeException: NEW
     [java] <<<NEW>>>
     [java] java.lang.RuntimeException: INIT_NEW
     [java] java.lang.RuntimeException: NEW
     [java] <<<NEW2>>>
     [java] java.lang.RuntimeException: INIT_NEW
     [java] ***java.lang.IllegalStateException: Not allowed to change

