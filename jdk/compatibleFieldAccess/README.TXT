Place asm-debug-all-4.2.jar in libs dir and run
ant clean
ant test-original-disable-transform
ant test-original
ant test-new-lib1
ant test-new-lib2

you can download the jar from here:
http://forge.ow2.org/project/showfiles.php?group_id=23

The output of all ant "test" runs should end with:

     [java] incubator.tests.Example1 OK
     [java] incubator.tests.Example2 OK
     [java] incubator.tests.Example3 OK
     [java] incubator.tests.Example4 OK
     [java] incubator.tests.Example5 OK
     [java] incubator.tests.Example6 OK
     [java] incubator.tests.Example7 OK
     [java] incubator.tests.Example8 OK
BUILD SUCCESSFUL

If you want to run all 4 test-runs in one row call
ant test

For a detailed discussion on the 8 examples. See the related sourcecodes in:
testsrc/incubator/tests

Example1: Everything is fine.
Example2: AsymeticAcessorError Detection
Example3: UnambiguousFieldError Detection for get Accessor
Example4: UnambiguousFieldError Detection for set Accessor
Example5: Static non static chane Detection    